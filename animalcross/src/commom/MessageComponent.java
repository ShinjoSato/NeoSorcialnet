package commom;

import java.io.Serializable;
import animalcross.character.CharacterSet;
import animalcross.character.Sizue;
import animalcross.character.Villager;

public class MessageComponent implements Serializable{
	final private String message;
	final private UserInfo userinfo;
	final private String messagetype;
	
	public MessageComponent(UserInfo userinfo, String message, String messagetype) {
		this.userinfo = userinfo;
		this.message = message;
		this.messagetype = messagetype;
	}
	
	public MessageComponent(UserInfo user, String message) {
		this(user, message, "message");
	}	
	
	public MessageComponent(String user, String message) {
		this(new UserInfo(user, "none"), message);
	}
	
	public Object getObject(String objectname) {
		Object object = new Object();
		switch(objectname) {
			case "Message":		object = this.message;					break;
			case "Character":	object = this.userinfo.getCharacter();	break;
			case "UserInfo":	object = this.userinfo;					break;
			case "Sender":		object = this.userinfo.getUserName();	break;
			case "MessageType":	object = this.messagetype;				break;
		}
		return object;
	}
	
	public String getSender() {
		return this.userinfo.getUserName();
	}
	
	
	public String getCharacter() {
		return this.userinfo.getCharacter();
	}
	
	public UserInfo getUserInfo() {
		return this.userinfo;
	}
	
	public String toString() {
		return "User:["+userinfo+"], MessageType: "+messagetype+", Message: "+message;
	}
}
