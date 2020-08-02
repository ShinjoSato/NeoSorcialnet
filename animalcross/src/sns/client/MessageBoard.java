package sns.client;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sns.client.obj2d.character.CharacterSet;
import sns.common.MessageComponent;

public class MessageBoard extends Board {
	public MessageBoard(double width, double height) {
		super(width, height);
		Label text = getMessageLabel("Message Board");
		board.getChildren().add(text);
		board.setStyle("-fx-padding: 5 5 5 5; -fx-background-color: #375646; -fx-border-width: 9; -fx-border-color: #8f6f54");
	}
	
	public Label getMessageLabel(String message) {
		Label msg = new Label(message);
		msg.setStyle("-fx-text-fill: \"white\"; -fx-font-size: 15pt;");
		msg.setWrapText(true);
		return msg;
	}
	
	public void setMessage(String name, String message) {
		Label nameLabel = new Label(name);
		Label messageLabel = getMessageLabel(message);
		HBox hbox = new HBox(nameLabel, messageLabel);
		
		this.board.getChildren().add(hbox);
	}
	
	public void setMessage(MessageComponent messagecomp) {
		System.out.println("setMessage: "+(String)messagecomp.getObject("Message"));
		Label name = new Label(messagecomp.getSender());//(Label) character.getObject("name");
		name.setTranslateY(-3);
		name.setStyle("-fx-background-color: #87cefa; -fx-text-fill: white; -fx-padding: 2 5 2 5; -fx-background-radius: 15; ");
		Label messageLabel = getMessageLabel((String)messagecomp.getObject("Message"));
		HBox hbox = new HBox(name, messageLabel);
		Platform.runLater(new Runnable() {
    	    @Override
    	    public void run() {
    	    	getBoard().getChildren().add(hbox);
    	    }
		});
	}
}
