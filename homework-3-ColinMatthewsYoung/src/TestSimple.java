import java.awt.Color;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Particle;
import edu.uwm.cs351.ParticleCollection;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;


public class TestSimple extends LockedTestCase {
	Particle e1 = new Particle(new Point(1,1),new Vector(),1.0,Color.WHITE); 
	Particle e2 = new Particle(new Point(2,2),new Vector(),2.0,Color.RED);
	Particle e3 = new Particle(new Point(3,3),new Vector(),3.0,Color.BLUE);
	Particle e4 = new Particle(new Point(4,4),new Vector(),4.0,Color.GREEN);
	Particle e5 = new Particle(new Point(5,5),new Vector(),5.0,Color.YELLOW);

	ParticleCollection s;
	
	protected ParticleCollection newCollection() {
		return new ParticleCollection();
	}

	@Override
	protected void setUp() {
		s = newCollection();
		try {
			assert 1/((int)e1.getPosition().x()-1) == 42 : "OK";
			System.err.println("Assertions must be enabled to use this test suite.");
			System.err.println("In Eclipse: add -ea in the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must be -ea enabled in the Run Configuration>Arguments>VM Arguments",true);
		} catch (ArithmeticException ex) {
			return;
		}
	}

	protected <T> void assertException(Class<?> excClass, Supplier<T> producer) {
		try {
			T result = producer.get();
			assertFalse("Should have thrown an exception, not returned " + result,true);
		} catch (RuntimeException ex) {
			if (!excClass.isInstance(ex)) {
				assertFalse("Wrong kind of exception thrown: "+ ex.getClass().getSimpleName(),true);
			}
		}		
	}

	protected <T> void assertException(Runnable f, Class<?> excClass) {
		try {
			f.run();
			assertFalse("Should have thrown an exception, not returned",true);
		} catch (RuntimeException ex) {
			if (!excClass.isInstance(ex)) {
				assertFalse("Wrong kind of exception thrown: "+ ex.getClass().getSimpleName(),true);
			}
		}		
	}

	/**
	 * Return the Particle as an integer
	 * <dl>
	 * <dt>-1<dd><i>(an exception was thrown)
	 * <dt>0<dd>null
	 * <dt>1<dd>e1
	 * <dt>2<dd>e2
	 * <dt>3<dd>e3
	 * <dt>4<dd>e4
	 * <dt>5<dd>e5
	 * </dl>
	 * @return integer encoding of Particle supplied
	 */
	protected int asInt(Supplier<Particle> g) {
		try {
			Particle n = g.get();
			if (n == null) return 0;
			return (int)(n.getPosition().x());
		} catch (RuntimeException ex) {
			return -1;
		}
	}
	
	public void testA() {
		// Nothing added yet:
		assertEquals(Ti(1112658640),s.size());
		assertFalse(s.iterator().hasNext());
		assertFalse(s.iterator().hasNext());
	}
	
	public void testB() {
		// Initially empty.
		// -1 for error, 0 for null, 1 for e1, 2 for e2 ...
		assertEquals(Ti(1848063),asInt(() -> s.iterator().next()));
		s.add(e1);
		assertEquals(Ti(337008384),asInt(() -> s.iterator().next()));
		Iterator<Particle> it = s.iterator();
		assertEquals(Ti(901033071),asInt(() -> it.next()));
		assertEquals(Ti(257085790),asInt(() -> it.next()));
	}
	
	public void testC() {
		// Initially empty.
		s.add(e5);
		s.add(e4);
		assertEquals(2,s.size());
		Iterator<Particle> it = s.iterator();
		// -1 for error, 0 for null, 1 for e1, 2 for e2 ...
		assertEquals(Ti(1876093076),asInt(() -> it.next()));
		assertEquals(Ti(56523864),asInt(() -> it.next()));
		assertEquals(Ti(1671626331),asInt(() -> it.next()));
	}
		
	public void testD() {
		s.add(e1);
		Iterator<Particle> it = s.iterator();
		assertTrue(it.hasNext());
		s.add(e2);
		// -1 for error, 0 for null, 1 for e1, 2 for e2 ...
		assertEquals(-1,asInt(() -> it.next()));
	}
	
	public void testE() {
		// Initially empty
		s.add(null);
		assertEquals(Ti(1049586199),s.size());
		assertEquals(Tb(1284130738),s.iterator().hasNext());
		// -1 for error, 0 for null, 1 for e1, 2 for e2 ...
		Iterator<Particle> it = s.iterator();
		assertEquals(Ti(1728369560),asInt(() -> it.next()));
		assertEquals(Ti(718450125),asInt(() -> it.next()));
	}
	
	public void testF() {
		s.add(e2);
		s.add(e1);
		ParticleCollection s2 = newCollection();
		s2.add(e4);
		s.addAll(s2);
		assertEquals(Ti(1153117195),s.size());
		Iterator<Particle> it = s.iterator();
		assertEquals(2,asInt(() -> it.next()));
		assertEquals(1,asInt(() -> it.next()));
		assertEquals(4,asInt(() -> it.next()));
		assertEquals(-1,asInt(() -> it.next()));
	}
	
