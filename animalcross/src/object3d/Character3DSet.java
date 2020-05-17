package object3d;

import java.io.File;
import java.io.Serializable;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class Character3DSet extends Object3D implements Serializable {
	private String name;
	protected VBox bubbleSpeech, speechVBox;
	
	public Character3DSet(String filename, String name, double x, double y, double z) {
		super(filename, x, y, z);
		this.bubbleSpeech = new VBox();
		this.speechVBox = new VBox();
		
		this.name = name;
		this.setSpeech("Hello, world");
		speechVBox.setTranslateX(-50d);
		speechVBox.setTranslateY(-120d);
		speechVBox.setVisible(false);

		body.getChildren().add(speechVBox);
		setTranslate(x, y, z);
	}
	
	public Character3DSet(String filename) {
		this(filename, "name here", 0, 0, 0);
	}

	public void setSpeech(String text) {
		if(speechVBox == null)		speechVBox = new VBox();
		else speechVBox.getChildren().clear();
		speechVBox.getChildren().add(getNameLabel());
		
		bubbleSpeech = getBubbleSpeech();	
		Label speech = new Label(text);
		speech.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-text-fill: chocolate;");
		speech.setWrapText(true);
		bubbleSpeech.getChildren().add(speech);

		speechVBox.getChildren().add(bubbleSpeech);
		speechVBox.setVisible(true);
	}
	
	private Label getNameLabel() { 
		Label label = new Label(name);
		label.setTranslateY(-3);
		label.setWrapText(true);
		label.setStyle("-fx-font-size: 7; -fx-background-color: #87cefa; -fx-text-fill: white; -fx-padding: 2 5 2 5; -fx-background-radius: 15; ");
		return label;
	}
	
	public VBox getBubbleSpeech() {
		VBox vbox = new VBox();
		vbox.setPrefSize(90, 40);
		vbox.setStyle("-fx-font-size: 7; -fx-background-color: #FFE4B5; -fx-padding: 2 3 2 3; -fx-border-width: 2; -fx-border-color: orange; -fx-background-radius: 15; -fx-border-radius: 15;");
		return vbox;
	}
	
	public String getName() { return name; }
	
	public void setName(String name) {
		this.name = name;
		setSpeech("");
	}
	
	public abstract double getStep();
	public abstract double getTall();
	public abstract String getCharacterName();
}
