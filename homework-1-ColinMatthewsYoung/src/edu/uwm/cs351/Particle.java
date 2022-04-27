//Colin Young

package edu.uwm.cs351;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Representation of a point mass moving in two dimensional space.
 * A particle has a location (a point), velocity (a vector), a mass (double)
 * and a color.  The mass and color never change.
 */
public class Particle {
	private volatile Point position; // must be "volatile" because of animation (not for other fields)
	private Vector velocity;
	private final double mass;
	private final Color color;
	
	
	/**
	 * Create a new particle with the given position, velocity, mass and color
	 * @param p position (location) of particle initially, must not be null
	 * @param v velocity of particle initially, must not be null
	 * @param m mass of particle
	 * @param c color of particle, must not be null
	 * @throws NullPointerException when p v or c are null.
	 * @throws IllegalArgumentException when m is less than zero.
	 */
	
	public Particle(Point p, Vector v, double m, Color c) {
		if(p == null)
			throw new NullPointerException("the position must not be null");

		if(v ==null)
			throw new NullPointerException("the velocity must not be null");
		if(m <0)
			throw new IllegalArgumentException("the Mass can not be negative");
		
		if(c == null)
			throw new NullPointerException("the color must not be null");
		
		this.position = p;
		this.velocity = v;
		this.mass = m;
		this.color = c;
		
	}
	
	/**
	 * Return the position of this particle
	 * @return position, never null
	 */
	public Point getPosition() {
		return this.position;
	}
	
	/**
	 * Return the velocity of this particular
	 * @return velocity, never null
	 */
	public Vector getVelocity() {
		return this.velocity;
		
	}
	
	/**
	 * Estimate the location of the particle after one unit of time by 
	 * letting it move at a constant velocity for that time from its starting
	 * position.  Essentially that simply means we apply the velocity as a vector
	 * to the particles current position.
	 */
	public void move() {
		this.position = this.velocity.move(this.position);
		
	}
	
	/**
	 * Draw the particle at its current position as a small circle.  The area of the circle
	 * is proportional to the mass: the radius of the circle is the square root of the mass.  
	 * @param g graphics context (must not be null)
	 */
	public void draw(Graphics g) {
		int radius = (int) Math.sqrt(this.mass);
		java.awt.Point p = this.position.asAWT();
		g.setColor(this.color);
		g.drawOval(radius, radius, radius, radius);
		g.drawOval(radius, radius, radius, radius);
		g.fillOval(p.x-radius, p.y-radius, radius*2, radius*2);
		g = null;
		// TODO: Read the documentation of fillOval carefully:
		// fillOval's first two parameters are the x and y coordinates of the
		// UPPER LEFT corner of the bounding box of the oval, NOT the center!
	}

	private static final double G = 1;

	/**
	 * Compute the Newtonian gravitational force that this particle exerts on the other.
	 * This force is proportional to the product of the masses and inversely proportional
	 * to the distance between them.  The constant of proportionality is given by {@link G}
	 * that is fixed at 1.0 for CS 351 purposes.  The direction of the force is toward this particle.
	 * @param other particle to operate gravitation on, must not be null
	 * @return force of gravitation toward this particle
	 */
	public Vector gravForceOn(Particle other) {
		
		
		 Vector u = new Vector(other.position,this.position);
		 u = u.normalize();
		//Vector gForce = new Vector();
		double gForce =((G*this.mass*other.mass)/Math.pow(other.position.distance(this.position),2));
		
		return u.scale(gForce);
	
	}
	
	/**
	 * Apply a force as an acceleration on the velocity (after dividing by the mass).
	 * This velocity is affected as though one applied the constant acceleration for one time unit.
	 * @param force force applied, must not be null
	 */
	public void applyForce(Vector force) {
		this.velocity= this.velocity.add(force.scale(1/mass));	
	
	}
}
