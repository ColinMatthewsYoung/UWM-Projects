import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.ps.Dictionary;
import edu.uwm.cs351.ps.ExecutionException;
import edu.uwm.cs351.ps.Name;

public class TestDictionary extends LockedTestCase {
	
	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (RuntimeException ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}

	private Dictionary dict;
	
	@Override
	protected void setUp() {
		try {
			assert dict.size() > 0 : "something terribly wrong";
			assertFalse("Need to add '-ea' to VM Arguments on Arguments Pane of Run Configuration",true);
		} catch (NullPointerException ex) {
			assertTrue("assertions set correctly",true);
		}
		dict = new Dictionary();
	}
	
	protected Name n(String s) {
		return new Name(s);
	}
	
	protected String eval(Supplier<?> r) {
		try {
			return ""+r.get();
		} catch (RuntimeException e) {
			return e.getClass().getSimpleName();
		}
	}
	
	public void test() {
		// Locked tests
		// Before we put in anything. Look at homework:
		assertEquals(Ts(1819171226), dict.toString());
		dict.put(n("Oranges"),3);
		// Remember the spaces and the /:
		assertEquals(Ts(1805540545), dict.toString());
		dict.put(n("Bread"),"whole-wheat");
		dict.put(n("Oranges"),10);
		dict.put(n("Candy"),null);
		assertEquals(Ti(926699717),dict.size());
		assertEquals(true,dict.known(n("Bread")));
		assertEquals(false,dict.known(n("whole-wheat")));
		// indicate the result
		// Use null for null
		// Use name of exception if it throws an exception
		// Otherwise, give the string of the result
		assertEquals(Ts(1005485529), eval( () -> dict.known(n("Candy"))));
		assertEquals(Ts(1956427066), eval( () -> dict.known(null)));
		assertEquals(Ts(215058030),eval( () -> dict.get(n("Oranges"))));
		assertEquals(Ts(773719046),eval( () -> dict.get(n("Bread"))));
		assertEquals(Ts(1030502759), eval( () -> dict.get(n("Candy"))));
		assertEquals(Ts(1489337995),eval( () -> dict.get(null)));
		assertEquals(Ts(1799300530),eval( () -> dict.get(n("whole-wheat"))));
		testcont(dict);
	}
	
	protected void testcont(Dictionary dict) {
		// Dictionary has
		//   /Bread -> whole-wheat
		//   /Candy -> null
		//   /Oranges -> 10
		Collection<Object> c = dict.values();
		Iterator<Object> it = c.iterator();
		assertEquals(Ts(1787805698),""+it.next());
		assertEquals(Ts(2099020083),""+it.next());
		assertEquals(Ts(107913310),""+it.next());
		assertEquals(Ts(572642528),dict.toString());
		testcont2(dict);
	}
	
	protected void testcont2(Dictionary dict) {
		// dict has
		//   /Bread -> whole-wheat
		//   /Candy -> null
		//   /Oranges -> 10
		Dictionary dict2 = new Dictionary();
		dict2.put(n("Oranges"),5);
		dict2.put(n("Milk"),"gallon");
		dict2.put(n("Candy"), false);
		dict.copy(dict2);
		assertEquals(Ts(850308410),""+dict.get(n("Oranges")));
		assertEquals(Ts(1526600001),dict.get(n("Bread")));
		assertEquals(Ts(1716696247),dict.get(n("Milk")));
		// indicate the result
		// Use null for null
		// Use name of exception if it throws an exception
		// Otherwise, give the string of the result
		// NB: This is for dict2
		assertEquals(Ts(1309057704),eval( () -> dict2.get(n("Candy"))));
		assertEquals(Ts(1681589321),eval( () -> dict2.get(n(("Bread")))));
	}
	
	public void test00() {
		assertEquals(0,dict.size());
	}
	
	public void test01() {
		assertException(ExecutionException.class,() -> { dict.get(n("foo")); });
	}
	
	public void test02() {
		assertFalse(dict.known(n("bar")));
	}
	
	public void test03() {
		assertFalse(dict.known(null));
	}
	
	public void test04() {
		assertException(ExecutionException.class,() -> dict.get(null));
	}
	
	public void test05() {
		assertException(ExecutionException.class, () -> { dict.put(null,"hello"); });
	}
	
	public void test10() {
		dict.put(n("hello"),8);
		assertEquals(1,dict.size());
	}
	
	public void test11() {
		dict.put(n("bye"),9);
		assertEquals(Integer.valueOf(9),dict.get(n("bye")));
		assertException(ExecutionException.class,() -> dict.get(n("hello")));
	}
	
	public void test12() {
		dict.put(n("time"), 10.30);
		assertTrue(dict.known(n("time")));
		assertFalse(dict.known(n("10.30")));
	}
	
	public void test13() {
		dict.put(n("hello"),null);
		assertNull(dict.get(n("hello")));
		dict.put(n("hello"),"bye");
		assertEquals("bye",dict.get(n("hello")));
	}
	
	public void test14() {
		dict.put(n("apples"),15);
		assertFalse(dict.known(n("bacon")));
	}
	
	public void test15() {
		dict.put(n("CS351"), 3);
		assertException(ExecutionException.class,() -> dict.get(n("CS251")));
	}
	
	public void test16() {
		dict.put(n("hello"),null);
		assertNull(dict.get(n("hello")));
		assertTrue(dict.known(n("hello")));
	}
	
	public void test17() {
		dict.put(n("WI"),"Madison");
		assertException(ExecutionException.class,() -> dict.get(n("Wi")));		
	}
	
	public void test18() {
		dict.put(n("VA"),"Richmond");
		assertFalse(dict.known(null));
	}
	
	public void test19() {
		dict.put(n("OK"),"Oklahoma City");
		assertException(ExecutionException.class,() -> dict.get(null));
	}
	
	public void test20() {
		dict.put(n("WI"),"Madison");
		dict.put(n("IL"),"Springfield");
		assertEquals(2,dict.size());
		assertEquals("Madison",dict.get(n("WI")));
		assertEquals("Springfield",dict.get(n("IL")));
	}
	
	public void test21() {
		dict.put(n("CA"),"Sacramento");
		dict.put(n("NV"),"Carson City");
		assertEquals("Carson City",dict.get(n("NV")));
	}
	
