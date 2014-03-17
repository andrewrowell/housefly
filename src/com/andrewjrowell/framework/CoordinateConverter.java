package com.andrewjrowell.framework;

/**
 * <p>Converts coordinate locations from the original resolution
 * (320 pixels wide by 480 pixels high) to whatever new resolution
 * I want the game to be rendered in. </p>
 * 
 * <p>This allows me to set the locations of objects in the game as if
 * the logic were still working in a 320x480 world, while the
 * actual rendering may be working with a higher or lower resolution.</p>
 * 
 * @author andrew
 *
 */

public class CoordinateConverter {
	final float WORLD_WIDTH; // Width of game world in pixels
	final float WORLD_HEIGHT; // Height of game world in pixels
	final float ORIGINAL_WIDTH = 320; // Width of game as it was originally coded
	final float ORIGINAL_HEIGHT = 480; // Height of game as it was originally coded
	final float X_RATIO; // Multiply by this to convert x coordinates
	final float Y_RATIO; // multiply by this to convert y coordinates
	
	/**
	 * 
	 * @param WORLD_WIDTH Width of game world in pixels
	 * @param WORLD_HEIGHT Height of game world in pixels
	 */
	public CoordinateConverter(float WORLD_WIDTH, float WORLD_HEIGHT){
			this.WORLD_WIDTH = WORLD_WIDTH;
			this.WORLD_HEIGHT = WORLD_HEIGHT;
			this.X_RATIO = WORLD_WIDTH / ORIGINAL_WIDTH;
			this.Y_RATIO = WORLD_HEIGHT / ORIGINAL_HEIGHT;
	}
	
	/**  convert x coordinate
	 * 
	 * @param x x coordinate according to a 320 x 480 grid
	 * @return x coordinate according to the actual resolution
	 */
	public float xcon(float x){
		return (x * X_RATIO);
	}
	
	/** convert x coordinate
	 * 
	 * @param x x coordinate according to a 320 x 480 grid
	 * @return x coordinate according to the actual resolution
	 */
	public int xcon(int x){
		return (int) (((float) x) * X_RATIO);
	}
	
	/** convert x coordinate
	 * 
	 * @param x x coordinate according to a 320 x 480 grid
	 * @return x coordinate according to the actual resolution
	 */
	public double xcon(double x){
		return (x * X_RATIO);
	}
	
	/** convert y coordinate
	 * 
	 * @param y y coordinate according to a 320 x 480 grid
	 * @return y coordinate according to the actual resolution
	 */
	public float ycon(float y){
		return (y * Y_RATIO);
	}
	
	/** convert y coordinate
	 * 
	 * @param y y coordinate according to a 320 x 480 grid
	 * @return y coordinate according to the actual resolution
	 */
	public int ycon(int y){
		return (int) (((float) y) * Y_RATIO);
	}
	
	/** convert y coordinate
	 * 
	 * @param y y coordinate according to a 320 x 480 grid
	 * @return y coordinate according to the actual resolution
	 */
	public double ycon(double y){
		return (y * Y_RATIO);
	}
}
