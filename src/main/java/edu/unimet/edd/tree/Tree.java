package edu.unimet.edd.tree;

import edu.unimet.edd.utils.LinkedList;
import edu.unimet.edd.utils.Person;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import edu.unimet.edd.hash.HashTable;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;

/**
 * The Tree class represents the genealogy tree, storing people and their
 * relationships. It also provides methods to load the genealogy data from JSON
 * and generate a visual graph.
 */
public class Tree {

    private HashTable table; // A hash table to store the people and their information

    /**
     * Constructs a Tree object.
     */
    public Tree() {
        table = new HashTable();
    }

    /**
     * Retrieves a person from the genealogy tree by their normalized full name.
     * If the person exists in the hash table, it will return the Person object.
     *
     * @param name The name of the person to retrieve.
     * @return The Person object, or null if the person is not found.
     */
    public Person getPerson(String name) {
        String normalizedFullName = normalizeName(name);  // Normalize the name for comparison
        return table.get(normalizedFullName);  // Use the HashTable's get method to retrieve the person
    }

    /**
     * Adds a person and their relationships to the genealogy tree.
     *
     * @param person The Person object to add.
     */
    public void addPerson(Person person) {
        // Generate all possible unique identifiers for this person
        String fullNameKey = normalizeName(getFullName(person));
        String nicknameKey = person.getNickname() != null ? normalizeName(person.getNickname()) : null;

        // Check if this person already exists using any key
        if ((fullNameKey != null && table.get(fullNameKey) != null)
                || (nicknameKey != null && table.get(nicknameKey) != null)) {
            // If the person exists, do not add duplicates
            return;
        }

        // Add the person using all possible keys
        addPersonToHashTable(person, fullNameKey, nicknameKey);

        // Handle father-child relationships
        if (person.getFather() != null) {
            String fatherName = normalizeName(person.getFather());

            // If the father does not exist, create a placeholder node
            if (table.get(fatherName) == null) {
                Person fatherPlaceholder = new Person(person.getFather(), null, null, null, null, null, null, null);
                addPersonToHashTable(fatherPlaceholder, fatherName, null);
            }
        }

        // Add placeholder children if they do not exist
        LinkedList children = person.getChildren();
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                String childName = normalizeName(children.get(i));

                // Check for duplicate children with the same first name and same parent
                boolean duplicateChild = checkDuplicateChild(person.getName(), childName);

                if (!duplicateChild) {
                    // If the child does not exist, create a placeholder node
                    if (table.get(childName) == null) {
                        Person childPlaceholder = new Person(children.get(i), null, null, null, null, null, null, null);
                        addPersonToHashTable(childPlaceholder, childName, null);
                    }
                }
            }
        }
    }

    /**
     * Checks if a child with the same first name and same parent already
     * exists.
     *
     * @param parentName The name of the parent.
     * @param childName The name of the child to check.
     * @return True if a duplicate child exists, otherwise false.
     */
    private boolean checkDuplicateChild(String parentName, String childName) {
        for (Person person : table.getAllPeople()) {
            // Compare first names and parents
            String existingChildFirstName = getFirstName(person.getName());
            String newChildFirstName = getFirstName(childName);

            if (existingChildFirstName.equals(newChildFirstName)
                    && normalizeName(person.getFather()).equals(normalizeName(parentName))) {
                return true; // Duplicate child found
            }
        }
        return false;
    }

    /**
     * Extracts the first name from a full name.
     *
     * @param name The full name.
     * @return The first name.
     */
    private String getFirstName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        String[] parts = name.split(" ");
        return parts[0]; // Return the first part before any space
    }

    /**
     * Adds a person to the hash table using multiple keys.
     *
     * @param person The Person object to add.
     * @param fullNameKey The full name key of the person.
     * @param nicknameKey The nickname key of the person (if available).
     */
    private void addPersonToHashTable(Person person, String fullNameKey, String nicknameKey) {
        if (fullNameKey != null) {
            table.put(fullNameKey, person);
        }
        if (nicknameKey != null) {
            table.put(nicknameKey, person);
        }
    }

    /**
     * Generates the full name of a person, including the "Of his name"
     * attribute if present, and appends "of his name".
     *
     * @param person The Person object whose full name is to be generated.
     * @return The full name of the person.
     */
    private String getFullName(Person person) {
        String name = person.getName();
        if (person.getOfHisName() != null) {
            name += " " + person.getOfHisName() + " of his name"; // Add "Of his name" if available
        }
        return name;
    }

    /**
     * Normalizes a name by standardizing the format for comparison. This method
     * handles names with commas and other special characters.
     *
     * @param name The name to normalize.
     * @return The normalized name.
     */
    private String normalizeName(String name) {
        if (name == null) {
            return null;
        }
        // Remove commas and extra spaces, then convert to lowercase for consistent comparison
        return name.trim().replace(",", "").toLowerCase();
    }

    /**
     * Creates a graph representation of the genealogy tree using GraphStream.
     *
     * @return A Graph object representing the genealogy tree.
     */
    public Graph createGraph() {
        Graph graph = new SingleGraph("GenealogyTree");

        // Set graph attributes
        graph.setAttribute("ui.quality", true);
        graph.setAttribute("ui.antialias", true);

        // Iterate through all people in the hash table
        for (Person person : table.getAllPeople()) {
            // Use unique identifier for nodes (prefer nickname if available)
            String uniqueIdentifier = person.getNickname() != null
                    ? normalizeName(person.getNickname())
                    : normalizeName(getFullName(person));

            // Add the node for the person if not already added
            if (graph.getNode(uniqueIdentifier) == null) {
                graph.addNode(uniqueIdentifier).setAttribute("ui.label", person.getName());
                System.out.println("Added vertex: " + person.getName());
            }

            // Add the father-child relationship
            if (person.getFather() != null) {
                String fatherId = normalizeName(person.getFather());

                // Add the father's node if not already added
                if (graph.getNode(fatherId) == null) {
                    graph.addNode(fatherId).setAttribute("ui.label", person.getFather());
                    System.out.println("Added vertex: " + person.getFather());
                }

                // Add a directed edge from father to child
                String edgeId = fatherId + "-" + uniqueIdentifier;
                if (graph.getEdge(edgeId) == null) {
                    graph.addEdge(edgeId, fatherId, uniqueIdentifier, true);
                }
            }

            // Add the child nodes and edges
            LinkedList children = person.getChildren();
            if (children != null) {
                for (int i = 0; i < children.size(); i++) {
                    String childName = (String) children.get(i); // Casting to String

                    // Normalize child name
                    String childId = normalizeName(childName);

                    // Add the child's node if not already added
                    if (graph.getNode(childId) == null) {
                        graph.addNode(childId).setAttribute("ui.label", childName);
                        System.out.println("Added vertex: " + childName);
                    }

                    // Add a directed edge from person to child
                    String edgeId = uniqueIdentifier + "-" + childId;
                    if (graph.getEdge(edgeId) == null) {
                        graph.addEdge(edgeId, uniqueIdentifier, childId, true);
                    }
                }
            }
        }

        return graph;
    }

    /**
     * Prints the family tree to the console, displaying all ancestors and
     * descendants for a given person in the genealogy.
     *
     * @param person The person whose family tree should be printed.
     */
    public void printFamilyLog(Person person) {
        LinkedList family = new LinkedList();
        collectFamilyTree(person, family);

        // Print the collected family members to the console
        StringBuilder familyLog = new StringBuilder();
        for (int i = 0; i < family.size(); i++) {
            familyLog.append(family.get(i));
            if (i < family.size() - 1) {
                familyLog.append(", ");
            }
        }
        System.out.println("Family of " + person.getName() + ": [" + familyLog.toString() + "]");
    }

    /**
     * Recursively collects the entire family tree (ancestors and descendants)
     * into a linked list.
     *
     * @param person The person to collect the family tree for.
     * @param family The list to store the family members.
     */
    private void collectFamilyTree(Person person, LinkedList family) {
        if (person == null) {
            return;
        }

        // Add the current person to the family list
        family.addString(person.getName());

        // Collect ancestors (father)
        if (person.getFather() != null) {
            Person father = table.get(person.getFather());
            collectFamilyTree(father, family);  // Collect the father's family tree
        }

        // Collect descendants (children)
        LinkedList children = person.getChildren();
        if (children != null && !children.isEmpty()) {
            for (int i = 0; i < children.size(); i++) {
                String childName = children.get(i);
                Person child = table.get(childName);
                collectFamilyTree(child, family);  // Collect the child's family tree
            }
        }
    }
}
