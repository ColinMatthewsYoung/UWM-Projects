import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;
import edu.uwm.cs351.ps.Dictionary;
import edu.uwm.cs351.ps.Name;
import edu.uwm.cs351.util.AbstractEntry;


public class TestEfficiency extends TestCase {

	private Dictionary dict;
	private Map<Name,Object> tree;
	
	private Random random;
	
	private static final int POWER = 20; // 1/2 million entries
	private static final int TESTS = 100_000;
	private static final int PREFIX = 10_000_000;
	
	protected static Name n(int i) {
		return new Name("N" + (PREFIX+i));
	}

	protected <T,U> Entry<T,U> e(T k, U v) {
		return new AbstractEntry<T,U>() {
			@Override
			public T getKey() {
				return k;
			}

			@Override
			public U getValue() {
				return v;
			}
		};
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		random = new Random();
		try {
			assert dict.size() == TESTS : "cannot run test with assertions enabled";
		} catch (NullPointerException ex) {
			throw new IllegalStateException("Cannot run test with assertions enabled");
		}
		tree = dict = new Dictionary();
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

	public void testE0() {
		for (int i=0; i < TESTS; ++i) {
			assertEquals((1<<(POWER-1))-1,dict.size());
		}
	}

	public void testE1() {
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(TESTS);
			assertTrue(dict.known(n(r*4+2)));
			assertEquals(Integer.valueOf(2),dict.get(n(r*4+2)));
			assertFalse(dict.known(n(r*2+1)));
		}
	}
	
	public void testE2() {
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
	
	public void testE3() {
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
	
	// Map versions
	
	public void testF0() {
		for (int i=0; i < TESTS; ++i) {
			assertFalse(tree.isEmpty());
		}
	}
	
	public void testF1() {
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(TESTS);
			assertTrue(tree.containsKey(n(r*4+2)));
			assertEquals(Integer.valueOf(2),tree.get((Object)n(r*4+2)));
			assertNull(tree.get((Object)n(r*2+1)));
			assertFalse(tree.containsKey((Object)n(r*2+1)));
		}
	}
	
	public void testF2() {
		Iterator<Name> it = tree.keySet().iterator();
		for (int i=0; i < TESTS; ++i) {
			assertTrue("After " + i + " next(), should still have next",it.hasNext());
			it.next();
		}
	}
	
	public void testF3() {
		for (int i=0; i < TESTS; ++i) {
			Iterator<Object> it = tree.values().iterator();
			assertTrue(it.hasNext());
			assertEquals(Integer.valueOf(2),it.next());
			assertTrue(it.hasNext());
			assertEquals(Integer.valueOf(3),it.next());
			assertTrue(it.hasNext());
		}
	}
	
	public void testF4() {
		Set<Integer> touched = new HashSet<Integer>();
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(TESTS);
			if (!touched.add(r)) continue; // don't check again
			assertEquals(Integer.valueOf(2),tree.remove((Object)n(r*4+2)));
			assertNull(tree.remove(n(r*2+1)));
		}
	}
	
	public void testF5() {
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(TESTS);
			assertFalse("should not contain bad entry for " + i,
					tree.entrySet().contains(e(n(r*4+2),1)));
			assertTrue("should contain entry for " + i,
					tree.entrySet().contains(e(n(r*4+2),2)));			
			assertFalse("should not contain non-existent entry for " + i,
					tree.entrySet().contains(e(n(r*4+1),2)));			
		}
	}
	
	public void testF6() {
		Set<Integer> touched = new HashSet<Integer>();
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(TESTS);
			if (!touched.add(r)) continue; // don't check again
			assertFalse("should not be able to remove bad entry for " + i,
					tree.entrySet().remove(e(n(r*4+2),1)));
			assertTrue("should be able to remove entry for " + i,
					tree.entrySet().remove(e(n(r*4+2),2)));
			assertFalse("should not be able to remove non-existent entry for " + i,
					tree.entrySet().remove(e(n(r*2+1),1)));
		}
	}
	
	public void testF7() {
		int removed = 0;
		int max = (1 << POWER);
		assertEquals(max/2-1,tree.size());
		Iterator<Entry<Name,Object>> it = tree.entrySet().iterator();
		for (int i = 2; i < max; i += 2) {
			assertEquals(n(i),it.next().getKey());
			if (random.nextBoolean()) {
				it.remove();
				++removed;
			}
		}
		assertEquals(max/2-1-removed,tree.size());
	}

	public void testF8() {
		for (int i=0; i < TESTS; ++i) {
			tree.clear();
			assertEquals(0, tree.size());
		}
	}
}
