package com.cosmin.core;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by cosminoprea on 5/10/19.
 */
public class ShowAllUserDemo {

	public static void main(String[] args) throws IOException {
		RandomAccessFile raf = new RandomAccessFile("user.dat","r");
		
		String str;
		for (int i = 0; i < raf.length()/100; i++) {
			byte[] b = new byte[32];
			raf.read(b);
			str = new String(b,"utf-8");
			System.out.println(str);
			
			raf.read(b);
			str = new String(b,"utf-8");
			System.out.println(str);
			
			raf.read(b);
			str = new String(b,"utf-8");
			System.out.println(str);
			
			System.out.println(raf.readInt());
		}
		raf.close();
	}
}
