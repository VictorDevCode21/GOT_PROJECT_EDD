package edu.unimet.edd.tree;

/**
 * The GenericLinkedList class represents a generic linked list. It allows
 * adding, removing, and traversing elements.
 *
 * @param <T> The type of data the list holds (generic type).
 */
public class GenericLinkedList<T> {

    private GenericNode<T> first;  // Head node of the list
    private GenericNode<T> last;  // Tail node of the list (used for efficient enqueue operations)
    private int size; // To track the size of the list

    /**
     * Constructor to create an empty linked list.
     */
    public GenericLinkedList() {
        this.first = null;
        this.last = null;
        this.size = 0;
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
        setSize(getSize() + 1);
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
        setSize(getSize() - 1); // Decrement size when a node is removed
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
            current = current.getNext();
        }
    }

    /**
     * Retrieves the first node of the linked list.
     * @return the first
     * 
     */
    public GenericNode<T> getFirst() {
        return first;
    }

    /**
     * Sets the first node of the linked list.
     * @param first the first to set
     * 
     */
    public void setFirst(GenericNode<T> first) {
        this.first = first;
    }

    /**
     * Retrieves the last node of the linked list.
     * @return the last
     */
    public GenericNode<T> getLast() {
        return last;
    }

    /**
     * Sets the last node of the linked list.
     * @param last the last to set
     */
    public void setLast(GenericNode<T> last) {
        this.last = last;
    }

    /**
     * Retrieves the current size of the linked list.
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Updates the size of the linked list.
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Removes and returns the first element from the linked list. If the list
     * is empty, it returns null.
     *
     * @return The first element of the list, or null if the list is empty.
     */
    public T removeFirst() {
        if (first == null) { // Check if the list is empty
            return null;
        }

        T data = first.getData(); // Store the data of the current head
        first = first.getNext(); // Move the first to the next node

        if (first == null) { // If the list is now empty, update the last
            last = null;
        }

        size--; // Decrease the size of the list
        return data; // Return the removed data
    }

    public String[] toArray() {
        String[] array = new String[getSize()];
        GenericNode current = first;
        int index = 0;

        while (current != null) {
            array[index++] = current.getData().toString();
            current = current.getNext();
        }

        return array;
    }

    /**
     * Checks if the linked list contains the specified element.
     *
     * @param element The element to search for in the list.
     * @return true if the element exists in the list; false otherwise.
     */
    public boolean contains(T element) {
        GenericNode<T> current = first; // Start from the head of the list

        while (current != null) {
            if (current.getData().equals(element)) { // Use equals to compare elements
                return true;
            }
            current = current.getNext(); // Move to the next node
        }

        return false; // Element not found
    }

}
