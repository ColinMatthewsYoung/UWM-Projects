package edu.uwm.cs351.ps;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;
import edu.uwm.cs351.util.AbstractEntry;
import junit.framework.TestCase;

//Colin Young

public class Dictionary extends AbstractMap<Name,Object> {
	
	

	
	private Node root;
	private int size;
	private Set<Entry<Name,Object>> mySet;
	private int version;

	private static class Node extends AbstractEntry<Name,Object>{
		Name key;
		Object data;
		Node left, right;
		Node(Name k, Object d) { key = k; data = d; }
		@Override
		public Name getKey() {
			// TODO Auto-generated method stub
			return key;
		}
		@Override
		public Object getValue() {
			// TODO Auto-generated method stub
			return data;
		}
		
		
		public Object setValue(Object d) {
			Object old = data;
			data = d;
			return old;
		}
	}
	
	private class EntrySet extends AbstractSet<Entry<Name,Object>>{

		@Override
		public Iterator<Entry<Name,Object>> iterator() {
			// TODO Auto-generated method stub
			return new MyIterator();
			
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return Dictionary.this.size();
		}
		
		public void clear() {
			Dictionary.this.clear();
		}
		
		@Override
		public boolean contains(Object k) {
			Iterator<Entry<Name,Object>> i = iterator();
			
			while(i.hasNext()) {
			Entry<?,?> current = i.next();
			if(k instanceof Entry<?,?>)
			if(current.equals((Entry<?,?>)k))
				return true;
			}
			
			return false;
		}
	}
	
	private class MyIterator implements Iterator<Entry<Name,Object>>{
		int myVersion;
		private Stack<Node> cursorStack;
		private Node current;
		
		MyIterator(){
			myVersion = version;
			current = null;
			cursorStack = new Stack<Node>();
			start();
			
		}
		
		public boolean wellFormed() {
			if(myVersion!=Dictionary.this.version)return true;
			
			Node ancestor = null;
			
			for(Node node : cursorStack) {
				if(node == null) return report("null in stack");
				
			}
			
			
			return true;
		}
		
		public Node getCurrent() {
			return current;
		}
		
		public void start() {
			faleFast();
			cursorStack.clear();
			if(root != null) {
				cursorStack.add(root);
				while(cursorStack.peek().left != null) cursorStack.add(cursorStack.peek().left);
			}
			
		}
		
		private void faleFast() {
			if(myVersion!=version) throw new ConcurrentModificationException("Versions not in sync");
		}
		
		@Override
		public boolean hasNext() {
			faleFast();
			// TODO Auto-generated method stub
			return !cursorStack.isEmpty();
		}

		@Override
		public Entry<Name, Object> next() {
			// TODO Auto-generated method stub
			if(!hasNext()) throw new NoSuchElementException("No next element");
			
			if(!cursorStack.isEmpty()) {
				current = cursorStack.pop();
			
				if(current.right !=null) {
					cursorStack.push(current.right);
					while(cursorStack.peek().left!=null) cursorStack.push(cursorStack.peek().left);
				}
			
			}
			else
				current =null;
			
			return current;
		}
		
		public void remove()
		{
			faleFast();
			if(current==null) throw new IllegalStateException("no current element");
			Dictionary.this.remove(current.getKey());
			current = null;
			myVersion = version;
		}
		
	}
	
	
	
	private static boolean doReport = true;
	
	@Override
	public Set<Entry<Name, Object>> entrySet() {
		// TODO Auto-generated method stub
		return mySet;
	}
	
	
	@Override
	public Object remove(Object key) {
		// TODO Auto-generated method stub
		for(Map.Entry<Name,Object> entry : mySet) {
			if(entry.getKey().equals(key)) {
				Object value = entry.getValue();
				Node r = (Node) entry;
			
				root = doRemove(root,r);
				size--;
				version++;
				return value;
			}
		}
		return null;
		
	}
	
