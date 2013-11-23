package com.andrewjrowell.fly.assets;

import com.andrewjrowell.framework.audio.Audio;
import com.andrewjrowell.framework.audio.Music;
import com.andrewjrowell.framework.audio.Sound;
import com.andrewjrowell.framework.gl.GLGame;
import com.andrewjrowell.framework.gl.Texture;
import com.andrewjrowell.framework.gl.TextureRegion;

public class MainAssets {
	static boolean loaded = false;
	
	public static Audio audio;
	public static Texture imagemap;
	public static Sound click;
	
	public static TextureRegion background;
	public static TextureRegion white;
	public static TextureRegion red;
	public static TextureRegion tallwhite;
	public static TextureRegion tallred;
	public static TextureRegion widewhite;
	public static TextureRegion widered;
	public static TextureRegion arrow;
	public static TextureRegion uparrow;
	public static TextureRegion downarrow;
	public static TextureRegion phone;
	public static TextureRegion fly;
	public static TextureRegion spider;
	public static TextureRegion lizard;
	public static TextureRegion duck;
	
	public static TextureRegion speedpowerup;
	
	public static TextureRegion rotten1;
	public static TextureRegion rotten2;
	public static TextureRegion rotten3;
	public static TextureRegion rotten4;
	public static TextureRegion rotten5;
	public static TextureRegion rotten6;
	public static TextureRegion rotten7;
	public static TextureRegion rotten8;
	public static TextureRegion rotten9;
	public static TextureRegion rotten10;
	public static TextureRegion rotten11;
	public static TextureRegion rotten12;
	public static TextureRegion rotten13;
	public static TextureRegion rotten14;
	public static TextureRegion rotten15;
	public static TextureRegion rotten16;
	
	public static TextureRegion A;
	public static TextureRegion B;
	public static TextureRegion C;
	public static TextureRegion D;
	public static TextureRegion E;
	public static TextureRegion F;
	public static TextureRegion G;
	public static TextureRegion H;
	public static TextureRegion I;
	public static TextureRegion J;
	public static TextureRegion K;
	public static TextureRegion L;
	public static TextureRegion M;
	public static TextureRegion N;
	public static TextureRegion O;
	public static TextureRegion P;
	public static TextureRegion Q;
	public static TextureRegion R;
	public static TextureRegion S;
	public static TextureRegion T;
	public static TextureRegion U;
	public static TextureRegion V;
	public static TextureRegion W;
	public static TextureRegion X;
	public static TextureRegion Y;
	public static TextureRegion Z;
	public static TextureRegion ONE;
	public static TextureRegion TWO;
	public static TextureRegion THREE;
	public static TextureRegion FOUR;
	public static TextureRegion FIVE;
	public static TextureRegion SIX;
	public static TextureRegion SEVEN;
	public static TextureRegion EIGHT;
	public static TextureRegion NINE;
	public static TextureRegion ZERO;
	public static TextureRegion QUESTION_MARK;
	public static TextureRegion EXCLAMATION_POINT;
	public static TextureRegion APOSTROPHE;
	public static TextureRegion PERIOD;
	
	public static TextureRegion a;
	public static TextureRegion b;
	public static TextureRegion c;
	public static TextureRegion d;
	public static TextureRegion e;
	public static TextureRegion f;
	public static TextureRegion g;
	public static TextureRegion h;
	public static TextureRegion i;
	public static TextureRegion j;
	public static TextureRegion k;
	public static TextureRegion l;
	public static TextureRegion m;
	public static TextureRegion n;
	public static TextureRegion o;
	public static TextureRegion p;
	public static TextureRegion q;
	public static TextureRegion r;
	public static TextureRegion s;
	public static TextureRegion t;
	public static TextureRegion u;
	public static TextureRegion v;
	public static TextureRegion w;
	public static TextureRegion x;
	public static TextureRegion y;
	public static TextureRegion z;
	public static TextureRegion one;
	public static TextureRegion two;
	public static TextureRegion three;
	public static TextureRegion four;
	public static TextureRegion five;
	public static TextureRegion six;
	public static TextureRegion seven;
	public static TextureRegion eight;
	public static TextureRegion nine;
	public static TextureRegion zero;
	public static TextureRegion question_mark;
	public static TextureRegion exclamation_point;
	public static TextureRegion apostrophe;
	public static TextureRegion period;
	
