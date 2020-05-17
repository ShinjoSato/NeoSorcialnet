package animalcross;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import menu.StartMenu;

public abstract class ScreenSet extends Application{
	protected static Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		ScreenSet.primaryStage = (primaryStage==null)? new Stage(): primaryStage;
	}
	
	public static Scene createNewScene(URL location, int width, int height) {
		FXMLLoader fxmlLoader = new FXMLLoader(location);
		Scene scene2 = null;
		try {
			scene2 = new Scene((Pane) fxmlLoader.load(), width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scene2;
	}

	public void makeScene(String fxmlfile, String title) throws Exception {
		//primaryStage.close();
		makeScene(fxmlfile, title, 520, 400);
	}

	public void makeScene(String fxmlfile, String title, int width, int height) throws Exception {
		URL location = getClass().getResource(fxmlfile);
		Scene scene = createNewScene(location, width, height);
		primaryStage.setScene(scene);
		primaryStage.setTitle(title);
		primaryStage.show();
	}
}
