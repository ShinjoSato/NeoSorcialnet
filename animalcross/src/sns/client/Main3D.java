package sns.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;
import javafx.scene.layout.Pane;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import sns.client.obj3d.Character3DSet;
import sns.client.obj3d.character.Ironman85;
import sns.client.obj3d.character.Peripper;
import sns.client.obj3d.character.Shinio;
import sns.client.obj3d.character.Spiderman;
import sns.common.Coordinates;
import sns.common.MessageComponent;
import sns.common.SpacialIndex;
import sns.common.UserInfo;
import sns.common.itemInterface;

public class Main3D extends Application implements itemInterface{
	private static HashMap<String, Character3DSet> friendsset;
	private static MessageBoard messageboard;
	private static LogBoard logboard;
	//private TextArea text;
	private String IPAddress = "10.114.205.7", PORT = "50000";
	private Socket socket;
	protected static Group group;
	protected static Pane interfacePane, graphic2dPane;
	private VBox settingboard;
	private HBox ipBox;
	private boolean isLoggedIn;
	private static GraphicSet graphicset;
	private Stage primaryStage;
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println("----- main start -----");
		isLoggedIn = false;
		friendsset = new HashMap<String, Character3DSet>();
			
		group = new Group();
		group.setStyle("-fx-background-image: url(\"sns/client/images/background.jpg\"); ");
		Scene scene = new Scene(group, 800, 650);
		scene.setOnKeyPressed(e -> {
			try {keyPressed3D(e);} 
			catch (Exception e1) {}
		});
		
		this.primaryStage = (primaryStage == null)? new Stage(): primaryStage;
		
		//Image icon = new Image(img_url[0]);
		//primaryStage.getIcons().add(icon);
		this.primaryStage.setTitle("Welcome to my chat room!");
		this.primaryStage.setScene(scene);
		this.primaryStage.show();

		messageboard = new MessageBoard(350, 460);
		messageboard.setVisible(false);
		VBox messageboardVBox = messageboard.getFrame(800, 250);
		logboard = new LogBoard(350, 250);
		logboard.setVisible(false);
		VBox logboardVBox = logboard.getFrame(800, 0);
			
		HBox textHBox = getTextArea(400d, 650d);

		settingboard = new VBox();
		
		interfacePane = new Pane(
			createIPAddressInput(IPAddress, PORT, "Shinjo", 400d, 0d),
			getMenuBar(),
			logboardVBox,
			messageboardVBox,
			textHBox,
			UISet.createEmailInput(),
			settingboard
		);
		
		graphicset = new GraphicSet();
		
		SubScene cube3dScene = graphicset.getSubScene();
		cube3dScene.setOnMouseClicked(e->{ System.out.println("You click 3d Subscene.");} );
		group.getChildren().addAll(
			cube3dScene,
			interfacePane
		);