	public static Sound slurp;
	public static Sound buzz;
	public static Music menumusic;
	public static Sound introchords;
	public static Sound majorchord;
	public static Sound minorchord;
	public static Sound tiltright;
	public static Sound tiltleft;
	public static Music speed;
	public static Sound crunch;
	
	
	public static void load(GLGame game){
		loaded = false;
		audio = game.getAudio();
		imagemap = new Texture(game, "imagemap.png");
		click = audio.newSound("click.wav");
		slurp = audio.newSound("slurp.wav");
		buzz = audio.newSound("buzz.wav");
		menumusic = audio.newMusic("menumusic.wav");
		introchords = audio.newSound("intromusic.wav");
		majorchord = audio.newSound("highscore.wav");
		minorchord = audio.newSound("nohighscore.wav");
		tiltright = audio.newSound("goingup.wav");
		tiltleft = audio.newSound("goingdown.wav");
		speed = audio.newMusic("speedsound.wav");
		crunch = audio.newSound("crunch.wav");
		
		speedpowerup = new TextureRegion(imagemap,0,544,32,32);
		
		 A = new TextureRegion(imagemap, 0, 0 , 48, 64);
		 B = new TextureRegion(imagemap, 48, 0 , 48, 64);
		 C = new TextureRegion(imagemap, 96, 0 , 48, 64);
		 D = new TextureRegion(imagemap, 144, 0 , 48, 64);
		 E = new TextureRegion(imagemap, 192, 0 , 48, 64);
		 F = new TextureRegion(imagemap, 240, 0 , 48, 64);
		 G = new TextureRegion(imagemap, 288, 0 , 48, 64);
		 H = new TextureRegion(imagemap, 336, 0 , 48, 64);
		 I = new TextureRegion(imagemap, 0, 64 , 48, 64);
		 J = new TextureRegion(imagemap, 48, 64 , 48, 64);
		 K = new TextureRegion(imagemap, 96, 64 , 48, 64);
		 L = new TextureRegion(imagemap, 144, 64 , 48, 64);
		 M = new TextureRegion(imagemap, 192, 64 , 48, 64);
		 N = new TextureRegion(imagemap, 240, 64 , 48, 64);
		 O = new TextureRegion(imagemap, 288, 64 , 48, 64);
		 P = new TextureRegion(imagemap, 336, 64 , 48, 64);
		 Q = new TextureRegion(imagemap, 0, 128 , 48, 64);
		 R = new TextureRegion(imagemap, 48, 128 , 48, 64);
		 S = new TextureRegion(imagemap, 96, 128 , 48, 64);
		 T = new TextureRegion(imagemap, 144, 128 , 48, 64);
		 U = new TextureRegion(imagemap, 192, 128 , 48, 64);
		 V = new TextureRegion(imagemap, 240, 128 , 48, 64);
		 W = new TextureRegion(imagemap, 288, 128 , 48, 64);
		 X = new TextureRegion(imagemap, 336, 128 , 48, 64);
		 Y = new TextureRegion(imagemap, 0, 192 , 48, 64);
		 Z = new TextureRegion(imagemap, 48, 192 , 48, 64);
		 ZERO = new TextureRegion(imagemap, 96, 192 , 48, 64);
		 ONE = new TextureRegion(imagemap, 144, 192 , 48, 64);
		 TWO = new TextureRegion(imagemap, 192, 192 , 48, 64);
		 THREE = new TextureRegion(imagemap, 240, 192 , 48, 64);
		 FOUR = new TextureRegion(imagemap, 288, 192 , 48, 64);
		 FIVE = new TextureRegion(imagemap, 336, 192 , 48, 64);
		 SIX = new TextureRegion(imagemap, 0, 256 , 48, 64);
		 SEVEN = new TextureRegion(imagemap, 48, 256 , 48, 64);
		 EIGHT = new TextureRegion(imagemap, 96, 256 , 48, 64);
		 NINE = new TextureRegion(imagemap, 144, 256 , 48, 64);
		 QUESTION_MARK = new TextureRegion(imagemap, 192, 256 , 48, 64);
		 EXCLAMATION_POINT = new TextureRegion(imagemap, 240, 256 , 48, 64);
		 APOSTROPHE = new TextureRegion(imagemap, 288, 256 , 48, 64);
		 PERIOD = new TextureRegion(imagemap, 336, 256 , 48, 64);
		 
		 a = new TextureRegion(imagemap, 0, 320, 32, 32);
		 b = new TextureRegion(imagemap, 32, 320, 32, 32);
		 c = new TextureRegion(imagemap, 64, 320, 32, 32);
		 d = new TextureRegion(imagemap, 96, 320, 32, 32);
		 e = new TextureRegion(imagemap, 128, 320, 32, 32);
		 f = new TextureRegion(imagemap, 160, 320, 32, 32);
		 g = new TextureRegion(imagemap, 192, 320, 32, 32);
		 h = new TextureRegion(imagemap, 224, 320, 32, 32);
		 i = new TextureRegion(imagemap, 0, 352, 32, 32);
		 j = new TextureRegion(imagemap, 32, 352, 32, 32);
		 k = new TextureRegion(imagemap, 64, 352, 32, 32);
		 l = new TextureRegion(imagemap, 96, 352, 32, 32);
		 m = new TextureRegion(imagemap, 128, 352, 32, 32);
		 n = new TextureRegion(imagemap, 160, 352, 32, 32);
		 o = new TextureRegion(imagemap, 192, 352, 32, 32);
		 p = new TextureRegion(imagemap, 224, 352, 32, 32);
		 q = new TextureRegion(imagemap, 0, 384, 32, 32);
		 r = new TextureRegion(imagemap, 32, 384, 32, 32);
		 s = new TextureRegion(imagemap, 64, 384, 32, 32);
		 t = new TextureRegion(imagemap, 96, 384, 32, 32);
		 u = new TextureRegion(imagemap, 128, 384, 32, 32);
		 v = new TextureRegion(imagemap, 160, 384, 32, 32);
		 w = new TextureRegion(imagemap, 192, 384, 32, 32);
		 x = new TextureRegion(imagemap, 224, 384, 32, 32);
		 y = new TextureRegion(imagemap, 0, 416, 32, 32);
		 z = new TextureRegion(imagemap, 32, 416, 32, 32);
		 zero = new TextureRegion(imagemap, 64, 416, 32, 32);
		 one = new TextureRegion(imagemap, 96, 416, 32, 32);
		 two = new TextureRegion(imagemap, 128, 416, 32, 32);
		 three = new TextureRegion(imagemap, 160, 416, 32, 32);
		 four = new TextureRegion(imagemap, 192, 416, 32, 32);
		 five = new TextureRegion(imagemap, 224, 416, 32, 32);
		 six = new TextureRegion(imagemap, 0, 448, 32, 32);
		 seven = new TextureRegion(imagemap, 32, 448, 32, 32);
		 eight = new TextureRegion(imagemap, 64, 448, 32, 32);
		 nine = new TextureRegion(imagemap, 96, 448, 32, 32);
		 question_mark = new TextureRegion(imagemap, 128, 448, 32, 32);
		 exclamation_point = new TextureRegion(imagemap, 160, 448, 32, 32);
		 apostrophe = new TextureRegion(imagemap, 192, 448, 32, 32);
		 period = new TextureRegion(imagemap, 224, 448, 32, 32);
		 
		 white = new TextureRegion(imagemap, 256, 512, 128, 128);
		 red = new TextureRegion(imagemap, 128, 544, 128, 128);
		 tallwhite = new TextureRegion(imagemap, 256, 512, 32, 128);
		 tallred = new TextureRegion(imagemap, 128, 544, 32, 128);
		 widewhite = new TextureRegion(imagemap, 256, 512, 128, 64);
		 widered = new TextureRegion(imagemap, 128, 544, 128, 64);
		 background = new TextureRegion(imagemap, 640, 0, 320, 320);
		 arrow = new TextureRegion(imagemap, 256, 448, 128, 64);
		 phone = new TextureRegion(imagemap, 512, 192, 128, 192);
		 fly = new TextureRegion(imagemap, 256, 320, 128, 128);
		 spider = new TextureRegion(imagemap, 384, 64, 64, 64);
		 lizard = new TextureRegion(imagemap, 384, 192, 128, 128);
		 duck = new TextureRegion(imagemap, 448, 0, 192, 192);
		 
		 rotten1 = new TextureRegion(imagemap, 0, 480, 32, 32);
		 rotten2 = new TextureRegion(imagemap, 1 * 32, 480, 32, 32);
		 rotten3 = new TextureRegion(imagemap, 2 * 32, 480, 32, 32);
		 rotten4 = new TextureRegion(imagemap, 3 * 32, 480, 32, 32);
		 rotten5 = new TextureRegion(imagemap, 4 * 32, 480, 32, 32);
		 rotten6 = new TextureRegion(imagemap, 5 * 32, 480, 32, 32);
		 rotten7 = new TextureRegion(imagemap, 6 * 32, 480, 32, 32);
		 rotten8 = new TextureRegion(imagemap, 7 * 32, 480, 32, 32);
		 rotten9 = new TextureRegion(imagemap, 0 * 32, 512, 32, 32);
		 rotten10 = new TextureRegion(imagemap, 1 * 32, 512, 32, 32);
		 rotten11 = new TextureRegion(imagemap, 2 * 32, 512, 32, 32);
		 rotten12 = new TextureRegion(imagemap, 3 * 32, 512, 32, 32);
		 rotten13 = new TextureRegion(imagemap, 4 * 32, 512, 32, 32);
		 rotten14 = new TextureRegion(imagemap, 5 * 32, 512, 32, 32);
		 rotten15 = new TextureRegion(imagemap, 6 * 32, 512, 32, 32);
		 rotten16 = new TextureRegion(imagemap, 7 * 32, 512, 32, 32);
		 
		 uparrow = new TextureRegion(imagemap, 384, 128, 32, 32);
		 downarrow = new TextureRegion(imagemap, 416, 128, 32, 32);
		 loaded = true;
	}
	
	public static boolean isLoaded(){
		return loaded;
	}
	
	public static void reload(){
		imagemap.reload();
	}
	
	public static void reloadAudio(){
		click = audio.newSound("click.wav");
		slurp = audio.newSound("slurp.wav");
		buzz = audio.newSound("buzz.wav");
		menumusic = audio.newMusic("menumusic.wav");
		introchords = audio.newSound("intromusic.wav");
		majorchord = audio.newSound("highscore.wav");
		minorchord = audio.newSound("nohighscore.wav");
		tiltright = audio.newSound("goingup.wav");
		tiltleft = audio.newSound("goingdown.wav");
		speed = audio.newMusic("speedsound.wav");
		crunch = audio.newSound("crunch.wav");
	}

	public static void reloadSpeedSound() {
		speed = audio.newMusic("speedsound.wav");
	}
}
