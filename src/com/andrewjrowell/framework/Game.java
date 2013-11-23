package com.andrewjrowell.framework;

import com.andrewjrowell.framework.audio.Audio;
import com.andrewjrowell.framework.diskio.FileIO;
import com.andrewjrowell.framework.graphics.Graphics;
import com.andrewjrowell.framework.input.Input;

public interface Game {
	public Input getInput();
	public FileIO getFileIO();
	public Graphics getGraphics();
	public Audio getAudio();
	public void setScreen(Screen screen);
	public Screen getCurrentScreen();
	public Screen getStartScreen();
}
