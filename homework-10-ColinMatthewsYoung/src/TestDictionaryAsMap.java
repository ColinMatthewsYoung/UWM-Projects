import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.ps.Dictionary;
import edu.uwm.cs351.ps.ExecutionException;
import edu.uwm.cs351.ps.Name;
import edu.uwm.cs351.util.AbstractEntry;


@SuppressWarnings("unlikely-arg-type")
public class TestDictionaryAsMap extends LockedTestCase {
	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (RuntimeException ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}

	protected Map<Name,Object> d1;
	protected Iterator<Entry<Name,Object>> it, it2;
	protected Entry<Name,Object> e, e2;
	
	@Override
	protected void setUp() {
		try {
			assert d1.get(null) == d1;
			throw new IllegalStateException("assertions must be enabled to run this test");
		} catch (NullPointerException ex) {
			// OK!
		}
		d1 = new Dictionary();
		testImplementationQuestions(false);
	}

	@SuppressWarnings("unused")
	private void testImplementationQuestions(boolean ignored) {
		// Answer the following questions about implementing Dictionary
		// by extending AbstractMap. You may omit parentheses for method names.

		// What is the only method of TreeMap that must be implemented 
		// before the class can compile?  Give its name:
		String answer1 = Ts(1544555792);
		
		// Which method from AbstractMap must be overridden, 
		// or else it will always throw an exception? Give its name:
		String answer2 = Ts(1970106667);
		
		// The EntrySet extends AbstractSet and should leave 
		// what method to always throw an exception?
		String answer3 = Ts(1166716897);
		
		// Dictionary, EntrySet and MyIterator all have "remove" methods.
		// Which class will actually do the work?  Name it:
		String answer4 = Ts(1777672317);
	}
	
	
	/// convenience methods for creating names (n), and entries (e)
	
	protected Name n(String s) { return new Name(s); }
	
	protected Name n(int i) { return n((""+(1000+i)).substring(1)); }

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

	/**
	 * text0XX
	 * Testing get, put and size
	 */
	
	public void test000() {
		assertTrue(d1.isEmpty());
	}
	
	public void test001() {
		assertEquals(0,d1.size());
	}
	
	public void test002() {
		assertNull(d1.get(n(42))); // NB: Map version does not throw an exception
	}
	
	public void test003() {
		assertNull(d1.get(42));
	}
	
	public void test004() {
		assertNull(d1.get("hello"));
	}
	
	public void test005() {
		assertNull(d1.get(new Object()));
	}
	
	public void test006() {
		assertNull(d1.get(d1));
	}
	
	public void test007() {
		assertNull(d1.get(null));
	}
	
	public void test008() {
		assertException(ExecutionException.class,() -> d1.put(null,"hello"));
	}
	
	public void test009() {
		assertEquals(0,d1.entrySet().size());
	}

	public void test010() {
		d1.put(n("hello"),8);
		assertEquals(1,d1.size());
	}
	
	public void test011() {
		d1.put(n("bye"),9);
		assertNull(d1.get(n("hello")));
		assertNull(d1.get(n("apples")));
	}
	
	public void test012() {
		d1.put(n("time"), 10.30);
		assertNull(d1.get("time"));
		assertNull(d1.get(10.30));
	}
	
	public void test013() {
		d1.put(n("WI"), "Madison");
		assertNull(d1.get(n("Wi")));
	}
	
	public void test014() {
		d1.put(n("apples"),10);
		assertNull(d1.get(null));
	}
	
	public void test015() {
		d1.put(n("candy"),null);
		assertNull(d1.get("candy"));
	}
	
	public void test016() {
		d1.put(n("bacon"),null);
		assertEquals(1,d1.size());
	}
	
	public void test017() {
		d1.put(n(""),"");
		assertEquals("",d1.get(n("")));
	}
	
	public void test018() {
		Collection<Object> c = d1.values();
		d1.put(n(""), n("\0"));
		assertNull(d1.get("\0"));
		assertEquals(1,c.size()); // making sure you deleted bad definition of "values()"
	}
	
	public void test019() {
		Object x = d1.put(n("hello"),42);
		assertNull(x);
	}
	
	public void test020() {
		d1.put(n("apples"),5);
		d1.put(n("apples"),10);
		assertEquals(Integer.valueOf(10),d1.get(n("apples")));
	}
	
	public void test021() {
		d1.put(n("CA"),"San Francisco");
		d1.put(n("CA"),"Sacramento");
		assertEquals(1,d1.size());
	}
	
	public void test022() {
		d1.put(n("hello"),"bye");
		Object x = d1.put(n("hello"),"world");
		assertEquals("bye",x);
	}
	
	public void test023() {
		d1.put(n("MI"),"Detroit");
		d1.put(n("MI"), "Lansing");
		assertEquals("Lansing",d1.get(n("MI")));
	}
	
	public void test024() {
		d1.put(n("hello"),null);
		d1.put(n("hello"),"bye");
		assertEquals("bye",d1.get(n("hello")));
	}
	
	public void test025() {
		d1.put(n("Candy"),0);
		d1.put(n("Candy"),null);
		assertNull(d1.get(n("Candy")));
		assertEquals(1,d1.size());
	}

	public void test026() {
		d1.put(n("WI"),"Madison");
		d1.put(n("IL"),"Springfield");
		assertEquals(2,d1.size());
		assertEquals("Madison",d1.get(n("WI")));
		assertEquals("Springfield",d1.get(n("IL")));
	}
	
	public void test027() {
		d1.put(n("CA"),"Sacramento");
		d1.put(n("NV"),"Carson City");
		assertEquals("Carson City",d1.get(n("NV")));
	}
	
	public void test028() {
		d1.put(n("IL"),"Springfield");
		d1.put(n("WI"), "Madison");
		assertNull(d1.get(n("MI")));
		assertNull(d1.get(n("ND")));
		assertNull(d1.get(n("YK")));
	}
	
	public void test029() {
		d1.put(n("TX"),"Austin");
		d1.put(n("FL"),"Tallahassee");
		assertNull(d1.get(n("WI")));
		assertNull(d1.get(n("CA")));
		assertNull(d1.get(n("MO")));
	}

	public void test030() {
		d1.put(n("ND"),"Bismarck");
		Object x = d1.put(n("SD"),"Pierre");
		assertNull(x);
	}

	public void test031() {
		d1.put(n("ND"),"Bismarck");
		d1.put(n("SD"),"Sioux Falls");
		Object x = d1.put(n("SD"),"Pierre");
		assertEquals("Sioux Falls",x);
	}

	public void test032() {
		d1.put(n("ND"),"Bismarck");
		d1.put(n("SD"),"Sioux Falls");
		d1.put(n("SD"),"Pierre");
		assertEquals(2,d1.size());
	}

	public void test033() {
		d1.put(n("ND"),"Bismarck");
		d1.put(n("SD"),"Sioux Falls");
		d1.put(n("SD"),"Pierre");
		assertEquals("Pierre",d1.get(n("SD")));
	}

	public void test034() {
		d1.put(n("IN"),"Indianapolis");
		d1.put(n("OH"),"Columbus");
		d1.put(n("IN"),"Gary");
		assertEquals(2,d1.size());
	}
	
	public void test035() {
		d1.put(n("IN"),"Indianapolis");
		d1.put(n("OH"),"Columbus");
		d1.put(n("IN"),"Gary");
		assertEquals("Gary",d1.get(n("IN")));
	}

	public void test036() {
		d1.put(n("IN"),"Indianapolis");
		d1.put(n("OH"),"Columbus");
		Object x = d1.put(n("IN"),"Gary");
		assertEquals("Indianapolis",x);
	}
		
	public void test037() {
		d1.put(n("IL"),"Springfield");
		d1.put(n("WI"),"Madison");
		d1.put(n("IL"),"Chicago");
		d1.put(n("WI"),"Milwaukee");
		Object x = d1.put(n("WI"),"Madison");
		assertEquals("Milwaukee",x);
	}

	public void test038() {
		d1.put(n("IL"),"Springfield");
		d1.put(n("WI"),"Madison");
		d1.put(n("IL"),"Chicago");
		d1.put(n("WI"),"Milwaukee");
		d1.put(n("WI"),"Madison");
		Object x = d1.put(n("IL"),"Springfield");
		assertEquals("Chicago",x);
	}

	public void test039() {
		d1.put(n("IL"),"Springfield");
		d1.put(n("WI"),"Madison");
		d1.put(n("IL"),"Chicago");
		d1.put(n("WI"),"Milwaukee");
		d1.put(n("WI"),"Madison");
		d1.put(n("IL"),"Springfield");
		assertEquals(2,d1.size());
		assertEquals("Madison",d1.get(n("WI")));
		assertEquals("Springfield",d1.get(n("IL")));
	}

	public void test040(){
		assertNull(d1.put(n("c"), 63));
		assertNull(d1.put(n("a"), 61));
		assertNull(d1.put(n("f"), 66));
		assertNull(d1.put(n("b"), 62));
		assertNull(d1.put(n("e"), 65));
		assertNull(d1.put(n("d"), 64));
		assertNull(d1.put(n("g"), 67));
	}

