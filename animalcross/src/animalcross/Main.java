package animalcross;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import animalcross.character.CharacterSet;
import animalcross.character.Sizue;
import animalcross.character.Villager;
import commom.MessageComponent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.Pane;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
		private CharacterSet characterbox;
		private static MessageBoard messageboard;
		private static LogBoard logboard;
		private TextArea text;
		private String IPAddress = "10.114.205.7";
		private Socket socket;
		protected Pane p;
		private HBox ipBox;
		private boolean isLoggedIn;
	 
		@Override
		public void start(Stage primaryStage) throws Exception {
			System.out.println("----- main start -----");
			isLoggedIn = false;
			
			p = new Pane();
			Scene scene = new Scene(p, 400, 300);
			primaryStage.setScene(scene);
			primaryStage.show();
	 		
			characterbox = new Villager("Shinjo");
			
			messageboard = new MessageBoard(350, 460);
			VBox field = messageboard.getFrame(30, 400);
			p.getChildren().add(field);
			
			logboard = new LogBoard(350, 250);
			VBox log = logboard.getFrame(30, 100);
			p.getChildren().add(log);
			
			p.setStyle("-fx-background-image: url(\"./animalcross/images/background.jpg\"); ");
			p.getChildren().add(characterbox.getModel());
	 
			//キーイベントの登録
			scene.setOnKeyPressed(e -> {
				try {keyPressed(e);} 
				catch (Exception e1) {}
			});
			
			text = new TextArea();
			text.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			    @Override
			    public void handle(KeyEvent ke) {
			        if (ke.getCode().equals(KeyCode.ENTER)) {
			        	characterbox.setSpeech(text.getText());
						if(!isLoggedIn) messageboard.setMessage(characterbox, text.getText());
						try {
							/**
							 * - sender
							 * replace String with ManageComponent
							 */
							ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
							System.out.println("characterbox: "+characterbox.getName());
							sender.writeObject(new MessageComponent(/*characterbox, */characterbox.getName(), text.getText()));//sender.writeObject(text.getText());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						text.setText("");
						ke.consume(); 
			        }
			        
			    }
			});
			text.setPrefSize(370, 70);

			HBox inputTextHBox = new HBox(text);
			inputTextHBox.setVisible(false);
			
			HBox textHBox = new HBox();
			textHBox.setLayoutX(600);
			textHBox.setLayoutY(800);
			textHBox.getChildren().add(inputTextHBox);
			
			Button button = new Button("Open");
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					if(inputTextHBox.isVisible()) {
						inputTextHBox.setVisible(false);
						button.setText("Open");
					}else {
						inputTextHBox.setVisible(true);
						button.setText("Close");
					}
				}
			});
			textHBox.getChildren().add(button);

			p.getChildren().add(textHBox);
			
			p.getChildren().add(createIPAddressInput(IPAddress, "Shinjo"));
		}

		
		private void showBox(Object box) {
			if( ((Node) box).isVisible() ) ((Node) box).setVisible(false);
			else ((Node) box).setVisible(true);
		}
	 
		private void keyPressed(KeyEvent e) throws Exception {
			//上下左右キーを押した時imgの座標を移動させる。
			switch(e.getCode()) {
				case LEFT: characterbox.setX(characterbox.getX()-10); break;
				case RIGHT: characterbox.setX(characterbox.getX()+10); break;
				case UP: characterbox.setY(characterbox.getY()-10); break;
				case DOWN: characterbox.setY(characterbox.getY()+10); break;
				default: break;
			}
		}
		
		private void connectToServer(String ipAddress, String userName) {
			System.out.println("----- connect to server -----");
			try {
				socket = new Socket(ipAddress, 50000);
				System.out.println("characterbox: "+characterbox.getName());
				/**
				 * - sender
				 * replace String with ManageComponent
				 */
				ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
				sender.writeObject(new MessageComponent(/*characterbox, */userName, "requect connection"));//sender.writeObject(userName);
				
				/**
				 * - receiver
				 * replace String with ManageComponent
				 */
				ObjectInputStream receiver = new ObjectInputStream(socket.getInputStream()); //java.net.SocketException: Connection reset
				MessageComponent messagecomp = (MessageComponent)receiver.readObject();//String messagecomp = (String)receiver.readObject();
				logboard.setMessage(messagecomp);
				
				ClientThread clientthread = new ClientThread(socket);
				clientthread.start();

				p.getChildren().add(createIPAddressIcon(ipAddress, userName));
				p.getChildren().remove(ipBox);
				isLoggedIn = true; 
			}catch(Exception e) {
				logboard.setMessage(new MessageComponent(/*characterbox, */"Client GUI", "Connection fails."));
				e.printStackTrace();
			}
		}
		
		public static void setMessageOnLogBoard(MessageComponent messagecomp) {
			logboard.setMessage(messagecomp);
		}
		
		public static void setMessageOnMessageBoard(MessageComponent messagecomp) {
			messageboard.setMessage(messagecomp);
		}
		
		public HBox createIPAddressInput(String ipaddress, String username) {
			TextField userName = new TextField(username);
			TextField ipText = new TextField(IPAddress);
			Button serverButton = new Button("Connect to server");
			serverButton.setOnAction(e -> connectToServer(ipText.getText(), userName.getText()));
			Button showIpText = new Button("Show");
			HBox ipHBox = new HBox(userName, ipText, serverButton);
			showIpText.setOnAction(e -> showBox(ipHBox));
			ipBox = new HBox(ipHBox, showIpText);
			return ipBox;
		}
		
		public VBox createIPAddressIcon(String ipaddress, String username) {
			ImageView resetsan = new ImageView(new File("./bin/animalcross/images/reset-san_200x113.png").toURI().toString());
			Label ip_label = new Label(ipaddress);
			ip_label.setStyle("-fx-font-size: 14pt; -fx-background-color: pink; -fx-padding: 2 5 2 5; -fx-background-radius: 15;");
			Label name_label = new Label(username);
			name_label.setStyle("-fx-font-size: 14pt; -fx-background-color: pink; -fx-padding: 2 5 2 5; -fx-background-radius: 15;");
			VBox ip_vbox = new VBox(resetsan, ip_label, name_label);
			ip_vbox.setAlignment(Pos.CENTER);
			ip_vbox.setLayoutX(1200.0);
			ip_vbox.setLayoutY(200.0);
			
			characterbox.setName(username);
			return ip_vbox;
		}
}
