package sns.common;

public class SpacialIndex {
	public final int X, Y, Z;
	
	public SpacialIndex(int X, int Y, int Z) {
		this.X = X;
		this.Y = Y;
		this.Z = Z;
	}
	
	public String toString() { return String.format("[%d, %d, %d]", X, Y, Z); }
}
