package jdkSrc.collection.set;

import java.util.Iterator;

import jdkSrc.collection.Collection;


public interface Set<E> extends Collection<E>{

    
    /**
     * Returns the number of elements in this set (its cardinality).
     */
    int size();
    
    
    /**
     * Returns <tt>true</tt> if this set contains no elements.
     */
    boolean isEmpty();
    
    
    /**
     * Returns an iterator over the elements in this set. 
     */
    Iterator<E> iterator();
    
    
    /**
    * Returns an array containing all of the elements in this set.
    * If this set makes any guarantees as to what order its elements
    * are returned by its iterator, this method must return the
    * elements in the same order.
    */
    Object[] toArray();
    
    /**
     * Returns an array containing all of the elements in this set; the
     * runtime type of the returned array is that of the specified array.
     * If the set fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this set.
     */
    <T> T[] toArray(T[] a);
    
    
    boolean add(E e);
    
    
    boolean remove(Object o);
    
    
    boolean equals(Object o);
    
    
    int hashCode();
}
