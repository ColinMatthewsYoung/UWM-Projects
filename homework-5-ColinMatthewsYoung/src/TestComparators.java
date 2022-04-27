import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Nondiscrimination;
import edu.uwm.cs351.RewardSeeker;
import edu.uwm.cs351.Smart;
import edu.uwm.cs351.Task;
import edu.uwm.cs351.TaskGenerator;
import edu.uwm.cs351.TyrannyOfTheUrgent;
import edu.uwm.cs351.ValueHunter;

public class TestComparators extends LockedTestCase {
	
	protected Comparator<Task> c;
	protected List<Task> tasks;
	protected Task t0, t1, t2, t3,t4, t5;
	protected Task best;
	protected Task worst;
	
	private static final int RANDOM = 100; // Warning: O(n^3)
	@Override
	protected void setUp() {
		best = new Task("best", Task.MAX_REWARD, 0, 0);
		worst = new Task("worst", 0, Task.MAX_TIME, Task.MAX_DURATION);
		t0 = new Task("Tast 0", Task.MAX_REWARD,0,0);
		t1 = new Task("Task 1", 1, 100, 10);
		t2 = new Task("Task 2", 2, 200, 10);
		t3 = new Task("Task 3", 1, 100, 5);
		t4 = new Task("Task 4", 2, 100, 5);
		t5 = new Task("Task 5", 3, 200, 15);
		
		tasks = new ArrayList<>();
		tasks.addAll(Arrays.asList(t0,t1,t2,t3,t4,t5));
		
		TaskGenerator gen = new TaskGenerator(10,10,100);
		for (int i=0; i < RANDOM; ++i) {
			tasks.add(gen.makeRandomTask());
		}
	}
	
	protected void testComparator(Comparator<Task> c, Task[]... taskClasses) {
		for (int i=0; i < taskClasses.length; ++i) {
			for (Task t1 : taskClasses[i]) {
				for (int j=0; j < taskClasses.length; ++j) {
					for (Task t2 : taskClasses[j]) {
						try {
							int expected = i-j;
							int result = c.compare(t1, t2);
							testComparison(c + "(" + t1 + "," + t2 + ")", expected, result);
						} catch (RuntimeException ex) {
							assertFalse(c + "(" + t1 + "," + t2 + ") threw exception " +ex, true);
						}
					}
				}
			}
		}
	}

	protected void testComparison(String explain, int expected, int result) {
		if (result < 0 && expected < 0 || result > 0 && expected > 0 || result == 0 && expected == 0) {
			assertTrue(true);
		} else {
			assertFalse(explain + "=" + result + ", not " + asString(expected) + " 0",true);
		}
	}
	
	/**
	 * Convert a comparison result into an operator string.
	 * These strings can safely be compared with ==.
	 * @param c comparison result, less than, equal to or greater than zero
	 * @return one of the three strings "<". "=" or ">".
	 */
	protected String asString(int c) {
		if (c == 0) return "=";
		else if (c < 0) return "<";
		else return ">";
	}
	
	protected void assertNotPerverse(Comparator<Task> c) {
		String n = c.toString();
		for (Task t : tasks) {
			assertTrue(n + "(" + t + "," + t + ") ", c.compare(t, t) == 0);
			assertTrue(n + "(best," + t + ") ", c.compare(best,t) <= 0);
			assertTrue(n + "(" + t + ",best) ", c.compare(t,best) >= 0);
			assertTrue(n + "(worst," + t + ") ", c.compare(worst,t) >= 0);
			assertTrue(n + "(" + t + ",worst) ", c.compare(t,worst) <= 0);
		}
	}
	
	protected void assertReflexive(Comparator<Task> c) {
		String n = c.toString();
		for (Task t : tasks) {
			testComparison(n + "(" + t + "," + t + ")", 0, c.compare(t, t));
		}
	}
	
	protected void assertAntiSymmetric(Comparator<Task> c) {
		String n = c.toString();
		for (Task t1 : tasks) {
			for (Task t2 : tasks) {
				int c12 = c.compare(t1, t2);
				int c21 = c.compare(t2, t1);
				if (c12 != -c21) {
					testComparison(n + "(" + t1 + "," + t2 + ") = " + c12 + ", but " + 
				                   n + "(" + t2 + "," + t1 + ")", -c12, c21);
				}
			}
		}
	}
	
