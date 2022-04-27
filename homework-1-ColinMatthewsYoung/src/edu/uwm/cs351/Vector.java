//Colin Young

package edu.uwm.cs351;

/**
 * An immutable class representing a two dimensional vector
 * in Cartesian space.
 */
public class Vector {
	private final double dx;
	private final double dy;
	
	/**
	 * Construct an empty vector.
	 */
	public Vector() {
		this.dx=0;
		this.dy=0;
		// TODO
	}
	
	/**
	 * Construct a vector with given delta (change of position)
	 * @param deltaX change in x coordinate
	 * @param deltaY change in y coordinate
	 */
	public Vector(double deltaX, double deltaY) {
		this.dx = deltaX;
		this.dy = deltaY;
		
		
	}
	
	/**
	 * Create a vector from p1 to p2.
	 * @param p1
	 * @param p2
	 */
	public Vector(Point p1, Point p2) {
		this.dx = p2.x() - p1.x();
		this.dy = p2.y() - p1.y();
		
	}
	/** @return x value */
	public double dx() {
		return this.dx;
	}
	/** @return y value */
	public double dy() {
		return this.dy;
	}
	//converts the double value of dx and dy into a string <x,y>.
	public String toString() {
		String xV = String.valueOf(this.dx);
		String yV = String.valueOf(this.dy);
		return "<"+xV+","+yV+">";
	}

	
	/**
	 * Return the new point after applying this vector to the argument.
	 * @param p old position
	 * @return new position
	 */
	public Point move(Point p) {
		double newX = p.x() + this.dx;
		double newY = p.y() + this.dy;
		return new Point(newX,newY);
		
	}
	
	/** Compute the magnitude of this vector.
	 * How far does it take a point from its origin?
	 * @return magnitude
	 */
	public double magnitude() {
		return Math.sqrt((Math.pow(dx, 2)+Math.pow(dy, 2))); //square root of dx^2 + dy^2
	
	}
	
	/**
	 * Return a new vector that is the sum of this vector and the parameter.
	 * @param other another vector (must not be null)
	 * @return new vector that is sum of this and other vectors.
	 */
	public Vector add(Vector other) {
		double newDX = this.dx + other.dx;
		double newDY = this.dy + other.dy;
		return new Vector(newDX,newDY);
		
	}
	
	/**
	 * Compute a new vector that scales this vector by the given amount.
	 * The new vector points in the same direction (unless scale is zero)
	 * but has magnitude scaled by the given amount.  If the scale
	 * is negative, the new vector points in the <em>opposite</em> direction.
	 * @param s scale amount
	 * @return new vector that scales this vector
	 */
	public Vector scale(double s) {
		double newDX = this.dx * s;
		double newDY = this.dy * s;
		return new Vector(newDX,newDY);
		
	}
	
	/**
	 * Return a unit vector (one with magnitude 1.0) in the same direction
	 * as this vector.  This operation is not defined on the zero vector.
	 * @return new vector with unit magnitude in the same direction as this.
	 */
	public Vector normalize() {
		double newDX = this.dx * (1/magnitude());
		double newDY = this.dy * (1/magnitude());
		return new Vector(newDX,newDY);
		
	}
	
	// TODO: Something else...
}
