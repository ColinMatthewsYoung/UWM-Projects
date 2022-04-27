import java.awt.Color;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Particle;
import edu.uwm.cs351.ParticleSeq;
import edu.uwm.cs351.Vector;



public class TestParticleSeq extends LockedTestCase {
	
	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (RuntimeException ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}

	private ParticleSeq s;
	Vector v = new Vector();
	Particle b1 = new Particle(new Point(1.0,20.0),v,1,Color.RED);
	Particle b2 = new Particle(new Point(2.0,40.0),v,2,Color.BLUE);
	Particle b2a = new Particle(new Point(2.0,40.0),v,2,Color.MAGENTA);
	Particle b3 = new Particle(new Point(3.0,60.0),v,3,Color.YELLOW);
	Particle b4 = new Particle(new Point(4.0,80.0),v,4,Color.GREEN);
	Particle b5 = new Particle(new Point(5.0,100.0),v,5,Color.CYAN);
	
	Particle b[] = { null, b1, b2, b3, b4, b5 };
	
	// Using the above array
	// convert a Particle result to an integer:
	// 0 = null, 1 = b1, 2 = b2 etc.
	// if the expression causes an error, the index is -1.
	// If the Particle is not in the array, the result "NONE OF THE ABOVE" is -2.
	int ix(Supplier<Particle> p) {
		try {
			Particle Particle = p.get();
			if (Particle == null) return 0;
			for (int i=0; i < b.length; ++i) {
				if (Particle == b[i]) return i;
			}
			return -2;
		} catch (RuntimeException ex) {
			return -1;
		}
	}
	
	@Override
	public void setUp() {
		s = new ParticleSeq(1); // start small
		try {
			assert 3/(int)(b1.getPosition().x()-1.0) == 42 : "OK";
			System.err.println("Assertions must be enabled to use this test suite.");
			System.err.println("In Eclipse: add -ea in the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must be -ea enabled in the Run Configuration>Arguments>VM Arguments",true);
		} catch (ArithmeticException ex) {
			return;
		}
	}

