// This is an assignment for students to complete after reading Chapter 4 of
// "Data Structures and Other Objects Using Java" by Michael Main.

package edu.uwm.cs351;

import java.awt.Color;

import javax.sound.midi.Sequence;

import junit.framework.TestCase;


/******************************************************************************
 * This class is a homework assignment;
 * A ParticleSeq is a collection of Notes.
 * The sequence can have a special "current element," which is specified and 
 * accessed through four methods
 * (start, getCurrent, advance and isCurrent).
 *
 ******************************************************************************/
public class ParticleSeq implements Cloneable
{
	// TODO: Declare the private static Node class.
	// It should have a constructor but no methods.
	// The fields of Node should have "default" access (neither public, nor private)
	// and should not start with underscores.
	private static class Node  {
		Particle data;
		Node next;
		
		public Node(Particle p, Node n) {
			this.data =p;
			this.next = n;
		}
		
	}
	
	// TODO: Declare the private fields of ParticleSeq needed for sequences
	// (in the textbook, page 226, five are recommended, 
	//  you declare all five.)
	private int manyNodes;
	private Node cursor;
	private Node precursor;
	private Node head;
	private Node tail;
	
	private static boolean doReport = true; // used only by invariant checker
	
	/**
	 * Used to report an error found when checking the invariant.
	 * By providing a string, this will help debugging the class if the invariant should fail.
	 * @param error string to print to report the exact error found
	 * @return false always
	 */
	private boolean report(String error) {
		if (doReport) System.out.println("Invariant error found: " + error);
		return false;
	}

	/**
	 * Check the invariant.  For information on what a class invariant is,
	 * please read page 123 in the textbook.
	 * Return false if any problem is found.  Returning the result
	 * of {@link #report(String)} will make it easier to debug invariant problems.
	 * @return whether invariant is currently true
	 */
	private boolean wellFormed() {
		// Invariant:
		// 1. The list must not include a cycle.
		// 2. "tail" must point to the last node in the list starting at "head".
		//    In particular, if the list is empty, "tail" must be null.
		// 3. "manyNodes" is number of nodes in list
		// 4. "precursor" is either null or points to a node in the list, other than the tail.
		// 5. "cursor" is the node after "precursor" (if "precursor" is not null),
		//    or is either null or the head node (otherwise).
		// This is the invariant of the data structure according to the
		// design in the textbook on pages 233-4 (3rd ed. 226-7)
		
		// Implementation:
		// Do multiple checks: each time returning false if a problem is found.
		// (Use "report" to give a descriptive report while returning false.)
		
		// We do the first one for you:
		// check that list is not cyclic
		if (head != null) {
			// This check uses the "tortoise and hare" algorithm attributed to Floyd.
			Node fast = head.next;
			for (Node p = head; fast != null && fast.next != null; p = p.next) {
				if (p == fast) return report("list is cyclic!");
				fast = fast.next.next;
			}
		}
		
		// Implement remaining conditions.
		
		//2
		if((head ==null && tail !=null))
			return report("tail is not set properly");
		else
			for (Node p = head; p!=null; p=p.next) {
				if(p.next == null && tail !=p)
					return report("tail not at end of set");
					
			}
		//3
		 int count=0;
		 for (Node p = head; p != null; p =p.next) {
			 count++;
		 }
		 if(count != this.manyNodes)
			 return report("manyNodes is not equal");
		 
		 //4
		 
		 if(precursor !=null && precursor== tail)
			 return report("precurser is out of bounds");
		 else if(precursor != null)
		 {
			 boolean found = false;
			 for (Node p = head; p !=null; p=p.next) {
				 if(precursor == p) {
					 found = true;
					 break;
				 }
			 }
			 if(!found)
				 return report("precurser is out of bounds");
		 }
		 //5
		 if(precursor==null && (cursor !=null && cursor != head))
			 return report("Cursor is out of bounds");
		 else if(precursor!=null && cursor != precursor.next && precursor.next!=null) 
			 return report("Cursor is out of bounds");
		// If no problems found, then return true:
		return true;
	}

