package object3d.material;

import object3d.Material3D;

public class Ground extends Material3D{
	public Ground(double translateX, double translateY, double translateZ) {
		super(obj_url[8] , translateX, translateY, translateZ);
	}
}