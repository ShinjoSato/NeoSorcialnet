package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import commom.MessageComponent;
import commom.UserInfo;
import animalcross.character.*;
import animalcross.character.CharacterSet;

public class ServerThread extends Thread{
	private Socket socket;
	private UserInfo userinfo;
	
	public ServerThread(Socket socket, UserInfo userinfo) {
		this.socket = socket;
		this.userinfo = userinfo;
		System.out.println("----- server thread -----");
	}
	
	public void run() {
		System.out.println(ServerHashMap.getServerHash().size());
		informLoginToUsers( new MessageComponent(userinfo, "enters this room.", "login") );
		informExistingFriendsToClient();
		
		while(true) {
			try {
				ObjectInputStream receiver = new ObjectInputStream(socket.getInputStream());
				MessageComponent messagecomp = (MessageComponent)receiver.readObject();

				HashMap<UserInfo, ServerThread>  hashmap = ServerHashMap.getServerHash();
				for(UserInfo eachuser: hashmap.keySet()) {
					hashmap.get(eachuser).sendMessageToUser(messagecomp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendMessageToUser(MessageComponent message) {
		try {
			ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
			sender.writeObject(message);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void informExistingFriendsToClient() {
		HashMap<UserInfo, ServerThread>  hashmap = ServerHashMap.getServerHash();
		for(UserInfo friend: hashmap.keySet()) {
			MessageComponent messagecomp = new MessageComponent(friend, "already exists in the server.", "login");
			hashmap.get(userinfo).sendMessageToUser(messagecomp);
		}
	}
	
	public void informLoginToUsers(MessageComponent message) {
		HashMap<UserInfo, ServerThread>  hashmap = ServerHashMap.getServerHash();
		for(UserInfo user: hashmap.keySet()) {
			if(!user.equals(message.getUserInfo())) {
				hashmap.get(user).sendMessageToUser(message);
			}
		}
	}
}