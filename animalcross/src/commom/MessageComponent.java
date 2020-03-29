package commom;

import java.io.Serializable;
import animalcross.character.CharacterSet;
import animalcross.character.Sizue;
import animalcross.character.Villager;

@SuppressWarnings("serial")
public class MessageComponent implements Serializable {
//	private CharacterSet character;
	private String user;
	private String message;
	
	public MessageComponent(String user, String message) {
		//this.character = new Villager(user);
		this.user = user;
		this.message = message;
	}
	
	/*public MessageComponent(String user, String message, CharacterSet character) {
		this.character = character;
		this.user = user;
		this.message = message;
	}*/
	
	public String getSender() {
		return this.user;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	/*public CharacterSet getCharacter() {
		return this.character;
	}*/
}
