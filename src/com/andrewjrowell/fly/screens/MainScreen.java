package com.andrewjrowell.fly.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.andrewjrowell.fly.assets.MainAssets;
import com.andrewjrowell.fly.bitmapfonts.BigFont;
import com.andrewjrowell.framework.CoordinateConverter;
import com.andrewjrowell.framework.Game;
import com.andrewjrowell.framework.Screen;
import com.andrewjrowell.framework.gl.Camera2D;
import com.andrewjrowell.framework.gl.GLGame;
import com.andrewjrowell.framework.gl.GLGraphics;
import com.andrewjrowell.framework.gl.SpriteBatcher;
import com.andrewjrowell.framework.input.Input.TouchEvent;
import com.andrewjrowell.framework.math.OverlapTester;
import com.andrewjrowell.framework.math.Rectangle;
import com.andrewjrowell.framework.math.Vector2;

/**
 * <p>Main menu screen<p>
 * 
 * <p>Displays four options: Play, View High Scores, Help, or Quit</p>
 * 
 * <p>Player can start a game, look at the top scores, open
 * the help screens, or quit the game from here.</p>
 * 
 * @author Andrew Rowell
 * @version 1.0
*/

public class MainScreen extends Screen{
	final float WORLD_WIDTH;
	final float WORLD_HEIGHT;
	BigFont bigfont;
	CoordinateConverter cc;
	
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2(); // Stores the position last touched
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle playBounds, highScoresBounds, helpBounds, quitBounds;
	
	// How much we need to shift the background to give the
	// appearance of constantly scrolling grass
	float offset;
			
	public MainScreen(Game game, float worldwidth, float worldheight) {
		super(game);
		WORLD_WIDTH = worldwidth;
		WORLD_HEIGHT = worldheight;
		bigfont = new BigFont(WORLD_WIDTH, WORLD_HEIGHT);
		cc = new CoordinateConverter(worldwidth, worldheight);
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		
		quitBounds = new Rectangle(cc.xcon(64), cc.ycon(32),
				cc.xcon(192),cc.ycon(64));
		highScoresBounds = new Rectangle(cc.xcon(16), cc.ycon(224),
				cc.xcon(288), cc.ycon(128));
		helpBounds = new Rectangle(cc.xcon(64),cc.ycon(128), 
				cc.xcon(192),cc.ycon(64));
		playBounds = new Rectangle(cc.xcon(64),cc.ycon(384),
				cc.xcon(192),cc.ycon(64));
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
	}
	
	/**
	 * <p>Update status of various elements of the MainScreen</p>
	 * 
	 * @param deltaTime time since last update()
	 */
	@Override
	public void update(float deltaTime) {
		MainAssets.menumusic.play();
		MainAssets.menumusic.setLooping(true);
		
		// Shift background
		offset += 32 * deltaTime;
		if(offset >= 320){
			offset = 0;
		}
		
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		// See if the player touched a button
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			
			camera.touchToWorld(touchPos.set(event.x, event.y));
						
			if(event.type == TouchEvent.TOUCH_UP){
				if(OverlapTester.pointInRectangle(playBounds, touchPos)){
					MainAssets.click.play(1.0f);
					MainAssets.menumusic.stop();
					game.setScreen(new GamePlayScreen(game, WORLD_WIDTH, WORLD_HEIGHT));
				}
				if(OverlapTester.pointInRectangle(highScoresBounds, touchPos)){
					MainAssets.click.play(1.0f);
					game.setScreen(new ViewHighScoreScreen(game, WORLD_WIDTH, WORLD_HEIGHT));
				}
				if(OverlapTester.pointInRectangle(helpBounds, touchPos)){
					MainAssets.click.play(1.0f);
					game.setScreen(new Help1Screen(game, WORLD_WIDTH, WORLD_HEIGHT));
				}
				if(OverlapTester.pointInRectangle(quitBounds, touchPos)){
					MainAssets.click.play(1.0f);
					MainAssets.menumusic.stop();
					System.exit(0);
				}
			}
		}
	}
	
	
	/**
	 * <p>Render various elements of MainScreen</p> 
	 * 
	 * @param deltaTime time since last present()
	 */
	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.setViewportAndMatrices();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		batcher.beginBatch(MainAssets.imagemap);
		
		for(int j = 0; j < 4; j++){
			batcher.drawLLSprite(0, (int) cc.ycon(j * 320.0f - offset),
					(int)WORLD_WIDTH,
					cc.ycon(320), MainAssets.background);
		}
		
		// Draw button backgrounds
		batcher.drawLLSprite((int) playBounds.lowerLeft.x,(int) playBounds.lowerLeft.y,
				(int)playBounds.width,(int) playBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) highScoresBounds.lowerLeft.x,(int) highScoresBounds.lowerLeft.y,
				(int)highScoresBounds.width,(int) highScoresBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) helpBounds.lowerLeft.x,(int) helpBounds.lowerLeft.y,
				(int)helpBounds.width,(int) helpBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) quitBounds.lowerLeft.x,(int) quitBounds.lowerLeft.y,
				(int)quitBounds.width,(int) quitBounds.height, MainAssets.white);
		
		// Draw Quit text
		bigfont.drawString(cc.xcon(64),
				cc.ycon(32), "QUIT", batcher);
		
		// Draw Play text
		bigfont.drawString(cc.xcon(64),
				cc.ycon(384), "PLAY", batcher);
		
		// Draw Help text
		bigfont.drawString(cc.xcon(64),
				cc.ycon(128), "HELP", batcher);
		
		// Draw High Scores text
		bigfont.drawString(cc.xcon(64),
				cc.ycon(288), "HIGH", batcher);
		bigfont.drawString(cc.xcon(16),
				cc.ycon(224), "SCORES", batcher);
		
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
