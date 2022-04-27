import java.util.Iterator;
import java.util.Random;

import edu.uwm.cs351.ps.Dictionary;
import edu.uwm.cs351.ps.Name;
import junit.framework.TestCase;


public class TestEfficiency extends TestCase {

	private Dictionary dict;
	
	private Random random;
	
	private static final int POWER = 21; // 1 million entries
	private static final int TESTS = 100_000;
	private static final int PREFIX = 10_000_000;
	
	protected static Name n(int i) {
		return new Name("N" + (PREFIX+i));
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		random = new Random();
		try {
			assert dict.size() == TESTS : "cannot run test with assertions enabled";
		} catch (NullPointerException ex) {
			throw new IllegalStateException("Cannot run test with assertions enabled");
		}
		dict = new Dictionary();
		int max = (1 << (POWER)); // 2^(POWER) = 2 million
		for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				dict.put(n(i), power);
			}
		}
	}
		
	
	@Override
	protected void tearDown() throws Exception {
		dict = null;
		super.tearDown();
	}

	public void testSize() {
		for (int i=0; i < TESTS; ++i) {
			assertEquals((1<<(POWER-1))-1,dict.size());
		}
	}

	public void testGet() {
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(TESTS);
			assertTrue(dict.known(n(r*4+2)));
			assertEquals(Integer.valueOf(2),dict.get(n(r*4+2)));
			assertFalse(dict.known(n(r*2+1)));
		}
	}
	
	public void testIterator() {
		Iterator<Object> it = dict.values().iterator();
		for (int i=2; i < TESTS; i += 2) {
			assertTrue("After " + i + " next(), should still have next",it.hasNext());
			Integer n = (Integer)it.next();
			if ((i & 3) == 2) {
				assertEquals(2,n.intValue());
			}
		}
	}

	public void testToString() {
		dict = new Dictionary();
		int max = TESTS;
		for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				dict.put(n(i), power);
			}
		}
		dict.toString();
	}
	
	public void testCopy() {
		Dictionary dict1 = new Dictionary();
		Dictionary dict2 = new Dictionary();
		int max = TESTS;
		for (int power = POWER; power > 0; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				dict2.put(n(i), power*10);
			}
		}
		int size = (1<<(POWER-1))-1;
		dict1.copy(dict);
		assertEquals(size,dict1.size());
		dict1.copy(dict2);
		assertEquals(size+(TESTS/2),dict1.size());
	}
}
