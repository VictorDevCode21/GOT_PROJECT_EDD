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
    
    
    /**
     * Retrieves the Person object stored in this node.
     *
     * @return the Person object stored in the node
     */
    public Person getValue() {
        return value;
    }
    
    
    /**
     * Sets the Person object stored in this node.
     *
     * @param value the Person object to set in the node
     */
    public void setValue(Person value) {
        this.value = value;
    }
    
    
    /**
     * Retrieves the next node in the linked list.
     *
     * @return the next Node in the linked list
     */
    public Node getNext() {
        return next;
    }
    
    
    /**
     * Sets the next node in the linked list.
     *
     * @param next the next Node to set
     */
    public void setNext(Node next) {
        this.next = next;
    }
}
