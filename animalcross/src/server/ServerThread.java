package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import commom.MessageComponent;
import animalcross.character.*;
import animalcross.character.CharacterSet;

public class ServerThread extends Thread{
	private Socket socket;
	public String userName;
	private CharacterSet characterset;
	
	public ServerThread(Socket socket, String userName/*, CharacterSet characterset*/) {
		this.socket = socket;
		this.userName = userName;
//		this.characterset = characterset;
		System.out.println("----- server thread -----");
	}
	
	
	public void run() {
		System.out.println(ServerHashMap.getServerHash().size());
		informLoginToUsers( new MessageComponent(/*characterset, */"Server", userName+" enters this room.") );
		
		while(true) {
			try {
				/**
				 * - messagecomp
				 * replace String with MessageComponent
				 */
				ObjectInputStream receiver = new ObjectInputStream(socket.getInputStream());
				MessageComponent messagecomp = (MessageComponent)receiver.readObject();//String messagecomp = (String)receiver.readObject();
				System.out.println(userName+" > "+messagecomp.getMessage()+" in ServerThread");
				
				HashMap<String, ServerThread>  hashmap = ServerHashMap.getServerHash();
				for(String eachuser: hashmap.keySet()) {
					hashmap.get(eachuser).sendMessageToUser(messagecomp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendMessageToUser(MessageComponent message) {
		System.out.println("sendMessageToUser:"+message);
		try {
			/**
			 * - sender
			 * replace String with ManageComponent
			 */
			ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
			sender.writeObject(message);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void informLoginToUsers(MessageComponent message) {
		HashMap<String, ServerThread>  hashmap = ServerHashMap.getServerHash();
		for(String user: hashmap.keySet()) {
			if(!user.equals(message.getSender())) {
				hashmap.get(user).sendMessageToUser(new MessageComponent(/*characterset, */message.getSender(), message.getMessage()));
			}
		}
	}
}
