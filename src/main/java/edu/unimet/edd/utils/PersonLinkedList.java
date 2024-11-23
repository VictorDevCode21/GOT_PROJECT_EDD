package edu.unimet.edd.utils;

/**
 * A simple implementation of a LinkedList. This class stores elements and
 * allows access to them by index.
 */
public class PersonLinkedList {

    private Node head;  // Head node of the list
    private int size;   // Size of the list

    public PersonLinkedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Adds a string to the linked list.
     *
     * @param value The string value to add to the list.
     */
    public void addString(String value) {
        Node newNode = new Node(value);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }

    /**
     * Adds a Person to the linked list.
     *
     * @param person The Person object to add to the list.
     */
    public void addPerson(Person person) {
        Node newNode = new Node(person.getName()); // Use the name of the person as the value for the node.
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }

    /**
     * Returns an array with the names of all persons in the list.
     *
     * @return A String array containing all person names in the list.
     */
    public String[] getAllPersons() {
        if (this.isEmpty()) {
            return new String[0]; // Devuelve un array vacío en lugar de null
        }

        String[] persons = new String[size];
        Node current = head;
        int index = 0;

        while (current != null) {
            persons[index++] = current.getValue();
            current = current.getNext();
        }

        return persons;
    }

    /**
     * Returns the element at the specified index.
     *
     * @param index The index of the element to retrieve.
     * @return The element at the specified index.
     */
    public String get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getValue();
    }

    /**
     * Returns the size of the linked list.
     *
     * @return The size of the list.
     */
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;

    }

    /**
     * Retrieves an element from the linked list based on its value.
     *
     * @param value The string value to search for.
     * @return The value if found, otherwise null.
     */
    public String get(String value) {
        Node current = head;
        while (current != null) {
            if (current.getValue().equals(value)) {
                return current.getValue();
            }
            current = current.getNext();
        }
        // If the value is not found, return null
        return null;
    }

    /**
     * Removes the first occurrence of a specified value from the
     * PersonLinkedList.
     *
     * @param value The value to remove from the list.
     * @return true if the value was found and removed, false otherwise.
     */
    public boolean remove(String value) {
        if (head == null) {
            return false; // List is empty, nothing to remove
        }

        // Special case: the value to remove is at the head of the list
        if (head.getValue().equals(value)) {
            head = head.getNext(); // Move head to the next node
            size--;
            return true;
        }

        // Traverse the list to find the node to remove
        Node current = head;
        while (current.getNext() != null) {
            if (current.getNext().getValue().equals(value)) {
                // Found the node, remove it by skipping it
                current.setNext(current.getNext().getNext());
                size--;
                return true;
            }
            current = current.getNext();
        }

        // If we reach here, the value was not found
        return false;
    }

    /**
     * Returns an iterator for this PersonLinkedList.
     *
     * @return A new iterator for the list.
     */
    public LinkedListIterator iterator() {
        return new LinkedListIterator();
    }

    /**
     * Iterator class for PersonLinkedList.
     */
    public class LinkedListIterator implements Iterator<String> {

        private Node current;

        public LinkedListIterator() {
            this.current = head;
        }

        /**
         * Checks if there is a next element in the list.
         *
         * @return True if there is a next element, false otherwise.
         */
        @Override
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next element in the list.
         *
         * @return The next element in the list.
         */
        @Override
        public String next() {
            if (!hasNext()) {
                throw new IllegalStateException("No more elements in the list");
            }
            String value = current.getValue();
            current = current.getNext();
            return value;
        }
    }

    /**
     * Converts the linked list to an array of strings.
     *
     * @return An array of strings representing the values in the list.
     */
    public String[] toArray() {
        String[] array = new String[size];
        Node current = head;
        int index = 0;
        while (current != null) {
            array[index++] = current.getValue();
            current = current.getNext();
        }
        return array;
    }

    /**
     * A Node class to represent each element in the PersonLinkedList.
     */
    private class Node {

        private String value;
        private Node next;

        public Node(String value) {
            this.value = value;
            this.next = null;
        }

        // Getter for the value of the node
        public String getValue() {
            return value;
        }

        // Setter for the value of the node
        public void setValue(String value) {
            this.value = value;
        }

        // Getter for the next node
        public Node getNext() {
            return next;
        }

        // Setter for the next node
        public void setNext(Node next) {
            this.next = next;
        }
    }
}
