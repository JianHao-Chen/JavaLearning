package jdkSrc.collection.set;

import java.util.Iterator;

import jdkSrc.collection.map.HashMap;

public class HashSet<E>
    extends AbstractSet<E>
    implements Set<E>{
    
    private transient HashMap<E,Object> map;
    
    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();
    
    /**
     * Constructs a new, empty set; the backing <tt>HashMap</tt> instance has
     * default initial capacity (16) and load factor (0.75).
     */
    public HashSet() {
        map = new HashMap<E,Object>();
    }
    
    

    /**
     * Returns an iterator over the elements in this set.  The elements
     * are returned in no particular order.
     */
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    /**
     * Returns the number of elements in this set (its cardinality).
     */
    public int size() {
        return map.size();
    }
    
    /**
     * Returns <tt>true</tt> if this set contains no elements.
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this set contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this set
     * contains an element <tt>e</tt> such that
     */
    public boolean contains(Object o) {
        return map.containsKey(o);
    }
    
    /**
     * Adds the specified element to this set if it is not already present.
     */
    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }
    
    /**
     * Removes the specified element from this set if it is present.
     */
    public boolean remove(Object o) {
        return map.remove(o)==PRESENT;
    }
    
    
}
