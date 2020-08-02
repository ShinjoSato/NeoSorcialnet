package sns.client.obj2d.character;

import java.io.File;
import java.io.Serializable;

import javafx.scene.image.ImageView;

public class Sizue extends CharacterSet implements Serializable {
	protected String charactername;
	
	public Sizue(String name, double x, double y) {
		super(name, x, y);
		this.charactername = "Sizue";
		model.getChildren().add(new ImageView(new File(img_url[4]).toURI().toString()));
	}
	
	public Sizue(String name) {
		this(name, 0, 0);
	}
	
	public String getCharacterName() {
		return this.charactername;
	}
}
