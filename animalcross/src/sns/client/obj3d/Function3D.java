package sns.client.obj3d;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public abstract class Function3D {
	/**
	 * This direction is "clockwise", so be careful to use!
	 */
	protected int direction;// change 0<=direction<12 into 0<=direction<360
	
	public Function3D(int direction) {
		this.direction = direction;
	}
	
	public void turnRight() {
		direction = (direction+90)%360;
		setDirection( direction );
		setTransform( new Rotate(90 , new Point3D(0, 1, 0)) );
		//setRotateAnimation( new Point3D(0, 1, 0), 3 );
	}
	
	public void turnLeft() {
		direction = (direction-90+360)%360;
		setDirection( direction );
		setTransform( new Rotate(-90 , new Point3D(0, 1, 0)) );
		//setRotateAnimation( new Point3D(0, -1, 0), -3 );
	}
	
	public void setTranslateAnimation(double x, double y, double z) {
		TranslateTransition animation = new TranslateTransition(Duration.seconds(0.2d), getTarget());
		animation.setFromX(getTranslateX()); animation.setToX(x);
		animation.setFromY(getTranslateY()); animation.setToY(y);
		animation.setFromZ(getTranslateZ()); animation.setToZ(z);
		animation.play();
	}
	
	public void setTranslateAnimationX(double x, double footstep) {
		TranslateTransition animation = new TranslateTransition(Duration.seconds(0.2d), getTarget());
		animation.setFromX(x); animation.setToX(x+footstep);
		animation.play();
	}
	
	public void setTranslateAnimationY(double y, double footstep) {
		TranslateTransition animation = new TranslateTransition(Duration.seconds(0.2d), getTarget());
		animation.setFromY(y); animation.setToX(y+footstep);
		animation.play();
	}
	
	public void setTranslateAnimationZ(double z, double footstep) {
		TranslateTransition animation = new TranslateTransition(Duration.seconds(0.2d), getTarget());
		animation.setFromX(z); animation.setToZ(z+footstep);
		animation.play();
	}
	
	public void setRotateAnimation(Point3D axis, int degree) {
		RotateTransition animation = new RotateTransition(Duration.seconds(0.6d), getTarget());
		animation.setCycleCount(1);
		animation.setAxis(axis);
		
		double from = direction, 		to = (direction+degree);
		animation.setFromAngle( from ); animation.setToAngle( to );
		animation.play();
	}
	
	public void moveFront(double x, double z, double footstep) {
		if(direction==90) 		setTranslateX(x+footstep);
		else if(direction==270) setTranslateX(x-footstep);
		else if(direction==180) setTranslateZ(z-footstep);
		else if(direction==0) 	setTranslateZ(z+footstep);
	}
	
	public void moveBack(double x, double z, double footstep) {
		if(direction==270) setTranslateX(x+footstep);
		else if(direction==90) setTranslateX(x-footstep);
		else if(direction==180) setTranslateZ(z+footstep);
		else if(direction==0) setTranslateZ(z-footstep);
	}
	
	public void moveLeft(double x, double z, double footstep) {
		if(direction==180) setTranslateX(x+footstep);
		else if(direction==0) setTranslateX(x-footstep);
		else if(direction==90) setTranslateZ(z+footstep);
		else if(direction==270) setTranslateZ(z-footstep);
	}
	
	public void moveRight(double x, double z, double footstep) {
		if(direction==180) setTranslateX(x-footstep);
		else if(direction==0) setTranslateX(x+footstep);
		else if(direction==90) setTranslateZ(z-footstep);
		else if(direction==270) setTranslateZ(z+footstep);
	}
	
	public double getTranslateX() { return getTarget().getTranslateX(); }
	public double getTranslateY() { return getTarget().getTranslateY(); }
	public double getTranslateZ() { return getTarget().getTranslateZ(); }
	public int getDirection() { return direction; }
	protected abstract Node getTarget();
	public void setTranslate(double x, double y, double z) { setTranslateX(x); setTranslateY(y); setTranslateZ(z); }
	public void setTranslateX(double x) { getTarget().setTranslateX(x); }
	public void setTranslateY(double y) { getTarget().setTranslateY(y); }
	public void setTranslateZ(double z) { getTarget().setTranslateZ(z); }
	public void setTransform(Rotate rotate) { getTarget().getTransforms().add(rotate); }
	public void setDirection(int direction) { this.direction = direction; }
}