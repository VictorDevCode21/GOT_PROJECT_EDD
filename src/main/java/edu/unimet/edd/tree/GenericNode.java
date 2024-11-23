package edu.unimet.edd.tree;

/**
 * The GenericNode class represents a node in a linked list. It stores data of any type
 * and has a reference to the next node.
 *
 * @param <T> The type of data this node holds (generic type).
 */
public class GenericNode<T> {

    private T data;         // The data the node holds
    private GenericNode<T> next;   // Reference to the next node

    /**
     * Constructor to create a new node with data.
     *
     * @param data The data to store in the node.
     */
    public GenericNode(T data) {
        this.data = data;
        this.next = null;
    }

    /**
     * Get the data stored in the node.
     *
     * @return The data of the node.
     */
    public T getData() {
        return data;
    }

    /**
     * Set the data for the node.
     *
     * @param data The data to set.
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Get the next node in the list.
     *
     * @return The next node.
     */
    public GenericNode<T> getNext() {
        return next;
    }

    /**
     * Set the next node in the list.
     *
     * @param next The next node.
     */
    public void setNext(GenericNode<T> next) {
        this.next = next;
    }
}