	private ParticleSeq(boolean doNotUse) {} // only for purposes of testing, do not change
	
	/**
	 * Create an empty sequence
	 * @param - none
	 * @postcondition
	 *   This sequence is empty 
	 **/   
	public ParticleSeq( )
	{
		// TODO: initialize fields (if necessary)
		this.manyNodes=0;
		this.head = null;
		this.tail = null;
		
		assert wellFormed() : "invariant failed in constructor";
	}

	/// Simple sequence methods

	/**
	 * Determine the number of elements in this sequence.
	 * @param - none
	 * @return
	 *   the number of elements in this sequence
	 **/ 
	public int size( )
	{
		assert wellFormed() : "invariant wrong at start of size()";
		// TODO: Implemented by student.
		// This method shouldn't modify any fields, hence no assertion at end
		return this.manyNodes;
	}
	
	/**
	 * Set the current element at the front of this sequence.
	 * @param - none
	 * @postcondition
	 *   The front element of this sequence is now the current element (but 
	 *   if this sequence has no elements at all, then there is no current 
	 *   element).
	 **/ 
	public void start( )
	{
		assert wellFormed() : "invariant wrong at start of start()";
		// TODO: Implemented by student.
		
		if(this.manyNodes !=0) {
			this.cursor = this.head;
			this.precursor = null;
		}
		
		assert wellFormed() : "invariant wrong at end of start()";
	}

	/**
	 * Accessor method to determine whether this sequence has a specified 
	 * current element that can be retrieved with the 
	 * getCurrent method. 
	 * @param - none
	 * @return
	 *   true (there is a current element) or false (there is no current element at the moment)
	 **/
	public boolean isCurrent( )
	{
		assert wellFormed() : "invariant wrong at start of getCurrent()";
		// TODO: Implemented by student.
		// This method shouldn't modify any fields, hence no assertion at end
		return this.cursor != null;
	}

	/**
	 * Accessor method to get the current element of this sequence. 
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true.
	 * @return
	 *   the current element of this sequence
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   getCurrent may not be called.
	 **/
	public Particle getCurrent( )
	{
		assert wellFormed() : "invariant wrong at start of getCurrent()";
		// TODO: Implemented by student.
		// This method shouldn't modify any fields, hence no assertion at end
		if(!isCurrent())
			throw new IllegalStateException("There is no current element");
		return this.cursor.data;
	}

	/**
	 * Move forward, so that the current element is now the next element in
	 * this sequence.
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true. 
	 * @postcondition
	 *   If the current element was already the end element of this sequence 
	 *   (with nothing after it), then there is no longer any current element. 
	 *   Otherwise, the new element is the element immediately after the 
	 *   original current element.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   advance may not be called.
	 **/
	public void advance( )
	{
		assert wellFormed() : "invariant wrong at start of advance()";
		// TODO: Implemented by student.
		if(!isCurrent())
			throw new IllegalStateException("There is no current element");
		if(this.cursor.next != null) {
			this.precursor = this.cursor;
			this.cursor = this.cursor.next;
		}
		else {
			this.cursor = null;
			this.precursor=null;
		}
		assert wellFormed() : "invariant wrong at end of advance()";
	}

	/**
	 * Add a new element to this sequence, after the current element. 
	 * If the new element would take this sequence beyond its current capacity,
	 * then the capacity is increased before adding the new element.
	 * @param element
	 *   the new element that is being added, may be null
	 * @postcondition
	 *   A new copy of the element has been added to this sequence. If there was
	 *   a current element, then the new element is placed after the current
	 *   element. If there was no current element, then the new element is placed
	 *   at the beginning of the sequence. In all cases, the new element becomes the
	 *   new current element of this sequence. 
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for increasing the sequence's capacity.
	 **/
	public void append(Particle element)
	{
		assert wellFormed() : "invariant wrong at start of append";
		// TODO: Implemented by student.
		
		Node n = new Node(element,null);
		
		if(!isCurrent()) {
			if(head == null) {
				head = n;
				tail = n;
			}
			else {
				n.next = head;
				head = n;
			}
		}
		else if(this.cursor== tail) {
			tail.next = n;
			tail = n;
			
		}
		else {
			n.next = this.cursor.next;
			this.cursor.next = n;
		}
		
		this.precursor = this.cursor;
		this.cursor = n;
		this.manyNodes++;
		
		assert wellFormed() : "invariant wrong at end of append";
	}

