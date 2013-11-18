package com.andrewjrowell.framework.interfaces;

import javax.microedition.khronos.opengles.GL10;

import javax.microedition.khronos.egl.EGLConfig;

public interface Renderer {
	public void onSurfaceCreated(GL10 gl, EGLConfig config);
	public void onSurfaceChanged(GL10 gl, int width, int height);
	public void onDrawFrame(GL10 gl);
}
