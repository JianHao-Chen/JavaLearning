package jdkSrc.collection.list;

import java.util.Iterator;

import jdkSrc.collection.NoSuchElementException;

public abstract class AbstractSequentialList<E> extends AbstractList<E> {
    protected AbstractSequentialList() {
    }
    
    /**
     * Returns the element at the specified position in this list.
     * 
     * This implementation first gets a list iterator pointing to the
     * indexed element (with <tt>listIterator(index)</tt>).  Then, it gets
     * the element using <tt>ListIterator.next</tt> and returns it.
     */
    public E get(int index) {
        try {
            return listIterator(index).next();
        }
        catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }
    
    /**
     * Replaces the element at the specified position in this list with the
     * specified element (optional operation).
     */
    public E set(int index, E element) {
        try {
            ListIterator<E> e = listIterator(index);
            E oldVal = e.next();
            e.set(element);
            return oldVal;
        }
        catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }
    
    
    /**
     * Inserts the specified element at the specified position in this list
     * (optional operation).  Shifts the element currently at that position
     * (if any) and any subsequent elements to the right (adds one to their
     * indices).
     *
     * <p>This implementation first gets a list iterator pointing to the
     * indexed element (with <tt>listIterator(index)</tt>).  Then, it
     * inserts the specified element with <tt>ListIterator.add</tt>.
     */
    public void add(int index, E element) {
        try {
            listIterator(index).add(element);
        } 
        catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }
    
    
    /**
     * Removes the element at the specified position in this list (optional
     * operation).  Shifts any subsequent elements to the left (subtracts one
     * from their indices).  Returns the element that was removed from the
     * list.
     *
     * <p>This implementation first gets a list iterator pointing to the
     * indexed element (with <tt>listIterator(index)</tt>).  Then, it removes
     * the element with <tt>ListIterator.remove</tt>.
     */
    public E remove(int index) {
        try {
            ListIterator<E> e = listIterator(index);
            E outCast = e.next();
            e.remove();
            return outCast;
        } 
        catch (NoSuchElementException exc) {
            throw new IndexOutOfBoundsException("Index: "+index);
        }
    }
    
    
    // Iterators
    
    /**
     * Returns an iterator over the elements in this list (in proper
     * sequence).<p>
     *
     * This implementation merely returns a list iterator over the list.
     */
    public Iterator<E> iterator() {
        return listIterator();
    }
    
    
    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     */
    public abstract ListIterator<E> listIterator(int index);
}
