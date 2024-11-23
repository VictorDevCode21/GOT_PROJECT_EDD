package edu.unimet.edd.utils;


import edu.unimet.edd.listeners.HashTableListener;

/**
 * A custom linked list for managing HashTable listeners.
 */
public class LinkedListListeners {

    private Node head;

    /**
     * Represents a node in the linked list.
     */
    private class Node {

        HashTableListener listener;
        Node next;

        public Node(HashTableListener listener) {
            this.listener = listener;
            this.next = null;
        }
    }

    /**
     * Adds a new listener to the list.
     *
     * @param listener The listener to add.
     */
    public void add(HashTableListener listener) {
        if (listener == null || contains(listener)) {
            return;
        }
        Node newNode = new Node(listener);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    /**
     * Checks if a listener is already in the list.
     *
     * @param listener The listener to check.
     * @return True if the listener is in the list, false otherwise.
     */
    public boolean contains(HashTableListener listener) {
        Node current = head;
        while (current != null) {
            if (current.listener == listener) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Notifies all listeners of a HashTable update.
     */
    public void notifyListeners() {
        Node current = head;
        while (current != null) {
            current.listener.onHashTableUpdated();
            current = current.next;
        }
    }
}