	public void test22() {
		dict.put(n("IL"),"Springfield");
		dict.put(n("WI"), "Madison");
		assertTrue(dict.known(n("WI")));
		assertFalse(dict.known(n("CA")));
	}
	
	public void test23() {
		dict.put(n("TX"),"Austin");
		dict.put(n("FL"),"Tallahassee");
		assertException(ExecutionException.class, () -> dict.get(n("WI")));
	}
	
	public void test24() {
		dict.put(n("MN"),"Saint Paul");
		dict.put(n("MT"),"Helena");
		assertException(ExecutionException.class, () -> dict.get(n("CA")));
		assertException(ExecutionException.class, () -> dict.get(n("MO")));
	}
	
	public void test25() {
		dict.put(n("ND"),"Bismarck");
		dict.put(n("SD"),"Pierre");
		assertEquals("Pierre",dict.get(n("SD")));
		dict.put(n("SD"),"Sioux Falls");
		assertEquals("Sioux Falls",dict.get(n("SD")));
	}
	
	public void test26() {
		dict.put(n("MI"),"Lansing");
		dict.put(n("OH"),"Dayton");
		dict.put(n("OH"),"Columbus");
		assertEquals(2,dict.size());
	}
	
	public void test27() {
		dict.put(n("IN"),"Indianapolis");
		dict.put(n("OH"),"Columbus");
		dict.put(n("IN"),"Gary");
		assertEquals("Gary",dict.get(n("IN")));
		assertTrue(dict.known(n("OH")));
		assertEquals(2,dict.size());
	}
	
	public void test28() {
		dict.put(n("IL"),"Springfield");
		dict.put(n("WI"),"Madison");
		dict.put(n("IL"),"Chicago");
		dict.put(n("WI"),"Milwaukee");
		dict.put(n("WI"),"Madison");
		dict.put(n("IL"),"Springfield");
		assertEquals(2,dict.size());
		assertEquals("Madison",dict.get(n("WI")));
	}
	
	public void test29() {
		dict.put(n("NM"),"Santa Fe");
		dict.put(n("NC"),"Raleigh");
		assertException(ExecutionException.class,() -> { dict.put(null,"London"); });
	}
	
	public void test30() {
		dict.put(n("AR"),"Little Rock");
		dict.put(n("CO"),"Denver");
		dict.put(n("PA"),"Harrisburg");
		assertEquals(3,dict.size());
		
		assertTrue(dict.known(n("PA")));
		assertTrue(dict.known(n("AR")));
		assertTrue(dict.known(n("CO")));
		assertFalse(dict.known(n("AK")));
		assertFalse(dict.known(n("AZ")));
		assertFalse(dict.known(n("MS")));
		assertFalse(dict.known(n("TX")));
		
		assertEquals("Harrisburg",dict.get(n("PA")));
		assertEquals("Denver",dict.get(n("CO")));
		assertEquals("Little Rock",dict.get(n("AR")));
		assertException(ExecutionException.class, () -> dict.get(n("AL")));
		assertException(ExecutionException.class, () -> dict.get(n("CA")));
		assertException(ExecutionException.class, () -> dict.get(n("MI")));
		assertException(ExecutionException.class, () -> dict.get(n("WI")));
		
		dict.put(n("AR"),null);
		dict.put(n("CO"),"Boulder");
		dict.put(n("PA"),"Pittsburgh");
		assertEquals(3,dict.size());
		assertEquals("Pittsburgh",dict.get(n("PA")));
		assertEquals("Boulder",dict.get(n("CO")));
		assertNull(dict.get(n("AR")));
	}
	
	public void test31() {
		dict.put(n("GA"),"Atlanta");
		dict.put(n("AZ"),"Phoenix");
		dict.put(n("VT"),"Montpelier");
		assertEquals(3,dict.size());
		
		assertTrue(dict.known(n("AZ")));
		assertTrue(dict.known(n("GA")));
		assertTrue(dict.known(n("VT")));
		assertFalse(dict.known(n("AL")));
		assertFalse(dict.known(n("CA")));
		assertFalse(dict.known(n("SD")));
		assertFalse(dict.known(n("WY")));
		
		assertEquals("Phoenix",dict.get(n("AZ")));
		assertEquals("Atlanta",dict.get(n("GA")));
		assertEquals("Montpelier",dict.get(n("VT")));
		assertException(ExecutionException.class, () -> dict.get(n("AL")));
		assertException(ExecutionException.class, () -> dict.get(n("CA")));
		assertException(ExecutionException.class, () -> dict.get(n("SD")));
		assertException(ExecutionException.class, () -> dict.get(n("WY")));
		
		dict.put(n("AZ"),"Tucson");
		dict.put(n("GA"),"Athens");
		dict.put(n("VT"),null);
		assertEquals(3,dict.size());
		assertEquals("Tucson",dict.get(n("AZ")));
		assertEquals("Athens",dict.get(n("GA")));
		assertNull(dict.get(n("VT")));
	}
	
	public void test32() {
		dict.put(n("DE"),"Dover");
		dict.put(n("SD"),"Pierre");
		dict.put(n("AK"),"Juneau");
		assertEquals(3,dict.size());
		
		assertTrue(dict.known(n("AK")));
		assertTrue(dict.known(n("DE")));
		assertTrue(dict.known(n("SD")));
		assertFalse(dict.known(n("AB")));
		assertFalse(dict.known(n("AZ")));
		assertFalse(dict.known(n("MO")));
		assertFalse(dict.known(n("WI")));
		
		assertEquals("Juneau",dict.get(n("AK")));
		assertEquals("Dover",dict.get(n("DE")));
		assertEquals("Pierre",dict.get(n("SD")));
		assertException(ExecutionException.class, () -> dict.get(n("AB")));
		assertException(ExecutionException.class, () -> dict.get(n("AZ")));
		assertException(ExecutionException.class, () -> dict.get(n("MO")));
		assertException(ExecutionException.class, () -> dict.get(n("WI")));
		
		dict.put(n("AK"),"Anchorage");
		dict.put(n("DE"),"Wilmington");
		dict.put(n("SD"),null);
		assertEquals(3,dict.size());
		assertEquals("Anchorage",dict.get(n("AK")));
		assertEquals("Wilmington",dict.get(n("DE")));
		assertNull(dict.get(n("SD")));
	}
	
