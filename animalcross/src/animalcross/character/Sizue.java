package animalcross.character;

import java.io.File;
import java.io.Serializable;

import javafx.scene.image.ImageView;

public class Sizue extends CharacterSet implements Serializable {
	public Sizue(String name) {
		super(name);
		model.getChildren().add(new ImageView(new File("./bin/animalcross/character/images/sizue_53x90.png").toURI().toString()));
	}
}
