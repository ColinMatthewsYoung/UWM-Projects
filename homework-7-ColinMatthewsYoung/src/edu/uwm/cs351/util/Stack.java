package edu.uwm.cs351.util;

import java.util.EmptyStackException;

//Colin Young
/**
 * A linked list implementation of the classic LIFO structure.
 * @param <E>
 */
public class Stack<E> implements Cloneable {
	private static class Node<T> {
		T data;
		Node<T> next;
		Node(T d, Node<T> n) { data = d; next = n; }
	}
	
	private Node<E> top;
	private int size;
	
	// TODO Add an invariant checker, and check in every method
	//      (twice if the method has side-effects; 
	//       "clone()" should check result at end of clone.)
	// TODO Add all the required methods.
	// TODO All except wellFormed, clone, and toString should be constant-time
	// TODO "clone()" should use the technique used in other ADTs this semester
	//      (or in the textbook p.85)
	// TODO toString is allowed to "decant" the contents of the stack into
	//      an auxiliary data structure or collection.  But should use
	//      a StringBuilder to avoid quadratic string building ("result += ...")
	// TODO All public methods must have @Override or descriptive javadoc.
	// TODO No warnings allowed except you may suppress one unchecked warning in "clone()"
	private static boolean doReport = true;
	
	private boolean report(String s) {
		if (doReport) System.out.println("invariant error: "+ s);
		return false;
	}
	
	/*
	 * if stack is empty top must be null
	 * size must equal number of elements in the stack
	 * top must be the head pointer
	 * stack must not cycle
	 * 
	 * */
	private boolean wellFormed() {
		if(size ==0 && top != null) return report("if stack is empty top must be null");
		
		int count=0;
		for(Node<E> p = top; p!=null; p=p.next) {
			count++;
		}
		if(count !=size) return report("size must equal the number of elements in the stack");
		
		if (top != null) {
			// This check uses the "tortoise and hare" algorithm attributed to Floyd.
			Node<E> fast = top.next;
			for (Node<E> p = top; fast != null && fast.next != null; p = p.next) {
				if (p == fast) return report("stack must not cycle");
				fast = fast.next.next;
			}
		}
		return true;
	}
	/*
	 *
	 *@return true if stack is empty
	 * */
	public boolean isEmpty() {
		//wellFormed();
		if(top != null) return false;
		return true;
	}
	
	/*
	 * returns the number of elements in the stack
	 * @return size
	 * */
	public int size() {
	//	wellFormed();
		return size;
	}
	
	/*
	 * pushes an element onto the top of the stack
	 * */
	public void push(E t) {
	//	wellFormed();
		Node<E> a = new Node<E>(t, null);
		if(size ==0) top =a;
		else {
			a.next = top;
			top = a;
		}
		
		size++;
	//	wellFormed();
		
	}
	
	
	/*
	 * removes the top element from the stack, and returns it
	 * @return the top element of the stack.
	 * */
	public E pop() { 
		//wellFormed();
		if(size == 0) throw new EmptyStackException();
		E result = top.data;
		top = top.next;
		size--;
		//wellFormed();
		return result;
	}
	
	/*
	 * Return the element at the top of the stack without removing it.
	 * @return the top element of the stack
	 * */
	public E peek() {
		//wellFormed();
		if(size == 0) throw new EmptyStackException();
		return top.data;
	}
	
	/*
	 * removes all elements from the stack
	 * */
	public void clear() {
	//	wellFormed();
		top = null;
		size =0;
	//	wellFormed();
	}
	
	/*
	 * returns a copy of the stack
	 * */
	@SuppressWarnings("unchecked")
	public Stack<E> clone(){
	//	wellFormed();
		Stack<E> result;
		
		try{
			result = (Stack<E>) super.clone( );
		}
		catch(CloneNotSupportedException e) {
			throw new RuntimeException("This class does not implement Cloneable");
		}
		for(Node<E> p = top; p!=null; p=p.next) {
			Node<E> a = new Node<E>(p.data, p.next);
			if(p == top) result.top = a;
		}
		
	//	wellFormed();
		return result;
		
	}
	
	/*
	 * @return result. A string in the form [1, 2, 3] showing what is on the stack. 
	 * the top of the stack is on the right.
	 * */
	public String toString() {
	//	wellFormed();
		String result = "";
		if(!isEmpty()) {
			if(size == 1) result = "" + top.data;
			else {
				for(Node<E> p = top; p!=null; p=p.next) {
					if(p.next !=null)
						result = ", " + p.data  + result;
					else
						result = p.data + "" + result;
				}
			}
		}
		result = "[" + result + "]";
		return result;
		
	}
	
}
