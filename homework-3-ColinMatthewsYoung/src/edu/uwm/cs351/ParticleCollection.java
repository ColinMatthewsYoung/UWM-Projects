package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A variant of the ParticleSeq ADT that follows the Collection model.
 */
public class ParticleCollection extends AbstractCollection<Particle> implements Cloneable {
	// TODO: Add all the contents here.
	// Remember:
	// - All public methods not marked @Override must be fully documented with javadoc
	// - You need to define and check the data structure invariant
	//   (essentially the same as in Homework #2)
	// - You should define a nested iterator class called MyIterator (with its own data structure), 
	//   and then the iterator() method simply returns a new instance.
	// You are permitted to copy in any useful code/comments from the Homework #2 solution.
	// But do not include any of the cursor-related methods, and in particular,
	// make sure you have no "currentIndex" field.
	private int version;
	private int manyItems;
	private Particle[] data;
	
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
		if (manyItems<0) return report("data is to small");

		
		return true;
	}
	
	/**
	 *Initialize an empty collection with initial capacity set to INITIAL_CAPACITY
	 *@param none
	 *@postcondition
	 *	the collection is empty with an initial capacity of INITIAL_CAPACITY
	 *@exception OutOfMemoryError
	 **/
	public ParticleCollection( )
	{
		// NB: NEVER assert the invariant at the START of the constructor.
		// (Why not?  Think about it.)
		// TODO: Implement this code.
		this.manyItems = 0;
		this.data = new Particle[INITIAL_CAPACITY];
		this.version = 1;
		
		assert wellFormed() : "Invariant false at end of constructor";
		
	}
	
	/**
	 *Initialize an empty collection with initial capacity set to INITIAL_CAPACITY
	 *@param initialCapacity
	 *	the initial capacity of the collection
	 *@precondition
	 *	initialCapacity is non-negative
	 *@postcondition
	 *	the collection is empty with an initial capacity of initialCapacity
	 *@exception OutOfMemoryError
	 **/
	public ParticleCollection(int initialCapacity)
	{
		// TODO: Implement this code.
		if(initialCapacity <0) throw new IllegalArgumentException("capacity cant be negative");
		
		this.data = new Particle[initialCapacity];
		this.manyItems = 0;
		this.version =1;
		
		
		assert wellFormed() : "Invariant false at end of constructor";
	}

	
	/**
	 * Change the current capacity of this sequence if needed.
	 * @param minimumCapacity
	 *   the new capacity for this collection
	 * @postcondition
	 *   This collections capacity has been changed to at least minimumCapacity.
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
	 * @Override //implementation
	 * 
	 * @param element
	 *  the new element that is being added, it may be null
	 *  
	 *  @postcondition
	 *  	a new copy of the element is being added to the end of the collection and the version has been changed to
	 *  	ensure iterators are current.
	 *  @exception OutOfMemoryError
	 *   Indicates insufficient memory for increasing the sequence's capacity.
	 * @note
	 *   An attempt to increase the capacity beyond
	 *   Integer.MAX_VALUE will cause the sequence to fail with an
	 *   arithmetic overflow.
	 */
	public boolean add(Particle element)
	{
		assert wellFormed() : "invariant failed at start of append";
		// TODO: Implement this code.
		ensureCapacity(this.manyItems+1);
		this.data[manyItems] = element;
		this.manyItems++;
		this.version++;
		
		assert wellFormed() : "invariant failed at end of append";
		
		return true;
	}
	
	/**
	 * @Override //implementation
	 * @param element
	 * 	removes the element from the collection
	 * @postcondition
	 *  the element has been removed from the collection and the version has been changed to ensure iterators are current.
	 *  	
	 */
	public boolean remove(Particle element) {
		assert wellFormed() : "invariant failed at start of remove";
		for(int i =0; i < manyItems; i++) {
			if (data[i]==element)
			{
				data[i]=null;
				manyItems--;
				this.version++;
				
				for(int j=i; j<manyItems; j++) {
					if(j+1 <data.length)
						data[j] = data[j+1];
						data[j+1] = null;
				}
				assert wellFormed() : "invariant failed at end of remove";
				return true;
				
			}
		}
		assert wellFormed() : "invariant failed at end of remove";
		return false;
	}
	
	
	/**
	 * @Override //required
	 * @param none
	 * @return
	 *  a new iterator.
	 * @exception OutOfMemoryError
	 *  Indicates insufficient memory for an array with this many elements.
	 */
	public Iterator<Particle> iterator() {
		// TODO Auto-generated method stub
		
		return new MyIterator();
	}
	
	/**
	 * @Override required
	 * @param none
	 * @return
	 * 	the number of elements in the collection.
	 */
	public int size() {
		// TODO Auto-generated method stub
		
		return this.manyItems;
	}
	
	
	/**
	 * @Override // implementation
	 * @param none
	 * @return
	 *  a copy of this ParticleCollection
	 * @exception OutOfMemoryError
	 *  Indicates insufficient memory for creating the clone.
	 */
	public ParticleCollection clone( )
	{  // Clone a ParticleSeq object.  (We follow textbook form.)
		assert wellFormed() : "invariant failed at start of clone";
		ParticleCollection answer;

		try
		{
			// super.clone() will create a new object with the
			// same value for *all* the fields.  This is a "shallow" clone.
			answer = (ParticleCollection) super.clone( );
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
	
	
	/**
	 * empty the collection with removeAll, which skips shifting down all elements in the collection since we are removing
	 * them all anyway
	 * @Override //Efficiency
	 * @param none
	 * @postcondition
	 * 	Empty the collection. 
	 */
	public void clear() {
        Iterator<Particle> it = iterator();
        
            ((MyIterator) it).removeAll();
     
    }

	
	private class MyIterator implements Iterator<Particle>// TODO: what should this implement?	
	{
		private int current;
		private int next;
		private int myVersion;
		boolean canRemove = false;
		
		public MyIterator() {
			this.myVersion = version;
			this.next = 0;
			this.current = next;

		}
		
		private void faleFast() {
			if(this.myVersion !=version) {
				throw new ConcurrentModificationException("versions not in sync");
			}
		}
		
		/**
		 * @Override // implementation
		 * @param none
		 * @precondition
		 * 	the iterator version matches the collections version
		 * @return
		 *  true if there is another element in the collection
		 *  false if we are at the end of the collection and there is no next element.
		 */
		public boolean hasNext() {
				faleFast();
				return this.next<manyItems;
			}
		
		
		/**
		 * @Override // implementation
		 * @param none
		 * @precondition
		 * 	the iterator version matches the collections version
		 * @postcondition
		 * 	current has been shifted to the next position, and next has been shifted up one position as long as there is 
		 * 	a next element in the collection.
		 * @return
		 *  the current particle
		 * @exception NoSuchElementException
		 *  Indicates there is no next element in the collection.
		 **/
		public Particle next() {
			assert wellFormed();
			
			if(hasNext()) {
				this.current = this.next;
				this.next++;
				this.canRemove = true;
			}
			
			else
				throw new NoSuchElementException("no next element exists");
			
			assert wellFormed() : "invariant failed at the end of next()";
			return data[this.current];
		}
		
		/**
		 * @Override // implementation
		 * @param none
		 * @precondition
		 * 	the iterator version matches the collections version.
		 *  there is a current element to be removed.
		 * @postcondition
		 * 	the current element is removed and all other elements are shifted down one position to fill in the hole
		 * 	in the collection. the version of both the iterator and collection are incremented to ensure they remain in sync
		 * @exception IllegalStateExceptoin
		 * 	indicates that there is no current element.
		 */
		public void remove() {
			assert wellFormed();
			
			faleFast();
			
			
			if(!this.canRemove)
				throw new IllegalStateException("no current element");
			
			assert wellFormed() : "invariant failed at the start of remove()";
			
			
			
			data[this.current]=null;
			manyItems--;
			
			
			if(this.current >= manyItems) {
				this.next = manyItems;
				this.current = this.next;
			}
			
			else {
				for(int i =this.current; i<manyItems; i++) {
					if(i+1 <data.length)
						data[i] = data[i+1];
						data[i+1] = null;
				}
				
					this.next = current;
			}
			
			
			
			this.canRemove = false;
			this.myVersion++;
			version++;

			assert wellFormed() : "invariant failed at the end of remove()";
		}
		
		/**
		 * @Override // implementation
		 * @param none
		 * @precondition
		 * 	the iterator version matches the collections version.
		 *  there is a current element to be removed.
		 * @postcondition
		 * 	removes all elements of the collection and the version of both the iterator and collection are incremented to ensure they remain in sync
		 * 
		 */
		public void removeAll() {
			assert wellFormed();
			
			faleFast();
			while (hasNext()) {
	            next();
			
		
			
			data[this.current]=null;
			manyItems--;
			if(this.current >= manyItems) {
				this.next = manyItems;
				this.current = this.next;
			}
			this.next = current;
			this.canRemove = false;
			this.myVersion++;
			version++;
			}
			assert wellFormed() : "invariant failed at the end of remove()";
		}
		
		// The nested MyIterator class should use the following
		// invariant checker:
		public boolean wellFormed() {
			if (!ParticleCollection.this.wellFormed()) return false;
			if (version != myVersion) return true; // not my fault if invariant broken
			if (current < 0 || current > manyItems) return report("current out of range: " + current + " not in range [0," + manyItems + "]");
			if (next < 0 || next > manyItems) return report("next out of range: " + next + " not in range[0," + manyItems + "]");
			if (next != current && next != current + 1) return report("next " + next + " isn't current or its successor (current = " + current + ")");
			return true;
		}
	}

	

	
}
