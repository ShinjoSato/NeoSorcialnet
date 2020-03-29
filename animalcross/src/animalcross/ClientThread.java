package animalcross;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import commom.MessageComponent;

public class ClientThread extends Thread{
	private Socket socket;

	public ClientThread(Socket socket) {
		System.out.println("----- client thread -----");
		this.socket = socket;
	}
	
	public void run() {
		while(true) {
			try {
				/**
				 * - receiver
				 * replace String with ManageComponent
				 */
				ObjectInputStream receiver = new ObjectInputStream(socket.getInputStream());
				MessageComponent messagecomp =(MessageComponent)receiver.readObject();
				Main.setMessageOnLogBoard(messagecomp);
				Main.setMessageOnMessageBoard(messagecomp);
				System.out.println("receive message");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
