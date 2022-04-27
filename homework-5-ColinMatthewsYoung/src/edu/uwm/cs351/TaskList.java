//Colin Young
package edu.uwm.cs351;

import java.util.Comparator;

import junit.framework.TestCase;

/**
 * The Class TaskList.
 */
public class TaskList {

	private Comparator<Task> priority;
	private Task head, tail;

	private static boolean doReport = true;
	
	private boolean report(String s) {
		if (doReport) System.out.println("Invariant error: " + s);
		return false;
	}
	
	/**
	 * Check the invariant:
	 * <ol>
	 * <li> The priority cannot be null
	 * <li> Neither {@link #head} nor {@link #tail} is null.
	 * <li> The {@link #head} is a most desirable task and {@link #tail}
	 *      is a least desirable task according to any priority that is
	 *      not "perverse" (see constructor documentation).
	 * <li> There is nothing before {@link #head} or after {@link #tail}
	 *      in the list.
	 * <li> The nodes link with consistent {@link Task#getPrevious()} and {@link Task#getNext()}	links
	 *      starting at {@link #head} and ending at {@link #tail}.
	 * </ol>
	 * If the invariant is not true, then a reason will be reported.
	 * <p>
	 * It is not required that the tasks are in priority order 
	 * since this is not under control of the Task List.
	 * <p>
	 * @return true is invariant is true, false otherwise
	 * @see #TaskList(Comparator) {@link #TaskList(Comparator)} for the definition of "perverse"
	 */
	private boolean wellFormed() {
		// TODO
		if(this.priority ==null)
			return report("priority is null");
		if(this.head ==null || this.tail == null)
			return report("head or tail is null");
		
		if(head.getDeadline() != 0 || head.getReward() != Task.MAX_REWARD || head.getDuration() !=0)
			return report("head is not most desirable");
		if(tail.getDeadline() != Task.MAX_TIME || tail.getReward() !=0 || tail.getDuration() != Task.MAX_DURATION)
			return report("tail is not least desirable");
		
		
		
		if(this.head.getPrevious()!=null || this.tail.getNext()!=null)
			return report("there is a task before head or after tail");
		
		
		for(Task p = this.head;p!=null; p=p.getNext()){
			
			if(p.getNext()==null && p != tail)
				return report("head does not lead to tail");
			if(p.getNext()!=null && p.getNext().getPrevious() != p)
				return report("can not travers list");
		}
		
		return true;
	}
	
	/**
	 * Instantiates a new task list with the given priority comparator.
	 * @param priority the priority comparator this TaskList will use.
	 * It must not be null or "perverse".
	 * <p>
	 * A <em>perverse</em> priority is one that
	 * <ul>
	 * <li> prefers low-reward tasks, or
	 * <li> prefers less urgent tasks, or
	 * <li> prefers longer tasks
	 * </ul>
	 * The non-discrimination priority that treats all tasks as equally important
	 * is <em>not</em> perverse, because it doesn't have any preferences.
	 */
	public TaskList(Comparator<Task> priority) {
		head = new Task("dummy", Task.MAX_REWARD, 0, 0);
		tail = new Task("dummy", 0, Task.MAX_TIME, Task.MAX_DURATION);
		head.addInPriority(tail, priority);
		this.priority = priority;
		assert wellFormed() : "invariant fails at end of constructor";
	}
	
	private TaskList(boolean ignored) {} // DO NOT CHANGE THIS
	
	/**
	 * Adds a task to this TaskList.
	 *
	 * @param t the task to add
	 */
	public void add(Task t) {
		assert wellFormed() : "invariant fails at beginning of add";
		
		// We have crafted the head to have priority no less than
		// all other tasks according to all of our comparators.
		// Similarly the tail should have priority no greater than
		// any other task.
		// That way, the head is always at the beginning of its list,
		// and the tail at the end.
		
		assert (priority.compare(head, t) <= 0) : "Priority is perverse";
		assert (priority.compare(t, tail) <= 0) : "Priority is perverse";

		// Add to list
		// TODO: if you fail tests, check that you are starting at the 
		// correct end.
		this.tail.addInPriority(t, priority);
		
		assert wellFormed() : "invariant fails at end of add";
	}
	
	/**
	 * Perform all possible tasks in the list that can be completed
	 * before the final deadline and the task's own deadline. We will
	 * treat the deadlines as relative to the moment this method is called.
	 * <p>
	 * Every time a task is completed, the task's reward is added to
	 * totalReward and currentTime advances by the duration of the task.
	 * <p>
	 * Tasks should be performed in priority order,
	 * except that no task should even be attempted unless there will be time
	 * to complete it before the final deadline and before its own deadline.
	 * <p>
	 * Tasks should be removed from the task list as they are completed or discarded.
	 * At the end, the task list should contain only our 'dummy' head node.
	 * @param finalDeadline the deadline that all possible tasks must be completed by
	 * @return reward sum of the rewards of each task completed on time
	 */
	public int performAll(int finalDeadline) {
		assert wellFormed() : "invariant fails at beginning of performAll";
		/*
		 	This printout may help you to implement this method.
		 
			System.out.println("My tasks: ");
			for (Task t = head.getNext(); t != tail; t = t.getNext())
				System.out.println("  " + t);
		
		*/
		
		int currentTime = 0;
		int totalReward = 0;
		
		// TODO: Implement this method.
		for(Task p=this.head.getNext(); p!=null; p=p.getNext()) {
			if(p.getNext()==null)
				continue;
			int futureTime = p.getDuration() + currentTime;
			if(finalDeadline<futureTime || p.getDeadline() < futureTime) {
				p.remove();
				p=head;
				continue;
			}
			totalReward+=p.getReward();
			currentTime = futureTime;
			p.remove();
			p = head;
		}
		
		assert wellFormed() : "invariant fails at end of performAll";
		return totalReward;
	}
	
