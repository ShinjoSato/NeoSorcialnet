package sns.client;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sns.common.Mail;

public class emailScreen extends ScreenSet {
	@FXML
	TextField sender, password, receiver, subject;
	@FXML
	TextArea message;

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		super.start(primaryStage);
		makeScene("menu/fxml/email.fxml", "Email", 600, 350);
	}

	@FXML
	public void sendEMail() {
		//System.out.println(message.getText());
		System.out.println( String.format("%s, %s, %s, %s, %s", sender.getText(), password.getText(), receiver.getText(), subject.getText(), message.getText()) );
		Mail mail = new Mail();
		mail.sendEMail(sender.getText(), password.getText(), receiver.getText(), subject.getText(), message.getText());
	}
}