	protected void assertTransitive(Comparator<Task> c) {
		String n = c.toString();
		for (Task t1 : tasks) {
			for (Task t2 : tasks) {
				int c12 = c.compare(t1, t2);
				for (Task t3 : tasks) {
					int c23 = c.compare(t2, t3);
					if (c12 < 0 && c23 > 0) continue;
					if (c12 > 0 && c23 < 0) continue;
					int expt13 = c12;
					if (c23 != 0) expt13 = c23;
					int c13 = c.compare(t1,  t3);
					if (asString(c13) != asString(expt13)) {
						testComparison(
								n + "(" + t1 + "," + t2 + ") = " + c12 + ", and " +
								n + "(" + t2 + "," + t3 + ") = " + c23 + ", but " +
								n + "(" + t1 + "," + t3 + ")", expt13, c13);
					}
				}
			}
		}
	}
	
	
	// Locked Tests
	
	public void test() {
		testND(false);
		testRS(false);
		testTU(false);
	}
	
	private void testND(boolean ignored){
		//Comparator Used: Non-discrimination: no preference
		c = Nondiscrimination.getInstance();
		
		//Constructor arguments are:
		//            Task( name        reward      deadline   duration)
		Task t1 = new Task("best",  Task.MAX_REWARD,    0,          0  );
		Task t2 = new Task("Task 2",      10,         600,        110  );
		Task t3 = new Task("Task 3",      20,         200,        100  );
		Task t4 = new Task("Task 4",      30,         400,        150  );
		
		// For unlocking Comparator tests...
		// If c.compare(a, b) > 0 then enter: >
		// If c.compare(a, b) = 0 then enter: =
		// If c.compare(a, b) < 0 then enter: <
		
		assertEquals("Task 2 _ Task 3", Ts(1686457190),   asString(c.compare(t2, t3)));
		assertEquals("Task 3 _ Task 4", Ts(535481914),    asString(c.compare(t3, t4)));
		assertEquals(" best  _ Task 2", Ts(778589714),    asString(c.compare(t1, t2)));
	}
	
	private void testRS(boolean ignored){
		//Comparator Used: RewardSeeker: preference based only on reward
		c = RewardSeeker.getInstance();
		
		//Constructor args are:
		//            Task( name        reward      deadline   duration)
		Task t1 = new Task("best",  Task.MAX_REWARD,    0,         0   );
		Task t2 = new Task("Task 2",      15,         500,       340   );
		Task t3 = new Task("Task 3",      30,         200,       110   );
		Task t4 = new Task("Task 4",      30,         500,       150   );
		
		// For unlocking Comparator tests...
		// If c.compare(a, b) > 0 then enter: >
		// If c.compare(a, b) = 0 then enter: =
		// If c.compare(a, b) < 0 then enter: <
		
		assertEquals("Task 2 _ Task 3", ">",    asString(c.compare(t2, t3)));
		assertEquals("Task 3 _ Task 4", Ts(395240224),    asString(c.compare(t3, t4)));
		assertEquals("Task 4 _ Task 2", "<",   asString(c.compare(t4, t2)));
		assertEquals(" best  _ Task 4", "<",   asString(c.compare(t1, t4)));
	}
	
	private void testTU(boolean ignored){
		//Comparator Used: TyrannyOfTheUrgent
		// Prefer earlier deadlines.
		// For tasks with equal deadline, compare based on reward.
		c = TyrannyOfTheUrgent.getInstance();
		
		//Constructor args are:
		//            Task( name       reward      deadline   duration)
		Task t1 = new Task("best", Task.MAX_REWARD,    0,         0   );
		Task t2 = new Task("Task 2",      2,          10,         5   );
		Task t3 = new Task("Task 3",     16,         100,       100   );
		Task t4 = new Task("Task 4",     15,         100,        90   );
		Task t5 = new Task("Task 5",     15,         100,        70   );
		
		// For unlocking Comparator tests...
		// If c.compare(a, b) > 0 then enter: >
		// If c.compare(a, b) = 0 then enter: =
		// If c.compare(a, b) < 0 then enter: <
		
		assertEquals("Task 2 _ Task 3", "<",    asString(c.compare(t2, t3)));
		assertEquals("Task 3 _ Task 4", "<",    asString(c.compare(t3, t4)));
		assertEquals("Task 4 _ Task 5", Ts(2000650588),    asString(c.compare(t4, t5)));
		assertEquals("Task 2 _  best ", ">",    asString(c.compare(t2, t1)));
	}
	
	
	/// Nondiscrimination tests
	
