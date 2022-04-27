import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.uwm.cs351.Particle;
import edu.uwm.cs351.ParticleSeq;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;
import junit.framework.TestCase;


public class TestEfficiency extends TestCase {
	Vector v = new Vector();
	Particle p1 = new Particle(new Point(1.0,20.0),v,1,Color.RED);
	Particle p2 = new Particle(new Point(2.0,40.0),v,2,Color.BLUE);
	Particle p3 = new Particle(new Point(3.0,60.0),v,3,Color.YELLOW);
	Particle p4 = new Particle(new Point(4.0,80.0),v,4,Color.GREEN);
	Particle p5 = new Particle(new Point(5.0,100.0),v,5,Color.CYAN);
	Particle p6 = new Particle(new Point(6.0,120.0),v,6,Color.MAGENTA);
	Particle p7 = new Particle(new Point(7.0,140.0),v,7,Color.ORANGE);
	Particle p8 = new Particle(new Point(8.0,150.0),v,8,Color.BLACK);

	Particle p[] = {null, p1, p2, p3, p4, p5, p6, p7, p8};
	
	ParticleSeq s;
	Random r;
	
	@Override
	public void setUp() {
		s = new ParticleSeq();
		r = new Random();
		try {
			assert 1/(int)(p5.getPosition().x()-5.0) == 42 : "OK";
			assertTrue(true);
		} catch (ArithmeticException ex) {
			System.err.println("Assertions must NOT be enabled to use this test suite.");
			System.err.println("In Eclipse: remove -ea from the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);
		}
	}

	private static final int MAX_LENGTH = 1000000;
	private static final int SAMPLE = 100;
	
	public void testLong() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.append(p[i%6]);
		}
		
		int sum = 0;
		s.start();
		for (int j=0; j < SAMPLE; ++j) {
			int n = r.nextInt(MAX_LENGTH/SAMPLE);
			for (int i=0; i < n; ++i) {
				s.advance();
			}
			sum += n;
			assertSame(p[sum%6],s.getCurrent());
		}
	}
	
	private static final int MAX_WIDTH = 100000;
	
	public void testWide() {
		ParticleSeq[] a = new ParticleSeq[MAX_WIDTH];
		for (int i=0; i < MAX_WIDTH; ++i) {
			a[i] = s = new ParticleSeq();
			int n = r.nextInt(SAMPLE);
			for (int j=0; j < n; ++j) {
				s.append(p[j%6]);
			}
		}
		
		for (int j = 0; j < SAMPLE; ++j) {
			int i = r.nextInt(a.length);
			s = a[i];
			if (s.size() == 0) continue;
			int n = r.nextInt(s.size());
			s.start();
			for (int k=0; k < n; ++k) {
				s.advance();
			}
			assertSame(p[n%6],s.getCurrent());
		}
	}
	
	public void testStochastic() {
		List<ParticleSeq> ss = new ArrayList<ParticleSeq>();
		ss.add(s);
		int max = 1;
		for (int i=0; i < MAX_LENGTH; ++i) {
			if (r.nextBoolean()) {
				s = new ParticleSeq();
				s.append(p3);
				ss.add(s);
			} else {
				s.addAll(s); // double size of s
				if (s.size() > max) {
					max = s.size();
					// System.out.println("Reached " + max);
				}
			}
		}
	}
}
