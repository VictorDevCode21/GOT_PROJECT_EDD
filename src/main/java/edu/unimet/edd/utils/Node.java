package edu.unimet.edd.utils;

/**
 * Represents a node in the singly linked list, storing a Person object.
 */
public class Node {

    private Person value; // The Person object stored in this node
    private Node next;    // Reference to the next node

    /**
     * Constructs a new Node with the given Person value.
     *
     * @param value the Person object to store in the node
     */
    public Node(Person value) {
        this.value = value;
        this.next = null;
    }

    public Person getValue() {
        return value;
    }

    public void setValue(Person value) {
        this.value = value;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