	public void testND0() {
		c = Nondiscrimination.getInstance();
		testComparison("ND(0,1)", 0, c.compare(t0, t1));
		testComparison("ND(1,2)", 0, c.compare(t1, t2));
		testComparison("ND(2,3)", 0, c.compare(t2, t3));
		testComparison("ND(3,4)", 0, c.compare(t3, t4));
		testComparison("ND(4,5)", 0, c.compare(t4, t5));
	}
		
	public void testND6() {
		assertNotPerverse(Nondiscrimination.getInstance());
	}
	
	public void testND7() {
		assertReflexive(Nondiscrimination.getInstance());
	}
	
	public void testND8() {
		assertAntiSymmetric(Nondiscrimination.getInstance());
	}
	
	public void testND9() {
		assertTransitive(Nondiscrimination.getInstance());
	}
	
	
	/// Reward Seeker tests
	
	public void testRS0() {
		Comparator<Task> c = RewardSeeker.getInstance();
		Task t1 = new Task("T1", 10, 10, 10);
		Task t2 = new Task("T2", 10, Task.MAX_TIME, Task.MAX_DURATION);
		Task t3 = new Task("T3", 10, 11, 11);
		
		testComparison("RW(" + t1 + "," + t2 + ")", 0, c.compare(t1,  t2));
		testComparison("RW(" + t1 + "," + t3 + ")", 0, c.compare(t1,  t3));
	}
	
	public void testRS1() {
		Comparator<Task> c = RewardSeeker.getInstance();
		Task t1 = new Task("T1", 10, 10, 10);
		Task t2 = new Task("T2", 11, Task.MAX_TIME, Task.MAX_DURATION);
		Task t3 = new Task("T3", 11, 11, 11);
		
		testComparison("RW(" + t1 + "," + t2 + ")", 1, c.compare(t1,  t2));
		testComparison("RW(" + t1 + "," + t3 + ")", 1, c.compare(t1,  t3));
	}
	
	public void testRS2() {
		Comparator<Task> c = RewardSeeker.getInstance();
		Task t1 = new Task("T1", 10, 10, 10);
		Task t2 = new Task("T2", 9, 0, 0);
		Task t3 = new Task("T3", 9, 1, 1);
		
		testComparison("RW(" + t1 + "," + t2 + ")", -1, c.compare(t1,  t2));
		testComparison("RW(" + t1 + "," + t3 + ")", -1, c.compare(t1,  t3));
	}
	
	public void testRS3() {
		Comparator<Task> c = RewardSeeker.getInstance();
		Task t1 = new Task("T1", 0, 10, 10);
		Task t2 = new Task("T2", 0, 0, 0);
		Task t3 = new Task("T3", 0, 20, 20);
		
		testComparison("RW(" + t1 + "," + t2 + ")", 0, c.compare(t1,  t2));
		testComparison("RW(" + t1 + "," + t3 + ")", 0, c.compare(t1,  t3));
		
	}
		
	public void testRS6() {
		assertNotPerverse(RewardSeeker.getInstance());
	}
	
	public void testRS7() {
		assertReflexive(RewardSeeker.getInstance());
	}
	
	public void testRS8() {
		assertAntiSymmetric(RewardSeeker.getInstance());
	}
	
	public void testRS9() {
		assertTransitive(RewardSeeker.getInstance());
	}
	

	/// Smart tests
	
	// We don't know how you implemented the "Smart" comparator.
	// But it should be non-perverse and follow rules of comparators
	// AND should not be the same as any existing comparator
	
