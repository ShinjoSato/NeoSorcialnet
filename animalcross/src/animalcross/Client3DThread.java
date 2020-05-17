package animalcross;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import animalcross.character.CharacterSet;
import commom.MessageComponent;
import commom.UserInfo;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

public class Client3DThread extends Thread{
	private Socket socket;
	private UserInfo client;

	public Client3DThread(Socket socket, UserInfo client) {
		System.out.println("----- client 3D thread -----");
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
		String sender_name = "";
		switch((String)messagecomp.getObject("MessageType")) {
			case "login":
				if(messagecomp.getObject("Sender").equals(client.getUserName())) {
					System.out.println(messagecomp.getObject("Sender")+"は私です。");
				}else {
					System.out.println(messagecomp+"を新たに追加します。");
					Main3D.setMessageOnLogBoard(messagecomp);
					Main3D.enterFriend(messagecomp);
				}
				break;
			case "message":
				System.out.println("receive message");
				Main3D.setFriendBubbleSpeech(messagecomp);
				Main3D.setMessageOnLogBoard(messagecomp);
				Main3D.setMessageOnMessageBoard(messagecomp);
				break;
			case "action":
				sender_name = (String)messagecomp.getObject("Sender");
				if( Main3D.getFriendsSet().containsKey(sender_name)) {
					String point = (String)messagecomp.getObject("Message");
					String[] str = point.split(",");
					double x = Double.parseDouble(str[0]), y = Double.parseDouble(str[1]),  z = Double.parseDouble(str[2]);
					Main3D.getFriendsSet().get(sender_name).setTranslate(x, y, z);
				}
				break;
			case "rotation":
				System.out.println("set Rotation");
				sender_name = (String)messagecomp.getObject("Sender");
				if( Main3D.getFriendsSet().containsKey(sender_name)) {
					int rotate = Integer.parseInt( String.valueOf( messagecomp.getObject("Message") ) );
					while(rotate != Main3D.getFriendsSet().get(sender_name).getDirection()) {
						Main3D.getFriendsSet().get(sender_name).turnRight();
					}
				}
				break;
			case "character":
				System.out.println(messagecomp);
				if(!client.getUserName().equals((String)messagecomp.getObject("Sender"))) {
					Main3D.changeFriendsCharacter(messagecomp);
				}
			default: break;
		}
	}
}