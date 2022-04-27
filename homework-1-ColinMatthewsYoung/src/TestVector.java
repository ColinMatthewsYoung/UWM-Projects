import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;


public class TestVector extends LockedTestCase {
	private Point p1, p2;
	private Vector v0, v1, v2;

	@Override
	protected void setUp() {
		p1 = new Point(2.0,3.0);
		p2 = new Point(-4.0,11.0);
		v0 = new Vector();
		v1 = new Vector(-3.0,0.0);
		v2 = new Vector(p1,p2);
	}

	public void testV0() {
		assertEquals( 0.0, v0.dx());
		assertEquals( 0.0, v0.dy());
	}

	public void testV1() {
		assertEquals( -3.0, v1.dx());
		assertEquals( 0.0, v1.dy());		
	}
	
	public void testV2() {
		// p1 = new Point(2.0,3.0);
		// p2 = new Point(-4.0,11.0);
		// v2 = new Vector(p1,p2);
		assertEquals(-6.0,v2.dx());
		assertEquals(8.0,v2.dy());
	}

	public void testV3() {
		assertEquals("<0.0,0.0>",v0.toString());
		assertEquals("<-6.0,0.0>",v1.add(v1).toString());
		assertEquals("<-3.0,0.0>",v1.toString());
		assertEquals("<-6.0,8.0>",v2.toString());
		Vector v3 = new Vector(1,3.99);
		assertEquals("<1.0,3.99>",v3.toString());
	}
	
	public void testV4() {
		// v1 = <-3,0>
		// p1 = (2,3)
		Point p = v1.move(p1);
		assertEquals(Td(718226249),p.x());
		assertEquals(3.0,p.y());
		
		// v2 = <-6,8>
		// p2 = (-4,11)
		p = v2.move(p2);
		assertEquals(-10.0,p.x());
		assertEquals(Td(139685712),p.y());
	}
	
	public void testV5() {
		// v1 = <-3,0>
		// v2 = <-6,8>
		Vector v = v1.add(v2);
		
		assertEquals(Td(478312381),v.dx());
		assertEquals(8.0,v.dy());
		
		Vector v3 = new Vector(8,-8);
		v = v.add(v3);
		assertEquals(-1.0,v.dx());
		assertEquals(0.0,v.dy());
	}
	
	public void testV6() {
		// v0 = <0,0>
		Vector v = v0.scale(Math.PI);
		
		assertEquals(0.0,v.dx());
		assertEquals(0.0,v.dy());
		
		// v1 = <-3,0>
		v = v1.scale(2.0);
		assertEquals(Td(6705108),v.dx());
		assertEquals(0.0,v.dy());
		
		// v2 = <-6,8>
		v = v2.scale(-0.25);
		assertEquals(1.5,v.dx());
		assertEquals(-2.0,v.dy());
		
		v = v.scale(0.0);
		assertEquals(0.0,v.dx());
		assertEquals(-0.0,v.dy());
	}
	
	public void testV7() {
		assertEquals( 0.0, v0.magnitude());
		// v1 = <-3,0>
		assertEquals( Td(1365897816), v1.magnitude());
		// v2 = <-6,8>
		assertEquals( Td(1409886087), v2.magnitude());
		assertEquals( Math.sqrt(2.0), new Vector(1,1).magnitude());
	}
	
	public void testV8() {
		// v1 = <-3,0>
		Vector v = v1.normalize();
		assertEquals(Td(630782561),v.dx());
		assertEquals(0.0,v.dy());
		
		assertEquals(5.0, new Vector(4,3).magnitude()); // useful information
		v = new Vector(4,3).normalize(); // read PDF to see what normalize does
		// in the following, we multiply the result by 1000 and round
		// Otherwise, we suffer from very small errors:
		assertEquals(800,(int)Math.round(v.dx()*1000));
		assertEquals(Ti(185361911),(int)Math.round(v.dy()*1000));
		
		// We won't test:   test(v0.normalize().toString(),"(NaN,NaN)","v0.normalize()");
	}

	public void testV9() {
		Vector v= new Vector(2.0,1.0);
		Point p = new Point(1,0);
		assertEquals("<2.0,1.0>",v.toString());
		// nothing of the following should have any effect:
		v.add(v);
		v.scale(3.14159);
		v.magnitude();
		v.normalize();
		v.move(p);
		assertEquals("<2.0,1.0>",v.toString()); // IMMUTABLE means doesn't change!
		assertEquals("(1.0,0.0)",p.toString());
	}

}
