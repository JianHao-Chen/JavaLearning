package jdkSrc.collection;

import java.util.Iterator;

public interface Collection<E> extends Iterable<E>{

    /**
     * Returns the number of elements in this collection.
     */
    int size();
    
    
    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     */
    boolean isEmpty();
    
    
    /**
     * Returns <tt>true</tt> if this collection contains the specified element.
     */
    boolean contains(Object o);
    
    
    /**
     * Returns an iterator over the elements in this collection.  There are no
     * guarantees concerning the order in which the elements are returned
     * (unless this collection is an instance of some class that provides a
     * guarantee).
     */
    Iterator<E> iterator();
    
    
    /**
     * Returns an array containing all of the elements in this collection.
     * If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     */
    Object[] toArray();
    
    /**
     * Returns an array containing all of the elements in this collection;
     * the runtime type of the returned array is that of the specified array.
     * If the collection fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this collection.
     * 
     * 
     *  toArray(new Object[0]) == toArray()
     */
    <T> T[] toArray(T[] a);
    
    
    
    
    boolean add(E e);
    
    boolean remove(Object o);
    
    boolean containsAll(Collection<?> c);
    
    boolean equals(Object o);
    
    int hashCode();
    
}
