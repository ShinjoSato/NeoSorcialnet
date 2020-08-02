package sns.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import sns.client.obj2d.character.CharacterSet;
import sns.common.MessageComponent;
import sns.common.UserInfo;

public class ClientThread extends Thread{
	private Socket socket;
	private UserInfo client;

	public ClientThread(Socket socket, UserInfo client) {
		System.out.println("----- client thread -----");
		this.socket = socket;
		this.client = client;
	}
	
	public void run() {
		while(true) {
			try {
				ObjectInputStream receiver = new ObjectInputStream(socket.getInputStream());
				MessageComponent messagecomp =(MessageComponent)receiver.readObject();
				takeAction(messagecomp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void takeAction(MessageComponent messagecomp) {
		switch((String)messagecomp.getObject("MessageType")) {
			case "login":
				if(messagecomp.getObject("Sender").equals(client.getUserName())) {
					System.out.println(messagecomp.getObject("Sender")+"は私です。");
				}else {
					System.out.println(messagecomp+"を新たに追加します。");
					Main.setMessageOnLogBoard(messagecomp);
					Main.enterFriend(messagecomp);
				}
				break;
			case "message":
				System.out.println("receive message");
				Main.setFriendBubbleSpeech(messagecomp);
				Main.setMessageOnLogBoard(messagecomp);
				Main.setMessageOnMessageBoard(messagecomp);
				break;
			case "action":
				//System.out.println("receive action:"+messagecomp.getObject("Sender")+" -> "+messagecomp.getObject("Message"));
				String sender_name = (String)messagecomp.getObject("Sender");
				if( Main.getFriendsSet().containsKey(sender_name)) {
					String point = (String)messagecomp.getObject("Message");
					String[] str = point.split(",");
					double x = Double.parseDouble(str[0]);
					double y = Double.parseDouble(str[1]);
					Main.getFriendsSet().get(sender_name).setPoint(x, y);
				}
				break;
			case "character":
				/**
				 * 2020-04-04
				 * I'll modify here to change friends' character design.
				 */
				System.out.println(messagecomp);
				if(!client.getUserName().equals((String)messagecomp.getObject("Sender"))) {
					Main.changeFriendsCharacter(messagecomp);
				}
				//CharacterSet friend = (CharacterSet) Main.getFriendsSet().get((String)messagecomp.getObject("Sender"));
			default: break;
		}
	}
}