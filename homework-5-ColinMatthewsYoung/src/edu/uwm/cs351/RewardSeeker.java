//Colin Young
package edu.uwm.cs351;

import java.util.Comparator;

/**
 * The class RewardSeeker.
 * 
 * Return t1 < t2 (comes before) if t1 has a higher reward.
 */
public class RewardSeeker implements Comparator<Task> {

	@Override
	public int compare(Task o1, Task o2) {
		// TODO: Implement this method.
	
		return o2.getReward() - o1.getReward();
	}

	@Override
	public String toString() {
		return "Reward Seeker";
	}
	
	private static Comparator<Task> instance = new RewardSeeker();
	
	/**
	 * Gets a single instance of RewardSeeker comparator.
	 * @return a single instance of RewardSeeker comparator
	 */
	public static Comparator<Task> getInstance() { return instance; }
}