	public void testSM0() {
		assertEquals("Smart", Smart.getInstance().toString());
	}
	
	public void testSM5() {
		c = Smart.getInstance();
		outer:
		for (Comparator<Task> other : Arrays.asList(Nondiscrimination.getInstance(), RewardSeeker.getInstance(), TyrannyOfTheUrgent.getInstance(), ValueHunter.getInstance())) {
			for (Task t1 : tasks) {
				for (Task t2 : tasks) {
					String others = asString(other.compare(t1, t2));
					String s = asString(c.compare(t1, t2));
					if (others != s) continue outer;
				}
			}
			assertFalse("Smart comparator appears the same as " + other, true);
		}		
	}
	
	public void testSM6() {
		assertNotPerverse(Smart.getInstance());
	}
	
	public void testSM7() {
		assertReflexive(Smart.getInstance());
	}
	
	public void testSM8() {
		assertAntiSymmetric(Smart.getInstance());
	}
	
	public void testSM9() {
		assertTransitive(Smart.getInstance());
	}
	

	/// Tyranny of the Urgent tests

	public void testTU0() {
		Comparator<Task> c = TyrannyOfTheUrgent.getInstance();
		Task t1 = new Task("T1", 10, 100, 10);
		Task t2 = new Task("T2", 10, 100, Task.MAX_DURATION);
		Task t3 = new Task("T3", 10, 100, 20);
		
		testComparison("TU(" + t1 + "," + t2 + ")", 0, c.compare(t1,  t2));
		testComparison("TU(" + t1 + "," + t3 + ")", 0, c.compare(t1,  t3));
	}

	public void testTU1() {
		Comparator<Task> c = TyrannyOfTheUrgent.getInstance();
		Task t1 = new Task("T1", 10, 100, 10);
		Task t2 = new Task("T2", 9, 100, 0);
		Task t3 = new Task("T3", 9, 100, 5);
		
		testComparison("TU(" + t1 + "," + t2 + ")", -1, c.compare(t1,  t2));
		testComparison("TU(" + t1 + "," + t3 + ")", -1, c.compare(t1,  t3));
	}

	public void testTU2() {
		Comparator<Task> c = TyrannyOfTheUrgent.getInstance();
		Task t1 = new Task("T1", 10, 100, 10);
		Task t2 = new Task("T2", 11, 100, Task.MAX_DURATION);
		Task t3 = new Task("T3", 11, 100, 15);
		
		testComparison("TU(" + t1 + "," + t2 + ")", 1, c.compare(t1,  t2));
		testComparison("TU(" + t1 + "," + t3 + ")", 1, c.compare(t1,  t3));
	}

	public void testTU3() {
		Comparator<Task> c = TyrannyOfTheUrgent.getInstance();
		Task t1 = new Task("T1", 10, 100, Task.MAX_DURATION);
		Task t2 = new Task("T2", Task.MAX_REWARD, 101, 0);
		Task t3 = new Task("T3", 15, 110, 10);
		
		testComparison("TU(" + t1 + "," + t2 + ")", -1, c.compare(t1,  t2));
		testComparison("TU(" + t1 + "," + t3 + ")", -1, c.compare(t1,  t3));
	}

	public void testTU4() {
		Comparator<Task> c = TyrannyOfTheUrgent.getInstance();
		Task t1 = new Task("T1", 10, 100, 10);
		Task t2 = new Task("T2", 0, 99, Task.MAX_DURATION);
		Task t3 = new Task("T3", 5, 90, 100);
		
		testComparison("TU(" + t1 + "," + t2 + ")", 1, c.compare(t1,  t2));
		testComparison("TU(" + t1 + "," + t3 + ")", 1, c.compare(t1,  t3));
	}

	public void testTU5() {
		Comparator<Task> c = TyrannyOfTheUrgent.getInstance();
		Task t1 = new Task("T1", 0, 0, 10);
		Task t2 = new Task("T2", 1, 0, Task.MAX_DURATION);
		Task t3 = new Task("T3", 0, 0, 0);
		
		testComparison("TU(" + t1 + "," + t2 + ")", 1, c.compare(t1,  t2));
		testComparison("TU(" + t1 + "," + t3 + ")", 0, c.compare(t1,  t3));		
	}
	
