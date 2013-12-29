package com.andrewjrowell.fly.screens;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.andrewjrowell.fly.assets.MainAssets;
import com.andrewjrowell.fly.entities.PlayerFly;
import com.andrewjrowell.fly.entities.Powerup;
import com.andrewjrowell.fly.entities.PowerupManager;
import com.andrewjrowell.fly.entities.Predator;
import com.andrewjrowell.fly.entities.PredatorManager;
import com.andrewjrowell.fly.entities.Rotten;
import com.andrewjrowell.fly.entities.RottenManager;
import com.andrewjrowell.framework.Game;
import com.andrewjrowell.framework.Screen;
import com.andrewjrowell.framework.diskio.HighScores;
import com.andrewjrowell.framework.gl.Camera2D;
import com.andrewjrowell.framework.gl.GLGame;
import com.andrewjrowell.framework.gl.GLGraphics;
import com.andrewjrowell.framework.gl.SpriteBatcher;
import com.andrewjrowell.framework.input.Input.TouchEvent;
import com.andrewjrowell.framework.math.Circle;
import com.andrewjrowell.framework.math.OverlapTester;
import com.andrewjrowell.framework.math.Vector2;

/**
 * <p> {@link Screen} with gameplay </p>
 * 
 * @author Andrew Rowell
 * @version 1.0
 */


public class GamePlayScreen extends Screen{
	final float WORLD_WIDTH = 320.0f;
	final float WORLD_HEIGHT = 480.0f;
	final static int TEXTX = 48; // Width of bitmap font
	final static int TEXTY = 64; // Height of bitmap font
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2(); // Stores the position last touched
	Camera2D camera;
	SpriteBatcher batcher;
	RottenManager rottens; // Food
	PredatorManager predators;
	PowerupManager powerups;
	PlayerFly fly;
	
	// How much we need to shift the background to give the
	// appearance of constantly scrolling grass
	float offset;
	
	// Player's score in game
	float score;
			
	public GamePlayScreen(Game game) {
		super(game);
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		rottens = new RottenManager(WORLD_WIDTH,WORLD_HEIGHT);
		predators = new PredatorManager(WORLD_WIDTH,WORLD_HEIGHT);
		powerups = new PowerupManager(WORLD_WIDTH,WORLD_HEIGHT);
		fly = new PlayerFly(WORLD_WIDTH);
		score = 0;
		
		HighScores.load(game.getFileIO());
		MainAssets.introchords.play(1.0f);
	}
	
	
	/**
	 * <p>Update various elements of the GamePlayScreen</p>
	 * 
	 * @param deltaTime time since last update()
	 */
	@Override
	public void update(float deltaTime) {
		
		// Paces are in pixels per second
		float xpace; // How fast we move forwards
		float ypace; // Affects lateral movement speeds
		
		if(powerups.getState() == PowerupManager.SPEED_ID){
			xpace = 48;
			ypace = 128;
		} else if (powerups.getState() == PowerupManager.SLOW_ID){
			xpace = 8;
			ypace = 32;
		} else {
			xpace = 48;
			ypace = 48;
		}
		
		//Move background
		offset += ypace * deltaTime;
		if(offset >= 320){
			offset = 0;
		}
		
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		// Process touch input
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			camera.touchToWorld(touchPos.set(event.x, event.y));
			if(event.type == TouchEvent.TOUCH_UP){}
		}
		
		// Update the rottens
		rottens.update(deltaTime, ypace);
		// Check to see how many rottens were eaten
		score += 10 * rottens.eaten(fly);

		// update predators
		predators.update(deltaTime, xpace, ypace);
		// Check for collisions with predators
		if(predators.collisionCheck(fly)){
			MainAssets.speed.stop();
			MainAssets.crunch.play(1.0f);
			if(HighScores.isHighScore((int) score)){
				game.setScreen(new EnterHighScoreScreen(game, (int) score));
			} else {
				game.setScreen(new NoHighScoreScreen(game));
			}
		}		
		
		powerups.update(deltaTime, ypace);
		powerups.checkCollisions(fly);
		
		// Move fly based on accelerometer
		fly.move(game.getInput().getAccelX(), deltaTime);
		
