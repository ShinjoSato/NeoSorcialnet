package server;

import java.util.HashMap;

public class ServerHashMap {
	private static HashMap<String, ServerThread> serverHash;
	
	public static void setServerThread(String username, ServerThread serverthread) {
		System.out.println("username: "+username);
		System.out.println("serverthread: "+serverthread);
		if(serverHash == null) serverHash = new HashMap<String, ServerThread>();
		serverHash.put(username, serverthread);
	}
	
	public static HashMap<String, ServerThread> getServerHash(){
		return serverHash;
	}
}
