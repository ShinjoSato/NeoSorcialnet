package sns.client.obj3d.character;

import sns.client.obj3d.Character3DSet;

public class Ironman85 extends Character3DSet {
	private String charactername="Iron Man 85";
	private double step=6d, tall=40d;
	public Ironman85(String name, double x, double y, double z) { 
		super(obj_url[0], name, x, y, z); 
		
		/*LightBase   light       = new PointLight();
        light.getTransforms().add( new Translate( 50.0 , -50.0 , 165.0 ) );
        body.getChildren().add(light);
        
        final AmbientLight ambientLight = new AmbientLight( Color.rgb(50,50,50) );
        body.getChildren().add(ambientLight);*/
	}
	public Ironman85(String name) { this(name, 0d, 0d, 0d); }
	public String getCharacterName() {return this.charactername;}
	@Override 
	public double getStep() { return step; }
	@Override
	public double getTall() { return tall; }
}
