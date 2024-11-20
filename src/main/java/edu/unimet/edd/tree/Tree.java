package edu.unimet.edd.tree;

import edu.unimet.edd.utils.LinkedList;
import edu.unimet.edd.utils.Person;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import edu.unimet.edd.hash.HashTable;
import org.graphstream.graph.Node;

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
        String fullNameKey = normalizeName(person.getName());
        String nicknameKey = person.getNickname() != null ? normalizeName(person.getNickname()) : null;

        // Debugging output to check the keys
//        System.out.println("Adding person with full name key: " + fullNameKey + " and nickname key: " + nicknameKey);
        // Check if this person already exists using any key
        if ((fullNameKey != null && table.get(fullNameKey) != null)
                || (nicknameKey != null && table.get(nicknameKey) != null)) {
            // If the person exists, do not add duplicates
//            System.out.println("Duplicate found, not adding: " + person.getName());
            return;
        }

        // Check if the person has a father
        if (person.getFather() != null) {
            // Get the father's normalized name
            String fatherName = normalizeName(person.getFather());

            // Try to find the father in the HashTable
            Person father = table.get(fatherName);

            // If the father exists, check for duplicate children
            if (father != null) {
                // Remove duplicates and then add the child
                String duplicatedChildName = person.checkDuplicateChild(father, person.getName(), table);

                if (duplicatedChildName != null) {
//                    if (table.get(duplicatedChildName) == null)
//                        System.out.println("No coincidences for the child");
                    Boolean deleted = table.remove(duplicatedChildName);
//                    if (deleted == true)
//                        System.out.println("Child eliminated: " + duplicatedChildName);
                    
                    

                    // Check if the duplicate child was removed
//                    if (table.get(duplicatedChildName) == null) {
//                        System.out.println("Successfully removed duplicated child: " + duplicatedChildName);
//                    } else {
//                        System.out.println("Failed to remove duplicated child: " + duplicatedChildName);
//                    }

                    // Ensure the father does not reference the removed child
                    father.getChildren().remove(duplicatedChildName);
//                    if (father.getChildren().get(duplicatedChildName) != null)
//                        System.out.println("Child's not in the list" + duplicatedChildName);
                }

                // Now add the current person as a child of the father
                if (table.get(duplicatedChildName) == null || !duplicatedChildName.equalsIgnoreCase(person.getName())) {
//                    System.out.println("added: " + person.getName());
                    father.getChildren().addString(person.getName());
                    
                }

            } else {
//                System.out.println("Father " + fatherName + " not found in the HashTable.");
            }
        }

        // Add the person using all possible keys
        addPersonToHashTable(person, fullNameKey, nicknameKey);

        // Debugging output: print the list of people in the HashTable
//        System.out.print("Lista de personas despues del metodo: [");
//        Person[] allPeople = table.getAllPeople();
//        for (int i = 0; i < allPeople.length; i++) {
//            System.out.print(allPeople[i].getName().toLowerCase());
//            if (i < allPeople.length - 1) {
//                System.out.print(", ");
//            }
//        }
//        System.out.println("]");
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
//                System.out.println("Added person to graph: " + person.getName());
            }
        }

        // Print the nicknames of all people in the HashTable (if not null)
//        System.out.println("Nicknames of people in the HashTable:");
        for (Person person : table.getAllPeople()) {
            if (person.getNickname() != null) {
//                System.out.println(" - " + person.getNickname());
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
}