	/**
	 * Remove the current element from this sequence.
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true.
	 * @postcondition
	 *   The current element has been removed from this sequence, and the 
	 *   following element (if there is one) is now the new current element. 
	 *   If there was no following element, then there is now no current 
	 *   element.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   removeCurrent may not be called. 
	 **/
	public void removeCurrent( )
	{
		assert wellFormed() : "invariant wrong at start of removeCurrent()";
		// TODO: Implemented by student.
		// See textbook pp.176-78, 181-184
		if(!isCurrent())
			throw new IllegalStateException("There is no current element");
		if(this.cursor == this.head) {
			this.head = this.cursor.next;
			this.cursor = this.head;
			}
		else if(this.cursor == this.tail) {
			this.precursor.next = null;
			this.tail = this.precursor;
			this.cursor = null;
			this.precursor = null;
		}
		else {
			this.precursor.next = this.cursor.next;
			this.cursor = this.cursor.next;
		}
		
		this.manyNodes--;
		if(this.manyNodes==0)
			this.tail=null;
		assert wellFormed() : "invariant wrong at end of removeCurrent()";
	}

	/**
	 * Place the contents of another sequence at the end of this sequence.
	 * @param addend
	 *   a sequence whose contents will be placed at the end of this sequence
	 * @precondition
	 *   The parameter, addend, is not null. 
	 * @postcondition
	 *   The elements from addend have been placed at the end of 
	 *   this sequence. The current element of this sequence if any,
	 *   remains unchanged.   The addend is unchanged.
	 * @exception NullPointerException
	 *   Indicates that addend is null. 
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory to increase the size of this sequence.
	 **/
	public void addAll(ParticleSeq addend)
	{
		assert wellFormed() : "invariant wrong at start of addAll";
		assert addend.wellFormed() : "invariant of parameter wrong at start of addAll";
		// TODO: Implemented by student.
		// (Using clone() will reduce the work.)
		if(addend == null)
			throw new NullPointerException("Addend is null");
		if(addend.head == null)
			return;
		ParticleSeq temp = addend.clone();
		
		if(this.head == null) {
			this.head = temp.head;
			this.tail = temp.tail;
		}
		else {
			if(this.head == this.tail) {
				this.head.next = temp.head;
				this.tail = temp.head;
			}
			else 
				this.tail.next = temp.head;
				this.tail = temp.tail;
		}
		
		
		
		
		this.manyNodes += addend.manyNodes;
		
		assert wellFormed() : "invariant wrong at end of insertAll";
		assert addend.wellFormed() : "invariant of parameter wrong at end of insertAll";
	}   


