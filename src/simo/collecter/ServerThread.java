package simo.collecter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import simo.collecter.db.DbConnectionFactory;
import simo.collecter.model.WifiSample;

/**
 * Thread that is launched when a device connects to the server
 * @author Thomas
 *
 */
public class ServerThread extends Thread {

	private DbConnectionFactory mConnectionFactory;
	private Socket mSocket;
	private Connection mConnection;

	public ServerThread(Socket socket) {
		this.mConnectionFactory = new DbConnectionFactory();
		this.mSocket = socket;
		this.mConnection = mConnectionFactory.create();
	}

	@Override
	public void run(){
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
			String istream = "";
			//read the input
			while((istream = input.readLine()) != null) {
				System.out.println(istream);
				//switch depending on what the input string is
				//at the moment, the only option is to add a sample to the database
				if(istream.startsWith("ADD")) {
					//build the WifiSample object from the input string and save it to the DB
					WifiSample sample = WifiSample.fromScanString(istream);
					saveSample(sample);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (input != null)
					input.close();
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}

	/**
	 * Saves a WifiSample to the database
	 * @param sample - the sample to save
	 * @return true for success - false otherwise
	 */
	private boolean saveSample(WifiSample sample) {

		/*
		String dotlessMac = removeDots(sample.getDeviceMac());
		try {
			Statement stmt = mConnection.createStatement();
			int retCode1 = stmt.executeUpdate(String.format(
				"INSERT INTO device_mac(mac, mac_string) VALUES ('%d', '%s')", Long.parseLong(dotlessMac, 16), sample.getDeviceMac()));
			if (retCode1 != 1) {
				System.err.println("Failed to insert new device in device_mac table");
			}	
		}catch (SQLException e) {
			System.err.println("Unable to execute save device_mac query");
			e.printStackTrace();
		} */
		
		try {
			
			//insert the device mac into its table
			String dotlessMac = removeDots(sample.getDeviceMac());
			
			//if this is a new device
			//added by Binghao
			Statement bhstmt = mConnection.createStatement();
			ResultSet rs = bhstmt.executeQuery(String.format(
					"SELECT * FROM device_mac WHERE mac_string = '%s'", sample.getDeviceMac()));
					
			if (!rs.next()){
				Statement stmt = mConnection.createStatement();
				int retCode1 = stmt.executeUpdate(String.format(
					"INSERT INTO device_mac(mac, mac_string) VALUES ('%d', '%s')", Long.parseLong(dotlessMac, 16), sample.getDeviceMac()));
				if (retCode1 != 1) {
					System.err.println("Failed to insert new device in device_mac table");
				}
			}
			
			
			//build the INSERT statement with the correct parameters
			PreparedStatement pStmt = mConnection.prepareStatement(
					String.format("INSERT INTO samples(timestamp, infra_mac, device_mac, rssi) " + 
					 "SELECT ?,id,('%d'),? FROM infrastructure WHERE mac = ? LIMIT 1", Long.parseLong(dotlessMac, 16)));
			pStmt.setLong(1, sample.getTimestamp());
			pStmt.setInt(2, sample.getRssi());
			pStmt.setString(3, sample.getInfrastructureMac());
			//execute the update and save the return code to check for success
			int retCode = pStmt.executeUpdate();
			//check the return code and quit the routine accordingly
			if (retCode != 1) {
				System.err.println("Failed to insert sample, retCode=" + retCode);
				return false;
			}	else{
				System.out.println("Successfully inserted one sample");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("Unable to execute save samples query");
			e.printStackTrace();
		} 

		return false;
	}
	
	//private String removeDots(String mac) {
	//	return mac.replace(':', '\0');
	//}
	//modified by Binghao
	private String removeDots(String mac) {
		StringBuilder sb = new StringBuilder(mac);
		sb.deleteCharAt(14);
		sb.deleteCharAt(11);
		sb.deleteCharAt(8);
		sb.deleteCharAt(5);
		sb.deleteCharAt(2);
		mac = sb.toString();
		return mac;
	}
	
	

}
