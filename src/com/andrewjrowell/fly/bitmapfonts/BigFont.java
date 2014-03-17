package com.andrewjrowell.fly.bitmapfonts;

import com.andrewjrowell.fly.assets.MainAssets;
import com.andrewjrowell.framework.gl.SpriteBatcher;

/**
 * Stores the width and height in pixels needed to
 * make a font the same proportionally to the screen
 * on any screen resolution with any physical
 * dimensions.
 * 
 * @author andrew
 *
 */

public class BigFont {
	final int WIDTH; // width that characters will be rendered, in pixels
	final int HEIGHT; // height that characters will be rendered, in pixels
	
	/**
	 * 
	 * @param worldwidth width of game world in pixels
	 * @param worldheight height of game world in pixels
	 */
	public BigFont(float worldwidth, float worldheight){
		WIDTH = (int) (worldwidth * 48.0 / 320.0);
		HEIGHT = (int) (worldheight * 64.0 / 480.0);
	}
	
	/**
	 * 
	 * @return width of font in pixels
	 */
	public int getWidth(){ return WIDTH;}
	
	/**
	 * 
	 * @return height of font in pixels
	 */
	public int getHeight(){ return HEIGHT;}
	
	
	/**
	 * <p>Render a string using a SpriteBatcher.</p>
	 * <p>Just iterates through each character in the string
	 * and calls drawChar() to render individual characters
	 * of the string, using the index of the current character
	 * to calculate the X position of the character to be
	 * renderes</p> 
	 * 
	 * @param locationx X position of first character
	 * @param locationy Y position of first character
	 * @param content string of text to render
	 * @param batcher SpriteBatcher that renders the characters
	 */
	public void drawString(int locationx, int locationy, String content, SpriteBatcher batcher){
		for(int i = 0; i < content.length(); i++){
			drawChar(locationx + (i * WIDTH), locationy, content.charAt(i), batcher);
		}
	}
	
	/**
	 * <p>Renders individual characters using a SpriteBatcher</p>
	 * <p>Used by BigFont only, screens should use drawString() to
	 * render individual characters</p>
	 * 
	 * @param locationx X position of character
	 * @param locationy Y position of character
	 * @param content character to be rendered
	 * @param batcher SpriteBatcher that renders the character
	 */
	private void drawChar(int locationx, int locationy, char content, SpriteBatcher batcher){
		switch(content){
			case 'A': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.A); break;
			case 'B': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.B); break;
			case 'C': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.C); break;
			case 'D': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.D); break;
			case 'E': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.E); break;
			case 'F': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.F); break;
			case 'G': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.G); break;
			case 'H': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.H); break;
			case 'I': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.I); break;
			case 'J': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.J); break;
			case 'K': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.K); break;
			case 'L': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.L); break;
			case 'M': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.M); break;
			case 'N': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.N); break;
			case 'O': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.O); break;
			case 'P': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.P); break;
			case 'Q': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.Q); break;
			case 'R': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.R); break;
			case 'S': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.S); break;
			case 'T': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.T); break;
			case 'U': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.U); break;
			case 'V': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.V); break;
			case 'W': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.W); break;
			case 'X': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.X); break;
			case 'Y': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.Y); break;
			case 'Z': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.Z); break;
			case '1': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.ONE); break;
			case '2': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.TWO); break;
			case '3': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.THREE); break;
			case '4': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.FOUR); break;
			case '5': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.FIVE); break;
			case '6': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.SIX); break;
			case '7': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.SEVEN); break;
			case '8': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.EIGHT); break;
			case '9': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.NINE); break;
			case '0': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.ZERO); break;
			case '?': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.QUESTION_MARK); break;
			case '!': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.EXCLAMATION_POINT); break;
			case '\'': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.APOSTROPHE); break;
			case '.': batcher.drawLLSprite(locationx, locationy,
					WIDTH, HEIGHT, MainAssets.PERIOD); break;
		}
	}
}
