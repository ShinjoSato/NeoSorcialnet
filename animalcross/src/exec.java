import javafx.application.Application;
import javafx.stage.Stage;
import sns.*;
import sns.client.Main3D;

public class exec extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Main3D m3d = new Main3D();
		try {
			m3d.start(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
