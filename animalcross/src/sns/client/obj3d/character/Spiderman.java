package sns.client.obj3d.character;

import sns.client.obj3d.Character3DSet;

public class Spiderman extends Character3DSet{
	private String charactername="Spider Man";
	private double step=4d, tall=40d;
	public Spiderman(String name, double x, double y, double z) { super(obj_url[4], name, x, y, z); }
	public Spiderman(String name) { this(name, 0d, 0d, 0d); }
	public String getCharacterName() {return this.charactername;}
	@Override
	public double getStep() {return step; }
	@Override
	public double getTall() {return tall;}
}