	public void test050(){
		d1.put(n("c"), 63);
		d1.put(n("a"), 61);
		d1.put(n("f"), 66);
		d1.put(n("b"), 62);
		d1.put(n("e"), 65);
		d1.put(n("d"), 64);
		d1.put(n("g"), 67);
		
		assertEquals(7, d1.size());
		assertEquals(Integer.valueOf(61), d1.get(n("a")));
		assertEquals(Integer.valueOf(62), d1.get(n("b")));
		assertEquals(Integer.valueOf(63), d1.get(n("c")));
		assertEquals(Integer.valueOf(64), d1.get(n("d")));
		assertEquals(Integer.valueOf(65), d1.get(n("e")));
		assertEquals(Integer.valueOf(66), d1.get(n("f")));
		assertEquals(Integer.valueOf(67), d1.get(n("g")));
	}

	public void test060(){
		d1.put(n("c"), 63);
		d1.put(n("a"), 61);
		d1.put(n("f"), 66);
		d1.put(n("b"), 62);
		d1.put(n("e"), 65);
		d1.put(n("d"), 64);
		d1.put(n("g"), 67);
		
		assertNull(d1.get(n("")));
		assertNull(d1.get(n("aa")));
		assertNull(d1.get(n("b\0")));
		assertNull(d1.get(n("c ")));
		assertNull(d1.get(n("d.")));
		assertNull(d1.get(n("eA")));
		assertNull(d1.get(n("f0")));
		assertNull(d1.get(n("g*")));
	}

	public void test070(){
		d1.put(n("c"), 63);
		d1.put(n("a"), 61);
		d1.put(n("f"), 66);
		d1.put(n("b"), 62);
		d1.put(n("e"), 65);
		d1.put(n("d"), 64);
		d1.put(n("g"), 67);
		
		assertEquals(7, d1.size());
		assertEquals(Integer.valueOf(61), d1.put(n("a"), 97));
		assertEquals(7, d1.size());
		assertEquals(Integer.valueOf(62), d1.put(n("b"), 98));
		assertEquals(7, d1.size());
		assertEquals(Integer.valueOf(63), d1.put(n("c"), 99));
		assertEquals(7, d1.size());
		assertEquals(Integer.valueOf(64), d1.put(n("d"), 100));
		assertEquals(7, d1.size());
		assertEquals(Integer.valueOf(65), d1.put(n("e"), 101));
		assertEquals(7, d1.size());
		assertEquals(Integer.valueOf(66), d1.put(n("f"), 102));
		assertEquals(7, d1.size());
		assertEquals(Integer.valueOf(67), d1.put(n("g"), 103));
		assertEquals(7, d1.size());
	}

	public void test080(){
		d1.put(n("c"), 63);
		d1.put(n("a"), 61);
		d1.put(n("f"), 66);
		d1.put(n("b"), 62);
		d1.put(n("e"), 65);
		d1.put(n("d"), 64);
		d1.put(n("g"), 67);
		
		d1.put(n("a"), 97);
		d1.put(n("b"), 98);
		d1.put(n("c"), 99);
		d1.put(n("d"), 100);
		d1.put(n("e"), 101);
		d1.put(n("f"), 102);
		d1.put(n("g"), 103);
		
		assertEquals(Integer.valueOf(97),d1.get(n("a")));
		assertEquals(Integer.valueOf(98),d1.get(n("b")));
		assertEquals(Integer.valueOf(99),d1.get(n("c")));
		assertEquals(Integer.valueOf(100),d1.get(n("d")));
		assertEquals(Integer.valueOf(101),d1.get(n("e")));
		assertEquals(Integer.valueOf(102),d1.get(n("f")));
		assertEquals(Integer.valueOf(103),d1.get(n("g")));
	}

	public void test090() {
		assertException(UnsupportedOperationException.class, () -> d1.entrySet().add(e(n("hello"),"bye")));
	}
	
	
	/*
	 * Test 1XX:
	 * Testing the iterator, without remove, or (before test180) fail-fast semantics.
	 * Tests containsKey/contains which if not implemented uses iterator
	 * but if implemented should do the correct thing.
	 */
	
	public void test100() {
		it = d1.entrySet().iterator();
		assertFalse(it.hasNext());
	}
	
	public void test101() {
		d1.put(n("one hundred one"),101);
		it = d1.entrySet().iterator();
		assertTrue(it.hasNext());
	}
	
	public void test102() {
		d1.put(n("one hundred two"),102);
		it = d1.entrySet().iterator();
		assertEquals(e(n("one hundred two"),102),it.next());
	}
	
	public void test103() {
		d1.put(n("one hundred three"),103);
		it = d1.entrySet().iterator();
		it.next();
		assertFalse(it.hasNext());
	}
	
	public void test104() {
		d1.put(n("one hundred four"),104);
		it = d1.entrySet().iterator();
		it.next();
		assertEquals(Integer.valueOf(104),d1.get(n("one hundred four")));
	}
	
	public void test105() {
		d1.put(n("one hundred five"),105);
		it = d1.entrySet().iterator();
		e = it.next();
		assertEquals(n("one hundred five"),e.getKey());
		assertEquals(Integer.valueOf(105),e.getValue());
	}
	
	public void test106() {
		d1.put(n("one hundred six"),106);
		it = d1.entrySet().iterator();
		e = it.next();
		e.setValue(16);
		assertEquals(Integer.valueOf(16),d1.get(n("one hundred six")));
	}
	
	public void test107() {
		d1.put(n("one hundred seven"),107);
		it = d1.entrySet().iterator();
		e = it.next();
		e.setValue("107");
		assertFalse(it.hasNext());
	}
	
	public void test108() {
		d1.put(n("one hundred eight"),108);
		d1.put(n("one hundred eight"),"108");
		it = d1.entrySet().iterator();
		assertTrue(it.hasNext());
		assertEquals("108",it.next().getValue());
	}
	
	public void test109() {
		d1.put(n("one hundred nine"),109);
		d1.put(n("one hundred nine"),"109");
		it = d1.entrySet().iterator();
		e = it.next();
		it2 = d1.entrySet().iterator();
		e.setValue(109); // structure not changed.  iterator not stale
		assertEquals(109,it2.next().getValue());
		assertFalse(it.hasNext());
		assertFalse(it2.hasNext());
	}
	
	public void test110() {
		d1.put(n("one hundred ten"),110);
		d1.put(n("sixty"),60);
		it = d1.entrySet().iterator();
		assertTrue(it.hasNext());
		assertEquals(e(n("one hundred ten"),110),it.next());
	}
	
	public void test111() {
		d1.put(n("one hundred eleven"),111);
		d1.put(n("sixty one"),61);
		it = d1.entrySet().iterator();
		it.next();
		assertTrue(it.hasNext());
		assertEquals(e(n("sixty one"),61),it.next());
		assertFalse(it.hasNext());
	}
	
	public void test112() {
		d1.put(n("sixty two"),62);
		d1.put(n("one hundred twelve"),112);
		it = d1.entrySet().iterator();
		assertEquals(e(n("one hundred twelve"),112),it.next());
		assertEquals(e(n("sixty two"),62),it.next());
	}
	
	public void test113() {
		d1.put(n("sixty three"),63);
		d1.put(n("one hundred thirteen"),113);
		it = d1.entrySet().iterator();
		assertEquals(e(n("one hundred thirteen"),113),it.next());
		it2 = d1.entrySet().iterator();
		assertEquals(e(n("sixty three"),63),it.next());
		assertEquals(e(n("one hundred thirteen"),113),it2.next());
	}
	
	public void test114() {
		d1.put(n("one hundred fourteen"),114);
		d1.put(n("sixty four"),64);
		it = d1.entrySet().iterator();
		it.next();
		d1.put(n("sixty four"),"64"); // doesn't change structure
		assertTrue(it.hasNext());
		assertEquals(e(n("sixty four"),"64"),it.next());
		assertFalse(it.hasNext());
	}
	
	public void test120() {
		d1.put(n("seventy"),70);
		d1.put(n("zero"),0);
		d1.put(n("one hundred twenty"),120);
		it = d1.entrySet().iterator();
		assertTrue(it.hasNext());
		assertEquals(120,it.next().getValue());
		assertTrue(it.hasNext());
		assertEquals(70,it.next().getValue());
		assertTrue(it.hasNext());
		assertEquals(0,it.next().getValue());
		assertFalse(it.hasNext());
	}
	
	public void test121() {
		d1.put(n("one hundred twenty-one"),121);
		d1.put(n("zero"),0);
		d1.put(n("seventy-one"),71);
		it = d1.entrySet().iterator();
		assertTrue(it.hasNext());
		assertEquals(121,it.next().getValue());
		assertTrue(it.hasNext());
		assertEquals(71,it.next().getValue());
		assertTrue(it.hasNext());
		assertEquals(0,it.next().getValue());
		assertFalse(it.hasNext());
	}
	
	public void test122() {
		d1.put(n("one hundred twenty-two"),122);
		d1.put(n("seventy-two"),72);
		d1.put(n("two"),2);
		it = d1.entrySet().iterator();
		assertTrue(it.hasNext());
		assertEquals(122,it.next().getValue());
		assertTrue(it.hasNext());
		assertEquals(72,it.next().getValue());
		assertTrue(it.hasNext());
		assertEquals(2,it.next().getValue());
		assertFalse(it.hasNext());
	}
	