		// Increase score
		score += deltaTime;
	}
	
	/**
	 * <p>Draw the various elements of the GamePlayScreen</p>
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
		
		// Draw background
		for(int j = 0; j < 4; j++){
			batcher.drawLLSprite(0, (int) (j * 320.0f - offset),320,320, MainAssets.background);
		}
		// Draw the fly
		batcher.drawSprite(fly.getPosition(),
				fly.getFlySize() + fly.getYPosition(),
				fly.getFlySize(), fly.getFlySize(), MainAssets.fly);
		
		// Draw rottens
		for(Rotten r : rottens.getRottens()){
			switch(r.type){
			case 1: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten1); break;
			case 2: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten2); break;
			case 3: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten3); break;
			case 4: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten4); break;
			case 5: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten5); break;
			case 6: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten6); break;
			case 7: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten7); break;
			case 8: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten8); break;
			case 9: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten9); break;
			case 10: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten10); break;
			case 11: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten11); break;
			case 12: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten12); break;
			case 13: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten13); break;
			case 14: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten14); break;
			case 15: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten15); break;
			case 16: batcher.drawSprite(r.x, r.y, 32, 32, MainAssets.rotten16); break;
			}
		}
		
		// Draw predators
		for(Predator p : predators.getPredators()){
			switch(p.type){
			case 1: batcher.drawSprite(p.x, p.y, 64, 64, MainAssets.spider); break;
			case 2: batcher.drawSprite(p.x, p.y, 128, 128, MainAssets.lizard); break;
			case 3: batcher.drawSprite(p.x, p.y, 192, 192, MainAssets.duck); break;
			}
		}
		
		// Draw powerups
		for(Powerup p : powerups.getPowerups()){
			switch(p.type){
				case PowerupManager.SPEED_ID: batcher.drawSprite(p.x,
						p.y, 32, 32, MainAssets.speedpowerup); break;
				case PowerupManager.SLOW_ID: batcher.drawSprite(p.x,
						p.y, 32, 32, MainAssets.slowpowerup); break;
			}
		}
		
		// Draw score
		batcher.drawLLSprite(0, 0, 32 * 5, 32, MainAssets.widered);
		batcher.drawLLSprite(32 * 5, 0, (int) WORLD_WIDTH - (32 * 5), 32, MainAssets.widewhite);
		batcher.drawLLSprite(0,0,32,32,MainAssets.s);
		batcher.drawLLSprite(32,0,32,32,MainAssets.c);
		batcher.drawLLSprite(64,0,32,32,MainAssets.o);
		batcher.drawLLSprite(96,0,32,32,MainAssets.r);
		batcher.drawLLSprite(128,0,32,32,MainAssets.e);
		
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
		case 1: batcher.drawLLSprite(160, 0, 32, 32, MainAssets.one); break;
		case 2: batcher.drawLLSprite(160, 0, 32, 32, MainAssets.two); break;
		case 3: batcher.drawLLSprite(160, 0, 32, 32, MainAssets.three); break;
		case 4: batcher.drawLLSprite(160, 0, 32, 32, MainAssets.four); break;
		case 5: batcher.drawLLSprite(160, 0, 32, 32, MainAssets.five); break;
		case 6: batcher.drawLLSprite(160, 0, 32, 32, MainAssets.six); break;
		case 7: batcher.drawLLSprite(160, 0, 32, 32, MainAssets.seven); break;
		case 8: batcher.drawLLSprite(160, 0, 32, 32, MainAssets.eight); break;
		case 9: batcher.drawLLSprite(160, 0, 32, 32, MainAssets.nine); break;
		case 0: batcher.drawLLSprite(160, 0, 32, 32, MainAssets.zero); break;
		}
		
		switch(digit2){
		case 1: batcher.drawLLSprite(192, 0, 32, 32, MainAssets.one); break;
		case 2: batcher.drawLLSprite(192, 0, 32, 32, MainAssets.two); break;
		case 3: batcher.drawLLSprite(192, 0, 32, 32, MainAssets.three); break;
		case 4: batcher.drawLLSprite(192, 0, 32, 32, MainAssets.four); break;
		case 5: batcher.drawLLSprite(192, 0, 32, 32, MainAssets.five); break;
		case 6: batcher.drawLLSprite(192, 0, 32, 32, MainAssets.six); break;
		case 7: batcher.drawLLSprite(192, 0, 32, 32, MainAssets.seven); break;
		case 8: batcher.drawLLSprite(192, 0, 32, 32, MainAssets.eight); break;
		case 9: batcher.drawLLSprite(192, 0, 32, 32, MainAssets.nine); break;
		case 0: batcher.drawLLSprite(192, 0, 32, 32, MainAssets.zero); break;
		}
		
		switch(digit3){
		case 1: batcher.drawLLSprite(224, 0, 32, 32, MainAssets.one); break;
		case 2: batcher.drawLLSprite(224, 0, 32, 32, MainAssets.two); break;
		case 3: batcher.drawLLSprite(224, 0, 32, 32, MainAssets.three); break;
		case 4: batcher.drawLLSprite(224, 0, 32, 32, MainAssets.four); break;
		case 5: batcher.drawLLSprite(224, 0, 32, 32, MainAssets.five); break;
		case 6: batcher.drawLLSprite(224, 0, 32, 32, MainAssets.six); break;
		case 7: batcher.drawLLSprite(224, 0, 32, 32, MainAssets.seven); break;
		case 8: batcher.drawLLSprite(224, 0, 32, 32, MainAssets.eight); break;
		case 9: batcher.drawLLSprite(224, 0, 32, 32, MainAssets.nine); break;
		case 0: batcher.drawLLSprite(224, 0, 32, 32, MainAssets.zero); break;
		}
		
		switch(digit4){
		case 1: batcher.drawLLSprite(256, 0, 32, 32, MainAssets.one); break;
		case 2: batcher.drawLLSprite(256, 0, 32, 32, MainAssets.two); break;
		case 3: batcher.drawLLSprite(256, 0, 32, 32, MainAssets.three); break;
		case 4: batcher.drawLLSprite(256, 0, 32, 32, MainAssets.four); break;
		case 5: batcher.drawLLSprite(256, 0, 32, 32, MainAssets.five); break;
		case 6: batcher.drawLLSprite(256, 0, 32, 32, MainAssets.six); break;
		case 7: batcher.drawLLSprite(256, 0, 32, 32, MainAssets.seven); break;
		case 8: batcher.drawLLSprite(256, 0, 32, 32, MainAssets.eight); break;
		case 9: batcher.drawLLSprite(256, 0, 32, 32, MainAssets.nine); break;
		case 0: batcher.drawLLSprite(256, 0, 32, 32, MainAssets.zero); break;
		}
		
		switch(digit5){
		case 1: batcher.drawLLSprite(288, 0, 32, 32, MainAssets.one); break;
		case 2: batcher.drawLLSprite(288, 0, 32, 32, MainAssets.two); break;
		case 3: batcher.drawLLSprite(288, 0, 32, 32, MainAssets.three); break;
		case 4: batcher.drawLLSprite(288, 0, 32, 32, MainAssets.four); break;
		case 5: batcher.drawLLSprite(288, 0, 32, 32, MainAssets.five); break;
		case 6: batcher.drawLLSprite(288, 0, 32, 32, MainAssets.six); break;
		case 7: batcher.drawLLSprite(288, 0, 32, 32, MainAssets.seven); break;
		case 8: batcher.drawLLSprite(288, 0, 32, 32, MainAssets.eight); break;
		case 9: batcher.drawLLSprite(288, 0, 32, 32, MainAssets.nine); break;
		case 0: batcher.drawLLSprite(288, 0, 32, 32, MainAssets.zero); break;
		}
		
		//Draw Powerup Bar
		if(powerups.getState() != PowerupManager.ID_NULL){
			batcher.drawLLSprite(0, (int) (WORLD_HEIGHT - 32), (int) WORLD_WIDTH, 32, MainAssets.widered);
			batcher.drawLLSprite(
				(int) (WORLD_WIDTH * (powerups.getPercentLeft())) ,
				(int) (WORLD_HEIGHT - 32.0),
				(int) WORLD_WIDTH, 32, MainAssets.widewhite);
		}
		
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
