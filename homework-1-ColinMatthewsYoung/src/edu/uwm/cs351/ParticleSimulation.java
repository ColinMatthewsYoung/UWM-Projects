package edu.uwm.cs351;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * A simulation of three particles in two dimensional Cartesian space
 * acting on each other using Newtonian gravitation.
 */
public class ParticleSimulation extends JPanel {
	/**
	 * Put this in to keep Eclipse happy ("KEH").
	 */
	private static final long serialVersionUID = 1L;
	
	private final Particle[] particles;
	
	/**
	 * Create a particle simulation with three particles
	 * @param p1 first particle, must not be null
	 * @param p2 second particle, must not be null
	 * @param p3 third particle, must not be null
	 */
	public ParticleSimulation(Particle p1, Particle p2, Particle p3) {
		if (p1 == null || p2 == null || p3 == null) {
			throw new NullPointerException("Cannot simulate with null particles");
		}
		particles = new Particle[] {p1, p2, p3};
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Particle p : particles) {
			p.draw(g);
		}
	}
	
	/**
	 * Accelerate each particle by the gravitational force of each other particle.
	 * Then compute the next position of all particles.
	 */
	public void move() {
		for (Particle p : particles) {
			Vector force = new Vector();
			for (Particle other : particles) {
				if (other == p) continue; // no force from particle on itself
				force = force.add(other.gravForceOn(p));
			}
			p.applyForce(force);
		}
		for (Particle p : particles) {
			p.move();
		}
		repaint();
	}
}
