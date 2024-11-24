package edu.unimet.edd.tree;

import edu.unimet.edd.utils.PersonLinkedList;
import edu.unimet.edd.utils.Person;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import edu.unimet.edd.hash.HashTable;
import edu.unimet.edd.listeners.HashTableListener;
import java.util.function.Consumer;
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
        for (Person person : table.getAllPeople()) {
            if (person.getFather() == null || person.getFather().equalsIgnoreCase("[unknown]")) {
                // Create the root node with the Person object
                TreeNode rootNode = new TreeNode(person, null);
                addChildren(rootNode); // Add children to this root node
                return rootNode;
            }
        }
        return null; // Return null if no root is found
    }

    /**
     * Adds children to the given parent node based on the HashTable data. This
     * method maintains all logic checks for father matching, including: -
     * Normalized names - Nicknames - First and last names It avoids recursion
     * for better performance.
     *
     * @param parent The parent TreeNode to which children will be added.
     */
    private void addChildren(TreeNode parent) {
        // Queue to manage nodes to process
        GenericLinkedList<TreeNode> queue = new GenericLinkedList<>();
        queue.add(parent);

        // Process each node in the queue
        while (!queue.isEmpty()) {
            TreeNode currentNode = queue.remove();
            String currentName = normalizeName(currentNode.getPerson().getName());

            // Iterate through all people to find matching children
            for (Person person : table.getAllPeople()) {
                if (person.getFather() != null) {
                    // Check if the person's father matches the current node's name
                    String fatherName = normalizeName(person.getFather());
                    if (fatherName.equals(currentName)
                            || person.getFather().equalsIgnoreCase(currentNode.getPerson().getNickname())
                            || getFirstAndLastName(person.getFather()).equalsIgnoreCase(getFirstAndLastName(currentNode.getPerson().getName()))) {

                        // Create a new TreeNode for the child
                        TreeNode childNode = new TreeNode(person, currentNode);
                        currentNode.addChild(childNode); // Add the child to the parent node

                        // Add the child to the queue for further processing
                        queue.add(childNode);
                    }
                }
            }
        }
    }

    /**
     * Perform a breadth-first search (BFS) on the genealogy tree and process
     * each node using the provided callback. This method calculates generations
     * efficiently and updates the HashTable with generation data.
     *
     * @param processNode A callback function to process each TreeNode during
     * BFS.
     * @return The number of generations in the tree, or -1 if the tree is
     * empty.
     */
    public Integer BFS(Consumer<TreeNode> processNode) {
        GenericLinkedList<TreeNode> queue = new GenericLinkedList<>();
        TreeNode root = getRoot();

        if (root == null) {
            return -1;
        }

        queue.add(root);
        int currentGeneration = 1;

        while (!queue.isEmpty()) {
            int levelSize = queue.getSize();

            // Process all nodes at the current level
            for (int i = 0; i < levelSize; i++) {
                TreeNode current = queue.remove();
                processNode.accept(current);

                // Update generation information only once per node
                Person person = current.getPerson();
                person.setGeneration(currentGeneration);
                table.put(person.getName(), person);

                // Enqueue all children of the current node
                GenericNode<TreeNode> childNode = current.getChildren().getFirst();
                while (childNode != null) {
                    queue.add(childNode.getData());
                    childNode = childNode.getNext();
                }
            }

            currentGeneration++;
        }
        return currentGeneration;
    }

    /**
     * Counts the total number of TreeNode objects in the genealogy tree. This
     * method performs a breadth-first search (BFS) to traverse all nodes and
     * counts them.
     *
     * @return The total number of TreeNode objects in the tree.
     */
    public int countTreeNodes() {
        // Initialize the counter to zero
        int count = 0;

        // Perform BFS to traverse the tree starting from the root
        GenericLinkedList<TreeNode> queue = new GenericLinkedList<>();
        TreeNode root = getRoot(); // Get the root node

        // Check if the tree is empty
        if (root == null) {
            return count; // Return 0 if the tree is empty
        }

        // Add the root node to the queue
        queue.add(root);

        // Perform a breadth-first search
        while (!queue.isEmpty()) {
            TreeNode current = queue.remove(); // Dequeue the current node
            count++; // Increment the counter for each node visited

            // Enqueue all children of the current node
            GenericLinkedList<TreeNode> children = current.getChildren();
            GenericNode<TreeNode> childNode = children.getFirst();
            while (childNode != null) {
                queue.add(childNode.getData()); // Add child node to the queue
                childNode = childNode.getNext();
            }
        }

        // Return the total count of nodes
        return count;
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
                    Boolean deleted = table.remove(duplicatedChildName);
                    if (deleted == true) {
                    }

                } else {
                }
            }

            // Add the person using all possible keys
            addPersonToHashTable(person, fullNameKey, nicknameKey);

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
    public Graph createGraph(String personToLookfor, boolean foreFathersNeeded, PersonLinkedList titleHolders, Integer generationNumber) {
        Graph graph = new SingleGraph("GenealogyTree");

        // Set graph attributes
        graph.setAttribute("ui.quality", true);
        graph.setAttribute("ui.antialias", true);

        // If no foreFathers needes, we load all the lineage        
        if (foreFathersNeeded) {
            loadForeFathersGraph(personToLookfor, graph);
        } else if (titleHolders != null) {
            loadTitleHoldersGraph(graph, titleHolders);
        } else if (generationNumber != null) {
            loadGenerationMembersGraph(graph, generationNumber);
        } else {
            loadAllLineageGraph(graph);
        }
        return graph;
    }

    /**
     * Loads a subgraph for a specific generation into the given graph. This
     * method creates nodes for all members of the specified generation and
     * connects these nodes with directed edges. If a node or edge already
     * exists, it is skipped.
     *
     * <p>
     * Steps:</p>
     * <ol>
     * <li>Iterates over all persons in the HashTable.</li>
     * <li>For each person in the specified generation:
     * <ul>
     * <li>Adds a node to the graph if it does not already exist.</li>
     * <li>Connects the new node to previously created nodes in the same
     * generation using directed edges.</li>
     * </ul>
     * </li>
     * </ol>
     *
     * <p>
     * Edge cases:</p>
     * <ul>
     * <li>If the generation number is invalid (<= 0), the method exits without
     * modifying the graph.</li> <li>Duplicate nodes and edges are avoided by c
     * hecking their existence in the graph.</li>
     * </ul>
     *
     * @param graph The Graph object where nodes and edges will be added.
     * @param generationNumber The generation number to process. Only persons
     * with this generation will be included in the graph.
     */
    private void loadGenerationMembersGraph(Graph graph, Integer generationNumber) {
        // List to keep track of previously created nodes for edge creation
        PersonLinkedList createdNodes = new PersonLinkedList();

        if (generationNumber > 0) {
            for (Person person : table.getAllPeople()) {
                if (person.getGeneration().equals(generationNumber)) {
                    String personName = person.getName();

                    // Add node if it doesn't already exist
                    if (graph.getNode(personName) == null) {
                        graph.addNode(personName).setAttribute("ui.label", personName);
                    }

                    // Create edges between the current node and all previously created nodes
                    for (String existingNode : createdNodes.getAllPersons()) {
                        String edgeId = existingNode + "-" + personName;

                        // Check if the edge already exists
                        if (graph.getEdge(edgeId) == null) {
                            // Add the edge with a unique identifier
                            graph.addEdge(edgeId, existingNode, personName, true); // true for directed edge
                        }
                    }

                    // Add the current node to the list of created nodes
                    createdNodes.addString(personName);
                }
            }
        }
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
                }
            }
        }

        // Print the nicknames of all people in the HashTable (if not null)
        for (Person person : table.getAllPeople()) {
            if (person.getNickname() != null) {
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
                for (Node node : graph) {
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
                    }
                } else {

                }
            }
        }
    }

    /**
     * This method iterates over all the people in the hash table and checks if
     * they have children. If the person has children, it searches for each
     * child's name in the hash table and sets the parent for the child if the
     * child is found. The method also logs information about the assignments
     * made.
     */
    public void setFatherForUndetailedChildren() {
        // Iterate through all people in the hash table.
        for (Person person : table.getAllPeople()) {

            // Get the list of children for the current person (PersonLinkedList).
            PersonLinkedList children = person.getChildren();

            // If the person has children, process them.
            if (children != null) {

                // Use the linked list iterator to go through each child in the list.
                PersonLinkedList.LinkedListIterator iterator = children.iterator();
                while (iterator.hasNext()) {
                    // Get the name of the current child.
                    String childName = iterator.next();

                    // Search for the child in the hash table using the child's name.
                    Person childFromTable = table.get(childName.toLowerCase());

                    // If the child is found in the table, set the parent for this child.
                    if (childFromTable != null) {
                        childFromTable.setFather(person.getName()); // Set the current person as the child's father.
                        table.put(childName, person);

                    } else {
                        // If the child is not found in the table, log a message.
                    }
                }
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