	public void testG() {
		assertException(NoSuchElementException.class, () -> s.iterator().next());
		assertException(() -> s.iterator().remove(), IllegalStateException.class);
	}
		

	public void testI() {
		s.add(e1);
		assertFalse(s.remove(e2));
		assertTrue(s.remove(e1));
		assertEquals(0,s.size());	
		s.add(e2);
		assertSame(e2,s.iterator().next());
		assertEquals(1,s.size());
	}

	public void testJ() {
		s.add(e2);
		s.add(e3);
		Iterator<Particle> it = s.iterator();
		it.next();
		it.remove();
		assertException(() -> it.remove(), IllegalStateException.class);
		assertEquals(1,s.size());
	}

	
	public void testK() {
		s.add(e1);
		s.add(null);
		s.add(e2);
		Iterator<Particle> it = s.iterator();
		it.next();
		it.remove();
		assertEquals(2,s.size());
		assertEquals(null,it.next());
		it.remove();
		assertEquals(1,s.size());
		assertSame(e2,s.iterator().next());
		assertSame(e2,it.next());
	}

	public void testL() {
		s.add(e3);
		s.add(e4);
		s.add(null);
		assertTrue(s.remove(null));
		assertFalse(s.remove(null));
		assertEquals(2,s.size());
	}
	
	public void testM() {
		s.add(e2);
		s.add(e3);
		s.add(e4);
		
		Iterator<Particle> it1 = s.iterator();
		assertSame(e2,it1.next());
		
		Iterator<Particle> it2 = s.iterator();
		assertSame(e2,it2.next());
		
		assertTrue(it1.hasNext());
		
		it2.remove();
		assertEquals(2,s.size());
		assertTrue(it2.hasNext());
		
		assertException(ConcurrentModificationException.class, () -> it1.hasNext());
		assertException(ConcurrentModificationException.class, () -> it1.next());
		assertException(() -> it1.remove(), ConcurrentModificationException.class);		
	}
 
	public void testN() {
		s.add(e1);
		s.add(e2);
		s.add(e3);
		s.add(e4);
		s.add(e5);
		assertSame(e1,s.iterator().next());
		s.add(e1);
		s.add(e2);
		s.add(e3);
		s.add(e4);
		s.add(e5);
		s.add(e1);
		s.add(null);
		assertEquals(12,s.size());
		assertTrue(s.remove(null));
		assertTrue(s.remove(e1));
		assertEquals(10,s.size());
		
		Iterator<Particle> it = s.iterator();
		assertSame(e2,it.next());
		assertSame(e3,it.next());
		assertSame(e4,it.next());
		assertSame(e5,it.next());
		assertSame(e1,it.next());
		assertSame(e2,it.next());
		assertSame(e3,it.next());
		assertSame(e4,it.next());
		assertSame(e5,it.next());
		assertSame(e1,it.next());
		assertFalse(it.hasNext());
	}
	

	
	

	public void testS() {
		ParticleCollection se = newCollection();
		se.add(e3);
		se.add(e4);
		se.add(e5);
		se.add(e3);
		se.add(e4);
		se.add(e5);
		se.add(e3);
		se.add(e4);
		se.add(e5);
		se.add(e3);
		se.add(e4);
		se.add(e5);
		se.add(e3);
		se.add(e4);
		se.add(e5);
		se.add(e3);
		se.add(e4);
		se.add(e5);
		se.add(e3);
		se.add(e4);
		se.add(e5);
		se.add(e3);
		se.add(e4);
		se.add(e5);
		// se has 24 elements
		s.add(e1);
		s.add(e2);
		s.addAll(se);
		assertEquals(26,s.size());
		s.addAll(se);
		assertEquals(50,s.size());
		se.addAll(s);
		assertEquals(74,se.size());
	}
	
	
	public void testT() {
		ParticleCollection c = s.clone();
		assertEquals(0, c.size());
	}
	
	public void testU() {
		s.add(e1);
		ParticleCollection c = s.clone();
		
		assertSame(e1,s.iterator().next());
		assertSame(e1,c.iterator().next());
	}
	
	public void testV() {
		s.add(e1);
		Iterator<Particle> it = s.iterator();
		ParticleCollection c = s.clone();
		
		c.add(e2);
		assertSame(e1,it.next());
		assertFalse(it.hasNext());
		it = c.iterator();
		it.next();
		assertSame(e2,it.next());
	}

	public void testW() {
		ParticleCollection c = s.clone();
		
		s.add(e1);
		assertEquals(0,c.size());
		
		c = s.clone();
		assertEquals(1,c.size());
		assertSame(e1,c.iterator().next());
		
		c.add(e2);
		s.add(e3);
		assertTrue(c.remove(e1));
		assertSame(e2,c.iterator().next());
		
		Iterator<Particle> it = s.iterator();
		assertSame(e1,it.next());
		
		it.remove();
		c.add(e4);
		assertTrue(c.remove(e2));
		
		assertSame(e3,it.next());
	}
	
	
	public void testX() {
		s = new ParticleCollection(20);
		s = new ParticleCollection(0);
	}
	
	public void testY() {
		assertException(IllegalArgumentException.class,() -> new ParticleCollection(-1));
	}
}
