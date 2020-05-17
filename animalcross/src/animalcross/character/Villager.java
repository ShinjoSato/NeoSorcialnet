package animalcross.character;

import java.io.File;

import javafx.scene.image.ImageView;

public class Villager extends CharacterSet{
	protected String charactername;
	
	public Villager(String name, double x, double y) {
		super(name, x, y);
		this.charactername = "Villager";
		model.getChildren().add(new ImageView(new File(img_url[5]).toURI().toString()));
	}
	
	public Villager(String name) {
		this(name, 0, 0);
		
	}
	
	public String getCharacterName() {
		return this.charactername;
	}
}
