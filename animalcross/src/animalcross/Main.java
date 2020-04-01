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
import commom.UserInfo;
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
	private static HashMap<String, CharacterSet> friendsset;
		
	private static MessageBoard messageboard;
	private static LogBoard logboard;
	private TextArea text;
	private String IPAddress = "10.114.205.7";
	private String PORT = "50000";
	private Socket socket;
	protected static Pane p;
	private HBox ipBox;
	private boolean isLoggedIn;
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("----- main start -----");
		isLoggedIn = false;
		friendsset = new HashMap<String, CharacterSet>();
			
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
						ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
						System.out.println("characterbox: "+characterbox.getName());
						UserInfo user = new UserInfo(characterbox.getName(), "Villager");
						MessageComponent messagecomp = new MessageComponent(user, text.getText());
						sender.writeObject(messagecomp);
					} catch (Exception e) {
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
			
		Button button = new Button();
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if(inputTextHBox.isVisible()) {
					inputTextHBox.setVisible(false);
				}else {
					inputTextHBox.setVisible(true);
				}
			}
		});
		button.setPrefSize(70.0, 70.0);
		button.setStyle("-fx-background-color: none; -fx-background-image: url(\"./animalcross/images/tubukichi_70x59.png\"); -fx-background-repeat: no-repeat;");
		textHBox.getChildren().add(button);

		p.getChildren().add(textHBox);
		p.getChildren().add(createIPAddressInput(IPAddress, PORT, "Shinjo"));
	}
		
	private void showBox(Object box) {
		if( ((Node) box).isVisible() ) ((Node) box).setVisible(false);
		else ((Node) box).setVisible(true);
	}
	 
	private void keyPressed(KeyEvent e) throws Exception {
		try {
			String characterpoint = null;
			switch(e.getCode()) {
				case LEFT: 	characterbox.setX(characterbox.getX()-10); break;
				case RIGHT:	characterbox.setX(characterbox.getX()+10); break;
				case UP: 	characterbox.setY(characterbox.getY()-10); break;
				case DOWN: 	characterbox.setY(characterbox.getY()+10); break;
				default: break;
			}
			if(isLoggedIn) {
				System.out.println(characterbox);
				ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
				UserInfo user = new UserInfo(characterbox.getName(), "Villager");
				characterpoint = characterbox.getPoint();
				MessageComponent messagecomp = new MessageComponent(user, characterpoint, "action");
				sender.writeObject(messagecomp);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
		
		
	private void connectToServer(String ipAddress, String portNumber, String userName) {
		System.out.println("----- connect to server -----");
		try {
			socket = new Socket(ipAddress, Integer.parseInt(portNumber));
			UserInfo user = new UserInfo(userName, "Villager");
			MessageComponent messagecomp = new MessageComponent(user, "requect connection");
			ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
			sender.writeObject(messagecomp);
				
			ObjectInputStream receiver = new ObjectInputStream(socket.getInputStream());
			MessageComponent messagecompreceive = (MessageComponent)receiver.readObject();
			logboard.setMessage(messagecompreceive);
				
			// In this place, I get current loggedin friends.		
			ClientThread clientthread = new ClientThread(socket, user);
			clientthread.start();

			p.getChildren().add(createIPAddressIcon(ipAddress, portNumber, userName));
			p.getChildren().remove(ipBox);
			isLoggedIn = true; 
		}catch(Exception e) {
			logboard.setMessage(new MessageComponent("Client GUI", "Connection fails."));
			e.printStackTrace();
		}
	}
		
	public static void setMessageOnLogBoard(MessageComponent messagecomp) {
		logboard.setMessage(messagecomp);
	}
		
	public static void setMessageOnMessageBoard(MessageComponent messagecomp) {
		messageboard.setMessage(messagecomp);
	}
		
	public HBox createIPAddressInput(String ipaddress, String portnumber, String username) {
		Label username_label = new Label("User Name");
		TextField userName = new TextField(username);
		HBox usernameHBox = new HBox(username_label, userName);
		usernameHBox.setStyle("-fx-font-size: 11pt; -fx-background-color: pink; -fx-padding: 2 5 2 5; -fx-background-radius: 15;");
			
		Label ipaddress_label = new Label("IP Address");
		TextField ipText = new TextField(IPAddress);
		HBox ipaddressHBox = new HBox(ipaddress_label, ipText);
		ipaddressHBox.setStyle("-fx-font-size: 11pt; -fx-background-color: pink; -fx-padding: 2 5 2 5; -fx-background-radius: 15;");
			
		Label portnumber_label = new Label("PORT Number");
		TextField portNumber = new TextField(portnumber);
		HBox portnumberHBox = new HBox(portnumber_label, portNumber);
		portnumberHBox.setStyle("-fx-font-size: 11pt; -fx-background-color: pink; -fx-padding: 2 5 2 5; -fx-background-radius: 15;");
			
		VBox inputVBox = new VBox(usernameHBox, ipaddressHBox, portnumberHBox);
			
		Button serverButton = new Button();
		serverButton.setPrefSize(70, 70);
		serverButton.setStyle("-fx-background-color: none; -fx-background-image: url(\"./animalcross/images/tanukichi_70x70.png\"); -fx-background-repeat: no-repeat;");
		serverButton.setOnAction(e -> connectToServer(ipText.getText(), portNumber.getText(), userName.getText()));
			
		Button showIpText = new Button();
		showIpText.setPrefSize(40, 40);
		showIpText.setStyle("-fx-background-color: none; -fx-background-image: url(\"./animalcross/images/icon_ipaddress_40x40.png\"); -fx-background-repeat: no-repeat;");
		HBox ipHBox = new HBox(inputVBox, serverButton);
		showIpText.setOnAction(e -> showBox(ipHBox));
		ipBox = new HBox(ipHBox, showIpText);
			
		ipBox.setLayoutX(1000.0);
		ipBox.setLayoutY(10.0);
			
		return ipBox;
	}
		
	public VBox createIPAddressIcon(String ipaddress, String portnumber, String username) {
		ImageView resetsan = new ImageView(new File("./bin/animalcross/images/reset-san_200x113.png").toURI().toString());
		Label ip_label = new Label(ipaddress+":"+portnumber);
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
		
	public static void enterFriend(MessageComponent message) {
		CharacterSet characterset = new Villager((String)message.getObject("Sender"));
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				p.getChildren().add(characterset.getModel());
			}
		});
		friendsset.put((String)message.getObject("Sender"), characterset);
	}
		
	public static HashMap<String, CharacterSet> getFriendsSet(){
		return friendsset;
	}
		
	public static void setFriendBubbleSpeech(MessageComponent messagecomp) {
		String friend_name = (String)messagecomp.getObject("Sender");
		if(friendsset.containsKey(friend_name)) {
			String message = (String)messagecomp.getObject("Message");
			Platform.runLater(new Runnable() {
	    	    @Override
	    	    public void run() {
	    	    	friendsset.get(friend_name).setSpeech(message);
	    	    }
			});
		}
	}
}