//Nicolas Stoian
//CS780 Image Processing
//Project 9.2 - Maximum Arc-Chord Distance Corner Detector

public class BoundaryPt{
	protected int x;
	protected int y;
	protected int maxCount;
	protected double maxDistance;
	protected int localMax;
	protected int corner;

	public BoundaryPt(){
		x = 0;
		y = 0;
		maxCount = 0;
		maxDistance = 0.0;
		localMax = 0;
		corner = 0;
	}

	public BoundaryPt(int x, int y){
		this.x = x;
		this.y = y;
		maxCount = 0;
		maxDistance = 0.0;
		localMax = 0;
		corner = 0;
	}
}
