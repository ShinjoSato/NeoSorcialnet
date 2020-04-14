package animalcross.character;

import java.io.File;
import java.io.Serializable;

import javafx.scene.image.ImageView;

public class Sizue extends CharacterSet implements Serializable {
	protected String charactername;
	
	public Sizue(String name, double x, double y) {
		super(name, x, y);
		this.charactername = "Sizue";
		model.getChildren().add(new ImageView(new File("./bin/animalcross/character/images/sizue_53x90.png").toURI().toString()));
	}
	
	public Sizue(String name) {
		this(name, 0, 0);
	}
	
	public String getCharacterName() {
		return this.charactername;
	}
}
