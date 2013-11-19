package com.andrewjrowell.fly.screens;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.andrewjrowell.fly.Assets;
import com.andrewjrowell.fly.HighScores;
import com.andrewjrowell.fly.items.Predator;
import com.andrewjrowell.fly.items.Rotten;
import com.andrewjrowell.fly.powerups.Powerup;
import com.andrewjrowell.framework.GLGraphics;
import com.andrewjrowell.framework.gl.Camera2D;
import com.andrewjrowell.framework.gl.SpriteBatcher;
import com.andrewjrowell.framework.interfaces.Game;
import com.andrewjrowell.framework.interfaces.Input.TouchEvent;
import com.andrewjrowell.framework.interfaces.Screen;
import com.andrewjrowell.framework.math.Circle;
import com.andrewjrowell.framework.math.OverlapTester;
import com.andrewjrowell.framework.math.Rectangle;
import com.andrewjrowell.framework.math.Vector2;
import com.andrewjrowell.framework.GLGame;

public class GamePlayScreen extends Screen{
	final float WORLD_WIDTH = 320.0f;
	final float WORLD_HEIGHT = 480.0f;
	final static int TEXTX = 48;
	final static int TEXTY = 64;
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2();
	Camera2D camera;
	SpriteBatcher batcher;
	ArrayList<Rotten> rottens;
	ArrayList<Predator> predators;
	ArrayList<Powerup> powerups;
	
	float offset, rottencounter, predatorcounter, powerupcounter, predfreq;
	
	float score;
	
	float flyPos;
	int flySize;
	
	boolean buzzing;
	
	int powerupState;
	float powerupTimeLeft;
			
