// This is an assignment for students to complete after reading Chapter 3 of
// "Data Structures and Other Objects Using Java" by Michael Main.

//Colin Young

package edu.uwm.cs351;

import junit.framework.TestCase;

/******************************************************************************
 * This class is a homework assignment;
 * A ParticleSeq is a collection of Particles.
 * The sequence can have a special "current element," which is specified and 
 * accessed through four methods that are not available in the sequence class 
 * (start, getCurrent, advance and isCurrent).
 *
 * Note:
 * <ol>
 * <li> The capacity of a sequence can change after it's created, but
 *   the maximum capacity is limited by the amount of free memory on the 
 *   machine. The constructor, append, addAll, clone
 *   and concatenation will result in an
 *   OutOfMemoryError when free memory is exhausted.
 * <li>
 *   A sequence's capacity cannot exceed the maximum integer 2,147,483,647
 *   ({@link Integer#MAX_VALUE}. Any attempt to create a larger capacity
 *   results in a failure due to an arithmetic overflow. 
 * </ol>
 * Neither of these conditions require any work for the implementors (students).
 ******************************************************************************/
public class ParticleSeq implements Cloneable
{
	// Implementation of the ParticleSeq class:
	//   1. The number of elements in the sequences is in the instance variable 
	//      manyItems.  The elements may be Particle objects or nulls.
	//   2. For any sequence, the elements of the
	//      sequence are stored in data[0] through data[manyItems-1], and we
	//      don't care what's in the rest of data.
	//   3. If there is a current element, then it lies in data[currentIndex];
	//      if there is no current element, then currentIndex equals -1. 

	private Particle[ ] data;
	private int manyItems;
	private int currentIndex; 

	private static int INITIAL_CAPACITY = 1;

	private static boolean doReport = true; // changed only by invariant tester
	
	private boolean report(String error) {
		if (doReport) System.out.println("Invariant error: " + error);
		else System.out.println("Caught problem: " + error);
		return false;
	}

	private boolean wellFormed() {
		// Check the invariant.
		// 1. data is never null
		if (data == null) return report("data is null"); // test the NEGATION of the condition

		// 2. The data array is at least as long as the number of items
		//    claimed by the sequence.
		// TODO
		if (data.length < manyItems) return report("data is to small");

		// 3. currentIndex is and never equal or more than the number of
		//    items claimed by the sequence, and never less than -1.
		// TODO
		if (currentIndex >= manyItems || currentIndex < -1) return report("currentIndex is out of bounds");
		// If no problems discovered, return true
		return true;
	}

	
	// This is only for testing the invariant.  Do not change!
	private ParticleSeq(boolean testInvariant) { }
	
	/**
	 * Initialize an empty sequence with an initial capacity of INITIAL_CAPACITY.
	 * The append method works
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @param - none
	 * @postcondition
	 *   This sequence is empty and has an initial capacity of INITIAL_CAPACITY
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for initial array.
	 **/   
	public ParticleSeq( )
	{
		// NB: NEVER assert the invariant at the START of the constructor.
		// (Why not?  Think about it.)
		// TODO: Implement this code.
		this.data = new Particle[INITIAL_CAPACITY];
		this.manyItems = 0;
		this.currentIndex = -1;
		
		assert wellFormed() : "Invariant false at end of constructor";
		
	}


	/**
	 * Initialize an empty sequence with a specified initial capacity. Note that
	 * the append method works
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @param initialCapacity
	 *   the initial capacity of this sequence
	 * @precondition
	 *   initialCapacity is non-negative.
	 * @postcondition
	 *   This sequence is empty and has the given initial capacity.
	 * @exception IllegalArgumentException
	 *   Indicates that initialCapacity is negative.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for an array with this many elements.
	 *   new Particle[initialCapacity].
	 **/   
	public ParticleSeq(int initialCapacity)
	{
		// TODO: Implement this code.
		if(initialCapacity <0) throw new IllegalArgumentException("capacity cant be negative");
		
		this.data = new Particle[initialCapacity];
		this.manyItems = 0;
		this.currentIndex = -1;
		
		assert wellFormed() : "Invariant false at end of constructor";
	}

