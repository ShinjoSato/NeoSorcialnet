package animalcross.character;

import java.io.File;
import java.io.Serializable;

import javafx.scene.image.ImageView;

public class Perio  extends CharacterSet implements Serializable {
	protected String charactername;
	
	public Perio(String name, double x, double y) {
		super(name, x, y);
		this.charactername = "Perio";
		model.getChildren().add(new ImageView(new File("./bin/animalcross/character/images/perio_106x90.png").toURI().toString()));
	}
	
	public Perio(String name) {
		this(name, 0, 0);
	}
	
	public String getCharacterName() {
		return this.charactername;
	}
}