package com.andrewjrowell.fly.entities;

/**
 * <p>Represents a piece of rotten food the fly eats to gain size and points</p>
 * 
* @author Andrew Rowell
* @version 1.0
*/


public class Rotten {
	public int type; // Only affects what it looks like
	
	public float x,y; // Position of the rotten in the game world
	
	// Indicates if the fly has eaten the rotten so the
	// GamePlayScreen can delete it
	public boolean eaten; 
	
	/**
	 * 
	 * @param screenwidth width of the game world in pixels
	 * @param screenheight height of the game world in pixels
	 */
	public Rotten(float screenwidth, float screenheight){
		type = (int) (Math.random() * 16) + 1;
		eaten = false;
		x = (float) (Math.random() * screenwidth);
		y = screenheight;
		System.out.println("New Rotten at: (" + x + ',' + y + ")");
	}
	/**
	 * Updates the position of the Rotten
	 * 
	 * @param deltaTime time since last updat()
	 * @param pace affects the speed at which the rotten moves down
	 */
	public void update(float deltaTime, float pace){
		y -= deltaTime * pace;
	}
}
