package edu.unimet.edd.tree;
import edu.unimet.edd.utils.LinkedList;
import edu.unimet.edd.utils.Person;

/**
 *
 * @author PC
 */
/**
 * This class represents a general tree that holds genealogical data. It uses a
 * custom implementation of LinkedList and Node to store and manage the data.
 */
public class Tree {

    private LinkedList peopleList;

    /**
     * Constructor to initialize the tree with an empty people list.
     */
    public Tree() {
        this.peopleList = new LinkedList();
    }

    /**
     * Adds a person to the tree.
     *
     * @param person The person to be added to the tree.
     */
    public void addPerson(Person person) {
        peopleList.add(person);
    }

    /**
     * Retrieves the LinkedList of all people in the tree.
     *
     * @return The list of people.
     */
    public LinkedList getPeopleList() {
        return peopleList;
    }

    /**
     * Prints the names of all people in the tree.
     */
    public void printTree() {
        peopleList.print();
    }

    /**
     * Finds a person by name in the tree.
     *
     * @param name The name of the person to be searched for.
     * @return The Person object if found, null otherwise.
     */
    public Person findPersonByName(String name) {
        for (int i = 0; i < peopleList.getSize(); i++) {
            Person person = (Person) peopleList.get(i);
            if (person.getName().equalsIgnoreCase(name)) {
                return person;
            }
        }
        return null;
    }
}