	/**
	 * Determine the number of elements in this sequence.
	 * @param - none
	 * @return
	 *   the number of elements in this sequence
	 **/ 
	public int size( )
	{
		assert wellFormed() : "invariant failed at start of size";
		// TODO: Implement this code.
		// size() should not modify anything, so we omit testing the invariant here
		return this.manyItems;
	}

	/**
	 * The first element (if any) of this sequence is now current.
	 * @param - none
	 * @postcondition
	 *   The front element of this sequence (if any) is now the current element (but 
	 *   if this sequence has no elements at all, then there is no current 
	 *   element).
	 **/ 
	public void start( )
	{
		assert wellFormed() : "invariant failed at start of start";
		// TODO: Implement this code.
		
		if(this.manyItems == 0)
			currentIndex = -1;
		else
			currentIndex = 0;
		
		assert wellFormed() : "invariant failed at end of start";
	}

	/**
	 * Accessor method to determine whether this sequence has a specified 
	 * current element (a Particle or null) that can be retrieved with the 
	 * getCurrent method. This depends on the status of the cursor.
	 * @param - none
	 * @return
	 *   true (there is a current element) or false (there is no current element at the moment)
	 **/
	public boolean isCurrent( )
	{
		assert wellFormed() : "invariant failed at start of isCurrent";
		// TODO: Implement this code.
		
		if(this.currentIndex == -1) 
			return false;
		
		return true;
		
		
	}

	/**
	 * Accessor method to get the current element of this sequence. 
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true.
	 * @return
	 *   the current element of this sequence, possibly null
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   getCurrent may not be called.
	 **/
	public Particle getCurrent( )
	{
		assert wellFormed() : "invariant failed at start of getCurrent";
		// TODO: Implement this code.
		if(!isCurrent())
			throw new IllegalStateException("there is no current element");
		
		return this.data[currentIndex];
	}

	/**
	 * Move forward, so that the next element is now the current element in
	 * this sequence.
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true. 
	 * @postcondition
	 *   If the current element was already the end element of this sequence 
	 *   (with nothing after it), then there is no longer any current element. 
	 *   Otherwise, the new current element is the element immediately after the 
	 *   original current element.
	 * @exception IllegalStateException
	 *   If there was no current element, so 
	 *   advance may not be called (the precondition was false).
	 **/
	public void advance( )
	{
		assert wellFormed() : "invariant failed at start of advance";
		// TODO: Implement this code.
		if(isCurrent()) {
			if(this.currentIndex < this.data.length-1 && this.currentIndex+1 < this.manyItems)
				currentIndex++;
			else
				this.currentIndex = -1;
		}
		else
			throw new IllegalStateException("there is no current element");
		
		assert wellFormed() : "invariant failed at end of advance";
	}

