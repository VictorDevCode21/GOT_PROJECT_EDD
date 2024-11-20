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

//    /**
//     * Adds a person and their relationships to the genealogy tree.
//     *
//     * @param person The Person object to add.
//     */
    public void addPerson(Person person) {
        // Generate all possible unique identifiers for this person
        String fullNameKey = normalizeName(getFullName(person));
        String nicknameKey = person.getNickname() != null ? normalizeName(person.getNickname()) : null;

        // Debugging output to check the keys
        System.out.println("Adding person with full name key: " + fullNameKey + " and nickname key: " + nicknameKey);

        // Check if this person already exists using any key
        if ((fullNameKey != null && table.get(fullNameKey) != null)
                || (nicknameKey != null && table.get(nicknameKey) != null)) {
            // If the person exists, do not add duplicates
            System.out.println("Duplicate found, not adding: " + person.getName());
            return;
        }

        // Add the person using all possible keys
        addPersonToHashTable(person, fullNameKey, nicknameKey);
        // Debugging output to show successful addition
        System.out.println("Successfully added person: " + person.getName());

        // Print a list of all people in the HashTable after the addition
        System.out.println("Current list of people in the HashTable:");
        for (Person storedPerson : table.getAllPeople()) {
            System.out.println("- Name: " + storedPerson.getName());
        }
        System.out.println("====================================");

        //          Print all people in the HashTable
//        System.out.println("Current contents of the HashTable:");
//        System.out.println("Current contents of the HashTable:");
//        Person[] people = table.getAllPeople(); // Use getAllPeople to retrieve stored persons
//        for (Person p : people) {
//            String father = (p.getFather() != null) ? p.getFather() : "No father registered";
//            System.out.println("- Name: " + p.getName() + ", Father: " + father);
//
//            // Print the children of this person
//            LinkedList children2 = p.getChildren();
//            if (children2 != null && children2.size() > 0) {
//                System.out.println("  Children:");
//                for (int i = 0; i < children2.size(); i++) {
//                    System.out.println("    - " + children2.get(i));
//                }
//            } else {
//                System.out.println("  No children registered.");
//            }
//        }   
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

        // Step 1: Add all people as nodes
        for (Person person : table.getAllPeople()) {
            String personName = normalizeName(person.getName());

            // Add the node if it does not exist
            if (graph.getNode(personName) == null) {
                graph.addNode(personName).setAttribute("ui.label", person.getName());
                System.out.println("Added person to graph: " + person.getName());
            }
        }

        // Print the nicknames of all people in the HashTable (if not null)
        System.out.println("Nicknames of people in the HashTable:");
        for (Person person : table.getAllPeople()) {
            if (person.getNickname() != null) {
                System.out.println(" - " + person.getNickname());
            }
        }

        // Step 2: Add edges to connect children with their parents
        for (Person person : table.getAllPeople()) {
            String childName = normalizeName(person.getName());

            // Add father-child relationship
            if (person.getFather() != null && !person.getFather().equalsIgnoreCase("[unknown]")) {
                String fatherName = normalizeName(person.getFather());

                // Check if father exists in the graph by name or nickname
                Node fatherNode = graph.getNode(fatherName);
                if (fatherNode == null && person.getFather() != null) {
                    for (Person possibleFather : table.getAllPeople()) {
                        if (possibleFather.getNickname() != null
                                && normalizeName(possibleFather.getNickname()).equals(fatherName)) {
                            fatherNode = graph.getNode(normalizeName(possibleFather.getName()));
                            break;
                        }
                    }
                }

                // Print all nodes currently in the graph
//                System.out.println("Current nodes in the graph:");
                for (Node node : graph) {
//                    System.out.println(" - " + node.getId());
                }

                // If still not found, try to match by first and last name
                if (fatherNode == null) {
                    for (Node node : graph) {
                        if (getFirstAndLastName(node.getId()).equals(getFirstAndLastName(fatherName))) {
                            fatherNode = node;
                            break;
                        }
                    }
                }

                // Check if both nodes exist in the graph
                if (fatherNode != null && graph.getNode(childName) != null) {
                    String edgeId = fatherNode.getId() + "-" + childName;
                    if (graph.getEdge(edgeId) == null) {
                        graph.addEdge(edgeId, fatherNode.getId(), childName, true);
//                        System.out.println("Added edge: " + person.getFather() + " -> " + person.getName());
                    }
                } else {
                    System.out.println("Skipped adding edge for father: " + person.getFather()
                            + " for person " + person.getName()
                            + " (Father node: " + (fatherNode != null ? "Exists" : "Does not exist")
                            + ", Child node: " + (graph.getNode(childName) != null ? "Exists" : "Does not exist") + ")");
                }
            }
        }

        return graph;
    }

    /**
     * Extracts the first and last name from a full name.
     *
     * @param fullName The full name string.
     * @return A string containing only the first and last name.
     */
    private String getFirstAndLastName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return "";
        }
        String[] parts = fullName.split(" ");
        if (parts.length < 2) {
            return fullName; // Return as is if less than two words
        }
        return parts[0] + " " + parts[1]; // Return the first and second word
    }

    /**
     * Checks if a child is already in the list of a parent's children using
     * their first name. Logs the current list of children for a parent and
     * whether a duplicate was found.
     *
     * @param parent The parent Person object.
     * @param childName The name of the child being added.
     * @return True if the child is considered a duplicate, false otherwise.
     */
    private boolean checkDuplicateChild(Person parent, String childName) {
        // Log the child being added
//        System.out.println("Attempting to add child: " + childName);

        if (parent != null) {
            // Get the list of children for the parent
            LinkedList currentChildren = parent.getChildren();

            // Log the current list of children
//            System.out.println("Current list of children for " + parent.getName() + ":");
            if (currentChildren != null && currentChildren.size() > 0) {
                String newChildFirstName = getFirstName(childName);
                for (int i = 0; i < currentChildren.size(); i++) {
                    String existingChildFirstName = getFirstName(currentChildren.get(i));

                    // Log each child's first name
//                    System.out.println("- " + currentChildren.get(i) + " (First Name: " + existingChildFirstName + ")");
                    // Check for a duplicate based on first name
                    if (newChildFirstName.equalsIgnoreCase(existingChildFirstName)) {
//                        System.out.println("Duplicate child detected: " + childName);
                        return true; // Duplicate found
                    }
                }
            } else {
//                System.out.println("- No children registered yet.");
            }
        } else {
//            System.out.println("Parent object is null.");
        }

        // No duplicate found
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
     * Retrieves all persons stored in the genealogy tree.
     *
     * @return A LinkedList containing all Person objects in the tree.
     */
    public LinkedList getAllPersons() {
        LinkedList personList = new LinkedList(); // Create a new LinkedList to store the persons.
        Person[] peopleArray = table.getAllPeople(); // Get the array of Person objects from the HashTable.

        // Convert the array into a LinkedList
        for (Person person : peopleArray) {
            personList.addPerson(person); // Add each person to the LinkedList.
        }

        return personList; // Return the LinkedList containing all persons.
    }
    
/**
 * Searches for a person in the genealogy tree based on the provided name.
 *
 * @param NameSearched The name of the person to search for.
 *                     
 * @return The Person object corresponding to the provided name,
 *         or  null if the person is not found in the table.
 */
public Person SearchTest(String NameSearched) {
    // Normalize the input name to ensure consistent comparison.
    String normalizedFullName = normalizeName(NameSearched);

    // Retrieve the person object from the hash table using the normalized name.
    return table.get(normalizedFullName);
}
    
    
    
}
