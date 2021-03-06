package sns.client;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sns.common.MessageComponent;

public class LogBoard extends Board{
	public LogBoard(double width, double height) {
		super(width, height);
		Label text = getMessageLabel("Log Board");
		board.getChildren().add(text);
		board.setStyle("-fx-padding: 5 5 5 5; -fx-background-color: rgba(0,0,0,0.3);");
	}
	
	public Label getMessageLabel(String message) {
		Label msg = new Label(message);
		msg.setStyle("-fx-text-fill: \"white\"; -fx-font-size: 10pt;");
		msg.setWrapText(true);
		return msg;
	}
	
	public void setMessage(MessageComponent messagecomp) {
		Label messageLabel = getMessageLabel(messagecomp.getSender()+": "+(String)messagecomp.getObject("Message"));
		HBox hbox = new HBox(messageLabel);
		Platform.runLater(new Runnable() {
    	    @Override
    	    public void run() {
    	    	getBoard().getChildren().add(hbox);
    	    }
		});
	}
	
	public void setTestcase(Board board, HBox hbox) {
		this.board.getChildren().add(hbox);
	}
}
