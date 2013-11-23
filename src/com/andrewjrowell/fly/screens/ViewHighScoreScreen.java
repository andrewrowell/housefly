package com.andrewjrowell.fly.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import com.andrewjrowell.fly.assets.MainAssets;
import com.andrewjrowell.framework.Game;
import com.andrewjrowell.framework.Screen;
import com.andrewjrowell.framework.diskio.HighScores;
import com.andrewjrowell.framework.gl.Camera2D;
import com.andrewjrowell.framework.gl.GLGame;
import com.andrewjrowell.framework.gl.GLGraphics;
import com.andrewjrowell.framework.gl.SpriteBatcher;
import com.andrewjrowell.framework.input.Input.TouchEvent;
import com.andrewjrowell.framework.math.OverlapTester;
import com.andrewjrowell.framework.math.Rectangle;
import com.andrewjrowell.framework.math.Vector2;

public class ViewHighScoreScreen extends Screen{
	final float WORLD_WIDTH = 320.0f;
	final float WORLD_HEIGHT = 480.0f;
	final static int TEXTX = 48;
	final static int TEXTY = 64;
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2();
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle nextBounds, textBounds, scoreBounds, nameBounds;
	
	float offset;
	
	boolean tiltright = true;
	
	float fly_x;
			
	public ViewHighScoreScreen(Game game) {
		super(game);
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		nextBounds = new Rectangle(96, 0, 128, 64);
		textBounds = new Rectangle(16, 320, 288, 128);
		scoreBounds = new Rectangle(32,128, 160, 160);
		nameBounds = new Rectangle(192, 128, 96, 160);
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		HighScores.load(game.getFileIO());
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
					MainAssets.click.play(1.0f);
					game.setScreen(new MainScreen(game));
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
		batcher.beginBatch(MainAssets.imagemap);
		
		for(int j = 0; j < 4; j++){
			batcher.drawLLSprite(0, (int) (j * 320.0f - offset),320,320, MainAssets.background);
		}
		
		batcher.drawLLSprite((int) nextBounds.lowerLeft.x,(int) nextBounds.lowerLeft.y,
				(int)nextBounds.width,(int) nextBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) textBounds.lowerLeft.x,(int) textBounds.lowerLeft.y,
				(int)textBounds.width,(int) textBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) scoreBounds.lowerLeft.x,(int) scoreBounds.lowerLeft.y,
				(int)scoreBounds.width,(int) scoreBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) nameBounds.lowerLeft.x,(int) nameBounds.lowerLeft.y,
				(int)nameBounds.width,(int) nameBounds.height, MainAssets.tallred);
		
		batcher.drawLLSprite(64, 384, TEXTX, TEXTY, MainAssets.H);
		batcher.drawLLSprite(64 + TEXTX, 384, TEXTX, TEXTY, MainAssets.I);
		batcher.drawLLSprite(64 + 2 * TEXTX, 384, TEXTX, TEXTY, MainAssets.G);
		batcher.drawLLSprite(64 + 3 * TEXTX, 384, TEXTX, TEXTY, MainAssets.H);
		batcher.drawLLSprite(16, 320, TEXTX, TEXTY, MainAssets.S);
		batcher.drawLLSprite(16 + TEXTX, 320, TEXTX, TEXTY, MainAssets.C);
		batcher.drawLLSprite(16 + 2 * TEXTX, 320, TEXTX, TEXTY, MainAssets.O);
		batcher.drawLLSprite(16 + 3 * TEXTX, 320, TEXTX, TEXTY, MainAssets.R);
		batcher.drawLLSprite(16 + 4 * TEXTX, 320, TEXTX, TEXTY, MainAssets.E);
		batcher.drawLLSprite(16 + 5 * TEXTX, 320, TEXTX, TEXTY, MainAssets.S);
		
		for(int i = 0; i < 5; i++){
			drawScore(i,32,32 * (8-i));
		}
		
		batcher.drawLLSprite(96, 0, 128, 64, MainAssets.arrow);
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}
	

	private void drawScore(int scoreindex, int x, int y){
		int tscore = HighScores.scores[scoreindex];
		int digit1 = (int) Math.floor(tscore / 10000);
		tscore -= (digit1 * 10000);
		int digit2 = (int) Math.floor(tscore / 1000);
		tscore -= (digit2 * 1000);
		int digit3 = (int) Math.floor(tscore / 100);
		tscore -= (digit3 * 100);
		int digit4 = (int) Math.floor(tscore / 10);
		tscore -= (digit4 * 10);
		int digit5 = (int) Math.floor(tscore / 1);
		tscore -= (digit5 * 1);
		drawDigit(x,y,digit1);
		drawDigit(x + 32,y,digit2);
		drawDigit(x + 64,y,digit3);
		drawDigit(x + 96,y,digit4);
		drawDigit(x + 128,y,digit5);
		drawName(x + 160,y,HighScores.names[scoreindex]);
		//draw name
	}
	
	private void drawDigit(int x, int y, int digit){
		switch(digit){
		case 1: batcher.drawLLSprite(x, y, 32, 32, MainAssets.one); break;
		case 2: batcher.drawLLSprite(x, y, 32, 32, MainAssets.two); break;
		case 3: batcher.drawLLSprite(x, y, 32, 32, MainAssets.three); break;
		case 4: batcher.drawLLSprite(x, y, 32, 32, MainAssets.four); break;
		case 5: batcher.drawLLSprite(x, y, 32, 32, MainAssets.five); break;
		case 6: batcher.drawLLSprite(x, y, 32, 32, MainAssets.six); break;
		case 7: batcher.drawLLSprite(x, y, 32, 32, MainAssets.seven); break;
		case 8: batcher.drawLLSprite(x, y, 32, 32, MainAssets.eight); break;
		case 9: batcher.drawLLSprite(x, y, 32, 32, MainAssets.nine); break;
		case 0: batcher.drawLLSprite(x, y, 32, 32, MainAssets.zero); break;
		}
	}
	
	private void drawName(int x, int y, String name){
		drawChar(x, y, name.charAt(0));
		drawChar(x + 32, y, name.charAt(1));
		drawChar(x + 64, y, name.charAt(2));
	}
	
	private void drawChar(int x, int y, char c){
		switch(c){
		case 'A': batcher.drawLLSprite(x, y, 32, 32, MainAssets.a); break;
		case 'B': batcher.drawLLSprite(x, y, 32, 32, MainAssets.b); break;
		case 'C': batcher.drawLLSprite(x, y, 32, 32, MainAssets.c); break;
		case 'D': batcher.drawLLSprite(x, y, 32, 32, MainAssets.d); break;
		case 'E': batcher.drawLLSprite(x, y, 32, 32, MainAssets.e); break;
		case 'F': batcher.drawLLSprite(x, y, 32, 32, MainAssets.f); break;
		case 'G': batcher.drawLLSprite(x, y, 32, 32, MainAssets.g); break;
		case 'H': batcher.drawLLSprite(x, y, 32, 32, MainAssets.h); break;
		case 'I': batcher.drawLLSprite(x, y, 32, 32, MainAssets.i); break;
		case 'J': batcher.drawLLSprite(x, y, 32, 32, MainAssets.j); break;
		case 'K': batcher.drawLLSprite(x, y, 32, 32, MainAssets.k); break;
		case 'L': batcher.drawLLSprite(x, y, 32, 32, MainAssets.l); break;
		case 'M': batcher.drawLLSprite(x, y, 32, 32, MainAssets.m); break;
		case 'N': batcher.drawLLSprite(x, y, 32, 32, MainAssets.n); break;
		case 'O': batcher.drawLLSprite(x, y, 32, 32, MainAssets.o); break;
		case 'P': batcher.drawLLSprite(x, y, 32, 32, MainAssets.p); break;
		case 'Q': batcher.drawLLSprite(x, y, 32, 32, MainAssets.q); break;
		case 'R': batcher.drawLLSprite(x, y, 32, 32, MainAssets.r); break;
		case 'S': batcher.drawLLSprite(x, y, 32, 32, MainAssets.s); break;
		case 'T': batcher.drawLLSprite(x, y, 32, 32, MainAssets.t); break;
		case 'U': batcher.drawLLSprite(x, y, 32, 32, MainAssets.u); break;
		case 'V': batcher.drawLLSprite(x, y, 32, 32, MainAssets.v); break;
		case 'W': batcher.drawLLSprite(x, y, 32, 32, MainAssets.w); break;
		case 'X': batcher.drawLLSprite(x, y, 32, 32, MainAssets.x); break;
		case 'Y': batcher.drawLLSprite(x, y, 32, 32, MainAssets.y); break;
		case 'Z': batcher.drawLLSprite(x, y, 32, 32, MainAssets.z); break;
		}
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
