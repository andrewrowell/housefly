package com.andrewjrowell.fly.entities;

import com.andrewjrowell.fly.assets.MainAssets;
import com.andrewjrowell.framework.math.Rectangle;

/**
 * 
 * <p>Holds information about the state of the player's fly</p>
 * 
 * @author andrew
 *
 */

public class PlayerFly {
	
	final float WORLD_WIDTH = 320; // width of game world in pixels
	final float WORLD_HEIGHT = 480; // height of game world in pixels
	final int FLY_Y_POSITION = 64; // distance from bottom of world
	
	float flyPosition; // X Position of fly
	int flySize; // size of fly in pixels
	boolean buzzing; // should buzzing sound be playing?
	
	public PlayerFly(){
		flyPosition = WORLD_WIDTH / 2;
		flySize = 16;
		buzzing = false;
	}
	
	/**
	 * <p>Gets collision bounds of the fly</p>
	 * @return bounds
	 */
	public Rectangle getBounds(){
		return new Rectangle(flyPosition - (flySize/2),64 + (flySize/2),flySize, flySize);
	}
	
	/**
	 * <p>Make fly one pixel larger</p>
	 */
	public void grow(){
		flySize++;
	}
	
	/**
	 * <p>move with a variable that affects how quickly the
	 * fly moves, and in what direction.</p>
	 * 
	 * @param variation how fast the fly should move
	 * @param deltaTime time passed since last update
	 */
	public void move(float variation, float deltaTime){
		if(Math.abs(variation) > 0.5){
			flyPosition -= (int) (variation * deltaTime * 32 * 2);
			if(!buzzing && Math.abs(variation) > 3){
				buzzing = true;
				MainAssets.buzz.play(1.0f);
			}
		} else {
			buzzing = false;
		}
		
		// Don't let fly go off screen
		if(flyPosition <= 0){
			flyPosition = 0;
		}
		if(flyPosition >= WORLD_WIDTH){
			flyPosition = WORLD_WIDTH;
		}
	}
	
	/**
	 * <p>Lets game world know if fly is moving fast enough to
	 * make a buzzing sound</p>
	 * 
	 * @return true if the fly is making a sound
	 */
	public boolean isBuzzing(){
		return buzzing;
	}
	
	/**
	 * <p>gets the x position of the fly (changes)</p>
	 * 
	 * @return position of fly on x axis
	 */
	public float getPosition(){
		return flyPosition;
	}
	
	/**
	 * <p>gets the y position of the fly (constant)</p>
	 * 
	 * @return fly's distance from the bottom of the game world
	 */
	public float getYPosition(){
		return (float) (FLY_Y_POSITION + flySize * WORLD_HEIGHT / 480.0);
	}
	
	/**
	 * <p>gets the size of the fly</p>
	 * 
	 * @return size of fly in pixels
	 */
	public int getFlySize(){
		return flySize;
	}
}
