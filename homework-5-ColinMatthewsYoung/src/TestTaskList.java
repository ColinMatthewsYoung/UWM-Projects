import edu.uwm.cs351.Nondiscrimination;
import edu.uwm.cs351.Task;
import edu.uwm.cs351.TaskList;
import junit.framework.TestCase;


public class TestTaskList extends TestCase {
	
	private TaskList tList;
	private Task t1, t2, t3, t4, t5, t6;
	
	@Override
	protected void setUp() {
		tList = new TaskList(Nondiscrimination.getInstance());

		t1 = new Task("Task 1", 2, 100, 100);
		t2 = new Task("Task 2", 3, 300, 200);
		t3 = new Task("Task 3", 7, 600, 300);
		t4 = new Task("Task 4", 11, 1000, 900);
		t5 = new Task("Task 5", 17, 1500, 800);
		t6 = new Task("Task 6", 19, 1600, 700);
	}
	
	public void testA() {
		tList.add(t3);
		// tList: [t3]
		assertTrue("dummy <- t3?",t3.getPrevious() != null);
		assertTrue("t3 -> dummy?", t3.getNext() != null);
		
		tList.add(t2); // add from tail!
		// tList: [t3,t2]
		assertTrue("dummy <- t3?",t3.getPrevious() != null);
		assertSame("t3 -> t2 ?",t2,t3.getNext());
		assertSame("t3 <- t2 ?",t3,t2.getPrevious());
		
		tList.add(t1);
		// tList: [t3, t2, t1]
		assertSame("t3 -> t2?",t2,t3.getNext());
		assertSame("t3 <- t2?",t3,t2.getPrevious());
		assertSame("t2 -> t1?",t1,t2.getNext());
		assertSame("t2 <- t1?",t2,t1.getPrevious());
	}

	public void testB() {
		assertEquals(0,tList.performAll(1500));
	}
	
	public void testC() {
		tList.add(t1);
		
		// tList: [t1]
		assertEquals(0,tList.performAll(99));
		checkAllRemoved();
	}
	
	public void testD() {
		tList.add(t1);
		
		// tList: [t1]
		assertEquals(2,tList.performAll(100));
		checkAllRemoved();
	}
	
	public void testE() {
		tList.add(t1);
		
		// tList: [t1]
		assertEquals(2,tList.performAll(101));
		checkAllRemoved();
	}
	
	public void testF() {
		tList.add(t2);
		
		// tList: [t2]
		assertEquals(3,tList.performAll(200));
		checkAllRemoved();
	}
	
	public void testG() {
		tList.add(t1);
		tList.add(t2);
		tList.add(t3);
		
		// tList: [t1, t2, t3]
		assertEquals(12,tList.performAll(600));
		checkAllRemoved();
	}
	
	public void testH() {
		tList.add(t1);
		tList.add(t2);
		tList.add(t3);
		
		// tList: [t1, t2, t3]
		assertEquals(5,tList.performAll(590));
		checkAllRemoved();
	}

	public void testI() {
		tList.add(t3);
		tList.add(t2);
		tList.add(t1);
		
		// tList: [t3, t2, t1]
		assertEquals(7,tList.performAll(600));
		checkAllRemoved();
	}
	
	public void testJ() {
		tList.add(t4);
		tList.add(t6);
		tList.add(t5);
		
		// tList: [t4, t6, t5]
		assertEquals("last task shouldn't complete",30,tList.performAll(3000));
		checkAllRemoved();
	}
	
	public void testK() {
		tList.add(t6);
		tList.add(t5);
		tList.add(t4);
		
		// tList: [t6, t5, t4]
		assertEquals("last task shouldn't complete",36,tList.performAll(3000));
		checkAllRemoved();
	}
	
	public void testL() {
		tList.add(t4);
		tList.add(t5);
		tList.add(t6);
		assertEquals("only 2nd task should complete",17,tList.performAll(800));
		checkAllRemoved();
	}

	public void testM() {
		tList.add(t4);
		tList.add(t5);
		tList.add(t6);
		assertEquals("only 2nd task shouldn't complete",30,tList.performAll(3000));
		checkAllRemoved();
	}
	
	public void testN(){
		tList.add(t1);
		tList.add(t2);
		tList.add(t3);
		tList.add(t4);
		tList.add(t5);
		tList.add(t6);
		assertEquals("1,2,3,5 should complete; t4, t6 shouldn't",29,tList.performAll(3000));
		checkAllRemoved();
	}
	
	public void testO() {
		tList.add(t6);
		tList.add(t5);
		tList.add(t4);
		tList.add(t3);
		tList.add(t2);
		tList.add(t1);
		assertEquals("6 and 5 should complete; other ones shouldn't",36,tList.performAll(3000));
		checkAllRemoved();
		
	}
	
	private void checkAllRemoved() {
		assertTrue("t1 removed from list",t1.getPrevious() == null && t1.getNext() == null);
		assertTrue("t2 removed from list",t2.getPrevious() == null && t2.getNext() == null);
		assertTrue("t3 removed from list",t3.getPrevious() == null && t3.getNext() == null);
		assertTrue("t4 removed from list",t4.getPrevious() == null && t4.getNext() == null);
		assertTrue("t5 removed from list",t5.getPrevious() == null && t5.getNext() == null);
		assertTrue("t6 removed from list",t6.getPrevious() == null && t6.getNext() == null);
	}


}
