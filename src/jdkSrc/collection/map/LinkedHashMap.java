package jdkSrc.collection.map;

import java.util.Iterator;

import jdkSrc.collection.ConcurrentModificationException;
import jdkSrc.collection.NoSuchElementException;

/**
 *  这个类继承了 HashMap , 并且它保存了一个双向链表的头结点,这个链表用于
 *  以另外一种方式(以结点插入的相同顺序输出)迭代这个Map.
 *
 * @param <K>
 * @param <V>
 */

public class LinkedHashMap<K,V> 
    extends HashMap<K,V> implements Map<K,V>{

    
    /**
     * The head of the doubly linked list.
     */
    private transient Entry<K,V> header;
    
    /**
     * The iteration ordering method for this linked hash map: <tt>true</tt>
     * for access-order, <tt>false</tt> for insertion-order.
     */
    private final boolean accessOrder;
    
    
    /**
     * Constructs an empty insertion-ordered <tt>LinkedHashMap</tt> instance
     * with the specified initial capacity and load factor.
     */
    public LinkedHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity);
        accessOrder = false;
    }
    
    /**
     * Constructs an empty insertion-ordered <tt>LinkedHashMap</tt> instance
     * with the default initial capacity (16) and load factor (0.75).
     */
    public LinkedHashMap() {
    super();
        accessOrder = false;
    }
    
    /**
     * Called by superclass constructors and pseudoconstructors (clone,
     * readObject) before any entries are inserted into the map.  Initializes
     * the chain.
     */
    void init() {
        header = new Entry<K,V>(-1, null, null, null);
        header.before = header.after = header;
    }
    
    /**
     * Transfers all entries to new table array.  This method is called
     * by superclass resize.  It is overridden for performance, as it is
     * faster to iterate using our linked list.
     * 
     * 覆写HashMap中的transfer方法，它在父类的resize方法中被调用,  
     * 扩容后,将key-value对重新映射到新的newTable中,  
     * 覆写该方法的目的是为了提高复制的效率,  
     *      这里充分利用双向循环链表的特点进行迭代,不用对底层的数组进行for循环。
     *      因为底层数组不少null的slot。
     */
    void transfer(HashMap.Entry[] newTable) {
        int newCapacity = newTable.length;
        for (Entry<K,V> e = header.after; e != header; e = e.after) {
            int index = indexFor(e.hash, newCapacity);
            e.next = newTable[index];
            newTable[index] = e;
        }
    }
    
    
    /**
     * This override alters behavior of superclass put method. It causes newly
     * allocated entry to get inserted at the end of the linked list and
     * removes the eldest entry if appropriate.
     */
    void addEntry(int hash, K key, V value, int bucketIndex) {
        createEntry(hash, key, value, bucketIndex);
        if (size >= threshold)
            resize(2 * table.length);
    }
    
    
    /**
     * This override differs from addEntry in that it doesn't resize the
     * table or remove the eldest entry.
     */
    void createEntry(int hash, K key, V value, int bucketIndex) {
        HashMap.Entry<K,V> old = table[bucketIndex];
        Entry<K,V> e = new Entry<K,V>(hash, key, value, old);
        table[bucketIndex] = e;
        e.addBefore(header);
        size++;
    }
    
    
    /**
     * LinkedHashMap entry.
     */
    private static class Entry<K,V> extends HashMap.Entry<K,V> {
        
        // These fields comprise the doubly linked list used for iteration.
        Entry<K,V> before, after;
        
        Entry(int hash, K key, V value, HashMap.Entry<K,V> next) {
            super(hash, key, value, next);
        }
        
        /**
         * Removes this entry from the linked list.
         */
        private void remove() {
            before.after = after;
            after.before = before;
        }
        
        /**
         * Inserts this entry before the specified existing entry in the list.
         */
        private void addBefore(Entry<K,V> existingEntry) {
            after = existingEntry;
            before = existingEntry.before;
            before.after = this;
            after.before = this;
        }
        
        
        void recordRemoval(HashMap<K,V> m) {
            remove();
        }
    }

    
    private abstract class LinkedHashIterator<T> implements Iterator<T>{
        Entry<K,V> nextEntry    = header.after;
        Entry<K,V> lastReturned = null;
        
        /**
         * The modCount value that the iterator believes that the backing
         * List should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        int expectedModCount = modCount;
        
        public boolean hasNext() {
            return nextEntry != header;
        }
        
        public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();
            
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            
            LinkedHashMap.this.remove(lastReturned.key);
            lastReturned = null;
            expectedModCount = modCount;
        }
        
        Entry<K,V> nextEntry() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            
            if (nextEntry == header)
                throw new NoSuchElementException();
            
            Entry<K,V> e = lastReturned = nextEntry;
            nextEntry = e.after;
            return e;
        }
    }
    
    Iterator<Map.Entry<K,V>> newEntryIterator() { return new EntryIterator(); }
    
    private class EntryIterator extends LinkedHashIterator<Map.Entry<K,V>> {
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }
}
