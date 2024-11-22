package edu.unimet.edd.tree;

/**
 * The GenericLinkedList class represents a generic linked list.
 * It allows adding, removing, and traversing elements.
 * 
 * @param <T> The type of data the list holds (generic type).
 */
public class GenericLinkedList<T> {
    private GenericNode<T> first;  // Head node of the list
    private GenericNode<T> last;  // Tail node of the list (used for efficient enqueue operations)

    /**
     * Constructor to create an empty linked list.
     */
    public GenericLinkedList() {
        this.first = null;
        this.last = null;
    }

    /**
     * Add a new node to the end of the list.
     * 
     * @param data The data to store in the new node.
     */
    public void add(T data) {
        GenericNode<T> newGenericNode = new GenericNode<>(data);  // Create a new node with the data
        if (getLast() == null) {
            setFirst(newGenericNode);  // If the list is empty, the new node becomes both the first and the last
            setLast(newGenericNode);
        } else {
            getLast().setNext(newGenericNode);  // Attach the new node at the end
            setLast(newGenericNode);         // Update the last to the new node
        }
    }

    /**
     * Remove and return the first element in the list.
     * 
     * @return The data of the removed node, or null if the list is empty.
     */
    public T remove() {
        if (getFirst() == null) {
            return null;  // If the list is empty, return null
        }
        T data = getFirst().getData();  // Get the data from the first
        setFirst(getFirst().getNext());    // Move the first to the next node
        if (getFirst() == null) {
            setLast(null);  // If the list is now empty, reset the last to null
        }
        return data;
    }

    /**
     * Check if the list is empty.
     * 
     * @return True if the list is empty, false otherwise.
     */
    public boolean isEmpty() {
        return getFirst() == null;
    }

    /**
     * Traverse and print all elements in the list.
     */
    public void printList() {
        GenericNode<T> current = getFirst();
        while (current != null) {
            System.out.println(current.getData());
            current = current.getNext();
        }
    }

    /**
     * @return the first
     */
    public GenericNode<T> getFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(GenericNode<T> first) {
        this.first = first;
    }

    /**
     * @return the last
     */
    public GenericNode<T> getLast() {
        return last;
    }

    /**
     * @param last the last to set
     */
    public void setLast(GenericNode<T> last) {
        this.last = last;
    }
}
