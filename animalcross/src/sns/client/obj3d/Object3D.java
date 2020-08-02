package sns.client.obj3d;

import java.io.File;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import sns.common.itemInterface;

public abstract class Object3D extends Function3D implements itemInterface{
	private Node node;
	protected Group body;
	
	public Object3D(String filename, double positionX, double positionY, double positionZ) {
		super(180); // change 6 into 180.
		this.node = createModelFromObj(filename);
		this.body = new Group(node);
		this.setTranslate(positionX, positionY, positionZ);	
	}

	public Object3D(String filename) { this(filename, 0, 0, 0); }
	
	public Node createModelFromObj( String filename ){
		File file = new File(filename);
		String url = file.getAbsolutePath();
//    	String  url         = new File( filename ).toURI().toString();
        Group   root   = new Group();
        ObjModelImporter    importer    = new ObjModelImporter();
        try{ importer.read( url ); }
        catch( ImportException e ){ e.printStackTrace(); }
        Node[]  meshes  = importer.getImport();
        root.getChildren().addAll( meshes );
        importer.close();
        return root;
    }
	
	public boolean collision(Object3D object) {
		if( Math.abs((getWidth()/2+getTranslateX()) - (object.getWidth()/2+object.getTranslateX())) < (getWidth()+object.getWidth())/2 &&
			Math.abs((getHeight()/2+getTranslateY()) - (object.getHeight()/2+object.getTranslateY())) < (getHeight()+object.getHeight())/2 &&
			Math.abs((getDepth()/2+getTranslateZ()) - (object.getDepth()/2+object.getTranslateZ())) < (getDepth()+object.getDepth())/2) {
			return true;
		}else {
			return false;
		}
	}
	
	public Group getBody() { return this.body; }
	public void setNode(Node node) {this.node = node;}
	public String getTranslate() {	return String.format("%f,%f,%f", getTranslateX(), getTranslateY(), getTranslateZ()); }
	public Node getNode() {return node;}
	public double getWidth() {
		if(direction==270 || direction==90)/*(direction%6==0)*/ return body.getBoundsInParent().getWidth();
		else return body.getBoundsInParent().getDepth();
	}
	public double getHeight() {return body.getBoundsInParent().getHeight(); }
	public double getDepth() {
		if(direction==270 || direction==90) return body.getBoundsInParent().getDepth();
		else  return body.getBoundsInParent().getWidth();
	}
	protected Node getTarget() { return body; }
}
