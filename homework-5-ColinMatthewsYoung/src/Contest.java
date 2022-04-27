import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.uwm.cs351.Nondiscrimination;
import edu.uwm.cs351.RewardSeeker;
import edu.uwm.cs351.Smart;
import edu.uwm.cs351.Task;
import edu.uwm.cs351.TaskGenerator;
import edu.uwm.cs351.TaskList;
import edu.uwm.cs351.TyrannyOfTheUrgent;
import edu.uwm.cs351.ValueHunter;

class Contest {

	private static List<Comparator<Task>> priorities = new ArrayList<Comparator<Task>>();
	private static int LIMIT = Task.MAX_DURATION;
	private static final int MEDIAN_REWARD = 100;
	private static final int MEDIAN_DURATION = 25;
	
	private TaskGenerator gen = new TaskGenerator(MEDIAN_REWARD, MEDIAN_DURATION, LIMIT);
	private List<Task> tasks = new ArrayList<Task>();

	//
	//	NB: To specify number of cycles to run:
	//	     Run-> Run Configurations -> Arguments -> Program Arguments -> [enter int here]
	//
	public static void main(String[] args) {

		// Parse argument and Setup
		int cycles;
		try {cycles = Math.abs(Integer.parseInt(args[0]));}
		catch (Exception e) {cycles = 1;}
		double[] scores = new double[5];

		// Create list of comparators...
		priorities.add(Nondiscrimination.getInstance());
		priorities.add(TyrannyOfTheUrgent.getInstance());
		priorities.add(RewardSeeker.getInstance());
		priorities.add(ValueHunter.getInstance());
		priorities.add(Smart.getInstance());

		// Run cycles
		int c = 0;
		while (++c <= cycles) {
			System.out.println("Cycle " + c + "...");
			String result = new Contest().run();
			
			switch (result) {
			case ("Nondiscrimination"): {scores[0]++;break;}
			case ("Reward Seeker"): {scores[1]++;break;}
			case ("Tyranny of the Urgent"): {scores[2]++;break;}
			case ("Value Hunter"): {scores[3]++;break;}
			case ("Smart"): {scores[4]++;break;}
			default: {throw new IllegalArgumentException("Don't change comparator toString methods.");}
			}
		}
		
		for (int i=0; i < scores.length; i++)
			scores[i] *= 100/(double)cycles;
		
		// Display final scores
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		System.out.println("x                                                              x");
		System.out.println("x     _______             __   _____                           x");
		System.out.println("x    / ____(_)___  ____  / /  / ___/_________  ________  _____ x");
		System.out.println("x   / /_  / / __ \\/ __ `/ /   \\__ \\/ ___/ __ \\/ ___/ _ \\/ ___/ x");
		System.out.println("x  / __/ / / / / / /_/ / /   ___/ / /__/ /_/ / /  /  __(__  )  x");
		System.out.println("x /_/   /_/_/ /_/\\__,_/_/   /____/\\___/\\____/_/   \\___/____/   x");
		System.out.println("x                                                              x");
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n");
		
		DecimalFormat df = new DecimalFormat("00.00");
		System.out.println("                Nondiscrimination: "+df.format(scores[0])+"%");
		System.out.println("                    Reward Seeker: "+df.format(scores[1])+"%");
		System.out.println("            Tyranny of the Urgent: "+df.format(scores[2])+"%");
		System.out.println("                     Value Hunter: "+df.format(scores[3])+"%");
		System.out.println("                            Smart: "+df.format(scores[4])+"%");

	}

	public String run() {
		int sum = 0;

		// Tasks to try our contestants with
		while (sum < LIMIT) {
			Task t = gen.makeRandomTask();
			tasks.add(t);
			sum += t.getDuration();
		}

		// Run contest
		Comparator<Task> winner = null;
		int maximum = -1;
		for (Comparator<Task> priority : priorities) {
			System.out.print("[Contestant: " + priority + "] ");
			TaskList tl = new TaskList(priority);
			for (Task t : tasks) {
				tl.add(t);
			}
			int result = tl.performAll(LIMIT);
			System.out.println("[Score: " + result + "]");
			if (result > maximum) {
				maximum = result;
				winner = priority;
			}
		}
		System.out.println("And the winner with a score of " + maximum + " is " + winner + ".\n");
		return winner.toString();
	}
}
