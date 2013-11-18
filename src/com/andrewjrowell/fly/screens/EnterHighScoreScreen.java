package com.andrewjrowell.fly.screens;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.FloatMath;

import com.andrewjrowell.fly.Assets;
import com.andrewjrowell.fly.HighScores;
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

public class EnterHighScoreScreen extends Screen{
	final float WORLD_WIDTH = 320.0f;
	final float WORLD_HEIGHT = 480.0f;
	final static int TEXTX = 48;
	final static int TEXTY = 64;
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2();
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle nextBounds, textBounds, nameBounds, scoreBounds, uparrow1bounds,
		uparrow2bounds, uparrow3bounds, downarrow1bounds, downarrow2bounds,
		downarrow3bounds;
	
	float offset;
		
	int score;
			
	String name;
	public EnterHighScoreScreen(Game game, int score) {
		super(game);
		this.score = score;
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		nextBounds = new Rectangle(96, 0, 128, 64);
		textBounds = new Rectangle(16, 320, 288, 128);
		nameBounds = new Rectangle(92, 192, 144, 128);
		scoreBounds = new Rectangle(40, 96, 240, 64);
		
		uparrow1bounds = new Rectangle(96, 288, 32, 32);
		uparrow2bounds = new Rectangle(144, 288, 32, 32);
		uparrow3bounds = new Rectangle(192, 288, 32, 32);
		downarrow1bounds = new Rectangle(96, 192, 32, 32);
		downarrow2bounds = new Rectangle(144, 192, 32, 32);
		downarrow3bounds = new Rectangle(192, 192, 32, 32);
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		name = "AAA";
		Assets.majorchord.play(1.0f);
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
				if(OverlapTester.pointInRectangle(uparrow1bounds, touchPos)){
					char[] namechars = name.toCharArray();
					namechars[0] = upChar(namechars[0]);
					name = String.valueOf(namechars);
					Assets.click.play(1.0f);
				}
				if(OverlapTester.pointInRectangle(uparrow2bounds, touchPos)){
					char[] namechars = name.toCharArray();
					namechars[1] = upChar(namechars[1]);
					name = String.valueOf(namechars);
					Assets.click.play(1.0f);
				}
				if(OverlapTester.pointInRectangle(uparrow3bounds, touchPos)){
					char[] namechars = name.toCharArray();
					namechars[2] = upChar(namechars[2]);
					name = String.valueOf(namechars);
					Assets.click.play(1.0f);
				}
				if(OverlapTester.pointInRectangle(downarrow1bounds, touchPos)){
					char[] namechars = name.toCharArray();
					namechars[0] = downChar(namechars[0]);
					name = String.valueOf(namechars);
					Assets.click.play(1.0f);
				}
				if(OverlapTester.pointInRectangle(downarrow2bounds, touchPos)){
					char[] namechars = name.toCharArray();
					namechars[1] = downChar(namechars[1]);
					name = String.valueOf(namechars);
					Assets.click.play(1.0f);
				}
				if(OverlapTester.pointInRectangle(downarrow3bounds, touchPos)){
					char[] namechars = name.toCharArray();
					namechars[2] = downChar(namechars[2]);
					name = String.valueOf(namechars);
					Assets.click.play(1.0f);
				}
				if(OverlapTester.pointInRectangle(nextBounds, touchPos)){
					Assets.click.play(1.0f);
					HighScores.addHighScore(score, name);
					HighScores.save(game.getFileIO());
					game.setScreen(new ViewHighScoreScreen(game));
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
		batcher.drawLLSprite((int) nameBounds.lowerLeft.x,(int) nameBounds.lowerLeft.y,
				(int)nameBounds.width,(int) nameBounds.height, Assets.white);
		batcher.drawLLSprite((int) scoreBounds.lowerLeft.x,(int) scoreBounds.lowerLeft.y,
				(int)scoreBounds.width,(int) scoreBounds.height, Assets.white);
		
		batcher.drawLLSprite(64, 384, TEXTX, TEXTY, Assets.H);
		batcher.drawLLSprite(64 + TEXTX, 384, TEXTX, TEXTY, Assets.I);
		batcher.drawLLSprite(64 + 2 * TEXTX, 384, TEXTX, TEXTY, Assets.G);
		batcher.drawLLSprite(64 + 3 * TEXTX, 384, TEXTX, TEXTY, Assets.H);
		batcher.drawLLSprite(16, 320, TEXTX, TEXTY, Assets.S);
		batcher.drawLLSprite(16 + TEXTX, 320, TEXTX, TEXTY, Assets.C);
		batcher.drawLLSprite(16 + 2 * TEXTX, 320, TEXTX, TEXTY, Assets.O);
		batcher.drawLLSprite(16 + 3 * TEXTX, 320, TEXTX, TEXTY, Assets.R);
		batcher.drawLLSprite(16 + 4 * TEXTX, 320, TEXTX, TEXTY, Assets.E);
		batcher.drawLLSprite(16 + 5 * TEXTX, 320, TEXTX, TEXTY, Assets.EXCLAMATION_POINT);
		
		batcher.drawLLSprite(96, 288, 32, 32, Assets.uparrow);
		batcher.drawLLSprite(144, 288, 32, 32, Assets.uparrow);
		batcher.drawLLSprite(192, 288, 32, 32, Assets.uparrow);
		batcher.drawLLSprite(96, 192, 32, 32, Assets.downarrow);
		batcher.drawLLSprite(144, 192, 32, 32, Assets.downarrow);
		batcher.drawLLSprite(192, 192, 32, 32, Assets.downarrow);

		
		drawScore(score, 40, 96);
		drawName(92, 224, name);
		
		batcher.drawLLSprite(96, 0, 128, 64, Assets.arrow);
		
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}
	
	private void drawScore(int score, int x, int y){
		int tscore = score;
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
		drawDigit(x + 48 * 1,y,digit2);
		drawDigit(x + 48 * 2,y,digit3);
		drawDigit(x + 48 * 3,y,digit4);
		drawDigit(x + 48 * 4,y,digit5);
	}
	
	private void drawDigit(int x, int y, int digit){
		switch(digit){
		case 1: batcher.drawLLSprite(x, y, 48, 64, Assets.ONE); break;
		case 2: batcher.drawLLSprite(x, y, 48, 64, Assets.TWO); break;
		case 3: batcher.drawLLSprite(x, y, 48, 64, Assets.THREE); break;
		case 4: batcher.drawLLSprite(x, y, 48, 64, Assets.FOUR); break;
		case 5: batcher.drawLLSprite(x, y, 48, 64, Assets.FIVE); break;
		case 6: batcher.drawLLSprite(x, y, 48, 64, Assets.SIX); break;
		case 7: batcher.drawLLSprite(x, y, 48, 64, Assets.SEVEN); break;
		case 8: batcher.drawLLSprite(x, y, 48, 64, Assets.EIGHT); break;
		case 9: batcher.drawLLSprite(x, y, 48, 64, Assets.NINE); break;
		case 0: batcher.drawLLSprite(x, y, 48, 64, Assets.ZERO); break;
		}
	}
	
	private void drawName(int x, int y, String name){
		drawChar(x, y, name.charAt(0));
		drawChar(x + 48, y, name.charAt(1));
		drawChar(x + 96, y, name.charAt(2));
	}
	
	private void drawChar(int x, int y, char c){
		switch(c){
		case 'A': batcher.drawLLSprite(x, y, 48, 64, Assets.A); break;
		case 'B': batcher.drawLLSprite(x, y, 48, 64, Assets.B); break;
		case 'C': batcher.drawLLSprite(x, y, 48, 64, Assets.C); break;
		case 'D': batcher.drawLLSprite(x, y, 48, 64, Assets.D); break;
		case 'E': batcher.drawLLSprite(x, y, 48, 64, Assets.E); break;
		case 'F': batcher.drawLLSprite(x, y, 48, 64, Assets.F); break;
		case 'G': batcher.drawLLSprite(x, y, 48, 64, Assets.G); break;
		case 'H': batcher.drawLLSprite(x, y, 48, 64, Assets.H); break;
		case 'I': batcher.drawLLSprite(x, y, 48, 64, Assets.I); break;
		case 'J': batcher.drawLLSprite(x, y, 48, 64, Assets.J); break;
		case 'K': batcher.drawLLSprite(x, y, 48, 64, Assets.K); break;
		case 'L': batcher.drawLLSprite(x, y, 48, 64, Assets.L); break;
		case 'M': batcher.drawLLSprite(x, y, 48, 64, Assets.M); break;
		case 'N': batcher.drawLLSprite(x, y, 48, 64, Assets.N); break;
		case 'O': batcher.drawLLSprite(x, y, 48, 64, Assets.O); break;
		case 'P': batcher.drawLLSprite(x, y, 48, 64, Assets.P); break;
		case 'Q': batcher.drawLLSprite(x, y, 48, 64, Assets.Q); break;
		case 'R': batcher.drawLLSprite(x, y, 48, 64, Assets.R); break;
		case 'S': batcher.drawLLSprite(x, y, 48, 64, Assets.S); break;
		case 'T': batcher.drawLLSprite(x, y, 48, 64, Assets.T); break;
		case 'U': batcher.drawLLSprite(x, y, 48, 64, Assets.U); break;
		case 'V': batcher.drawLLSprite(x, y, 48, 64, Assets.V); break;
		case 'W': batcher.drawLLSprite(x, y, 48, 64, Assets.W); break;
		case 'X': batcher.drawLLSprite(x, y, 48, 64, Assets.X); break;
		case 'Y': batcher.drawLLSprite(x, y, 48, 64, Assets.Y); break;
		case 'Z': batcher.drawLLSprite(x, y, 48, 64, Assets.Z); break;
		}
	}
	
	private char upChar(char c){
		switch(c){
		case 'A': return 'Z';
		case 'B': return 'A';
		case 'C': return 'B';
		case 'D': return 'C';
		case 'E': return 'D';
		case 'F': return 'E';
		case 'G': return 'F';
		case 'H': return 'G';
		case 'I': return 'H';
		case 'J': return 'I';
		case 'K': return 'J';
		case 'L': return 'K';
		case 'M': return 'L';
		case 'N': return 'M';
		case 'O': return 'N';
		case 'P': return 'O';
		case 'Q': return 'P';
		case 'R': return 'Q';
		case 'S': return 'R';
		case 'T': return 'S';
		case 'U': return 'T';
		case 'V': return 'U';
		case 'W': return 'V';
		case 'X': return 'W';
		case 'Y': return 'X';
		case 'Z': return 'Y';
		}
		return 'A';
	}
	
	private char downChar(char c){
		switch(c){
		case 'A': return 'B';
		case 'B': return 'C';
		case 'C': return 'D';
		case 'D': return 'E';
		case 'E': return 'F';
		case 'F': return 'G';
		case 'G': return 'H';
		case 'H': return 'I';
		case 'I': return 'J';
		case 'J': return 'K';
		case 'K': return 'L';
		case 'L': return 'M';
		case 'M': return 'N';
		case 'N': return 'O';
		case 'O': return 'P';
		case 'P': return 'Q';
		case 'Q': return 'R';
		case 'R': return 'S';
		case 'S': return 'T';
		case 'T': return 'U';
		case 'U': return 'V';
		case 'V': return 'W';
		case 'W': return 'X';
		case 'X': return 'Y';
		case 'Y': return 'Z';
		case 'Z': return 'A';
		}
		return 'Z';
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
