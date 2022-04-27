

import java.io.StringReader;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.ps.Array;
import edu.uwm.cs351.ps.Engine;
import edu.uwm.cs351.ps.Interpreter;
import edu.uwm.cs351.ps.Scanner;

public class TestEngine extends LockedTestCase {

	Engine engine;
	
	protected void setUp() {
		engine = new Engine(10,10);
	}
	
	protected void execute(String s) {
		Interpreter i = new Interpreter(new Scanner(new StringReader(s)), engine);
		i.run();
	}
	
	public void test00() {
		engine.push("a");
		engine.push("b");
		engine.push("c");
		// debug string prints the stack:
		assertEquals(Ts(1246496985),engine.debugString());
		execute("pop");
		assertEquals("[a, b]",engine.debugString());
		execute("(d)"); // Recall that parens are PostScript's way to show string literals
		assertEquals(Ts(1069313328),engine.debugString());
	}
	
	public void test01() {
		engine.push("a");
		engine.push("b");
		engine.push("c");
		execute("0 index");
		assertEquals("[a, b, c, c]",engine.debugString());
	}
	
	public void test02() {
		engine.push("a");
		engine.push("b");
		engine.push("c");
		execute("1 index");
		assertEquals(Ts(1810631773), engine.debugString());
	}
	
	public void test03() {
		engine.push("a");
		engine.push("b");
		engine.push("c");
		execute("2 index");
		assertEquals("[a, b, c, a]", engine.debugString());
	}
	
	public void test09() {
		execute("(7) (6) (5) (4) (3) (2) (1) 3 index");
		assertEquals("[7, 6, 5, 4, 3, 2, 1, 4]", engine.debugString());
	}
	
	public void test10() {
		execute("count");
		Double d = (Double)engine.pop();
		assertEquals(Ti(839574740),d.intValue());
	}
	
	public void test11() {
		engine.push("a");
		execute("count");
		Double d = (Double)engine.pop();
		assertEquals(1,d.intValue());
	}
	
	public void test13() {
		engine.push("a");
		engine.push("b");
		engine.push("c");
		execute("count");
		Double d = (Double)engine.pop();
		assertEquals(3,d.intValue());
	}
	
	public void test20() {
		execute("[ counttomark");
		assertEquals("[<mark>, 0.0]",engine.debugString());
		Double d = (Double)engine.pop();
		assertEquals(0,d.intValue());		
	}
	
	public void test21() {
		execute("[ (a) (b) [ (c) counttomark");
		Double d = (Double)engine.pop();
		assertEquals(1,d.intValue());
		assertEquals("[<mark>, a, b, <mark>, c]",engine.debugString());
	}
	
	public void test22() {
		execute("[ (a) [ (b) (c) counttomark");
		Double d = (Double)engine.pop();
		assertEquals(Ti(1368049932),d.intValue());
		assertEquals("[<mark>, a, <mark>, b, c]",engine.debugString());
	}
	
	public void test26() {
		execute("[ (a) [ (b) (c) (d) (e) (f) (g) (h) counttomark");
		Double d = (Double)engine.pop();
		assertEquals(7,d.intValue());
		assertEquals("[<mark>, a, <mark>, b, c, d, e, f, g, h]",engine.debugString());
	}
	
	public void test27() {
		execute("[ (0, 1) [ (a) (b, c) ([) (d, e) counttomark");
		Double d = (Double)engine.pop();
		assertEquals(4, d.intValue());
		assertEquals("[<mark>, 0, 1, <mark>, a, b, c, [, d, e]", engine.debugString());
	}
	
	public void test30() {
		execute("(a) [ cleartomark");
		assertEquals("[a]",engine.debugString());
	}
	
	public void test36() {
		execute("[ (z) [ 1 2 3 4 5 (c) cleartomark");
		assertEquals("[<mark>, z]",engine.debugString());
	}
	
	public void test40() {
		execute("[]");
		Array a = (Array)engine.pop();
		assertEquals(0,a.length());
		assertEquals("[]",engine.debugString()); // coincidence
	}
	
	public void test41() {
		execute("(hello) [ ]");
		Array a = (Array)engine.pop();
		assertEquals(0,a.length());
		assertEquals("[hello]",engine.debugString());
	}
	
	public void test42() {
		execute("[ (one) [ (two) ]");
		Array a = (Array)engine.pop();
		assertEquals(1, a.length());
		assertEquals("two", a.get(0));
		assertEquals("[<mark>, one]",engine.debugString());
	}
	
	public void test44() {
		execute("(a) [ 1 2 3 4 ]");
		Array a = (Array)engine.pop();
		assertEquals(4,a.length());
		assertEquals(Double.valueOf(1),a.get(0));
		assertEquals(Double.valueOf(2),a.get(1));
		assertEquals(Double.valueOf(3),a.get(2));
		assertEquals(Double.valueOf(4),a.get(3));
		assertEquals("[a]",engine.debugString());
	}
	
	public void test49() {
		execute("[ 9 [ (b) [ (c) (d) ] 10 20 30]");
		Array a = (Array)engine.pop();
		assertEquals(5,a.length());
		assertEquals("b",a.get(0));
		Array a2 = (Array)a.get(1);
		assertEquals(2, a2.length());
		assertEquals("c",a2.get(0));
		assertEquals("d",a2.get(1));
		assertEquals(Double.valueOf(10),a.get(2));
		assertEquals(Double.valueOf(20),a.get(3));
		assertEquals(Double.valueOf(30),a.get(4));
		assertEquals("[<mark>, 9.0]",engine.debugString());
	}
	
	public void test50() {
		execute("(a) (b) (c) (d) 2 0 roll");
		assertEquals(Ts(1670987571),engine.debugString());
	}
	
	public void test51() {
		execute("(a) (b) (c) (d) 2 1 roll");
		assertEquals("[a, b, d, c]",engine.debugString());
	}
	
	public void test52() {
		execute("(a) (b) (c) (d) 2 -1 roll");
		assertEquals("[a, b, d, c]",engine.debugString());
	}
	
	public void test53() {
		execute("(a) (b) (c) (d) 3 1 roll");
		assertEquals(Ts(763956264),engine.debugString());
	}
	
	public void test54() {
		execute("(a) (b) (c) (d) 3 2 roll");
		assertEquals("[a, c, d, b]",engine.debugString());
	}
	
	public void test55() {
		execute("(a) (b) (c) (d) 3 -1 roll");
		assertEquals("[a, c, d, b]",engine.debugString());
	}
	
	public void test56() {
		execute("(a) (b) (c) (d) 4 1 roll");
		assertEquals("[d, a, b, c]",engine.debugString());
	}
	
	public void test57() {
		execute("(a) (b) (c) (d) 4 -2 roll");
		assertEquals("[c, d, a, b]",engine.debugString());
	}
	
	public void test58() {
		execute("(a) (b) (c) (d) 4 -1 roll");
		assertEquals("[b, c, d, a]",engine.debugString());
	}
	
	public void test59() {
		execute("(1) (2) (3) (4) (5) (6) (7) (8) (9) 7 -5 roll");
		assertEquals("[1, 2, 8, 9, 3, 4, 5, 6, 7]",engine.debugString());
	}
}