	public static class TestInvariant extends TestCase {
		
		protected TaskList self;

		private Task h0 = new Task("dummy",Task.MAX_REWARD,0,0);
		private Task h1 = new Task("dummy",Task.MAX_REWARD,0,0);
		private Task h2 = new Task("dummy",Task.MAX_REWARD,0,0);
		private Task h3 = new Task("dummy",Task.MAX_REWARD,0,0);

		private Task t0 = new Task("dummy",0,Task.MAX_TIME,Task.MAX_DURATION);
		private Task t1 = new Task("dummy",0,Task.MAX_TIME,Task.MAX_DURATION);
		private Task t2 = new Task("dummy",0,Task.MAX_TIME,Task.MAX_DURATION);
		private Task t3 = new Task("dummy",0,Task.MAX_TIME,Task.MAX_DURATION);

		private Task ha = new Task("dummy",Task.MAX_REWARD-1,0,0);
		private Task hb = new Task("dummy",Task.MAX_REWARD,1,0);
		private Task hc = new Task("dummy",Task.MAX_REWARD,0,1);
		private Task ta = new Task("dummy",1,Task.MAX_TIME,Task.MAX_DURATION);
		private Task tb = new Task("dummy",0,Task.MAX_TIME-1,Task.MAX_DURATION);
		private Task tc = new Task("dummy",0,Task.MAX_TIME,Task.MAX_DURATION-1);


		private Task g1 = new Task("Task 1",1,100,5);

		private Comparator<Task> perverse = (t1,t2) -> t1.getReward() - t2.getReward();
		
		@Override
		protected void setUp() {
			self = new TaskList(false);
			self.priority = perverse; // not used in these tests
		}
		
		protected void assertWellFormed(boolean b) {
			boolean saved = doReport;
			try {
				doReport = b;
				assertEquals(b, self.wellFormed());
			} finally {
				doReport = saved;
			}
		}
		
		public void testA() {
			self.head = null;
			self.tail = null;
			assertWellFormed(false);
			self.tail = t0;
			assertWellFormed(false);
		}
		
		public void testB() {
			self.head = h0;
			self.tail = null;
			assertWellFormed(false);
			self.tail = t0;
			assertWellFormed(false);
			Task.connect(h0,t0);
			assertWellFormed(true);
		}
		
		public void testC() {
			self.head = g1;
			self.tail = t0;
			Task.connect(self.head, self.tail);
			assertWellFormed(false);
			
			self.head = ha;
			self.tail = t1;
			Task.connect(self.head, self.tail);
			assertWellFormed(false);
			
			self.head = hb;
			self.tail = t2;
			Task.connect(self.head, self.tail);
			assertWellFormed(false);
			
			self.head = hc;
			self.tail = t3;
			Task.connect(self.head, self.tail);
			assertWellFormed(false);			
		}
		
		public void testD() {
			self.head = h0;
			self.tail = g1;
			Task.connect(self.head, self.tail);
			assertWellFormed(false);
			
			self.head = h1;
			self.tail = ta;
			Task.connect(self.head, self.tail);
			assertWellFormed(false);

			self.head = h2;
			self.tail = tb;
			Task.connect(self.head, self.tail);
			assertWellFormed(false);

			self.head = h3;
			self.tail = tc;
			Task.connect(self.head, self.tail);
			assertWellFormed(false);
		}
	
		public void testE() {
			self.head = h0;
			self.tail = t2;
			Task.connect(self.head, t3, g1, ha, self.tail);
			assertWellFormed(true);
			
			self.priority = null;
			assertWellFormed(false);
		}
		
		public void testF() {
			Task.connect(h0,h1,t1);
			self.head = h1;
			self.tail = t1;
			assertWellFormed(false);
			
			Task.connect(h2,t2,t3);
			self.head = h3;
			self.tail = t3;
			assertWellFormed(false);
		}
		
		public void testG() {
			Task.connect(h0,h1,h2,h3);
			Task.connect(t0,t1,t2,t3);
			self.head = h0;
			self.tail = t3;
			assertWellFormed(false);
		}
		
		public void testH() {
			Task.connect(h0,t0);
			Task.connect(h1,t1);
			self.head = h0;
			self.tail = t1;
			assertWellFormed(false);
		}
		
		public void testI() {
			Task.connect(h0, g1, h1, g1, t0);
			self.head = h0;
			self.tail = t0;
			assertWellFormed(false);
		}
		
		public void testJ() {
			Task.connect(h0, g1, h1, t0, h2, t0);
			self.head = h0;
			self.tail = t0;
			assertWellFormed(false);
		}
	}
}
