package edu.unimet.edd.hash;

import edu.unimet.edd.utils.Person;

/**
 * Represents a singly linked list to store Entries of String and Person. This
 * class is used for handling collisions in the HashTable.
 */
public class LinkedList {

    private Node first; // First node of the list
    private int size;

    /**
     * Constructs an empty LinkedList.
     */
    public LinkedList() {
        first = null;
        size = 0;
    }

    /**
     * Returns the current size of the LinkedList.
     *
     * @return the number of elements in the list
     */
    public int getSize() {
        return size;
    }

    /**
     * Adds a new value to the end of the LinkedList. This method is used for
     * adding new entries in the HashTable.
     *
     * @param key the key of the Entry
     * @param value the value of the Entry (Person)
     */
    public void add(String key, Person value) {
        Entry<String, Person> entry = new Entry<>(key, value);
        Node node = new Node(entry);
        if (size == 0) { // Empty list
            first = node;
        } else { // Non-empty list
            Node aux = first;
            while (aux.getNext() != null) {
                aux = aux.getNext();
            }
            aux.setNext(node);
        }
        size++;
    }

    /**
     * Removes and returns the first element of the LinkedList.
     *
     * @return the removed element (Entry<String, Person>)
     * @throws IllegalStateException if the list is empty
     */
    public Entry<String, Person> removeFirst() {
        if (size == 0) {
            throw new IllegalStateException("Cannot remove from an empty list");
        }
        Entry<String, Person> value = first.getValue();
        first = first.getNext();
        size--;
        return value;
    }

    /**
     * Checks if the LinkedList contains an entry with the given key.
     *
     * @param key the key to search for
     * @return true if the key exists in the list, false otherwise
     */
    public boolean containsKey(String key) {
        Node current = first;
        while (current != null) {
            if (current.getValue().getKey().equals(key)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * Returns the first node in the LinkedList.
     *
     * @return the first node
     */
    public Node getFirstNode() {
        return first;
    }

    /**
     * Retrieves the value associated with a key from the LinkedList.
     *
     * @param key the key to search for
     * @return the value associated with the key, or null if not found
     */
    public Person get(String key) {
        Node current = first;
        while (current != null) {
            if (current.getValue().getKey().equals(key)) {
                return current.getValue().getValue();
            }
            current = current.getNext();
        }
        return null; // Return null if key is not found
    }

    /**
     * Prints all elements of the LinkedList in order.
     */
    public void print() {
        Node current = first;
        while (current != null) {
            System.out.print(current.getValue().getKey() + " -> ");
            current = current.getNext();
        }
        System.out.println("null");
    }
}
