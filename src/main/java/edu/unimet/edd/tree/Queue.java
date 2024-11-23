package edu.unimet.edd.tree;

/**
 * The Queue class provides a higher-level interface for queue operations. It
 * uses GenericLinkedList to implement the queue behavior for TreeNodes.
 */
public class Queue {

    private GenericLinkedList<TreeNode> linkedList;  // The underlying linked list for the queue

    /**
     * Constructor for Queue. Initializes a new GenericLinkedList for TreeNodes.
     */
    public Queue() {
        this.linkedList = new GenericLinkedList<>(); // Initialize the generic linked list
    }

    /**
     * Enqueue a TreeNode into the queue.
     *
     * @param data The TreeNode to enqueue.
     */
    public void enqueue(TreeNode data) {
        linkedList.add(data); // Add the data to the end of the linked list
    }

    /**
     * Dequeue a TreeNode from the queue.
     *
     * @return The TreeNode that was dequeued, or null if the queue is empty.
     */
    public TreeNode dequeue() {
        return linkedList.remove(); // Remove the first element from the linked list
    }

    /**
     * Check if the queue is empty.
     *
     * @return true if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return linkedList.isEmpty(); // Check if the linked list is empty
    }

    /**
     * Get the size of the queue.
     *
     * @return The number of elements in the queue.
     */
    public int size() {
        int size = 0;
        GenericNode<TreeNode> current = linkedList.getFirst();
        while (current != null) {
            size++;
            current = current.getNext();
        }
        return size;
    }
}
