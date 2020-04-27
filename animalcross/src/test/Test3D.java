package test;

import java.io.File;
import java.io.ObjectOutputStream;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import commom.MessageComponent;
import commom.UserInfo;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.LightBase;
import javafx.scene.Node;
import javafx.scene.ParallelCamera;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
 
public class Test3D extends Application {
 
    public static void main(String[] args){
        launch( args );
    }

    Node model;
    PerspectiveCamera camera;
    private int front = 0;
    private int camera_mode = 0;
  
    @Override
    public void start(Stage primaryStage) throws Exception{
        Group threeDgroup = create3DGroup(new Group());
        SubScene threeDscene = new SubScene(threeDgroup, 800, 700, true, SceneAntialiasing.BALANCED);
        threeDscene.setFill(Color.BEIGE);
        threeDscene.setCamera( camera);
        
        Pane userinterface = new Pane(new VBox(new Label("Hello,\n is this a pen?"), new Button("Click!")));
        
        Group   root        = new Group(threeDscene, userinterface);
        Scene   scene       = new Scene( root, 1000 , 750 , true );
        scene.setFill( Color.SKYBLUE );
        scene.setOnKeyPressed(e -> {
			try {keyPressed(e);} 
			catch (Exception except) {}
		});
        camera = setCameraFPS(camera);
        //camera = setCameraOverview(camera);
        
        // ウィンドウ表示
        primaryStage.setScene( scene );
        primaryStage.show();
    }
    
    
    public Group create3DGroup(Group group) {
        // モデル取込
        model = createModelFromObj( "src/test/obj/peripper.obj" );
        model = setTranslate(model, model.getTranslateX(), 700d-40d, 400d);
        group.getChildren().add( model );
    	
    	for(int i=0; i<20; i++) {
        	for(int j=0; j<20; j++) {
        		Node m = createModelFromObj( "src/test/obj/sandblock.obj" );
        		m = setTranslate(m, 0d + 40d*i, 700d, 400d+40d*j);
        		group.getChildren().add(m);
        		if(0.8<Math.random()) {
        			Node wall = createModelFromObj("src/test/obj/wallblock.obj");
            		wall = setTranslate(wall, 0d + 40d*i, 700d-40d, 400d+40d*j);
        			group.getChildren().add(wall);            		
        		}else {
        			if(0.9 < Math.random()) {
            			Node ali = createModelFromObj("src/test/obj/music-alien.obj");
                		ali = setTranslate(ali, 0d + 40d*i,  700d-40d, 400d+40d*j);
                		group.getChildren().add(ali);
            		}
        		}
        	}
        }
    	group.getChildren().add(new VBox(new Label("Hello, world")));  	
    	
    	 // カメラ設定
        camera  = new PerspectiveCamera(true);
        camera.setTranslateZ( -40 );
        camera.setTranslateY(200d);
        camera.setFarClip(5000d);
        group.getChildren().add(camera);
    	
    	// 光源設定
        LightBase   light       = new PointLight();
        light.setTranslateZ( -100 );
        light.setTranslateY( -100 );
        group.getChildren().add( light );
        
    	return group;
    }  
    
    public PerspectiveCamera setCameraOverview(PerspectiveCamera camera) {
    	camera.setFarClip( 2000 );
    	camera.setTranslateY(-400);
    	camera.setTranslateZ( -500 );
    	camera.getTransforms().add( new Rotate( -40 , new Point3D( 1 , 0 , 0 ) ) );
    	return camera;
    }
    
    public PerspectiveCamera setCameraFPS(PerspectiveCamera camera) {
    	camera.setFarClip( 2000 );
    	camera.setTranslateY(630);
    	camera.setTranslateZ( 400 );
    	return camera;
    }
    
    private void keyPressed(KeyEvent e) throws Exception {
		try {
			double distance = 6d;
			double x = model.getTranslateX(), y = model.getTranslateY(), z = model.getTranslateZ();

			switch(e.getCode()) {
				case D:// Initial direction: East
					if(front == 0) goToEast(x, distance);
					else if(front == 3) goToSouth(z, distance);
					else if(front == 6) goToWest(x, distance);
					else if(front == 9) goToNorth(z, distance);
					break;
				case A:// Initial direction: West
					if(front == 0) goToWest(x, distance);
					else if(front == 3) goToNorth(z, distance);
					else if(front == 6) goToEast(x, distance);
					else if(front == 9) goToSouth(z, distance);
					break;
				case W:// Initial direction: North
					if(front == 0) goToNorth(z, distance);
					else if(front == 3) goToEast(x, distance);
					else if(front == 6) goToSouth(z, distance);
					else if(front == 9) goToWest(x, distance);
					break;
				case S:// Initial direction: South
					if(front == 0) goToSouth(z, distance);
					else if(front == 3) goToWest(x, distance);
					else if(front == 6) goToNorth(z, distance);
					else if(front == 9) goToEast(x, distance);
					break;
				case P:
					camera_mode = (camera_mode+1)%2;
					System.out.println(camera_mode);
					switch(camera_mode) {
						//case 0: setCameraOverview(); break;
						case 1: camera = setCameraFPS(camera); break;
					}
				case LEFT:
					camera.getTransforms().add( new Rotate( -90 , new Point3D( 0 , 1 , 0 ) ) );
					front = (front - 3 + 12)%12;
					break;
				case RIGHT:	 
					camera.getTransforms().add( new Rotate( 90 , new Point3D( 0 , 1 , 0 ) ) );
					front = (front + 3)%12;
					break;
				case UP:
					camera.getTransforms().add( new Rotate( 15 , new Point3D( 1 , 0 , 0 ) ) );
					break;
				case DOWN: 
					camera.getTransforms().add( new Rotate( -15 , new Point3D( 1 , 0 , 0 ) ) );
					break;
				case SPACE:
					camera.setTranslateY( camera.getTranslateY() + 2*distance);
				default: break;
			}
			System.out.println("Camera tansfer:"+camera.getTranslateX()+", "+camera.getTranslateY()+", "+camera.getTranslateZ());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
    
    public void goToEast(double model_x, double distance) {
    	model.setTranslateX(model_x + distance);
		camera.setTranslateX( camera.getTranslateX() + distance);
    }
    
    public void goToSouth(double model_z, double distance) {
    	model.setTranslateZ(model_z - distance);
		camera.setTranslateZ( camera.getTranslateZ() - distance);
    }
    
    public void goToWest(double model_x, double distance) {
    	model.setTranslateX(model_x - distance);
		camera.setTranslateX( camera.getTranslateX() - distance);
    }
    
    public void goToNorth(double model_z, double distance) {
    	model.setTranslateZ(model_z + distance);
		camera.setTranslateZ( camera.getTranslateZ() + distance);
    }
    
    public Node setTranslate(Node node, double x, double y, double z) {
    	node.setTranslateX(x);
    	node.setTranslateY(y);
    	node.setTranslateZ(z);
    	return node;
    }
     
    public Node createModelFromObj( String objectfile ){
        Group   root   = new Group();
        ObjModelImporter    importer    = new ObjModelImporter();
        try{
        	String url =  new File( objectfile ).toURI().toString();
            importer.read( url );
        }catch( ImportException e ){
            e.printStackTrace();
        }
        Node[]  meshes  = importer.getImport();
        root.getChildren().addAll( meshes );
        importer.close();
         
        return root;
    }
}