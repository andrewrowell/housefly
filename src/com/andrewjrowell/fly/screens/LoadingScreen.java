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

public class LoadingScreen extends Screen{
	final float WORLD_WIDTH = 320.0f;
	final float WORLD_HEIGHT = 480.0f;
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2();
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle nextBounds, textBounds;
	
	float offset;
	
	
	float fly_x;
	
	boolean loadingStarted = false;
			
	public LoadingScreen(Game game) {
		super(game);
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		fly_x = WORLD_WIDTH / 2;
	}
	@Override
	public void update(float deltaTime) {
		if(!loadingStarted){
			new Thread(){
				public void run() { MainAssets.load((GLGame) game); }
			}.start();
			loadingStarted = true;
		}
		offset += 32 * deltaTime;
		if(offset >= 320){
			offset = 0;
		}
		game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		if(MainAssets.isLoaded()){
			MainAssets.reload();
			game.setScreen(new MainScreen(game));
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
