import java.util.Comparator;
import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Nondiscrimination;
import edu.uwm.cs351.RewardSeeker;
import edu.uwm.cs351.Task;
import edu.uwm.cs351.TyrannyOfTheUrgent;
import edu.uwm.cs351.ValueHunter;


public class TestTask extends LockedTestCase {
	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (RuntimeException ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}
	
	private Comparator<Task> c;
	private Task t1,t2,t3,t4,t5,t6,t7,t8;
	
	private static void testTasks(String operation, Task... expected) {
		for (int i=0; i < expected.length; ++i) {
			Task t = expected[i];
			Task expectedPrev =                 (i == 0)? null: expected[i-1];
			Task expectedNext = (i == expected.length-1)? null: expected[i+1];
			
			assertSame("After "+operation+", "+t+".prev was incorrect", expectedPrev, t.getPrevious());
			assertSame("After "+operation+", "+t+".next was incorrect", expectedNext, t.getNext());
		}
	}
	
	public String getName(Task t){
		     if (t == null) return "null";
	    else if (t == t1)   return "t1";
		else if (t == t2)   return "t2";
		else if (t == t3)   return "t3";
		else if (t == t4)   return "t4";
		else if (t == t5)   return "t5";
		   else             return "t6";
	}
	
	public void test0(){
		//Comparator Used: Nondiscrimination
		c = Nondiscrimination.getInstance();
		
		t1 = new Task("t1", 5, 500, 50);
		t2 = new Task("t2", 2, 200, 20);
		t3 = new Task("t3", 8, 800, 80);
		
		//For these locked tests, options are... t1, t2, t3, null
		assertEquals("t1.getNext() == ____", Ts(1702893884),  getName(t1.getNext()));
		
		t1.addInPriority(t2, c);
		assertEquals("t1.getNext() == ____", Ts(1604484432),  getName(t1.getNext()));
		
		t1.addInPriority(t3, c);
		assertEquals("t1.getNext() == ____", Ts(483087791),   getName(t1.getNext()));
		assertEquals("t3.getNext() == ____", Ts(536755138),   getName(t3.getNext()));
		assertEquals("t2.getNext() == ____", Ts(1929370452),  getName(t2.getNext()));
	}
	
	public void test1(){
		//Comparator Used: RewardSeeker
		c = RewardSeeker.getInstance();
		
		//Constructor args are:
		//       Task(  name        reward      deadline   duration)
		t1 = new Task(  "t1",         10,         600,        110  );
		t2 = new Task(  "t2",         20,         200,        100  );
		t3 = new Task(  "t3",         20,         400,        150  );
		
		//For these locked tests, options are... t1, t2, t3, null
		t1.addInPriority(t3, c);
		assertEquals("t1.getNext() == ____", Ts(659658959),   getName(t1.getNext()));
		assertEquals("t3.getNext() == ____", Ts(1753483005),     getName(t3.getNext()));
		
		t1.addInPriority(t2, c);
		assertEquals("    t3.getNext() == ____", Ts(1457613836),      getName(t3.getNext()));
		assertEquals("t3.getPrevious() == ____", Ts(1569063673),  getName(t3.getPrevious()));
		assertEquals("    t2.getNext() == ____", Ts(889684718),       getName(t2.getNext()));
		assertEquals("    t1.getNext() == ____", Ts(1432254084),      getName(t1.getNext()));
	}
	
	public void test2() {
		c = ValueHunter.getInstance();
		t1 = new Task("t1", 10, 100, 10);
		t2 = new Task("t2", 10, 100, 10);
		t3 = new Task("t3", 10, 100, 10);
		t4 = new Task("t4", 10, 100, 10);
		
		t1.addInPriority(t2, c);
		testTasks("t1.addInpriority(t2)", t1, t2);
		
		t2.addInPriority(t3, c);
		testTasks("t2.addInPriority(t3)", t1, t3, t2);
		
		t1.addInPriority(t4, c);
		testTasks("t1.addInPriority(t4)", t1, t4, t3, t2);
	}
	
	public void test3() {
		c = Nondiscrimination.getInstance();
		t1 = new Task("Task 1", 5, 500, 50);
		t2 = new Task("Task 2", 2, 200, 20);
		t3 = new Task("Task 3", 8, 800, 80);
		t4 = new Task("Task 4", 5, 500, 50);
		
		t1.addInPriority(t2, c);
		testTasks("t1.addInPriority(t2, ND)", t1,t2);
		
		t2.addInPriority(t3, c);
		testTasks("t2.addInPriority(t3, ND)", t1,t3,t2);
		
		t1.addInPriority(t4, c);
		testTasks("t1.addInPriority(t4, ND)", t1,t4,t3,t2);		
	}
	
	public void test4() {
		c = RewardSeeker.getInstance();
		t1 = new Task("Task 1", 5, 500, 50);
		t2 = new Task("Task 2", 2, 200, 20);
		t3 = new Task("Task 3", 8, 800, 80);
		t4 = new Task("Task 4", 5, 100, 10);
		t5 = new Task("Task 5", 6, 900, 90);
		
		t1.addInPriority(t2, c);
		testTasks("t1.addInPriority(t2, RS)", t1,t2);
		
		t1.addInPriority(t3, c);
		testTasks("t1.addInPriority(t3, RS)", t3,t1,t2);
		
		t2.addInPriority(t4, c);
		testTasks("t2.addInPriority(t4, RS)", t3,t1,t4,t2);
		
		t1.addInPriority(t5, c);
		testTasks("t1.addInPriority(t5, RS)", t3,t5,t1,t4,t2);
	}
	
