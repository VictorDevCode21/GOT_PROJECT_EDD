package edu.unimet.edd.hash;

/**
 * Represents a key-value pair used in a HashTable.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public class Entry<K, V> {
    private K key;
    private V value;

    /**
     * Constructs an entry with the specified key and value.
     *
     * @param key the key of the entry
     * @param value the value of the entry
     */
    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the key of the entry.
     *
     * @return the key of the entry
     */
    public K getKey() {
        return key;
    }

    /**
     * Returns the value of the entry.
     *
     * @return the value of the entry
     */
    public V getValue() {
        return value;
    }

    /**
     * Sets the value of the entry.
     *
     * @param value the new value to set
     */
    public void setValue(V value) {
        this.value = value;
    }
}
