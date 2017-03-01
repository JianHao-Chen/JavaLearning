package jdkSrc.collection.list;

import java.util.Iterator;

import jdkSrc.collection.Collection;

public interface List<E> extends Collection<E> {

    /**
     * Returns the number of elements in this list.
     */
    int size();
    
    
    /**
     * Returns <tt>true</tt> if this list contains no elements.
     */
    boolean isEmpty();
    
    
    /**
     * Returns <tt>true</tt> if this list contains the specified element.
     */
    boolean contains(Object o);
    
    
    /**
     * Returns an iterator over the elements in this list in proper sequence.
     */
    Iterator<E> iterator();
    
    
    /**
     * Returns an array containing all of the elements in this list in proper
     * sequence (from first to last element).
     * 
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this list.  (In other words, this method must
     * allocate a new array even if this list is backed by an array).
     * The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     */
    Object[] toArray();
    
    /**
     * Returns an array containing all of the elements in this list in
     * proper sequence (from first to last element); the runtime type of
     * the returned array is that of the specified array.  If the list fits
     * in the specified array, it is returned therein.  Otherwise, a new
     * array is allocated with the runtime type of the specified array and
     * the size of this list.
     */
    <T> T[] toArray(T[] a);
    
    
    /**
     * Appends the specified element to the end of this list (optional
     * operation).
     */
    boolean add(E e);
    
    
    /**
     * Removes the first occurrence of the specified element from this list,
     * if it is present (optional operation).
     */
    boolean remove(Object o);
    
    
    // Comparison and hashing
    
    
    /**
     * Compares the specified object with this list for equality.  Returns
     * <tt>true</tt> if and only if the specified object is also a list, both
     * lists have the same size, and all corresponding pairs of elements in
     * the two lists are <i>equal</i>.
     */
    boolean equals(Object o);
    
    /**
     * Returns the hash code value for this list.  The hash code of a list
     * is defined to be the result of the following calculation:
     * <pre>
     *  int hashCode = 1;
     *  Iterator&lt;E&gt; i = list.iterator();
     *  while (i.hasNext()) {
     *      E obj = i.next();
     *      hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
     *  }
     * </pre>
     * This ensures that <tt>list1.equals(list2)</tt> implies that
     * <tt>list1.hashCode()==list2.hashCode()</tt> for any two lists,
     * <tt>list1</tt> and <tt>list2</tt>, as required by the general
     * contract of {@link Object#hashCode}.
     */
    int hashCode();
    
    
    // Positional Access Operations
    
    /**
     * Returns the element at the specified position in this list.
     */
    E get(int index);
    
    
    /**
     * Replaces the element at the specified position in this list with the
     * specified element (optional operation).
     */
    E set(int index, E element);
    
    /**
     * Inserts the specified element at the specified position in this list
     * (optional operation).  Shifts the element currently at that position
     * (if any) and any subsequent elements to the right (adds one to their
     * indices).
     */
    void add(int index, E element);
    
    
    /**
     * Removes the element at the specified position in this list (optional
     * operation).  Shifts any subsequent elements to the left (subtracts one
     * from their indices).  Returns the element that was removed from the
     * list.
     */
    E remove(int index);
    
    
    // Search Operations
    
    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     */
    int indexOf(Object o);
    
    
    /**
     * Returns the index of the last occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     */
    int lastIndexOf(Object o);
    
    // List Iterators
    
    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     */
    ListIterator<E> listIterator();
 
}
