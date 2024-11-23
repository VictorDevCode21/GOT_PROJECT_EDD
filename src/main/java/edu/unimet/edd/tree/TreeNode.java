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
     * @param name The name of the person.
     * @param father The father of the person.
     */
    public TreeNode(Person person, TreeNode father) {
        this.person = person;
        this.father = father;
        this.children = new GenericLinkedList<>(); // Initialize the list of children
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
        this.getChildren().add(child); // Add the child to the list of children
    }

    /**
     * @return the person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * @param person the person to set
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(GenericLinkedList<TreeNode> children) {
        this.children = children;
    }
}
