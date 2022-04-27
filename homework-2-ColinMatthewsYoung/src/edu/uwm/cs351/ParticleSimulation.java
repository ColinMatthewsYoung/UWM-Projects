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
	
	private final ParticleSeq particles = new ParticleSeq();
	
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
		// TODO: add the particles to the sequence
		particles.append(p1);
		particles.append(p2);
		particles.append(p3);
	}
	
	/**
	@Override
	@param g Graphics.
	draws all particles on the screen.
	*/
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// OLD code:
		//for (Particle p : particles) {
		//	p.draw(g);
		//}
		// TODO: draw each particle
		particles.start();
		while(particles.isCurrent()) {
		particles.getCurrent().draw(g);
		particles.advance();
		}
		
		
	}
	
	/**
	 * Accelerate each particle by the gravitational force of each other particle.
	 * Then compute the next position of all particles.
	 */
	public void move() {
		// OLD code:
		/*
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
		*/
		// TODO: new code
		// Warning: Do not change "particles" in any way,
		// (e.g. don't call particles.start())
		// because that could interfere with the paintComponent method
		
		ParticleSeq cpy = particles.clone();
		ParticleSeq cpy2 = particles.clone();
		
		cpy2.start();
		while(cpy2.isCurrent()) {
			Vector force = new Vector();
			while(cpy.isCurrent()) {
				if(cpy.getCurrent().equals(cpy2.getCurrent())) {
					cpy.advance();
					continue;
				}
				force = force.add(cpy.getCurrent().gravForceOn(cpy2.getCurrent()));
				cpy.advance();
			}
			cpy.start();
			cpy2.getCurrent().applyForce(force);
			cpy2.advance();
		}
		cpy2.start();
		
		while(cpy2.isCurrent()) {
			cpy2.getCurrent().move();
			cpy2.advance();
		}
			
		particles.equals(cpy2);
		
		repaint();
	}
}