	/**
	 * Change the current capacity of this sequence if needed.
	 * @param minimumCapacity
	 *   the new capacity for this sequence
	 * @postcondition
	 *   This sequence's capacity has been changed to at least minimumCapacity.
	 *   If the capacity was already at or greater than minimumCapacity,
	 *   then the capacity is left unchanged.
	 *   If the capacity is changed, it must be at least twice as big as before.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for: new array of minimumCapacity elements.
	 **/
	private void ensureCapacity(int minimumCapacity)
	{
		// TODO: Implement this code.
		// This is a private method: don't check invariants
		
		//taken from activity2
		
		if (minimumCapacity < data.length) return; 
	    int newSize = data.length*2;
	    if (newSize < minimumCapacity) newSize = minimumCapacity; 
	    if(newSize ==0) newSize=1; //catches any empty arrays.
	    Particle[] newArray = new Particle[newSize]; 
	    for (int i=0; i < manyItems; ++i) {
	      newArray[i] = data[i]; 
	    }
	    data = newArray; 
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
	 * @note
	 *   An attempt to increase the capacity beyond
	 *   Integer.MAX_VALUE will cause the sequence to fail with an
	 *   arithmetic overflow.
	 **/
	public void append(Particle element)
	{
		assert wellFormed() : "invariant failed at start of append";
		// TODO: Implement this code.
		ensureCapacity(this.manyItems+1);
		currentIndex++;
		
			ensureCapacity(manyItems+1);
			for(int i=manyItems; i>currentIndex; i--) {
				data[i] = data[i-1];
			}
			this.data[this.currentIndex] = element;
			
		
		
		this.manyItems++;
		
		assert wellFormed() : "invariant failed at end of append";
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
		assert wellFormed() : "invariant failed at start of removeCurrent";
		// TODO: Implement this code.
		// You will need to shift elements in the array.
		if(isCurrent()) {
			
			
			data[currentIndex]=null;
			this.manyItems--;
			
			if(currentIndex >= this.manyItems)
				currentIndex = -1;
			
			else {
				for(int i =currentIndex; i<manyItems; i++) {
					if(i+1 <data.length)
						data[i] = data[i+1];
						data[i+1] = null;
				}
			}
		}
		else 
			throw new IllegalStateException("no current element");
		assert wellFormed() : "invariant failed at end of removeCurrent";
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
	 * @note
	 *   An attempt to increase the capacity beyond
	 *   Integer.MAX_VALUE will cause an arithmetic overflow
	 *   that will cause the sequence to fail.
	 **/
	public void addAll(ParticleSeq addend)
	{
		assert wellFormed() : "invariant failed at start of addAll";
		// TODO: Implement this code.
		ParticleSeq d = addend.clone();
		ParticleSeq c = addend;
		
		if(addend != null) {
			ensureCapacity(manyItems + addend.manyItems);
			int added = 0;
			for(int i=0; i<addend.manyItems;i++) {
				this.data[manyItems+i] = addend.data[i];
				added++;
				
			}
			this.manyItems+=added;
		}
		else
			throw new NullPointerException("Parameter can not be null");
		
		
		assert wellFormed() : "invariant failed at end of addAll";
	}   

	
	
	/**
	 * Generate a copy of this sequence.
	 * @param - none
	 * @return
	 *   The return value is a copy of this sequence. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	@Override // implementation
	public ParticleSeq clone( )
	{  // Clone a ParticleSeq object.  (We follow textbook form.)
		assert wellFormed() : "invariant failed at start of clone";
		ParticleSeq answer;

		try
		{
			// super.clone() will create a new object with the
			// same value for *all* the fields.  This is a "shallow" clone.
			answer = (ParticleSeq) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  // This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}

		// TODO: clone the data array
		
		//taken from the book, page 136;
		answer.data = this.data.clone();
		
		assert wellFormed() : "invariant failed at end of clone";
		assert answer.wellFormed() : "invariant failed for clone";
		
		return answer;
	}

	
	public static class TestInvariant extends TestCase {
		private ParticleSeq hs;
		
		@Override // implementation
		public void setUp() {
			hs = new ParticleSeq(false);
			doReport = false;
		}
		
		public void test0() {
			hs.currentIndex = -1;
			assertFalse(hs.wellFormed());
		}
		
		public void test1() {
			hs.data = new Particle[2];
			assertFalse(hs.wellFormed());
		}
		
		public void test2() {
			hs.data = new Particle[0];
			hs.currentIndex = -1;
			assertTrue(hs.wellFormed());
		}
		
		public void test3() {
			hs.data = new Particle[3];
			hs.manyItems = 4;
			assertFalse(hs.wellFormed());
		}
		
		public void test4() {
			hs.data = new Particle[4];
			hs.manyItems = 4;
			assertTrue(hs.wellFormed());
		}
		
		public void test5() {
			hs.data = new Particle[10];
			hs.manyItems = 4;
			assertTrue(hs.wellFormed());
		}
		
		public void test6() {
			hs.data = new Particle[5];
			hs.manyItems = 4;
			hs.currentIndex = 4;
			assertFalse(hs.wellFormed());
		}
		
		public void test7() {
			hs.data = new Particle[3];
			hs.manyItems = 3;
			hs.currentIndex = -1;
			assertTrue(hs.wellFormed());
		}
		
		public void test8() {
			hs.data = new Particle[5];
			hs.manyItems = 3;
			hs.currentIndex = 3;
			assertFalse(hs.wellFormed());
		}
		
		public void test9() {
			hs.data = new Particle[5];
			hs.manyItems = 5;
			hs.currentIndex = 4;
			assertTrue(hs.wellFormed());
		}
	}
}

