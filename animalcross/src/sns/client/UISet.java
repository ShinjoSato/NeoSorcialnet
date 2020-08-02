package sns.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sns.common.Mail;
import sns.common.SaveData;
import sns.common.UserInfo;

public class UISet {
	
	/*public static VBox createIPAddressIcon(String ipaddress, String portnumber, String username) {
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
		//ip_vbox.setLayoutX(200.0);
		//ip_vbox.setLayoutY(20.0);
			
		//avator.setName(username);
		//return ip_vbox;
	//}*/
	

	protected static VBox createEmailInput() {		
		Button isVisible = new Button();
		//isVisible.setOnAction(e-> showBox(mailVBox));
		isVisible.setOnAction(e->{
			emailScreen email = new emailScreen();
			try { email.start(new Stage()); } 
			catch (Exception e1) { e1.printStackTrace(); }
		});
		isVisible.setPrefSize(106, 90);
		isVisible.setStyle("-fx-background-color: none; -fx-background-image: url(\"./sns/client/obj2d/character/images/perio_106x90.png\"); -fx-background-repeat: no-repeat;");
		
		VBox all = new VBox(/*mailVBox,*/ isVisible);
		all.setTranslateY(500d);
		return all;
	}
	
	private static void showBox(Object box) {
		if( ((Node) box).isVisible() ) ((Node) box).setVisible(false);
		else ((Node) box).setVisible(true);
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
	
	public static SaveData reloadSaveData() {
		SaveData savedata = null;
		try(FileInputStream f = new FileInputStream("data/renshu.dat");
			BufferedInputStream b = new BufferedInputStream(f);
			ObjectInputStream in = new ObjectInputStream(b)){
	 
			savedata = (SaveData) in.readObject();
	 
			System.out.println(savedata);			
		} catch ( IOException e ) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return savedata;
	}
	
	
}
