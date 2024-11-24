package edu.unimet.edd.hash;

import edu.unimet.edd.utils.Person;

/**
 * Represents a node in a linked list.
 * This class is adapted to store an Entry&lt;String, Person&gt; object.
 */
public class Node {
    private Entry<String, Person> value;
    private Node next;

    /**
     * Constructor for the Node class.
     *
     * @param value the value to store in the node (Entry&lt;String, Person&gt;)
     */
    public Node(Entry<String, Person> value) {
        this.value = value;
        this.next = null;
    }

    /**
     * Gets the value stored in the node.
     *
     * @return the value of the node (Entry&lt;String, Person&gt;)
     */
    public Entry<String, Person> getValue() {
        return value;
    }

    /**
     * Sets the value of the node.
     *
     * @param value the new value to store in the node (Entry&lt;String, Person&gt;)
     */
    public void setValue(Entry<String, Person> value) {
        this.value = value;
    }

    /**
     * Gets the next node.
     *
     * @return the next node
     */
    public Node getNext() {
        return next;
    }

    /**
     * Sets the next node.
     *
     * @param next the next node
     */
    public void setNext(Node next) {
        this.next = next;
    }
}
