package com.andrewjrowell.fly.entities;

import java.util.ArrayList;

import com.andrewjrowell.fly.assets.MainAssets;
import com.andrewjrowell.framework.math.Circle;
import com.andrewjrowell.framework.math.OverlapTester;

/**
 * <p>Encapsulates the code that manages the powerups away from
 * the GamePlayScreen</p>
 * 
 * @author andrew
 *
 */

public class PowerupManager {

	public static final int ID_NULL = 0;
	public static final int SPEED_ID = 1;
	public static final int SLOW_ID = 2;
	public static final int SWITCHDIR_ID = 3;
	
	public static final int SPEED_DURATION = 10;
	public static final int SLOW_DURATION = 15;
	public static final int SWITCHDIR_DURATION = 10;
	
	final float WORLD_WIDTH = 320; // width of game world in pixels
	final float WORLD_HEIGHT = 480; // height of game world in pixels
	
	ArrayList<Powerup> powerups;
	
	float powerupcounter; // Countdown for powerups to appear
	
	int powerupState; // What kind of powerup is active
	float powerupTimeLeft; // Time until powerup expires
	float powerupDuration; // Length of currently active powerup
	
	public PowerupManager(){
		powerups = new ArrayList<Powerup>();
		powerupcounter = 20;
		powerupState = ID_NULL;
		powerupTimeLeft = 0;
	}
	
	/**
	 * <p>Used by rendering and other game entities to affect
	 * game world</p>
	 * 
	 * @return id of currently active powerup
	 */
	public int getState(){
		return powerupState;
	}
	
	/**
	 * <p>Updates the positions of the powerups and adds a
	 * new one if necessary.</p>
	 * 
	 * @param deltaTime time since last update
	 * @param pace speed modifier
	 */
	public void update(float deltaTime, float pace){
		for(Powerup p : powerups){
			p.update(deltaTime, pace);
		}
		
		// Decrease powerup time
		powerupTimeLeft -= deltaTime;
		if(powerupTimeLeft <= 0){
			powerupState = ID_NULL;
		}
				
		// Spawn a powerup every 30 seconds
		powerupcounter += deltaTime * pace / 48f;
		if(powerupcounter >= 30){
			int newid = (int) (Math.random() * 3) + 1;
			Powerup pu = new Powerup(WORLD_WIDTH, WORLD_HEIGHT, newid);
			powerups.add(pu);
			powerupcounter = 0;
		}
	}
	
	/**
	 * <p>Does a bounds check to see if the fly has hit
	 * any powerups.</p>
	 * 
	 * @param fly Player's Fly
	 */
	public void checkCollisions(PlayerFly fly){
		ArrayList<Powerup> powerups2 = new ArrayList<Powerup>();
		
		for(Powerup p : powerups){
			if(p.type == SPEED_ID && OverlapTester.overlapCircleRectangle(
					new Circle(p.x, p.y, (int) (WORLD_WIDTH * 28.0 / 320.0)),
					fly.getBounds())){
				powerupState = PowerupManager.SPEED_ID;
				powerupTimeLeft = PowerupManager.SPEED_DURATION;
				powerupDuration = PowerupManager.SPEED_DURATION;
				MainAssets.reloadSpeedSound();
				MainAssets.speed.play();
				p.remove = true;
			}
			if(p.type == SLOW_ID && OverlapTester.overlapCircleRectangle(
					new Circle(p.x, p.y, 28),
					fly.getBounds())){
				powerupState = PowerupManager.SLOW_ID;
				powerupTimeLeft = PowerupManager.SLOW_DURATION;
				powerupDuration = PowerupManager.SLOW_DURATION;
				MainAssets.reloadSlowSound();
				MainAssets.slow.play();
				p.remove = true;
			}
			if(p.type == SWITCHDIR_ID && OverlapTester.overlapCircleRectangle(
					new Circle(p.x, p.y, (28)),
					fly.getBounds())){
				powerupState = PowerupManager.SWITCHDIR_ID;
				powerupTimeLeft = PowerupManager.SWITCHDIR_DURATION;
				powerupDuration = PowerupManager.SWITCHDIR_DURATION;
				MainAssets.reloadSwitchDirSound();
				MainAssets.switchdir.play();
				p.remove = true;
			}
		}
		
		for(Powerup p : powerups){
			if(p.remove != true){
				powerups2.add(p);
			}
		}
		
		powerups = powerups2;
	}
	
	/**
	 * <p>Lets rendering code access individual powerups</p>
	 * 
	 * @return ArrayList of the individual Powerups
	 */
	public ArrayList<Powerup> getPowerups(){
		return powerups;
	}
	
	public float getPercentLeft(){
		return (powerupTimeLeft / powerupDuration);
	}
}
