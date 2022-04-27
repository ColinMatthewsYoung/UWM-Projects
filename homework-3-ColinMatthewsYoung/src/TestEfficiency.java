import java.awt.Color;
import java.util.Iterator;
import java.util.Random;

import edu.uwm.cs351.Particle;
import edu.uwm.cs351.ParticleCollection;
import edu.uwm.cs351.Point;
import edu.uwm.cs351.Vector;
import junit.framework.TestCase;


public class TestEfficiency extends TestCase {
	Particle e1 = new Particle(new Point(1,1),new Vector(),1.0,Color.WHITE); 
	Particle e2 = new Particle(new Point(2,2),new Vector(),2.0,Color.RED);
	Particle e3 = new Particle(new Point(3,3),new Vector(),3.0,Color.BLUE);
	Particle e4 = new Particle(new Point(4,4),new Vector(),4.0,Color.GREEN);
	Particle e5 = new Particle(new Point(5,5),new Vector(),5.0,Color.YELLOW);
	Particle e6 = new Particle(new Point(6,6),new Vector(),6.0,Color.GRAY);
	Particle e7 = new Particle(new Point(7,7),new Vector(),7.0,Color.MAGENTA);
	Particle e8 = new Particle(new Point(8,8),new Vector(),8.0,Color.ORANGE);

	Particle p[] = {null, e1, e2, e3, e4, e5, e6, e7, e8};
	
	ParticleCollection s;
	Random r;
	
	@Override
	public void setUp() {
		s = new ParticleCollection();
		r = new Random();
		try {
			assert 1/(int)(e4.getPosition().x()*4-1) == 42 : "OK";
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
			s.add(p[i%6]);
		}
		
		int sum = 0;
		Iterator<Particle> it = s.iterator();
		Particle current = it.next();
		for (int j=0; j < SAMPLE; ++j) {
			int n = r.nextInt(MAX_LENGTH/SAMPLE);
			for (int i=0; i < n; ++i) {
				current = it.next();
			}
			sum += n;
			assertSame(p[sum%6],current);
		}
	}
	
	private static final int MAX_WIDTH = 100000;
	
	public void testWide() {
		ParticleCollection[] a = new ParticleCollection[MAX_WIDTH];
		for (int i=0; i < MAX_WIDTH; ++i) {
			a[i] = s = new ParticleCollection();
			int n = r.nextInt(SAMPLE);
			for (int j=0; j < n; ++j) {
				s.add(p[j%6]);
			}
		}
		
		for (int j = 0; j < SAMPLE; ++j) {
			int i = r.nextInt(a.length);
			s = a[i];
			if (s.size() == 0) continue;
			int n = r.nextInt(s.size());
			Iterator<Particle> it = s.iterator();
			Particle current = it.next();
			for (int k=0; k < n; ++k) {
				current = it.next();
			}
			assertSame(p[n%6],current);
		}
	}
	
	public void testClear() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.add(p[i%6]);
		}
		s.clear();
		assert(s.isEmpty());
	}
	
}
