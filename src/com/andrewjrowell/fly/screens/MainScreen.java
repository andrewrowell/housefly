package com.andrewjrowell.fly.screens;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.andrewjrowell.fly.Assets;
import com.andrewjrowell.framework.GLGraphics;
import com.andrewjrowell.framework.gamedev2d.DynamicGameObject;
import com.andrewjrowell.framework.gamedev2d.GameObject;
import com.andrewjrowell.framework.gl.Camera2D;
import com.andrewjrowell.framework.gl.SpatialHashGrid;
import com.andrewjrowell.framework.gl.SpriteBatcher;
import com.andrewjrowell.framework.gl.Texture;
import com.andrewjrowell.framework.gl.Vertices;
import com.andrewjrowell.framework.interfaces.Audio;
import com.andrewjrowell.framework.interfaces.Game;
import com.andrewjrowell.framework.interfaces.Sound;
import com.andrewjrowell.framework.interfaces.Input.TouchEvent;
import com.andrewjrowell.framework.interfaces.Screen;
import com.andrewjrowell.framework.math.OverlapTester;
import com.andrewjrowell.framework.math.Rectangle;
import com.andrewjrowell.framework.math.Vector2;
import com.andrewjrowell.framework.GLGame;

public class MainScreen extends Screen{
	final float WORLD_WIDTH = 320.0f;
	final float WORLD_HEIGHT = 480.0f;
	final static int TEXTX = 48;
	final static int TEXTY = 64;
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2();
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle playBounds, highScoresBounds, helpBounds, quitBounds;
	
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
	@Override
	public void update(float deltaTime) {
		Assets.menumusic.play();
		Assets.menumusic.setLooping(true);
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
				if(OverlapTester.pointInRectangle(playBounds, touchPos)){
					Assets.click.play(1.0f);
					Assets.menumusic.stop();
					game.setScreen(new GamePlayScreen(game));
				}
				if(OverlapTester.pointInRectangle(highScoresBounds, touchPos)){
					Assets.click.play(1.0f);
					game.setScreen(new ViewHighScoreScreen(game));
				}
				if(OverlapTester.pointInRectangle(helpBounds, touchPos)){
					Assets.click.play(1.0f);
					game.setScreen(new Help1Screen(game));
				}
				if(OverlapTester.pointInRectangle(quitBounds, touchPos)){
					Assets.click.play(1.0f);
					Assets.menumusic.stop();
					System.exit(0);
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
		
		batcher.drawLLSprite((int) playBounds.lowerLeft.x,(int) playBounds.lowerLeft.y,
				(int)playBounds.width,(int) playBounds.height, Assets.white);
		batcher.drawLLSprite((int) highScoresBounds.lowerLeft.x,(int) highScoresBounds.lowerLeft.y,
				(int)highScoresBounds.width,(int) highScoresBounds.height, Assets.white);
		batcher.drawLLSprite((int) helpBounds.lowerLeft.x,(int) helpBounds.lowerLeft.y,
				(int)helpBounds.width,(int) helpBounds.height, Assets.white);
		batcher.drawLLSprite((int) quitBounds.lowerLeft.x,(int) quitBounds.lowerLeft.y,
				(int)quitBounds.width,(int) quitBounds.height, Assets.white);
		
		batcher.drawLLSprite(64, 32, TEXTX, TEXTY, Assets.Q);
		batcher.drawLLSprite(64 + TEXTX, 32, TEXTX, TEXTY, Assets.U);
		batcher.drawLLSprite(64 + 2 * TEXTX, 32, TEXTX, TEXTY, Assets.I);
		batcher.drawLLSprite(64 + 3 * TEXTX, 32, TEXTX, TEXTY, Assets.T);
		
		batcher.drawLLSprite(64, 384, TEXTX, TEXTY, Assets.P);
		batcher.drawLLSprite(64 + TEXTX, 384, TEXTX, TEXTY, Assets.L);
		batcher.drawLLSprite(64 + 2 * TEXTX, 384, TEXTX, TEXTY, Assets.A);
		batcher.drawLLSprite(64 + 3 * TEXTX, 384, TEXTX, TEXTY, Assets.Y);
		
		batcher.drawLLSprite(64, 128, TEXTX, TEXTY, Assets.H);
		batcher.drawLLSprite(64 + TEXTX, 128, TEXTX, TEXTY, Assets.E);
		batcher.drawLLSprite(64 + 2 * TEXTX, 128, TEXTX, TEXTY, Assets.L);
		batcher.drawLLSprite(64 + 3 * TEXTX, 128, TEXTX, TEXTY, Assets.P);
		
		batcher.drawLLSprite(64, 288, TEXTX, TEXTY, Assets.H);
		batcher.drawLLSprite(64 + TEXTX, 288, TEXTX, TEXTY, Assets.I);
		batcher.drawLLSprite(64 + 2 * TEXTX, 288, TEXTX, TEXTY, Assets.G);
		batcher.drawLLSprite(64 + 3 * TEXTX, 288, TEXTX, TEXTY, Assets.H);
		batcher.drawLLSprite(16, 224, TEXTX, TEXTY, Assets.S);
		batcher.drawLLSprite(16 + TEXTX, 224, TEXTX, TEXTY, Assets.C);
		batcher.drawLLSprite(16 + 2 * TEXTX, 224, TEXTX, TEXTY, Assets.O);
		batcher.drawLLSprite(16 + 3 * TEXTX, 224, TEXTX, TEXTY, Assets.R);
		batcher.drawLLSprite(16 + 4 * TEXTX, 224, TEXTX, TEXTY, Assets.E);
		batcher.drawLLSprite(16 + 5 * TEXTX, 224, TEXTX, TEXTY, Assets.S);
		
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
