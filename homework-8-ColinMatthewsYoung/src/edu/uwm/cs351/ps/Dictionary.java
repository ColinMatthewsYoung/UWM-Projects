package edu.uwm.cs351.ps;

import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;

//Colin Young

public class Dictionary {

	private static class Node {
		Name key;
		Object data;
		Node left, right;
		Node(Name k, Object d) { key = k; data = d; }
	}
	
	private Node root;
	private int size;
	
	private static boolean doReport = true;
	
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
		
		
		if(r == null) return 0;
		if(r.key == null) return -1;
		
		if(lo!=null && r.key.rep.compareTo(lo)<=0) return -1;
		if(hi!=null && r.key.rep.compareTo(hi)>=0) return -1;
		
		
		
		int l=0;
		int rg =0;
		
		l= checkTree(r.left,lo,r.key.rep);
		rg= checkTree(r.right,r.key.rep,hi);
		if(l>=0 && rg>=0)
			return 1+l+rg;
		else
			return -1;
		
		 // TODO
	}
	
	 
	private boolean wellFormed() {
		// TODO; check things!
		// NB: If you correct check for things in range (and no duplicates)
		// then cycles will be automatically detected (they will be duplicates).
		
		if (checkTree(root,null,null)!= size)return false;
		
		
		
		
		return true;
	}
	
	/**
	 * Create an empty dictionary.
	 */
	public Dictionary() {
		// TODO
		root = null;
		size = 0;
		assert wellFormed() : "invariant broken in constructor";
	}
	
	// TODO: helper method to find a node in a subtree matching a given name
	// This code does the work of get and known.
	
	/**
	 * Return the definition of a name in this dictionary
	 * @param n name to lookup, may not be null
	 * @return definition for the name
	 * @throws ExecutionException if there is no definition, or if the name is null
	 */
	public Object get(Name n) throws ExecutionException {
		assert wellFormed() : "invariant broken at start of get()";
		// TODO
		if(n==null)throw new ExecutionException("undefined");
		
		Node temp = new Node(n, null);
		
		temp = search(root,n);
		
		
		if(temp==null || temp.key == null || temp.key.rep.compareTo(n.rep) !=0)
			throw new ExecutionException("undefined");
		
		return temp.data;
	}
	
	/**
	 * Return whether the parameter is a name in the dictionary
	 * @param n name to look up, may be null
	 * @return whether n is a name in the dictionary
	 */
	public boolean known(Name n) {
		assert wellFormed() : "invariant broken at start of known()";
		// TODO
		if(n == null) return false;
		Node temp = new Node(n, null);
		
		temp = search(root,n);
		if(temp !=null && temp.key.rep.compareTo(n.rep) == 0) return true;
		return false;
	}

	
	private static Node search(Node n, Name name) {
		Node temp = new Node(name, null);
		if(n==null || n.key.rep.compareTo(name.rep)==0) 
			return n;
		
		if(n.key.rep.compareTo(name.rep)>0) {
			temp =search(n.left,name);
		}
		if(n.key.rep.compareTo(name.rep)<0) {
			temp =search(n.right,name);
		}
		
		return temp;
	
	}
		
	
	
	/**
	 * Return the number of names defined in the dictionary.
	 * @return number of names in dictionary.
	 */
	public int size() {
		assert wellFormed() : "invariant broken at start of size()";
		return size; // TODO: very easy
	}
	
	// TODO: recommended: helper function for put
	
	/**
	 * Define a name in the dictionary.
	 * If the name already has a definition, the old definition is replaced
	 * with the new definition.
	 * @param n name to defined (must not be null)
	 * @param x (new) definition of the name (may be null)
	 * @exception ExecutionException if the name is null
	 */
	public void put(Name n, Object x) {
		assert wellFormed() : "invariant broken at start of put()";
		if(n==null)
			throw new ExecutionException("undefined");
		
		 Node temp = new Node(n,x);
		 root = doPut(root, temp);

		
		assert wellFormed() : "invariant broken at end of put()";
	}
	
	private Node doPut(Node r, Node n) {
		if(r == null) {
			r = new Node(n.key,n.data); 
			size++;
			return r;
		}
		if(r.key.rep.compareTo(n.key.rep)==0)r.data = n.data;
		
		if(r.key.rep.compareTo(n.key.rep)>0) 
			r.left =doPut(r.left,n);
			
		if(r.key.rep.compareTo(n.key.rep)<0)
			r.right =doPut(r.right,n);
		return r;
	}
	
	// TODO: Helper method for copy (use pre-order traversal)
	
	/**
	 * Copy all the definitions from the argument into this dictionary
	 * replacing previous definitions (if any).
	 * @param dict1 dictionary whose definition we copy (must not be null)
	 * NB: Behavior if the argument is null is not defined.
	 */
	public void copy(Dictionary dict1) {
		assert wellFormed() : "invariant broken at start of copy()";
		// TODO
		root = doCopy(root, dict1.root);
		assert wellFormed() : "invariant broken at end of copy()";
	}
	
	private Node doCopy(Node tree, Node copy) {
		if(copy==null) return tree;
		
		
		tree =doPut(tree,copy);
		tree =doCopy(tree,copy.left);
		tree =doCopy(tree,copy.right);
		
		
		return tree;
	}
	
	
	// TODO: Helper method for values (use in-order traversal)
	
	/**
	 * Return a collection with a copy of all the definitions
	 * in the dictionary in sorted order.
	 * @return a collection which has a copy of all definitions of names in the
	 * dictionary, in order of the names that were defined.
	 */
	public Collection<Object> values() {
		assert wellFormed() : "invariant broken at start of values()";
		Collection<Object> result = new ArrayList<>();
		
		doValues(root,result);
		
		// TODO
		return result;
	}
	private static void doValues(Node n, Collection<Object> results) {
		if(n == null) return;

		doValues(n.left,results);
		results.add(n.data);
		doValues(n.right,results);

		
	}
	// TODO: Helper method for toString (use in-order traversal) 
	
	/**
	 * Return a string of the form << name1 value1 name2 value2 ... namek valuek >>
	 * where the names are in order and everything is separated by single spaces.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// TODO
		sb.append("<< ");
		doToString(root,sb);
		sb.append(">>");
		return sb.toString();
	}
	
	private static void doToString(Node n, StringBuilder sb) {
		if(n== null)return;
		doToString(n.left,sb);
		if(n.data==null)
			sb.append("/" +n.key.rep +" null ");
		else
			sb.append("/" +n.key.rep +" " + n.data.toString() +" ");
		doToString(n.right,sb);
		
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
