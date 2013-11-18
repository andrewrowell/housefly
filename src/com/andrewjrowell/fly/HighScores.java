package com.andrewjrowell.fly;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.andrewjrowell.framework.interfaces.FileIO;

public class HighScores {
	public static int scores[] = new int[]{0,0,0,0,0};
	public static String names[] = new String[]{"AAA", "AAA", "AAA", "AAA", "AAA"};
	static String file = ".houseflyscores";
	
	public static void load(FileIO files){
		BufferedReader in = null;
		try{
			in = new BufferedReader(new InputStreamReader(files.readFile(file)));
			for(int i = 0; i < 5; i++){
				scores[i] = Integer.parseInt(in.readLine());
				names[i] = in.readLine();
			}
		} catch (IOException e){} catch(NumberFormatException e2){} finally {
			try{
				if(in != null){
					in.close();
				}
			} catch (IOException e){}
		}
	}
	
	public static void save(FileIO files){
		BufferedWriter out = null;
		try{
			out = new BufferedWriter(new OutputStreamWriter(files.writeFile(file)));
			for(int i = 0; i < 5; i++){
				out.write(Integer.toString(scores[i]));
				out.write("\n");
				out.write(names[i]);
				out.write("\n");
			}
		} catch (IOException e){} finally {
			try{
				if(out != null){
					out.close();
				}
			} catch(IOException e){}
		}
	}
	
	public static boolean isHighScore(int score){
		for(int i = 0; i < 5; i++){
			if(score > scores[i]){
				return true;
			}
		}
		return false;
	}
	
	public static void addHighScore(int score, String name){
		for(int i = 0; i < 5; i++){
			if(score > scores[i]){
				for(int j = 4; j > i; j--){
					scores[j] = scores[j-1];
					names[j] = names[j-1];
				}
				scores[i] = score;
				names[i] = name;
				break;
			}
		}
	}
}
