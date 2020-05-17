package menu;

import java.io.IOException;
import java.net.URL;

import animalcross.Main3D;
import animalcross.ScreenSet;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartMenu extends ScreenSet{
	@Override
	public void start(Stage primaryStage) throws Exception {
		super.start(primaryStage);
		makeScene("fxml/start.fxml", "Shinio no Social Net");
	}

	@FXML
	protected void enter() {
		Main3D main3d = new Main3D();
		try {
			primaryStage.hide();
			main3d.start(new Stage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
