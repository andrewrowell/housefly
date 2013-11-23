package com.andrewjrowell.fly;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.andrewjrowell.fly.assets.MainAssets;
import com.andrewjrowell.fly.assets.PreAssets;
import com.andrewjrowell.fly.screens.LoadingScreen;
import com.andrewjrowell.framework.Screen;
import com.andrewjrowell.framework.gl.GLGame;

public class MainActivity extends GLGame {
	boolean firstTimeCreate = true;
	
	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}
	
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
