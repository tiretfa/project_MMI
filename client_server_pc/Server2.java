package Bluetooth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

public class Server2 {
	
	static Socket s;
	static ServerSocket ss;
	static InputStreamReader isr;
	static BufferedReader br;
	static String message;
	
	
	public static void main(String[] args){
		try {
			ss = new ServerSocket(6000);
			while(true){
				s = ss.accept();
				isr = new InputStreamReader(s.getInputStream());
				br = new BufferedReader(isr);
				message = br.readLine();
				System.out.println(message);
			}
			
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
	}

}
