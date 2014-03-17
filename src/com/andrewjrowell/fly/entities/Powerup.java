package com.andrewjrowell.fly.entities;

/**
 * <p>Represents a speed powerup in a {@link GamePlayScreen}
 * which makes the fly move faster.</p>
 * 
* @author Andrew Rowell
* @version 1.0
*/

public class Powerup {
	
	//TODO create getters and setters for this class
	public float x, y; // Position of powerup on screen
	public int type;
	
	// Marked true if something has happened (off screen, eaten by fly)
	// that would cause the powerup to no longer be visible to the player
	public boolean remove;
	
	/**
	 * 
	 * @param WORLD_WIDTH Width of the game world in pixels
	 * @param WORLD_HEIGHT Height of the game world in pixels
	 * @param id unique number to identify type of powerup
	 */
	public Powerup(float WORLD_WIDTH, float WORLD_HEIGHT, int id){
		type = id;
		
		// Set the powerup's inital position to a random point
		// at the top of the screen
		x = (int) (Math.random() * (WORLD_WIDTH - 32)) + 16;
		y = 480;
		
		remove = false;
	}
	
	/**
	 * 
	 * @param deltaTime Amount of time since last update
	 * @param pace Speed at which the powerup moves
	 */
	public void update(float deltaTime, float pace){
		y -= deltaTime * pace;
		if(y <= 0){
			remove = true;
		}
	}
}
