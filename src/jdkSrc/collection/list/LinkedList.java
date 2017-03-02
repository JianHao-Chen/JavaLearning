package jdkSrc.collection.list;

import java.util.Iterator;

import jdkSrc.collection.ConcurrentModificationException;
import jdkSrc.collection.NoSuchElementException;

/**
 *  Linked list implementation of the <tt>List</tt> interface.  Implements all
 * optional list operations.
 * 
 *  In addition to implementing the <tt>List</tt> interface,
 * the <tt>LinkedList</tt> class provides uniformly named methods to
 * <tt>get</tt>, <tt>remove</tt> and <tt>insert</tt> an element at the
 * beginning and end of the list.
 * 
 *  All of the operations perform as could be expected for a doubly-linked
 * list.  Operations that index into the list will traverse the list from
 * the beginning or the end, whichever is closer to the specified index.<p>
 * 
 * @param <E>
 */


public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>{
    
    private transient Entry<E> header = new Entry<E>(null, null, null);
    private transient int size = 0;
    
    /**
     * Constructs an empty list.
     */
    public LinkedList() {
        header.next = header.previous = header;
    }
    
    public int size() {
        return size;
    }
    
    
    private Entry<E> addBefore(E e, Entry<E> entry) {
        Entry<E> newEntry = new Entry<E>(e, entry, entry.previous);
        newEntry.previous.next = newEntry;
        newEntry.next.previous = newEntry;
        size++;
        modCount++;
        return newEntry;
    }

    private E remove(Entry<E> e) {
        if (e == header)
            throw new NoSuchElementException();
        E result = e.element;
        e.previous.next = e.next;
        e.next.previous = e.previous;
        e.next = e.previous = null;
        e.element = null;
        size--;
        modCount++;
        return result;
    }
    
    /**
     * Returns a list-iterator of the elements in this list (in proper
     * sequence), starting at the specified position in the list.
     * Obeys the general contract of <tt>List.listIterator(int)</tt>.<p>
     */
    public ListIterator<E> listIterator(int index) {
        return new ListItr(index);
    }

    private class ListItr implements ListIterator<E> {
        private Entry<E> lastReturned = header;
        private Entry<E> next;
        private int nextIndex;
        private int expectedModCount = modCount;
        
        ListItr(int index) {
            if (index < 0 || index > size)
                throw new IndexOutOfBoundsException("Index: "+index+
                                    ", Size: "+size);
            if (index < (size >> 1)) {
                next = header.next;
                for (nextIndex=0; nextIndex<index; nextIndex++)
                    next = next.next;
            }
            else {
                next = header;
                for (nextIndex=size; nextIndex>index; nextIndex--)
                    next = next.previous;
            }
        }
        
        public boolean hasNext() {
            return nextIndex != size;
        }

        public boolean hasPrevious() {
            return nextIndex != 0;
        }

        @Override
        public E next() {
            checkForComodification();
            if (nextIndex == size)
                throw new NoSuchElementException();
            
            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.element;
        }

        public E previous() {
            if (nextIndex == 0)
                throw new NoSuchElementException();
            
            lastReturned = next = next.previous;
            nextIndex--;
            checkForComodification();
            return lastReturned.element;
        }

        public int nextIndex() {
            return nextIndex;
        }

        public int previousIndex() {
            return nextIndex-1;
        }

        public void add(E e) {
            checkForComodification();
            lastReturned = header;
            addBefore(e, next);
            nextIndex++;
            expectedModCount++;
        }

        public void remove() {
            checkForComodification();
            Entry<E> lastNext = lastReturned.next;
            try {
                LinkedList.this.remove(lastReturned);
            } catch (NoSuchElementException e) {
                throw new IllegalStateException();
            }
            if (next==lastReturned)
                next = lastNext;
            else
                nextIndex--;
            lastReturned = header;
            expectedModCount++;
        }

        public void set(E e) {
            if (lastReturned == header)
                throw new IllegalStateException();
            checkForComodification();
            lastReturned.element = e;
        }
        
        final void checkForComodification() {
            if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
        }
    }
    
    
    
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }
    
    /** Adapter to provide descending iterators via ListItr.previous */
    private class DescendingIterator implements Iterator {
        final ListItr itr = new ListItr(size());
        public boolean hasNext() {
            return itr.hasPrevious();
        }
        public E next() {
            return itr.previous();
        }
        public void remove() {
            itr.remove();
        }
    }
    
    /**
     * 双向链表的节点.
     *
     * @param <E>
     */
    private static class Entry<E> {
        E element;
        Entry<E> next;
        Entry<E> previous;
        
        Entry(E element, Entry<E> next, Entry<E> previous) {
            this.element = element;
            this.next = next;
            this.previous = previous;
        }
    }

}
