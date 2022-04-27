import edu.uwm.cs.junit.LockedTestCase;


public class UnlockTests {
	public static void main(String[] args){
		unlock("TestPoint");
		unlock("TestVector");
		unlock("TestParticle");
	}
	
	private static void unlock(String classname){
		LockedTestCase.unlockAll(classname);
		System.out.format("All tests in %s.java are unlocked.%n"
				+ "You can run them against your program now.%n"

				+ "Remember to push %s.tst (refresh the project to show it).%n%n", classname, classname);
	}

}
