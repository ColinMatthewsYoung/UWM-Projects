import java.awt.Color;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs.test.util.Call;
import edu.uwm.cs.test.util.MockGraphics;
import edu.uwm.cs351.Particle;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;


public class TestParticle extends LockedTestCase {
	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (RuntimeException ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}

	private Point p1, p2;
	private Vector v1, v2;

	@Override
	protected void setUp() {
		p1 = new Point(2.0,3.0);
		p2 = new Point(-4.0,11.0);
		v1 = new Vector(-3.0,0.0);
		v2 = new Vector(p1,p2);
	}

	public void testA() {
		Particle o1 = new Particle(p1,v1,10,Color.BLUE);
		assertSame(p1,o1.getPosition());
		assertSame(v1,o1.getVelocity());
		
		Particle o2 = new Particle(p2,v2,20,Color.RED);
		assertSame(p2,o2.getPosition());
		assertSame(v2,o2.getVelocity());
	}
	
	public void testB() {
		// p1 = (2,3)
		// v1 = (-3,0)
		Particle o1 = new Particle(p1,v1,10,Color.BLUE);
		o1.move();
		assertEquals(-1.0,o1.getPosition().x());
		assertEquals(3.0,o1.getPosition().y());
		assertEquals(-3.0,o1.getVelocity().dx());
		assertEquals(0.0,o1.getVelocity().dy());
		
		o1.move();
		assertEquals(-4.0,o1.getPosition().x());
		assertEquals(3.0,o1.getPosition().y());
		assertEquals(-3.0,o1.getVelocity().dx());
		assertEquals(0.0,o1.getVelocity().dy());		
	}
	
	public void testC() {
		// p2 = (-4,11);
		// v2 = <-6,8>;
		Particle o2 = new Particle(p2,v2,20,Color.RED);
		o2.move();
		assertEquals(-10.0,o2.getPosition().x());
		assertEquals(19.0,o2.getPosition().y());
		assertEquals(-6.0,o2.getVelocity().dx());
		assertEquals(8.0,o2.getVelocity().dy());

		o2.move();
		assertEquals(-16.0,o2.getPosition().x());
		assertEquals(27.0,o2.getPosition().y());	
	}
	
	public void testD() {
		// p1 = (2,3)
		// v1 = (-3,0)
		Particle o1 = new Particle(p1,v1,10,Color.BLUE);
		
		// v2 = <-6,8>;
		o1.applyForce(v2); // accelerate the particle
		
		// Read the assignment carefully if you have problems with this:
		assertEquals(Td(1126852156),o1.getPosition().x());
		assertEquals(3.0,o1.getPosition().y());
		// Recall that mass of particle is 10 units
		assertEquals(Td(587463272),o1.getVelocity().dx());
		assertEquals(0.8,o1.getVelocity().dy());
	}
	
	public void testE() { // observe acceleration in practice
		Particle o4 = new Particle(new Point(0,0),new Vector(1,0),4,Color.RED); 
		Vector v = new Vector(0,1);
		
		o4.move();
		assertEquals(1.0,o4.getPosition().x());
		assertEquals(0.0,o4.getPosition().y());
		
		o4.applyForce(v); // mass is 4
		assertEquals(0.25,o4.getVelocity().dy());
		o4.move();
		assertEquals(2.0,o4.getPosition().x());
		assertEquals(0.25,o4.getPosition().y());
		
		o4.applyForce(v);
		assertEquals(0.5,o4.getVelocity().dy());
		o4.move();
		assertEquals(3.0,o4.getPosition().x());
		assertEquals(0.75,o4.getPosition().y());	
		
		o4.applyForce(v);
		assertEquals(Td(1709924516),o4.getVelocity().dy());
		o4.move();
		assertEquals(4.0,o4.getPosition().x());
		assertEquals(Td(1278380973),o4.getPosition().y());
	}
	