	public void test33() {
		dict.put(n("WI"),"Madison");
		dict.put(n("MN"),"Saint Paul");
		dict.put(n("IN"),"Indianapolis");
		assertEquals(3,dict.size());
		
		assertTrue(dict.known(n("IN")));
		assertTrue(dict.known(n("MN")));
		assertTrue(dict.known(n("WI")));
		assertFalse(dict.known(n("AK")));
		assertFalse(dict.known(n("CO")));
		assertFalse(dict.known(n("SD")));
		assertFalse(dict.known(n("WY")));
		
		assertEquals("Indianapolis",dict.get(n("IN")));
		assertEquals("Saint Paul",dict.get(n("MN")));
		assertEquals("Madison",dict.get(n("WI")));
		assertException(ExecutionException.class, () -> dict.get(n("AK")));
		assertException(ExecutionException.class, () -> dict.get(n("CO")));
		assertException(ExecutionException.class, () -> dict.get(n("SD")));
		assertException(ExecutionException.class, () -> dict.get(n("WY")));
		
		dict.put(n("IN"),null);
		dict.put(n("MN"),"Minneapolis");
		dict.put(n("WI"),"Milwaukee");
		assertEquals(3,dict.size());
		assertEquals("Minneapolis",dict.get(n("MN")));
		assertEquals("Milwaukee",dict.get(n("WI")));
		assertNull(dict.get(n("IN")));
	}
	
	public void test34() {
		dict.put(n("RI"),"Providence");
		dict.put(n("CA"),"Sacramento");
		dict.put(n("MS"),"Jackson");
		assertEquals(3,dict.size());
		
		assertTrue(dict.known(n("CA")));
		assertTrue(dict.known(n("MS")));
		assertTrue(dict.known(n("RI")));
		assertFalse(dict.known(n("AK")));
		assertFalse(dict.known(n("GA")));
		assertFalse(dict.known(n("MT")));
		assertFalse(dict.known(n("SD")));
		
		assertEquals("Sacramento",dict.get(n("CA")));
		assertEquals("Jackson",dict.get(n("MS")));
		assertEquals("Providence",dict.get(n("RI")));
		assertException(ExecutionException.class, () -> dict.get(n("AK")));
		assertException(ExecutionException.class, () -> dict.get(n("GA")));
		assertException(ExecutionException.class, () -> dict.get(n("MT")));
		assertException(ExecutionException.class, () -> dict.get(n("SD")));
		
		dict.put(n("CA"),"Los Angeles");
		dict.put(n("RI"),"Bristol");
		dict.put(n("MS"),null);
		assertEquals(3,dict.size());
		assertEquals("Los Angeles",dict.get(n("CA")));
		assertEquals("Bristol",dict.get(n("RI")));
		assertNull(dict.get(n("MS")));
	}
	
	public void test35() {
		dict.put(n("CT"),"Hartford");
		dict.put(n("WY"),"Cheyenne");
		dict.put(n("SC"),"Columbia");
		assertEquals(3,dict.size());
		
		assertTrue(dict.known(n("CT")));
		assertTrue(dict.known(n("SC")));
		assertTrue(dict.known(n("WY")));
		assertFalse(dict.known(n("CA")));
		assertFalse(dict.known(n("MA")));
		assertFalse(dict.known(n("UT")));
		assertFalse(dict.known(n("YE")));
		
		assertEquals("Hartford",dict.get(n("CT")));
		assertEquals("Columbia",dict.get(n("SC")));
		assertEquals("Cheyenne",dict.get(n("WY")));
		assertException(ExecutionException.class, () -> dict.get(n("CA")));
		assertException(ExecutionException.class, () -> dict.get(n("MA")));
		assertException(ExecutionException.class, () -> dict.get(n("UT")));
		assertException(ExecutionException.class, () -> dict.get(n("YE")));
		
		dict.put(n("CT"),"New York");
		dict.put(n("SC"),"Charleston");
		dict.put(n("WY"),null);
		assertEquals(3,dict.size());
		assertEquals("New York",dict.get(n("CT")));
		assertEquals("Charleston",dict.get(n("SC")));
		assertNull(dict.get(n("WY")));
	}
	
	public void test36() {
		dict.put(n("KY"),"Frankfort");
		dict.put(n("IA"),"Des Moines");
		dict.put(n("WI"),"Madison");
		dict.put(n("MN"),"Saint Paul");
		dict.put(n("IN"),"Indianapolis");
		assertEquals(5,dict.size());
		
		assertTrue(dict.known(n("IA")));
		assertTrue(dict.known(n("IN")));
		assertTrue(dict.known(n("KY")));
		assertTrue(dict.known(n("MN")));
		assertTrue(dict.known(n("WI")));
		assertFalse(dict.known(n("CT")));
		assertFalse(dict.known(n("IL")));
		assertFalse(dict.known(n("KS")));
		assertFalse(dict.known(n("MA")));
		assertFalse(dict.known(n("SD")));
		assertFalse(dict.known(n("WY")));
		
		assertEquals("Des Moines",dict.get(n("IA")));
		assertEquals("Indianapolis",dict.get(n("IN")));
		assertEquals("Frankfort",dict.get(n("KY")));
		assertEquals("Saint Paul",dict.get(n("MN")));
		assertEquals("Madison",dict.get(n("WI")));
		assertException(ExecutionException.class, () -> dict.get(n("CT")));
		assertException(ExecutionException.class, () -> dict.get(n("IL")));
		assertException(ExecutionException.class, () -> dict.get(n("KS")));
		assertException(ExecutionException.class, () -> dict.get(n("MA")));
		assertException(ExecutionException.class, () -> dict.get(n("SD")));
		assertException(ExecutionException.class, () -> dict.get(n("WY")));
		
		dict.put(n("IA"),"Ames");
		dict.put(n("KY"),"Lexington");
		dict.put(n("IN"),null);
		dict.put(n("MN"),"Minneapolis");
		dict.put(n("WI"),"Milwaukee");
		assertEquals(5,dict.size());
		assertEquals("Ames",dict.get(n("IA")));
		assertEquals("Lexington",dict.get(n("KY")));
		assertEquals("Minneapolis",dict.get(n("MN")));
		assertEquals("Milwaukee",dict.get(n("WI")));
		assertNull(dict.get(n("IN")));
	}
	
