package animalcross;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class Board {
    protected VBox board;
	protected VBox frame;
    
	public Board(double width, double height) {//350, 460
	    board = new VBox();
		board.setPrefHeight(height);
		board.setPrefWidth(width);
		
		ScrollPane scrollPane = new ScrollPane(board);
	    scrollPane.setFitToHeight(true);
	    scrollPane.setVvalue(100.0);
	    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
	    scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
	    scrollPane.setStyle("-fx-background-color:transparent;");
		scrollPane.widthProperty().addListener((o) -> {
	        Node vp = scrollPane.lookup(".viewport");
	        vp.setStyle("-fx-background-color: transparent;");
	    });
		
	    frame = new VBox(scrollPane);
	    frame.setPrefHeight(height);
	    frame.setPrefWidth(width);
	}
	
	public boolean isVisible() {
		return this.frame.isVisible();
	}
	
	public void setVisible(boolean isVisible) {
		this.frame.setVisible(isVisible);
	}
	
	public VBox getFrame(double layoutX, double layoutY) {
		frame.setLayoutX(layoutX);
		frame.setLayoutY(layoutY);
		return this.frame;
	}
	
	public VBox getBoard() {
		return this.board;
	}
	
	public abstract Label getMessageLabel(String message);
}