	public void test123() {
		d1.put(n("three"),3);
		d1.put(n("one hundred twenty-three"),123);
		d1.put(n("seventy-three"),73);
		it = d1.entrySet().iterator();
		assertTrue(it.hasNext());
		assertEquals(123,it.next().getValue());
		assertTrue(it.hasNext());
		assertEquals(73,it.next().getValue());
		assertTrue(it.hasNext());
		assertEquals(3,it.next().getValue());
		assertFalse(it.hasNext());
	}
	
	public void test124() {
		d1.put(n("twenty-four"),24);
		d1.put(n("one hundred twenty-four"),124);
		d1.put(n("four"),4);
		it = d1.entrySet().iterator();
		assertTrue(it.hasNext());
		assertEquals(4,it.next().getValue());
		assertTrue(it.hasNext());
		assertEquals(124,it.next().getValue());
		assertTrue(it.hasNext());
		assertEquals(24,it.next().getValue());
		assertFalse(it.hasNext());
	}
	
	public void test125() {
		d1.put(n("one hundred twenty-five"),125);
		d1.put(n("twenty-five"),25);
		d1.put(n("five"),5);
		d1.put(n("one hundred twenty-five"),"125");
		d1.put(n("twenty-five"),"25");
		d1.put(n("five"),"5");
		it = d1.entrySet().iterator();
		assertTrue(it.hasNext());
		assertEquals("5",it.next().getValue());
		assertTrue(it.hasNext());
		assertEquals("125",it.next().getValue());
		assertTrue(it.hasNext());
		assertEquals("25",it.next().getValue());
		assertFalse(it.hasNext());
	}
	
	public void test126() {
		d1.put(n("one hundred twenty-six"),126);
		d1.put(n("twenty-six"),26);
		d1.put(n("seventy-six"),76);
		d1.put(n("six"),6);
		it = d1.entrySet().iterator();
		assertEquals(126,it.next().getValue());
		assertEquals(76,it.next().getValue());
		assertEquals(6,it.next().getValue());
		assertEquals(26,it.next().getValue());
	}
	
	public void test127() {
		d1.put(n("127"),"one hundred twenty-seven");
		d1.put(n("27"),"twenty-seven");
		d1.put(n("77"),"seventy-seven");
		d1.put(n("7"),"seven");
		d1.put(n("1"),"one");
		it = d1.entrySet().iterator();
		assertEquals("1",it.next().getKey().rep);
		assertEquals("127",it.next().getKey().rep);
		assertEquals("27",it.next().getKey().rep);
		assertEquals("7",it.next().getKey().rep);
		assertEquals("77",it.next().getKey().rep);
	}
	
	public void test128() {
		d1.put(n("128"),"one hundred twenty-eight");
		d1.put(n("028"),"twenty-eight");
		d1.put(n("078"),"seventy-eight");
		d1.put(n("008"),"eight");
		d1.put(n("001"),"one");
		d1.put(n("280"),"two hundren eighty");
		Iterator<Name> it = d1.keySet().iterator();
		assertEquals("001",it.next().rep);
		assertEquals("008",it.next().rep);
		assertEquals("028",it.next().rep);
		assertEquals("078",it.next().rep);
		assertEquals("128",it.next().rep);
		assertEquals("280",it.next().rep);
	}
	
	public void test129() {
		Collection<Object> v = d1.values();
		d1.put(n("one hundred twenty-nine"),129);
		d1.put(n("twenty-nine"),29);
		d1.put(n("seventy-nine"),79);
		d1.put(n("nine"),9);
		d1.put(n("eight"),8);
		d1.put(n("twelve"),12);
		d1.put(n("one"),1);
		Iterator<Object> it = v.iterator();
		assertEquals(8,it.next());
		assertEquals(9,it.next());
		assertEquals(1,it.next());
		assertEquals(129,it.next());
		assertEquals(79,it.next());
		assertEquals(12,it.next());
		assertEquals(29,it.next());
		assertFalse(it.hasNext());
	}

	private void makeTree() {
		// (((1)2(((3(4))5)6((7(8))9)))10((11)12(13)))
		d1.put(n(100),"100");
		d1.put(n(20),"20");
		d1.put(n(10),"10");
		d1.put(n(60),"60");
		d1.put(n(50),"50");
		d1.put(n(30),"30");
		d1.put(n(40),"40");
		d1.put(n(90), "90");
		d1.put(n(70), "70");
		d1.put(n(80),"80");
		d1.put(n(120),"120");
		d1.put(n(110),"110");
		d1.put(n(130),"130");
	}

	public void test130() {
		Set<Name> s = d1.keySet();
		makeTree();
		Iterator<Name> it = s.iterator();
		assertEquals(n(10),it.next());
		assertEquals(n(20),it.next());
		assertEquals(n(30),it.next());
		assertEquals(n(40),it.next());
		assertEquals(n(50),it.next());
		assertEquals(n(60),it.next());
		assertEquals(n(70),it.next());
		assertEquals(n(80),it.next());
		assertEquals(n(90),it.next());
		assertEquals(n(100),it.next());
		assertEquals(n(110),it.next());
		assertEquals(n(120),it.next());
		assertEquals(n(130),it.next());
	}
	
	public void test140() {
		makeTree();
		assertTrue(d1.containsKey(n(10)));
		assertTrue(d1.containsKey(n(20)));
		assertTrue(d1.containsKey(n(30)));
		assertTrue(d1.containsKey(n(40)));
		assertTrue(d1.containsKey(n(50)));
		assertTrue(d1.containsKey(n(60)));
		assertTrue(d1.containsKey(n(70)));
		assertTrue(d1.containsKey(n(80)));
		assertTrue(d1.containsKey(n(90)));
		assertTrue(d1.containsKey(n(100)));
		assertTrue(d1.containsKey(n(110)));
		assertTrue(d1.containsKey(n(120)));
		assertTrue(d1.containsKey(n(130)));
	}
	
	public void test145() {
		makeTree();
		assertFalse(d1.containsKey(n(0)));
		assertFalse(d1.containsKey(n(15)));
		assertFalse(d1.containsKey(n(25)));
		assertFalse(d1.containsKey(n(35)));
		assertFalse(d1.containsKey(n(45)));
		assertFalse(d1.containsKey(n(55)));
		assertFalse(d1.containsKey(n(65)));
		assertFalse(d1.containsKey(n(75)));
		assertFalse(d1.containsKey(n(85)));
		assertFalse(d1.containsKey(n(95)));
		assertFalse(d1.containsKey(n(105)));
		assertFalse(d1.containsKey(n(115)));
		assertFalse(d1.containsKey(n(125)));
		assertFalse(d1.containsKey(n(135)));
		assertFalse(d1.containsKey(n(140)));
	}
	
	public void test149() {
		makeTree();
		assertFalse(d1.containsKey("10"));
		assertFalse(d1.containsKey(null));
		assertFalse(d1.containsKey(new Object()));
	}
	
	public void test150() {
		d1.put(n("one hundred fifty"), 150);
		assertTrue(d1.entrySet().contains(e(n("one hundred fifty"),150)));
	}
	
	public void test151() {
		d1.put(n("one hundred fifty-one"),151);
		assertFalse(d1.entrySet().contains(e(n("one hundred fifty-one"),150)));
	}
	
	public void test152() {
		d1.put(n("one hundred fifty-two"),152);
		assertFalse(d1.entrySet().contains(e(n("one hundred fifty-two"),null)));
	}

	public void test153() {
		d1.put(n("one hundred fifty-three"),null);
		assertTrue(d1.entrySet().contains(e(n("one hundred fifty-three"),null)));
	}

	public void test154() {
		d1.put(n("one hundred fifty-four"),null);
		assertFalse(d1.entrySet().contains(e(n("one hundred fifty-four"),154)));
	}

	public void test155() {
		d1.put(n("one hundred fifty-five"),155);
		assertFalse(d1.entrySet().contains(e("/one hundred fifty-five",155)));
	}
	
	public void test156() {
		d1.put(n("one hundred fifty-six"),156);
		assertFalse(d1.entrySet().contains(e((Name)null,156)));
	}
	
	public void test157() {
		d1.put(n("one hundred fifty-seven"),157);
		assertFalse(d1.entrySet().contains("/one hundred fifty-seven=157"));
	}
	
	public void test158() {
		d1.put(n("one hundred fifty-eight"),158);
		assertFalse(d1.entrySet().contains(158));
	}
	
	public void test159() {
		d1.put(n("one hundred fifty-nine"),159);
		assertFalse(d1.entrySet().contains(null));
	}
	
	public void test160() {
		Set<Entry<Name,Object>> s = d1.entrySet();
		makeTree();
		assertTrue(s.contains(e(n(10),"10")));
		assertTrue(s.contains(e(n(20),"20")));
		assertTrue(s.contains(e(n(30),"30")));
		assertTrue(s.contains(e(n(40),"40")));
		assertTrue(s.contains(e(n(50),"50")));
		assertTrue(s.contains(e(n(60),"60")));
		assertTrue(s.contains(e(n(70),"70")));
		assertTrue(s.contains(e(n(80),"80")));
		assertTrue(s.contains(e(n(90),"90")));
		assertTrue(s.contains(e(n(100),"100")));
		assertTrue(s.contains(e(n(110),"110")));
		assertTrue(s.contains(e(n(120),"120")));
		assertTrue(s.contains(e(n(130),"130")));
	}
	
