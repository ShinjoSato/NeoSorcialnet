package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import commom.MessageComponent;

public class ServerTest1 {

	public static void main(String[] args) throws IOException {
		final int PORT = 50000;
		try {
			ServerSocket serverSocket =new ServerSocket(PORT);
			System.out.println("----- start server. connection is built. PORT number is "+PORT+" -----");
			while(true) {	
				Socket socket = serverSocket.accept();

				ObjectInputStream reciever = new ObjectInputStream(socket.getInputStream());
				Object object = reciever.readObject();
				MessageComponent messagecomp = (MessageComponent)object;
				
				ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
				sender.writeObject(messagecomp);
				ServerThread serverthread = new ServerThread(socket, messagecomp.getUserInfo());
				ServerHashMap.setServerThread(messagecomp.getUserInfo(), serverthread);
				serverthread.start();
			} 
		}catch (Exception e) {
			e.printStackTrace();
			
		}
	}
}
