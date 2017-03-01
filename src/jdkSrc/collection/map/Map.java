package jdkSrc.collection.map;

public interface Map<K,V> {

	/**
     * Returns the number of key-value mappings in this map.
     */
	int size();
	
	/**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     */
	boolean isEmpty();
	
	
	 /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     */
	V get(Object key);
	
	
	/**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value. 
     */
	V put(K key, V value);
	
	
	/**
     * Removes the mapping for a key from this map if it is present
     * (optional operation).
     */
	V remove(Object key);
	
	
	/**
     * Compares the specified object with this map for equality.
     */
	boolean equals(Object o);
	
	
	/**
     * Returns the hash code value for this map.
     */
	int hashCode();
	
	
	
	interface Entry<K,V>{
		/**
         * Returns the key corresponding to this entry.
         */
		K getKey();
		
		/**
         * Returns the value corresponding to this entry.
         */
		V getValue();
		
		/**
         * Replaces the value corresponding to this entry with the specified
         * value (optional operation).
         */
		V setValue(V value);
		
		/**
         * Compares the specified object with this entry for equality.
         * Returns <tt>true</tt> if the given object is also a map entry and
         * the two entries represent the same mapping. 
         */
		boolean equals(Object o);
		
		
		/**
         * Returns the hash code value for this map entry.  The hash code
         * of a map entry <tt>e</tt> is defined to be: <pre>
         *     (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
         *     (e.getValue()==null ? 0 : e.getValue().hashCode())
         */
		int hashCode();
	}
	
}
