package jdkSrc.collection;

public class ConcurrentModificationException extends RuntimeException{
    /**
     * Constructs a ConcurrentModificationException with no
     * detail message.
     */
    public ConcurrentModificationException() {
    }

    /**
     * Constructs a <tt>ConcurrentModificationException</tt> with the
     * specified detail message.
     *
     * @param message the detail message pertaining to this exception.
     */
    public ConcurrentModificationException(String message) {
    super(message);
    }
}
