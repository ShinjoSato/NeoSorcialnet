package object3d.character;

import object3d.Character3DSet;

public class Skeleton extends Character3DSet{
	private String charactername="Skeleton";
	private double step=4d, tall = 40d;
	public Skeleton(String name, double x, double y, double z) { super(obj_url[3], name, x, y, z); }
	public Skeleton(String name) { this(name, 0d, 0d, 0d); }
	public String getCharacterName() {return this.charactername;}
	@Override
	public double getStep() {return step;}
	@Override
	public double getTall() {return tall; }
}