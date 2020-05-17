package animalcross;

import java.util.ArrayList;

import commom.Coordinates;
import commom.MessageComponent;
import commom.SpacialIndex;
import commom.UserInfo;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import object3d.Character3DSet;
import object3d.Object3D;
import object3d.character.Ironman85;
import object3d.character.Peripper;
import object3d.character.Shinio;
import object3d.character.Skeleton;
import object3d.character.Spiderman;

public class GraphicSet {
	private Character3DSet character;
	private CameraSetting camera;
	private Group group;
	private SubScene subscene;
	private Field3D field3d;
	private Coordinates initial;

	public Character3DSet perio;
		
	public GraphicSet() {
		initial = new Coordinates(-520d, 400d, -520d);
		this.subscene = create3DScene();
		
		perio = new Skeleton( "Perio", 0d, 376d, 20d );
		group.getChildren().add(perio.getBody());
	}
	
	public SubScene create3DScene() {
        group = new Group();
        character = new Ironman85( "name from create3Dscene", 0d, initial.Y-24d, 0d);
        group.getChildren().add( character.getBody());
        
        field3d = new Field3D( initial );
        group.getChildren().add(field3d.getGroup());

        // 3d setting
        SubScene   scene3d       = new SubScene( group, 800 , 650, true, SceneAntialiasing.BALANCED );
        scene3d.setFill( Color.SKYBLUE);

        camera = new CameraSetting("FPS", character.getTranslateX(), character.getTranslateY(), character.getTranslateZ());
        if(true) {
        	// It need to send to the server to tell this character rotates.
        	character.turnLeft();
        	character.turnLeft();
        }
        scene3d.setCamera( camera.getCamera() ); 
        
        return scene3d;
	}
	
	public void moveFront() {
		Character3DSet test = createPuppet(character);
		test.moveFront(test.getTranslateX(), test.getTranslateZ(), test.getStep());
		
		if(canMove(test)) {
			/*double x = test.getTranslateX(), y = test.getTranslateY(), z = test.getTranslateZ();*/ // It doesn't work properly.
			
			character.moveFront(character.getTranslateX(), character.getTranslateZ(), character.getStep());
			camera.move(character.getTranslateX(), character.getTranslateY()-character.getTall()-4d, character.getTranslateZ());
		}
	}
	
	public void moveBack() {
		Character3DSet test = createPuppet(character);
		test.moveBack(test.getTranslateX(), test.getTranslateZ(), test.getStep());
		
		if(canMove(test)) {
			character.moveBack(character.getTranslateX(), character.getTranslateZ(), character.getStep());
			camera.move(character.getTranslateX(), character.getTranslateY()-character.getTall()-4d, character.getTranslateZ());
		}
	}
	
	public void moveRight() {
		Character3DSet test = createPuppet(character);
		test.moveRight(test.getTranslateX(), test.getTranslateZ(), test.getStep());
		
		if(canMove(test)) {
			character.moveRight(character.getTranslateX(), character.getTranslateZ(), character.getStep());
			camera.move(character.getTranslateX(), character.getTranslateY()-character.getTall()-4d, character.getTranslateZ());
		}
	}
	
	public void moveLeft() {
		Character3DSet test = createPuppet(character);
		test.moveLeft(test.getTranslateX(), test.getTranslateZ(), test.getStep());
		
		if(canMove(test)) {
			character.moveLeft(character.getTranslateX(), character.getTranslateZ(), character.getStep());
			camera.move(character.getTranslateX(), character.getTranslateY()-character.getTall()-4d, character.getTranslateZ());
		}
	}
	
	public void moveUp() {
		Character3DSet test = createPuppet(character);
		test.setTranslateY( character.getTranslateY()-24d );
		
		if(canMove(test)) {
			character.setTranslateY( character.getTranslateY()-24d );
			camera.setTranslate(character.getTranslateX(), character.getTranslateY()-character.getHeight()*3/4, character.getTranslateZ());
		}
	}
	
	public void moveDown() {
		Character3DSet test = createPuppet(character);
		test.setTranslateY( character.getTranslateY()+24d );
		
		if(canMove(test)) {
			character.setTranslateY( character.getTranslateY()+24d );
			camera.setTranslate(character.getTranslateX(), character.getTranslateY()-character.getHeight()*3/4, character.getTranslateZ());
		}
	}
	
	private Character3DSet createPuppet(Character3DSet model) {
		Character3DSet test = new Ironman85("test");
		test.setTranslate(model.getTranslateX(), model.getTranslateY(), model.getTranslateZ());
		test.moveLeft(model.getTranslateX(), model.getTranslateZ(), model.getStep());
		while(model.getDirection() != test.getDirection()) {
			test.turnRight();
		}
		return test;
	}
	
	public boolean canMove(Object3D object) {
		ArrayList<SpacialIndex> spacialindex = field3d.getOverlaps(object);
		for(int i=0; i<spacialindex.size(); i++) {
			if(10 <= field3d.getMaterialNumberFromIndex(spacialindex.get(i))) {
				return false;
			}
		}
		return true;
	}
	
	public void turnRight() {
		character.turnRight();
		camera.turnRight();
	}
	
	public void turnLeft() {
		character.turnLeft();
		camera.turnLeft(); 
	}
	
	public Character3DSet renewCharacter(Character3DSet character3dset, String character) {
		double x = character3dset.getTranslateX(), y = character3dset.getTranslateY(), z = character3dset.getTranslateZ();
		int direction = character3dset.getDirection();
		group.getChildren().remove(character3dset.getBody());
		character3dset = replaceCharacter(character3dset.getName(), character);
		character3dset.setTranslate(x, y, z);
		character3dset.setDirection(direction);
		group.getChildren().add(character3dset.getBody());
		return character3dset;
	}
	
	public Character3DSet replaceCharacter(String name, String character) {
		Character3DSet character3dset = null;
		switch(character) {
			case "Peripper": 	character3dset = new Peripper(name, 0d, initial.Y-24d, -200d); 	break;
			case "Iron Man 85": 		character3dset = new Ironman85(name, 0d, initial.Y-24d, -200d); 	break;
			case "Spider Man": character3dset = new Spiderman(name, 0d, initial.Y-24d, -200d); 	break;
			case "Shinio": 	character3dset = new Shinio(name, 0d, 400d-24d, -200d); 	break;
		}
		assert character3dset!=null: "* The character is null!! *";
		return character3dset;
	}
	
	public SubScene getSubScene() { return this.subscene; }
	public Character3DSet getCharacter() { return this.character; }
	public CameraSetting getCamera() { return this.camera; }
	public Field3D getField() {return field3d; }
	public Group getGroup() { return this.group; }
	public void setCharacter(Character3DSet character) { this.character = character; }
	public void setField(Field3D field3d) {this.field3d = field3d; }
}
