//Colin Young
package edu.uwm.cs351;

import java.util.Comparator;

/**
 * The class ValueHunter.
 * 
 * Returns t1 < t2 (comes before) if t1 has more 'value' than t2.
 * We define value as bang (importance = reward) for the buck (size = duration).
 * To avoid problems with duration = 0, we treat these tasks specially:
 * All tasks with duration = 0 are scheduled before other tasks, and
 * (predictably) higher reward 0 duration tasks are scheduled earlier.
 * For tasks with equal value, it prefers the one with the earlier deadline.
 */
public class ValueHunter implements Comparator<Task> {

	@Override
	public int compare(Task o1, Task o2) {
		int valueDif = o2.getReward() * o1.getDuration() - o1.getReward() * o2.getDuration();

		if (valueDif != 0) return valueDif;
		
		// It can be zero if both durations are zero, or
		// if one task is 0/0, as well as 
		// if the values (reward/duration) are equal.
		
		// We treat the special cases first:
		if (o1.getDuration() == 0 && o2.getDuration() == 0) {
			valueDif = o2.getReward() - o1.getReward();
			if (valueDif != 0) return valueDif;
			// otherwise, value *is* indeed equal
			// and is handled below
		} else if (o1.getDuration() == 0 && o1.getReward() == 0) {
			// we know that o2's duration cannot be zero
			return -1; // o1 should come first
		} else if (o2.getDuration() == 0 && o2.getReward() == 0) {
			return 1; // o2 should come first.
		}
		// otherwise we have a true tie in the value:
		return o1.getDeadline() - o2.getDeadline();
	}

	@Override
	public String toString() {
		return "Value Hunter";
	}
	
	private static Comparator<Task> instance = new ValueHunter();
	
	/**
	 * Gets a single instance of ValueHunter comparator.
	 * @return a single instance of ValueHunter comparator
	 */
	public static Comparator<Task> getInstance() { return instance; }
}
