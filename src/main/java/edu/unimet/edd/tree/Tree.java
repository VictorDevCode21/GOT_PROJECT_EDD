package edu.unimet.edd.tree;

import edu.unimet.edd.utils.PersonLinkedList;
import edu.unimet.edd.utils.Person;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import edu.unimet.edd.hash.HashTable;
import edu.unimet.edd.listeners.HashTableListener;
import javax.swing.JOptionPane;
import org.graphstream.graph.Node;

/**
 * The Tree class represents the genealogy tree, storing people and their
 * relationships. It also provides methods to load the genealogy data from JSON
 * and generate a visual graph.
 */
public class Tree implements HashTableListener {

    private HashTable table; // A hash table to store the people and their information
    private TreeNode root; // Root node of the tree

    /**
     * Constructs a Tree object.
     */
    public Tree() {
        table = HashTable.getInstance();
        table.addListener(this);
        this.root = getRoot(); // Initialize root node by calling getRoot method
    }

    @Override
    public void onHashTableUpdated() {
        // Synchronize the local HashTable instance with the singleton instance
        this.table.syncData(HashTable.getInstance());
    }

    /**
     * Retrieves the root node of the genealogy tree. It searches through the
     * hash table for a person whose father is "unknown" or null.
     *
     * @return The root node of the tree, or null if not found.
     */
    public TreeNode getRoot() {
        // Iterate over all people in the hash table to find the root
        for (Person person : table.getAllPeople()) {
            if (person.getFather() == null || person.getFather().equalsIgnoreCase("unknown")) {
                // Return the root node if a person with no father or an unknown father is found
                return new TreeNode(person.getName(), null);
            }
        }
        return null; // Return null if no root is found
    }

    /**
     * Perform a breadth-first search (BFS) on the genealogy tree, starting from
     * the root. It prints the names of the people in the tree in BFS order.
     */
    public void BFS() {
        GenericLinkedList<TreeNode> queue = new GenericLinkedList<>(); // Create a new queue for BFS
        TreeNode root = getRoot(); // Get the root of the tree

        if (root == null) {
            System.out.println("Tree is empty.");
            return;
        }

        queue.add(root); // Enqueue the root node

        while (!queue.isEmpty()) {
            TreeNode current = queue.remove(); // Dequeue a node
            System.out.println("Visited: " + current.getName()); // Process the node (e.g., print the name)

            // Enqueue all children of the current node
            GenericLinkedList<TreeNode> children = current.getChildren();
            GenericNode<TreeNode> childNode = children.getFirst();
            while (childNode != null) {
                queue.add(childNode.getData()); // Enqueue each child
                childNode = childNode.getNext();
            }
        }
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

            if (father == null) {
                Person[] all = table.getAllPeople();
                for (int i = 0; i < table.getAllPeople().length; i++) {
                    String temp = all[i].getName();

                    if (temp == null) {
                        continue;
                    }

                    String current = getFirstAndLastName(temp);
                    if (current.equalsIgnoreCase(fatherName)) {
                        father = table.get(temp);
                    }
                }

            }

            // If the father exists, check for duplicate children
            if (father != null) {
                // Remove duplicates and then add the child
                String duplicatedChildName = person.checkDuplicateChild(father, person.getName(), table);

                if (duplicatedChildName != null) {
//                    if (table.get(duplicatedChildName) == null)
//                        System.out.println("No coincidences for the child");
                    Boolean deleted = table.remove(duplicatedChildName);
                    if (deleted == true) {
//                        System.out.println("Child eliminated: " + duplicatedChildName + " father: " + father.getName());
                    }

                } else {
//                System.out.println("Father " + fatherName + " not found in the HashTable.");
                }
            }

            // Add the person using all possible keys
            addPersonToHashTable(person, fullNameKey, nicknameKey);
//        System.out.println("Person: " + person.getName() + " Father: " + person.getFather());

//        System.out.println("Person added: " + person.getName());
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
//        Debugging output: print all people in the table along with their children
//        System.out.println("\nCurrent people in the HashTable with their children:\n");
//        Person[] allPeople = table.getAllPeople();
//        for (Person person2 : allPeople) {
//            if (person2 != null) {
//                // Print the person's name
//                System.out.print("Person name: " + person2.getName() + " | Children: ");
//
//                // Print the list of children
//                if (person2.getChildren() != null && person2.getChildren().size() > 0) {
//                    System.out.print("[");
//                    for (int i = 0; i < person2.getChildren().size(); i++) {
//                        System.out.print(person2.getChildren().get(i));
//                        if (i < person2.getChildren().size() - 1) {
//                            System.out.print(", ");
//                        }
//                    }
//                    System.out.println("]");
//                } else {
//                    // No children
//                    System.out.println("No children.");
//                }
//            }
//        }
        }
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
     * Creates a graph representation of the genealogy tree using GraphStream.
     *
     * @param personToLookFor The name of the person to search for.
     * @param foreFathersNeeded If true, loads only the forefathers of the given
     * person.
     * @return A Graph object representing the genealogy tree or forefathers.
     */
    public Graph createGraph(String personToLookfor, boolean foreFathersNeeded, PersonLinkedList titleHolders) {
        Graph graph = new SingleGraph("GenealogyTree");

        // Set graph attributes
        graph.setAttribute("ui.quality", true);
        graph.setAttribute("ui.antialias", true);

        // If no foreFathers needes, we load all the lineage        
        if (foreFathersNeeded) {
            loadForeFathersGraph(personToLookfor, graph);
        } else if (titleHolders != null) {
            loadTitleHoldersGraph(graph, titleHolders);
        } else {
            loadAllLineageGraph(graph);
        }
        return graph;
    }

