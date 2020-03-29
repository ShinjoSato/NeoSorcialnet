package animalcross.character;

import java.io.File;

import javafx.scene.image.ImageView;

public class Villager extends CharacterSet{
	public Villager(String name) {
		super(name);
		model.getChildren().add(new ImageView(new File("./bin/animalcross/character/images/murabito_90x90.png").toURI().toString()));
	}

}
