package com.googlecode.gycreative.faststorage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {
	public static byte[] readFile(File f) {
		byte [] fileData = new byte[(int)f.length()];
		try {
			DataInputStream dis = new DataInputStream((new FileInputStream(f)));
			dis.readFully(fileData);
			dis.close();
			
			return fileData;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeFile(File f, byte[] data) {
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(data);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
