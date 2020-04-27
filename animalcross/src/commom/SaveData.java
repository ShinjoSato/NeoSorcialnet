package commom;

import java.io.Serializable;

public class SaveData implements Serializable{
	private UserPrivacy userpriv;
	private String ipaddress, portnumber; 
	
	public SaveData(UserPrivacy userpriv, String ipaddress, String portnumber) {
		this.userpriv = userpriv;
		this.ipaddress = ipaddress;
		this.portnumber = portnumber;
	}
	
	public UserPrivacy getUserPrivacy() {
		return this.userpriv;
	}
	
	public String getIPAddress() {
		return this.ipaddress;
	}
	
	public String getPortNumber() {
		return this.portnumber;
	}
	
	public String toString() {
		return userpriv+", "+ipaddress+", "+portnumber;
	}
}
