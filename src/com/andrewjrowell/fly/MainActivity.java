package com.andrewjrowell.fly;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.andrewjrowell.fly.assets.MainAssets;
import com.andrewjrowell.fly.assets.PreAssets;
import com.andrewjrowell.fly.screens.LoadingScreen;
import com.andrewjrowell.framework.Screen;
import com.andrewjrowell.framework.gl.GLGame;

/**
 * <p>Main Activity of Housefly which initiates the LoadingScreen.</p>
 * 
 * <p>Originally by Mario Zechner, in "Beginning Android Games (1st Edition)".</p>
 * 
 * <p>I (Andrew Rowell) modified it so that a loading screen would be displayed
 * while the game's assets were loaded.</p>
 * 
 * <p>Zechner's original version did not have a loading screen, which loaded all
 * of the game's assets before displaying anything to the user. If the game
 * had a significant amount of assets (sound, music, graphics) then it would
 * appear to the user that the game had frozen, before finally opening up the
 * main menu.</p>
 * 
* @author Mario Zechner
* @author Andrew Rowell
* @version 1.1
*/

public class MainActivity extends GLGame {
	boolean firstTimeCreate = true;
	
	
	/**
	 * <p>Used by GLGame to choose what screen to start in.</p>
	 * 
	 * <p>Currently picks the special Screen {@link LoadingScreen} which uses
	 * Assets from a special Asset set called PreAssets that takes
	 * minimal time to load so that there is no lag between the user
	 * launching the application and the user seeing output from the
	 * game.</p>
	 * 
	 * @return A special {@link Screen} that has minimal required Assets
	 */
	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}

	/**
	 * <p>Determines what Assets need to be loaded, and loads/reloads them.</p>
	 * 
	 * <p>Calls {@link GLGame#onSurfaceCreated(GL10, EGLConfig)}<p>
	 * 
	 * @param gl a GL10 that gets used by {@link GLGame#onSurfaceCreated(GL10, EGLConfig)}
	 * @param config an EGLConfig that gets used by {@link GLGame#onSurfaceCreated(GL10, EGLConfig)} 
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config){
		super.onSurfaceCreated(gl, config);
		if(firstTimeCreate){
			PreAssets.load(this);
			firstTimeCreate = false;
		} else {
			MainAssets.reload();
		}
	}
}
