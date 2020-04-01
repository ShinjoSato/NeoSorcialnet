package server;

import java.util.HashMap;

import commom.UserInfo;

public class ServerHashMap {
	private static HashMap<UserInfo, ServerThread> serverHash;
	
	public static void setServerThread(UserInfo userinfo, ServerThread serverthread) {
		System.out.println("username: "+userinfo.getUserName());
		System.out.println("serverthread: "+serverthread);
		if(serverHash == null) serverHash = new HashMap<UserInfo, ServerThread>();
		serverHash.put(userinfo, serverthread);
	}
	
	public static HashMap<UserInfo, ServerThread> getServerHash(){
		return serverHash;
	}
}
