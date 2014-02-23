package simo.collecter.model;

/**
 * Represents a scan from an access point, contains the AP mac address,
 * the device mac addess, the RSSI between both and a timestamp in UNIX time (seconds)
 * @author Thomas
 *
 */
public class WifiSample {

	private String infrastructureMac;
	private String deviceMac;
	private int rssi;
	private long timestamp;
	
	public WifiSample(String infrastructureMac, String deviceMac, int rssi,
			long timestamp) {
		super();
		this.infrastructureMac = infrastructureMac;
		this.deviceMac = deviceMac;
		this.rssi = rssi;
		this.timestamp = timestamp;
	}
	
	/**
	 * Returns a WifiSample object from the string the access points send to the server
	 * @param incomingString
	 * @return the WifiSample object
	 */
	public static WifiSample fromScanString(String incomingString) {
		String[] tokens = incomingString.split(";");
		
		return new WifiSample(tokens[2], 
				tokens[3], 
				Integer.valueOf(tokens[4]), 
				Long.valueOf(tokens[1]));	
	}
	
	public String getInfrastructureMac() {
		return infrastructureMac;
	}
	public void setInfrastructureMac(String infrastructureMac) {
		this.infrastructureMac = infrastructureMac;
	}
	public String getDeviceMac() {
		return deviceMac;
	}
	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}
	public int getRssi() {
		return rssi;
	}
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
