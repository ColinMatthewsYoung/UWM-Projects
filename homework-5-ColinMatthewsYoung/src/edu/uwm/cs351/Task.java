//Colin Young
package edu.uwm.cs351;

import java.util.Comparator;

/**
 * The Class Task.
 */
public class Task {
	
	/** The maximum value for reward */
	public static final int MAX_REWARD = 1000;
	
	/** The maximum units of time this task takes to complete. */
	public static final int MAX_DURATION = 1000;
	
	/** The maximum amount of time we can run tasks. */
	public static final int MAX_TIME = Integer.MAX_VALUE;

	/** The name of this task. */
	final String name;
	
	/** How many units of good are received for accomplishing this task.
	 *  In range: [0,MAX_VALUE] */
	private final int reward;
	
	/** How many units of time this task this task takes to complete.
	 *  In range: [0,MAX_VALUE] */
	private final int duration;
	
	/** The time that this task must be completed by.
	 * The time must not be negative o more than MAX_TIME.
	 *  The window to perform the task is [0, deadline]
	 *  where 0 represents the moment performAll(int) is called. */
	private final int deadline;
	
	/** The links to the previous and next tasks. */
	private Task prev, next;
	
	/**
	 * Instantiates a new task.
	 * @param name the name of the task, must not be null
	 * @param reward the reward for doing the task
	 * @param deadline the deadline for the task in time
	 * @param duration the duration of the task
	 * @throws IllegalArgumentException if a parameter is negative or out of bounds.
	 * @see #MAX_REWARD
	 * @see #MAX_DURATION
	 * @see #MAX_TIME
	 */
	public Task(String name, int reward, int deadline, int duration) {
		if (name == null) throw new NullPointerException("name must not be null");
		checkParameter("reward",reward,MAX_REWARD);
		checkParameter("duration",duration,MAX_DURATION);
		checkParameter("deadline",deadline,MAX_TIME);
		this.name = name;
		this.reward = reward;
		this.deadline = deadline;
		this.duration = duration;
	}
	
	private static void checkParameter(String name, int p, int max) {
		if (p < 0 || p > max) throw new IllegalArgumentException(name + " must be in range [0," + max + "]: " + p);}
	
	/** Gets the name.
	 * @return the name */
	public String getName() {return name;}
	
	/** Gets the reward.
	 * @return the reward */
	public int getReward() {return reward;}
	
	/** Gets the deadline.
	 * @return the deadline */
	public int getDeadline() {return deadline;}
	
	/** Gets the duration.
	 * @return the duration */
	public int getDuration() {return duration;}
	
	/** Gets the previous task.
	 * @return the previous task */
	public Task getPrevious() {return prev;}
	
	/** Gets the next task.
	 * @return the next task */
	public Task getNext() {return next;}
	
	/**
	 * Add another task into this task's list by priority order.
	 * <p>
	 * If the other task should come before, place it somewhere before this task.
	 * If the other task has equal priority, it should be placed immediately after this task,
	 *   <em>unless</em> this task has some task before it and nothing after it, in which case
	 *   it should come immediately before
	 * If the other task should come after, place it somewhere after this task.
	 * It may be necessary to move multiple times forward or multiple times backward (but not both!).
	 * Make sure tasks don't just pass a new task back and forth.
	 * </p>
	 * @param other the task to add to our list
	 * @param priority comparator of tasks in the list.
	 * @throws IllegalArgumentException if we try to add a task that is already in a list.
	 * @throws IllegalArgumentException if we try to add a task to itself
	 */
	public void addInPriority(Task other, Comparator<Task> priority) {
		// System.out.println("Adding " + other + " to " + this); // perhaps useful for debugging
		// TODO: Implement this method.  No loops, only recursion.
		//
		// NB: While TaskList happens to call this method only on the head of the list,
		//	   we can't assume all classes that utilize Task will do so. That is why
		//	   we must consider all scenarios, including those where this method is
		//	   called on a Task in the middle or end of the list.
			
			
			if(this == other)
				throw new IllegalArgumentException("cant add task to itself");
			if(other.next!=null || other.prev != null)
				throw new IllegalArgumentException("already in a list");
			
			
			int result=priority.compare(other, this);
			
			if(result < 0) {
				if(this.getPrevious()!=null && priority.compare(other, this.getPrevious())<=0)
					this.getPrevious().addInPriority(other, priority);
				else {
					other.prev = this.prev;
					other.next = this;
					if(this.prev !=null)
						this.prev.next = other;
					this.prev = other;
				}
			}
			
			else if(result > 0) {
				if(this.getNext() != null && priority.compare(other, this.getNext())>=0)
					this.getNext().addInPriority(other, priority);
				else {
					other.prev = this;
					other.next = this.next;
					if(this.next !=null)
						this.next.prev = other;
					this.next = other;
					
				}
			}
			else {
				if(this.getPrevious() !=null && this.getNext() == null){
					//place before 
					other.prev = this.prev;
					this.prev.next = other;
					this.prev = other;
					other.next = this;
					
				}
				else {
					//place after
					other.prev = this;
					other.next = this.next;
					if(this.next != null) 
						this.next.prev = other;
					this.next = other;
					
					
				}
			}
		}
			
			
	
	
	
	/**
	 * Remove this item from its list.
	 * This method must not be called on the head or tail or a list
	 * because there is no way to update the Task List 's head or tail field.
	 * This task will be completely disconnected from any other tasks.
	 * @throws IllegalStateException if the task is first or last in its list.
	 */
	public void remove() {
		// TODO: Implement this method.  No loops or recursion required.
		if(this.prev == null || this.next == null)
			throw new IllegalStateException("cant remove first or last item in the list");
		
		this.prev.next = this.next;
		this.next.prev = this.prev;
		this.next = null;
		this.prev = null;
	}
	
	
	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[Task " + name + "]-{Reward:" + reward + ", Deadline:" + deadline + ", Duration:" + duration + "}";
	}
	
	// needed to help test task list invariant.  It's "default" access -- not accessible outside the package
	/**
	 * Create a linked list with the given tasks.
	 
	 * @param tasks series of non-null tasks none of which can have existing prev/next links.
	 * @throws IllegalArgumentException if any task has non-null prev and next links.
	 */
	static void connect(Task... tasks) {
		for (Task t : tasks) {
			if (t.prev != null || t.next != null) throw new IllegalArgumentException("task already in a list");
		}
		Task p = null;
		for (Task t : tasks) {
			if (p != null) {
				p.next = t;
				t.prev = p;
			}
			p = t;
		}
	}
	
}