	public void test37() {
		dict.put(n("MA"),"Boston");
		dict.put(n("TN"),"Nashville");
		dict.put(n("UT"),"Salt Lake City");
		dict.put(n("ID"),"Boise");
		dict.put(n("AL"),"Montgomery");
		assertEquals(5,dict.size());
		
		assertTrue(dict.known(n("AL")));
		assertTrue(dict.known(n("ID")));
		assertTrue(dict.known(n("MA")));
		assertTrue(dict.known(n("TN")));
		assertTrue(dict.known(n("UT")));
		assertFalse(dict.known(n("AB")));
		assertFalse(dict.known(n("BC")));
		assertFalse(dict.known(n("IJ")));
		assertFalse(dict.known(n("ON")));
		assertFalse(dict.known(n("TU")));
		assertFalse(dict.known(n("YT")));
		
		assertEquals("Montgomery",dict.get(n("AL")));
		assertEquals("Boise",dict.get(n("ID")));
		assertEquals("Boston",dict.get(n("MA")));
		assertEquals("Nashville",dict.get(n("TN")));
		assertEquals("Salt Lake City",dict.get(n("UT")));
		assertException(ExecutionException.class, () -> dict.get(n("AB")));
		assertException(ExecutionException.class, () -> dict.get(n("BC")));
		assertException(ExecutionException.class, () -> dict.get(n("IJ")));
		assertException(ExecutionException.class, () -> dict.get(n("ON")));
		assertException(ExecutionException.class, () -> dict.get(n("TU")));
		assertException(ExecutionException.class, () -> dict.get(n("YT")));
		
		dict.put(n("AL"),"Birmingham");
		dict.put(n("ID"),null);
		dict.put(n("MA"),"Amherst");
		dict.put(n("TN"),"Memphis");
		dict.put(n("UT"),null);
		assertEquals(5,dict.size());
		assertEquals("Birmingham",dict.get(n("AL")));
		assertNull(dict.get(n("ID")));
		assertEquals("Amherst",dict.get(n("MA")));
		assertEquals("Memphis",dict.get(n("TN")));
		assertNull(dict.get(n("UT")));
	}
	
	public void test38() {
		dict.put(n("MD"),"Annapolis");
		dict.put(n("KS"),"Topeka");
		dict.put(n("HI"),"Honolulu");
		dict.put(n("LA"),"Baton Rouge");
		dict.put(n("OR"),"Salem");
		dict.put(n("MO"),"Jefferson City");
		dict.put(n("WA"),"Olympia");
		assertEquals(7,dict.size());

		assertTrue(dict.known(n("HI")));
		assertTrue(dict.known(n("KS")));
		assertTrue(dict.known(n("LA")));
		assertTrue(dict.known(n("MD")));
		assertTrue(dict.known(n("MO")));
		assertTrue(dict.known(n("OR")));
		assertTrue(dict.known(n("WA")));
		assertFalse(dict.known(n("FL")));
		assertFalse(dict.known(n("K")));
		assertFalse(dict.known(n("L")));
		assertFalse(dict.known(n("MB")));
		assertFalse(dict.known(n("MT")));
		assertFalse(dict.known(n("NB")));
		assertFalse(dict.known(n("QC")));
		assertFalse(dict.known(n("YT")));
		
		assertEquals("Honolulu",dict.get(n("HI")));
		assertEquals("Topeka",dict.get(n("KS")));
		assertEquals("Baton Rouge",dict.get(n("LA")));
		assertEquals("Annapolis",dict.get(n("MD")));
		assertEquals("Jefferson City",dict.get(n("MO")));
		assertEquals("Salem",dict.get(n("OR")));
		assertEquals("Olympia",dict.get(n("WA")));
		assertException(ExecutionException.class, () -> dict.get(n("FL")));
		assertException(ExecutionException.class, () -> dict.get(n("K")));
		assertException(ExecutionException.class, () -> dict.get(n("L")));
		assertException(ExecutionException.class, () -> dict.get(n("MB")));
		assertException(ExecutionException.class, () -> dict.get(n("MT")));
		assertException(ExecutionException.class, () -> dict.get(n("NB")));
		assertException(ExecutionException.class, () -> dict.get(n("QC")));
		assertException(ExecutionException.class, () -> dict.get(n("YK")));

		dict.put(n("HI"),null);
		dict.put(n("KS"),"Kansas City");
		dict.put(n("LA"),"New Orleans");
		dict.put(n("MD"),"Baltimore");
		dict.put(n("MO"),"Kansas City");
		dict.put(n("OR"),"Portland");
		dict.put(n("WA"),"Seattle");
		assertEquals(7,dict.size());
		assertNull(dict.get(n("HI")));
		assertEquals("Kansas City",dict.get(n("KS")));
		assertEquals("New Orleans",dict.get(n("LA")));
		assertEquals("Baltimore",dict.get(n("MD")));
		assertEquals("Kansas City",dict.get(n("MO")));
		assertEquals("Portland",dict.get(n("OR")));
		assertEquals("Seattle",dict.get(n("WA")));
	}
	
	public void test40() {
		dict.put(n("NY"),"Albany");
		dict.put(n("MD"),"Annapolis");
		dict.put(n("GA"),"Atlanta");
		dict.put(n("ME"),"Augusta");
		dict.put(n("TX"),"Austin");
		dict.put(n("LA"),"Baton Rouge");
		dict.put(n("ND"),"Bismarck");
		dict.put(n("ID"),"Boise");
		dict.put(n("MA"),"Boston");
		dict.put(n("NV"),"Carson City");
		dict.put(n("WV"),"Charleston");
		dict.put(n("WY"),"Cheyenne");
		dict.put(n("SC"),"Columbia");
		dict.put(n("OH"),"Columbus");
		dict.put(n("NH"),"Concord");
		dict.put(n("CO"),"Denver");
		dict.put(n("IA"),"Des Moines");
		dict.put(n("DE"),"Dover");
		dict.put(n("KY"),"Frankfort");
		dict.put(n("PA"),"Harrisburg");
		dict.put(n("CT"),"Hartford");
		dict.put(n("MT"),"Helena");
		dict.put(n("HI"),"Honolulu");
		dict.put(n("IN"),"Indianapolis");
		dict.put(n("MS"),"Jackson");
		
		assertEquals(25,dict.size());
	}
	
