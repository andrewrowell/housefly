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

public class Help1Screen extends Screen{
	final float WORLD_WIDTH = 320.0f;
	final float WORLD_HEIGHT = 480.0f;
	final static int TEXTX = 48;
	final static int TEXTY = 64;
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2();
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle nextBounds, textBounds;
	
	float offset;
	
	boolean tiltright = true;
	
	float fly_x;
			
	public Help1Screen(Game game) {
		super(game);
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		nextBounds = new Rectangle(96, 0, 128, 64);
		textBounds = new Rectangle(64, 384, 192, 64);
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		fly_x = WORLD_WIDTH / 2;
	}
	@Override
	public void update(float deltaTime) {
		offset += 32 * deltaTime;
		if(offset >= 320){
			offset = 0;
		}
		
		if(fly_x >= WORLD_WIDTH && tiltright == true){
			fly_x = WORLD_WIDTH - 1;
			tiltright = false;
			Assets.tiltleft.play(1.0f);
		}
		if(fly_x <= 0 && tiltright == false){
			fly_x = 1;
			tiltright = true;
			Assets.tiltright.play(1.0f);
		}
		if(tiltright){
			fly_x += 64 * deltaTime;
		} else {
			fly_x -= 64 * deltaTime;
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
					game.setScreen(new Help2Screen(game));
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
		
		batcher.drawLLSprite((int) nextBounds.lowerLeft.x,(int) nextBounds.lowerLeft.y,
				(int)nextBounds.width,(int) nextBounds.height, Assets.white);
		batcher.drawLLSprite((int) textBounds.lowerLeft.x,(int) textBounds.lowerLeft.y,
				(int)textBounds.width,(int) textBounds.height, Assets.white);
		
		batcher.drawLLSprite(64, 384, TEXTX, TEXTY, Assets.T);
		batcher.drawLLSprite(64 + TEXTX, 384, TEXTX, TEXTY, Assets.I);
		batcher.drawLLSprite(64 + 2 * TEXTX, 384, TEXTX, TEXTY, Assets.L);
		batcher.drawLLSprite(64 + 3 * TEXTX, 384, TEXTX, TEXTY, Assets.T);
		
		batcher.drawLLSprite(96, 0, 128, 64, Assets.arrow);
		
		if(tiltright == true){
			batcher.drawSprite(160, 256, 128, 192, 350, Assets.phone);
		} else {
			batcher.drawSprite(160, 256, 128, 192, 10, Assets.phone);
		}
		
		batcher.drawSprite(fly_x, 96, 64, 64, Assets.fly);
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
