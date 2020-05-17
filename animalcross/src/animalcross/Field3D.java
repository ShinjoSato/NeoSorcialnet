package animalcross;

import java.util.ArrayList;

import commom.Coordinates;
import commom.SpacialIndex;
import javafx.scene.Group;
import javafx.scene.LightBase;
import javafx.scene.PointLight;
import javafx.scene.web.WebView;
import object3d.Character3DSet;
import object3d.Material3D;
import object3d.Object3D;
import object3d.character.Peripper;
import object3d.material.Block;
import object3d.material.Glass;
import object3d.material.Grass;
import object3d.material.Ground;
import object3d.material.Leaf;
import object3d.material.Nightsky;
import object3d.material.Wood;

public class Field3D {
	private final int WIDTH=30, HEIGHT=8, DEPTH=30;
	private final Coordinates origin;
	protected Group group;
	private int[][][] spacialindex;

	public Field3D(Coordinates origin) {
		this.origin = origin;
		group = new Group();
		spacialindex = new int[WIDTH][HEIGHT][DEPTH];
		loadSpacialIndexFromFile();
		setObjectOnField();
		createField();
	}
	
	public void createField() {		
        LightBase   light       = new PointLight();
        light.setTranslateZ( 10d );
        //group.getChildren().add( light );
		
        WebView webView = new WebView();
        webView.getEngine().load("http://sample-blog.com");
        webView.setTranslateY(400d-600d);
        webView.setTranslateZ(2300d);
        webView.setTranslateX(-600d);
        //group.getChildren().add(webView);
	}
	
	public void loadSpacialIndexFromFile() {
		for(int i=0; i<WIDTH; i++) {
			for(int j=0; j<HEIGHT; j++) {
				for(int k=0; k<DEPTH; k++) {
					if(j==0) spacialindex[i][j][k] = 15;
					if( (WIDTH/2<i && i<=WIDTH/2+1) && (DEPTH/2<k && k<=DEPTH/2+1) ) spacialindex[i][j][k] = 16;
					else if(j==HEIGHT-1) spacialindex[i][j][k] = 13;
					else if( i==0  || i==WIDTH-1 || k==0 || k==DEPTH-1 ) spacialindex[i][j][k] = 13;
				}
			}
		}
	}
	
	public void setObjectOnField() {
		for(int i=0; i<WIDTH; i++) {
			for(int j=0; j<HEIGHT; j++) {
				for(int k=0; k<DEPTH; k++) {
					Material3D material = getMaterialById(spacialindex[i][j][k], 24d*i+origin.X, -24d*j+origin.Y, 24d*k+origin.Z);
					if(material != null) group.getChildren().add(material.getBody());
				}
			}
		}
	}
	
	public Material3D getMaterialById(int id, double translateX, double translateY, double translateZ) {
		Material3D material = null;
		switch(id) {
			case 10: material = new Ground(translateX, translateY, translateZ); break;
			case 11: material = new Leaf(translateX, translateY, translateZ); break;
			case 12: material = new Block(translateX, translateY, translateZ); break;
			case 13: material = new Nightsky(translateX, translateY, translateZ); break;
			case 14: material = new Glass(translateX, translateY, translateZ); break;
			case 15: material = new Grass(translateX, translateY, translateZ); break;
			case 16: material = new Wood(translateX, translateY, translateZ); break;
		}
		return material;
	}
	
	double materialWidth = 24d, materialHeight = 24d, materialDepth = 24d;
	public ArrayList<SpacialIndex> getOverlaps(Object3D object){
		ArrayList<SpacialIndex> overlap = new ArrayList<SpacialIndex>();
		System.out.println("Object width: "+object.getWidth()+", startX: "+ (object.getTranslateX()-origin.X) + ", endX: "+ (object.getTranslateX()+object.getWidth()-origin.X) );
		int startX = (int)Math.floor((object.getTranslateX()-origin.X)/materialWidth), endX = (int)Math.floor((object.getTranslateX()+object.getWidth()-origin.X)/materialWidth);
		int startY = (int)Math.floor((-object.getTranslateY()+origin.Y)/materialHeight), endY = (int)Math.floor((-object.getTranslateY()+object.getHeight()+origin.Y)/materialHeight);
		int startZ = (int)Math.floor((object.getTranslateZ()-origin.Z)/materialDepth), endZ = (int)Math.floor((object.getTranslateZ()+object.getDepth()-origin.Z)/materialDepth);
		for(int i=startX; i<=endX; i++) {
			for(int j=startY; j<=endY; j++) {
				for(int k=startZ; k<=endZ; k++) {
					overlap.add(new SpacialIndex(i, j, k));
				}
			}
		}
		return overlap;
	}
	
	public Group getGroup() {return group;}
	public Coordinates getOrigin(){return origin;}
	public int getMaterialNumberFromIndex(SpacialIndex index) {
		return spacialindex[index.X][index.Y][index.Z];
	}
}