	// This test tests many things.
	// We recommend that you run the UnlockTests to unlock all its tests at once.
	public void test() {
		// s is empty.
		assertEquals(Ti(887770454),s.size());
		// In the following, if a Particle index is called for ("ix"), answer:
		// 0 if the result will be null
		// 1 if the result will be the Particle b1
		// 2 if the result will be the Particle b2
		// 3,4,5 etc.
		// -1 if the code will crash with an exception
		// -2 none of the above (some other Particle, such as b2a)
		assertEquals(Ti(491728531),ix(() -> s.getCurrent()));
		s.append(b1);
		assertEquals(Ti(1854083915),ix(() -> s.getCurrent()));
		s.append(null);
		assertEquals(Ti(236938571),ix(() -> s.getCurrent()));
		s.start();
		s.append(b3);
		assertEquals(Ti(710084632),ix(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(565750963),ix(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(371995505),ix(() -> s.getCurrent()));
		s.append(b2a);
		assertEquals(Ti(221507525),ix(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(25562286),ix(() -> s.getCurrent()));
		testcont(s);		
	}
	
	// Continuation of test:
	private void testcont(ParticleSeq s) {
		s.advance();
		assertEquals(3,ix(()->s.getCurrent()));
		// At this point s is [clone(b2),b1,*b3,null] with * marking current
		ParticleSeq s2 = new ParticleSeq();
		// In the following, if a Particle index is called for ("ix"), answer:
		// 0 if the result will be null
		// 1 if the result will be the Particle b1
		// 2 if the result will be the Particle b2, etc for 3, 4, 5
		// -1 if the code will crash with an exception
		// -2 none of the above (some other Particle)
		s2.append(b4);
		s2.append(b5);
		assertEquals(Ti(1860957759),ix(() -> s2.getCurrent()));
		s.addAll(s2);
		assertEquals(Ti(566769707),s.size());
		// What does addAll() say about what is current afterwards?
		assertEquals(Ti(1724402076),ix(()->s.getCurrent()));
		assertEquals(Ti(1459925108),ix(() -> s2.getCurrent()));
		s.advance();
		assertEquals(Ti(1196621866),ix(()->s.getCurrent()));
		s.advance();
		assertEquals(Ti(1006831199),ix(()->s.getCurrent()));
		testremove(s);
	}
	
	private void testremove(ParticleSeq s) {
		// s is [clone(b2), b1, b3, null, *b4, b5] where * marks current element
		// In the following, if a Particle index is called for ("ix"), answer:
		// 0 if the result will be null
		// 1 if the result will be the Particle b1
		// 2 if the result will be the Particle b2, etc for 3, 4, 5
		// -1 if the code will crash with an exception
		// -2 none of the above (some other Particle)
		s.removeCurrent();
		assertEquals(Ti(980256184),ix(() -> s.getCurrent()));
		s.removeCurrent();
		assertEquals(Ti(1957337308),ix(() -> s.getCurrent()));
		s.start();
		s.removeCurrent();
		assertEquals(Ti(1557219254),ix(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(1191281444),ix(() -> s.getCurrent()));
	}
	
	
	
	//// Numbered tests
	
	
	/// test0N: simple tests on the empty sequence
	
	public void test00() {
		assertEquals(0,s.size());
	}
	
	public void test01() {
		assertFalse(s.isCurrent());
	}
	
	public void test02() {
		s.start(); // doesn't crash
	}
		
	public void test03() {
		assertException(IllegalStateException.class,() -> s.getCurrent());		
	}
	
	public void test04() {
		assertException(IllegalStateException.class, () -> s.advance());
	}
	
	public void test05() {
		s.start();
		assertFalse(s.isCurrent());
	}
	
	public void test06() {
		s = new ParticleSeq(0);
		assertEquals(0,s.size());
	}
	
	public void test07() {
		assertException(IllegalArgumentException.class, () -> new ParticleSeq(-1));
	}
	
	
	/// test1N: tests of a single element sequence
	
	public void test10() {
		s.append(b1);
		assertEquals(1,s.size());
	}
	
	public void test11() {
		s.append(b2);
		assertTrue(s.isCurrent());
	}
	
	public void test12() {
		s.append(b3);
		assertSame(b3,s.getCurrent());
	}
	
	public void test13() {
		s.append(b4);
		s.start();
		assertSame(b4,s.getCurrent());
	}
	
	public void test14() {
		s.append(b5);
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test15() {
		s.append(b5);
		s.advance();
		s.start();
		assertTrue(s.isCurrent());
	}
	
	public void test16() {
		s.append(null);
		assertEquals(1,s.size());
	}
	
	public void test17() {
		s.append(null);
		assertTrue(s.isCurrent());
		assertNull(s.getCurrent());
	}
	
	public void test18() {
		s.append(null);
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test19() {
		s = new ParticleSeq(0);
		s.append(b1);
		assertEquals(1,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
	}

	
	/// test2N: tests of two element sequences
	
	public void test20() {
		s.append(b1);
		s.append(b2);
		assertEquals(2,s.size());
	}
	
	public void test21() {
		s.append(b3);
		s.append(b4);
		assertTrue(s.isCurrent());
	}
	
	public void test22() {
		s.append(b1);
		s.append(b2);
		assertSame(b2,s.getCurrent());
	}
	
	public void test23() {
		s.append(b1);
		s.append(b2);
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test24() {
		s.append(b4);
		s.append(b5);
		s.start();
		assertTrue(s.isCurrent());
		assertEquals(2,s.size());
		assertEquals(b4,s.getCurrent());
	}
	
	public void test25() {
		s.append(b2);
		s.append(b5);
		s.start();
		s.advance();
		s.start();
		assertTrue(s.isCurrent());
		assertEquals(2,s.size());
		assertEquals(b2,s.getCurrent());
	}
	
	public void test26() {
		s.append(b1);
		s.advance();
		s.append(b2);
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
	}
	
	public void test27() {
		s.append(b3);
		s.advance();
		s.append(b4);
		s.advance();
		assertEquals(2,s.size());
		assertTrue(s.isCurrent());
		assertSame(b3,s.getCurrent());
	}
	
	public void test28() {
		s.append(null);
		s.append(null);
		assertEquals(2,s.size());
	}
	
	public void test29() {
		s.append(null);
		s.append(null);
		assertTrue(s.isCurrent());
	}
		
	
	/// test3N: tests of three or more element sequences
	
	public void test30() {
		s.append(b1);
		s.append(b2);
		s.append(b3);
		assertEquals(3,s.size());
		assertSame(b3,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b1,s.getCurrent());
		s.advance();
		assertSame(b2,s.getCurrent());
		s.advance();
		assertSame(b3,s.getCurrent());
	}
	
	public void test31() {
		s.append(b4);
		s.append(b5);
		s.advance();
		s.append(b1);
		assertSame(b1,s.getCurrent());
		s.advance();
		assertEquals(3,s.size());
		assertSame(b4,s.getCurrent());
		s.advance();
		assertSame(b5,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test32() {
		s.append(b2);
		s.append(b4);
		s.start();
		s.append(b3);
		assertSame(b3,s.getCurrent());
		s.advance();
		assertSame(b4,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b2,s.getCurrent());
		assertEquals(3,s.size());
	}
	
	public void test33() {
		s.append(b1);
		s.append(null);
		s.append(b2);
		assertSame(b2,s.getCurrent());
		s.start();
		assertSame(b1,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertNull(s.getCurrent());
	}
	
	public void test34() {
		s.append(b4);
		s.append(b5);
		s.advance();
		s.append(null);
		assertNull(s.getCurrent());
		assertEquals(3,s.size());
		s.advance();
		assertSame(b4,s.getCurrent());
	}
	
	public void test35() {
		s.append(b1);
		s.append(b2);
		s.append(b3);
		s.start();
		s.append(b4);
		assertSame(b4,s.getCurrent());
		s.advance();
		assertSame(b2,s.getCurrent());
		s.advance();
		assertSame(b3,s.getCurrent());
		s.advance();
		assertException(IllegalStateException.class,() -> s.getCurrent() );
	}
	
	public void test36() {
		s.append(b5);
		s.append(b4);
		s.append(b3);
		s.start();
		s.advance();
		s.append(b2);
		assertEquals(b2,s.getCurrent());
		assertEquals(4,s.size());
		s.advance();
	}
	
	public void test37() {
		s.append(b1);
		s.advance();
		s.append(b2);
		s.advance();
		s.append(b3);
		s.advance();
		s.append(b4);
		s.advance();
		s.append(b5);
		s.advance();
		assertSame(b1,s.getCurrent());
		s.advance();
		assertSame(b3,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		assertException(IllegalStateException.class,() -> s.advance() );		
	}
	
	public void test38() {
		s.append(b1);
		s.append(b2);
		s.append(b3);
		s.append(b4);
		s.append(b5);
		s.append(null);
		s.append(b5);
		s.append(b4);
		s.append(b3);
		s.append(b2);
		s.append(b1);
		s.append(null);
		s.append(b1);
		s.append(b2);
		s.append(b3);
		s.append(b4);
		s.append(b5);
		assertEquals(17,s.size());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(null,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(null,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test39() {
		s = new ParticleSeq();
		for (int i=0; i < 39; ++i) {
			s.append(b[i%b.length]);
		}
		assertEquals(39,s.size());
		s.start();
		for (int i=0; i < 39; ++i) {
			assertTrue(s.isCurrent());
			assertEquals(b[i%b.length],s.getCurrent());
			s.advance();
		}
		assertFalse(s.isCurrent());
	}
	
	
	/// test4N: tests of removeCurrent
	
	public void test40() {
		s.append(b1);
		s.removeCurrent();
		assertFalse(s.isCurrent());
	}
	
	public void test41() {
		s.append(b2);
		s.removeCurrent();
		assertEquals(0,s.size());
	}
	
	public void test42() {
		s.append(b3);
		s.append(b4);
		s.removeCurrent();
		assertFalse(s.isCurrent());
		assertEquals(1,s.size());
		s.start();
		assertSame(b3,s.getCurrent());
	}
	
	public void test43() {
		s.append(b4);
		s.append(b3);
		s.start();
		s.removeCurrent();
		assertTrue(s.isCurrent());
		assertSame(b3,s.getCurrent());
	}
	
	public void test44() {
		s.append(b4);
		s.append(null);
		s.start();
		s.removeCurrent();
		assertTrue(s.isCurrent());
		assertSame(null,s.getCurrent());
	}
	
	public void test45() {
		s.append(b4);
		s.append(b3);
		s.append(b5);
		s.start();
		s.advance();
		s.removeCurrent();
		assertSame(b5,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		assertEquals(2,s.size());
		s.start();
		assertSame(b4,s.getCurrent());
	}
	
	public void test46() {
		s.append(b4);
		s.append(b5);
		s.append(null);
		s.removeCurrent();
		assertEquals(2,s.size());
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test47() {
		s.append(b1);
		s.append(b2);
		s.append(b3);
		s.append(b4);
		s.append(b5);
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); 
		s.removeCurrent();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent());
		s.removeCurrent();
		assertSame(b5,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test48() {
		s.append(b1);
		s.append(b2);
		s.append(b3);
		s.append(b4);
		s.append(b5);
		s.start();
		assertSame(b1,s.getCurrent()); s.removeCurrent();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.removeCurrent();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.removeCurrent();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test49() {
		for (int i=0; i < 49; ++i) {
			s.append(b[i%b.length]);
		}
		s.start();
		for (int i=0; i < 49; ++i) {
			assertSame("b[" + i + "]",b[i%b.length],s.getCurrent());
			if ((i%2) == 0) s.removeCurrent();
			else s.advance();
		}
		s.start();
		for (int i=0; i < 24; ++i) {
			assertSame("b[" + (i*2+1) + "]", b[(i*2+1)%b.length],s.getCurrent());
			s.removeCurrent();
		}
		assertEquals(0,s.size());
	}
	
	
	/// test5N: errors with removeCurrent
	
	public void test50() {
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test51() {
		s.append(b1);
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test52() {
		s.append(b1);
		s.removeCurrent();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test53() {
		s.append(b1);
		s.append(b2);
		s.removeCurrent();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test54() {
		s.append(b1);
		s.append(b2);
		s.append(b3);
		s.append(b4);
		s.append(b5);
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test55() {
		s.append(b5);
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
		s.append(b4);
		s.advance();
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
		s.append(b3);
		s.advance();
		s.advance();
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
		s.append(b2);
		s.advance();
		s.advance();
		s.advance();
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
		s.append(b1);
		assertEquals(5,s.size());
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}
	
	
	/// test6N: short tests of addAll
	
	public void test60() {
		ParticleSeq se = new ParticleSeq();
		s.addAll(se);
		assertEquals(0,s.size());
	}

	public void test61() {
		ParticleSeq se = new ParticleSeq();
		s.append(b1);
		s.addAll(se);
		assertEquals(b1,s.getCurrent());
	}

	public void test62() {
		ParticleSeq se = new ParticleSeq();
		s.append(b2);
		s.advance();
		s.addAll(se);
		assertFalse(s.isCurrent());
	}

	public void test63() {
		ParticleSeq se = new ParticleSeq();
		s.append(b3);
		s.append(b4);
		s.addAll(se);
		assertSame(b4,s.getCurrent());
	}

	public void test64() {
		ParticleSeq se = new ParticleSeq();
		se.append(b1);
		s.addAll(se);
		assertFalse(s.isCurrent());
		assertTrue(se.isCurrent());
		assertEquals(1,s.size());
		assertEquals(1,se.size());
		s.start();
		assertSame(b1,s.getCurrent());
		assertSame(b1,se.getCurrent());
	}
	
	public void test65() {
		ParticleSeq se = new ParticleSeq();
		se.append(b1);
		s.append(b2);
		s.addAll(se);
		assertTrue(s.isCurrent());
		assertEquals(2,s.size());
		assertEquals(1,se.size());
		assertSame(b2,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
	}
	
	public void test66() {
		ParticleSeq se = new ParticleSeq();
		se.append(b1);
		s.append(b2);
		s.advance();
		s.addAll(se);
		assertFalse(s.isCurrent());
		assertEquals(2,s.size());
		assertEquals(1,se.size());
		assertTrue(se.isCurrent());
		assertSame(b1,se.getCurrent());
		s.start();
		assertSame(b2,s.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
	}
	
	public void test67() {
		ParticleSeq se = new ParticleSeq();
		se.append(b1);
		se.advance();
		s.append(b3);
		s.append(b2);
		s.addAll(se);
		assertTrue(s.isCurrent());
		assertSame(b2,s.getCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertFalse(se.isCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b3,s.getCurrent());
	}
	
	public void test68() {
		ParticleSeq se = new ParticleSeq();
		se.append(b2);
		se.append(b1);	
		s.append(b4);
		s.append(b3);
		s.addAll(se);
		assertTrue(s.isCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
	}

	public void test69() {
		ParticleSeq se = new ParticleSeq(100);
		se.append(b4);
		se.append(null);
		se.start();
		s.addAll(se);
		assertFalse(s.isCurrent());
		assertEquals(2,s.size());
		assertException(IllegalStateException.class, () -> s.advance());
		s.start();
		assertSame(b4,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(null,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	
	/// test7N: bigger tests with add all
	
	public void test70() {
		ParticleSeq se = new ParticleSeq(1);
		se.append(b1);
		se.append(b2);
		se.append(b3);
		assertFalse(s.isCurrent());
		s.addAll(se);
		assertFalse(s.isCurrent());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertEquals(3,s.size());
	}
	
	public void test71() {
		ParticleSeq se = new ParticleSeq(1);
		se.append(b2);
		se.append(b3);
		se.append(b4);
		se.append(b5);
		se.advance();
		s.append(b1);
		s.advance();
		s.addAll(se);
		assertFalse(s.isCurrent());
		assertEquals(5,s.size());
		assertEquals(4,se.size());
		assertFalse(se.isCurrent());
		// check s
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
	}
	
	public void test72() {
		ParticleSeq se = new ParticleSeq(1);
		s.append(b1);
		s.append(b2);
		s.start();
		se.append(b3);
		se.append(b4);
		se.append(b5);
		se.append(null);
		se.append(b1);
		s.addAll(se);
		assertEquals(7,s.size());
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(null,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
	}

	public void test73() {
		ParticleSeq se = new ParticleSeq();
		se.append(b3);
		se.append(b4);
		se.append(b5);
		se.append(b3);
		se.append(b4);
		se.append(b5);
		se.append(b3);
		se.append(b4);
		se.append(b5);
		se.append(b3);
		se.append(b4);
		se.append(b5);
		se.append(b3);
		se.append(b4);
		se.append(b5);
		se.append(b3);
		se.append(b4);
		se.append(b5);
		se.append(b3);
		se.append(b4);
		se.append(b5);
		se.append(b3);
		se.append(b4);
		se.append(b5);
		// se has 24 elements
		s.append(b1);
		s.append(b2);
		s.addAll(se);
		assertEquals(26,s.size());
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		s.addAll(se);
		assertEquals(50,s.size());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent()); s.advance();
		assertSame(b5,s.getCurrent()); s.advance();
		assertSame(b3,s.getCurrent()); s.advance();
		assertSame(b4,s.getCurrent());
	}
	
	public void test79() {
		s.append(b1);
		s.append(b2);
		ParticleSeq se = new ParticleSeq(1);
		se.append(b3);
		se.append(b4);
		se.append(b5);
		s.addAll(se); // s = [1,2,3,4,5]
		se.addAll(s); // se = [3,4,5,1,2,3,4,5]
		s.addAll(se);
		se.addAll(s);
		s.addAll(se);
		assertEquals(34,s.size());
	}
	
	
	/// test8N: addAll with same sequence
	
	public void test80() {
		s.addAll(s);
		assertFalse(s.isCurrent());
		assertEquals(0,s.size());
	}
	
	public void test81() {
		s.append(b1);
		s.addAll(s);
		assertEquals(2,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test82() {
		s.append(b1);
		s.advance();
		s.addAll(s);
		assertEquals(2,s.size());
		assertFalse(s.isCurrent());
	}
	
	public void test83() {
		s.append(b1);
		s.removeCurrent();
		assertEquals(0,s.size());
		assertFalse(s.isCurrent());
	}
	
	public void test84() {
		s.append(b2);
		s.append(b1);
		s.addAll(s);
		assertEquals(4,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
	}
	
	public void test85() {
		s.append(b1);
		s.append(b2);
		s.start();
		s.addAll(s);
		assertEquals(4,s.size());
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
	}

	public void test86() {
		s.append(b1);
		s.append(b2);
		s.append(null);
		s.advance();
		assertFalse(s.isCurrent());
		s.addAll(s);
		assertFalse(s.isCurrent());
		assertEquals(6,s.size());
		s.start();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(null,s.getCurrent()); s.advance();
		assertSame(b1,s.getCurrent()); s.advance();
		assertSame(b2,s.getCurrent()); s.advance();
		assertSame(null,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
	}
	
	public void test88() {
		s.append(b1);
		s.append(b2);
		s.addAll(s);
		s.addAll(s);
		s.addAll(s);
		s.addAll(s);
		s.addAll(s);
		assertEquals(64,s.size());
	}
		
	
	/// test9N: testing clone
	
	public void test90() {
		ParticleSeq c = s.clone();
		assertFalse(c.isCurrent());
		assertEquals(0, c.size());
	}
	
	public void test91() {
		s.append(b1);
		ParticleSeq c = s.clone();
		
		assertTrue(s.isCurrent());
		assertSame(b1,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
		
		assertTrue(c.isCurrent());
		assertSame(b1,c.getCurrent()); c.advance();
		assertFalse(c.isCurrent());
	}
	
	public void test92() {
		s.append(b1);
		s.advance();
		ParticleSeq c = s.clone();
		
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		c.start();
		assertTrue(c.isCurrent());
		assertSame(b1,c.getCurrent());
	}

	public void test93() {
		s.append(b1);
		s.append(b2);
		ParticleSeq c = s.clone();
		
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b2,s.getCurrent());
		assertSame(b2,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
	}
	
	public void test94() {
		s.append(b2);
		s.append(b1);
		s.append(b3);
		s.start();
		ParticleSeq c = s.clone();
		assertSame(b2,s.getCurrent());
		assertSame(b2,c.getCurrent());
		s.advance();
		c.advance();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b1,s.getCurrent());
		assertSame(b1,c.getCurrent());
		s.advance();
		c.advance();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(b3,s.getCurrent());
		assertSame(b3,c.getCurrent());
	}
	
	public void test95() {
		s.append(b1);
		ParticleSeq c = s.clone();
		s.advance();
		s.append(b2);
		assertSame(b2,s.getCurrent());
		assertSame(b1,c.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
		c.advance();
		s.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		assertEquals(1,c.size());
		assertEquals(2,s.size());
	}
	
	public void test96() {
		s = new ParticleSeq(10);
		// same as test95 !!
		s.append(b1);
		ParticleSeq c = s.clone();
		s.advance();
		s.append(b2);
		assertSame(b2,s.getCurrent());
		assertSame(b1,c.getCurrent());
		s.advance();
		assertSame(b1,s.getCurrent());
		c.advance();
		s.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		assertEquals(1,c.size());
		assertEquals(2,s.size());
	}
	
	public void test97() {
		s.append(b1);
		s.append(b2);
		s.append(b3);
		s.start();
		ParticleSeq c = s.clone();
		assertSame(b1,s.getCurrent());
		assertSame(b1,c.getCurrent());
		
		s.removeCurrent();
		
		assertSame(b2,s.getCurrent());
		assertSame(b1,c.getCurrent());
		
		assertEquals(2,s.size());
		assertEquals(3,c.size());
	}

	public void test98() {
		for (int i=0; i < 98; ++i) {
			s.append(b[i%b.length]);
		}
		ParticleSeq c = s.clone();
		c.start();
		c.removeCurrent();
		s.start();
		s.advance();
		for (int i=1; i < 98; ++i) {
			assertEquals(b[i%b.length],s.getCurrent());
			assertEquals(b[i%b.length],c.getCurrent());
			s.advance();
			c.advance();
		}
	}
	

}
