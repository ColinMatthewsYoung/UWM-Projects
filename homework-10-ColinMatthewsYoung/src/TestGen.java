// Testing sequences of 10 commands.
// 100000 tests passed
// 200000 tests passed
// 300000 tests passed
// 400000 tests passed
// 500000 tests passed
// 600000 tests passed
// 700000 tests passed
// 800000 tests passed
// 900000 tests passed
// 1000000 tests passed
// Testing sequences of 20 commands.
// 100000 tests passed
// 200000 tests passed
// 300000 tests passed
// 400000 tests passed
// 500000 tests passed
// 600000 tests passed
// 700000 tests passed
// 800000 tests passed
// 900000 tests passed
// 1000000 tests passed
// Testing sequences of 40 commands.
// 100000 tests passed
// 200000 tests passed
// 300000 tests passed
// 400000 tests passed
// 500000 tests passed
// 600000 tests passed
// 700000 tests passed
// 800000 tests passed
// 900000 tests passed
// 1000000 tests passed
// Testing sequences of 80 commands.
// 100000 tests passed
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;

import edu.uwm.cs351.ps.Name;
import edu.uwm.cs351.ps.Dictionary;
import edu.uwm.cs351.util.AbstractEntry;


public class TestGen extends TestCase {
	protected void assertException(Class<?> exc, Runnable r) {
		try {
			r.run();
			assertFalse("should have thrown an exception.",true);
		} catch (RuntimeException e) {
			if (exc == null) return;
			assertTrue("threw wrong exception type.",exc.isInstance(e));
		}
	}

	protected void assertEquals(int expected, Integer result) {
		super.assertEquals(Integer.valueOf(expected),result);
	}

	protected Name n(String s) { return new Name(s); }

	protected Map.Entry<Name,Object> e(Name n, Object o) {
		return new AbstractEntry<Name,Object>() {
			@Override public Name getKey() { return n; }
			@Override public Object getValue() { return o; }
		};
	}

	public void test() {
		Dictionary d0 = new Dictionary();
		assertNull(d0.put(n("n11"),650));
		assertNull((Object)(d0).get((Object)(n("n19"))));
		assertException(edu.uwm.cs351.ps.ExecutionException.class, () -> d0.get(n("hello")));
		assertNull(d0.put(n("n18"),n("n11")));
		assertNull(d0.put(n("n12"),new String("and")));
		assertNull(d0.put(n("apples"),476));
		assertEquals(false,d0.containsKey(n("roll")));
		Set<Map.Entry<Name,Object>> s0 = d0.entrySet();
		Iterator<Map.Entry<Name,Object>> i0 = s0.iterator();
		assertEquals(false,s0.contains(e(null,n("n16"))));
		assertEquals(e(n("apples"),476),i0.next());
		assertEquals(e(n("n11"),650),i0.next());
		assertEquals(false,s0.remove(e(n("atan"),new String("Fondly"))));
		assertEquals(true,d0.containsKey(n("n12")));
		assertNull((Object)(d0).get((Object)(null)));
		assertSame(s0,d0.entrySet());
		assertSame(s0,d0.entrySet());
		assertEquals(true,i0.hasNext());
		i0.remove(); // should terminate normally
		assertEquals(true,i0.hasNext());
		assertEquals(false,d0.containsKey(n("WI")));
		assertEquals(false,s0.remove(e(n("fo"),n("n15"))));
		assertSame(s0,d0.entrySet());
		assertEquals(e(n("n12"),new String("and")),i0.next());
		assertEquals(e(n("n18"),n("n11")),i0.next());
		assertEquals(3,d0.size());
		d0.copy(d0); // should terminate normally
		assertException(java.util.NoSuchElementException.class, () -> i0.next());
	}
}
