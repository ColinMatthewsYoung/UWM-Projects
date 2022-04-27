package edu.uwm.cs351.ps;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.color.ColorSpace;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.uwm.cs351.util.Stack;
//Colin Young
/**
 * A partial PostScript engine for interpreting PostScript.
 * @author boyland
 */
public class Engine {
	private static final int TITLEBAR_SIZE = 20;
	private final BufferedImage page;
	private final Stack<Object> evalStack = new Stack<>();
	private final Stack<Dictionary> dictStack = new Stack<>();
	private final Stack<GraphicsState> graphicsStack = new Stack<>();
	private final Random random = new Random();
	private JDialog window;

	private GraphicsState createGraphicsState() {
		Graphics2D graphics = (Graphics2D)page.getGraphics();
		graphics.setColor(Color.BLACK);
		return new GraphicsState(graphics);
	}
	
	protected void clearImage() {
		Graphics g2d = this.page.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, this.page.getWidth(null), this.page.getHeight(null));
	}
	
	/**
	 * Create a PostScript engine with the given page size.
	 * @param width width of the "paper"
	 * @param height height of the "paper".
	 */
	public Engine(int width, int height) {
		page = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
		clearImage();
		Dictionary systemDict = createSystemDict();
		dictStack.push(systemDict);
		Dictionary userDict = new Dictionary();
		userDict.put(new Name("userdict"), userDict);
		dictStack.push(userDict);
		graphicsStack.push(createGraphicsState());
		SwingUtilities.invokeLater(() -> {
			try {
				window = new JDialog((JFrame)null, "Click in Window when done.",true);
				window.setSize(width, height+TITLEBAR_SIZE);
				ShowPanel content = new ShowPanel(page);
				window.add(content);
				content.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						super.mouseReleased(e);
						window.setVisible(false);
					}	
				});
			} catch (HeadlessException ex) {
				// don't show anything
			}
		});
	}
	
	/**
	 * Stop the postscript engine.  This enables the JVM to shut down cleanly.
	 */
	public void shutdown() {
		SwingUtilities.invokeLater(() -> {
			window.setVisible(false);
			window.dispose();
		});
	}
	
	/**
	 * Execute an object: usually just push it on the stack,
	 * but a name will be looked up and its value (if a procedure or
	 * operator) will be executed.
	 * @param x object to execute.
	 */
	public void execute(Object x) {
		if (x instanceof Name) {
			String rep = ((Name) x).rep;
			if (rep.startsWith("/")) {
				evalStack.push(new Name(rep.substring(1)));
			} else {
				Object obj = lookup((Name)x);
				if (obj instanceof Procedure) {
					Procedure p = (Procedure)obj;
					p.execute(this);
				} else if (obj instanceof Operator) {
					((Operator)obj).execute(this);
				} else {
					push(obj);
				}
			}
		} else if (x instanceof Operator) {
			((Operator)x).execute(this);
		} else {
			push(x);
		}
	}
	
	/**
	 * Push an object on the evaluation stack.
	 * @param x object to push
	 */
	public void push(Object x) {
		evalStack.push(x);
	}
	
	// convenience methods: make sure we only push Double numbers 
	/**
	 * Push an integer (by converting it first to a double).
	 * @param i
	 */
	public void push(int i) {
		evalStack.push(Double.valueOf(i));
	}
	/**
	 * Push a float on the stack (after first converting it to a double).
	 * @param f
	 */
	public void push(float f) {
		evalStack.push(Double.valueOf(f));
	}
	
	/**
	 * Remove and return the top element of the evaluation stack.
	 * @return former top element of the evaluation stack.
	 */
	public Object pop() {
		return evalStack.pop();
	}
	
	/**
	 * Look up a name in the dictionary stack.
	 * We check dictionaries in the stack to find a definition.
	 * If no definition is found, we generate an error operator.
	 * @param n name to look up, should not be null.
	 * @return binding of the name, or an error operator.
	 */
	public Object lookup(Name n) {
		Stack<Dictionary> cl = dictStack.clone();
		while (!cl.isEmpty()) {
			Dictionary dict = cl.pop();
			if (dict.known(n)) return dict.get(n);
		}
		return new UndefinedNameOperator(n);
	}
	
	/**
	 * Print the evaluation stack as a string. 
	 * @return the evaluation stack rendered as a string.
	 */
	public String debugString() {
		return evalStack.toString();
	}
	
	
	//// Standard operators
	
	/// Stack Operations
	
	private static void pop(Engine e) {
		e.pop();
	}
	
	private static void roll(Engine e) {
		// TODO
		Object rotate = e.pop();
		Object numbers = e.pop();
		Double rollj =(Double)rotate;
		Double rolln = (Double)numbers;
		int j = rollj.intValue();
		int n = rolln.intValue();
		Object[] values = new Object[n];
		for(int i = n-1; i>=0; --i) {
			values[i] = e.pop();
		}
		Object temp;
		if(j>0) {
			for(int h=0; h<j; h++) {
				for(int i=values.length-1; i>0; i--) {
					temp = values[i];
					values[i] = values[i-1];
					values[i-1]= temp;
				}
			}
		}
		else if(j<0) {
			for(int h=0; h>j; h--) {
				for(int i=0; i<values.length-1; i++) {
					temp = values[i];
					values[i] = values[i+1];
					values[i+1]= temp;
				}
			}
		}
		
		
		for(int i =0; i<n; i++) {
			e.evalStack.push(values[i]);
		}
		
		
		
	}
	
	private static void copy(Engine e) {
		Object top = e.pop();
		if (top instanceof Array) copy(e,(Array)top);
		else if (top instanceof Dictionary) copy(e,(Dictionary)top);
		else if (top instanceof Double) {
			Double d = (Double)top;
			int n = d.intValue();
			Object[] values = new Object[n];
			for (int i=n-1; i >= 0; --i) {
				values[i] = e.pop();
			}
			for (int i=0; i < n; ++i) {
				e.evalStack.push(values[i]);
			}
			for (int i=0; i < n; ++i) {
				e.evalStack.push(values[i]);
			}
		} else {
			throw new ExecutionException("unknown copy");
		}
	}
	
	private static void index(Engine e) {
		// TODO
		Object top = e.evalStack.pop();
		
	
			Double d = (Double)top;
			int n = d.intValue();
			Object[] values = new Object[n];
			for(int i =n-1; i>=0; --i) {
				values[i]= e.pop();
			}
			Object index = e.evalStack.peek();
			for (int i=0; i < n; ++i) {
				e.evalStack.push(values[i]);
			}
			
			e.evalStack.push(index);
		}
	
		
	
	
	private static void clear(Engine e) {
		e.evalStack.clear();
	}
	
	private static void count(Engine e) {
		// TODO
		double size = e.evalStack.size();
		e.evalStack.push(size);
	}
	
	private static void mark(Engine e) {
		e.push(Mark.getInstance());
	}
	
	private static void counttomark(Engine e) {
		// TODO
			
				Double d = (double) e.evalStack.size();
				int n = d.intValue();
				Object[] values = new Object[n];
				double markIndex=0;
				for(int i = n-1; e.evalStack.peek() != Mark.getInstance(); --i)
				{
					values[i] = e.pop();
					markIndex++;
				}
				for (int i=0; i < n; ++i) {
					if(values[i]!=null)
					e.evalStack.push(values[i]);
				}
				e.evalStack.push(markIndex);
				
		
	}
	
	private static void cleartomark(Engine e) {
		// TODO
		while(e.evalStack.peek() != Mark.getInstance()) {
			e.pop();
		}
		e.pop();
	}
	
	
	/// Math operations
	
	private static void add(Engine e) {
		Double d2 = (Double)e.pop();
		Double d1 = (Double)e.pop();
		e.push(d1+d2);
	}
	
	private static void sub(Engine e) {
		Double d2 = (Double)e.pop();
		Double d1 = (Double)e.pop();
		e.push(d1-d2);
	}
	
	private static void mul(Engine e) {
		Double d2 = (Double)e.pop();
		Double d1 = (Double)e.pop();
		e.push(d1*d2);
	}
	
	private static void div(Engine e) {
		Double d2 = (Double)e.pop();
		Double d1 = (Double)e.pop();
		e.push(d1/d2);
	}
	
	private static void idiv(Engine e) {
		Double d2 = (Double)e.pop();
		Double d1 = (Double)e.pop();
		e.push(Double.valueOf(d1.intValue()/d2.intValue()));
	}
	
	private static void mod(Engine e) {
		Double d2 = (Double)e.pop();
		Double d1 = (Double)e.pop();
		e.push(Double.valueOf(d1.intValue()%d2.intValue()));
	}

	private static void neg(Engine e) {
		Double d1 = (Double)e.pop();
		e.push(-d1);
	}

	private static void abs(Engine e) {
		Double d1 = (Double)e.pop();
		e.push(Math.abs(d1));
	}

	private static void ceiling(Engine e) {
		Double d1 = (Double)e.pop();
		e.push(Math.ceil(d1));
	}

	private static void floor(Engine e) {
		Double d1 = (Double)e.pop();
		e.push(Math.floor(d1));
	}

	private static void truncate(Engine e) {
		Double d1 = (Double)e.pop();
		e.push(Double.valueOf(d1.intValue()));
	}

	private static void round(Engine e) {
		Double d1 = (Double)e.pop();
		e.push(Math.round(d1));
	}
	
	private static void sqrt(Engine e) {
		Double d1 = (Double)e.pop();
		e.push(Math.sqrt(d1));
	}
	
	private static void atan(Engine e) {
		Double dx = (Double)e.pop();
		Double dy = (Double)e.pop();
		e.push(Math.toDegrees(Math.atan2(dy, dx)));
	}
	
	private static void cos(Engine e) {
		Double d1 = (Double)e.pop();
		e.push(Math.cos(Math.toRadians(d1)));
	}
	
	private static void sin(Engine e) {
		Double d1 = (Double)e.pop();
		e.push(Math.sin(Math.toRadians(d1)));
	}
	
	private static void exp(Engine e) {
		Double d1 = (Double)e.pop();
		e.push(Math.exp(d1));
	}
	
	private static void ln(Engine e) {
		Double d1 = (Double)e.pop();
		e.push(Math.log(d1));
	}
	
	private static void log(Engine e) {
		Double d1 = (Double)e.pop();
		e.push(Math.log10(d1));
	}
	
	private static void rand(Engine e) {
		int result = e.random.nextInt();
		if (result < 0) result = -(result+1);
		e.push(Double.valueOf(result));
	}
	
	private static void srand(Engine e) {
		Double d1 = (Double)e.pop();
		e.random.setSeed(d1.longValue());
	}
	
	// rrand too difficult for now.
	
	private static void cvi(Engine e) {
		Object arg = e.pop();
		if (arg instanceof String) {
			e.push(Integer.parseInt((String)arg));
		} else {
			e.push(((Double)arg).intValue());
		}
	}
	
	private static void cvn(Engine e) {
		String s = (String)e.pop();
		e.push(new Name(s));
	}
	
	private static void cvr(Engine e) {
		Object arg = e.pop();
		if (arg instanceof String) {
			e.push(Double.parseDouble((String)arg));
		} else {
			e.push(((Double)arg).doubleValue());
		}
	}
	
	
	/// Arrays and Dictionaries
	
	private static void array(Engine e) {
		Double d1 = (Double)e.pop();
		e.evalStack.push(new DefaultArray(new Object[d1.intValue()]));
	}
	
	private static void _arrayEnd(Engine e) {
		// TODO
		counttomark(e);
		Object count = e.pop();
		
		Double d = (double) count;
		int n = d.intValue();
		Object[] defaultArray = new Object[n];
		for(int i = n-1; e.evalStack.peek() != Mark.getInstance(); --i)
		{
			defaultArray[i] = e.pop();
		}
		e.pop();
		e.evalStack.push(new DefaultArray(defaultArray));
	}
	
	private static void length(Engine e) {
		Object top = e.pop();
		int l;
		if (top instanceof Array) {
			l = ((Array)top).length();
		} else if (top instanceof Dictionary) {
			l = ((Dictionary)top).size();
		} else if (top instanceof String) {
			l = ((String)top).length();
		} else {
			throw new ClassCastException("Unlnown object for length: " + top);
		}
		e.push(Double.valueOf(l));
	}
	
	private static void get(Engine e) {
		Object index = e.pop();
		Object collection = e.pop();
		if (collection instanceof Dictionary) {
			e.push(((Dictionary)collection).get((Name)index));
		} else {
			int i = ((Double)index).intValue();
			Array array = ((Array)collection);
			e.push(array.get(i));
		}
	}
	
	private static void put(Engine e) {
		Object any = e.pop();
		Object index = e.pop();
		Object collection = e.pop();
		if (collection instanceof Dictionary) {
			((Dictionary)collection).put((Name)index, any);
		} else {
			int i = ((Double)index).intValue();
			Array array = (Array)collection;
			array.put(i,any);
		}
	}
	
	private static void getinterval(Engine e) {
		int count = ((Double)e.pop()).intValue();
		int low = ((Double)e.pop()).intValue();
		Array array = (Array)e.pop();
		if (array.length() < count + low) throw new ArrayIndexOutOfBoundsException(count+low);
		e.push(new Subarray(array,low,low+count));
	}
	
	private static void putinterval(Engine e) {
		Array array2 = (Array)e.pop();
		int low = ((Double)e.pop()).intValue();
		Array array1 = (Array)e.pop();
		int n = array2.length();
		for (int i = 0; i < n; ++i) {
			array1.put(i+low,array2.get(i));
		}
	}
	
	private static void aload(Engine e) {
		Array array1 = (Array)e.pop();
		for (Object o : array1) {
			e.push(o);
		}
		e.push(array1);
	}
	
	private static void astore(Engine e) {
		Array array = (Array)e.pop();
		int n = array.length();
		for (int i=n-1; i >=0; --i) {
			array.put(i,e.pop());
		}
		e.push(array);
	}
	
	private static void copy(Engine e, Array array2) {
		Array array1 = (Array)e.pop();
		int n1 = array1.length();
		int n2 = array2.length();
		if (n1 > n2) throw new ArrayIndexOutOfBoundsException(n2-1);
		for (int i =0; i < n1; ++i) {
			array2.put(i,array1.get(i));
		}
		if (n1 == n2) {
			e.push(array2);
		} else {
			e.push(new Subarray(array2,0,n1));
		}
	}
	
	private static void forall(Engine e) {
		Procedure p = (Procedure)e.pop();
		Object compound = e.pop();
		Iterable<Object> coll;
		if (compound instanceof Array) {
			coll = (Array)compound;
		} else if (compound instanceof Dictionary) {
			coll = ((Dictionary)compound).values();
		} else {
			throw new ExecutionException("Not a compound: " + compound);
		}
		for (Object o : coll) {
			e.push(o);
			p.execute(e);
		}
	}
	
	private static void dict(Engine e) {
		e.pop();
		e.push(new Dictionary());
	}
	
	private static void begin(Engine e) {
		Dictionary d = (Dictionary)e.pop();
		e.dictStack.push(d);
	}
	
	private static void end(Engine e) {
		e.dictStack.pop();
	}
	
	private static void def(Engine e) {
		Object any = e.pop();
		Name n = (Name)e.pop();
		e.dictStack.peek().put(n,any);
	}
	
	private static void load(Engine e) {
		Name n = (Name)e.pop();
		e.push(e.lookup(n));
	}
	
	private static void store(Engine e) {
		Object any = e.pop();
		Name n = (Name)e.pop();
		Stack<Dictionary> cl = e.dictStack.clone();
		while (!cl.isEmpty()) {
			Dictionary d = cl.pop();
			if(d.known(n)) {
				d.put(n,any);
				return;
			}
		}
		throw new ExecutionException("Store not possible for " + n);
	}
	
	private static void known(Engine e) {
		Name n = (Name)e.pop();
		Dictionary dict = (Dictionary)e.pop();
		if (dict.known(n)) {
			e.push(Boolean.TRUE);
		} else {
			e.push(Boolean.FALSE);
		}
	}
	
	private static void where(Engine e) {
		Name n = (Name)e.pop();
		Stack<Dictionary> cl = e.dictStack.clone();
		while (!cl.isEmpty()) {
			Dictionary d = cl.pop();
			if(d.known(n)) {
				e.push(d);
				e.push(Boolean.TRUE);
				return;
			}
		}
		e.push(Boolean.FALSE);
	}
	
	private static void copy(Engine e, Dictionary dict2) {
		Dictionary dict1 = (Dictionary)e.pop();
		dict2.copy(dict1);
		e.push(dict2);
	}
	
	private static void currentdict(Engine e) {
		e.push(e.dictStack.peek());
	}
	
	private static void countdictstack(Engine e) {
		e.push(Double.valueOf(e.dictStack.size()));
	}
	
	private static void dictstack(Engine e) {
		Array a = (Array) e.pop();
		if (a.length() < e.dictStack.size()) {
			throw new IndexOutOfBoundsException("array too small");
		}
		int n = e.dictStack.size();
		Stack<Dictionary> cl = e.dictStack.clone();
		for (int i=0; i < n; ++i) {
			a.put(n-i-1, cl.pop());
		}
		if (n == a.length()) e.push(a);
		else e.push(new Subarray(a,0,n));
	}
	
	private static void bind(Engine e) {
		Object x = e.pop();
		if (x instanceof Procedure) {
			((Procedure)x).bind(e);
		}
		e.push(x);
	}
	
	
	
	/// Boolean operators
	
	
	private static void eq(Engine e) {
		Object e1 = e.pop();
		Object e2 = e.pop();
		e.push(Boolean.valueOf(e1 == e2));
	}
	
	private static void ge(Engine e) {
		Object e1 = e.pop();
		Object e2 = e.pop();
		boolean result;
		if (e1 instanceof String) {
			result = ((String)e1).compareTo((String)e2) >= 0;
		} else {
			result = ((Double)e1) >= ((Double)e2);
		}
		e.push(Boolean.valueOf(result));
	}
	
	private static void gt(Engine e) {
		Object e1 = e.pop();
		Object e2 = e.pop();
		boolean result;
		if (e1 instanceof String) {
			result = ((String)e1).compareTo((String)e2) > 0;
		} else {
			result = ((Double)e1) > ((Double)e2);
		}
		e.push(Boolean.valueOf(result));
		
	}
	
	private static void and(Engine e) {
		Object e1 = e.pop();
		Object e2 = e.pop();
		if (e1 instanceof Boolean) {
			e.push(Boolean.valueOf(((Boolean)e1) && ((Boolean)e2)));
		} else {
			e.push(Double.valueOf(((Double)e1).intValue() & ((Double)e2).intValue()));
		}
	}
	
	private static void or(Engine e) {
		Object e1 = e.pop();
		Object e2 = e.pop();
		if (e1 instanceof Boolean) {
			e.push(Boolean.valueOf(((Boolean)e1) || ((Boolean)e2)));
		} else {
			e.push(Double.valueOf(((Double)e1).intValue() | ((Double)e2).intValue()));
		}
	}
	
	private static void not(Engine e) {
		Object e1 = e.pop();
		if (e1 instanceof Boolean) {
			e.push(Boolean.valueOf(!((Boolean)e1)));
		} else {
			e.push(Double.valueOf(~(((Double)e1).intValue())));
		}
	}
	
	private static void xor(Engine e) {
		Object e1 = e.pop();
		Object e2 = e.pop();
		if (e1 instanceof Boolean) {
			e.push(Boolean.valueOf(!((Boolean)e1) == ((Boolean)e2)));
		} else {
			e.push(Double.valueOf(((Double)e1).intValue() ^ ((Double)e2).intValue()));
		}
		
	}
	
	private static void bitshift(Engine e) {
		int shft = ((Double)e.pop()).intValue();
		int val = ((Double)e.pop()).intValue();
		if (shft > 0) {
			val <<= shft;
		} else {
			val >>= shft;
		}
		e.push(Double.valueOf(val));
	}
	
	
	
	/// Control flow
	
	
	private static void exec(Engine e) {
		Object o = e.pop();
		if (o instanceof Procedure) {
			((Procedure)o).execute(e);
		} else if (o instanceof Operator) {
			e.execute(o);
		} else {
			e.push(o);
		}
	}
	
	private static void ifelse(Engine e) {
		Procedure p2 = (Procedure)e.pop();
		Procedure p1 = (Procedure)e.pop();
		Boolean b = (Boolean)e.pop();
		if (b.booleanValue()) {
			e.execute(p1);
		} else {
			e.execute(p2);
		}
	}
	
	private static void for_(Engine e) {
		Procedure p = (Procedure)e.pop();
		double l = ((Double)e.pop()).doubleValue();
		double k = ((Double)e.pop()).doubleValue();
		double j = ((Double)e.pop()).doubleValue();
		try {
			if (k < 0) {
				for (double i=j; i >= l; i += k) {
					e.push(i);
					p.execute(e);
				}
			} else {
				for (double i=j; i <= l; i += k) {
					e.push(i);
					p.execute(e);
				}
			}
		} catch (ExitException e1) {
			// done!
		}
	}
	
	private static void repeat(Engine e) {
		Procedure p = (Procedure)e.pop();
		int n = ((Double)e.pop()).intValue();
		try {
			for (int i=0; i < n; ++i) {
				e.execute(p);
			}
		} catch (ExitException e1) {
			// done
		}
	}
	
	private static void loop(Engine e) {
		Procedure p = (Procedure)e.pop();
		try {
			while (true) {
				e.execute(p);
			}
		} catch (ExitException e1) {
			// done
		}
	}
	
	private static void exit(Engine e) {
		throw new ExitException();
	}
	
	private static void quit(Engine e) {
		System.exit(1);
	}
	
	
	
	/// Debugging
	
	
	private static void print(Engine e) {
		Object x = e.pop();
		System.out.print(x);
	}
	
	private static void println(Engine e) {
		print(e);
		System.out.println();
	}
	
	private static void stack(Engine e) {
		Stack<?> copy = (Stack<?>)e.evalStack.clone();
		while (!copy.isEmpty()) {
			System.out.print(copy.pop() + " ");
		}
	}

	
	private static void pstack(Engine e) {
		Stack<?> copy = (Stack<?>)e.evalStack.clone();
		while (!copy.isEmpty()) {
			System.out.println(copy.pop());
		}
	}

	
	
	/// Graphics
	
	private static void gsave(Engine e) {
		e.graphicsStack.push(e.graphicsStack.peek().copy());
	}
	
	private static void grestore(Engine e) {
		e.graphicsStack.pop();
	}
	
	private static void grestoreall(Engine e) {
		while (e.graphicsStack.size() > 1) {
			grestore(e);
		}
	}
	
	private static void initgraphics(Engine e) {
		e.graphicsStack.pop();
		e.graphicsStack.push(e.createGraphicsState());
	}
	
	private BasicStroke getBasicStroke() {
		return (BasicStroke)graphicsStack.peek().graphics.getStroke();
	}
	private void setStroke(BasicStroke str) {
		graphicsStack.peek().graphics.setStroke(str);
	}
	
	private static void setlinewidth(Engine e) {
		Double d = (Double)e.pop();
		BasicStroke str = e.getBasicStroke();
		str = new BasicStroke(d.floatValue(),str.getEndCap(),str.getLineJoin(),str.getMiterLimit(),str.getDashArray(),str.getDashPhase());
		e.setStroke(str);
	}
	
	private static void currentlinewidth(Engine e) {
		BasicStroke str = e.getBasicStroke();
		e.push(Double.valueOf(str.getLineWidth()));
	}
	
	private static void setlinecap(Engine e) {
		Double d = (Double)e.pop();
		BasicStroke str = e.getBasicStroke();
		str = new BasicStroke(str.getLineWidth(),d.intValue(),str.getLineJoin(),str.getMiterLimit(),str.getDashArray(),str.getDashPhase());
		e.setStroke(str);
	}
	
	private static void currentlinecap(Engine e) {
		BasicStroke str = e.getBasicStroke();
		e.push(Double.valueOf(str.getEndCap()));
	}
	
	private static void setlinejoin(Engine e) {
		Double d = (Double)e.pop();
		BasicStroke str = e.getBasicStroke();
		str = new BasicStroke(str.getLineWidth(),str.getEndCap(),d.intValue(),str.getMiterLimit(),str.getDashArray(),str.getDashPhase());
		e.setStroke(str);
	}
	
	private static void currentlinejoin(Engine e) {
		BasicStroke str = e.getBasicStroke();
		e.push(Double.valueOf(str.getLineJoin()));
	}
	
	private static void setmiterlimit(Engine e) {
		Double d = (Double)e.pop();
		BasicStroke str = e.getBasicStroke();
		str = new BasicStroke(str.getLineWidth(),str.getEndCap(),str.getLineJoin(),d.floatValue(),str.getDashArray(),str.getDashPhase());
		e.setStroke(str);
	}
	
	private static void currentmiterlimit(Engine e) {
		BasicStroke str = e.getBasicStroke();
		e.push(Double.valueOf(str.getMiterLimit()));
	}
	
	private static void setdash(Engine e) {
		Double d = (Double)e.pop();
		Array a = (Array)e.pop();
		BasicStroke str = e.getBasicStroke();
		int n = a.length();
		float[] dashes = new float[n];
		for (int i=0; i < n; ++i) {
			dashes[i] = ((Double)a.get(i)).floatValue();
		}
		str = new BasicStroke(str.getLineWidth(),str.getEndCap(),str.getLineJoin(),str.getMiterLimit(),
				dashes,d.floatValue());
		e.setStroke(str);
	}
	
	private static void currentdash(Engine e) {
		BasicStroke str = e.getBasicStroke();
		float[] dashes = str.getDashArray();
		Object[] contents = new Object[dashes.length];
		for (int i=0; i < dashes.length; ++i) {
			contents[i] = dashes[i];
		}
		e.push(new DefaultArray(contents));
		e.push(Double.valueOf(str.getDashPhase()));
	}
	
	private Color getColor() {
		return graphicsStack.peek().graphics.getColor();
	}
	private void setColor(Color c) {
		graphicsStack.peek().graphics.setColor(c);
	}
	
	private static void setgray(Engine e) {
		Double d = (Double)e.pop();
		e.setColor(Color.getHSBColor(0, 0, d.floatValue()));
	}
	
	private static void currentgray(Engine e) {
		Color c = e.getColor();
		float[] g = c.getColorComponents(ColorSpace.getInstance(ColorSpace.CS_GRAY),null);
		e.push(Double.valueOf(g[0]));
	}
	
	private static void sethsbcolor(Engine e) {
		Double b = (Double)e.pop();
		Double s = (Double)e.pop();
		Double h = (Double)e.pop();
		Color c = Color.getHSBColor(h.floatValue(), s.floatValue(), b.floatValue());
		e.setColor(c);
	}
	
	private static void currenthsbcolor(Engine e) {
		Color c = e.getColor();
		float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		e.push(hsb[0]);
		e.push(hsb[1]);
		e.push(hsb[2]);		
	}
	
	private static int asByte(Object x) {
		return (int)(((Double)x).doubleValue()*255);
	}
	
	private static void setrgbcolor(Engine e) {
		int b = asByte(e.pop());
		int g = asByte(e.pop());
		int r = asByte(e.pop());
		e.setColor(new Color(r,g,b));
	}
	
	private static void currentrgbcolor(Engine e) {
		Color c = e.getColor();
		e.push(c.getRed()/255.0);
		e.push(c.getGreen()/255.0);
		e.push(c.getBlue()/255.0);
	}
	
	private static void translate(Engine e) {
		Double ty = (Double)e.pop();
		Double tx = (Double)e.pop();
		GraphicsState gst = e.graphicsStack.peek();
		// we need to update the path in case its gets drawn.
		gst.path.transform(AffineTransform.getTranslateInstance(-tx, -ty));
		Graphics2D g = gst.graphics;		
		g.translate(tx, ty);
	}
	
	private static void scale(Engine e) {
		Double sy = (Double)e.pop();
		Double sx = (Double)e.pop();
		GraphicsState gst = e.graphicsStack.peek();
		// we need to update the path created so far to "undo" the scale
		gst.path.transform(AffineTransform.getScaleInstance(1/sx, 1/sy));
		Graphics2D g = gst.graphics;
		g.scale(sx, sy);
	}
	
	private static void rotate(Engine e) {
		Double a = (Double)e.pop();
		GraphicsState gst = e.graphicsStack.peek();
		double theta = Math.toRadians(a);
		// we need to update the path created so far to "undo" the rotation:
		gst.path.transform(AffineTransform.getRotateInstance(-theta));
		Graphics2D g = gst.graphics;
		g.rotate(theta);
	}
	
	
	/// Paths
	
	
	private static void newpath(Engine e) {
		GraphicsState gst = e.graphicsStack.peek();
		gst.path.reset();
	}
	
	private static void currentpoint(Engine e) {
		GraphicsState gst = e.graphicsStack.peek();
		Point2D p = gst.path.getCurrentPoint();
		if (p == null) {
			e.push(0);
			e.push(0);
		} else {
			e.push(p.getX());
			e.push(p.getY());
		}
	}
	
	private static void moveto(Engine e) {
		Double y = (Double)e.pop();
		Double x = (Double)e.pop();
		GraphicsState gst = e.graphicsStack.peek();
		gst.path.moveTo(x.floatValue(), y.floatValue());
	}
	
	private static void lineto(Engine e) {
		GraphicsState gst = e.graphicsStack.peek();
		Double y = (Double)e.pop();
		Double x = (Double)e.pop();
		gst.path.lineTo(x.floatValue(), y.floatValue());
	}
	
	protected double popAngle() {
		Double a = (Double)pop();
		while (a < 0) a += 360;
		while (a > 360) a -= 360;
		return a;
	}
	
	private static void arc(Engine e) {
		double angle2 = e.popAngle();
		double angle1 = e.popAngle();
		Double radius = (Double)e.pop();
		Double centery = (Double)e.pop();
		Double centerx = (Double)e.pop();
		Arc2D.Double arc = new Arc2D.Double();
		arc.width = radius*2;
		arc.height = radius*2;
		arc.x = centerx - radius;
		arc.y = centery - radius;
		arc.start = -angle1;
		double extent = angle1 - angle2;
		if (extent > 0) extent -= 360;
		arc.extent = extent;
		GraphicsState gst = e.graphicsStack.peek();
		gst.path.append(arc, true);
	}
	
	private static void arcn(Engine e) {
		double angle2 = e.popAngle();
		double angle1 = e.popAngle();
		Double radius = (Double)e.pop();
		Double centery = (Double)e.pop();
		Double centerx = (Double)e.pop();
		Arc2D.Double arc = new Arc2D.Double();
		arc.width = radius*2;
		arc.height = radius*2;
		arc.x = centerx - radius;
		arc.y = centery - radius;
		arc.start = angle1;
		double extent = angle2 - angle1;
		if (extent < 0) extent += 360;
		arc.extent = extent;
		GraphicsState gst = e.graphicsStack.peek();
		gst.path.append(arc, true);
	}
	
	private static void curveto(Engine e) {
		Double y3 = (Double)e.pop();
		Double x3 = (Double)e.pop();
		Double y2 = (Double)e.pop();
		Double x2 = (Double)e.pop();
		Double y1 = (Double)e.pop();
		Double x1 = (Double)e.pop();
		GraphicsState gst = e.graphicsStack.peek();
		gst.path.curveTo(x1, y1, x2, y2, x3, y3);
	}
	
	private static void closepath(Engine e) {
		GraphicsState gst = e.graphicsStack.peek();
		gst.path.closePath();
	}
	
	private static void fill(Engine e) {
		GraphicsState gst = e.graphicsStack.peek();
		gst.graphics.fill(gst.path);
		gst.path.reset();
	}
	
	private static void stroke(Engine e) {
		GraphicsState gst = e.graphicsStack.peek();
		gst.graphics.draw(gst.path);
		gst.path.reset();
	}
	
	private static void erasepage(Engine e){
		e.clearImage();
	}
	
	private static void showpage(Engine e) {
		try {
			SwingUtilities.invokeAndWait(() -> {
				e.window.setVisible(true);
			});
		} catch (InvocationTargetException | InterruptedException e1) {
			e1.printStackTrace();
		}
		e.clearImage();
		//grestoreall(e);
		//initgraphics(e);
	}

	
	
	/// Fonts
	
	
	private static void findfont(Engine e) {
		Name n = (Name)e.pop();
		Font f = new Font(n.rep,Font.PLAIN,1);
		e.push(f);
	}
	
	private static void scalefont(Engine e) {
		Double d = (Double)e.pop();
		Font f = (Font)e.pop();
		e.push(new Font(f.getName(),f.getStyle(),d.intValue()));
	}
	
	private static void setfont(Engine e) {
		Font f = (Font)e.pop();
		GraphicsState gst = e.graphicsStack.peek();
		gst.graphics.setFont(f);
	}
	
	private static void currentfont(Engine e) {
		GraphicsState gst = e.graphicsStack.peek();
		e.push(gst.graphics.getFont());
	}
	
	private static void show(Engine e) {
		Object any = e.pop();
		GraphicsState gst = e.graphicsStack.peek();
		Point2D p = gst.path.getCurrentPoint();
		Graphics2D g = (Graphics2D)gst.graphics.create();
		g.translate(p.getX(),p.getY());
		g.scale(1,-1); // draw upside down, because PostScript uses opposite coordinates.
		g.drawString(any.toString(), 0, 0);
	}
	
	
	//// System Dictionary
	
	private static Dictionary createSystemDict() {
		Dictionary result = new Dictionary();
		result.put(new Name("systemdict"),result);
		result.put(new Name("pop"),(Operator)((Engine e) -> { pop(e); }));
		result.put(new Name("roll"),(Operator)Engine::roll);
		result.put(new Name("copy"),(Operator)Engine::copy);
		result.put(new Name("index"),(Operator)Engine::index);
		result.put(new Name("clear"),(Operator)Engine::clear);
		result.put(new Name("count"),(Operator)Engine::count);
		result.put(new Name("mark"),(Operator)Engine::mark);
		result.put(new Name("counttomark"),(Operator)Engine::counttomark);
		result.put(new Name("cleartomark"),(Operator)Engine::cleartomark);
		result.put(new Name("add"),(Operator)Engine::add);
		result.put(new Name("sub"),(Operator)Engine::sub);
		result.put(new Name("mul"),(Operator)Engine::mul);
		result.put(new Name("div"),(Operator)Engine::div);
		result.put(new Name("idiv"),(Operator)Engine::idiv);
		result.put(new Name("mod"),(Operator)Engine::mod);
		result.put(new Name("neg"),(Operator)Engine::neg);
		result.put(new Name("abs"),(Operator)Engine::abs);
		result.put(new Name("ceiling"),(Operator)Engine::ceiling);
		result.put(new Name("floor"),(Operator)Engine::floor);
		result.put(new Name("truncate"),(Operator)Engine::truncate);
		result.put(new Name("round"),(Operator)Engine::round);
		result.put(new Name("sqrt"),(Operator)Engine::sqrt);
		result.put(new Name("atan"),(Operator)Engine::atan);
		result.put(new Name("cos"),(Operator)Engine::cos);
		result.put(new Name("sin"),(Operator)Engine::sin);
		result.put(new Name("exp"),(Operator)Engine::exp);
		result.put(new Name("ln"),(Operator)Engine::ln);
		result.put(new Name("log"),(Operator)Engine::log);
		result.put(new Name("rand"),(Operator)Engine::rand);
		result.put(new Name("srand"),(Operator)Engine::srand);
		result.put(new Name("cvi"),(Operator)Engine::cvi);
		result.put(new Name("cvn"),(Operator)Engine::cvn);
		result.put(new Name("cvr"),(Operator)Engine::cvr);
		
		// arrays and dictionaries
		result.put(new Name("array"),(Operator)Engine::array);
		result.put(new Name("["),(Operator)Engine::mark);
		result.put(new Name("]"),(Operator)Engine::_arrayEnd);
		result.put(new Name("length"),(Operator)Engine::length);
		result.put(new Name("get"),(Operator)Engine::get);
		result.put(new Name("put"),(Operator)Engine::put);
		result.put(new Name("getinterval"),(Operator)Engine::getinterval);
		result.put(new Name("outinterval"),(Operator)Engine::putinterval);
		result.put(new Name("aload"),(Operator)Engine::aload);
		result.put(new Name("astore"),(Operator)Engine::astore);
		result.put(new Name("forall"),(Operator)Engine::forall);
		result.put(new Name("dict"),(Operator)Engine::dict);
		result.put(new Name("begin"),(Operator)Engine::begin);
		result.put(new Name("end"),(Operator)Engine::end);
		result.put(new Name("def"),(Operator)Engine::def);
		result.put(new Name("load"),(Operator)Engine::load);
		result.put(new Name("store"),(Operator)Engine::store);
		result.put(new Name("known"),(Operator)Engine::known);
		result.put(new Name("where"),(Operator)Engine::where);
		result.put(new Name("currentdict"),(Operator)Engine::currentdict);
		result.put(new Name("countdictstack"),(Operator)Engine::countdictstack);
		result.put(new Name("dictstack"),(Operator)Engine::dictstack);
		result.put(new Name("bind"),(Operator)Engine::bind);
		// boolean operators
		result.put(new Name("eq"),(Operator)Engine::eq);
		result.put(new Name("ge"),(Operator)Engine::ge);
		result.put(new Name("gt"),(Operator)Engine::gt);
		result.put(new Name("and"),(Operator)Engine::and);
		result.put(new Name("or"),(Operator)Engine::or);
		result.put(new Name("not"),(Operator)Engine::not);
		result.put(new Name("xor"),(Operator)Engine::xor);
		result.put(new Name("true"),Boolean.TRUE);
		result.put(new Name("false"),Boolean.FALSE);
		result.put(new Name("bitshift"),(Operator)Engine::bitshift);
		// control flow
		result.put(new Name("exec"),(Operator)Engine::exec);
		result.put(new Name("ifelse"),(Operator)Engine::ifelse);
		result.put(new Name("for"),(Operator)Engine::for_);
		result.put(new Name("repeat"),(Operator)Engine::repeat);
		result.put(new Name("loop"),(Operator)Engine::loop);
		result.put(new Name("exit"),(Operator)Engine::exit);
		result.put(new Name("quit"),(Operator)Engine::quit);
		// debugging
		result.put(new Name("print"),(Operator)Engine::print);
		result.put(new Name("="),(Operator)Engine::print);
		result.put(new Name("=="),(Operator)Engine::println);
		result.put(new Name("stack"),(Operator)Engine::stack);
		result.put(new Name("pstack"),(Operator)Engine::pstack);
		// graphics
		result.put(new Name("gsave"),(Operator)Engine::gsave);
		result.put(new Name("grestore"),(Operator)Engine::grestore);
		result.put(new Name("grestoreall"),(Operator)Engine::grestoreall);
		result.put(new Name("initgraphics"),(Operator)Engine::initgraphics);
		result.put(new Name("setlinewidth"),(Operator)Engine::setlinewidth);
		result.put(new Name("currentlinewidth"),(Operator)Engine::currentlinewidth);
		result.put(new Name("setlinecap"),(Operator)Engine::setlinecap);
		result.put(new Name("currentlinecap"),(Operator)Engine::currentlinecap);
		result.put(new Name("setlinejoin"),(Operator)Engine::setlinejoin);
		result.put(new Name("currentlinejoin"),(Operator)Engine::currentlinejoin);
		result.put(new Name("setmiterlimit"),(Operator)Engine::setmiterlimit);
		result.put(new Name("currentmiterlimit"),(Operator)Engine::currentmiterlimit);
		result.put(new Name("setdash"),(Operator)Engine::setdash);
		result.put(new Name("currentdash"),(Operator)Engine::currentdash);
		result.put(new Name("setgray"),(Operator)Engine::setgray);
		result.put(new Name("currentgray"),(Operator)Engine::currentgray);
		result.put(new Name("sethsbcolor"),(Operator)Engine::sethsbcolor);
		result.put(new Name("currenthsbcolor"),(Operator)Engine::currenthsbcolor);
		result.put(new Name("setrgbcolor"),(Operator)Engine::setrgbcolor);
		result.put(new Name("getrgbcolor"),(Operator)Engine::currentrgbcolor);
		result.put(new Name("translate"),(Operator)Engine::translate);
		result.put(new Name("scale"),(Operator)Engine::scale);
		result.put(new Name("rotate"),(Operator)Engine::rotate);
		// path
		result.put(new Name("newpath"),(Operator)Engine::newpath);
		result.put(new Name("currentpoint"),(Operator)Engine::currentpoint);
		result.put(new Name("moveto"),(Operator)Engine::moveto);
		result.put(new Name("lineto"),(Operator)Engine::lineto);
		result.put(new Name("arc"),(Operator)Engine::arc);
		result.put(new Name("arcn"),(Operator)Engine::arcn);
		result.put(new Name("curveto"),(Operator)Engine::curveto);
		result.put(new Name("closepath"),(Operator)Engine::closepath);
		result.put(new Name("fill"),(Operator)Engine::fill);
		result.put(new Name("stroke"),(Operator)Engine::stroke);
		result.put(new Name("erasepage"),(Operator)Engine::erasepage);
		result.put(new Name("showpage"),(Operator)Engine::showpage);
		// fonts
		result.put(new Name("findfont"),(Operator)Engine::findfont);
		result.put(new Name("scalefont"),(Operator)Engine::scalefont);
		result.put(new Name("setfont"),(Operator)Engine::setfont);
		result.put(new Name("currentfont"),(Operator)Engine::currentfont);
		result.put(new Name("show"),(Operator)Engine::show);
		
		/*
		result.put(new Name(""),(Operator)Engine::);
		result.put(new Name(""),(Operator)Engine::);
		result.put(new Name(""),(Operator)Engine::);
		result.put(new Name(""),(Operator)Engine::);
		*/
		return result;
	}
}
