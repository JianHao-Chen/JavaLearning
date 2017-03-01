package jdkSrc.collection;

import java.util.Arrays;
import java.util.Iterator;

public abstract class AbstractCollection<E> implements Collection<E>{

    protected AbstractCollection() {
    }
    
    
    // Query Operations
    
    /**
     * Returns an iterator over the elements contained in this collection.
     */
    public abstract Iterator<E> iterator();
    
    
    public abstract int size();
    
    
    public boolean isEmpty(){
        return size() == 0;
    }
    
    
    /**
     * <p>This implementation iterates over the elements in the collection,
     * checking each element in turn for equality with the specified element.
     */
    public boolean contains(Object o) {
        Iterator<E> e = iterator();
        while (e.hasNext())
            if (o.equals(e.next()))
                return true;
        
        return false;
    }
    
    
    
    /**
     * <p>This implementation returns an array containing all the elements
     * returned by this collection's iterator, in the same order, stored in
     * consecutive elements of the array, starting with index {@code 0}.
     * The length of the returned array is equal to the number of elements
     * returned by the iterator, even if the size of this collection changes
     * during iteration, as might happen if the collection permits
     * concurrent modification during iteration.  The {@code size} method is
     * called only as an optimization hint; the correct result is returned
     * even if the iterator returns a different number of elements.
     *
     * <p>This method is equivalent to:
     *
     *  <pre> {@code
     * List<E> list = new ArrayList<E>(size());
     * for (E e : this)
     *     list.add(e);
     * return list.toArray();
     * }</pre>
     */
    public Object[] toArray() {
        Object[] r = new Object[size()];
        Iterator<E> it = iterator();
        for (int i = 0; i < r.length; i++){
            
            r[i] = it.next();   
        }
        return r;
    }
    
    /**
     *<p>This method is equivalent to:
     *      List<E> list = new ArrayList<E>(size());
     *      for (E e : this)
     *          list.add(e);
     *          return list.toArray(a);
     */
    public <T> T[] toArray(T[] a) {
        int size = size();
        T[] r = (T[])java.lang.reflect.Array
        .newInstance(a.getClass().getComponentType(), size);
        Iterator<E> it = iterator();
        
        for (int i = 0; i < r.length; i++) {
            r[i] = (T)it.next();
        }
        return r;
    }
    
    // Bulk Operations
    
    
    /**
     * <p>This implementation iterates over the specified collection,
     * checking each element returned by the iterator in turn to see
     * if it's contained in this collection.  If all elements are so
     * contained <tt>true</tt> is returned, otherwise <tt>false</tt>.
     */
    public boolean containsAll(Collection<?> c) {
        Iterator<?> e = c.iterator();
        while (e.hasNext())
            if (!contains(e.next()))
            return false;
        return true;
    }
    
    // Modification Operations
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }
    
    
    /**
     * This implementation iterates over the collection looking for the
     * specified element.  If it finds the element, it removes the element
     * from the collection using the iterator's remove method.
     */
    public boolean remove(Object o) {
        Iterator<E> e = iterator();
        if (o==null){
            // throw exception
        }
        
        while(e.hasNext()){
            if(o.equals(e.next())){
                e.remove();
                return true;
            }
        }
        return false;
    }
    
    
    public String toString() {
        Iterator<E> i = iterator();
        if (! i.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = i.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (! i.hasNext())
            return sb.append(']').toString();
            sb.append(", ");
        }
    }
    
}
