//Colin Young
package edu.uwm.cs351;

import java.util.Comparator;

/**
 * The class TyrannyOfTheUrgent.
 * 
 * Return t1 < t2 (comes before) if t1 has an earlier deadline.
 * For tasks with equal deadline, compare based on reward.
 */
public class TyrannyOfTheUrgent implements Comparator<Task> {

	@Override
	public int compare(Task o1, Task o2) {
		// TODO: Implement this method.
		if(o1.getDeadline() - o2.getDeadline() ==0)
			return o2.getReward() - o1.getReward();
		return o1.getDeadline() - o2.getDeadline();
	}
	
	@Override
	public String toString() {
		return "Tyranny of the Urgent";
	}
	
	private static Comparator<Task> instance = new TyrannyOfTheUrgent();
	
	/**
	 * Gets a single instance of TyrannyOfTheUrgent comparator.
	 * @return a single instance of TyrannyOfTheUrgent comparator
	 */
	public static Comparator<Task> getInstance() { return instance; }

}
