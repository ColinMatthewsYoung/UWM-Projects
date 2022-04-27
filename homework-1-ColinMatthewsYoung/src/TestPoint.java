import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Point;


public class TestPoint extends LockedTestCase {
	Point p1, p2;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		p1 = new Point(2.0,3.0);
		p2 = new Point(-4.0,11.0);
	}

	public void test0() {
		// p1 = (2,3)
		// p2 = (-4,11)
		assertEquals( Td(1060106651), p1.x());
		assertEquals( 3.0, p1.y());
		assertEquals( -4.0, p2.x());
		assertEquals( Td(1855124357), p2.y());
		assertEquals( 2.0, p1.x());
	}

	public void test1() {
		// p1 = (2,3)
		// p2 = (-4,11)
		// If a Locked Test asks for a string, don't add quotes
		assertEquals(Ts(1443358720),p1.toString()); // remember: 2.0 prints as "2.0"
		assertEquals("(-4.0,11.0)",p2.toString());
	}

	public void test2() {
		Point p3 = new Point(0.3,4.67);
		assertEquals("(0.3,4.67)",p3.toString());
		Point p4 = new Point(-0.3,-4.67);
		assertEquals("(-0.3,-4.67)",p4.toString());
	}
	
	public void test3() {
		java.awt.Point ap1 = p1.asAWT();
		assertEquals( 2, ap1.x);
		assertEquals( 3, ap1.y);
		java.awt.Point ap2 = p2.asAWT();
		assertEquals( -4, ap2.x);
		assertEquals( 11, ap2.y);
	}
	
	public void test4() {
		Point p3 = new Point(1.3,4.7);
		java.awt.Point ap3 = p3.asAWT(); // rounds to nearest
		assertEquals( Ti(1944755747), ap3.x);
		assertEquals( Ti(654549201), ap3.y);
		
		Point p4 = new Point(-1.3,-4.7);
		java.awt.Point ap4 = p4.asAWT();
		assertEquals( -1, ap4.x);
		assertEquals( -5, ap4.y);		
	}
	
	public void test5() {
		// p1 = (2,3)
		// p2 = (-4,11)
		assertEquals( Td(386903507), p1.distance(new Point(2,-4)));
		assertEquals( 3.0, p1.distance(new Point(-1,3)));
		assertEquals( Td(1072040139), p1.distance(new Point(6,6)));
		assertEquals( 10.0, p1.distance(p2));
		assertEquals( 10.0, p2.distance(p1));
	}
	
	public void test6() {
		// p1 = (2,3)
		// p2 = (-4,11)
		assertEquals(Td(35988175), p1.distance(p1));
		assertEquals(0.0, p2.distance(new Point(-4,11)));
	}

	public void test7() {
		// these will pass unless you do something unexpected:
		assertEquals(true, p1.equals(p1));
		assertEquals(false, p1.equals(new Point(2,3)));
	}
}
