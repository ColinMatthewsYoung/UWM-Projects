//Colin Young
package edu.uwm.cs351;

import java.util.Comparator;

/**
 * The class Smart.
 * 
 * Write your own comparator to try and beat the others!
 */
public class Smart implements Comparator<Task> {


	// Write everything for this class.  Look at other comparators for the required format.
	// Its name should be "Smart"
	@Override
	public int compare(Task o1, Task o2) {
	
		/*wins around 37 - 40%, ties urgent about 20% of the time.*/
		double value1 = o1.getDeadline() +  (o2.getReward()*(o1.getDuration())*0.008);
		double value2 = o2.getDeadline() +  (o1.getReward()*(o2.getDuration())*0.008);
		return (int) (value1 - value2);
		
		
		
		
		
		
	}
	
	@Override
	public String toString() {
		return "Smart";
	}
	
	private static Comparator<Task> instance = new Smart();
	
	public static Comparator<Task> getInstance(){return instance;}
}
