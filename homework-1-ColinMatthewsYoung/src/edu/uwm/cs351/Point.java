//Colin Young

package edu.uwm.cs351;

/**
 * Immutable location in two dimensional space.
 */
public class Point {
	private final double x;
	private final double y;
	
	/**
	 * Create a point at the given (x,y) coordinates
	 * @param xCoord x coordinate
	 * @param yCoord y coordinate
	 */
	public Point(double xCoord, double yCoord) {
		this.x = xCoord;
		this.y = yCoord;
	}
	
	// Getters for x and y (see assignment)
	/**@return x */
	public double x() {
		return this.x;
	}
	/**@return y*/
	public double y() {
		return this.y;
	}
	
	
	/**
	 * Round this point to the closest AWT point (using integer coordinates).
	 * @return rounded point
	 */
	public java.awt.Point asAWT() {
		int pX = (int)Math.round(x);
		int pY = (int)Math.round(y);
		java.awt.Point p = new java.awt.Point(pX,pY);
		return p;
		
		// TODO
	}
	
	/**
	 * Compute the distance (never negative) between two points.
	 * @param other another point, must not be null
	 * @return distance between points.
	 */
	public double distance(Point other) {
		Vector dis =new Vector(this,other);		
		return dis.magnitude();
		// TODO
	}
	
	/**
	 * Return string of the form (x,y)
	 * @see java.lang.Object#toString()
	 */
	@Override // implementation
	public String toString() {
		//make sure its not null?
		String xCoord = String.valueOf(this.x);
		String yCoord = String.valueOf(this.y);
		return "("+xCoord+","+yCoord+")";
		// TODO
	}
}
