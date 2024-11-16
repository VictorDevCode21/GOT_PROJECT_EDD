package edu.unimet.edd.utils;

import edu.unimet.edd.utils.Person;

/**
 * Represents a singly linked list to store Person objects. This list is used
 * for handling people in a genealogical tree.
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
     * Adds a new Person to the end of the LinkedList.
     *
     * @param person the Person to be added to the list
     */
    public void add(Person person) {
        Node node = new Node(person);
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
     * @return the removed Person
     * @throws IllegalStateException if the list is empty
     */
    public Person removeFirst() {
        if (size == 0) {
            throw new IllegalStateException("Cannot remove from an empty list");
        }
        Person value = first.getValue();
        first = first.getNext();
        size--;
        return value;
    }

    /**
     * Checks if the LinkedList contains the given Person.
     *
     * @param person the Person to search for
     * @return true if the Person exists in the list, false otherwise
     */
    public boolean contains(Person person) {
        Node current = first;
        while (current != null) {
            if (current.getValue().equals(person)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * Returns the Person at the specified index in the LinkedList.
     *
     * @param index the index of the Person to retrieve
     * @return the Person at the given index
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    public Person get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getValue();
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
     * Prints all elements of the LinkedList in order.
     */
    public void print() {
        Node current = first;
        while (current != null) {
            System.out.print(current.getValue().getName() + " -> ");
            current = current.getNext();
        }
        System.out.println("null");
    }
}
