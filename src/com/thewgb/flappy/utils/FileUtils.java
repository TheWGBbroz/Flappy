package com.thewgb.flappy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
	private FileUtils() {}
	
	public static String loadAsString(String file) {
		StringBuilder res = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.class.getClassLoader().getResourceAsStream(file)));
			String buffer = "";
			while((buffer = reader.readLine()) != null) {
				res.append(buffer + '\n');
			}
			reader.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return res.toString();
	}
}
