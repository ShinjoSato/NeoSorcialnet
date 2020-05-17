package animalcross;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import object3d.Function3D;

public class CameraSetting extends Function3D{
	private PerspectiveCamera camera;
	private String type;
	
	public CameraSetting(String type, double translateX, double translateY, double translateZ) {
		super(0); // change 0 into 90.
		camera  = new PerspectiveCamera( true );
		switch(type) {
			case "Overview": 
				setOverview(0d, 310d, -600d); 
				setTransform( new Rotate(10 , new Point3D(-1, 0, 0)) );
				this.type="Overview"; 
				break;
			case "FPS": 
				setFPS(0d, 295d/* =376.0-40.0 */, -90d);//345
				this.type="FPS";
				break;
		}
	}
	
	public void setOverview(double translateX, double translateY, double translateZ) {
        camera.setFarClip( 4000d );
        setTranslate(translateX, translateY, translateZ);
	}
	
	public void setFPS(double translateX, double translateY, double translateZ) {
		camera.setFarClip(4000d);
		setTranslate(translateX, translateY, translateZ);
	}
	
	@Override
	public void turnRight() {
		if(type.equals("FPS")) setRotateAnimation( new Point3D(0, 1, 0), 90 );
		
		direction = direction+90;
		setDirection( direction );
	}
	
	@Override
	public void turnLeft() {
		if(type.equals("FPS")) setRotateAnimation( new Point3D(0, 1, 0), -90 );
		
		direction = (direction+270)%360;
		setDirection( direction );
	}
	
	public void move(double x, double y, double z) {
		switch(type) {
			case "Overview": setTranslate(x, y-50d, z-300d); break;
			case "FPS": setTranslateAnimation(x, y, z); break;//setTranslate(x, y, z); break;
		}
	}
	
	public PerspectiveCamera getCamera() { return this.camera; }
	public String getType() { return type; } 
	protected Node getTarget() { return camera; }
	public void setFarClip(double far) { camera.setFarClip(far); }	
}