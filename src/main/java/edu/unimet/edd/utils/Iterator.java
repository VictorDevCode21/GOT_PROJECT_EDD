package edu.unimet.edd.utils;

/**
 * Custom iterator interface for navigating collections.
 *
 * @param <T> The type of elements returned by this iterator.
 */
public interface Iterator<T> {

    boolean hasNext();

    T next();
}
