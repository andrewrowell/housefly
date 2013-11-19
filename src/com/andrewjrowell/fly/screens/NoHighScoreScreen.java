package com.andrewjrowell.fly.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.andrewjrowell.fly.Assets;
import com.andrewjrowell.framework.GLGraphics;
import com.andrewjrowell.framework.gl.Camera2D;
import com.andrewjrowell.framework.gl.SpriteBatcher;
import com.andrewjrowell.framework.interfaces.Game;
import com.andrewjrowell.framework.interfaces.Input.TouchEvent;
import com.andrewjrowell.framework.interfaces.Screen;
import com.andrewjrowell.framework.math.OverlapTester;
import com.andrewjrowell.framework.math.Rectangle;
import com.andrewjrowell.framework.math.Vector2;
import com.andrewjrowell.framework.GLGame;

public class NoHighScoreScreen extends Screen{
	final float WORLD_WIDTH = 320.0f;
	final float WORLD_HEIGHT = 480.0f;
	final static int TEXTX = 48;
	final static int TEXTY = 64;
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2();
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle noHighScoreBounds, tryAgainBounds, yesBounds, noBounds;
	
	float offset;
	
	boolean tiltright = true;
				
	public NoHighScoreScreen(Game game) {
		super(game);
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		noHighScoreBounds = new Rectangle(40, 256, 240, 192);
		tryAgainBounds = new Rectangle(8, 96, 304, 128);
		yesBounds = new Rectangle(0, 0, 144, 64);
		noBounds = new Rectangle(216, 0, 136, 64);
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		Assets.minorchord.play(1.0f);
	}
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
					Assets.click.play(1.0f);
					game.setScreen(new MainScreen(game));
				}
				if(OverlapTester.pointInRectangle(yesBounds, touchPos)){
					Assets.click.play(1.0f);
					game.setScreen(new GamePlayScreen(game));
				}
			}
		}
	}
	
	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.setViewportAndMatrices();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		batcher.beginBatch(Assets.imagemap);
		
		for(int j = 0; j < 4; j++){
			batcher.drawLLSprite(0, (int) (j * 320.0f - offset),320,320, Assets.background);
		}
		
		batcher.drawLLSprite((int) noHighScoreBounds.lowerLeft.x,(int) noHighScoreBounds.lowerLeft.y,
				(int)noHighScoreBounds.width,(int) noHighScoreBounds.height, Assets.white);
		batcher.drawLLSprite((int) tryAgainBounds.lowerLeft.x,(int) tryAgainBounds.lowerLeft.y,
				(int)tryAgainBounds.width,(int) tryAgainBounds.height, Assets.white);
		batcher.drawLLSprite((int) yesBounds.lowerLeft.x,(int) yesBounds.lowerLeft.y,
				(int)yesBounds.width,(int) yesBounds.height, Assets.white);
		batcher.drawLLSprite((int) noBounds.lowerLeft.x,(int) noBounds.lowerLeft.y,
				(int)noBounds.width,(int) noBounds.height, Assets.white);
		
		batcher.drawLLSprite(0, 0, TEXTX, TEXTY, Assets.Y);
		batcher.drawLLSprite(0 + TEXTX, 0, TEXTX, TEXTY, Assets.E);
		batcher.drawLLSprite(0 + 2 * TEXTX, 0, TEXTX, TEXTY, Assets.S);
		
		batcher.drawLLSprite(224, 0, TEXTX, TEXTY, Assets.N);
		batcher.drawLLSprite(224 + TEXTX, 0, TEXTX, TEXTY, Assets.O);
		
		batcher.drawLLSprite(112, 384, TEXTX, TEXTY, Assets.N);
		batcher.drawLLSprite(112 + TEXTX, 384, TEXTX, TEXTY, Assets.O);
		batcher.drawLLSprite(64, 320, TEXTX, TEXTY, Assets.H);
		batcher.drawLLSprite(64 + TEXTX, 320, TEXTX, TEXTY, Assets.I);
		batcher.drawLLSprite(64 + 2 * TEXTX, 320, TEXTX, TEXTY, Assets.G);
		batcher.drawLLSprite(64 + 3 * TEXTX, 320, TEXTX, TEXTY, Assets.H);
		batcher.drawLLSprite(40, 256, TEXTX, TEXTY, Assets.S);
		batcher.drawLLSprite(40 + TEXTX, 256, TEXTX, TEXTY, Assets.C);
		batcher.drawLLSprite(40 + 2 * TEXTX, 256, TEXTX, TEXTY, Assets.O);
		batcher.drawLLSprite(40 + 3 * TEXTX, 256, TEXTX, TEXTY, Assets.R);
		batcher.drawLLSprite(40 + 4 * TEXTX, 256, TEXTX, TEXTY, Assets.E);
		
		batcher.drawLLSprite(88, 160, TEXTX, TEXTY, Assets.T);
		batcher.drawLLSprite(88 + TEXTX, 160, TEXTX, TEXTY, Assets.R);
		batcher.drawLLSprite(88 + 2 * TEXTX, 160, TEXTX, TEXTY, Assets.Y);
		batcher.drawLLSprite(16, 96, TEXTX, TEXTY, Assets.A);
		batcher.drawLLSprite(16 + TEXTX, 96, TEXTX, TEXTY, Assets.G);
		batcher.drawLLSprite(16 + 2 * TEXTX, 96, TEXTX, TEXTY, Assets.A);
		batcher.drawLLSprite(16 + 3 * TEXTX, 96, TEXTX, TEXTY, Assets.I);
		batcher.drawLLSprite(16 + 4 * TEXTX, 96, TEXTX, TEXTY, Assets.N);
		batcher.drawLLSprite(16 + 5 * TEXTX, 96, TEXTX, TEXTY, Assets.QUESTION_MARK);
				
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
