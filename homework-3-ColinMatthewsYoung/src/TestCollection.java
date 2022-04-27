
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import junit.framework.TestCase;

/*
 * Abstract class for testing collections.  Do not try to run it on its own.
 */
public abstract class TestCollection<E> extends TestCase {

	protected <T> void assertException(Class<?> excClass, Runnable f) {
		try {
			f.run();
			assertFalse("Should have thrown an exception, not returned",true);
		} catch (RuntimeException ex) {
			if (!excClass.isInstance(ex)) {
				ex.printStackTrace();
				assertFalse("Wrong kind of exception thrown: "+ ex.getClass().getSimpleName(),true);
			}
		}		
	}
	
	/**
	 * Convert the result into a string, or into the name of the exception thrown
	 * @param supp supplier of something, may return null
	 * @return string of result, or simple name of exception thrown
	 */
	protected <T> String toString(Supplier<T> supp) {
		try {
			return ""+supp.get();
		} catch (RuntimeException ex) {
			return ex.getClass().getSimpleName();
		}
	}

	protected Collection<E> c;
	protected E[] e;
	protected boolean 
	    permitNulls = true, 
	    permitDuplicates = true, 
	    preserveOrder = true,
	    failFast = true,
	    hasRemove = true;

	protected Iterator<E> it;
	