	public void test41() {
		dict.put(n("NY"),"Albany");
		dict.put(n("MD"),"Annapolis");
		dict.put(n("GA"),"Atlanta");
		dict.put(n("ME"),"Augusta");
		dict.put(n("TX"),"Austin");
		dict.put(n("LA"),"Baton Rouge");
		dict.put(n("ND"),"Bismarck");
		dict.put(n("ID"),"Boise");
		dict.put(n("MA"),"Boston");
		dict.put(n("NV"),"Carson City");
		dict.put(n("WV"),"Charleston");
		dict.put(n("WY"),"Cheyenne");
		dict.put(n("SC"),"Columbia");
		dict.put(n("OH"),"Columbus");
		dict.put(n("NH"),"Concord");
		dict.put(n("CO"),"Denver");
		dict.put(n("IA"),"Des Moines");
		dict.put(n("DE"),"Dover");
		dict.put(n("KY"),"Frankfort");
		dict.put(n("PA"),"Harrisburg");
		dict.put(n("CT"),"Hartford");
		dict.put(n("MT"),"Helena");
		dict.put(n("HI"),"Honolulu");
		dict.put(n("IN"),"Indianapolis");
		dict.put(n("MS"),"Jackson");

		assertTrue(dict.known(n("CO")));
		assertTrue(dict.known(n("CT")));
		assertTrue(dict.known(n("DE")));
		assertTrue(dict.known(n("GA")));
		assertTrue(dict.known(n("HI")));
		assertTrue(dict.known(n("IA")));
		assertTrue(dict.known(n("ID")));
		assertTrue(dict.known(n("IN")));
		assertTrue(dict.known(n("KY")));
		assertTrue(dict.known(n("LA")));
		assertTrue(dict.known(n("MA")));
		assertTrue(dict.known(n("MD")));
		assertTrue(dict.known(n("ME")));
		assertTrue(dict.known(n("MS")));
		assertTrue(dict.known(n("MT")));
		assertTrue(dict.known(n("ND")));
		assertTrue(dict.known(n("NH")));
		assertTrue(dict.known(n("NV")));
		assertTrue(dict.known(n("NY")));
		assertTrue(dict.known(n("OH")));
		assertTrue(dict.known(n("PA")));
		assertTrue(dict.known(n("SC")));
		assertTrue(dict.known(n("TX")));
		assertTrue(dict.known(n("WV")));
		assertTrue(dict.known(n("WY")));
	}
	
	public void test42() {
		dict.put(n("NY"),"Albany");
		dict.put(n("MD"),"Annapolis");
		dict.put(n("GA"),"Atlanta");
		dict.put(n("ME"),"Augusta");
		dict.put(n("TX"),"Austin");
		dict.put(n("LA"),"Baton Rouge");
		dict.put(n("ND"),"Bismarck");
		dict.put(n("ID"),"Boise");
		dict.put(n("MA"),"Boston");
		dict.put(n("NV"),"Carson City");
		dict.put(n("WV"),"Charleston");
		dict.put(n("WY"),"Cheyenne");
		dict.put(n("SC"),"Columbia");
		dict.put(n("OH"),"Columbus");
		dict.put(n("NH"),"Concord");
		dict.put(n("CO"),"Denver");
		dict.put(n("IA"),"Des Moines");
		dict.put(n("DE"),"Dover");
		dict.put(n("KY"),"Frankfort");
		dict.put(n("PA"),"Harrisburg");
		dict.put(n("CT"),"Hartford");
		dict.put(n("MT"),"Helena");
		dict.put(n("HI"),"Honolulu");
		dict.put(n("IN"),"Indianapolis");
		dict.put(n("MS"),"Jackson");

		assertFalse(dict.known(n("CA")));
		assertFalse(dict.known(n("CS")));
		assertFalse(dict.known(n("CZ")));
		assertFalse(dict.known(n("FL")));
		assertFalse(dict.known(n("GE")));
		assertFalse(dict.known(n("HO")));
		assertFalse(dict.known(n("IB")));
		assertFalse(dict.known(n("IL")));
		assertFalse(dict.known(n("KS")));
		assertFalse(dict.known(n("L")));
		assertFalse(dict.known(n("LB")));
		assertFalse(dict.known(n("MB")));
		assertFalse(dict.known(n("MDA")));
		assertFalse(dict.known(n("MO")));
		assertFalse(dict.known(n("MST")));
		assertFalse(dict.known(n("NC")));
		assertFalse(dict.known(n("NE")));
		assertFalse(dict.known(n("NJ")));
		assertFalse(dict.known(n("NW")));
		assertFalse(dict.known(n("NZ")));
		assertFalse(dict.known(n("ON")));
		assertFalse(dict.known(n("PE")));
		assertFalse(dict.known(n("SK")));
		assertFalse(dict.known(n("UT")));
		assertFalse(dict.known(n("WWW")));
		assertFalse(dict.known(n("YK")));
	}
	
	public void test43() {
		dict.put(n("NY"),"Albany");
		dict.put(n("MD"),"Annapolis");
		dict.put(n("GA"),"Atlanta");
		dict.put(n("ME"),"Augusta");
		dict.put(n("TX"),"Austin");
		dict.put(n("LA"),"Baton Rouge");
		dict.put(n("ND"),"Bismarck");
		dict.put(n("ID"),"Boise");
		dict.put(n("MA"),"Boston");
		dict.put(n("NV"),"Carson City");
		dict.put(n("WV"),"Charleston");
		dict.put(n("WY"),"Cheyenne");
		dict.put(n("SC"),"Columbia");
		dict.put(n("OH"),"Columbus");
		dict.put(n("NH"),"Concord");
		dict.put(n("CO"),"Denver");
		dict.put(n("IA"),"Des Moines");
		dict.put(n("DE"),"Dover");
		dict.put(n("KY"),"Frankfort");
		dict.put(n("PA"),"Harrisburg");
		dict.put(n("CT"),"Hartford");
		dict.put(n("MT"),"Helena");
		dict.put(n("HI"),"Honolulu");
		dict.put(n("IN"),"Indianapolis");
		dict.put(n("MS"),"Jackson");
		
		assertEquals("Denver",dict.get(n("CO")));
		assertEquals("Hartford",dict.get(n("CT")));
		assertEquals("Dover",dict.get(n("DE")));
		assertEquals("Atlanta",dict.get(n("GA")));
		assertEquals("Honolulu",dict.get(n("HI")));
		assertEquals("Des Moines",dict.get(n("IA")));
		assertEquals("Boise",dict.get(n("ID")));
		assertEquals("Indianapolis",dict.get(n("IN")));
		assertEquals("Frankfort",dict.get(n("KY")));
		assertEquals("Baton Rouge",dict.get(n("LA")));
		assertEquals("Boston",dict.get(n("MA")));
		assertEquals("Annapolis",dict.get(n("MD")));
		assertEquals("Augusta",dict.get(n("ME")));
		assertEquals("Jackson",dict.get(n("MS")));
		assertEquals("Helena",dict.get(n("MT")));
		assertEquals("Bismarck",dict.get(n("ND")));
		assertEquals("Concord",dict.get(n("NH")));
		assertEquals("Carson City",dict.get(n("NV")));
		assertEquals("Albany",dict.get(n("NY")));
		assertEquals("Columbus",dict.get(n("OH")));
		assertEquals("Harrisburg",dict.get(n("PA")));
		assertEquals("Columbia",dict.get(n("SC")));
		assertEquals("Austin",dict.get(n("TX")));
		assertEquals("Charleston",dict.get(n("WV")));
		assertEquals("Cheyenne",dict.get(n("WY")));
	}
	
