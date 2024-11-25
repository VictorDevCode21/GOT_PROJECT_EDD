package edu.unimet.edd.utils;

/**
 * Custom iterator interface for navigating collections.
 *
 * @param <T> The type of elements returned by this iterator.
 */
public interface Iterator<T> {
    
    /**
     * Checks if there are more elements to iterate over.
     *
     * @return true if there are more elements, false otherwise.
     */
    boolean hasNext();
    
    
    /**
     * Retrieves the next element in the iteration.
     *
     * @return the next element in the iteration.
     * 
     */
    T next();
}