	public void test170() {
		makeTree();
		Set<Entry<Name,Object>> s = d1.entrySet();
		assertFalse(s.contains(e(n(10),"1")));
		assertFalse(s.contains(e(n(20),"2")));
		assertFalse(s.contains(e(n(30),"3")));
		assertFalse(s.contains(e(n(40),"4")));
		assertFalse(s.contains(e(n(50),"5")));
		assertFalse(s.contains(e(n(60),"6")));
		assertFalse(s.contains(e(n(70),"7")));
		assertFalse(s.contains(e(n(80),"8")));
		assertFalse(s.contains(e(n(90),"9")));
		assertFalse(s.contains(e(n(100),"10")));
		assertFalse(s.contains(e(n(110),"11")));
		assertFalse(s.contains(e(n(120),"12")));
		assertFalse(s.contains(e(n(130),"13")));
	}
	
	public void test179() {
		makeTree();
		assertEquals(13,d1.size());
		assertNull(d1.get(0));
	
		for (int i=10; i <= 130; i+=10)
			assertEquals(""+i,d1.get(n(i)));
	
		for (int i=5; i < 130; i+=10)
			assertNull(d1.get(i));
	
		assertNull(d1.get(14));
		
		testCollection(d1.keySet(),"BIG",n(10),n(20),n(30),n(40),n(50),n(60),n(70),n(80),n(90),n(100),n(110),n(120),n(130));
	
		for (int i=10; i <= 130; i += 20)
			d1.put(n(i), "-"+i);
		
		assertEquals(13,d1.size());
	
		for (int i=10; i <= 130; i += 10) {
			String expected = (((i/10)&1) == 0 ? "" : "-") + i;
			assertEquals(expected,d1.get(n(i)));
		}
		testCollection(d1.keySet(),"BIG-",n(10),n(20),n(30),n(40),n(50),n(60),n(70),n(80),n(90),n(100),n(110),n(120),n(130));
		
	}

	/*
	 * Fail fast semantics (without remove) testing starts here.
	 */
	
	public void test180() {
		it = d1.entrySet().iterator();
		d1.put(n("180"), 180);
		assertException(ConcurrentModificationException.class,() -> it.hasNext());
	}
	
	public void test181() {
		it = d1.entrySet().iterator();
		d1.put(n("181"), 181);
		assertException(ConcurrentModificationException.class,() -> it.next());
	}
	
	public void test182() {
		d1.put(n("182"),182);
		it = d1.entrySet().iterator();
		assertTrue(it.hasNext());
		d1.put(n("0"),0);
		assertException(ConcurrentModificationException.class,() -> it.hasNext());
	}
	
	public void test183() {
		d1.put(n("183"),183);
		d1.put(n("0"),0);
		it = d1.entrySet().iterator();
		d1.put(n("200"),200);
		assertException(ConcurrentModificationException.class,() -> it.hasNext());
	}
	
	public void test184() {
		d1.put(n("184"),184);
		it = d1.entrySet().iterator();
		d1.put(n("0"),0);
		it2 = d1.entrySet().iterator();
		assertTrue(it2.hasNext());
		assertEquals(0,it2.next().getValue());
		d1.put(n("200"),200);
		assertException(ConcurrentModificationException.class,() -> it2.next());
	}
	
