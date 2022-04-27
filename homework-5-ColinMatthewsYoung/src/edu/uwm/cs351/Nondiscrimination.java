//Colin Young
package edu.uwm.cs351;

import java.util.Comparator;

/**
 * The class Nondiscrimination.
 * 
 * A task priority mechanism that does not discriminate on the basis
 * of reward or deadline or duration.  Every task has equal priority.
 */
public class Nondiscrimination implements Comparator<Task> {

	@Override
	public int compare(Task o1, Task o2) {
		// TODO: Implement this method.
		return 0;
	}

	@Override
	public String toString() {
		return "Nondiscrimination";
	}
	
	private static Comparator<Task> instance = new Nondiscrimination();
	
	/**
	 * Gets a single instance of Nondiscrimination comparator.
	 * @return a single instance of Nondiscrimination comparator
	 */
	public static Comparator<Task> getInstance() { return instance; }
}
