package edu.unimet.edd.tree;


public class GenericQueue<T> {

    private GenericLinkedList<T> list = new GenericLinkedList<>();

    public void enqueue(T item) {
        list.add(item);
    }

    public T dequeue() {
        return list.removeFirst(); // Suponiendo que tienes un método para eliminar el primer elemento
    }

    public boolean isEmpty() {
        return list.getSize() == 0;
    }
}
