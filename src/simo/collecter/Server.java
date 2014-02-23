package simo.collecter;

import java.io.IOException;
import java.net.ServerSocket;

import simo.collecter.db.DbConnectionFactory;

/**
 * 
 * @author Thomas
 *
 */
public class Server {

	private static final int SERVER_PORT = 9999;
	
	public static void main(String[] args) {
		
		DbConnectionFactory.dbName = args[0];
		
		ServerSocket welcomeSocket;
		
		try {
			welcomeSocket = new ServerSocket(SERVER_PORT);
			while (true) {
				//launch a new thread for each device that connects to the server
				new ServerThread(welcomeSocket.accept()).start();
			}			
		} catch (IOException e) {
			System.out.println("Could not listen on port: "+ SERVER_PORT);
			System.exit(-1);
		}
	}
	
}
