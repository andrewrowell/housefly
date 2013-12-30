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
 * <p>Tells the player that they did not earn a high score<p>
 * 
 * @author Andrew Rowell
 * @version 1.0
*/

public class NoHighScoreScreen extends Screen{
	final float WORLD_WIDTH;
	final float WORLD_HEIGHT;
	final static int TEXTX = 48; // Width of bitmap font
	final static int TEXTY = 64; // Height of bitmap font
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2(); // Stores the position last touched
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle noHighScoreBounds, tryAgainBounds, yesBounds, noBounds;
	
	// How much we need to shift the background to give the
	// appearance of constantly scrolling grass
	float offset;
				
	public NoHighScoreScreen(Game game, float worldwidth, float worldheight) {
		super(game);
		WORLD_WIDTH = worldwidth;
		WORLD_HEIGHT = worldheight;
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		noHighScoreBounds = new Rectangle(40, 256, 240, 192);
		tryAgainBounds = new Rectangle(8, 96, 304, 128);
		yesBounds = new Rectangle(0, 0, 144, 64);
		noBounds = new Rectangle(216, 0, 136, 64);
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		MainAssets.minorchord.play(1.0f);
	}
	
	
	/**
	 * <p>Update various elements of the NoHighScoreScreen</p>
	 * 
	 * @param deltaTime time since last update()
	 */
	@Override
	public void update(float deltaTime) {
		offset += 32 * deltaTime;
		if(offset >= 320){
			offset = 0;
		}

		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			
			camera.touchToWorld(touchPos.set(event.x, event.y));
						
			if(event.type == TouchEvent.TOUCH_UP){
				if(OverlapTester.pointInRectangle(noBounds, touchPos)){
					MainAssets.click.play(1.0f);
					game.setScreen(new MainScreen(game, WORLD_WIDTH, WORLD_HEIGHT));
				}
				if(OverlapTester.pointInRectangle(yesBounds, touchPos)){
					MainAssets.click.play(1.0f);
					game.setScreen(new GamePlayScreen(game, WORLD_WIDTH, WORLD_HEIGHT));
				}
			}
		}
	}
	
	/**
	 * <p>Render various elements of NoHighScoreScreen</p>
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
		
		batcher.drawLLSprite((int) noHighScoreBounds.lowerLeft.x,(int) noHighScoreBounds.lowerLeft.y,
				(int)noHighScoreBounds.width,(int) noHighScoreBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) tryAgainBounds.lowerLeft.x,(int) tryAgainBounds.lowerLeft.y,
				(int)tryAgainBounds.width,(int) tryAgainBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) yesBounds.lowerLeft.x,(int) yesBounds.lowerLeft.y,
				(int)yesBounds.width,(int) yesBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) noBounds.lowerLeft.x,(int) noBounds.lowerLeft.y,
				(int)noBounds.width,(int) noBounds.height, MainAssets.white);
		
		batcher.drawLLSprite(0, 0, TEXTX, TEXTY, MainAssets.Y);
		batcher.drawLLSprite(0 + TEXTX, 0, TEXTX, TEXTY, MainAssets.E);
		batcher.drawLLSprite(0 + 2 * TEXTX, 0, TEXTX, TEXTY, MainAssets.S);
		
		batcher.drawLLSprite(224, 0, TEXTX, TEXTY, MainAssets.N);
		batcher.drawLLSprite(224 + TEXTX, 0, TEXTX, TEXTY, MainAssets.O);
		
		batcher.drawLLSprite(112, 384, TEXTX, TEXTY, MainAssets.N);
		batcher.drawLLSprite(112 + TEXTX, 384, TEXTX, TEXTY, MainAssets.O);
		batcher.drawLLSprite(64, 320, TEXTX, TEXTY, MainAssets.H);
		batcher.drawLLSprite(64 + TEXTX, 320, TEXTX, TEXTY, MainAssets.I);
		batcher.drawLLSprite(64 + 2 * TEXTX, 320, TEXTX, TEXTY, MainAssets.G);
		batcher.drawLLSprite(64 + 3 * TEXTX, 320, TEXTX, TEXTY, MainAssets.H);
		batcher.drawLLSprite(40, 256, TEXTX, TEXTY, MainAssets.S);
		batcher.drawLLSprite(40 + TEXTX, 256, TEXTX, TEXTY, MainAssets.C);
		batcher.drawLLSprite(40 + 2 * TEXTX, 256, TEXTX, TEXTY, MainAssets.O);
		batcher.drawLLSprite(40 + 3 * TEXTX, 256, TEXTX, TEXTY, MainAssets.R);
		batcher.drawLLSprite(40 + 4 * TEXTX, 256, TEXTX, TEXTY, MainAssets.E);
		
		batcher.drawLLSprite(88, 160, TEXTX, TEXTY, MainAssets.T);
		batcher.drawLLSprite(88 + TEXTX, 160, TEXTX, TEXTY, MainAssets.R);
		batcher.drawLLSprite(88 + 2 * TEXTX, 160, TEXTX, TEXTY, MainAssets.Y);
		batcher.drawLLSprite(16, 96, TEXTX, TEXTY, MainAssets.A);
		batcher.drawLLSprite(16 + TEXTX, 96, TEXTX, TEXTY, MainAssets.G);
		batcher.drawLLSprite(16 + 2 * TEXTX, 96, TEXTX, TEXTY, MainAssets.A);
		batcher.drawLLSprite(16 + 3 * TEXTX, 96, TEXTX, TEXTY, MainAssets.I);
		batcher.drawLLSprite(16 + 4 * TEXTX, 96, TEXTX, TEXTY, MainAssets.N);
		batcher.drawLLSprite(16 + 5 * TEXTX, 96, TEXTX, TEXTY, MainAssets.QUESTION_MARK);
				
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
