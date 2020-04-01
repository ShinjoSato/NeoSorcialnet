package animalcross.character;

import java.io.File;

import javafx.scene.image.ImageView;

public class Villager extends CharacterSet{
	protected String charactername;
	
	public Villager(String name) {
		super(name);
		this.charactername = "Villager";
		model.getChildren().add(new ImageView(new File("./bin/animalcross/character/images/murabito_90x90.png").toURI().toString()));
	}
	
	public String getCharacterName() {
		return this.charactername;
	}
}
