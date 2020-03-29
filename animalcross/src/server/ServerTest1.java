package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import animalcross.*;
import commom.MessageComponent;

public class ServerTest1 {

	public static void main(String[] args) throws IOException {
		System.out.println("Start server.");
		try {
			ServerSocket serverSocket =new ServerSocket(50000);
			System.out.println("----- connection built -----");
			//receive information from client
			while(true) {
					
				Socket socket = serverSocket.accept();
				/**
				 * - receiver
				 * replace String with ManageComponent
				 */
				ObjectInputStream reciever = new ObjectInputStream(socket.getInputStream());
				MessageComponent messagecomp = (MessageComponent)reciever.readObject();// messagecomp = userName
				
				/**
				 * - sender
				 * Not replace String with ManageComponent
				 */
				ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
				sender.writeObject(messagecomp);
				System.out.println("### Welcome "+messagecomp+" ###");
				ServerThread serverthread = new ServerThread(socket, messagecomp.getSender()/*, messagecomp.getCharacter()*/);
				ServerHashMap.setServerThread(messagecomp.getSender(), serverthread);//ServerHashMap.setServerThread(messagecomp, serverthread);
				serverthread.start();
			} 
		}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
		}
	}
}
