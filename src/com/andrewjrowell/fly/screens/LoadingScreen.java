package com.andrewjrowell.fly.screens;

import javax.microedition.khronos.opengles.GL10;

import com.andrewjrowell.fly.assets.MainAssets;
import com.andrewjrowell.fly.assets.PreAssets;
import com.andrewjrowell.framework.Game;
import com.andrewjrowell.framework.Screen;
import com.andrewjrowell.framework.gl.Camera2D;
import com.andrewjrowell.framework.gl.GLGame;
import com.andrewjrowell.framework.gl.GLGraphics;
import com.andrewjrowell.framework.gl.SpriteBatcher;
import com.andrewjrowell.framework.math.Rectangle;
import com.andrewjrowell.framework.math.Vector2;

/**
 * <p> Displays a splashscreen from a special Pre-Asset file that
 * is small enough to load almost instantly, while the rest of the
 * game's assets are loaded.<p>
 * 
 * <p> Added on to the framework design from Beginning Android
 * Games by Mario Zechner. </p>
 * 
 * @author Andrew Rowell
 * @version 1.0
*/

public class LoadingScreen extends Screen{
	final float WORLD_WIDTH;
	final float WORLD_HEIGHT;
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2();
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle nextBounds, textBounds;
	
	boolean loadingStarted = false;
			
	/**
	 * 
	 * @param game
	 * @param worldwidth width of game world in pixels
	 * @param worldheight height of game world in pixels
	 */
	public LoadingScreen(Game game, float worldwidth, float worldheight) {
		super(game);
		WORLD_WIDTH = worldwidth;
		WORLD_HEIGHT = worldheight;
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		
		glGraphics.getGL().glClearColor(1,1,1,1);
	}
	
	
	/**
	 * <p>Waits for main assets to load before
	 * changing to MainScreen</p>
	 * 
	 * @param deltaTime time since last update()
	 */
	@Override
	public void update(float deltaTime) {
		if(!loadingStarted){
			new Thread(){
				public void run() { MainAssets.load((GLGame) game); }
			}.start();
			loadingStarted = true;
		}
		game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		if(MainAssets.isLoaded()){
			MainAssets.reload();
			game.setScreen(new MainScreen(game, WORLD_WIDTH, WORLD_HEIGHT));
		}
	}
	
	
	/**
	 * <p>Renders the splashscreen from PreAssets</p>
	 * 
	 * @param deltaTime time since last present
	 */
	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.setViewportAndMatrices();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		batcher.beginBatch(PreAssets.imagemap);
		
		batcher.drawLLSprite(0, 0, 320,480, PreAssets.background);
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}
	@Override
	public void pause() {}
	@Override
	public void resume() {
		glGraphics.getGL().glClearColor(1,1,1,1);
	}
	@Override
	public void dispose() {}
}