	protected void assertAndRemove(E val, E[] values, boolean[] used) {
		int n = values.length;
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < n; ++i) {
			if (!used[i] && values[i].equals(val)) {
				used[i] = true;
				return;
			} else {
				sb.append(val);
				sb.append(" ");
			}
		}
		assertFalse(val + " not in { " + sb + "}",true);
	}
	
	@SafeVarargs
	protected final void testcol(Collection<E> l, String name, E... parts)
	{
		assertEquals(name + ".size()",parts.length,l.size());
		Iterator<E> it = l.iterator();
		boolean[] used = null;
		if (!preserveOrder) {
			used = new boolean[parts.length];
		}
		int i=0;
		while (it.hasNext() && i < parts.length) {
			E obj = it.next();
			if (preserveOrder) {
				assertEquals(name + ".next[" + i + "]",parts[i],obj);
			} else {
				assertAndRemove(obj,parts,used);
			}
			++i;
		}
		assertFalse(name + ".hasNext() is true, even after " + parts.length + " next() calls",it.hasNext());
		assertFalse(name + ".hasNext() is false, after only " + i + " next() calls",(i < parts.length));
	}

	@Override
	protected void setUp() {
		try {
			assert c.size() == 0;
			assertTrue("Assertions not enabled.  Add -ea to VM Args Pane in Arguments tab of Run Configuration",false);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		initCollections();
		if (e.length < 10) throw new RuntimeException("test not properly set up");
	}
	
	/**
	 * Initialize c and e.
	 * The e array must have at least 10 elements.
	 */
	protected abstract void initCollections();
	
	
	/// test0X: tests of add/size without using iterators.
	
	public void test00() {
		assertEquals(0,c.size());
	}
	
	public void test01() {
		c.add(e[1]);
		assertEquals(1,c.size());
	}
	
	public void test02() {
		c.add(e[1]);
		c.add(e[4]);
		assertEquals(2,c.size());
	}
	
	public void test03() {
		c.add(e[2]);
		c.add(e[5]);
		c.add(e[8]);
		assertEquals(3,c.size());
	}
	
	public void test04() {
		if (!permitNulls) return;
		c.add(null);
		c.add(e[5]);
		assertEquals(2,c.size());
	}
	
	public void test05() {
		if (!permitNulls | !permitDuplicates) return;
		c.add(null);
		c.add(null);
		c.add(null);
		assertEquals(3,c.size());
	}
	
	public void test06() {
		c.add(e[6]);
		c.add(e[5]);
		c.add(e[4]);
		c.add(e[3]);
		c.add(e[2]);
		c.add(e[1]);
		assertEquals(6,c.size());
	}
	
	
	/// test1X: test of hasNext

	public void test10() {
		it = c.iterator();
		assertFalse(it.hasNext());
	}
	
	public void test11() {
		c.add(e[1]);
		it = c.iterator();
		assertTrue(it.hasNext());
	}
	
	public void test12() {
		c.add(e[3]);
		c.add(e[6]);
		it = c.iterator();
		assertTrue(it.hasNext());
	}
	
	public void test13() {
		if (!permitNulls) return;
		c.add(null);
		it = c.iterator();
		assertTrue(it.hasNext());
	}
	
	public void test14() {
		it = c.iterator();
		c.add(e[4]);
		if (!failFast) return;
		assertException(ConcurrentModificationException.class,() -> it.hasNext());
	}
	
	public void test15() {
		c.add(e[5]);
		it = c.iterator();
		c.add(e[6]);
		if (!failFast) return;
		assertException(ConcurrentModificationException.class,() -> it.hasNext());
	}
	
	public void test16() {
		Iterator<E> it2 = c.iterator();
		c.add(e[6]);
		it = c.iterator();
		assertTrue(it.hasNext());
		if (!failFast) return;
		assertException(ConcurrentModificationException.class,() -> it2.hasNext());
	}
	
	public void test17() {
		c.add(e[7]);
		Iterator<E> it2 = c.iterator();
		c.add(e[8]);
		it = c.iterator();
		assertTrue(it.hasNext());
		if (!failFast) return;
		assertException(ConcurrentModificationException.class,() -> it2.hasNext());
	}
	
	public void test18() {
		c.add(e[8]);
		Iterator<E> it2 = c.iterator();
		it = c.iterator();
		assertTrue(it.hasNext());
		assertTrue(it2.hasNext());
	}
	
	public void test19() {
		it = c.iterator();
		assertFalse(it.hasNext());
		it = c.iterator();
		assertFalse(it.hasNext());
		if (!permitNulls) return;
		c.add(null);
		it = c.iterator();
		assertTrue(it.hasNext());
		it = c.iterator();
		assertTrue(it.hasNext());
	}
	
	
	/// test2X: test of iterator next
	
	public void test20() {
		it = c.iterator();
		assertException(NoSuchElementException.class,() -> it.next());
	}
	
	public void test21() {
		c.add(e[1]);
		it = c.iterator();
		assertEquals(e[1],it.next());
	}
	
	public void test22() {
		c.add(e[2]);
		it = c.iterator();
		it.next(); // for side-effect
		assertException(NoSuchElementException.class,() -> it.next());
	}
	
	public void test23() {
		c.add(e[2]);
		c.add(e[3]);
		it = c.iterator();
		if (preserveOrder) {
			assertEquals(e[2],it.next());
		} else {
			E obj = it.next();
			assertTrue(obj.equals(e[2]) || obj.equals(e[3]));
		}
	}
	
	public void test24() {
		c.add(e[2]);
		c.add(e[4]);
		it = c.iterator();
		it.next(); // for side-effect
		if (preserveOrder) {
			assertEquals(e[4],it.next());
		} else {
			E obj = it.next();
			assertTrue(obj.equals(e[2]) || obj.equals(e[4]));
		}
	}
	
	public void test25() {
		c.add(e[5]);
		c.add(e[2]);
		it = c.iterator();
		it.next();
		it.next();
		assertException(NoSuchElementException.class,() -> it.next());
	}
	
	public void test26() {
		c.add(e[6]);
		c.add(e[7]);
		Iterator<E> it2 = c.iterator();
		it = c.iterator();
		if (preserveOrder) {
			assertEquals(e[6], it.next());
			assertEquals(e[6], it2.next());
			assertEquals(e[7], it.next());
		} else {
			E obj1 = it.next();
			assertEquals(obj1, it2.next());
			E obj2 = it.next();
			if (obj1.equals(e[6])) {
				assertEquals(e[7],obj2);
			} else {
				assertEquals(e[7],obj1);
				assertEquals(e[6],obj2);
			}
		}
	}
	
	public void test27() {
		if (!failFast) return;
		it = c.iterator();
		c.add(e[2]);
		assertException(ConcurrentModificationException.class, () -> it.next());
	}
	
	public void test28() {
		c.add(e[2]);
		it = c.iterator();
		assertEquals(e[2],it.next());
		c.add(e[4]);
		it = c.iterator();
		E obj1 = it.next();
		if (preserveOrder) {
			assertEquals(e[2],obj1);
		} else {
			assertTrue(obj1.equals(e[2]) || obj1.equals(e[4]));
		}
		c.add(e[6]);
		Iterator<E> it2 = c.iterator();
		if (failFast) {
			assertException(ConcurrentModificationException.class, () -> it.next());
		}
		E obj2 = it2.next();
		if (preserveOrder) {
			assertEquals(e[2],obj2);
		} else {
			assertTrue(obj2.equals(e[2]) || obj2.equals(e[4]) || obj2.equals(e[6]));
		}
		it = c.iterator();
		E obj3 = it2.next();
		if (preserveOrder) {
			assertEquals(e[4],obj3);
		} else {
			assertFalse(obj3.equals(obj2));
			assertTrue(obj3.equals(e[2]) || obj3.equals(e[4]) || obj3.equals(e[6]));
		}
		assertEquals(obj2,it.next());
		c.add(e[8]);
		if (failFast) {
			assertException(ConcurrentModificationException.class, () -> it.next());
			assertException(ConcurrentModificationException.class, () -> it2.next());
		}
	}
	
	public void test29() {
		c.add(e[3]);
		c.add(e[6]);
		c.add(e[9]);
		it = c.iterator();
		E obj1 = it.next();
		if (preserveOrder) {
			assertEquals(e[3],obj1);
		} else {
			assertTrue(obj1.equals(e[3]) || obj1.equals(e[6]) || obj1.equals(e[9]));
		}
		Iterator<E> it2 = c.iterator();
		Iterator<E> it3 = c.iterator();
		assertEquals(obj1,it3.next());
		E obj2 = it.next();
		if (preserveOrder) {
			assertEquals(e[6],obj2);
		} else {
			assertTrue(obj2.equals(e[3]) || obj2.equals(e[6]) || obj2.equals(e[9]));
			assertFalse(obj1.equals(obj2));
		}
		E obj3 = it.next();
		if (preserveOrder) {
			assertEquals(e[9],obj3);
		} else {
			assertTrue(obj3.equals(e[3]) || obj3.equals(e[6]) || obj3.equals(e[9]));
			assertFalse(obj1.equals(obj3));
			assertFalse(obj2.equals(obj3));
		}
		assertEquals(obj2,it3.next());
		assertEquals(obj1,it2.next());
		assertException(NoSuchElementException.class,() -> it.next());
	}
	
	
	/// test3X: test iterators using "testcol"
	
	public void test30() {
		testcol(c,"empty");
	}
	
	public void test31()
	{
		c.add(e[1]);
		testcol(c,"{1}",e[1]);
	}
	
	public void test32() {
		c.add(e[0]);
		c.add(e[4]);
		testcol(c,"{0,4}",e[0],e[4]);
	}
	
	public void test33() {
		c.add(e[1]);
		c.add(e[4]);
		c.add(e[5]);
		testcol(c,"{1,4,5}",e[1],e[4],e[5]);
	}
	
	public void test34() {
		c.add(e[8]);
		c.add(e[3]);
		c.add(e[1]);
		c.add(e[0]);
		testcol(c,"{8,3,1,0}",e[8],e[3],e[1],e[0]);
	}
	
	public void test35() {
		if (!permitNulls) return;
		c.add(e[3]);
		c.add(null);
		c.add(e[8]);
		testcol(c,"{3,null,8}",e[3],null,e[8]);
	}
	
	
	/// text4X: testing clear
	
	public void test40() {
		c.clear();
		assertEquals(0,c.size());
	}
	
	public void test41() {
		c.add(e[1]);
		c.clear();
		assertFalse(c.iterator().hasNext());
	}
	
	public void test42() {
		c.add(e[4]);
		c.add(e[2]);
		c.clear();
		it = c.iterator();
		assertException(NoSuchElementException.class,() -> it.next());
	}
	
	public void test43() {
		it = c.iterator();
		c.add(e[3]);
		c.clear();
		if (failFast) {
			assertException(ConcurrentModificationException.class, () -> it.hasNext());
		}
	}
	
	public void test44() {
		it = c.iterator();
		c.add(e[4]);
		c.add(e[5]);
		c.clear();
		if (failFast) {
			assertException(ConcurrentModificationException.class, () -> it.next());
		}
	}
	
	public void test45() {
		c.add(e[4]);
		c.add(e[5]);
		c.clear();
		assertEquals(0,c.size());
		c.add(e[9]);
		assertEquals(1,c.size());
	}
	
	public void test46() {
		c.add(e[4]);
		c.add(e[6]);
		it = c.iterator();
		c.clear();
		if (failFast) {
			assertException(ConcurrentModificationException.class, () -> it.hasNext());
			assertException(ConcurrentModificationException.class, () -> it.next());
		}
		it = c.iterator();
		assertFalse(it.hasNext());
		assertException(NoSuchElementException.class,() -> it.next());
	}
	
	
	/// test5X: tests of iterator remove
	
	public void test50() {
		if (!hasRemove) return;
		c.add(e[1]);
		it = c.iterator();
		it.next();
		it.remove();
		assertFalse(it.hasNext());
		testcol(c,"{1} after remove(1)");
	}
	
	public void test51() {
		if (!hasRemove) return;
		c.add(e[8]);
		c.add(e[4]);
		it = c.iterator();
		it.next();
		it.remove();
		if (preserveOrder) {
			testcol(c,"{8,4} after remove(8)",e[4]);
		}
		assertTrue(it.hasNext());
		if (preserveOrder) {
			assertEquals(e[4],it.next());
		}
	}
	
	public void test52() {
		if (!hasRemove) return;
		c.add(e[8]);
		c.add(e[4]);
		it = c.iterator();
		it.next();
		it.next();
		it.remove();
		if (preserveOrder) {
			testcol(c,"{8,4} after remove(4)",e[8]);
		}
		assertFalse(it.hasNext());
	}
	
	public void test53() {
		if (!hasRemove) return;
		c.add(e[6]);
		c.add(e[1]);
		c.add(e[3]);
		it = c.iterator();
		E obj1 = it.next();
		it.remove();
		if (preserveOrder) {
			testcol(c,"{6,1,3} after remove(6)",e[1],e[3]);
		}
		assertTrue(it.hasNext());
		E obj2 = it.next();
		if (preserveOrder) {
			assertEquals(e[1],obj2);
		} else {
			assertFalse(obj2.equals(obj1));
		}
	}
	
	public void test54() {
		if (!hasRemove) return;
		c.add(e[6]);
		c.add(e[1]);
		c.add(e[3]);
		it = c.iterator();
		E obj1 = it.next();
		E obj2 = it.next();
		it.remove();
		if (preserveOrder) {
			testcol(c,"{6,1,3} after remove(1)",e[6],e[3]);
		}
		assertTrue(it.hasNext());
		E obj3 = it.next();
		if (preserveOrder) {
			assertEquals(e[3],obj3);
		} else {
			assertFalse(obj1.equals(obj3));
			assertFalse(obj2.equals(obj3));
		}
	}

	public void test55() {
		if (!hasRemove) return;
		c.add(e[6]);
		c.add(e[1]);
		c.add(e[3]);
		it = c.iterator();
		it.next();
		it.next();
		it.next();
		it.remove();
		if (preserveOrder) {
			testcol(c,"{6,1,3} after remove(3)",e[6],e[1]);
		} else {
			assertEquals(2,c.size());			
		}
		assertFalse(it.hasNext());	
	}

	public void test56() {
		if (!hasRemove) return;
		c.add(e[6]);
		c.add(e[1]);
		c.add(e[3]);
		it = c.iterator();
		E obj1 = it.next(); it.remove();
		E obj2 = it.next(); it.remove();
		if (preserveOrder) {
			testcol(c,"{6,1,3} after remove(6,1)",e[3]);
		}
		assertTrue(it.hasNext());	
		E obj3 = it.next();
		if (preserveOrder) {
			assertEquals(e[3],obj3);
		} else {
			assertFalse(obj1.equals(obj3));
			assertFalse(obj2.equals(obj3));
		}
	}

	public void test57() {
		if (!hasRemove) return;
		c.add(e[6]);
		c.add(e[1]);
		c.add(e[3]);
		it = c.iterator();
		it.next(); it.remove();
		it.next();
		it.next(); it.remove();
		if (preserveOrder) {
			testcol(c,"{6,1,3} after remove(6,3)",e[1]);
		}
		assertFalse(it.hasNext());	
	}

	public void test58() {
		if (!hasRemove) return;
		c.add(e[6]);
		c.add(e[1]);
		c.add(e[3]);
		it = c.iterator();
		it.next(); it.remove();
		it.next(); it.remove();
		it.next(); it.remove();
		testcol(c,"{6,1,3} after remove(6,1,3)");
		assertFalse(it.hasNext());	
	}

	public void test59() {
		if (!permitDuplicates || !preserveOrder || !hasRemove) return;
		c.add(e[3]);
		c.add(e[5]);
		c.add(e[3]);
		c.add(e[4]);
		it = c.iterator();
		it.next();
		it.next();
		it.next();
		it.remove();
		testcol(c,"{3,5,3,4} after removing SECOND 3",e[3],e[5],e[4]);
	}
	
	
	/// test6X: complex tests with remove
	
	public void test60() {
		if (!preserveOrder || !hasRemove) return;
		c.add(e[1]);
		c.add(e[2]);
		c.add(e[3]);
		c.add(e[4]);
		
		it = c.iterator();
		it.next();
		it.remove();
		assertTrue("Three more after 1 removed",it.hasNext());
		assertEquals("Next after 1 removed",e[2],it.next());
		testcol(c,"{X1,2,3,4}",e[2],e[3],e[4]);
		assertTrue("Two more after next() and 1 removed",it.hasNext());
		assertEquals("Next after next() after 1 removed",e[3],it.next());
		it.remove();
		testcol(c,"{X1,2,X3,4}",e[2],e[4]);
		assertTrue("One more after 1,3 removed",it.hasNext());
		assertEquals("Next after 1,3 removed",e[4],it.next());
		it.remove();
		testcol(c,"{X1,2,X3,X4}",e[2]);
		assertTrue("No more after 1,3,4 removed",!it.hasNext());
		
		it = c.iterator();
		it.next();
		it.remove();
		testcol(c,"all removed");
		assertTrue("No more after everything removed",!it.hasNext());
		
	}
	
	public void test61() {
		if (!preserveOrder || !hasRemove) return;
		
		c.add(e[1]);
		c.add(e[2]);
		c.add(e[3]);
		c.add(e[4]);
		
		it= c.iterator();
		it.next();
		it.remove();
		testcol(c,"{X1,2,3,4}",e[2],e[3],e[4]);
		it = c.iterator();
		it.next();
		it.remove();
		testcol(c,"{X1,X2,3,4}",e[3],e[4]);
		it = c.iterator();
		it.next();
		it.next();
		it.remove();
		testcol(c,"{X1,X2,3,X4}",e[3]);
		it = c.iterator();
		it.next();
		it.remove();
		testcol(c,"all removed again");
	}

	
	/// test7X: remove errors
	
	public void test70()
	{
		if (!hasRemove) return;
		it = c.iterator();
		assertException(IllegalStateException.class,() -> it.remove());
	}
	
	public void test71()
	{
		if (!hasRemove) return;
		c.add(e[5]);
		it = c.iterator();
		assertException(IllegalStateException.class,() -> it.remove());
		testcol(c,"still {5}",e[5]);
	}
	
	public void test72()
	{
		if (!hasRemove) return;
		c.add(e[9]);
		it = c.iterator();
		it.next();
		it.remove();
		assertException(NoSuchElementException.class, () -> it.next());
		testcol(c,"{X9}");
	}
	
	public void test73()
	{
		if (!hasRemove) return;
		c.add(e[0]);
		c.add(e[4]);
		it = c.iterator();
		it.next();
		it.remove();
		assertException(IllegalStateException.class,() -> it.remove());
		if (preserveOrder) {
			testcol(c,"{X0,4}",e[4]);
		}
	}
	
	public void test74()
	{
		if (!hasRemove) return;
		c.add(e[9]);
		c.add(e[8]);
		it = c.iterator();
		Iterator<E> it2 = c.iterator();
		it.next();
		it2.next();
		it.remove();
		if (failFast) {
			assertException(ConcurrentModificationException.class, () -> it2.hasNext());
			assertException(ConcurrentModificationException.class, () -> it2.next());
			assertException(ConcurrentModificationException.class, () -> it2.remove());
		}
		assertTrue("after remove() after first remove(), hasNext() should still be true",it.hasNext());
		if (preserveOrder) {
			testcol(c,"{X9,8}",e[8]);
		}
	}
	
	public void test75() {
		if (!hasRemove) return;
		c.add(e[1]);
		c.add(e[8]);
		it = c.iterator();
		it.next();
		it.remove();
		
		Iterator<E> it2 = c.iterator();
		it.next();
		it.remove();
		
		if (failFast) {
			assertException(ConcurrentModificationException.class, () -> it2.hasNext());
			assertException(ConcurrentModificationException.class, () -> it2.next());
			assertException(ConcurrentModificationException.class, () -> it2.remove());
		}

		assertException(IllegalStateException.class, () -> it.remove());
		
		assertTrue("after remove() after second remove(), hasNext() should still be false",(!it.hasNext()));
		testcol(c,"{X1,X8}");
	}

	
	/// testing contains/remove
	
	public void test80() {
		assertFalse(c.contains(e[0]));
	}
	
	public void test81() {
		c.add(e[1]);
		assertFalse(c.contains(e[0]));
		assertTrue(c.contains(e[1]));
	}
	
	public void test82() {
		c.add(e[2]);
		c.add(e[3]);
		assertFalse(c.contains(e[1]));
		assertTrue(c.contains(e[2]));
		assertFalse(c.contains(e[0]));
		assertTrue(c.contains(e[3]));
	}
	
	public void test83() {
		c.add(e[4]);
		c.add(e[5]);
		c.add(e[6]);
		assertFalse(c.contains(e[3]));
		assertTrue(c.contains(e[4]));
		assertFalse(c.contains(e[2]));
		assertTrue(c.contains(e[5]));
		assertFalse(c.contains(e[7]));
		assertTrue(c.contains(e[6]));
	}

	public void test84() {
		c.add(e[7]);
		c.add(e[8]);
		c.add(e[9]);
		assertFalse(c.contains(null));
		assertFalse(c.contains(new Object()));
		assertFalse(c.contains(c));
		assertFalse(c.contains("Eleph4nt"));
	}
	
	public void test85() {
		if (!hasRemove) return;
		assertFalse(c.remove(e[0]));
	}
	
	public void test86() {
		if (!hasRemove) return;
		c.add(e[1]);
		assertFalse(c.remove(e[0]));
		assertTrue(c.remove(e[1]));
		assertFalse(c.remove(e[1]));
	}
	
	public void test87() {
		if (!hasRemove) return;
		c.add(e[2]);
		c.add(e[3]);
		assertFalse(c.remove(e[1]));
		assertTrue(c.remove(e[3]));
		assertFalse(c.contains(e[3]));
		assertTrue(c.remove(e[2]));
	}
	
	public void test88() {
		if (!hasRemove) return;
		c.add(e[4]);
		c.add(e[5]);
		c.add(e[6]);
		assertTrue(c.remove(e[5]));
		if (preserveOrder) {
			testcol(c,"{4,X5,6}",e[4],e[6]);
		}
		assertFalse(c.remove(c));
		assertFalse(c.remove("Eleph4nt"));
		assertFalse(c.remove(null));
	}
	
	public void test89() {
		if (!hasRemove) return;
		if (!permitNulls) return;
		c.add(e[7]);
		c.add(null);
		c.add(e[8]);
		assertEquals(3,c.size());
		assertTrue(c.remove(null));
		assertEquals(2,c.size());
	}
}
