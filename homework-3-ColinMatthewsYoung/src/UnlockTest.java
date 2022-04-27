import edu.uwm.cs.junit.LockedTestCase;

public class UnlockTest {
	public static void main(String[] args){
		unlock("TestSimple");
	}
	
	private static void unlock(String classname){
		LockedTestCase.unlockAll(classname);
		System.out.format("Tests in %s.java are unlocked.%n"
				+ "You can run it against your progam now.%n"
				+ "Remember to push %s.tst (refresh the project to show it).%n%n", classname, classname);
	}
}
