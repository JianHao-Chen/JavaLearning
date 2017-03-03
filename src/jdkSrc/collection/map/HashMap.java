package jdkSrc.collection.map;

import java.util.Iterator;

import jdkSrc.collection.ConcurrentModificationException;
import jdkSrc.collection.NoSuchElementException;
import jdkSrc.collection.set.AbstractSet;
import jdkSrc.collection.set.Set;

public class HashMap<K,V> implements Map<K,V>{

    //-------------		static field	-----------------	
    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    
    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;
    
    /**
     * The load factor used when none specified in constructor.
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    
    
    //-------------		instance variable	-----------------	
    /**
     * The table, resized as necessary. Length MUST Always be a power of two.
     */
    transient Entry[] table;
    
    /**
     * The number of key-value mappings contained in this map.
     */
    transient int size;
    
    /**
     * The next size value at which to resize (capacity * load factor).(阈值)
     */
    int threshold;
    
    /**
     * The load factor for the hash table.
     */
    final float loadFactor = DEFAULT_LOAD_FACTOR;
    
    /**
     * The number of times this HashMap has been structurally modified
     * Structural modifications are those that change the number of mappings in
     * the HashMap or otherwise modify its internal structure (e.g.,
     * rehash).  This field is used to make iterators on Collection-views of
     * the HashMap fail-fast.  (See ConcurrentModificationException).
     */
    transient volatile int modCount;
    
    //-------------     constructor     -----------------
    
    /**
     * Constructs an empty <tt>HashMap</tt> with the specified initial
     * capacity
     */
    public HashMap(int initialCapacity){
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        
        // Find a power of 2 >= initialCapacity
        int capacity = 1;
        while (capacity < initialCapacity)
            capacity <<= 1;
        
        threshold = (int)(capacity * loadFactor);
        table = new Entry[capacity];
        init();
    }
    
    /**
     * Constructs an empty <tt>HashMap</tt> with the default initial capacity
     * (16) and the default load factor (0.75).
     */
    public HashMap() {
        threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Entry[DEFAULT_INITIAL_CAPACITY];
        init();
    }
    
