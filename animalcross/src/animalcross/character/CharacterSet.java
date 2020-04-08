package animalcross.character;

import java.io.File;
import java.io.Serializable;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class CharacterSet implements Serializable {
	private String name;
	private ImageView img;
	protected VBox model, bubbleSpeech, speechVBox;
	
	public CharacterSet(String name, double x, double y) {
		this.bubbleSpeech = new VBox();
		this.speechVBox = new VBox();
		model = new VBox();
		model.setAlignment(Pos.CENTER);
		setPosition(x, y);

		this.name = name;
		this.setSpeech("Hello, world");
		model.getChildren().add(speechVBox);
	}
	
	public CharacterSet(String name) {
		this(name, 0, 0);
	}
	
	public VBox getModel() {
		return this.model;
	}
	
	public void setPosition(double x, double y) {
		this.model.setLayoutX(x);
		this.model.setLayoutY(y);
	}
	
	public void setSpeech(String text) {
		if(speechVBox == null)		speechVBox = new VBox();
		else speechVBox.getChildren().clear();

		Label nameLabel = (Label) getObject("name");
		nameLabel.setWrapText(true);
		speechVBox.getChildren().add(nameLabel);
		
		bubbleSpeech = (VBox) getObject("bubbleSpeech");
		
		Label speech = new Label(text);
		speech.setStyle("-fx-font-family: \"Comic Sans MS\"; -fx-text-fill: chocolate;");
		speech.setWrapText(true);
		bubbleSpeech.getChildren().add(speech);

		speechVBox.getChildren().add(bubbleSpeech);
	}
	
	public Object getObject(String content) {
		Object object;
		switch(content) {
			case "bubbleSpeech":
				object = new VBox();
				((Region) object).setPrefSize(210, 100);
				((Node) object).setStyle("-fx-background-color: #FFE4B5; -fx-padding: 2 3 2 3; -fx-border-width: 2; -fx-border-color: orange; -fx-background-radius: 15; -fx-border-radius: 15;");
				break;
			case "name":
				object = new Label(name);
				((Node) object).setTranslateY(-3);
				((Node) object).setStyle("-fx-background-color: #87cefa; -fx-text-fill: white; -fx-padding: 2 5 2 5; -fx-background-radius: 15; ");
				break;
			default:
				object = new Object();
				System.out.println("DOESN'T EXIST "+content);
		}
		return object;
	}
	
	public void setPoint(double x, double y) {
		setX(x); setY(y);
	}
	
	public void setX(double x) {
		this.model.setLayoutX(x);
	}
	
	public void setY(double y) {
		this.model.setLayoutY(y);
	}
	
	public String getPoint() {
		return String.valueOf(getX()+","+getY());
	}
	
	public double getX() {
		return this.model.getLayoutX();
	}
	
	public double getY() {
		return this.model.getLayoutY();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		setSpeech("");
	}
	
	public abstract String getCharacterName();
}