	private Node doRemove(Node r,Node n) {
		if(r==null)
			return null;
		if(r.key.rep.compareTo(n.key.rep)>0)
			r.left=doRemove(r.left,n);
		else if(r.key.rep.compareTo(n.key.rep)<0)
			r.right=doRemove(r.right,n);
			
		else if(r !=n)
				r.right=doRemove(r.right,n);
		
		else {
		if(r.left==null)
			return r.right;
		
		if(r.right==null)
			return r.left;
		
		Node  temp = r.right;
		Node  pTemp = r;
		while(temp.left!=null) {
			pTemp = temp;
			temp = temp.left;
			
		}
		
		temp.left = r.left;
		
		if(pTemp!=r) {
			pTemp.left = temp.right;
			temp.right = r.right;
		}
		
		
		
		
		r=temp;
		return r;
		}
		return r;
		}
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		if(size!=0) {
		root = null;
		size = 0;
		version++;
		}
	}

	
	private static boolean report(String s) {
		if (doReport) System.err.println("Invariant error: " + s);
		return false;
	}

	
	// This method is useful for checkTree if errors are found.
	private static int reportNeg(String s) {
		report(s);
		return -1;
	}
	
	/**
	 * Check if a subtree is well formed.
	 * None of the keys may be null and all of the keys must be
	 * in the given range (lo,hi) exclusive, except that a null bound means
	 * there is no bound in that direction, and finally all subtrees
	 * must also be well formed.
	 * @param r  root of the subtree, may be null
	 * @param lo exclusive lower bound.  If null, then no lower bound.
	 * @param hi exclusive upper bound.  If null, then no upper bound.
	 * @return number of nodes in subtree, if well formed, -1 otherwise.
	 */
	private static int checkTree(Node r, String lo, String hi) {
		if (r == null) return 0;
		if (r.key == null || r.key.rep == null) return reportNeg("null key found");
		if (lo != null && lo.compareTo(r.key.rep) >= 0 ||
		    hi != null && hi.compareTo(r.key.rep) <= 0) {
			return reportNeg("key out of place: " + r.key + " in (" + lo + "," + hi + ")");
		}
		int n1 = checkTree(r.left,lo,r.key.rep);
		int n2 = checkTree(r.right,r.key.rep,hi);
		if (n1 < 0 || n2 < 0) return -1;
		return n1 + n2 + 1;
	}
	
	private boolean wellFormed() {
		int n = checkTree(root,null,null);
		if (n < 0) return false;
		if (n != size) return report("Size wrong: " + size + " should be " + n);
		return true;
	}
	
	/**
	 * Create an empty dictionary.
	 */
	public Dictionary() {
		root = null;
		size = 0;
		version=0;
		mySet = new EntrySet();
		assert wellFormed() : "invariant broken in constructor";
	}
	
	private Node getNode(Node r, Name n) {
		if (r == null) return r;
		if (n == null) return null;
		int c = r.key.rep.compareTo(n.rep);
		if (c == 0) return r;
		if (c > 0) return getNode(r.left,n);
		else return getNode(r.right,n);
	}
	
	/**
	 * Return the definition of a name in this dictionary
	 * @param n name to lookup, may not be null
	 * @return definition for the name
	 * @throws ExecutionException if there is no definition, or if the name is null
	 */
	public Object get(Name n) throws ExecutionException {
		assert wellFormed() : "invariant broken at start of get()";
		Node r = getNode(root,n);
		if (r != null) return r.data;
		throw new ExecutionException("undefined");
	}
	
	//overload get();
	public Object get(Object k) {
		
		if(k instanceof Name) {
			Node r = getNode(root,(Name)k);
			if (r != null) return r.data;
		
		}
		return null;
	}
	
	
	/**
	 * Return whether the parameter is a name in the dictionary
	 * @param n name to look up, may be null
	 * @return whether n is a name in the dictionary
	 */
	public boolean known(Name n) {
		assert wellFormed() : "invariant broken at start of known()";
		Node r = getNode(root,n);
		if (r != null) return true;
		return false;
	}

	public boolean contains(Name n) {
		
		return known(n);
	}
	/**
	 * Return the number of names defined in the dictionary.
	 * @return number of names in dictionary.
	 */
	public int size() {
		assert wellFormed() : "invariant broken at start of size()";
		return size;
	}
	
	private Node doPut(Node r, Name n, Object d) {
		if (r == null) {
			r = new Node(n,d);
			size++;
			version++;
			
		} else {
			int c = r.key.rep.compareTo(n.rep);
			if (c == 0) r.data = d;
			else if (c > 0) r.left = doPut(r.left,n,d);
			else r.right = doPut(r.right,n,d);
		}
		return r;
	}
	
	/**
	 * Define a name in the dictionary.
	 * If the name already has a definition, the old definition is replaced
	 * with the new definition.
	 * @param n name to defined (must not be null)
	 * @param x (new) definition of the name (may be null)
	 * @exception ExecutionException if the name is null
	 */
	public Object put(Name n, Object x) {
		assert wellFormed() : "invariant broken at start of put()";
		Object old = get((Object)n);
		if (n == null || n.rep == null) throw new ExecutionException("null key not allowed");
		root = doPut(root,n,x);
		
		
		assert wellFormed() : "invariant broken at end of put()";
		
		return old;
	}
	
	
	
	private void doCopy(Node r) {
		if (r == null) return;
		put(r.key,r.data);
		doCopy(r.left);
		doCopy(r.right);
	}
	
	/**
	 * Copy all the definitions from the argument into this dictionary
	 * replacing previous definitions (if any).
	 * @param dict1 dictionary whose definition we copy (must not be null)
	 * NB: Behavior if the argument is null is not defined.
	 */
	public void copy(Dictionary dict1) {
		assert wellFormed() : "invariant broken at start of copy()";
		doCopy(dict1.root);
		assert wellFormed() : "invariant broken at start of copy()";
	}
	
	
	
	private void doToString(Node r, StringBuilder into) {
		if (r == null) return;
		doToString(r.left,into);
		into.append(' ');
		into.append(r.key);
		into.append(' ');
		into.append(r.data);
		doToString(r.right,into);
	}
	
	/**
	 * Return a string of the form << name1 value1 name2 value2 ... namek valuek >>
	 * where the names are in order and everything is separated by single spaces.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<<");
		doToString(root,sb);
		sb.append(" >>");
		return sb.toString();
	}
	
	public static class TestInvariant extends TestCase {
		protected Dictionary self;
		private Node[] n;
		private Name n0 = new Name("N");
		
		protected Name n(int i) {
			return new Name("N" + (1000+i));
		}
		
		
		
		
		@Override
		protected void setUp() {
			self = new Dictionary();
			self.root = null;
			self.size = 0;
			n = new Node[15];
			for (int i=0; i < 15; ++i) {
				n[i] = new Node(n(i*10),i);
			}
			n[0].key = null;
			n[6].left = n[2]; n[6].right = n[10];
			n[2].left = n[1]; n[2].right = n[5];
			n[5].left = n[3];
			n[3].right = n[4];
			n[10].left = n[9]; n[10].right = n[12];
			n[9].left = n[7];
			n[7].right = n[8];
			n[12].left = n[11]; n[12].right = n[13];
			doReport = false;
		}
		
		public void testA() {
			assertEquals(0,checkTree(null,null,null));
			assertEquals(-1,checkTree(n[0],null,null));
			assertEquals(1,checkTree(n[1],null,null));
			assertEquals(0,checkTree(null,"bar","foo"));
		}
		
		public void testB() {
			assertEquals(1,checkTree(n[1],n(9).rep,n(11).rep));
			assertEquals(1,checkTree(n[1],n(0).rep,null));
			assertEquals(1,checkTree(n[1],null,n(20).rep));
			assertEquals(-1,checkTree(n[1],n(9).rep,n(10).rep));
			assertEquals(-1,checkTree(n[1],n(10).rep,n(11).rep));
			assertEquals(-1,checkTree(n[1],n(10).rep,null));
			assertEquals(-1,checkTree(n[1],null,n(10).rep));
		}
		
		public void testC() {
			assertEquals(13,checkTree(n[6],null,null));
			assertEquals(13,checkTree(n[6],n(0).rep,n(140).rep));
			assertEquals(-1,checkTree(n[6],n(15).rep,null));
			assertEquals(-1,checkTree(n[6],null,n(125).rep));
		}
		
		public void testD() {
			self.root = null;
			self.size = 0;
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testE() {
			self.root = n[1];
			self.size = 0;
			assertFalse(self.wellFormed());
			self.size = 2;
			assertFalse(self.wellFormed());
			self.size = -1;
			assertFalse(self.wellFormed());
			self.size = 1;
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testF() {
			self.root = n[0];
			self.size = 1;
			assertFalse(self.wellFormed());
			n[0].key = n0;
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testG() {
			self.root = n[14];
			self.size = 2;
			n[14].right = n[1];
			assertFalse(self.wellFormed());
			n[14].right = null;
			n[14].left = n[1];
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testH() {
			self.root = n[1];
			self.size = 2;
			n[1].left = n[14];
			assertFalse(self.wellFormed());
			n[1].left = null;
			n[1].right = n[14];
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testI() {
			self.root = n[3]; // n[3]'s right is n[4]
			self.size = 3;
			n[4].left = n[0];
			assertFalse(self.wellFormed());
			self.size = 0;
			assertFalse(self.wellFormed());
			n[0].key = n0;
			assertFalse(self.wellFormed());
			self.size = 3;
			assertFalse(self.wellFormed());
			n[4].left = null;
			n[4].right = n[14];
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testJ() {
			self.root = n[5]; //n[5].left = n[3]; n[3].right = n[4]
			self.size = 4;
			n[4].right = n[14];
			assertFalse(self.wellFormed());
			self.size = 1;
			assertFalse(self.wellFormed());
			n[0].key = n0;
			assertFalse(self.wellFormed());
			self.size = 4;
			assertFalse(self.wellFormed());
			n[4].right = null;
			assertFalse(self.wellFormed());			
			self.size = 3;
			doReport = true;
			assertTrue(self.wellFormed());
		}

		public void testK() {
			self.root = n[5]; //n[5].left = n[3]; n[3].right = n[4]
			self.size = 4;
			n[4].left = n[0];
			assertFalse(self.wellFormed());
			self.size = 1;
			assertFalse(self.wellFormed());
			n[0].key = n0;
			assertFalse(self.wellFormed());
			self.size = 4;
			assertFalse(self.wellFormed());
			n[0].key = n(35);
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		
		// The following tests concern the tree
		// (((10)20(((30(40))50)60((70(80))90)))100((110)120(130)))
		
		public void testL() {
			self.root = n[6]; // whole tree
			self.size = 13;
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testM() {
			self.root = n[6];
			self.size = 14;
			
			n[1].left = n[0];
			assertFalse(self.wellFormed());
			n[0].key = n(10);
			assertFalse(self.wellFormed());
			n[1].left = n[1];
			assertFalse(self.wellFormed());
			
			n[1].left = n[0];
			n[0].key = n(05);
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testN() {
			self.root = n[6];
			self.size = 14;
			
			n[1].right = n[14];
			assertFalse(self.wellFormed());
			n[1].right = n[0];
			assertFalse(self.wellFormed());
			n[0].key = n(20);
			assertFalse(self.wellFormed());
			n[1].right = n[2];
			assertFalse(self.wellFormed());
			
			n[1].right = n[0];
			n[0].key = n(15);
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testO() {
			self.root = n[6];
			self.size = 14;
			
			n[3].left = n[1];
			assertFalse(self.wellFormed());
			n[3].left = n[0];
			assertFalse(self.wellFormed());
			n[0].key = n(20);
			assertFalse(self.wellFormed());
			n[3].left = n[2];
			assertFalse(self.wellFormed());
			
			n[3].left = n[0];
			n[0].key = n(25);
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testP() {
			self.root = n[6];
			self.size = 14;
			
			n[4].left = n[1];
			assertFalse(self.wellFormed());
			n[4].left = n[0];
			assertFalse(self.wellFormed());
			n[0].key = n(30);
			assertFalse(self.wellFormed());
			n[4].left = n[3];
			assertFalse(self.wellFormed());
			
			n[4].left = n[0];
			n[0].key = n(35);
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testQ() {
			self.root = n[6];
			self.size = 14;
			
			n[4].right = n[14];
			assertFalse(self.wellFormed());
			n[4].right = n[0];
			assertFalse(self.wellFormed());
			n[0].key = n(50);
			assertFalse(self.wellFormed());
			n[4].right = n[5];
			assertFalse(self.wellFormed());
			
			n[4].right = n[0];
			n[0].key = n(45);
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testR() {
			self.root = n[6];
			self.size = 14;
			
			n[5].right = n[14];
			assertFalse(self.wellFormed());
			n[5].right = n[0];
			assertFalse(self.wellFormed());
			n[0].key = n(60);
			assertFalse(self.wellFormed());
			n[5].right = n[6];
			assertFalse(self.wellFormed());
			
			n[5].right = n[0];
			n[0].key = n(55);
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testS() {
			self.root = n[6];
			self.size = 14;
			
			n[7].left = n[1];
			assertFalse(self.wellFormed());
			n[7].left = n[0];
			assertFalse(self.wellFormed());
			n[0].key = n(60);
			assertFalse(self.wellFormed());
			n[7].left = n[6];
			assertFalse(self.wellFormed());
			
			n[7].left = n[0];
			n[0].key = n(65);
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testT() {
			self.root = n[6];
			self.size = 14;
			
			n[8].left = n[1];
			assertFalse(self.wellFormed());
			n[8].left = n[0];
			assertFalse(self.wellFormed());
			n[0].key = n(70);
			assertFalse(self.wellFormed());
			n[8].left = n[7];
			assertFalse(self.wellFormed());
			
			n[8].left = n[0];
			n[0].key = n(75);
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testU() {
			self.root = n[6];
			self.size = 14;
			
			n[8].right = n[14];
			assertFalse(self.wellFormed());
			n[8].right = n[0];
			assertFalse(self.wellFormed());
			n[0].key = n(90);
			assertFalse(self.wellFormed());
			n[8].right = n[9];
			assertFalse(self.wellFormed());
			
			n[8].right = n[0];
			n[0].key = n(85);
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testV() {
			self.root = n[6];
			self.size = 14;
			
			n[9].right = n[14];
			assertFalse(self.wellFormed());
			n[9].right = n[0];
			assertFalse(self.wellFormed());
			n[0].key = n(100);
			assertFalse(self.wellFormed());
			n[9].right = n[10];
			assertFalse(self.wellFormed());
			
			n[9].right = n[0];
			n[0].key = n(95);
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testW() {
			self.root = n[6];
			self.size = 14;
			
			n[11].left = n[1];
			assertFalse(self.wellFormed());
			n[11].left = n[0];
			assertFalse(self.wellFormed());
			n[0].key = n(100);
			assertFalse(self.wellFormed());
			n[11].left = n[10];
			assertFalse(self.wellFormed());
			
			n[11].left = n[0];
			n[0].key = n(105);
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testX() {
			self.root = n[6];
			self.size = 14;
			
			n[11].right = n[14];
			assertFalse(self.wellFormed());
			n[11].right = n[0];
			assertFalse(self.wellFormed());
			n[0].key = n(120);
			assertFalse(self.wellFormed());
			n[11].right = n[12];
			assertFalse(self.wellFormed());
			
			n[11].right = n[0];
			n[0].key = n(115);
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testY() {
			self.root = n[6];
			self.size = 14;
			
			n[13].left = n[1];
			assertFalse(self.wellFormed());
			n[13].left = n[0];
			assertFalse(self.wellFormed());
			n[0].key = n(120);
			assertFalse(self.wellFormed());
			n[13].left = n[12];
			assertFalse(self.wellFormed());
			
			n[13].left = n[0];
			n[0].key = n(125);
			doReport = true;
			assertTrue(self.wellFormed());
		}
		
		public void testZ() {
			self.root = n[6];
			self.size = 14;
			
			n[13].right = n[0];
			assertFalse(self.wellFormed());
			n[0].key = n(130);
			assertFalse(self.wellFormed());
			n[13].right = n[13];
			assertFalse(self.wellFormed());
			
			n[13].right = n[14];
			doReport = true;
			assertTrue(self.wellFormed());
		}
	}

	
}
