package sns.client.obj3d.character;

import sns.client.obj3d.Character3DSet;

public class Shinio extends Character3DSet{
	private String charactername="Shinio";
	private double step=4d, tall=24d;
	public Shinio(String name, double x, double y, double z) { super(obj_url[2], name, x, y, z); }
	public Shinio(String name) { this(name, 0d, 0d, 0d); }
	public String getCharacterName() {return this.charactername;}
	@Override
	public double getStep() {return step; }
	@Override
	public double getTall() {return tall;}
}