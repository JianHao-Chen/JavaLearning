package jdkSrc.collection.list;

import java.util.Iterator;

import jdkSrc.collection.AbstractCollection;
import jdkSrc.collection.ConcurrentModificationException;
import jdkSrc.collection.NoSuchElementException;

public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {

    /**
     * The number of times this list has been <i>structurally modified</i>.
     */
    protected transient int modCount = 0;
    
    protected AbstractList() {
    }
    
    /**
    * Appends the specified element to the end of this list (optional
    * operation).
    *
    * <p>Lists that support this operation may place limitations on what
    * elements may be added to this list.  In particular, some
    * lists will refuse to add null elements, and others will impose
    * restrictions on the type of elements that may be added.  List
    * classes should clearly specify in their documentation any restrictions
    * on what elements may be added.
    *
    * <p>This implementation calls {@code add(size(), e)}.
    */
    public boolean add(E e) {
        add(size(), e);
        return true;
    }
    
    /**
     * This implementation always throws an
     * {@code UnsupportedOperationException}.
     */
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }
    
    
    abstract public E get(int index);
    
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }
    
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }
    
    
    // Search Operations
    
    /**
     * <p>This implementation first gets a list iterator (with
     * {@code listIterator()}).  Then, it iterates over the list until the
     * specified element is found or the end of the list is reached.
     */
    public int indexOf(Object o) {
        ListIterator<E> e = listIterator();
        if (o==null) 
            return -1;
        else{
            while (e.hasNext())
                if (o.equals(e.next()))
                    return e.previousIndex();
        }
        return -1;
    }
    
    
    /**
     * This implementation first gets a list iterator that points to the end
     * of the list (with {@code listIterator(size())}).  Then, it iterates
     * backwards over the list until the specified element is found, or the
     * beginning of the list is reached.
     */
    public int lastIndexOf(Object o) {
        ListIterator<E> e = listIterator(size());
        if (o==null) 
            return -1;
        else{
            while (e.hasPrevious())
                if (o.equals(e.previous()))
                    return e.nextIndex();
        }
        return -1;
    }
    
    
    
    
    // Iterators
    
    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * <p>This implementation returns a straightforward implementation of the
     * iterator interface, relying on the backing list's {@code size()},
     * {@code get(int)}, and {@code remove(int)} methods.
     */
    public Iterator<E> iterator() {
        return new Itr();
    }
    
    
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }
    
    
    public ListIterator<E> listIterator(final int index) {
        if (index<0 || index>size())
            throw new IndexOutOfBoundsException("Index: "+index);

        return new ListItr(index);
    }
    
    
    
    private class Itr implements Iterator<E> {
        /**
         * Index of element to be returned by subsequent call to next.
         */
        int cursor = 0;
        
        /**
         * Index of element returned by most recent call to next or
         * previous.  Reset to -1 if this element is deleted by a call
         * to remove.
         */
        int lastRet = -1;
        
        /**
         * The modCount value that the iterator believes that the backing
         * List should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        int expectedModCount = modCount;
        
        public boolean hasNext() {
            return cursor != size();
        }
        
        public E next() {
            checkForComodification();
            try {
                E next = get(cursor);
                lastRet = cursor++;
                return next;
            }
            catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            } 
        }
        
        public void remove() {
            if (lastRet == -1)
                throw new IllegalStateException();
            checkForComodification();
            
            try {
                AbstractList.this.remove(lastRet);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
                expectedModCount = modCount;
            }
            catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }
        
        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }
    
    
    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public E previous() {
            checkForComodification();
            try {
                int i = cursor - 1;
                E previous = get(i);
                lastRet = cursor = i;
                return previous;
            }
            catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor-1;
        }

        public void add(E e) {
            checkForComodification();
            try {
                AbstractList.this.add(cursor++, e);
                lastRet = -1;
                expectedModCount = modCount;
            }
            catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void set(E e) {
            if (lastRet == -1)
                throw new IllegalStateException();
            checkForComodification();
            try {
                AbstractList.this.set(lastRet, e);
                expectedModCount = modCount;
            }
            catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
    
    
    // Comparison and hashing
    
    /**
     * Compares the specified object with this list for equality.  Returns
     * {@code true} if and only if the specified object is also a list, both
     * lists have the same size, and all corresponding pairs of elements in
     * the two lists are <i>equal</i>.  (Two elements {@code e1} and
     * {@code e2} are <i>equal</i> if {@code (e1==null ? e2==null :
     * e1.equals(e2))}.)  In other words, two lists are defined to be
     * equal if they contain the same elements in the same order.<p>
     */
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof List))
            return false;
        
        ListIterator<E> e1 = listIterator();
        ListIterator e2 = ((List) o).listIterator();
        
        while(e1.hasNext() && e2.hasNext()) {
            E o1 = e1.next();
            Object o2 = e2.next();
            if (!(o1==null ? o2==null : o1.equals(o2)))
            return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }
    
    
    /**
     * Returns the hash code value for this list.
     *
     * <p>This implementation uses exactly the code that is used to define the
     * list hash function in the documentation for the {@link List#hashCode}
     * method.
     */
    public int hashCode() {
        int hashCode = 1;
        Iterator<E> i = iterator();
        while (i.hasNext()) {
            E obj = i.next();
            hashCode = 31*hashCode + (obj==null ? 0 : obj.hashCode());
        }
        return hashCode;
    }
}
