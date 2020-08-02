package sns.test;

import java.io.File;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.LightBase;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Test3d extends Application{
	public static void main(String[] args)
    {
        launch( args );
    }
 
	private Node avator;
	
    @Override
    public void start(Stage primaryStage) throws Exception
    {
    	// 3d components
        Group   group3d = new Group();
        for(int i=0; i<10; i++) {
        	for(int j=0; j<10; j++) {
            	Node sand = createModelFromObj("src/test3d/object/sandblock.obj");
    			group3d.getChildren().add(sand);
        		if(0.8 < Math.random()) {
        			Node alien = createModelFromObj("src/test3d/object/music-alien.obj");
        			group3d.getChildren().add(alien);
        		}
        	}
        }
        avator       = createModelFromObj( "src/test3d/object/peripper.obj" );
        group3d.getChildren().add( avator );
        
        // 3d setting
        SubScene   scene3d       = new SubScene( group3d , 800 , 650 );
        scene3d.setFill( Color.SKYBLUE);
        PerspectiveCamera   camera  = new PerspectiveCamera( true );
        camera.setFarClip( 1300d );
        camera.setTranslateZ( -500d );
        camera.setTranslateY( -20d );
        scene3d.setCamera( camera ); 
        // 光源設定
        LightBase   light       = new PointLight();
        light.setTranslateZ( -1500 );
        group3d.getChildren().add( light );
         
        // user interface
        VBox userinterface = new VBox(new Label("Hello, \nthis is a pen."), new Button("click here!"));
        
        Group rootGroup = new Group(scene3d, userinterface);
        Scene mainScene = new Scene(rootGroup, 1000, 750, true);
        mainScene.setOnKeyPressed(e -> {
			try {keyPressed3D(e);} 
			catch (Exception e1) {}
		});
        // ウィンドウ表示
        primaryStage.setScene( mainScene );
        primaryStage.show();
         
    }
     
    private void keyPressed3D(KeyEvent e) {
    	System.out.println("click");
		double distance = 20d;
    	switch(e.getCode()) {
    		case A: avator.setTranslateX( avator.getTranslateX() - distance); break;
    		case D: avator.setTranslateX( avator.getTranslateX() + distance); break;
    		case W: avator.setTranslateZ( avator.getTranslateZ() + distance); break;
    		case S: avator.setTranslateZ( avator.getTranslateZ() - distance); break;
    	}
	}

    public Node createModelFromObj( String filename ){
    	String  url         = new File( filename ).toURI().toString();
        Group   root   = new Group();
        ObjModelImporter    importer    = new ObjModelImporter();
        try{
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
