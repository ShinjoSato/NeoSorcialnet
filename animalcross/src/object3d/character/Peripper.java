package object3d.character;

import object3d.Character3DSet;

public class Peripper extends Character3DSet{
	private String charactername="Peripper";
	private double step=4d, tall = 40d;
	public Peripper(String name, double x, double y, double z) { super(obj_url[1], name, x, y, z); }
	public Peripper(String name) { this(name, 0d, 0d, 0d); }
	public String getCharacterName() {return this.charactername;}
	@Override
	public double getStep() {return step;}
	@Override
	public double getTall() {return tall; }
}