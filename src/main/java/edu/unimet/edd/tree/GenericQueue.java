package edu.unimet.edd.tree;


/**
 * A generic queue implemented with a linked list.
 * 
 *
 * @param <T> The type of elements in the queue.
 */
public class GenericQueue<T> {

    private GenericLinkedList<T> list = new GenericLinkedList<>();

    
    /**
     * Adds an item to the end of the queue.
     * 
     * @param item The item to add to the queue.
     */
    public void enqueue(T item) {
        list.add(item);
    }

    /**
     * Removes and returns the item at the front of the queue.
     * 
     * @return The item at the front of the queue.
     * 
     */
    public T dequeue() {
        return list.removeFirst(); // Suponiendo que tienes un método para eliminar el primer elemento
    }

    /**
     * Checks if the queue is empty.
     * 
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return list.getSize() == 0;
    }
}
