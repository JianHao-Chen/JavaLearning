package jdkSrc.collection.list;

import java.util.Iterator;

public interface ListIterator<E> extends Iterator {

    // Query Operations
    
    /**
     * Returns <tt>true</tt> if this list iterator has more elements when
     * traversing the list in the forward direction.
     */
    boolean hasNext();
    
    
    /**
     * Returns <tt>true</tt> if this list iterator has more elements when
     * traversing the list in the reverse direction. 
     */
    boolean hasPrevious();
    
    
    /**
     * Returns the next element in the list.  This method may be called
     * repeatedly to iterate through the list, or intermixed with calls to
     * <tt>previous</tt> to go back and forth.
     */
    E next();
    
    /**
     * Returns the previous element in the list. This method may be called
     * repeatedly to iterate through the list backwards
     */
    E previous();
    
    
    // Modification Operations
    
    
    /**
     * Inserts the specified element into the list (optional operation).  The
     * element is inserted immediately before the next element that would be
     * returned by <tt>next</tt>, if any, and after the next element that
     * would be returned by <tt>previous</tt>, if any.  (If the list contains
     * no elements, the new element becomes the sole element on the list.)
     * The new element is inserted before the implicit cursor: a subsequent
     * call to <tt>next</tt> would be unaffected, and a subsequent call to
     * <tt>previous</tt> would return the new element.  (This call increases
     * by one the value that would be returned by a call to <tt>nextIndex</tt>
     * or <tt>previousIndex</tt>.)
     */
    void add(E e);
    
    /**
     * Removes from the list the last element that was returned by
     * <tt>next</tt> or <tt>previous</tt> (optional operation).  This call can
     * only be made once per call to <tt>next</tt> or <tt>previous</tt>.
     */
    void remove();
    
    /**
     * Replaces the last element returned by <tt>next</tt> or
     * <tt>previous</tt> with the specified element (optional operation).
     * This call can be made only if neither <tt>ListIterator.remove</tt> nor
     * <tt>ListIterator.add</tt> have been called after the last call to
     * <tt>next</tt> or <tt>previous</tt>.
     */
    void set(E e);
}
