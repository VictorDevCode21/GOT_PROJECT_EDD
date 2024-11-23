package edu.unimet.edd.hash;

/**
 * A custom linked list to manage buckets in the HashTable.
 */
public class BucketLinkedList {

    private Node head;

    /**
     * Represents a node in the linked list, holding a key-value pair.
     */
    private class Node {

        String key;
        String value;
        Node next;

        public Node(String key, String value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    /**
     * Constructor to initialize an empty bucket.
     */
    public BucketLinkedList() {
        this.head = null;
    }

    /**
     * Adds or updates a key-value pair in the bucket.
     *
     * @param key The key to add or update.
     * @param value The value associated with the key.
     */
    public void put(String key, String value) {
        Node current = head;

        // Check if key already exists, update value
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        // Key does not exist, add new node
        Node newNode = new Node(key, value);
        newNode.next = head;
        head = newNode;
    }

    /**
     * Retrieves a value by key.
     *
     * @param key The key to search for.
     * @return The associated value, or null if key not found.
     */
    public String get(String key) {
        Node current = head;
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Iterates over all nodes in the bucket and applies the provided action.
     *
     * @param action The action to apply to each key-value pair.
     */
    public void forEach(BucketAction action) {
        Node current = head;
        while (current != null) {
            action.apply(current.key, current.value);
            current = current.next;
        }
    }

    /**
     * Clears the bucket by removing all nodes.
     */
    public void clear() {
        head = null;
    }

    /**
     * Creates a deep copy of the current bucket.
     *
     * @return A new BucketLinkedList containing the same key-value pairs.
     */
    public BucketLinkedList cloneBucket() {
        BucketLinkedList clone = new BucketLinkedList();
        Node current = head;

        while (current != null) {
            clone.put(current.key, current.value);
            current = current.next;
        }

        return clone;
    }

    /**
     * Functional interface for actions on key-value pairs.
     */
    public interface BucketAction {

        void apply(String key, String value);
    }
}