	/**
	 * Generate a copy of this sequence.
	 * @param - none
	 * @return
	 *   The return value is a copy of this sequence. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 *   Whatever was current in the original object is now current in the clone.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	public ParticleSeq clone( )
	{  	 
		assert wellFormed() : "invariant wrong at start of clone()";

		ParticleSeq result;

		try
		{
			result = (ParticleSeq) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  
			// This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}

		// TODO: Implemented by student.
		// Now do the hard work of cloning the list.
		// See pp. 193-197, 228
		// Setting precursor correctly is tricky.
		
		if(result.head == null)
			return result;
		
		Node pre = new Node(null,null);
		result.cursor = result.head;
		for(Node p=this.head; p!=null; p=p.next) {
			Node n = new Node(p.data,null);
			if(p == result.head) {
				result.head = n;
				result.cursor=n;
			}
			else {
			result.cursor.next = n;
			result.precursor = result.cursor;
			result.cursor = n;
			}
			if(p.next == null)
				result.tail = n;
			if(p == this.precursor)
				pre = result.cursor;
			
		}
		
		
		//set the precursor and cursor.
		if(pre.next !=null) {
			result.precursor = pre;
			result.cursor = pre.next;
		}
		if(this.cursor == null) {
			result.cursor = null;
			result.precursor = null;
		}
		if(this.cursor == this.head) {
			result.cursor = result.head;
			result.precursor = null;
		}
		if(this.precursor == this.head) {
			result.precursor = result.head;
			result.cursor = result.precursor.next;
			}
		
		assert wellFormed() : "invariant wrong at end of clone()";
		assert result.wellFormed() : "invariant wrong for result of clone()";
		return result;
	}

	
	public static class TestInvariantChecker extends TestCase {
		ParticleSeq ps = new ParticleSeq(false);
		Particle p1 = new Particle(new Point(1,1),new Vector(),1.0,Color.RED);
		Particle p2 = new Particle(new Point(2,2),new Vector(),2.0,Color.BLUE);
		
		@Override
		protected void setUp() {
			ps = new ParticleSeq(false);
			ps.precursor = null;
			ps.cursor = null;
			ps.head = null;
			ps.tail = null;
			ps.manyNodes = 0;
			doReport = false;
		}
		
		protected static void assertWellFormed(ParticleSeq ps) {
			try {
				doReport = true;
				assertTrue(ps.wellFormed());
			} finally {
				doReport = false;
			}
		}
		
		public void testA() {
			ps.manyNodes = 1;
			assertFalse(ps.wellFormed());
			ps.manyNodes = 0;
			assertWellFormed(ps);
			ps.manyNodes = -1;
			assertFalse(ps.wellFormed());
		}
		
		public void testB() {
			ps.tail = new Node(p2,null);
			assertFalse(ps.wellFormed());
			ps.tail = null;
			ps.cursor = new Node(p1,null);
			assertFalse(ps.wellFormed());
			ps.cursor = null;
			ps.precursor = new Node(null,null);
			assertFalse(ps.wellFormed());
		}
		
		public void testC() {
			ps.manyNodes = 1;
			ps.head = new Node(p1,null);
			assertFalse(ps.wellFormed());
			ps.tail = new Node(p1,null);
			assertFalse(ps.wellFormed());
			ps.head = null;
			assertFalse(ps.wellFormed());
		}
		
		public void testD() {
			ps.manyNodes = 1;
			ps.head = new Node(null,null);
			ps.tail = ps.head;
			assertWellFormed(ps);
			
			ps.manyNodes = 0;
			assertFalse(ps.wellFormed());
			ps.manyNodes = 2;
			assertFalse(ps.wellFormed());
		}
		
		public void testE() {
			ps.head = new Node(p1,null);
			ps.tail = ps.head;
			ps.manyNodes = 1;
			assertWellFormed(ps);
			
			ps.precursor = ps.head;
			assertFalse(ps.wellFormed());
			ps.cursor = ps.tail;
			assertFalse(ps.wellFormed());
			ps.precursor = null;
			assertWellFormed(ps);
		}
		
		public void testF() {
			ps.head = ps.tail = new Node(p1,null);
			ps.manyNodes = 1;
			ps.cursor = new Node(p1,null);
			assertFalse(ps.wellFormed());
		}

		public void testG() {
			ps.head = new Node(p2, null);
			ps.tail = ps.head;
			ps.manyNodes = 1;
			assertWellFormed(ps);
			
			ps.head.next = ps.tail;
			assertFalse(ps.wellFormed());
			ps.manyNodes = 2;
			assertFalse(ps.wellFormed());
		}
		
		public void testH() {
			ps.head = new Node(p1, null);
			ps.tail = new Node(p2, null);
			ps.manyNodes = 2;
			assertFalse(ps.wellFormed());
			ps.head.next = ps.tail;
			assertWellFormed(ps);
			
			ps.manyNodes = 1;
			assertFalse(ps.wellFormed());
			ps.manyNodes = 3;
			assertFalse(ps.wellFormed());
			ps.manyNodes = 0;
			assertFalse(ps.wellFormed());
		}
		
		public void testI() {
			ps.tail = new Node(p1, null);
			ps.head = new Node(p2, ps.tail);
			ps.manyNodes = 2;
			assertWellFormed(ps);
			
			ps.tail = new Node(p1,null);
			assertFalse(ps.wellFormed());
			ps.tail = null;
			assertFalse(ps.wellFormed());
			ps.tail = ps.head;
			assertFalse(ps.wellFormed());
			ps.head.next.next = ps.tail;
			assertFalse(ps.wellFormed());
			ps.manyNodes = 3;
			assertFalse(ps.wellFormed());
		}
		
		public void testJ() {
			ps.tail = new Node(p2, null);
			ps.head = new Node(p1, ps.tail);
			ps.manyNodes = 2;
			assertWellFormed(ps);
			
			ps.precursor = ps.tail;
			assertFalse(ps.wellFormed());
			ps.cursor = ps.head;
			assertFalse(ps.wellFormed());
			ps.cursor = ps.tail;
			assertFalse(ps.wellFormed());
			
			ps.precursor = ps.head;
			ps.cursor = null;
			assertFalse(ps.wellFormed());
			ps.cursor = ps.head;
			assertFalse(ps.wellFormed());
			ps.cursor = ps.tail;
			assertWellFormed(ps);
			
			ps.precursor = null;
			ps.cursor = null;
			assertWellFormed(ps);
			ps.cursor = ps.head;
			assertWellFormed(ps);
			ps.cursor = ps.tail;
			assertFalse(ps.wellFormed());
		}
		
		public void testK() {
			ps.tail = new Node(p1, null);
			ps.head = new Node(p2, ps.tail);
			ps.manyNodes = 2;
			assertWellFormed(ps);

			ps.precursor = new Node(p2, ps.tail);
			ps.cursor = ps.tail;
			assertFalse(ps.wellFormed());
			
			ps.cursor = ps.precursor;
			ps.precursor = null;
			assertFalse(ps.wellFormed());
			
		}
		
		public void testL() {
			ps.head = ps.tail = new Node(p1,null);
			ps.head = new Node(p2,ps.head);
			ps.head = new Node(null,ps.head);
			ps.head = new Node(p2,ps.head);
			ps.head = new Node(p1,ps.head);
			ps.manyNodes = 1;
			assertFalse(ps.wellFormed());
			ps.manyNodes = 2;
			assertFalse(ps.wellFormed());
			ps.manyNodes = 3;
			assertFalse(ps.wellFormed());
			ps.manyNodes = 4;
			assertFalse(ps.wellFormed());
			ps.manyNodes = 0;
			assertFalse(ps.wellFormed());
			ps.manyNodes = -1;
			assertFalse(ps.wellFormed());
			ps.manyNodes = 5;
			assertWellFormed(ps);
			
			ps.tail = new Node(p1, null);
			assertFalse(ps.wellFormed());
		}
		
		public void testM() {
			ps.head = ps.tail = new Node(p1,null);
			ps.head = new Node(p2,ps.head);
			ps.head = new Node(null,ps.head);
			ps.head = new Node(p2,ps.head);
			ps.head = new Node(p1,ps.head);
			ps.manyNodes = 5;
			assertWellFormed(ps);
			
			ps.tail = null;
			assertFalse(ps.wellFormed());
			ps.tail = new Node(p1,null);
			assertFalse(ps.wellFormed());
			ps.tail = ps.head;
			assertFalse(ps.wellFormed());
			ps.tail = ps.tail.next;
			assertFalse(ps.wellFormed());
			ps.tail = ps.tail.next;
			assertFalse(ps.wellFormed());
			ps.tail = ps.tail.next;
			assertFalse(ps.wellFormed());
			ps.tail = ps.tail.next;
			
			assertWellFormed(ps);
		}
		
		public void testN() {
			Node n1,n2,n3,n4,n5;
			ps.head = n5 = new Node(p1,null);
			ps.head = n4 = new Node(p2,ps.head);
			ps.head = n3 = new Node(null,ps.head);
			ps.head = n2 = new Node(p2,ps.head);
			ps.head = n1 = new Node(p1,ps.head);
			ps.manyNodes = 5;
			ps.tail = n5;

			ps.precursor = new Node(p1,null);
			assertFalse(ps.wellFormed());
			ps.precursor = new Node(p1,n1);
			ps.cursor = n1;
			assertFalse(ps.wellFormed());
			ps.precursor = new Node(p1,n2);
			ps.cursor = n2;
			assertFalse(ps.wellFormed());
			ps.precursor = new Node(p2,n3);
			ps.cursor = n3;
			assertFalse(ps.wellFormed());
			ps.precursor = new Node(null,n4);
			ps.cursor = n4;
			assertFalse(ps.wellFormed());
			ps.precursor = new Node(p2,n5);
			ps.cursor = n5;
			assertFalse(ps.wellFormed());
			
			doReport = true;
			
			ps.precursor = null;
			ps.cursor = n1;
			assertTrue(ps.wellFormed());
			ps.precursor = n1;
			ps.cursor = n2;
			assertTrue(ps.wellFormed());
			ps.precursor = n2;
			ps.cursor = n3;
			assertTrue(ps.wellFormed());
			ps.precursor = n3;
			ps.cursor = n4;
			assertTrue(ps.wellFormed());
			ps.precursor = n4;
			ps.cursor = n5;
			assertTrue(ps.wellFormed());
			
			ps.precursor = n5;
			ps.cursor = null;
			doReport = false;
			assertFalse(ps.wellFormed());
		}
		
		public void testO() {
			Node n1,n2,n3,n4,n5;
			ps.head = n5 = new Node(p1,null);
			ps.head = n4 = new Node(p2,ps.head);
			ps.head = n3 = new Node(null,ps.head);
			ps.head = n2 = new Node(p2,ps.head);
			ps.head = n1 = new Node(p1,ps.head);
			ps.manyNodes = 5;
			ps.tail = n5;

			ps.precursor = null;
			ps.cursor = n2;
			assertFalse(ps.wellFormed());
			ps.cursor = n3;
			assertFalse(ps.wellFormed());
			ps.cursor = n4;
			assertFalse(ps.wellFormed());
			ps.cursor = n5;
			assertFalse(ps.wellFormed());
			
			ps.precursor = n1;
			ps.cursor = n1;
			assertFalse(ps.wellFormed());
			ps.cursor = n3;
			assertFalse(ps.wellFormed());
			ps.cursor = n4;
			assertFalse(ps.wellFormed());
			ps.cursor = n5;
			assertFalse(ps.wellFormed());
			ps.cursor = null;
			assertFalse(ps.wellFormed());
			
			ps.precursor = n2;
			ps.cursor = n1;
			assertFalse(ps.wellFormed());
			ps.cursor = n2;
			assertFalse(ps.wellFormed());
			ps.cursor = n4;
			assertFalse(ps.wellFormed());
			ps.cursor = n5;
			assertFalse(ps.wellFormed());
			ps.cursor = null;
			assertFalse(ps.wellFormed());
			
			ps.precursor = n3;
			ps.cursor = n1;
			assertFalse(ps.wellFormed());
			ps.cursor = n2;
			assertFalse(ps.wellFormed());
			ps.cursor = n3;
			assertFalse(ps.wellFormed());
			ps.cursor = n5;
			assertFalse(ps.wellFormed());
			ps.cursor = null;
			assertFalse(ps.wellFormed());
			
			ps.precursor = n4;
			ps.cursor = n1;
			assertFalse(ps.wellFormed());
			ps.cursor = n2;
			assertFalse(ps.wellFormed());
			ps.cursor = n3;
			assertFalse(ps.wellFormed());
			ps.cursor = n4;
			assertFalse(ps.wellFormed());
			ps.cursor = null;
			assertFalse(ps.wellFormed());
			
			ps.precursor = n5;
			ps.cursor = n1;
			assertFalse(ps.wellFormed());
			ps.cursor = n2;
			assertFalse(ps.wellFormed());
			ps.cursor = n3;
			assertFalse(ps.wellFormed());
			ps.cursor = n4;
			assertFalse(ps.wellFormed());
			ps.cursor = n4;
			assertFalse(ps.wellFormed());
			ps.cursor = null;
			assertFalse(ps.wellFormed());			
		}
	}
}

