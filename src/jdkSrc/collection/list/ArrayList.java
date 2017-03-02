package jdkSrc.collection.list;

import java.util.Arrays;

public class ArrayList<E> extends AbstractList<E>
    implements List<E>{

    /**
     * The array buffer into which the elements of the ArrayList are stored.
     * The capacity of the ArrayList is the length of this array buffer.
     */
    private transient Object[] elementData;
    
    
    /**
     * The size of the ArrayList (the number of elements it contains).
     */
    private int size;
    
    
    /**
     * Constructs an empty list with the specified initial capacity.
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        
        this.elementData = new Object[initialCapacity];
    }
    
    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public ArrayList() {
        this(10);
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }
    
    
    /**
     * Increases the capacity of this <tt>ArrayList</tt> instance, if
     * necessary, to ensure that it can hold at least the number of elements
     * specified by the minimum capacity argument.
     */
    public void ensureCapacity(int minCapacity) {
        modCount++;
        int oldCapacity = elementData.length;
        if (minCapacity > oldCapacity) {
            Object oldData[] = elementData;
            int newCapacity = (oldCapacity * 3)/2 + 1;
            if (newCapacity < minCapacity)
                newCapacity = minCapacity;
            // minCapacity is usually close to size, so this is a win:
            elementData = Arrays.copyOf(elementData, newCapacity);    
        }
    }
    
    
    // Positional Access Operations
    
    /**
     * Returns the element at the specified position in this list.
     */
    public E get(int index) {
        RangeCheck(index);
        return (E) elementData[index];
    }
    
    /**
     * Replaces the element at the specified position in this list with
     * the specified element.
     */
    public E set(int index, E element) {
        RangeCheck(index);
        E oldValue = (E) elementData[index];
        elementData[index] = element;
        return oldValue;
    }
    
    /**
     * Appends the specified element to the end of this list.
     */
    public boolean add(E e) {
        ensureCapacity(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }
    
    
    /**
     * Inserts the specified element at the specified position in this
     * list. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     */
    public void add(int index, E element) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(
            "Index: "+index+", Size: "+size);
        
        ensureCapacity(size+1);  // Increments modCount!!
        System.arraycopy(elementData, index, elementData, index + 1,
                size - index);
        elementData[index] = element;
        size++;
    }
    
    
    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     */
    public E remove(int index) {
        RangeCheck(index);
        modCount++;
        
        E oldValue = (E) elementData[index];
        
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData,index + 1,
                    elementData , index , numMoved);
        
        elementData[--size] = null;// Let gc do its work
        return oldValue;
    }
    
    
    /**
     * Removes the first occurrence of the specified element from this list,
     * if it is present.  If the list does not contain the element, it is
     * unchanged.
     */
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        else{
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }
    
    /*
     * Private remove method that skips bounds checking and does not
     * return the value removed.
     */
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--size] = null; // Let gc do its work
    }
    
   
    /**
     * Checks if the given index is in range.  If not, throws an appropriate
     * runtime exception.
     */
    private void RangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(
            "Index: "+index+", Size: "+size);
        
        if(index < 0)
            throw new IndexOutOfBoundsException("Index: "+index);
    }
}