	public void testTU6() {
		assertNotPerverse(TyrannyOfTheUrgent.getInstance());
	}
	
	public void testTU7() {
		assertReflexive(TyrannyOfTheUrgent.getInstance());
	}
	
	public void testTU8() {
		assertAntiSymmetric(TyrannyOfTheUrgent.getInstance());
	}
	
	public void testTU9() {
		assertTransitive(TyrannyOfTheUrgent.getInstance());
	}
	
	
	/// Value Hunter tests
	// Since we implemented the value hunter for you, 
	// these tests should all pass
	
	public void testVH0() {
		Comparator<Task> c = ValueHunter.getInstance();
		Task t1 = new Task("T1", 10, 100, 5);
		Task t2 = new Task("T2", 2, 100, 1);
		Task t3 = new Task("T3", 20, 100, 10);
		
		testComparison("VH(" + t1 + "," + t2 + ")", 0, c.compare(t1,  t2));
		testComparison("VH(" + t1 + "," + t3 + ")", 0, c.compare(t1,  t3));
	}
	
	public void testVH1() {
		Comparator<Task> c = ValueHunter.getInstance();
		Task t1 = new Task("T1", 10, 100, 5);
		Task t2 = new Task("T2", 2, 101, 1);
		Task t3 = new Task("T3", 20, 110, 10);
		
		testComparison("VH(" + t1 + "," + t2 + ")", -1, c.compare(t1,  t2));
		testComparison("VH(" + t1 + "," + t3 + ")", -1, c.compare(t1,  t3));
	}
	
	public void testVH2() {
		Comparator<Task> c = ValueHunter.getInstance();
		Task t1 = new Task("T1", 10, 100, 5);
		Task t2 = new Task("T2", 2, 99, 1);
		Task t3 = new Task("T3", 20, 50, 10);
		
		testComparison("VH(" + t1 + "," + t2 + ")", 1, c.compare(t1,  t2));
		testComparison("VH(" + t1 + "," + t3 + ")", 1, c.compare(t1,  t3));
	}
	
	public void testVH3() {
		Comparator<Task> c = ValueHunter.getInstance();
		Task t1 = new Task("T1", 10, 100, 9);
		Task t2 = new Task("T2", 10, Task.MAX_TIME, 10);
		Task t3 = new Task("T3", 20, 200, 20);
		
		testComparison("VH(" + t1 + "," + t2 + ")", -1, c.compare(t1,  t2));
		testComparison("VH(" + t1 + "," + t3 + ")", -1, c.compare(t1,  t3));
	}
	
	public void testVH4() {
		Comparator<Task> c = ValueHunter.getInstance();
		Task t1 = new Task("T1", 10, 100, 9);
		Task t2 = new Task("T2", 11, 9, 9);
		Task t3 = new Task("T3", 21, 200, 18);
		
		testComparison("VH(" + t1 + "," + t2 + ")", 1, c.compare(t1,  t2));
		testComparison("VH(" + t1 + "," + t3 + ")", 1, c.compare(t1,  t3));
	}
	
	public void testVH5() {
		Comparator<Task> c = ValueHunter.getInstance();
		Task t1 = new Task("T1", 0, 100, 0);
		Task t2 = new Task("T2", 0, 100, 1);
		Task t3 = new Task("T3", 1, 200, 0);
		Task t4 = new Task("T4", 0, 101, 0);
		
		testComparison("VH(" + t1 + "," + t2 + ")", -1, c.compare(t1,  t2));
		testComparison("VH(" + t1 + "," + t3 + ")", 1, c.compare(t1,  t3));
		testComparison("VH(" + t1 + "," + t4 + ")", -1, c.compare(t1,  t4));
	}

	public void testVH6() {
		assertNotPerverse(ValueHunter.getInstance());
	}
	
	public void testVH7() {
		assertReflexive(ValueHunter.getInstance());
	}
	
	public void testVH8() {
		assertAntiSymmetric(ValueHunter.getInstance());
	}
	
	public void testVH9() {
		assertTransitive(ValueHunter.getInstance());
	}
	


}