	public void test44() {
		dict.put(n("NY"),"Albany");
		dict.put(n("MD"),"Annapolis");
		dict.put(n("GA"),"Atlanta");
		dict.put(n("ME"),"Augusta");
		dict.put(n("TX"),"Austin");
		dict.put(n("LA"),"Baton Rouge");
		dict.put(n("ND"),"Bismarck");
		dict.put(n("ID"),"Boise");
		dict.put(n("MA"),"Boston");
		dict.put(n("NV"),"Carson City");
		dict.put(n("WV"),"Charleston");
		dict.put(n("WY"),"Cheyenne");
		dict.put(n("SC"),"Columbia");
		dict.put(n("OH"),"Columbus");
		dict.put(n("NH"),"Concord");
		dict.put(n("CO"),"Denver");
		dict.put(n("IA"),"Des Moines");
		dict.put(n("DE"),"Dover");
		dict.put(n("KY"),"Frankfort");
		dict.put(n("PA"),"Harrisburg");
		dict.put(n("CT"),"Hartford");
		dict.put(n("MT"),"Helena");
		dict.put(n("HI"),"Honolulu");
		dict.put(n("IN"),"Indianapolis");
		dict.put(n("MS"),"Jackson");
		
		assertException(ExecutionException.class, () -> dict.get(n("CA")));
		assertException(ExecutionException.class, () -> dict.get(n("CS")));
		assertException(ExecutionException.class, () -> dict.get(n("CZ")));
		assertException(ExecutionException.class, () -> dict.get(n("FL")));
		assertException(ExecutionException.class, () -> dict.get(n("GE")));
		assertException(ExecutionException.class, () -> dict.get(n("HO")));
		assertException(ExecutionException.class, () -> dict.get(n("IB")));
		assertException(ExecutionException.class, () -> dict.get(n("IL")));
		assertException(ExecutionException.class, () -> dict.get(n("KS")));
		assertException(ExecutionException.class, () -> dict.get(n("L")));
		assertException(ExecutionException.class, () -> dict.get(n("LB")));
		assertException(ExecutionException.class, () -> dict.get(n("MB")));
		assertException(ExecutionException.class, () -> dict.get(n("MDA")));
		assertException(ExecutionException.class, () -> dict.get(n("MO")));
		assertException(ExecutionException.class, () -> dict.get(n("MST")));
		assertException(ExecutionException.class, () -> dict.get(n("NC")));
		assertException(ExecutionException.class, () -> dict.get(n("NE")));
		assertException(ExecutionException.class, () -> dict.get(n("NJ")));
		assertException(ExecutionException.class, () -> dict.get(n("NW")));
		assertException(ExecutionException.class, () -> dict.get(n("NZ")));
		assertException(ExecutionException.class, () -> dict.get(n("ON")));
		assertException(ExecutionException.class, () -> dict.get(n("PE")));
		assertException(ExecutionException.class, () -> dict.get(n("SK")));
		assertException(ExecutionException.class, () -> dict.get(n("UT")));
		assertException(ExecutionException.class, () -> dict.get(n("WWW")));
		assertException(ExecutionException.class, () -> dict.get(n("YK")));
	}
	
