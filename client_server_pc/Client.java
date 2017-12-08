package Bluetooth;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client {
	
	static public void sendMessage(){
		
		//ip du portable visible dans les info "about phone -> state"
		String ip = "192.168.1.39";
		//String ip = "100.82.25.30";
		PrintWriter pw ;
		
		Scanner sc = new Scanner(System.in);
		System.out.println("entrée : ");
		String message = sc.nextLine();
		
		try {
			Socket s = new Socket(ip,7801);
			pw = new PrintWriter(s.getOutputStream());
			pw.write(message);
			pw.flush();
			pw.close();
			s.close();			
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public static void main(String[] zero) {
		while(true){
			Client.sendMessage();
		}
		
	}


}
