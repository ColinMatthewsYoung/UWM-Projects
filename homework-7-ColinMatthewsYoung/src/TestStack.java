import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.util.Stack;


public class TestStack extends LockedTestCase {
	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (RuntimeException ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}

	private Stack<Integer> s;
	
	@Override
	protected void setUp() {
		try {
			assert s.isEmpty();
			assertFalse("Please turn on assertions!",true);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		s = new Stack<Integer>();
	}

	public void testS00() {
		Stack<String> s = new Stack<String>();
		assertTrue(s.isEmpty());
	}

	public void testS01() {
		assertTrue(s.isEmpty());
	}
	
	public void testS10() {
		s.push(77);
		assertEquals(Tb(1948889376), s.isEmpty());
		assertEquals(Ti(717212931), s.peek().intValue());
		assertEquals(Tb(1575342336), s.isEmpty());
		assertEquals(Ti(1634853426), s.pop().intValue());
		assertEquals(Tb(176940846), s.isEmpty());
	}

	public void testS12() {
		s.push(42);
		s.push(-9);
		assertEquals(Ti(1627457663),s.peek().intValue());
		assertFalse(s.isEmpty());
		assertEquals(Ti(1230102286),s.pop().intValue());
		assertFalse(s.isEmpty());
		assertEquals(Ti(2006237031),s.peek().intValue());
		assertEquals(42,s.pop().intValue());
		assertTrue(s.isEmpty());
	}
	
	public void testS13() {
		s.push(0);
		s.push(-32768);
		s.push(88);
		assertEquals(Ti(1667245384),s.peek().intValue());
		assertFalse(s.isEmpty());
		assertEquals(88,s.pop().intValue());
		assertFalse(s.isEmpty());
		assertEquals(-32768,s.peek().intValue());
		assertEquals(Ti(1859343223),s.pop().intValue());
		assertEquals(Ti(2050818480),s.pop().intValue());
		assertTrue(s.isEmpty());
	}
	
	public void testS19() {
		s.push(null);
		assertFalse(s.isEmpty());
		assertNull(s.peek());
		assertNull(s.pop());
		assertTrue(s.isEmpty());
	}

	public void testS20() {
		try {
			s.pop();
			assertFalse("pop of an empty stack returned",true);
		} catch (Exception e) {
			//what exception should be thrown?
			assertEquals(Ts(570783829), e.getClass().getSimpleName());
		}
	}

	public void testS21() {
		try {
			s.peek();
			assertFalse("peek on an empty stack returned",true);
		} catch (Exception e) {
			//what exception should be thrown?
			assertEquals(Ts(1903488762), e.getClass().getSimpleName());
		}
	}

	public void testS30() {
		assertTrue(s.clone().isEmpty());
	}

	public void testS31() {
		s.push(1001);
		Stack<Integer> c = s.clone();
		assertEquals(1001,c.pop().intValue());
		assertEquals(1001,s.peek().intValue());
	}

	public void testS32() {
		s.push(169);
		Stack<Integer> c = s.clone();
		s.push(343);
		c.push(-55);
		assertEquals(Ti(1620679750),c.peek().intValue());
		assertEquals(Ti(324933978),s.pop().intValue());
		assertEquals(-55,c.pop().intValue());
		assertEquals(Ti(1020844101),s.pop().intValue());
		assertEquals(Ti(695736991),c.pop().intValue());
	}
	
	public void testS33() {
		class Hidden extends Stack<String> {
			public boolean isEmpty() {
				return true;
			}
		}
		Hidden h = new Hidden();
		Stack<String> st = h.clone();
		assertTrue("clone didn't use super.clone()",st instanceof Hidden);
	}

	public void testS34() {
		Stack<String> s1 = new Stack<String>();
		Stack<String> s2 = s1.clone();
		s1.push("hello");
		s2.push("bye");
		assertEquals(Ts(2131980359),s1.pop());
		assertEquals(Ts(1875506833),s2.pop());
	}
	
	public void testS39() {
		for (int i=0; i < 1000; ++i) {
			s.push(i);
		}
		Stack<Integer> c = s.clone();
		for (int i=1; i < 500; ++i) {
			assertEquals(1000,i+c.pop());
			assertEquals(1000,i+s.pop());
		}
		s.push(-17);
		c.push(33);
		assertEquals(-17,s.pop().intValue());
		assertEquals(33,c.pop().intValue());
	}
	
	public void testS40() {
		for (int i=0; i < 1000; ++i) {
			s.push(i);
			assertFalse(s.isEmpty());
		}
		for (int i=0; i < 1000; ++i) {
			if ((i & 1) == 0) {
				assertEquals(999,i+s.peek());
			}
			assertEquals(999,i+s.pop());
		}
	}

	public void testS42() {
		for (int i=0; i < 1000; ++i) {
			s.push(i);
		}
		s.clear();
		
		assertTrue(s.isEmpty());
		
		s.clear();
		assertTrue(s.isEmpty());
	}
	
	public void testS50() {
		// elements separated by ", " in square brackets
		assertEquals(Ts(1790144628),s.toString());
	}
	
	public void testS51() {
		// elements separated by ", " in square brackets
		s.push(-4);
		assertEquals(Ts(789077849),s.toString());
	}
	
	public void testS52() {
		// elements separated by ", " in square brackets
		s.push(13);
		s.push(20);
		assertEquals(Ts(328978396),s.toString());
	}
	
	public void testS53() {
		s.push(null);
		s.push(-1492);
		s.push(null);
		assertEquals("[null, -1492, null]",s.toString());
	}
}