	public void test45() {
		dict.put(n("NY"),"Albany");
		dict.put(n("MD"),"Annapolis");
		dict.put(n("GA"),"Atlanta");
		dict.put(n("ME"),"Augusta");
		dict.put(n("TX"),"Austin");
		dict.put(n("LA"),"Baton Rouge");
		dict.put(n("ND"),"Bismarck");
		dict.put(n("ID"),"Boise");
		dict.put(n("MA"),"Boston");
		dict.put(n("NV"),"Carson City");
		dict.put(n("WV"),"Charleston");
		dict.put(n("WY"),"Cheyenne");
		dict.put(n("SC"),"Columbia");
		dict.put(n("OH"),"Columbus");
		dict.put(n("NH"),"Concord");
		dict.put(n("CO"),"Denver");
		dict.put(n("IA"),"Des Moines");
		dict.put(n("DE"),"Dover");
		dict.put(n("KY"),"Frankfort");
		dict.put(n("PA"),"Harrisburg");
		dict.put(n("CT"),"Hartford");
		dict.put(n("MT"),"Helena");
		dict.put(n("HI"),"Honolulu");
		dict.put(n("IN"),"Indianapolis");
		dict.put(n("MS"),"Jackson");
		
		dict.put(n("NY"),"New York");
		dict.put(n("MD"),"Baltimore");
		dict.put(n("GA"),"Athens");
		dict.put(n("ME"),"Portland");
		dict.put(n("TX"),"Houston");
		dict.put(n("LA"),"New Orleans");
		dict.put(n("ND"),"");
		dict.put(n("ID"),null);
		dict.put(n("MA"),"Lexington");
		dict.put(n("NV"),"Las Vegas");
		dict.put(n("WV"),"");
		dict.put(n("WY"),null);
		dict.put(n("SC"),"Charleston");
		dict.put(n("OH"),"Cincinnati");
		dict.put(n("NH"),"Manchester");
		dict.put(n("CO"),"Boulder");
		dict.put(n("IA"),"Ames");
		dict.put(n("DE"),"Wilmington");
		dict.put(n("KY"),"Louisville");
		dict.put(n("PA"),"Philadelphia");
		dict.put(n("CT"),"Bridgeport");
		dict.put(n("MT"),"Billings");
		dict.put(n("HI"),"Hawaii City");
		dict.put(n("IN"),"Gary");
		dict.put(n("MS"),"");
		
		assertEquals("Boulder",dict.get(n("CO")));
		assertEquals("Bridgeport",dict.get(n("CT")));
		assertEquals("Wilmington",dict.get(n("DE")));
		assertEquals("Athens",dict.get(n("GA")));
		assertEquals("Hawaii City",dict.get(n("HI")));
		assertEquals("Ames",dict.get(n("IA")));
		assertEquals(null,dict.get(n("ID")));
		assertEquals("Gary",dict.get(n("IN")));
		assertEquals("Louisville",dict.get(n("KY")));
		assertEquals("New Orleans",dict.get(n("LA")));
		assertEquals("Lexington",dict.get(n("MA")));
		assertEquals("Baltimore",dict.get(n("MD")));
		assertEquals("Portland",dict.get(n("ME")));
		assertEquals("",dict.get(n("MS")));
		assertEquals("Billings",dict.get(n("MT")));
		assertEquals("",dict.get(n("ND")));
		assertEquals("Manchester",dict.get(n("NH")));
		assertEquals("Las Vegas",dict.get(n("NV")));
		assertEquals("New York",dict.get(n("NY")));
		assertEquals("Cincinnati",dict.get(n("OH")));
		assertEquals("Philadelphia",dict.get(n("PA")));
		assertEquals("Charleston",dict.get(n("SC")));
		assertEquals("Houston",dict.get(n("TX")));
		assertEquals("",dict.get(n("WV")));
		assertEquals(null,dict.get(n("WY")));
	}
	
	public void test50() {
		Collection<Object> c = dict.values();
		assertTrue(c.isEmpty());
	}
	
	public void test51() {
		dict.put(n("France"),"Paris");
		Collection<Object> c = dict.values();
		assertEquals(1,c.size());
		assertTrue(c.contains("Paris"));
	}
	
	public void test52() {
		dict.put(n("Germany"),"Berlin");
		dict.put(n("Switzerland"),"Bern");
		Collection<Object> c = dict.values();
		assertEquals(2,c.size());
		assertTrue(c.contains("Berlin"));
		assertTrue(c.contains("Bern"));
	}
	
	public void test53() {
		dict.put(n("Spain"),"Madrid");
		dict.put(n("Italy"),"Rome");
		dict.put(n("Portugal"),"Lisbon");
		Collection<Object> c = dict.values();
		assertEquals(3,c.size());
		assertTrue(c.contains("Madrid"));
		assertTrue(c.contains("Rome"));
		assertTrue(c.contains("Lisbon"));
	}
	
	public void test54() {
		dict.put(n("test"),null);
		dict.put(n("TEST"),null);
		Collection<Object> c = dict.values();
		assertEquals(2,c.size());
		assertTrue(c.contains(null));
		Iterator<Object> it = c.iterator();
		assertNull(it.next());
		assertNull(it.next());
	}
	
	public void test55() {
		dict.put(n("Slovakia"),"Bratislava");
		dict.put(n("Czech"),"Prague");
		Collection<Object> c = dict.values();
		assertEquals(2,c.size());
		Iterator<Object> it = c.iterator();
		assertEquals("Prague",it.next());
		assertEquals("Bratislava",it.next());		
	}
	
	public void test56() {
		dict.put(n("Slovenia"), "Ljubljana");
		dict.put(n("Croatia"), "Zagreb");
		dict.put(n("Macedonia"), "Skopje");
		dict.put(n("Bosnia & Hercegovina"), "Sarajevo");
		dict.put(n("Montenegro"), "Titograd");
		dict.put(n("Serbia"), "Belgrade");
		dict.put(n("Kosovo"), "Pristina");
		Collection<Object> c = dict.values();
		assertEquals(7,c.size());
		Iterator<Object> it = c.iterator();
		assertEquals("Sarajevo",it.next());
		assertEquals("Zagreb",it.next());
		assertEquals("Pristina",it.next());
		assertEquals("Skopje",it.next());
		assertEquals("Titograd",it.next());
		assertEquals("Belgrade",it.next());
		assertEquals("Ljubljana",it.next());
	}
	
	public void test60() {
		String s= dict.toString();
		assertTrue("Doesn't start with with <<: '" + s + "'",s.startsWith("<<"));
		assertTrue("Doesn't end with with >>: '" + s + "'",s.endsWith(">>"));
		s = s.substring(2,s.length()-2);
		assertTrue("Has extraneous non-space characters: '" + s + '"',s.trim().isEmpty());
	}
	
	public void test61() {
		dict.put(n("CS-250"),"Sorenson");
		String s= dict.toString();
		assertTrue("Doesn't start with with <<: '" + s + "'",s.startsWith("<<"));
		assertTrue("Doesn't end with with >>: '" + s + "'",s.endsWith(">>"));
		s = s.substring(2,s.length()-2).trim();
		String[] pieces = s.split(" ");
		assertEquals("should have two parts: '" + s + "'",2,pieces.length);
		assertEquals("/CS-250",pieces[0]);
		assertEquals("Sorenson",pieces[1]);
	}
	
	public void test62() {
		dict.put(n("CS-240"),"McNally");
		dict.put(n("CS-150"),"Rock");
		
		String s= dict.toString();
		s = s.substring(2,s.length()-2).trim();
		String[] pieces = s.split(" ");
		assertEquals("should have four parts: '" + s + "'",4,pieces.length);
		assertEquals("/CS-150",pieces[0]);
		assertEquals("Rock",pieces[1]);	
		assertEquals("/CS-240",pieces[2]);
		assertEquals("McNally",pieces[3]);		
	}
	