	public void testF() {
		Particle a = new Particle(new Point(0,3), new Vector(1.9,1.51),4,Color.CYAN);
		Particle b = new Particle(new Point(2,3), new Vector(0.87,19.5),5,Color.PINK);
		
		// Read Homework PDF
		Vector gf = a.gravForceOn(b);
		assertEquals(Td(1408163757),gf.dx());
		assertEquals(0.0,gf.dy());
		
		gf = b.gravForceOn(a);
		assertEquals(5.0,gf.dx());
		assertEquals(0.0,gf.dy());		
	}
	
	public void testG() {
		Particle o0 = new Particle(new Point(0,0),new Vector(0,0),12,Color.BLACK);
		Particle ox = new Particle(new Point(4,0), new Vector(-3,0),80,Color.GREEN);
		Particle oy = new Particle(new Point(0,-3), new Vector(0,1),150,Color.MAGENTA);

		Vector v = o0.gravForceOn(ox);		
		assertEquals( -60.0, v.dx());
		assertEquals( 0.0, v.dy());
		
		Vector v1 = ox.gravForceOn(o0);
		assertEquals( 60.0, v1.dx());
		assertEquals( 0.0, v1.dy());
		
		Vector v2 = o0.gravForceOn(oy);
		assertEquals( 0.0, v2.dx());
		assertEquals( 200.0, v2.dy());
		
		Vector v3 = ox.gravForceOn(oy);
		assertEquals( 384.0, v3.dx());
		assertEquals( 288, Math.round(v3.dy()));
	}
	
	public void testH() {
		Particle o1 = new Particle(p1,v1,100,Color.BLUE);
		Particle o3 = new Particle(p2,v2,5,Color.GREEN);
		Vector v = o1.gravForceOn(o3);
		assertEquals(  3, Math.round(v.dx()));
		assertEquals( -4.0, v.dy());
	}
	
	public void testI() {
		Particle o1 = new Particle(p1,v1,10,Color.BLUE);
		Particle o2 = new Particle(p2,v2,20,Color.RED);
		
		o1.gravForceOn(o2); // should have no side-effects
		
		assertSame(p1,o1.getPosition());
		assertSame(v1,o1.getVelocity());
		
		assertSame(p2,o2.getPosition());
		assertSame(v2,o2.getVelocity());		
	}
	
	public void testJ() {
		assertException(NullPointerException.class, () -> new Particle(null,v1,10,Color.BLUE));
		assertException(NullPointerException.class, () -> new Particle(p1,null,10,Color.BLUE));
		assertException(NullPointerException.class, () -> new Particle(p2,v1,10,null));
	}
	
	// We test the graphics drawing using a graphics "mock"
	// which remembers what cals were made to the Graphics object.
	
	public void testK() {
		Particle o1 = new Particle(new Point(100,200),v1,25,Color.BLUE);
		
		MockGraphics mg = new MockGraphics();
		o1.draw(mg.getGraphics());
		
		assertEquals(2, mg.numCalls());
		
		Call call1 = mg.getCall(0);
		assertEquals("setColor", call1.getMethodName());
		assertEquals(Color.BLUE, call1.getArg(0));
		
		Call call2 = mg.getCall(1);
		assertEquals("fillOval", call2.getMethodName());
		assertEquals(new Integer(95), call2.getArg(0));
		assertEquals(new Integer(195), call2.getArg(1));
		assertEquals(new Integer(10), call2.getArg(2));
		assertEquals(new Integer(10), call2.getArg(3));
	}
	
	public void testL() {
		Particle p1 = new Particle(new Point(50,80),new Vector(10,-10),49,Color.MAGENTA);

		p1.move();
		MockGraphics mg = new MockGraphics();
		p1.draw(mg.getGraphics());
		
		assertEquals(2, mg.numCalls());
		
		Call call1 = mg.getCall(0);
		assertEquals("setColor", call1.getMethodName());
		assertEquals(Color.MAGENTA, call1.getArg(0));
		
		Call call2 = mg.getCall(1);
		assertEquals("fillOval", call2.getMethodName());
		assertEquals(new Integer(53), call2.getArg(0));
		assertEquals(new Integer(63), call2.getArg(1));
		assertEquals(new Integer(14), call2.getArg(2));
		assertEquals(new Integer(14), call2.getArg(3));
	}
}
