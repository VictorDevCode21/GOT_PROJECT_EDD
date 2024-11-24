package edu.unimet.edd.tree;

import edu.unimet.edd.utils.Person;

/**
 * The TreeNode class represents a person in the genealogy tree. It contains
 * information about the person's name, father, and children.
 */
public class TreeNode {

    private Person person; // The Person object associated with this node
    private TreeNode father; // The father of the person
    private GenericLinkedList<TreeNode> children; // The list of children for this person

    /**
     * Constructor for TreeNode.
     *
     * @param person The person represented by this node in the genealogy tree.
     * @param father The father of the person.
     */
    public TreeNode(Person person, TreeNode father) {
        this.person = person;
        this.father = father;
        this.children = new GenericLinkedList<>(); // Initialize the list of children
    }

    /**
     * Retrieves the father of the person represented by this node.
     *
     * @return the father of the person
     */
    public TreeNode getFather() {
        return father;
    }
    
    /**
     * Sets the father of the person represented by this node.
     *
     * @param father the father to set
     */
    public void setFather(TreeNode father) {
        this.father = father;
    }
    
    /**
     * Retrieves the list of children of the person represented by this node.
     *
     * @return the list of children
     */
    public GenericLinkedList<TreeNode> getChildren() {
        return children;
    }
    
    /**
     * Adds a child node to the list of children for this person.
     *
     * @param child the child node to add
     */
    public void addChild(TreeNode child) {
        this.getChildren().add(child); // Add the child to the list of children
    }

    /**
     * Retrieves the Person object associated with this node.
     * 
     * @return the person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Sets the Person object for this node.
     * 
     * @param person the person to set
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * Sets the list of children for the person represented by this node.
     * 
     * @param children the children to set
     */
    public void setChildren(GenericLinkedList<TreeNode> children) {
        this.children = children;
    }
}
