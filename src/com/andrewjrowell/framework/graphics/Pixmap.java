package com.andrewjrowell.framework.graphics;

import com.andrewjrowell.framework.graphics.Graphics.PixmapFormat;

public interface Pixmap {
	public int getWidth();
	public int getHeight();
	public PixmapFormat getFormat();
	public void dispose();
}