	public void test5() {
		c =  TyrannyOfTheUrgent.getInstance();
		t1 = new Task("Task 1", 5, 500, 50);
		t2 = new Task("Task 2", 2, 200, 20);
		t3 = new Task("Task 3", 3, 300, 30);
		t4 = new Task("Task 4", 3, 500, 10);
		t5 = new Task("Task 5", 4, 500, 90);
		t6 = new Task("Task 6", 3, 500, 30);
		t7 = new Task("Task 7", 2, 500, 20);
		t8 = new Task("Task 8", 3, 200, 10);
		
		t1.addInPriority(t2, c);
		testTasks("t1.addInPriority(t1, TotU)", t2,t1);

		t1.addInPriority(t3, c);
		testTasks("t1.addInPriority(t2, TotU)", t2,t3,t1);
		
		t1.addInPriority(t4, c);
		testTasks("t1.addInPriority(t3, TotU)", t2,t3,t1,t4);
		
		t1.addInPriority(t5, c);
		testTasks("t1.addInPriority(t4, TotU)", t2,t3,t1,t5,t4);	
		
		t2.addInPriority(t6, c);
		testTasks("t2.addInPriority(t5, TotU)", t2,t3,t1,t5,t6,t4);	

		t1.addInPriority(t7, c);
		testTasks("t1.addInPriority(t6, TotU)", t2,t3,t1,t5,t6,t4,t7);	
		
		t7.addInPriority(t8, c);
		testTasks("t7.addInPriority(t7, TotU)", t8,t2,t3,t1,t5,t6,t4,t7);	
	}
	
	public void test6() {
		c =  ValueHunter.getInstance();
		t1 = new Task("Task 1", 5, 500, 50);
		t2 = new Task("Task 2", 4, 500, 10);
		t3 = new Task("Task 3", 3, 500, 10);
		t4 = new Task("Task 4", 2, 600, 20);
		t5 = new Task("Task 5", 2, 500, 20);
		
		t1.addInPriority(t2, c);
		testTasks("t1.addInPriority(t1, VH)", t2,t1);
		
		t2.addInPriority(t3, c);
		testTasks("t2.addInPriority(t3, VH)", t2,t3,t1);

		t1.addInPriority(t4, c);
		testTasks("t1.addInPriority(t4, VH)", t2,t3,t1,t4);
		
		t4.addInPriority(t5, c);
		testTasks("t4.addInPriority(t5, VH)", t2,t3,t1,t5,t4);
	}

	public void test7() {
		c = Nondiscrimination.getInstance();
		t1 = new Task("Task 1", 5, 500, 50);
		t2 = new Task("Task 2", 4, 500, 10);
		t3 = new Task("Task 3", 3, 500, 10);

		
		t1.addInPriority(t2, c);

		assertException(IllegalArgumentException.class, () -> t3.addInPriority(t1, c) );
		assertException(IllegalArgumentException.class, () -> t3.addInPriority(t2, c) );
		assertException(IllegalArgumentException.class, () -> t3.addInPriority(t3, c) );

	}
	
	public void test8() {
		c = Nondiscrimination.getInstance();
		t1 = new Task("Task 1", 1, 100, 10);
		t2 = new Task("Task 2", 2, 200, 20);
		t3 = new Task("Task 3", 3, 300, 30);
		t4 = new Task("Task 4", 4, 400, 40);
		t5 = new Task("Task 5", 5, 500, 50);
		
		t1.addInPriority(t5, c);
		t1.addInPriority(t2, c);
		t2.addInPriority(t3, c);
		t3.addInPriority(t4, c);
		
		testTasks("built list with ND comparator", t1,t2,t3,t4,t5);
		
		t3.remove();
		testTasks("removed middle node", t3);
		testTasks("removed middle node", t1,t2,t4,t5);
		
		t2.remove();
		testTasks("removed middle node again", t1,t4,t5);
	}
	
	public void test9() {
		c = Nondiscrimination.getInstance();
		t1 = new Task("Task 1", 1, 100, 10);
		t2 = new Task("Task 2", 2, 200, 20);
		t3 = new Task("Task 3", 3, 300, 30);
		t4 = new Task("Task 4", 4, 400, 40);
		t5 = new Task("Task 5", 5, 500, 50);
		t6 = new Task("Task 6", 6, 600, 60);
		
		t1.addInPriority(t5, c);
		t1.addInPriority(t2, c);
		t2.addInPriority(t3, c);
		t3.addInPriority(t4, c);

		testTasks("built list with ND comparator", t1,t2,t3,t4,t5);

		assertException(IllegalStateException.class, () -> t1.remove());		
		assertException(IllegalStateException.class, () -> t5.remove());
		assertException(IllegalStateException.class, () -> t6.remove());

	}
}