	public void test190() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next();
		it.next();
		it.next();
		d1.put(n(0),"0");
		assertException(ConcurrentModificationException.class,() -> it.hasNext());
	}
	
	/**
	 * test2XX test remove in the main class, clear in the main class,
	 * and clear in the entry set class.
	 */
	
	public void test200() {
		assertNull(d1.remove(n("hello")));
	}
	
	public void test201() {
		assertNull(d1.remove("bye"));
	}
	
	public void test202() {
		assertNull(d1.remove(new Object()));
	}
	
	public void test203() {
		assertNull(d1.remove(null));
	}
	
	public void test210() {
		d1.put(n("apples"),10);
		assertEquals(10,d1.remove(n("apples")));
	}
	
	public void test211() {
		d1.put(n("bread"),2);
		assertNull(d1.remove("/bread"));
	}
	
	public void test212() {
		d1.put(n("carrots"),12);
		assertNull(d1.remove("carrots"));
	}
	
	public void test213() {
		d1.put(n("donuts"),0);
		assertNull(d1.remove(n("apples")));
		assertNull(d1.remove(n("eggs")));
	}
	
	public void test214() {
		d1.put(n("eggs"),12);
		d1.remove(n("eggs"));
		assertEquals(0,d1.size());
	}
	
	public void test215() {
		d1.put(n("fish"),1);
		d1.remove("fish");
		assertNull(d1.get("fish"));
	}
	
	public void test216() {
		d1.put(n("grapes"),"1 lb.");
		d1.remove(n("hummus"));
		assertEquals(1,d1.size());
	}
	
	public void test217() {
		d1.put(n("ice-cream"),"1 pint");
		Set<Entry<Name,Object>> s = d1.entrySet();
		d1.remove(n("ice-cream"));
		assertEquals(0,s.size());
	}
	
	public void test220() {
		d1.put(n(220),"220");
		d1.put(n(22),"22");
		assertEquals("22",d1.remove(n(22)));
	}
	
	public void test221() {
		d1.put(n(221), "221");
		d1.put(n(21), "21");
		d1.remove(n(21));
		assertEquals(1,d1.size());
	}
	
	public void test222() {
		d1.put(n(222),"222");
		d1.put(n(22), "22");
		d1.remove(n(22));
		assertEquals("222",d1.get(n(222)));
	}
	
	public void test223() {
		d1.put(n(223),"223");
		d1.put(n(23),"23");
		assertEquals("223",d1.remove(n(223)));
	}
	
	public void test224() {
		d1.put(n(224),"224");
		d1.put(n(24),"24");
		d1.remove(n(224));
		assertEquals(1,d1.size());
	}
	
	public void test225() {
		d1.put(n(225),"225");
		d1.put(n(25),"25");
		d1.remove(n(225));
		assertEquals("25",d1.get(n(25)));
	}
	
	public void test226() {
		d1.put(n(226),"226");
		d1.put(n(622),"622");
		assertEquals("622",d1.remove(n(622)));
		assertEquals(1,d1.size());
		assertEquals("226",d1.get(n(226)));
	}
	
	public void test227() {
		d1.put(n(227),"227");
		d1.put(n(722), "722");
		assertEquals("227",d1.remove(n(227)));
		assertEquals(1,d1.size());
		assertEquals("722",d1.get(n(722)));
	}

	public void test230() {
		d1.put(n("apples"),5);
		d1.put(n("bread"),1);
		d1.put(n("carrots"),8);
		assertEquals(5,d1.remove(n("apples")));
		assertEquals(2,d1.size());
		assertEquals(null,d1.get(n("apples")));
		assertEquals(1,d1.get(n("bread")));
		assertEquals(8,d1.get(n("carrots")));
	}

	public void test231() {
		d1.put(n("apples"),5);
		d1.put(n("bread"),1);
		d1.put(n("carrots"),8);
		assertEquals(1,d1.remove(n("bread")));
		assertEquals(2,d1.size());
		assertEquals(5,d1.get(n("apples")));
		assertEquals(null,d1.get(n("bread")));
		assertEquals(8,d1.get(n("carrots")));
	}

	public void test232() {
		d1.put(n("apples"),5);
		d1.put(n("bread"),1);
		d1.put(n("carrots"),8);
		assertEquals(8,d1.remove(n("carrots")));
		assertEquals(2,d1.size());
		assertEquals(5,d1.get(n("apples")));
		assertEquals(1,d1.get(n("bread")));
		assertEquals(null,d1.get(n("carrots")));
	}

	public void test234() {
		d1.put(n("donuts"),0);
		d1.put(n("fish"),1);
		d1.put(n("eggs"),12);
		assertEquals(0,d1.remove(n("donuts")));
		assertEquals(2,d1.size());
		assertEquals(null,d1.remove(n("donuts")));
		assertEquals(12,d1.remove(n("eggs")));
		assertEquals(1,d1.remove(n("fish")));
	}

	public void test235() {
		d1.put(n("donuts"),0);
		d1.put(n("fish"),1);
		d1.put(n("eggs"),12);
		assertEquals(12,d1.remove(n("eggs")));
		assertEquals(2,d1.size());
		assertEquals(0,d1.remove(n("donuts")));
		assertEquals(null,d1.remove(n("eggs")));
		assertEquals(1,d1.remove(n("fish")));
	}

	public void test236() {
		d1.put(n("donuts"),0);
		d1.put(n("fish"),1);
		d1.put(n("eggs"),12);
		assertEquals(1,d1.remove(n("fish")));
		assertEquals(2,d1.size());
		assertEquals(0,d1.remove(n("donuts")));
		assertEquals(12,d1.remove(n("eggs")));
		assertEquals(null,d1.remove(n("fish")));
	}
	
	public void test237() {
		d1.put(n("lemons"),"3");
		d1.put(n("milk"),"1 gal");
		d1.put(n("grapes"),"1 lb");
		assertEquals("1 lb",d1.remove(n("grapes")));
		assertEquals(2,d1.size());
		assertEquals(null,d1.get(n("grapes")));
		assertEquals("3",d1.get(n("lemons")));
		assertEquals("1 gal",d1.get(n("milk")));
	}
	
	public void test238() {
		d1.put(n("lemons"),"3");
		d1.put(n("milk"),"1 gal");
		d1.put(n("grapes"),"1 lb");
		assertEquals("1 gal",d1.remove(n("milk")));
		assertEquals(2,d1.size());
		assertEquals("1 lb",d1.get(n("grapes")));
		assertEquals("3",d1.get(n("lemons")));
		assertEquals(null,d1.get(n("milk")));
	}
	
	public void test239() {
		d1.put(n("lemons"),"3");
		d1.put(n("milk"),"1 gal");
		d1.put(n("grapes"),"1 lb");
		assertEquals("3",d1.remove(n("lemons")));
		assertEquals(2,d1.size());
		assertEquals("1 lb",d1.get(n("grapes")));
		assertEquals(null,d1.get(n("lemons")));
		assertEquals("1 gal",d1.get(n("milk")));
	}
	
	public void test240() {
		makeTree();
		assertEquals("10",d1.remove(n(10)));
		assertEquals(null,d1.get(n(10)));
		assertEquals("20",d1.get(n(20)));
		assertEquals("30",d1.get(n(30)));
		assertEquals("40",d1.get(n(40)));
		assertEquals("50",d1.get(n(50)));
		assertEquals("60",d1.get(n(60)));
		assertEquals("70",d1.get(n(70)));
		assertEquals("80",d1.get(n(80)));
		assertEquals("90",d1.get(n(90)));
		assertEquals("100",d1.get(n(100)));
		assertEquals("110",d1.get(n(110)));
		assertEquals("120",d1.get(n(120)));
		assertEquals("130",d1.get(n(130)));
	}
	
	public void test241() {
		makeTree();
		assertEquals("20",d1.remove(n(20)));
		assertEquals("10",d1.get(n(10)));
		assertEquals(null,d1.get(n(20)));
		assertEquals("30",d1.get(n(30)));
		assertEquals("40",d1.get(n(40)));
		assertEquals("50",d1.get(n(50)));
		assertEquals("60",d1.get(n(60)));
		assertEquals("70",d1.get(n(70)));
		assertEquals("80",d1.get(n(80)));
		assertEquals("90",d1.get(n(90)));
		assertEquals("100",d1.get(n(100)));
		assertEquals("110",d1.get(n(110)));
		assertEquals("120",d1.get(n(120)));
		assertEquals("130",d1.get(n(130)));		
	}

	public void test242() {
		makeTree();
		assertEquals("30",d1.remove(n(30)));
		assertEquals("10",d1.get(n(10)));
		assertEquals("20",d1.get(n(20)));
		assertEquals(null,d1.get(n(30)));
		assertEquals("40",d1.get(n(40)));
		assertEquals("50",d1.get(n(50)));
		assertEquals("60",d1.get(n(60)));
		assertEquals("70",d1.get(n(70)));
		assertEquals("80",d1.get(n(80)));
		assertEquals("90",d1.get(n(90)));
		assertEquals("100",d1.get(n(100)));
		assertEquals("110",d1.get(n(110)));
		assertEquals("120",d1.get(n(120)));
		assertEquals("130",d1.get(n(130)));
	}
	
	public void test243() {
		makeTree();
		assertEquals("40",d1.remove(n(40)));
		assertEquals("10",d1.get(n(10)));
		assertEquals("20",d1.get(n(20)));
		assertEquals("30",d1.get(n(30)));
		assertEquals(null,d1.get(n(40)));
		assertEquals("50",d1.get(n(50)));
		assertEquals("60",d1.get(n(60)));
		assertEquals("70",d1.get(n(70)));
		assertEquals("80",d1.get(n(80)));
		assertEquals("90",d1.get(n(90)));
		assertEquals("100",d1.get(n(100)));
		assertEquals("110",d1.get(n(110)));
		assertEquals("120",d1.get(n(120)));
		assertEquals("130",d1.get(n(130)));
	}
	
	public void test244() {
		makeTree();
		assertEquals("50",d1.remove(n(50)));
		assertEquals("10",d1.get(n(10)));
		assertEquals("20",d1.get(n(20)));
		assertEquals("30",d1.get(n(30)));
		assertEquals("40",d1.get(n(40)));
		assertEquals(null,d1.get(n(50)));
		assertEquals("60",d1.get(n(60)));
		assertEquals("70",d1.get(n(70)));
		assertEquals("80",d1.get(n(80)));
		assertEquals("90",d1.get(n(90)));
		assertEquals("100",d1.get(n(100)));
		assertEquals("110",d1.get(n(110)));
		assertEquals("120",d1.get(n(120)));
		assertEquals("130",d1.get(n(130)));
	}
	
	public void test245() {
		makeTree();
		assertEquals("60",d1.remove(n(60)));
		assertEquals("10",d1.get(n(10)));
		assertEquals("20",d1.get(n(20)));
		assertEquals("30",d1.get(n(30)));
		assertEquals("40",d1.get(n(40)));
		assertEquals("50",d1.get(n(50)));
		assertEquals(null,d1.get(n(60)));
		assertEquals("70",d1.get(n(70)));
		assertEquals("80",d1.get(n(80)));
		assertEquals("90",d1.get(n(90)));
		assertEquals("100",d1.get(n(100)));
		assertEquals("110",d1.get(n(110)));
		assertEquals("120",d1.get(n(120)));
		assertEquals("130",d1.get(n(130)));
	}
	
	public void test246() {
		makeTree();
		assertEquals("70",d1.remove(n(70)));
		assertEquals("10",d1.get(n(10)));
		assertEquals("20",d1.get(n(20)));
		assertEquals("30",d1.get(n(30)));
		assertEquals("40",d1.get(n(40)));
		assertEquals("50",d1.get(n(50)));
		assertEquals("60",d1.get(n(60)));
		assertEquals(null,d1.get(n(70)));
		assertEquals("80",d1.get(n(80)));
		assertEquals("90",d1.get(n(90)));
		assertEquals("100",d1.get(n(100)));
		assertEquals("110",d1.get(n(110)));
		assertEquals("120",d1.get(n(120)));
		assertEquals("130",d1.get(n(130)));
	}
	
	public void test247() {
		makeTree();
		assertEquals("80",d1.remove(n(80)));
		assertEquals("10",d1.get(n(10)));
		assertEquals("20",d1.get(n(20)));
		assertEquals("30",d1.get(n(30)));
		assertEquals("40",d1.get(n(40)));
		assertEquals("50",d1.get(n(50)));
		assertEquals("60",d1.get(n(60)));
		assertEquals("70",d1.get(n(70)));
		assertEquals(null,d1.get(n(80)));
		assertEquals("90",d1.get(n(90)));
		assertEquals("100",d1.get(n(100)));
		assertEquals("110",d1.get(n(110)));
		assertEquals("120",d1.get(n(120)));
		assertEquals("130",d1.get(n(130)));
	}
	
	public void test248() {
		makeTree();
		assertEquals("90",d1.remove(n(90)));
		assertEquals("10",d1.get(n(10)));
		assertEquals("20",d1.get(n(20)));
		assertEquals("30",d1.get(n(30)));
		assertEquals("40",d1.get(n(40)));
		assertEquals("50",d1.get(n(50)));
		assertEquals("60",d1.get(n(60)));
		assertEquals("70",d1.get(n(70)));
		assertEquals("80",d1.get(n(80)));
		assertEquals(null,d1.get(n(90)));
		assertEquals("100",d1.get(n(100)));
		assertEquals("110",d1.get(n(110)));
		assertEquals("120",d1.get(n(120)));
		assertEquals("130",d1.get(n(130)));
	}
	
	public void test249() {
		makeTree();
		assertEquals("100",d1.remove(n(100)));
		assertEquals("10",d1.get(n(10)));
		assertEquals("20",d1.get(n(20)));
		assertEquals("30",d1.get(n(30)));
		assertEquals("40",d1.get(n(40)));
		assertEquals("50",d1.get(n(50)));
		assertEquals("60",d1.get(n(60)));
		assertEquals("70",d1.get(n(70)));
		assertEquals("80",d1.get(n(80)));
		assertEquals("90",d1.get(n(90)));
		assertEquals(null,d1.get(n(100)));
		assertEquals("110",d1.get(n(110)));
		assertEquals("120",d1.get(n(120)));
		assertEquals("130",d1.get(n(130)));
	}
	
	public void test250() {
		d1.clear();
		assertEquals(0,d1.size());
	}
	
	public void test251() {
		d1.put(n("apples"),4);
		d1.clear();
		assertEquals(0,d1.size());
	}
	
	public void test252() {
		d1.put(n("bread"),1);
		d1.clear();
		assertEquals(null,d1.get(n("bread")));
	}

	public void test253() {
		d1.put(n("carrots"),8);
		d1.put(n("donuts"),0);
		d1.clear();
		assertEquals(0,d1.size());
	}
	
	public void test254() {
		d1.put(n("eggs"),12);
		d1.put(n("fish"),1);
		d1.put(n("grapes"),100);
		d1.clear();
		assertEquals(null,d1.get(n("fish")));
	}
	
	public void test255() {
		d1.put(n("hummus"),"8 oz.");
		d1.put(n("ice cream"),"1 qt.");
		d1.put(n("juice"),"0.5 gal");
		d1.put(n("kale"),"1 bunch");
		d1.clear();
		assertEquals(0,d1.size());
	}
	
	public void test256() {
		d1.put(n("oranges"), 6);
		d1.put(n("milk"),"1 gal.");
		d1.put(n("lemons"), 5);
		d1.put(n("nuts"),"12 oz.");
		d1.put(n("parsley"),"1 bunch");
		d1.put(n("quinoa"),"1 lb.");
		d1.clear();
		assertEquals(null,d1.get(n("oranges")));
	}
	
	public void test257() {
		makeTree();
		d1.clear();
		for (int i=1; i <= 13; ++i) {
			assertNull(d1.get(n(i*10)));
		}
	}
	
	public void test258() {
		makeTree();
		d1.clear();
		makeTree();
		for (int i=1; i <= 13; ++i) {
			assertEquals(""+(i*10),d1.get(n(i*10)));
		}
	}
	
	public void test259() {
		it = d1.entrySet().iterator();
		d1.clear(); // nothing to clear: so, shouldn't disturb iterator
		assertFalse(it.hasNext());
	}
	
	public void test260() {
		d1.entrySet().clear();
		assertEquals(0,d1.size());
	}
	
	public void test261() {
		d1.put(n("WI"),"Madison");
		d1.entrySet().clear();
		assertEquals(0,d1.size());
	}
	
	public void test262() {
		Set<Entry<Name,Object>> s = d1.entrySet();
		d1.put(n("IL"),"Springfield");
		s.clear();
		assertEquals(0,d1.size());
	}
	
	public void test263() {
		d1.put(n("CA"),"Sacramento");
		d1.put(n("NV"),"Carson City");
		d1.entrySet().clear();
		assertEquals(0,d1.size());
	}
	
	public void test264() {
		d1.put(n("MT"),"Helena");
		d1.put(n("ND"),"Bismarck");
		d1.put(n("MN"),"Minneapolis");
		d1.entrySet().clear();
		assertEquals(0,d1.size());
	}
	
	public void test265() {
		d1.put(n("ME"),"Augusta");
		d1.put(n("VT"),"Montpelier");
		d1.put(n("NH"),"Concord");
		d1.put(n("MA"),"Boston");
		d1.put(n("RI"),"Providence");
		d1.put(n("CT"),"Hartford");
		d1.entrySet().clear();
		assertEquals(0,d1.size());
		assertEquals(null,d1.get(n("Augusta")));
	}
	
	public void test266() {
		makeTree();
		d1.entrySet().clear();
		assertEquals(0,d1.size());
	}
	
	public void test269() {
		it = d1.entrySet().iterator();
		d1.entrySet().clear(); // nothing to remove
		assertFalse(it.hasNext());
	}
	
	/*
	 * test3XX: tests of remove using iterators
	 * (Errors starting 350)
	 */
	
	public void test300() {
		d1.put(n(300),300);
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertEquals(0,d1.size());
	}
	
	public void test301() {
		d1.put(n(301),301);
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertFalse(it.hasNext());
	}
	
	public void test302() {
		d1.put(n(302),302);
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertNull(d1.get(n(302)));
	}
	
	public void test303() {
		d1.put(n(303),303);
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		it = d1.entrySet().iterator();
		assertFalse(it.hasNext());
	}
	
	public void test310() {
		d1.put(n(310),310);
		d1.put(n(31),31);
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertEquals(1,d1.size());
	}
	
	public void test311() {
		d1.put(n(311),311);
		d1.put(n(31),31);
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertTrue(it.hasNext());
	}
	
	public void test312() {
		d1.put(n(312),312);
		d1.put(n(31),31);
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertEquals(312,d1.get(n(312)));
	}
	
	public void test313() {
		d1.put(n(313),313);
		d1.put(n(31),31);
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertEquals(e(n(313),313),it.next());
	}

	public void test314() {
		d1.put(n(314),314);
		d1.put(n(31),31);
		it = d1.entrySet().iterator();
		it.next();
		it.next();
		it.remove();
		assertEquals(1,d1.size());
	}

	public void test315() {
		d1.put(n(315),315);
		d1.put(n(31),31);
		it = d1.entrySet().iterator();
		it.next();
		it.next();
		it.remove();
		assertFalse(it.hasNext());
	}

	public void test316() {
		d1.put(n(316),316);
		d1.put(n(31),31);
		it = d1.entrySet().iterator();
		it.next();
		it.next();
		it.remove();
		assertEquals(31,d1.get(n(31)));
	}
	
	public void test317() {
		d1.put(n(31),31);
		d1.put(n(317),317);
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertEquals(1,d1.size());
	}
	
	public void test318() {
		d1.put(n(31),31);
		d1.put(n(318),318);
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertTrue(it.hasNext());
		assertEquals(e(n(318),318),it.next());
		assertEquals(318,d1.get(n(318)));
	}

	public void test319() {
		d1.put(n(31),31);
		d1.put(n(319),319);
		it = d1.entrySet().iterator();
		it.next();
		it.next();
		it.remove();
		assertFalse(it.hasNext());
		assertEquals(1,d1.size());
		assertEquals(31,d1.get(n(31)));
	}
	
	public void test320() {
		d1.put(n(203), 203);
		d1.put(n(320), 320);
		d1.put(n(32),32);
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertEquals(2,d1.size());
		assertTrue(it.hasNext());
		assertEquals(e(n(203),203),it.next());
		assertEquals(e(n(320),320),it.next());
	}
	
	public void test321() {
		d1.put(n(213),213);
		d1.put(n(321),321);
		d1.put(n(32),32);
		it = d1.entrySet().iterator();
		it.next();
		it.next();
		it.remove();
		assertEquals(2,d1.size());
		assertTrue(it.hasNext());
		assertEquals(e(n(321),321),it.next());
		assertEquals(32,d1.get(n(32)));
	}
	
	public void test322() {
		d1.put(n(213),213);
		d1.put(n(321),321);
		d1.put(n(32),32);
		it = d1.entrySet().iterator();
		it.next();
		it.next();
		it.next();
		it.remove();
		assertEquals(2,d1.size());
		assertFalse(it.hasNext());
		assertEquals(32,d1.get(n(32)));
		assertEquals(213,d1.get(n(213)));
	}
	
	public void test323() {
		d1.put(n(323),323);
		d1.put(n(23),23);
		d1.put(n(233),233);
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertEquals(2,d1.size());
		assertTrue(it.hasNext());
		assertEquals(e(n(233),233),it.next());
		assertEquals(e(n(323),323),it.next());
	}
	
	public void test324() {
		d1.put(n(324),324);
		d1.put(n(32),32);
		d1.put(n(243),243);
		it = d1.entrySet().iterator();
		it.next();
		it.next();
		it.remove();
		assertEquals(2,d1.size());
		assertTrue(it.hasNext());
		assertEquals(e(n(324),324),it.next());
		assertFalse(it.hasNext());
		assertEquals(32,d1.get(n(32)));
	}
	
	public void test325() {
		d1.put(n(325),325);
		d1.put(n(32),32);
		d1.put(n(253),253);
		it = d1.entrySet().iterator();
		it.next();
		it.next();
		it.next();
		it.remove();
		assertEquals(2,d1.size());
		assertFalse(it.hasNext());
		assertEquals(32,d1.get(n(32)));
		assertEquals(253,d1.get(n(253)));
	}
		
	public void test326() {
		d1.put(n(32),32);
		d1.put(n(263),263);
		d1.put(n(326),326);
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertEquals(2,d1.size());
		assertTrue(it.hasNext());
		assertEquals(e(n(263),263),it.next());
		assertEquals(e(n(326),326),it.next());
	}

	public void test327() {
		d1.put(n(32),32);
		d1.put(n(273),273);
		d1.put(n(327),327);
		it = d1.entrySet().iterator();
		it.next();
		it.next();
		it.remove();
		assertEquals(2,d1.size());
		assertTrue(it.hasNext());
		assertEquals(e(n(327),327),it.next());
		assertEquals(32,d1.get(n(32)));
	}
	
	public void test328() {
		d1.put(n(32),32);
		d1.put(n(283),283);
		d1.put(n(328),328);
		it = d1.entrySet().iterator();
		it.next();
		it.next();
		it.next();
		it.remove();
		assertEquals(2,d1.size());
		assertFalse(it.hasNext());
		assertEquals(32,d1.get(n(32)));
		assertEquals(283,d1.get(n(283)));
	}
		
	public void test329() {
		d1.put(n(329),329);
		d1.put(n(293),293);
		d1.put(n(32),32);
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertEquals(2,d1.size());
		assertEquals(e(n(293),293),it.next());
		assertEquals(e(n(329),329),it.next());
		it.remove();
		assertEquals(1,d1.size());
		assertEquals(293,d1.get(n(293)));
		assertFalse(it.hasNext());
	}
	
	
	public void test330() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.remove();
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 40) assertNull(d1.get(n(40)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test331() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next();
		it.remove();
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 50) assertNull(d1.get(n(50)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test332() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next();
		it.remove();
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 60) assertNull(d1.get(n(60)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test333() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next(); it.next();
		it.remove();
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 70) assertNull(d1.get(n(70)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test334() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next(); it.next(); it.next();
		it.remove();
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 80) assertNull(d1.get(n(80)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test335() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next(); it.next(); it.next();
		it.next();
		it.remove();
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 90) assertNull(d1.get(n(90)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}

	public void test336() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next();
		it.remove();
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 100) assertNull(d1.get(n(100)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test337() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next(); it.next();
		it.remove();
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 110) assertNull(d1.get(n(110)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test338() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next(); it.next(); it.next();
		it.remove();
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 120) assertNull(d1.get(n(120)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test339() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next(); it.next(); it.next(); 
		it.next();
		it.remove();
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 130) assertNull(d1.get(n(130)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test340() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); 
		it.remove(); it.next(); it.next(); it.next(); it.remove();
		assertEquals(11,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 10 || i == 40) assertNull("d1.get(n("+i+"))",d1.get(n(i)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test341() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next();
		it.remove(); it.next(); it.next(); it.next(); it.remove();
		assertEquals(11,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 20 || i == 50) assertNull("d1.get(n("+i+"))",d1.get(n(i)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test342() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next();
		it.remove(); it.next(); it.next(); it.next(); it.remove();
		assertEquals(11,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 30 || i == 60) assertNull("d1.get(n("+i+"))",d1.get(n(i)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test343() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.remove(); it.next(); it.next(); it.next(); it.remove();
		assertEquals(11,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 40 || i == 70) assertNull("d1.get(n("+i+"))",d1.get(n(i)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test344() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next();
		it.remove(); it.next(); it.next(); it.next(); it.remove();
		assertEquals(11,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 50 || i == 80) assertNull("d1.get(n("+i+"))",d1.get(n(i)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test345() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next();
		it.remove(); it.next(); it.next(); it.next(); it.remove();
		assertEquals(11,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 60 || i == 90) assertNull("d1.get(n("+i+"))",d1.get(n(i)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test346() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next(); it.next();
		it.remove(); it.next(); it.next(); it.next(); it.remove();
		assertEquals(11,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 70 || i == 100) assertNull("d1.get(n("+i+"))",d1.get(n(i)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test347() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next(); it.next(); it.next();
		it.remove(); it.next(); it.next(); it.next(); it.remove();
		assertEquals(11,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 80 || i == 110) assertNull("d1.get(n("+i+"))",d1.get(n(i)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test348() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next(); it.next(); it.next();
		it.next();
		it.remove(); it.next(); it.next(); it.next(); it.remove();
		assertEquals(11,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 90 || i == 120) assertNull("d1.get(n("+i+"))",d1.get(n(i)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test349() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next(); it.next(); it.next();
		it.next(); it.next();
		it.remove(); it.next(); it.next(); it.next(); it.remove();
		assertEquals(11,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 100 || i == 130) assertNull("d1.get(n("+i+"))",d1.get(n(i)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test350() {
		it = d1.entrySet().iterator();
		assertException(IllegalStateException.class,() -> it.remove());
	}
	
	public void test351() {
		d1.put(n("351"),"Data Structures & Algorithms");
		it = d1.entrySet().iterator();
		assertException(IllegalStateException.class,() -> it.remove());
	}
	
	public void test352() {
		d1.put(n("352"),"Algorithms & Data Structures");
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertException(IllegalStateException.class,() -> it.remove());
	}
	
	public void test353() {
		d1.put(n("353"),"Algorithms for Data Structures");
		d1.put(n("153"),"Ruby on Rails");
		it = d1.entrySet().iterator();
		it.next();
		it.remove();
		assertException(IllegalStateException.class,() -> it.remove());
	}
	
	public void test360() {
		d1.put(n(360),"360");
		it = d1.entrySet().iterator();
		d1.remove(n(361));
		assertTrue(it.hasNext());
	}
	
	public void test361() {
		d1.put(n(361),"361");
		it = d1.entrySet().iterator();
		d1.remove(n(361));
		assertException(ConcurrentModificationException.class, () -> it.hasNext());
	}
	
	public void test362() {
		d1.put(n(362),"362");
		it = d1.entrySet().iterator();
		d1.remove(n(362));
		assertException(ConcurrentModificationException.class, () -> it.next());
	}
	
	public void test363() {
		d1.put(n(363),"363");
		it = d1.entrySet().iterator();
		it.next();
		d1.remove(n(363));
		assertException(ConcurrentModificationException.class, () -> it.hasNext());
	}
	
	public void test364() {
		d1.put(n(364),"364");
		it = d1.entrySet().iterator();
		it.next();
		d1.remove(n(364));
		assertException(ConcurrentModificationException.class, () -> it.remove());
	}
	
	public void test370() {
		d1.put(n(370),"370");
		it = d1.entrySet().iterator();
		it2 = d1.entrySet().iterator();
		assertTrue(it.hasNext());
		assertTrue(it2.hasNext());
		it2.next();
		assertTrue(it.hasNext());
		assertFalse(it2.hasNext());
		it2.remove();
		assertException(ConcurrentModificationException.class, () -> it.hasNext());
		assertException(ConcurrentModificationException.class, () -> it.next());
	}
	
	public void test375() {
		makeTree();
		it = d1.entrySet().iterator();
		it2 = d1.entrySet().iterator();
		it.next(); it.next();
		it2.next(); it2.next(); it2.next();
		it2.remove();
		assertException(ConcurrentModificationException.class, () -> it.hasNext());
		assertException(ConcurrentModificationException.class, () -> it.next());
		assertException(ConcurrentModificationException.class, () -> it.remove());
	}
	
	public void test380() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next();
		d1.clear();
		assertException(ConcurrentModificationException.class, () -> it.hasNext());
		assertException(ConcurrentModificationException.class, () -> it.next());
		assertException(ConcurrentModificationException.class, () -> it.remove());
	}
	
	public void test382() {
		makeTree();
		it = d1.entrySet().iterator();
		((Dictionary)d1).copy((Dictionary)d1); // should have no effect
		assertTrue(it.hasNext());
	}
	
	public void test383() {
		makeTree();
		it = d1.entrySet().iterator();
		Dictionary d2 = new Dictionary();
		d2.put(n(30), "30");
		((Dictionary)d1).copy(d2); // should have no effect
		assertTrue(it.hasNext());
	}
	
	public void test390() {
		makeTree();
		it = d1.entrySet().iterator();
		it.next(); it.next(); it.next(); it.next();
		d1.entrySet().clear();
		assertException(ConcurrentModificationException.class, () -> it.hasNext());
		assertException(ConcurrentModificationException.class, () -> it.next());
		assertException(ConcurrentModificationException.class, () -> it.remove());
	}
	
	public void test399() {
		makeTree();
		it = d1.entrySet().iterator();
		while (it.hasNext()) {
			it.next();
			it.remove();
		}
		assertEquals(0,d1.size());
	}
	
	/*
	 * test4XX testing remove on Entry Set
	 */
	
	public void test400() {
		assertFalse(d1.entrySet().remove(null));
	}
	
	public void test401() {
		assertFalse(d1.entrySet().remove(n(401)));
	}
	
	public void test402() {
		assertFalse(d1.entrySet().remove(e("/402","402")));
	}
	
	public void test403() {
		assertFalse(d1.entrySet().remove(e(n(403),403)));
	}
	
	public void test404() {
		it = d1.entrySet().iterator();
		d1.entrySet().remove(e(n(404),"404"));
		assertFalse(it.hasNext());
	}
	
	public void test410() {
		d1.put(n(410),410);
		assertFalse(d1.entrySet().remove(null));
		assertEquals(1,d1.size());
	}
	
	public void test411() {
		d1.put(n(411),411);
		assertFalse(d1.entrySet().remove("411"));
	}
	
	public void test412() {
		d1.put(n(412), 412);
		assertFalse(d1.entrySet().remove(n(412)));
		assertEquals(1,d1.size());
	}
	
	public void test413() {
		d1.put(n(413), 413);
		assertFalse(d1.entrySet().remove(e("/413",413)));
		assertEquals(1,d1.size());
	}
	
	public void test414() {
		d1.put(n(414), 414);
		assertFalse(d1.entrySet().remove(e(n(414),"414")));
		assertEquals(1,d1.size());
	}
	
	public void test415() {
		d1.put(n(415), 415);
		assertFalse(d1.entrySet().remove(e(n(415),null)));
		assertEquals(1,d1.size());
	}
	
	public void test416() {
		d1.put(n(416), null);
		assertFalse(d1.entrySet().remove(e(n(416),416)));
		assertEquals(1,d1.size());
	}
	
	public void test417() {
		d1.put(n(417), 417);
		assertTrue(d1.entrySet().remove(e(n(417),Integer.valueOf(417))));
		assertEquals(0,d1.size());
	}
	
	public void test418() {
		d1.put(n(418),418);
		it = d1.entrySet().iterator();
		assertFalse(d1.entrySet().remove(e(n(418),"418")));
		assertEquals(e(n(418),418),it.next());
	}

	public void test419() {
		d1.put(n(419),"419");
		assertTrue(d1.entrySet().remove(e(n(419),new String("419"))));
		assertEquals(0,d1.size());
	}
	
	public void test420() {
		d1.put(n(420), 420);
		d1.put(n(42),42);
		assertFalse(d1.entrySet().remove(e(n(42),"42")));
		assertTrue(d1.entrySet().remove(e(n(420),420)));
		assertEquals(1,d1.size());
		assertEquals(42,d1.get(n(42)));
	}
	
	public void test421() {
		d1.put(n(421), "421");
		d1.put(n(42), "42");
		assertFalse(d1.entrySet().remove(e(n(421),421)));
		assertTrue(d1.entrySet().remove(e(n(42),"42")));
		assertEquals(1,d1.size());
		assertEquals("421",d1.get(n(421)));
	}
	
	public void test422() {
		d1.put(n(42), 42);
		d1.put(n(422), "422");
		assertFalse(d1.entrySet().remove(e(n(42),"42")));
		assertTrue(d1.entrySet().remove(e(n(422),"422")));
		assertEquals(1,d1.size());
		assertEquals(42,d1.get(n(42)));
	}
	
	public void test423() {
		d1.put(n(42),"42");
		d1.put(n(423), 423);
		assertFalse(d1.entrySet().remove(e(n(42),42)));
		assertTrue(d1.entrySet().remove(e(n(423),423)));
		assertEquals(1,d1.entrySet().size());
		assertEquals("42",d1.get(n(42)));
	}
	
	public void test424() {
		Set<Entry<Name,Object>> s = d1.entrySet();
		d1.put(n(424),424);
		d1.put(n(42),42);
		assertTrue(s.remove(e(n(424),424)));
		assertEquals(1,d1.size());
	}
	
	public void test430() {
		makeTree();
		d1.entrySet().remove(e(n(10),"10"));
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 10) assertNull(d1.get(n(10)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test431() {
		makeTree();
		d1.entrySet().remove(e(n(20),"20"));
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 20) assertNull(d1.get(n(20)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test432() {
		makeTree();
		d1.entrySet().remove(e(n(30),"30"));
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 30) assertNull(d1.get(n(30)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test433() {
		makeTree();
		d1.entrySet().remove(e(n(40),"40"));
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 40) assertNull(d1.get(n(40)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test434() {
		makeTree();
		d1.entrySet().remove(e(n(50),"50"));
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 50) assertNull(d1.get(n(50)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test435() {
		makeTree();
		d1.entrySet().remove(e(n(60),"60"));
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 60) assertNull(d1.get(n(60)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test436() {
		makeTree();
		d1.entrySet().remove(e(n(70),"70"));
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 70) assertNull(d1.get(n(70)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test437() {
		makeTree();
		d1.entrySet().remove(e(n(80),"80"));
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 80) assertNull(d1.get(n(80)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test438() {
		makeTree();
		d1.entrySet().remove(e(n(90),"90"));
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 90) assertNull(d1.get(n(90)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test439() {
		makeTree();
		d1.entrySet().remove(e(n(100),"100"));
		assertEquals(12,d1.size());
		for (int i=10; i <=130; i += 10) {
			if (i == 100) assertNull(d1.get(n(100)));
			else assertEquals("d1.get(n("+i+"))",""+i,d1.get(n(i)));
		}
	}
	
	public void test440() {
		makeTree();
		Set<Entry<Name,Object>> s = d1.entrySet();
		for (int i=130; i > 0; i -= 10) {
			s.remove(e(n(i),""+i));
		}
		assertEquals(0,d1.size());
	}
	
	public void test445() {
		makeTree();
		for (Entry<Name,Object> e : d1.entrySet()) {
			e.setValue(e.getKey());
		}
		for (int i=10; i <=130; i+= 10) {
			assertEquals("d1.get(n("+i+"))",n(i),d1.get(n(i)));
		}
	}
	
	public void test460() {
		d1.put(n(460), 460);
		assertException(UnsupportedOperationException.class,() -> d1.entrySet().add(e(n(460), "460")));
	}
	
	public void test465() {
		d1.put(n(365),"365");
		it = d1.entrySet().iterator();
		assertFalse(d1.entrySet().remove(e(n(365),365)));
		assertTrue(it.hasNext());
	}
	
	public void test466() {
		d1.put(n(366),"366");
		it = d1.entrySet().iterator();
		assertTrue(d1.entrySet().remove(e(n(366),"366")));
		assertException(ConcurrentModificationException.class, () -> it.hasNext());
	}
	
	public void test467() {
		d1.put(n(367),"367");
		it = d1.entrySet().iterator();
		assertTrue(d1.entrySet().remove(e(n(367),"367")));
		assertException(ConcurrentModificationException.class, () -> it.next());
	}
	
	public void test468() {
		d1.put(n(368),"368");
		it = d1.entrySet().iterator();
		it.next();
		assertTrue(d1.entrySet().remove(e(n(368),"368")));
		assertException(ConcurrentModificationException.class, () -> it.hasNext());
	}
	
	public void test469() {
		d1.put(n(369),"369");
		it = d1.entrySet().iterator();
		it.next();
		assertTrue(d1.entrySet().remove(e(n(369),"369")));
		assertException(ConcurrentModificationException.class, () -> it.remove());
	}
	
	
	
	/** 
	 * test50x: Tests for remove subsets
	 */
	
	public void test500() {
		makeTree();
		for (Iterator<Name> it = d1.keySet().iterator(); it.hasNext();) {
			it.next();
			it.remove();
		}
		
		assertEquals(0,d1.size());
		for (int i=0; i <= 140; ++i)
			assertNull(d1.get(i));
	}
	
	public void test501() {
		makeTree();
		for (Iterator<Name> it = d1.keySet().iterator(); it.hasNext();)
			if (((Integer.parseInt(it.next().rep,10) / 10) & 1) != 0)
				it.remove();

		assertEquals(6,d1.size());
		for (int i=10; i <= 130; i += 10)
			if (((i/10) & 1) == 0)
				assertEquals(""+i,d1.get(n(i)));	
			else
				assertNull("d1.get(n(" + i + ")) should have been removed (odd)",d1.get(n(i)));
	}
	
	public void test502() {
		makeTree();
		for (Iterator<Name> it = d1.keySet().iterator(); it.hasNext();)
			if (((Integer.parseInt(it.next().rep,10) / 10) & 1) == 0)
				it.remove();

		assertEquals(7,d1.size());
		for (int i=10; i <= 130; i += 10)
			if (((i/10) & 1) != 0)
				assertEquals(""+i,d1.get(n(i)));	
			else
				assertNull("d1.get(n(" + i + ")) should have been removed (even)",d1.get(n(i)));
	}

	public void test503() {
		makeTree();
		for (Iterator<Name> it = d1.keySet().iterator(); it.hasNext();)
			if (Integer.parseInt(it.next().rep,10) < 70)
				it.remove();

		assertEquals(7,d1.size());
		for (int i=10; i <= 130; i += 10)
			if (i > 60)
				assertEquals(""+i,d1.get(n(i)));	
			else
				assertNull("d1.get(n(" + i + ")) should have been removed (small)",d1.get(n(i)));
	}

	public void test504() {
		makeTree();
		for (Iterator<Name> it = d1.keySet().iterator(); it.hasNext();)
			if (Integer.parseInt(it.next().rep,10) >= 70)
				it.remove();

		assertEquals(6,d1.size());
		for (int i=10; i <= 130; i += 10)
			if (i <= 60)
				assertEquals(""+i,d1.get(n(i)));	
			else
				assertNull("d1.get(n(" + i + ")) should have been removed (small)",d1.get(n(i)));
	}
	
	public void test550() {
		makeTree();
		assertNull(d1.get(null));
		assertNull(d1.get("hello"));
		assertNull(d1.get(n("hello")));
		assertNull(d1.remove(null));
		assertNull(d1.remove("hello"));
		assertNull(d1.remove(n("hello")));
		assertFalse(d1.containsKey(null));
		assertFalse(d1.containsKey("hello"));
		assertFalse(d1.containsKey(n("hello")));
		assertFalse(d1.entrySet().contains(null));
		assertFalse(d1.entrySet().contains("hello"));
		assertFalse(d1.entrySet().contains(n("hello")));
		assertFalse(d1.entrySet().contains(e("100",10)));
		assertFalse(d1.entrySet().remove(null));
		assertFalse(d1.entrySet().remove("hello"));
		assertFalse(d1.entrySet().remove(n("hello")));
		assertFalse(d1.entrySet().remove(e("100",10)));
	}
	
	private <T> void testCollection(Collection<T> l, String name, @SuppressWarnings("unchecked") T... parts)
	{
		assertEquals(name + ".size()",parts.length,l.size());
		Iterator<T> it = l.iterator();
		int i=0;
		while (it.hasNext() && i < parts.length) {
			assertEquals(name + "[" + i + "]",parts[i],it.next());
			++i;
		}
		
		assertFalse(name + " claims too long (iterator claims more elements)",it.hasNext());
		try {
			it.next();
			assertFalse("iterator should have thrown exception, there is no next", true);
		} catch(Exception e){
			assertTrue(e instanceof NoSuchElementException);
		}
		
		assertFalse(name + " claims too short (iterator stops too soon, after " + i + " elements)",(i < parts.length));
	}
}