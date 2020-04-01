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
	
	public CharacterSet(String name) {
		this.bubbleSpeech = new VBox();
		this.speechVBox = new VBox();
		model = (VBox) getObject("model");
		this.name = name;
		this.setSpeech("Hello, world");
		model.getChildren().add(speechVBox);
	}
	
	public VBox getModel() {
		return this.model;
	}
	
	public void setSpeech(String text) {
		if(speechVBox == null)		speechVBox = new VBox();
		else speechVBox.getChildren().clear();

		Label nameLabel = (Label) getObject("name");
		nameLabel.setWrapText(true);
		speechVBox.getChildren().add(nameLabel);
		
		bubbleSpeech = (VBox) getObject("bubbleSpeech");
		
		Label speech = new Label(text);
		speech.setWrapText(true);
		bubbleSpeech.getChildren().add(speech);

		speechVBox.getChildren().add(bubbleSpeech);
	}
	
	public Object getObject(String content) {
		Object object;
		switch(content) {
			case "model":
				object = new VBox();
				((VBox) object).setAlignment(Pos.CENTER);
				break;
			case "bubbleSpeech":
				object = new VBox();
				((Region) object).setPrefSize(130, 40);
				((Node) object).setStyle("-fx-background-color: #FFE4B5; -fx-padding: 2 2 2 2;");
				break;
			case "name":
				object = new Label(name);
				((Node) object).setTranslateY(-3);
				((Node) object).setStyle("-fx-background-color: #87cefa; -fx-text-fill: white; -fx-padding: 2 5 2 5; -fx-background-radius: 15; ");
				break;
			default:
				object = new Object();
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
