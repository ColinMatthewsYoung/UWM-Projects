import edu.uwm.cs351.util.Stack;
import junit.framework.TestCase;


public class TestEfficiency extends TestCase {
	Stack<String> st;
	
	private static final int MAX_LENGTH = 1_000_000;
	private static final String[] STRINGS = {
			"A", "B", "C", "D", "E", 
			"F", "G", "H", "I", "J"
	};

	@Override
	public void setUp() {
		try {
			assert st.pop() == "OK";
			assertTrue(true);
		} catch (NullPointerException ex) {
			System.err.println("You must disable assertions to run this test.");
			System.err.println("Go to Run > Run Configurations. Select the 'Arguments' tab");
			System.err.println("Then remove '-ea' from the VM Arguments box.");
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);
		}
		st = new Stack<String>();
		for (int i=0; i < MAX_LENGTH; ++i) {
			st.push(STRINGS[i % STRINGS.length]);
		}
	}

	public void testIsEmpty() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertFalse(st.isEmpty());
		}
		st = new Stack<>();
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertTrue(st.isEmpty());
		}
	}
	
	public void testSize() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(MAX_LENGTH, st.size());
		}
	}
	
	public void testPeek() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals("J", st.peek());
		}
	}
	
	public void testPop() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(STRINGS[STRINGS.length-(i % STRINGS.length)-1], st.pop());
		}
	}
	
	public void testClear() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			st.clear();
		}
		assertTrue(st.isEmpty());
	}
	
	public void testClone() {
		// It's possible to write a O(1)
		// clone, but for this Homework, we only require O(n)
		Stack<String> clone = st.clone();
		assertEquals(MAX_LENGTH, clone.size());
	}
	
	public void testToString() {
		String str = st.toString();
		assertEquals (MAX_LENGTH*3, str.length());
	}
}