    /**
     * Loads a graph with nodes representing title holders and ensures that all
     * nodes are interconnected through edges. If a person is not found in the
     * hash table, a warning is printed to the console.
     *
     * @param graph The graph where the nodes and edges will be added.
     * @param titleHolders A PersonLinkedList containing the names of title
     * holders to be added to the graph.
     */
    private void loadTitleHoldersGraph(Graph graph, PersonLinkedList titleHolders) {
        // List to keep track of previously created nodes for edge creation
        PersonLinkedList createdNodes = new PersonLinkedList();

        for (String personName : titleHolders.getAllPersons()) {
            // Retrieve the person from the hash table
            Person newPerson = table.get(personName);

            if (newPerson == null) {
                System.out.println("Person not found in the HashTable: " + personName);
                continue; // Skip if person is not found
            }

            // Check if the node for this person already exists
            if (graph.getNode(personName) == null) {
                // Add the node to the graph with its label
                graph.addNode(personName).setAttribute("ui.label", personName);
            }

            // Create edges between the current node and all previously created nodes
            for (String existingNode : createdNodes.getAllPersons()) {
                String edgeId = existingNode + "-" + personName;

                // Ensure the edge does not already exist before adding
                if (graph.getEdge(edgeId) == null) {
                    graph.addEdge(edgeId, existingNode, personName);
                }
            }

            // Add the current node to the list of created nodes
            createdNodes.addString(personName);
        }
    }

    /**
     * Loads the forefathers of a specific person into the graph.
     *
     * @param personName The name of the person whose forefathers will be
     * loaded.
     * @param graph The Graph object where nodes and edges will be added.
     */
    private void loadForeFathersGraph(String personName, Graph graph) {
        // Step 1: Find the person in the HashTable
        Person currentPerson = table.get(personName);

        if (currentPerson == null) {
            System.out.println("Person not found in the HashTable: " + personName);
            return;
        }

        // Step 2: Traverse the forefathers chain
        while (currentPerson != null) {
            String currentName = normalizeName(currentPerson.getName());

            // Add the current person as a node if it doesn't already exist
            if (graph.getNode(currentName) == null) {
                graph.addNode(currentName).setAttribute("ui.label", currentPerson.getName());
            }

            // Get the father's name and search for their record
            String fatherName = currentPerson.getFather();
            if (fatherName != null && !fatherName.equalsIgnoreCase("[unknown]")) {
                String normalizedFatherName = normalizeName(fatherName);

                // Attempt to get the father from the HashTable
                Person fatherPerson = table.get(normalizedFatherName);

                // If not found, search using getFirstAndLastName and nickname
                if (fatherPerson == null) {
                    for (Person person : table.getAllPeople()) {
                        // Compare by first and last name
                        if (getFirstAndLastName(normalizeName(person.getName()))
                                .equals(getFirstAndLastName(normalizedFatherName))) {
                            fatherPerson = person;
                            break;
                        }
                        // Compare by nickname
                        if (person.getNickname() != null
                                && normalizeName(person.getNickname()).equals(normalizedFatherName)) {
                            fatherPerson = person;
                            break;
                        }
                    }
                }

                // If a father is found, add them to the graph
                if (fatherPerson != null) {
                    String fatherNodeName = normalizeName(fatherPerson.getName());
                    if (graph.getNode(fatherNodeName) == null) {
                        graph.addNode(fatherNodeName).setAttribute("ui.label", fatherPerson.getName());
                    }

                    // Add an edge between the current person and their father
                    String edgeId = fatherNodeName + "-" + currentName;
                    if (graph.getEdge(edgeId) == null) {
                        graph.addEdge(edgeId, fatherNodeName, currentName, true);
                    }

                    // Move to the father for the next iteration
                    currentPerson = fatherPerson;
                } else {
                    // If no father record is found, terminate the loop
                    currentPerson = null;
                }
            } else {
                // If father name is unknown, terminate the loop
                currentPerson = null;
            }
        }
    }

    /**
     * Populates the graph with all individuals and their parental relationships
     * from the HashTable. This method adds nodes for all people and creates
     * edges to represent father-child relationships.
     *
     * <p>
     * The process is divided into two main steps:</p>
     * <ol>
     * <li>Add all individuals as nodes in the graph.</li>
     * <li>Add edges to connect children with their respective fathers, ensuring
     * the relationships are correctly established.</li>
     * </ol>
     *
     * <p>
     * For each father-child relationship:</p>
     * <ul>
     * <li>If the father's node does not already exist, the method searches for
     * it using either the father's nickname or their first and last names
     * within the HashTable.</li>
     * <li>An edge is added only if both the father and child nodes exist in the
     * graph.</li>
     * </ul>
     *
     * @param graph The Graph object where nodes and edges will be added to
     * represent the lineage of all individuals.
     */
    private void loadAllLineageGraph(Graph graph) {
        // Step 1: Add all people as nodes
        {
            for (Person person : table.getAllPeople()) {
                String personName = normalizeName(person.getName());

                // Add the node if it does not exist
                if (graph.getNode(personName) == null) {
                    graph.addNode(personName).setAttribute("ui.label", person.getName());
//                System.out.println("Added person to graph: " + person.getName());
                }
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
     * @return A PersonLinkedList containing all Person objects in the tree.
     */
    public PersonLinkedList getAllPersons() {
        PersonLinkedList personList = new PersonLinkedList(); // Create a new PersonLinkedList to store the persons.
        Person[] peopleArray = table.getAllPeople(); // Get the array of Person objects from the HashTable.

        // Convert the array into a PersonLinkedList
        for (Person person : peopleArray) {
            personList.addPerson(person); // Add each person to the PersonLinkedList.
        }

        return personList; // Return the PersonLinkedList containing all persons.
    }
}
