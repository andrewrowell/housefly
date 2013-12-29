package com.andrewjrowell.fly.entities;

import java.util.ArrayList;

import com.andrewjrowell.framework.math.Circle;
import com.andrewjrowell.framework.math.OverlapTester;

/**
 * <p>Encapsulates the code that manages the predators away from
 * the GamePlayScreen</p> 
 * 
 * @author andrew
 *
 */

public class PredatorManager {
	ArrayList<Predator> predators;
	
	final float WORLD_WIDTH;
	final float WORLD_HEIGHT;
	
	float delay; // affects frequency of spawning
	// as delay gets lower, predators appear more often
	
	float counter; // Countdown for predator spawning
	
	public PredatorManager(float worldwidth, float worldheight){
		WORLD_WIDTH = worldwidth;
		WORLD_HEIGHT = worldheight;
		predators = new ArrayList<Predator>();
		counter = 0;
		delay = 3;
	}
	
	/**
	 * <p>Updates the positions of the predators and adds a new
	 * one if necessary</p>
	 * 
	 * @param deltaTime time since last update
	 * @param xpace lateral speed modifier
	 * @param ypace vertical speed modifier
	 */
	public void update(float deltaTime, float xpace, float ypace){
		counter += deltaTime * ypace / 48;
		// Divide by 48 so at normal pace, counter
		// represents time in seconds
		
		if(counter >= 6){
			Predator newPred = new Predator(WORLD_WIDTH, WORLD_HEIGHT);
			predators.add(newPred);
			counter = 3 - delay;
			delay *= 0.9;
		}
		
		for(Predator p : predators){
			p.update(deltaTime, xpace, ypace);
		}
	}

	/**
	 * <p>Does a bounds check to see if the fly has hit any
	 * predators</p>
	 * 
	 * @param fly Player's Fly
	 * @return true if eaten by predator
	 */
	public boolean collisionCheck(PlayerFly fly) {
		for(Predator p : predators){
			if(p.type == 1 && OverlapTester.overlapCircleRectangle(
					new Circle(p.x, p.y, 28),
					fly.getBounds())){
				return true;
			}
			if(p.type == 2 && OverlapTester.overlapCircleRectangle(
					new Circle(p.x, p.y, 56),
					fly.getBounds())){
				return true;
			}
			if(p.type == 3 && OverlapTester.overlapCircleRectangle(
					new Circle(p.x, p.y, 84),
					fly.getBounds())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <p> lets rendering code access individual predators</p>
	 * @return ArrayList of the individual predators
	 */
	public ArrayList<Predator> getPredators(){
		return predators;
	}
}
