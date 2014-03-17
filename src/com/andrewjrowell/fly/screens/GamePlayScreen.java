package com.andrewjrowell.fly.screens;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.andrewjrowell.fly.assets.MainAssets;
import com.andrewjrowell.fly.bitmapfonts.BigFont;
import com.andrewjrowell.fly.entities.PlayerFly;
import com.andrewjrowell.fly.entities.Powerup;
import com.andrewjrowell.fly.entities.PowerupManager;
import com.andrewjrowell.fly.entities.Predator;
import com.andrewjrowell.fly.entities.PredatorManager;
import com.andrewjrowell.fly.entities.Rotten;
import com.andrewjrowell.fly.entities.RottenManager;
import com.andrewjrowell.framework.CoordinateConverter;
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
 * @version 1
 */


public class GamePlayScreen extends Screen{
	final float WORLD_WIDTH;
	final float WORLD_HEIGHT;
	BigFont bigfont;
	CoordinateConverter cc;
	
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
			
	public GamePlayScreen(Game game, float worldwidth, float worldheight) {
		super(game);
		WORLD_WIDTH = worldwidth;
		WORLD_HEIGHT = worldheight;
		bigfont = new BigFont(WORLD_WIDTH, WORLD_HEIGHT);
		cc = new CoordinateConverter(WORLD_WIDTH, WORLD_HEIGHT);
		
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		rottens = new RottenManager();
		predators = new PredatorManager();
		powerups = new PowerupManager();
		fly = new PlayerFly();
		score = 0;
		
		HighScores.load(game.getFileIO());
		MainAssets.introchords.play(1f);
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
			xpace = 48.0f;
			ypace =128.0f;
		} else if (powerups.getState() == PowerupManager.SLOW_ID){
			xpace = 8.0f;
			ypace = 32.0f;
		} else {
			xpace = 48.0f;
			ypace = 48.0f;
		}
		