    /**
     * 因为对Entry的定位是通过 hashCode & (tableLength -1)的。
     * 如果hashCode的低位相同,将会定位到相同index。所以需要对key的hashCode进行再hash。
     * 
     * 使得最低位上原hashCode的8位都参与了^运算，所以在table.length为默认值16的情况下
     * hashCode任意位的变化基本都能反应到最终hash table 定位算法中
     * 
     * @param h
     * @return
     */
    static int hash(int h) {
     // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
    
    /**
     * Returns index for hash code h.
     * hashCode 与数组长度-1做一次"与"运算
     * 
     * <1> 因为数组长度为2的n次方,所以(数组长度-1)的值为 二进制的"11111..."
     * <2> 假设(数组长度-1)的值为 二进制的"11111...0",那么index = h & (length-1)
     *     的结果的最后一位一定只能为0,也就是index的值永远不会等于不能被2整除的数,
     *     即这个数组的可用空间就只有一半。
     * <3> 可用空间减半直接导致冲突发生的概率大增。
     * 
     */
    static int indexFor(int h, int length) {
        return h & (length-1);
    }
    
    /**
     * Initialization hook for subclasses.
     */
    void init() {
    }
    
    /**
     * Returns the number of key-value mappings in this map.
     */
    public int size() {
        return size;
    }
    
    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     */
    public V get(Object key) {
        if (key == null)
            return null;
        
        int hash = hash(key.hashCode());
        int index = indexFor(hash, table.length);
        Entry<K,V> e = table[index];
        for ( ; e != null ; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k)))
                return e.value;
        }
        return null;
    }
    
    /**
     * Returns the entry associated with the specified key in the
     * HashMap.  Returns null if the HashMap contains no mapping
     * for the key.
     */
    final Entry<K,V> getEntry(Object key) {
        int hash = (key == null) ? 0 : hash(key.hashCode());
        int index = indexFor(hash, table.length);
        Entry<K,V> e = table[index];
        for ( ; e != null ; e = e.next) {
            Object k;
            if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                return e;
        }
        return null;
    }
    
    
    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     */
    public V put(K key, V value) {
        if (key == null)
            return null;
        int hash = hash(key.hashCode());
        int index = indexFor(hash, table.length);
        Entry<K,V> e = table[index];
        for ( ; e != null ; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))){
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        modCount++;
        addEntry(hash, key, value, index);
        return null;
        
    }
    
    /**
     * Adds a new entry with the specified key, value and hash code to
     * the specified bucket.  It is the responsibility of this
     * method to resize the table if appropriate.
     */
    void addEntry(int hash, K key, V value, int bucketIndex) {
        Entry<K,V> e = table[bucketIndex];
        table[bucketIndex] = new Entry<K,V>(hash, key, value, e);
        if (size++ >= threshold)
            resize(2 * table.length);
    }
    
    /**
     * Rehashes the contents of this map into a new array with a
     * larger capacity.  This method is called automatically when the
     * number of keys in this map reaches its threshold.
     */
    void resize(int newCapacity) {
        Entry[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        
        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int)(newCapacity * loadFactor);
    }
    
    /**
     * Transfers all entries from current table to newTable.
     * 如果多线程同时执行同一个hashMap的此方法,可能造成死循环 
     */
    void transfer(Entry[] newTable) {
        Entry[] src = table;
        int newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) {
            Entry<K,V> e = src[j];
            if (e != null) {
                src[j] = null;
                do {    //头插法插入到新链表
                    Entry<K,V> next = e.next;
                    int i = indexFor(e.hash, newCapacity);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                }while (e != null);
            }
        }
        
    }
    
    
    /**
     * Removes the mapping for the specified key from this map if present.
     */
     public V remove(Object key) {
         Entry<K,V> e = removeEntryForKey(key);
         return (e == null ? null : e.value);
     }
     
     
     /**
      * Removes and returns the entry associated with the specified key
      * in the HashMap.  Returns null if the HashMap contains no mapping
      * for this key.
      */
     final Entry<K,V> removeEntryForKey(Object key) {
         int hash = (key == null) ? 0 : hash(key.hashCode());
         int i = indexFor(hash, table.length);
         
         Entry<K,V> prev = table[i];
         Entry<K,V> e = prev;
         
         while (e != null) {
             Entry<K,V> next = e.next;
             Object k;
             if (e.hash == hash &&
                     ((k = e.key) == key || (key != null && key.equals(k)))) {
                 modCount++;
                 size--;
                 if (prev == e)
                     table[i] = next;
                 else
                     prev.next = next;
                 e.recordRemoval(this);
                 return e;
             }
             prev = e;
             e = next;
         }
         return e;
     }
     
     
     /**
      * Returns <tt>true</tt> if this map contains a mapping for the
      * specified key.
      */
     public boolean containsKey(Object key) {
         return getEntry(key) != null;
     }
    
    
    static class Entry<K,V> implements Map.Entry<K,V>{
		final K key;
		V value;
		Entry<K,V> next;
		final int hash;
		
		/**
         * Creates new entry.
         */
		Entry(int h, K k, V v, Entry<K,V> n) {
			value = v;
            next = n;
            key = k;
            hash = h;
		}
		
		public final K getKey() {
			return key;
		}

		public final V getValue() {
			return value;
		}

		public final V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}
		
		public final boolean equals(Object o) {
			if (!(o instanceof Map.Entry))
                return false;
			Map.Entry e = (Map.Entry)o;
			Object k1 = getKey();
            Object k2 = e.getKey();
            
            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
            	Object v1 = getValue();
                Object v2 = e.getValue();
                if (v1 == v2 || (v1 != null && v1.equals(v2)))
                    return true;
            }
            return false;
		}
		
		public final int hashCode() {
			return (key==null   ? 0 : key.hashCode()) ^
             	(value==null ? 0 : value.hashCode());
		}
		 
		public final String toString() {
	        return getKey() + "=" + getValue();
	    }
		
		/**
         * This method is invoked whenever the value in an entry is
         * overwritten by an invocation of put(k,v) for a key k that's already
         * in the HashMap.
         */
		void recordAccess(HashMap<K,V> m) {
        }
		
		/**
         * This method is invoked whenever the entry is
         * removed from the table.
         */
        void recordRemoval(HashMap<K,V> m) {
        }
	}
    
    
    
    
    private abstract class HashIterator<E> implements Iterator<E>{
        Entry<K,V> next;    // next entry to return
        int expectedModCount;   // For fast-fail
        int index;      // current slot
        Entry<K,V> current; // current entry
        
        /*
         * 找到Entry数组中第一个不为null的slot
         */
        HashIterator() {
            expectedModCount = modCount;
            if (size > 0) { // advance to first entry
                Entry[] t = table;
                while (index < t.length && (next = t[index++]) == null)
                    ;
            }
        }
        
        public final boolean hasNext() {
            return next != null;
        }
        
        /*
         *  1. 取next的值 e = next.
         *  2. 更新 next:
         *      a. 如果当前链表未到达尾部,就把 next = e.next
         *      b. 如果当前链表到达尾部,就需要找到下一个不为空的链表,
         *          index = 当前slot的下标,
         *          next = 当前链表的首节点.
         */
        final Entry<K,V> nextEntry() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            Entry<K,V> e = next;
            if (e == null)
                throw new NoSuchElementException();
            if ((next = e.next) == null) {
                Entry[] t = table;
                while (index < t.length && (next = t[index++]) == null)
                    ;
            }
            current = e;
            return e;
        }
        
        public void remove() {
            if (current == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            Object k = current.key;
            current = null;
            HashMap.this.removeEntryForKey(k);
            expectedModCount = modCount;
        }
    }
    
    private final class ValueIterator extends HashIterator<V> {
        public V next() {
            return nextEntry().value;
        }
    }

    private final class KeyIterator extends HashIterator<K> {
        public K next() {
            return nextEntry().getKey();
        }
    }
    
    private final class EntryIterator extends HashIterator<Map.Entry<K,V>> {

        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }
    
    
    // Methods in HashMap to get Iterator instance.
    Iterator<K> newKeyIterator()   {
        return new KeyIterator();
    }
    
    Iterator<Map.Entry<K,V>> newEntryIterator()   {
        return new EntryIterator();
    }
    
    
    
    // Views
    
    
  //-----------------   View of Key   ----------------------------
    
    transient volatile Set<K> keySet = null;
    
    public Set<K> keySet() {
        Set<K> ks = keySet;
        return (ks != null ? ks : (keySet = new KeySet()));
    }
    
    private final class KeySet extends AbstractSet<K> {

        public Iterator<K> iterator() {
            return newKeyIterator();
        }

        public int size() {
            return size;
        }
        
        public boolean contains(Object o) {
            return containsKey(o);
        }
        
        public boolean remove(Object o) {
            return HashMap.this.removeEntryForKey(o) != null;
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    //-----------------   View of Entry   ----------------------------  
    private transient Set<Map.Entry<K,V>> entrySet = null;
    
    public Set<Map.Entry<K,V>> entrySet() {
        Set<Map.Entry<K,V>> es = entrySet;
        return es != null ? es : (entrySet = new EntrySet());
    }

    private final class EntrySet extends AbstractSet<Map.Entry<K,V>>{

        public Iterator<Map.Entry<K, V>> iterator() {
            return newEntryIterator();
        }
        
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<K,V> e = (Map.Entry<K,V>) o;
            Entry<K,V> candidate = getEntry(e.getKey());
            return candidate != null && candidate.equals(e);
        }

        public int size() {
            return size;
        }
    }
   
}	
