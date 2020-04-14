package commom;

import java.io.Serializable;

public class UserInfo implements Serializable{
	final private String name;
	final private String character;
	
	public UserInfo(String name, String character) {
		this.name = name;
		this.character = character;
	}
	
	public String getUserName() {
		return this.name;
	}
	
	public String getCharacter() {
		return this.character;
	}
	
	public String toString() {
		return "Name: "+name+", Character: "+character;
	}
}
