package jdkSrc.collection.set;

import java.util.Iterator;

import jdkSrc.collection.AbstractCollection;
import jdkSrc.collection.Collection;

public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E> {
    
    protected AbstractSet() {
    }
    
    // Comparison and hashing
    
    /**
     * Compares the specified object with this set for equality.  Returns
     * <tt>true</tt> if the given object is also a set, the two sets have
     * the same size, and every member of the given set is contained in
     * this set.  This ensures that the <tt>equals</tt> method works
     * properly across different implementations of the <tt>Set</tt>
     * interface.<p>
     */
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Set))
            return false;
        
        Collection c = (Collection) o;
        if (c.size() != size())
            return false;
        
        return containsAll(c);
    }
    
    
    
    /**
     * Returns the hash code value for this set.  The hash code of a set is
     * defined to be the sum of the hash codes of the elements in the set
     */
    public int hashCode() {
        int h = 0;
        Iterator<E> i = iterator();
        while (i.hasNext()) {
            E obj = i.next();
            if (obj != null)
                h += obj.hashCode();
        }
        return h;
    }
}
