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
 * <p>Tells the player that they did not earn a high score<p>
 * 
 * @author Andrew Rowell
 * @version 1.0
*/

public class NoHighScoreScreen extends Screen{
	final float WORLD_WIDTH;
	final float WORLD_HEIGHT;
	BigFont bigfont;
	CoordinateConverter cc;
	
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
		bigfont = new BigFont(WORLD_WIDTH, WORLD_HEIGHT);
		glGraphics = ((GLGame)game).getGLGraphics();
		cc = new CoordinateConverter(worldwidth,worldheight);
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		noHighScoreBounds = new Rectangle(cc.xcon(40), cc.ycon(256), cc.xcon(240), cc.ycon(192));
		tryAgainBounds = new Rectangle(cc.xcon(8), cc.ycon(96), cc.xcon(304), cc.ycon(128));
		yesBounds = new Rectangle(0, 0, cc.xcon(144), cc.ycon(64));
		noBounds = new Rectangle(cc.xcon(216), 0, cc.xcon(136), cc.ycon(64));
		
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
			batcher.drawLLSprite(0, (int) cc.ycon(j * 320.0f - offset),cc.xcon(320),cc.ycon(320), MainAssets.background);
		}
		
		batcher.drawLLSprite((int) noHighScoreBounds.lowerLeft.x,(int) noHighScoreBounds.lowerLeft.y,
				(int)noHighScoreBounds.width,(int) noHighScoreBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) tryAgainBounds.lowerLeft.x,(int) tryAgainBounds.lowerLeft.y,
				(int)tryAgainBounds.width,(int) tryAgainBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) yesBounds.lowerLeft.x,(int) yesBounds.lowerLeft.y,
				(int)yesBounds.width,(int) yesBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) noBounds.lowerLeft.x,(int) noBounds.lowerLeft.y,
				(int)noBounds.width,(int) noBounds.height, MainAssets.white);
		
		bigfont.drawString(0,0, "YES", batcher);
		bigfont.drawString(cc.xcon(224),0, "NO", batcher);
		
		bigfont.drawString(cc.xcon(112),cc.ycon(384), "NO", batcher);	
		bigfont.drawString(cc.xcon(64),cc.ycon(320), "HIGH", batcher);
		bigfont.drawString(cc.xcon(40),cc.ycon(256), "SCORE", batcher);
		
		bigfont.drawString(cc.xcon(88),cc.ycon(160), "TRY", batcher);
		bigfont.drawString(cc.xcon(16),cc.ycon(96), "AGAIN?", batcher);
				
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
