package commom;

public class UserPrivacy extends UserInfo{
	private String email, emailpass;
	
	public UserPrivacy(String name, String character, String email, String emailpass) {
		super(name, character);
		this.email = email;
		this.emailpass = emailpass;
	}
	
	public UserPrivacy(UserInfo userinfo, String email, String emailpass) {
		this(userinfo.getUserName(), userinfo.getCharacter(), email, emailpass);
	}
	
	public String getEmail() {return this.email;}
	public String getEmailPass() {return this.emailpass;}
	
	public String toString() {return super.toString()+", "+email+", "+emailpass;}
}