		//UISet.saveData();
		UISet.reloadSaveData();
	}
	
	private HBox getTextArea(double layoutX, double layoutY) {
		TextArea text = new TextArea();
		text.setPrefSize(370, 20);
		text.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					graphicset.getCharacter().setSpeech(text.getText());
					if(!isLoggedIn) messageboard.setMessage(graphicset.getCharacter().getName(), text.getText());
					else {
						UserInfo userinfo = new UserInfo(graphicset.getCharacter().getName(), "Iron Man 85");
						sendMessageToServer( new MessageComponent(userinfo,  text.getText()) );
					}
					text.setText("");
					ke.consume(); 
				}			
			}
		});
		HBox inputTextHBox = new HBox(text);
		inputTextHBox.setVisible(false);
		
		Button button = new Button();
		button.setPrefSize(20.0, 20.0);
		button.setStyle("-fx-background-color: none; -fx-background-image: url(\"sns/client/images/tubukichi_70x59.png\"); -fx-background-repeat: no-repeat;");
		button.setOnAction(act -> showBox(inputTextHBox));
		
		HBox textHBox = new HBox(inputTextHBox, button);
		textHBox.setLayoutX(layoutX); textHBox.setLayoutY(layoutY);
		return textHBox;
	}
	
	private void keyPressed3D(KeyEvent e) {
		switch(e.getCode()) {
    		case A: graphicset.moveLeft(); break;
    		case D: graphicset.moveRight(); break;
    		case W: graphicset.moveFront(); break;
   			case S: graphicset.moveBack(); break;
    		case DIGIT1:	settingboard.setVisible( (settingboard.isVisible())? false: true ); break;
   			case DIGIT2: messageboard.setVisible( (messageboard.isVisible())? false: true ); break;
   			case DIGIT3: logboard.setVisible( (logboard.isVisible())? false: true ); break;
    		case LEFT:  graphicset.turnLeft(); 	break;
    		case RIGHT: graphicset.turnRight(); break;
    		case UP: 	graphicset.getCharacter().setTransform( new Rotate(90 , new Point3D(1, 0, 0)) ); break;
   			case DOWN: 	graphicset.getCharacter().setTransform( new Rotate(90 , new Point3D(-1, 0, 0)) ); break;
   			case L: graphicset.moveUp(); break;
   			case M: graphicset.moveDown(); break;
		}
		if(isLoggedIn) {
			if(e.getCode().equals(KeyCode.A) || e.getCode().equals(KeyCode.D) || e.getCode().equals(KeyCode.W) || e.getCode().equals(KeyCode.S)){
				UserInfo user = new UserInfo(graphicset.getCharacter().getName(), graphicset.getCharacter().getCharacterName());
				MessageComponent messagecomp = new MessageComponent(user, graphicset.getCharacter().getTranslate(), "action");
				sendMessageToServer(messagecomp);
			}else if(e.getCode().equals(KeyCode.LEFT) || e.getCode().equals(KeyCode.RIGHT) || e.getCode().equals(KeyCode.UP) || e.getCode().equals(KeyCode.DOWN)){
				UserInfo user = new UserInfo(graphicset.getCharacter().getName(), graphicset.getCharacter().getCharacterName());
				String rotate = String.valueOf(  graphicset.getCharacter().getDirection() );
				MessageComponent messagecomp = new MessageComponent(user, rotate, "rotation");
				sendMessageToServer(messagecomp);
			}
		}
		if(graphicset.getCharacter().collision( graphicset.perio)) System.out.println("Crash!");
		else System.out.println("Not crash.");
	}
	
	public MenuBar getMenuBar() {
		 MenuBar menuBar = new MenuBar();
		 menuBar.setUseSystemMenuBar(true);
		 
	     // --- Menu File
	     Menu Edit = new Menu("Edit");
	     MenuItem Edit_Sizue = new MenuItem("Ironman85", new ImageView(img_url[1]));
	     Edit_Sizue.setOnAction(e -> renewCharacter(graphicset.getCharacter(), "Iron Man 85") );
	     MenuItem Edit_Villager = new MenuItem("Peripper", new ImageView(img_url[2]));
	     Edit_Villager.setOnAction(e -> renewCharacter(graphicset.getCharacter(), "Peripper") );
	     MenuItem Edit_Spidy = new MenuItem("Spiderman", new ImageView(img_url[2]));
	     Edit_Spidy.setOnAction(e -> renewCharacter(graphicset.getCharacter(), "Spider Man") );
	     MenuItem Edit_Shinio = new MenuItem("Shinio", new ImageView(img_url[2]));
	     Edit_Shinio.setOnAction(e -> renewCharacter(graphicset.getCharacter(), "Shinio") );
	     Edit.getItems().addAll(Edit_Sizue, Edit_Villager, Edit_Spidy, Edit_Shinio);
	     
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
	
	public void renewCharacter(Character3DSet character3dset, String character) {
		character3dset = graphicset.renewCharacter(character3dset, character);
		graphicset.setCharacter(character3dset);
		if(isLoggedIn) {
			UserInfo userinfo = new UserInfo(character3dset.getName(), character3dset.getCharacterName());
			sendMessageToServer( new MessageComponent(userinfo, "change character design", "character") );
		}
	}
	
	private void sendMessageToServer(MessageComponent messagecomp) {
		try {
			ObjectOutputStream sender = new ObjectOutputStream(socket.getOutputStream());
			sender.writeObject(messagecomp);
		} catch (Exception e) { e.printStackTrace(); }
	}
		
	private void showBox(Object box) {
		if( ((Node) box).isVisible() ) ((Node) box).setVisible(false);
		else ((Node) box).setVisible(true);
	}
		
	private void connectToServer(String ipAddress, String portNumber, String userName) {
		System.out.println("----- connect to server -----\nUser Name: "+userName);
		try {
			socket = new Socket(ipAddress, Integer.parseInt(portNumber));

			UserInfo user = new UserInfo(userName, graphicset.getCharacter().getCharacterName());
			MessageComponent messagecomp = new MessageComponent(user, "requect connection");
			sendMessageToServer(messagecomp);
				
			ObjectInputStream receiver = new ObjectInputStream(socket.getInputStream());
			MessageComponent messagecompreceive = (MessageComponent)receiver.readObject();
			logboard.setMessage(messagecompreceive);
				
			Client3DThread clientthread = new Client3DThread(socket, user);
			clientthread.start();
			
			settingboard.getChildren().add(new Label(ipAddress+"\n"+portNumber+"\n"+userName));
			interfacePane.getChildren().remove(ipBox);
			isLoggedIn = true; 
			
			graphicset.getCharacter().setName(userName);
		}catch(Exception e) {
			logboard.setMessage(new MessageComponent("Client GUI", "Connection fails."));
			e.printStackTrace();
		}
	}
		
	public static void setMessageOnLogBoard(MessageComponent messagecomp) { logboard.setMessage(messagecomp); }
	public static void setMessageOnMessageBoard(MessageComponent messagecomp) { messageboard.setMessage(messagecomp); }
		
	public HBox createIPAddressInput(String ipaddress, String portnumber, String username, double layoutX, double layoutY) {
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
		serverButton.setStyle("-fx-background-color: none; -fx-background-image: url(\"sns/client/images/tanukichi_70x70.png\"); -fx-background-repeat: no-repeat;");
		serverButton.setOnAction(e -> connectToServer(ipText.getText(), portNumber.getText(), userName.getText()));
			
		Button showIpText = new Button();
		showIpText.setPrefSize(40, 40);
		showIpText.setStyle("-fx-background-color: none; -fx-background-image: url(\"sns/client/images/icon_ipaddress_40x40.png\"); -fx-background-repeat: no-repeat;");
		HBox ipHBox = new HBox(inputVBox, serverButton);
		showIpText.setOnAction(e -> showBox(ipHBox));
		ipBox = new HBox(ipHBox, showIpText);
		
		ipBox.setLayoutX(layoutX);
		ipBox.setLayoutY(layoutY);
			
		return ipBox;
	}
		
	public static void enterFriend(MessageComponent message) {
		Character3DSet character3dset = graphicset.replaceCharacter((String)message.getObject("Sender"), message.getCharacter());
		friendsset.put((String)message.getObject("Sender"), character3dset);
		Platform.runLater(new Runnable() {
			@Override
			public void run() { graphicset.getGroup().getChildren().add( friendsset.get((String)message.getObject("Sender")).getBody() ); }
		});
	}
	
	
	public static void changeFriendsCharacter(MessageComponent messagecomp) {
		String friend = (String)messagecomp.getObject("Sender");
		double x = friendsset.get(friend).getTranslateX(), y = friendsset.get(friend).getTranslateY(), z = friendsset.get(friend).getTranslateZ();
		int direction = friendsset.get(friend).getDirection();
		removeFriend(friend);
		enterFriend(messagecomp);
		friendsset.get(friend).setTranslate(x, y, z);
	}
		
	public static HashMap<String, Character3DSet> getFriendsSet(){ return friendsset; }
	
	public static void removeFriend(String friend) {
		Group friendbody = friendsset.get(friend).getBody();
		Platform.runLater(new Runnable() {
    	    @Override
    	    public void run() { graphicset.getGroup().getChildren().remove(friendbody); }
		});
		friendsset.remove(friend);
	}
		
	public static void setFriendBubbleSpeech(MessageComponent messagecomp) {
		String friend_name = (String)messagecomp.getObject("Sender");
		if(friendsset.containsKey(friend_name)) {
			String message = (String)messagecomp.getObject("Message");
			Platform.runLater(new Runnable() {
	    	    @Override
	    	    public void run() { friendsset.get(friend_name).setSpeech(message); }
			});
		}
	}
}