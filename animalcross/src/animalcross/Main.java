package animalcross;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import commom.Mail;
import commom.MessageComponent;
import commom.SaveData;
import commom.UserInfo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
	protected static AnchorPane p;
	
	protected static Pane graphic2dPane;
	
	private HBox ipBox;
	private boolean isLoggedIn;
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("----- main start -----");
		isLoggedIn = false;
		friendsset = new HashMap<String, CharacterSet>();
			
		p = new AnchorPane();
		p.setStyle("-fx-background-image: url(\"./animalcross/images/background.jpg\"); ");
		Scene scene = new Scene(p, 500, 700);
		scene.setOnKeyPressed(e -> {
			try {keyPressed(e);} 
			catch (Exception e1) {}
		});
		
		Image icon = new Image("./animalcross/images/haniwa_30x30.png");
		primaryStage.getIcons().add(icon);
		primaryStage.setTitle("Welcome to my chat room!");
		primaryStage.setScene(scene);
		
		//primaryStage.setOpacity(0.7);
		
		primaryStage.show();
	 		
		characterbox = new Villager("Shinjo", 130.0, 130.0);
			
		messageboard = new MessageBoard(350, 460);
		VBox messageboardVBox = messageboard.getFrame(0, 250);
			
		logboard = new LogBoard(350, 250);
		VBox logboardVBox = logboard.getFrame(0, 0);
			
		text = new TextArea();
		text.setPrefSize(370, 70);
		text.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
				characterbox.setSpeech(text.getText());
					if(!isLoggedIn) messageboard.setMessage(characterbox.getName(), text.getText());
					else {
						UserInfo userinfo = new UserInfo(characterbox.getName(), characterbox.getCharacterName());
						sendMessageToServer( new MessageComponent(userinfo,  text.getText()) );
					}
					text.setText("");
					ke.consume(); 
				}			
			}
		});
		HBox inputTextHBox = new HBox(text);
		inputTextHBox.setVisible(false);
		HBox textHBox = new HBox();
		/**
		 * Position
		 * - Large Window (600, 800)
		 * - Small Window (20, 600)
		 */
		textHBox.setLayoutX(20);
		textHBox.setLayoutY(600);
		textHBox.getChildren().add(inputTextHBox);
			
		Button button = new Button();
		button.setPrefSize(70.0, 70.0);
		button.setStyle("-fx-background-color: none; -fx-background-image: url(\"./animalcross/images/tubukichi_70x59.png\"); -fx-background-repeat: no-repeat;");
		button.setOnAction(act -> showBox(inputTextHBox));
		textHBox.getChildren().add(button);
		
		Pane graphic2dPane = new Pane();
		//graphic2dPane.getChildren().addAll(characterbox.getModel());
		
		p.getChildren().addAll(
			characterbox.getModel(),
			//graphic2dPane,
			createIPAddressInput(IPAddress, PORT, "Shinjo"),
			getMenuBar(),
			logboardVBox,
			messageboardVBox,
			textHBox,
			createEmailInput()
		);
		
		//createOtherStage(logboardVBox, 100,100);
		saveData();
		reloadSaveData();
	}
	
	/**
	 * 
	 */
	public void createOtherStage(VBox vbox, double width, double height) {
		Pane pane = new AnchorPane();
		pane.getChildren().add(vbox);
		Scene scene = new Scene(pane, width, height);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Menu bar is from here.
	 */
	public MenuBar getMenuBar() {
		 MenuBar menuBar = new MenuBar();
		 menuBar.setUseSystemMenuBar(true);
		 
	     // --- Menu File
	     Menu Edit = new Menu("Edit");
	     MenuItem Edit_Sizue = new MenuItem("Sizue", new ImageView("./animalcross/character/images/sizue_18x30.png"));
	     Edit_Sizue.setOnAction(e -> renewCharacter("Sizue", characterbox.getName()));
	     MenuItem Edit_Villager = new MenuItem("Villager", new ImageView("./animalcross/character/images/murabito_30x30.png"));
	     Edit_Villager.setOnAction(e -> renewCharacter("Villager", characterbox.getName()));
	     Edit.getItems().addAll(Edit_Sizue, Edit_Villager);
	     
	     // --- Menu sub window
	     Menu menu_board = new Menu("Panel");
	     MenuItem board_log = new MenuItem("Log Board");
	     board_log.setOnAction(e->{ logboard.setVisible( (logboard.isVisible())? false: true ); });
	     MenuItem board_message = new MenuItem("Message Board");
	     board_message.setOnAction(e->{ messageboard.setVisible( (messageboard.isVisible())? false: true ); });
	     menu_board.getItems().addAll(board_log, board_message);
	     
	     menuBar.getMenus().addAll(Edit, menu_board);
	     return menuBar;
	}
	
	public void renewCharacter(String character, String username) {
		System.out.println("renewCharacter");
		double x = characterbox.getX(), y = characterbox.getY();
		p.getChildren().remove(characterbox.getModel());
		//graphic2dPane.getChildren().remove(characterbox.getModel());

		switch(character) {
			case "Villager": 	characterbox = new Villager(username); 	break;
			case "Sizue": 		characterbox = new Sizue(username); 	break;
		}
		
		characterbox.setPoint(x, y);
		p.getChildren().add(characterbox.getModel());
		//graphic2dPane.getChildren().add(characterbox.getModel());
		
		if(isLoggedIn) {
			UserInfo userinfo = new UserInfo(characterbox.getName(), characterbox.getCharacterName());
			sendMessageToServer( new MessageComponent(userinfo, "change character design", "character") );
		}
	}
	
	private void sendMessageToServer(MessageComponent messagecomp) {
		try {
			ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
			sender.writeObject(messagecomp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	private void showBox(Object box) {
		if( ((Node) box).isVisible() ) ((Node) box).setVisible(false);
		else ((Node) box).setVisible(true);
	}
	 
	private void keyPressed(KeyEvent e) throws Exception {
		try {
			String characterpoint = null;
			int distance = 15;
			switch(e.getCode()) {
				case LEFT: 	characterbox.setX(characterbox.getX() - distance); break;
				case RIGHT:	characterbox.setX(characterbox.getX() + distance); break;
				case UP: 	characterbox.setY(characterbox.getY() - distance); break;
				case DOWN: 	characterbox.setY(characterbox.getY() + distance); break;
				default: break;
			}
			if(isLoggedIn) {
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

			UserInfo user = new UserInfo(userName, characterbox.getCharacterName());
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
		System.out.println("----- createIPAddressInput -----");
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
		showIpText.setOnAction(e -> connectToServer(ipText.getText(), portNumber.getText(), userName.getText()));
		showIpText.setPrefSize(40, 40);
		showIpText.setStyle("-fx-background-color: none; -fx-background-image: url(\"./animalcross/images/icon_ipaddress_40x40.png\"); -fx-background-repeat: no-repeat;");
		HBox ipHBox = new HBox(inputVBox, serverButton);
		//showIpText.setOnAction(e -> showBox(ipHBox));
		ipBox = new HBox(ipHBox, showIpText);
		
		/**
		 * Position
		 * - Large Window: (1000.0, 10.0)
		 * - Small Window: (100.0, 10.0)
		 */
		ipBox.setLayoutX(100.0);
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
		/**
		 * Position
		 * - Large Widow: (1200.0, 200.0)
		 * - Small Window (300.0, 20.0)
		 */
		ip_vbox.setLayoutX(300.0);
		ip_vbox.setLayoutY(20.0);
			
		characterbox.setName(username);
		return ip_vbox;
	}
	
	private VBox createEmailInput() {
		/**
		 * Mail Input
		 */
		Label senderLabel = new Label("Sender:");
		TextField senderField = new TextField("shinjo.software@gmail.com");
		HBox senderHBox = new HBox(senderLabel, senderField);
		
		Label passLabel = new Label("Password:");
		TextField passField = new TextField("Zxcvbnm12345sato");
		HBox passHBox = new HBox(passLabel, passField);
		
		
		Label addressLabel = new Label("To:");
		TextField addressField = new TextField();
		HBox addressHBox = new HBox(addressLabel, addressField);
		
		Label subjectLabel = new Label("Subject:");
		TextField subjectField = new TextField();
		HBox subjectHBox = new HBox(subjectLabel, subjectField);
		
		Label messageLabel = new Label("Message:");
		TextArea messageText = new TextArea();
		messageText.setPrefSize(370, 70);
		HBox messageHBox = new HBox(messageLabel, messageText);
		
		Button mailButton = new Button("mail");
		/*mailButton.setOnAction(e -> {
			Mail.sendEMail(senderField.getText(), passField.getText(), addressField.getText(), subjectField.getText(), messageText.getText());
		});*/
		HBox buttonHBox = new HBox(mailButton);
		buttonHBox.setAlignment(Pos.CENTER_RIGHT);
		VBox mailVBox = new VBox(senderHBox, passHBox, addressHBox, subjectHBox, messageHBox, buttonHBox);
		
		Button isVisible = new Button();
		isVisible.setOnAction(e-> showBox(mailVBox));
		isVisible.setPrefSize(106, 90);
		isVisible.setStyle("-fx-background-color: none; -fx-background-image: url(\"./animalcross/character/images/perio_106x90.png\"); -fx-background-repeat: no-repeat;");
		
		VBox all = new VBox(mailVBox, isVisible);
		return all;
	}
		
	public static void enterFriend(MessageComponent message) {
		System.out.println(message);
		CharacterSet characterset = replaceCharacter((String)message.getObject("Sender"), message.getCharacter());
		friendsset.put((String)message.getObject("Sender"), characterset);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				p.getChildren().add( friendsset.get((String)message.getObject("Sender")).getModel() );
				//graphic2dPane.getChildren().add( friendsset.get((String)message.getObject("Sender")).getModel() );
			}
		});
	}
	
	public static CharacterSet replaceCharacter(String name, String character) {
		CharacterSet characterset = null;
		switch(character) {
			case "Villager": 	characterset = new Villager(name); 	break;
			case "Sizue": 		characterset = new Sizue(name); 	break;
		}
		return characterset;
	}
	
	public static void changeFriendsCharacter(MessageComponent messagecomp) {
		System.out.println(messagecomp.getObject("Sender")+"キャラクターを変更しました");
		String friend = (String)messagecomp.getObject("Sender");
		double x = friendsset.get(friend).getX(), y = friendsset.get(friend).getY();
		removeFriend(friend);
		enterFriend(messagecomp);
		friendsset.get(friend).setPoint(x, y);
	}
		
	public static HashMap<String, CharacterSet> getFriendsSet(){
		return friendsset;
	}
	
	public static void removeFriend(String friend) {
		VBox friendVBox = friendsset.get(friend).getModel();
		Platform.runLater(new Runnable() {
    	    @Override
    	    public void run() {
    	    	p.getChildren().remove(friendVBox);
    	    	//graphic2dPane.getChildren().remove(friendVBox);
    	    }
		});
		friendsset.remove(friend);
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
	
	public static void saveData() {
		try(FileOutputStream f = new FileOutputStream("data/renshu.dat");
			BufferedOutputStream b = new BufferedOutputStream(f);
			ObjectOutputStream out = new ObjectOutputStream(b)){
	 
			SaveData savedata = new SaveData(new UserInfo("UserName", "User Character"), "user ip address", "user port Number");
			out.writeObject(savedata);
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		System.out.println("saveData()");
	}
	
	public static void reloadSaveData() {
		try(FileInputStream f = new FileInputStream("data/renshu.dat");
			BufferedInputStream b = new BufferedInputStream(f);
			ObjectInputStream in = new ObjectInputStream(b)){
	 
			SaveData savedata = (SaveData) in.readObject();
	 
			System.out.println(savedata);
		} catch ( IOException e ) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}