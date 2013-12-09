package com.andrewjrowell.fly.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.andrewjrowell.fly.assets.MainAssets;
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
	final float WORLD_WIDTH = 320.0f;
	final float WORLD_HEIGHT = 480.0f;
	final static int TEXTX = 48; // Width of bitmap font 
	final static int TEXTY = 64; // Height of bitmap font
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2(); // Stores the position last touched
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle playBounds, highScoresBounds, helpBounds, quitBounds;
	
	// How much we need to shift the background to give the
	// appearance of constantly scrolling grass
	float offset;
			
	public MainScreen(Game game) {
		super(game);
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		quitBounds = new Rectangle(64, 32, 192, 64);
		highScoresBounds = new Rectangle(16, 224, 288, 128);
		helpBounds = new Rectangle(64, 128, 192, 64);
		playBounds = new Rectangle(64, 384, 192, 64);
		
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
					game.setScreen(new GamePlayScreen(game));
				}
				if(OverlapTester.pointInRectangle(highScoresBounds, touchPos)){
					MainAssets.click.play(1.0f);
					game.setScreen(new ViewHighScoreScreen(game));
				}
				if(OverlapTester.pointInRectangle(helpBounds, touchPos)){
					MainAssets.click.play(1.0f);
					game.setScreen(new Help1Screen(game));
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
			batcher.drawLLSprite(0, (int) (j * 320.0f - offset),320,320, MainAssets.background);
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
		batcher.drawLLSprite(64, 32, TEXTX, TEXTY, MainAssets.Q);
		batcher.drawLLSprite(64 + TEXTX, 32, TEXTX, TEXTY, MainAssets.U);
		batcher.drawLLSprite(64 + 2 * TEXTX, 32, TEXTX, TEXTY, MainAssets.I);
		batcher.drawLLSprite(64 + 3 * TEXTX, 32, TEXTX, TEXTY, MainAssets.T);
		
		// Draw Play text
		batcher.drawLLSprite(64, 384, TEXTX, TEXTY, MainAssets.P);
		batcher.drawLLSprite(64 + TEXTX, 384, TEXTX, TEXTY, MainAssets.L);
		batcher.drawLLSprite(64 + 2 * TEXTX, 384, TEXTX, TEXTY, MainAssets.A);
		batcher.drawLLSprite(64 + 3 * TEXTX, 384, TEXTX, TEXTY, MainAssets.Y);
		
		// Draw Help text
		batcher.drawLLSprite(64, 128, TEXTX, TEXTY, MainAssets.H);
		batcher.drawLLSprite(64 + TEXTX, 128, TEXTX, TEXTY, MainAssets.E);
		batcher.drawLLSprite(64 + 2 * TEXTX, 128, TEXTX, TEXTY, MainAssets.L);
		batcher.drawLLSprite(64 + 3 * TEXTX, 128, TEXTX, TEXTY, MainAssets.P);
		
		// Draw High Scores text
		batcher.drawLLSprite(64, 288, TEXTX, TEXTY, MainAssets.H);
		batcher.drawLLSprite(64 + TEXTX, 288, TEXTX, TEXTY, MainAssets.I);
		batcher.drawLLSprite(64 + 2 * TEXTX, 288, TEXTX, TEXTY, MainAssets.G);
		batcher.drawLLSprite(64 + 3 * TEXTX, 288, TEXTX, TEXTY, MainAssets.H);
		batcher.drawLLSprite(16, 224, TEXTX, TEXTY, MainAssets.S);
		batcher.drawLLSprite(16 + TEXTX, 224, TEXTX, TEXTY, MainAssets.C);
		batcher.drawLLSprite(16 + 2 * TEXTX, 224, TEXTX, TEXTY, MainAssets.O);
		batcher.drawLLSprite(16 + 3 * TEXTX, 224, TEXTX, TEXTY, MainAssets.R);
		batcher.drawLLSprite(16 + 4 * TEXTX, 224, TEXTX, TEXTY, MainAssets.E);
		batcher.drawLLSprite(16 + 5 * TEXTX, 224, TEXTX, TEXTY, MainAssets.S);
		
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resume() {
		glGraphics.getGL().glClearColor(1,1,1,1);
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
