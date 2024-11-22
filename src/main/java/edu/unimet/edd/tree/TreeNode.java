package edu.unimet.edd.tree;

/**
 * The TreeNode class represents a person in the genealogy tree. It contains
 * information about the person's name, father, and children.
 */
public class TreeNode {

    private String name; // The name of the person
    private TreeNode father; // The father of the person
    private GenericLinkedList<TreeNode> children; // The list of children for this person

    /**
     * Constructor for TreeNode.
     *
     * @param name The name of the person.
     * @param father The father of the person.
     */
    public TreeNode(String name, TreeNode father) {
        this.name = name;
        this.father = father;
        this.children = new GenericLinkedList<>(); // Initialize the list of children
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TreeNode getFather() {
        return father;
    }

    public void setFather(TreeNode father) {
        this.father = father;
    }

    public GenericLinkedList<TreeNode> getChildren() {
        return children;
    }

    public void addChild(TreeNode child) {
        this.children.add(child); // Add the child to the list of children
    }
}
