package commom;

import java.io.Serializable;

public class SaveData implements Serializable{
	private UserInfo userinfo;
	private String ipaddress, portnumber; 
	
	public SaveData(UserInfo userinfo, String ipaddress, String portnumber) {
		this.userinfo = userinfo;
		this.ipaddress = ipaddress;
		this.portnumber = portnumber;
	}
	
	public String toString() { return String.format("%s, %s, %s", userinfo, ipaddress, portnumber); }
}
