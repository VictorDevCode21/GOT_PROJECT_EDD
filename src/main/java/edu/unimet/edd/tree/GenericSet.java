package edu.unimet.edd.tree;

/**
 * A custom set implementation using GenericLinkedList as the underlying data
 * structure. This set ensures that no duplicate elements are added.
 *
 * @param <T> The type of data stored in the set.
 */
public class GenericSet<T> {

    private GenericLinkedList<T> list;

    /**
     * Constructor to create an empty set.
     */
    public GenericSet() {
        this.list = new GenericLinkedList<>();
    }

    /**
     * Adds an element to the set. If the element is already present, it will
     * not be added.
     *
     * @param element The element to be added to the set.
     */
    public void add(T element) {
        // Check if the element is already in the set
        if (!contains(element)) {
            list.add(element); // Add the element if it's not already in the set
        }
    }

    /**
     * Checks if the set contains the specified element.
     *
     * @param element The element to check.
     * @return True if the element is in the set, false otherwise.
     */
    public boolean contains(T element) {
        GenericNode<T> current = list.getFirst();
        while (current != null) {
            if (current.getData().equals(element)) {
                return true; // Element found
            }
            current = current.getNext();
        }
        return false; // Element not found
    }

    /**
     * Removes the specified element from the set.
     *
     * @param element The element to be removed from the set.
     */
    public void remove(T element) {
        GenericNode<T> current = list.getFirst();
        GenericNode<T> previous = null;

        while (current != null) {
            if (current.getData().equals(element)) {
                if (previous == null) {
                    // If the element is the first node
                    list.setFirst(current.getNext());
                } else {
                    // If the element is in the middle or end
                    previous.setNext(current.getNext());
                }
                if (current == list.getLast()) {
                    list.setLast(previous); // Update last if needed
                }
                list.setSize(list.getSize() - 1); // Decrease the size
                return;
            }
            previous = current;
            current = current.getNext();
        }
    }

    /**
     * Clears the set by removing all elements.
     */
    public void clear() {
        list.setFirst(null); // Remove all elements by clearing the first node
        list.setLast(null);  // Also reset the last node
        list.setSize(0);     // Reset the size to 0
    }

    /**
     * Returns the number of elements in the set.
     *
     * @return The number of elements in the set.
     */
    public int size() {
        return list.getSize();
    }

    /**
     * Checks if the set is empty.
     *
     * @return True if the set is empty, false otherwise.
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Converts the set to an array of elements.
     *
     * @return An array of elements in the set.
     */
    public String[] toArray() {
        return list.toArray();
    }

    /**
     * Prints all elements in the set.
     */
    public void printSet() {
        list.printList();
    }
}