	public GamePlayScreen(Game game) {
		super(game);
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		rottens = new ArrayList<Rotten>();
		predators = new ArrayList<Predator>();
		powerups = new ArrayList<Powerup>();
		rottencounter = 0;
		powerupcounter = 20;
		flyPos = WORLD_WIDTH / 2;
		flySize = 16;
		buzzing = false;
		score = 0;
		predatorcounter = 0;
		predfreq = 3;
		powerupState = 0;
		powerupTimeLeft = 0;
		
		HighScores.load(game.getFileIO());
		Assets.introchords.play(1.0f);
	}
	@Override
	public void update(float deltaTime) {
		int pace;
		if(powerupState == Powerup.POWERUP_ID_SPEED){
			pace = 128;
		} else {
			pace = 48;
		}
		offset += pace * deltaTime;
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
				
			}
		}
		rottencounter += deltaTime * (pace/32.0);
		if(rottencounter >= 9){
			rottens.add(new Rotten(WORLD_WIDTH, WORLD_HEIGHT));
			rottencounter = 0;
		}
		
		for(Rotten r : rottens){
			r.update(deltaTime, pace);
		}
		
		ArrayList<Rotten> rottens2 = new ArrayList<Rotten>();
		
		for(Rotten r : rottens){
			if(OverlapTester.overlapRectangles(
					new Rectangle(flyPos - (flySize/2),64 + (flySize/2),flySize, flySize),
					new Rectangle(r.x - 16, r.y - 16, 32, 32))){
				r.eaten = true;
				flySize++;
				Assets.slurp.play(1.0f);
				score += 10;
			} else {
				rottens2.add(r);
			}
		}
		rottens = rottens2;
		

		predatorcounter += deltaTime * (pace/32.0);
		if(predatorcounter >= 6){
			Predator newPred = new Predator(WORLD_WIDTH, WORLD_HEIGHT);
			predators.add(newPred);
			predatorcounter = 3 - predfreq;
			predfreq *= 0.9;
		}
		
		for(Predator p : predators){
			p.update(deltaTime, pace);
		}
				
		for(Predator p : predators){
			if(p.type == 1 && OverlapTester.overlapCircleRectangle(
					new Circle(p.x, p.y, 28),
					new Rectangle(flyPos - (flySize/2),64 + (flySize/2),flySize, flySize))){
				Assets.speed.stop();
				Assets.crunch.play(1.0f);
				if(HighScores.isHighScore((int) score)){
					game.setScreen(new EnterHighScoreScreen(game, (int) score));
				} else {
					game.setScreen(new NoHighScoreScreen(game));
				}
			}
			if(p.type == 2 && OverlapTester.overlapCircleRectangle(
					new Circle(p.x, p.y, 56),
					new Rectangle(flyPos - (flySize/2),64 + (flySize/2),flySize, flySize))){
				Assets.speed.stop();
				Assets.crunch.play(1.0f);
				if(HighScores.isHighScore((int) score)){
					game.setScreen(new EnterHighScoreScreen(game, (int) score));
				} else {
					game.setScreen(new NoHighScoreScreen(game));
				}
			}
			if(p.type == 3 && OverlapTester.overlapCircleRectangle(
					new Circle(p.x, p.y, 84),
					new Rectangle(flyPos - (flySize/2),64 + (flySize/2),flySize, flySize))){
				Assets.speed.stop();
				Assets.crunch.play(1.0f);
				if(HighScores.isHighScore((int) score)){
					game.setScreen(new EnterHighScoreScreen(game, (int) score));
				} else {
					game.setScreen(new NoHighScoreScreen(game));
				}
			}
		}
		
		ArrayList<Powerup> powerups2 = new ArrayList<Powerup>();
		for(Powerup p : powerups){
			p.update(deltaTime, pace);
		}
		
		for(Powerup p : powerups){
			if(p.type == 1 && OverlapTester.overlapCircleRectangle(
					new Circle(p.x, p.y, 28),
					new Rectangle(flyPos - (flySize/2),64 + (flySize/2),flySize, flySize))){
				powerupState = Powerup.POWERUP_ID_SPEED;
				powerupTimeLeft = Powerup.POWERUP_DURATION;
				Assets.reloadSpeedSound();
				Assets.speed.play();
				p.remove = true;
			}
		}
		
		for(Powerup p : powerups){
			if(p.remove != true){
				powerups2.add(p);
			}
		}
		
		powerups = powerups2;
		
		
		if(Math.abs(game.getInput().getAccelX()) > 0.5){
			flyPos -= (int) (game.getInput().getAccelX() * deltaTime * 32 * 2);
			if(!buzzing && Math.abs(game.getInput().getAccelX()) > 3){
				Assets.buzz.play(1.0f);
				buzzing = true;
			}
		} else {
			buzzing = false;
		}
		
		if(flyPos <= 0){
			flyPos = 0;
		}
		if(flyPos >= WORLD_WIDTH){
			flyPos = WORLD_WIDTH;
		}
		
		score += deltaTime;
		
		powerupTimeLeft -= deltaTime;
		if(powerupTimeLeft <= 0){
			powerupState = 0;
		}
		
		powerupcounter += deltaTime;
		if(powerupcounter >= 30){
			Powerup pu = new Powerup(WORLD_WIDTH, WORLD_HEIGHT);
			powerups.add(pu);
			powerupcounter = 0;
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
		
		batcher.drawSprite(flyPos, 64 + flySize, flySize, flySize, Assets.fly);
		
		for(Rotten r : rottens){
			switch(r.type){
			case 1: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten1); break;
			case 2: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten2); break;
			case 3: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten3); break;
			case 4: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten4); break;
			case 5: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten5); break;
			case 6: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten6); break;
			case 7: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten7); break;
			case 8: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten8); break;
			case 9: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten9); break;
			case 10: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten10); break;
			case 11: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten11); break;
			case 12: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten12); break;
			case 13: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten13); break;
			case 14: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten14); break;
			case 15: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten15); break;
			case 16: batcher.drawSprite(r.x, r.y, 32, 32, Assets.rotten16); break;
			}
		}
		for(Predator p : predators){
			switch(p.type){
			case 1: batcher.drawSprite(p.x, p.y, 64, 64, Assets.spider); break;
			case 2: batcher.drawSprite(p.x, p.y, 128, 128, Assets.lizard); break;
			case 3: batcher.drawSprite(p.x, p.y, 192, 192, Assets.duck); break;
			}
		}
		
		for(Powerup p : powerups){
			switch(p.type){
			case Powerup.POWERUP_ID_SPEED: batcher.drawSprite(p.x, p.y, 32, 32, Assets.speedpowerup); break;
			}
		}
		
		batcher.drawLLSprite(0, 0, 32 * 5, 32, Assets.widered);
		batcher.drawLLSprite(32 * 5, 0, (int) WORLD_WIDTH - (32 * 5), 32, Assets.widewhite);
		batcher.drawLLSprite(0,0,32,32,Assets.s);
		batcher.drawLLSprite(32,0,32,32,Assets.c);
		batcher.drawLLSprite(64,0,32,32,Assets.o);
		batcher.drawLLSprite(96,0,32,32,Assets.r);
		batcher.drawLLSprite(128,0,32,32,Assets.e);
		
		int tscore = (int) score;
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
				
		switch(digit1){
		case 1: batcher.drawLLSprite(160, 0, 32, 32, Assets.one); break;
		case 2: batcher.drawLLSprite(160, 0, 32, 32, Assets.two); break;
		case 3: batcher.drawLLSprite(160, 0, 32, 32, Assets.three); break;
		case 4: batcher.drawLLSprite(160, 0, 32, 32, Assets.four); break;
		case 5: batcher.drawLLSprite(160, 0, 32, 32, Assets.five); break;
		case 6: batcher.drawLLSprite(160, 0, 32, 32, Assets.six); break;
		case 7: batcher.drawLLSprite(160, 0, 32, 32, Assets.seven); break;
		case 8: batcher.drawLLSprite(160, 0, 32, 32, Assets.eight); break;
		case 9: batcher.drawLLSprite(160, 0, 32, 32, Assets.nine); break;
		case 0: batcher.drawLLSprite(160, 0, 32, 32, Assets.zero); break;
		}
		
		switch(digit2){
		case 1: batcher.drawLLSprite(192, 0, 32, 32, Assets.one); break;
		case 2: batcher.drawLLSprite(192, 0, 32, 32, Assets.two); break;
		case 3: batcher.drawLLSprite(192, 0, 32, 32, Assets.three); break;
		case 4: batcher.drawLLSprite(192, 0, 32, 32, Assets.four); break;
		case 5: batcher.drawLLSprite(192, 0, 32, 32, Assets.five); break;
		case 6: batcher.drawLLSprite(192, 0, 32, 32, Assets.six); break;
		case 7: batcher.drawLLSprite(192, 0, 32, 32, Assets.seven); break;
		case 8: batcher.drawLLSprite(192, 0, 32, 32, Assets.eight); break;
		case 9: batcher.drawLLSprite(192, 0, 32, 32, Assets.nine); break;
		case 0: batcher.drawLLSprite(192, 0, 32, 32, Assets.zero); break;
		}
		
		switch(digit3){
		case 1: batcher.drawLLSprite(224, 0, 32, 32, Assets.one); break;
		case 2: batcher.drawLLSprite(224, 0, 32, 32, Assets.two); break;
		case 3: batcher.drawLLSprite(224, 0, 32, 32, Assets.three); break;
		case 4: batcher.drawLLSprite(224, 0, 32, 32, Assets.four); break;
		case 5: batcher.drawLLSprite(224, 0, 32, 32, Assets.five); break;
		case 6: batcher.drawLLSprite(224, 0, 32, 32, Assets.six); break;
		case 7: batcher.drawLLSprite(224, 0, 32, 32, Assets.seven); break;
		case 8: batcher.drawLLSprite(224, 0, 32, 32, Assets.eight); break;
		case 9: batcher.drawLLSprite(224, 0, 32, 32, Assets.nine); break;
		case 0: batcher.drawLLSprite(224, 0, 32, 32, Assets.zero); break;
		}
		
		switch(digit4){
		case 1: batcher.drawLLSprite(256, 0, 32, 32, Assets.one); break;
		case 2: batcher.drawLLSprite(256, 0, 32, 32, Assets.two); break;
		case 3: batcher.drawLLSprite(256, 0, 32, 32, Assets.three); break;
		case 4: batcher.drawLLSprite(256, 0, 32, 32, Assets.four); break;
		case 5: batcher.drawLLSprite(256, 0, 32, 32, Assets.five); break;
		case 6: batcher.drawLLSprite(256, 0, 32, 32, Assets.six); break;
		case 7: batcher.drawLLSprite(256, 0, 32, 32, Assets.seven); break;
		case 8: batcher.drawLLSprite(256, 0, 32, 32, Assets.eight); break;
		case 9: batcher.drawLLSprite(256, 0, 32, 32, Assets.nine); break;
		case 0: batcher.drawLLSprite(256, 0, 32, 32, Assets.zero); break;
		}
		
		switch(digit5){
		case 1: batcher.drawLLSprite(288, 0, 32, 32, Assets.one); break;
		case 2: batcher.drawLLSprite(288, 0, 32, 32, Assets.two); break;
		case 3: batcher.drawLLSprite(288, 0, 32, 32, Assets.three); break;
		case 4: batcher.drawLLSprite(288, 0, 32, 32, Assets.four); break;
		case 5: batcher.drawLLSprite(288, 0, 32, 32, Assets.five); break;
		case 6: batcher.drawLLSprite(288, 0, 32, 32, Assets.six); break;
		case 7: batcher.drawLLSprite(288, 0, 32, 32, Assets.seven); break;
		case 8: batcher.drawLLSprite(288, 0, 32, 32, Assets.eight); break;
		case 9: batcher.drawLLSprite(288, 0, 32, 32, Assets.nine); break;
		case 0: batcher.drawLLSprite(288, 0, 32, 32, Assets.zero); break;
		}
		
		//Powerup Bar
		if(powerupState != 0){
			batcher.drawLLSprite(0, (int) (WORLD_HEIGHT - 32), (int) WORLD_WIDTH, 32, Assets.widered);
			batcher.drawLLSprite((int) (WORLD_WIDTH * (powerupTimeLeft / Powerup.POWERUP_DURATION)) , (int) (WORLD_HEIGHT - 32.0),
				(int) WORLD_WIDTH, 32, Assets.widewhite);
		}
		
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
