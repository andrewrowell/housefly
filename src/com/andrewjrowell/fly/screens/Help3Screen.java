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

public class Help3Screen extends Screen{
	final float WORLD_WIDTH = 320.0f;
	final float WORLD_HEIGHT = 480.0f;
	final static int TEXTX = 48;
	final static int TEXTY = 64;
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2();
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle nextBounds, textBounds, textBounds2;
	
	float offset, food_y, resetcountdown;
			
	public Help3Screen(Game game) {
		super(game);
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		nextBounds = new Rectangle(96, 0, 128, 64);
		textBounds = new Rectangle(88, 384, 144, 64);
		textBounds2 = new Rectangle(64, 96, 192, 64);
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		food_y = 320;
		resetcountdown = 3;
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
				if(OverlapTester.pointInRectangle(nextBounds, touchPos)){
					Assets.click.play(1.0f);
					game.setScreen(new MainScreen(game));
				}
			}
		}
		food_y -= 32 * deltaTime;
		if(food_y <= 192){
			food_y = 192;
			resetcountdown -= deltaTime;
		}
		if(resetcountdown <= 0){
			food_y = 320;
			resetcountdown = 3;
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
		
		batcher.drawLLSprite((int) nextBounds.lowerLeft.x,(int) nextBounds.lowerLeft.y,
				(int)nextBounds.width,(int) nextBounds.height, Assets.white);
		batcher.drawLLSprite((int) textBounds.lowerLeft.x,(int) textBounds.lowerLeft.y,
				(int)textBounds.width,(int) textBounds.height, Assets.white);
		
		batcher.drawLLSprite(88, 384, TEXTX, TEXTY, Assets.E);
		batcher.drawLLSprite(88 + TEXTX, 384, TEXTX, TEXTY, Assets.A);
		batcher.drawLLSprite(88 + 2 * TEXTX, 384, TEXTX, TEXTY, Assets.T);
		
		if(food_y > 192 + 32){
			batcher.drawSprite(160,food_y,32,32,Assets.rotten1);
			batcher.drawSprite(160,192,32,32,Assets.fly);
		} else {
			batcher.drawLLSprite((int) textBounds2.lowerLeft.x,(int) textBounds2.lowerLeft.y,
					(int)textBounds2.width,(int) textBounds2.height, Assets.white);
			batcher.drawLLSprite(64, 96, TEXTX, TEXTY, Assets.G);
			batcher.drawLLSprite(64 + TEXTX, 96, TEXTX, TEXTY, Assets.R);
			batcher.drawLLSprite(64 + 2 * TEXTX, 96, TEXTX, TEXTY, Assets.O);
			batcher.drawLLSprite(64 + 3 * TEXTX, 96, TEXTX, TEXTY, Assets.W);
			batcher.drawSprite(160,192,32 + (32 * (3 - resetcountdown) / 3),
					32 + (32 * (3 - resetcountdown) / 3),Assets.fly);
		}
		//batcher.drawSprite(160,192,64,64,Assets.fly);
		
		batcher.drawLLSprite(96, 0, 128, 64, Assets.arrow);
		
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