	public void test63() {
		dict.put(n("CS-315"),"McNally");
		dict.put(n("CS-251"),"Zhao");
		dict.put(n("CS-251"),"Sorenson");
		dict.put(n("CS-337"),"Sorenson");
		dict.put(n("CS-337"),"Rock");
		
		String s= dict.toString();
		assertTrue("Doesn't start with with <<: '" + s + "'",s.startsWith("<<"));
		assertTrue("Doesn't end with with >>: '" + s + "'",s.endsWith(">>"));
		s = s.substring(2,s.length()-2).trim();
		String[] pieces = s.split(" ");
		assertEquals("should have six parts: '" + s + "'",6,pieces.length);
		
		assertEquals("/CS-251",pieces[0]);
		assertEquals("Sorenson",pieces[1]);	
		assertEquals("/CS-315",pieces[2]);
		assertEquals("McNally",pieces[3]);		
		assertEquals("/CS-337",pieces[4]);
		assertEquals("Rock",pieces[5]);		
	}
	
	public void test64() {
		dict.put(n("1c"),2.5);
		dict.put(n("5"),null);
		
		String s= dict.toString();
		s = s.substring(2,s.length()-2).trim();
		String[] pieces = s.split(" ");
		assertEquals("should have four parts: '" + s + "'",4,pieces.length);
		assertEquals("/1c",pieces[0]);
		assertEquals("2.5",pieces[1]);	
		assertEquals("/5",pieces[2]);
		assertEquals("null",pieces[3]);				
	}
	
	public void test67() {
		dict.put(n("CS-351"),"Boyland");
		dict.put(n("CS-535"),"Cheng");
		dict.put(n("CS-202"),"Kate");
		dict.put(n("CS-422"),"Mali");
		dict.put(n("CS-443"),"McRoy");
		dict.put(n("CS-431"),"Zhao");
		
		String s= dict.toString();
		s = s.substring(2,s.length()-2).trim();
		String[] pieces = s.split(" ");
		assertEquals("should have twelve parts: '" + s + "'",12,pieces.length);
		assertEquals("/CS-202",pieces[0]);
		assertEquals("Kate",pieces[1]);
		assertEquals("/CS-351",pieces[2]);
		assertEquals("Boyland",pieces[3]);
		assertEquals("/CS-422",pieces[4]);
		assertEquals("Mali",pieces[5]);
		assertEquals("/CS-431",pieces[6]);
		assertEquals("Zhao",pieces[7]);
		assertEquals("/CS-443",pieces[8]);
		assertEquals("McRoy",pieces[9]);
		assertEquals("/CS-535",pieces[10]);
		assertEquals("Cheng",pieces[11]);
	}
	
	public void test70() {
		dict.copy(dict);
		assertEquals(0,dict.size());
	}
	
	public void test71() {
		Dictionary dict2 = new Dictionary();
		dict.put(n("Hello"),"Hallo");
		dict2.put(n("Good day"),"Guten Tag");
		assertEquals(1,dict.size());
		assertEquals(1,dict2.size());
		dict.copy(dict2);
		assertEquals(2,dict.size());
		assertEquals(1,dict2.size());
		assertEquals("Guten Tag",dict.get(n("Good day")));
		assertEquals("Hallo",dict.get(n("Hello")));
	}
	
	public void test72() {
		Dictionary dict2 = new Dictionary();
		dict.put(n("Hello"),"Hallo");
		dict2.put(n("Hello"),"Guten Tag");
		assertEquals(1,dict.size());
		assertEquals(1,dict2.size());
		dict.copy(dict2);
		assertEquals(1,dict.size());
		assertEquals(1,dict2.size());
		assertEquals("Guten Tag",dict.get(n("Hello")));
	}
	
	public void test73() {
		dict.put(n("Hello"),"Bonjour");
		dict.copy(dict);
		assertEquals(1,dict.size());
		assertEquals("Bonjour",dict.get(n("Hello")));
	}
	
	public void test74() {
		Dictionary dict2 = new Dictionary();
		dict.put(n("Bye"),null);
		dict2.put(n("Bye"),"");
		dict2.copy(dict);
		assertNull(dict2.get(n("Bye")));
		dict2.put(n("Bye"),"");
		assertNull(dict.get(n("Bye")));
		dict.copy(dict2);
		assertEquals(1,dict.size());
		assertEquals("",dict.get(n("Bye")));
	}
	
	public void test75() {
		Dictionary dict2 = new Dictionary();
		dict.put(n("A"),"1");
		dict2.put(n("B"),"2");
		dict.copy(dict2);
		dict.put(n("C"),"3");
		assertEquals(3,dict.size());
		assertEquals(1,dict2.size());
		assertFalse(dict2.known(n("A")));
		assertFalse(dict2.known(n("C")));
	}
	
	public void test76() {
		Dictionary dict2 = new Dictionary();
		dict.put(n("A"),"1");
		dict.put(n("B"),"2");
		dict2.put(n("A"),1);
		dict2.put(n("C"),3);
		dict.copy(dict2);
		assertEquals(3,dict.size());
		assertEquals(2,dict2.size());
		dict2.copy(dict);
		assertEquals(3,dict2.size());
		dict.copy(dict2);
		assertEquals(3,dict.size());
		assertEquals(1,dict.get(n("A")));
		assertEquals("2",dict.get(n("B")));
		assertEquals(3,dict.get(n("C")));
	}
	
	public void test78() {
		Dictionary dict2 = new Dictionary();
		dict.put(n("CS-315"),"McNally");
		dict.put(n("CS-240"),"McNally");
		dict.put(n("CS-337"),"Sorenson");
		dict.put(n("CS-202"),"Rock");
		dict.put(n("CS-251"),"Sorenson");
		dict.put(n("CS-361"),"Rock");
		dict2.put(n("CS-315"),"McNally");
		dict2.put(n("CS-150"),"Rock");
		dict2.put(n("CS-337"),"Rock");
		dict2.put(n("CS-241"),"Sorenson");
		dict2.put(n("CS-250"),"Sorenson");
		dict2.put(n("CS-151"),"McNally");
		dict.copy(dict2);
		assertEquals(10,dict.size());
		assertEquals("Rock",dict.get(n("CS-337")));
		assertEquals("Sorenson",dict.get(n("CS-251")));
		assertEquals("McNally",dict.get(n("CS-151")));
	}

	public void test79() {
		dict.put(n("Hello"), 79);
		dict.put(n("bye"), -188);
		dict.copy(dict);
		assertEquals(2, dict.size());
		assertEquals(-188, dict.get(n("bye")));
	}
}