		// Shift background
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
			MainAssets.crunch.play(1f);
			if(HighScores.isHighScore((int) score)){
				game.setScreen(new EnterHighScoreScreen(game, (int) score, WORLD_WIDTH, WORLD_HEIGHT));
			} else {
				game.setScreen(new NoHighScoreScreen(game, WORLD_WIDTH, WORLD_HEIGHT));
			}
		}		
		
		powerups.update(deltaTime, ypace);
		powerups.checkCollisions(fly);
		
		// Move fly based on accelerometer
		if(powerups.getState() == PowerupManager.SWITCHDIR_ID){
			fly.move(-1 * game.getInput().getAccelX(), deltaTime);
		} else {
			fly.move(game.getInput().getAccelX(), deltaTime);
		}
		
		// Increase score
		score += deltaTime;
		if(powerups.getState() == PowerupManager.SWITCHDIR_ID){
			score += deltaTime * 2;
		}
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
			batcher.drawLLSprite(0, (int) cc.ycon(j * 320 - offset),(int) WORLD_WIDTH, cc.ycon(320), MainAssets.background);
		}
		// Draw the fly
		batcher.drawSprite(cc.xcon(fly.getPosition()), cc.ycon(fly.getYPosition()),
				cc.xcon(fly.getFlySize()), cc.ycon(fly.getFlySize()),
				fly.getRotation(game.getInput().getAccelX(),
						(powerups.getState() == PowerupManager.SWITCHDIR_ID)),
				MainAssets.fly);
		
		// Draw rottens
		for(Rotten r : rottens.getRottens()){
			switch(r.type){
			case 1: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten1); break;
			case 2: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten2); break;
			case 3: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten3); break;
			case 4: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten4); break;
			case 5: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten5); break;
			case 6: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten6); break;
			case 7: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten7); break;
			case 8: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten8); break;
			case 9: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten9); break;
			case 10: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten10); break;
			case 11: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten11); break;
			case 12: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten12); break;
			case 13: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten13); break;
			case 14: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten14); break;
			case 15: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten15); break;
			case 16: batcher.drawSprite(cc.xcon(r.x), cc.ycon(r.y), cc.xcon(32), cc.ycon(32), MainAssets.rotten16); break;
			}
		}
		
		// Draw predators
		for(Predator p : predators.getPredators()){
			switch(p.type){
			case 1: batcher.drawSprite(cc.xcon(p.x), cc.xcon(p.y), cc.xcon(64), cc.ycon(64), MainAssets.spider); break;
			case 2: batcher.drawSprite(cc.xcon(p.x), cc.xcon(p.y), cc.xcon(128), cc.ycon(128), MainAssets.lizard); break;
			case 3: batcher.drawSprite(cc.xcon(p.x), cc.xcon(p.y), cc.xcon(192), cc.ycon(192), MainAssets.duck); break;
			}
		}
		
		// Draw powerups
		for(Powerup p : powerups.getPowerups()){
			switch(p.type){
				case PowerupManager.SPEED_ID: batcher.drawSprite(cc.xcon(p.x),
						cc.ycon(p.y), cc.xcon(32), cc.ycon(32), MainAssets.speedpowerup); break;
				case PowerupManager.SLOW_ID: batcher.drawSprite(cc.xcon(p.x),
						cc.ycon(p.y), cc.xcon(32), cc.ycon(32), MainAssets.slowpowerup); break;
				case PowerupManager.SWITCHDIR_ID: batcher.drawSprite(cc.xcon(p.x),
						cc.ycon(p.y), cc.xcon(32), cc.ycon(32), MainAssets.switchdirpowerup); break;
			}
		}
		
		// Draw score
		batcher.drawLLSprite(0, 0, cc.xcon(32 * 5), cc.ycon(32), MainAssets.widered);
		batcher.drawLLSprite(cc.xcon(32 * 5), 0, cc.xcon(320 - (32 * 5)), cc.ycon(32), MainAssets.widewhite);
		batcher.drawLLSprite(0,0,cc.xcon(32), cc.ycon(32),MainAssets.s);
		batcher.drawLLSprite(cc.xcon(32),0,cc.xcon(32), cc.ycon(32),MainAssets.c);
		batcher.drawLLSprite(cc.xcon(64),0,cc.xcon(32), cc.ycon(32),MainAssets.o);
		batcher.drawLLSprite(cc.xcon(96),0,cc.xcon(32), cc.ycon(32),MainAssets.r);
		batcher.drawLLSprite(cc.xcon(128),0,cc.xcon(32), cc.ycon(32),MainAssets.e);
		
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
		case 1: batcher.drawLLSprite(cc.xcon(160), 0, cc.xcon(32), cc.ycon(32), MainAssets.one); break;
		case 2: batcher.drawLLSprite(cc.xcon(160), 0, cc.xcon(32), cc.ycon(32), MainAssets.two); break;
		case 3: batcher.drawLLSprite(cc.xcon(160), 0, cc.xcon(32), cc.ycon(32), MainAssets.three); break;
		case 4: batcher.drawLLSprite(cc.xcon(160), 0, cc.xcon(32), cc.ycon(32), MainAssets.four); break;
		case 5: batcher.drawLLSprite(cc.xcon(160), 0, cc.xcon(32), cc.ycon(32), MainAssets.five); break;
		case 6: batcher.drawLLSprite(cc.xcon(160), 0, cc.xcon(32), cc.ycon(32), MainAssets.six); break;
		case 7: batcher.drawLLSprite(cc.xcon(160), 0, cc.xcon(32), cc.ycon(32), MainAssets.seven); break;
		case 8: batcher.drawLLSprite(cc.xcon(160), 0, cc.xcon(32), cc.ycon(32), MainAssets.eight); break;
		case 9: batcher.drawLLSprite(cc.xcon(160), 0, cc.xcon(32), cc.ycon(32), MainAssets.nine); break;
		case 0: batcher.drawLLSprite(cc.xcon(160), 0, cc.xcon(32), cc.ycon(32), MainAssets.zero); break;
		}
		
		switch(digit2){
		case 1: batcher.drawLLSprite(cc.xcon(192), 0, cc.xcon(32), cc.ycon(32), MainAssets.one); break;
		case 2: batcher.drawLLSprite(cc.xcon(192), 0, cc.xcon(32), cc.ycon(32), MainAssets.two); break;
		case 3: batcher.drawLLSprite(cc.xcon(192), 0, cc.xcon(32), cc.ycon(32), MainAssets.three); break;
		case 4: batcher.drawLLSprite(cc.xcon(192), 0, cc.xcon(32), cc.ycon(32), MainAssets.four); break;
		case 5: batcher.drawLLSprite(cc.xcon(192), 0, cc.xcon(32), cc.ycon(32), MainAssets.five); break;
		case 6: batcher.drawLLSprite(cc.xcon(192), 0, cc.xcon(32), cc.ycon(32), MainAssets.six); break;
		case 7: batcher.drawLLSprite(cc.xcon(192), 0, cc.xcon(32), cc.ycon(32), MainAssets.seven); break;
		case 8: batcher.drawLLSprite(cc.xcon(192), 0, cc.xcon(32), cc.ycon(32), MainAssets.eight); break;
		case 9: batcher.drawLLSprite(cc.xcon(192), 0, cc.xcon(32), cc.ycon(32), MainAssets.nine); break;
		case 0: batcher.drawLLSprite(cc.xcon(192), 0, cc.xcon(32), cc.ycon(32), MainAssets.zero); break;
		}
		
		switch(digit3){
		case 1: batcher.drawLLSprite(cc.xcon(224), 0, cc.xcon(32), cc.ycon(32), MainAssets.one); break;
		case 2: batcher.drawLLSprite(cc.xcon(224), 0, cc.xcon(32), cc.ycon(32), MainAssets.two); break;
		case 3: batcher.drawLLSprite(cc.xcon(224), 0, cc.xcon(32), cc.ycon(32), MainAssets.three); break;
		case 4: batcher.drawLLSprite(cc.xcon(224), 0, cc.xcon(32), cc.ycon(32), MainAssets.four); break;
		case 5: batcher.drawLLSprite(cc.xcon(224), 0, cc.xcon(32), cc.ycon(32), MainAssets.five); break;
		case 6: batcher.drawLLSprite(cc.xcon(224), 0, cc.xcon(32), cc.ycon(32), MainAssets.six); break;
		case 7: batcher.drawLLSprite(cc.xcon(224), 0, cc.xcon(32), cc.ycon(32), MainAssets.seven); break;
		case 8: batcher.drawLLSprite(cc.xcon(224), 0, cc.xcon(32), cc.ycon(32), MainAssets.eight); break;
		case 9: batcher.drawLLSprite(cc.xcon(224), 0, cc.xcon(32), cc.ycon(32), MainAssets.nine); break;
		case 0: batcher.drawLLSprite(cc.xcon(224), 0, cc.xcon(32), cc.ycon(32), MainAssets.zero); break;
		}
		
		switch(digit4){
		case 1: batcher.drawLLSprite(cc.xcon(256), 0, cc.xcon(32), cc.ycon(32), MainAssets.one); break;
		case 2: batcher.drawLLSprite(cc.xcon(256), 0, cc.xcon(32), cc.ycon(32), MainAssets.two); break;
		case 3: batcher.drawLLSprite(cc.xcon(256), 0, cc.xcon(32), cc.ycon(32), MainAssets.three); break;
		case 4: batcher.drawLLSprite(cc.xcon(256), 0, cc.xcon(32), cc.ycon(32), MainAssets.four); break;
		case 5: batcher.drawLLSprite(cc.xcon(256), 0, cc.xcon(32), cc.ycon(32), MainAssets.five); break;
		case 6: batcher.drawLLSprite(cc.xcon(256), 0, cc.xcon(32), cc.ycon(32), MainAssets.six); break;
		case 7: batcher.drawLLSprite(cc.xcon(256), 0, cc.xcon(32), cc.ycon(32), MainAssets.seven); break;
		case 8: batcher.drawLLSprite(cc.xcon(256), 0, cc.xcon(32), cc.ycon(32), MainAssets.eight); break;
		case 9: batcher.drawLLSprite(cc.xcon(256), 0, cc.xcon(32), cc.ycon(32), MainAssets.nine); break;
		case 0: batcher.drawLLSprite(cc.xcon(256), 0, cc.xcon(32), cc.ycon(32), MainAssets.zero); break;
		}
		
		switch(digit5){
		case 1: batcher.drawLLSprite(cc.xcon(288), 0, cc.xcon(32), cc.ycon(32), MainAssets.one); break;
		case 2: batcher.drawLLSprite(cc.xcon(288), 0, cc.xcon(32), cc.ycon(32), MainAssets.two); break;
		case 3: batcher.drawLLSprite(cc.xcon(288), 0, cc.xcon(32), cc.ycon(32), MainAssets.three); break;
		case 4: batcher.drawLLSprite(cc.xcon(288), 0, cc.xcon(32), cc.ycon(32), MainAssets.four); break;
		case 5: batcher.drawLLSprite(cc.xcon(288), 0, cc.xcon(32), cc.ycon(32), MainAssets.five); break;
		case 6: batcher.drawLLSprite(cc.xcon(288), 0, cc.xcon(32), cc.ycon(32), MainAssets.six); break;
		case 7: batcher.drawLLSprite(cc.xcon(288), 0, cc.xcon(32), cc.ycon(32), MainAssets.seven); break;
		case 8: batcher.drawLLSprite(cc.xcon(288), 0, cc.xcon(32), cc.ycon(32), MainAssets.eight); break;
		case 9: batcher.drawLLSprite(cc.xcon(288), 0, cc.xcon(32), cc.ycon(32), MainAssets.nine); break;
		case 0: batcher.drawLLSprite(cc.xcon(288), 0, cc.xcon(32), cc.ycon(32), MainAssets.zero); break;
		}
		
		//Draw Powerup Bar
		if(powerups.getState() != PowerupManager.ID_NULL){
			batcher.drawLLSprite(0, cc.ycon(480 - 32), cc.xcon(320), cc.ycon(32), MainAssets.widered);
			batcher.drawLLSprite((int) cc.xcon(320 * powerups.getPercentLeft()) ,
				cc.ycon(480 - 32), cc.xcon(320), cc.ycon(32), MainAssets.widewhite);
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
