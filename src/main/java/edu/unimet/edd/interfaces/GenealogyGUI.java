package edu.unimet.edd.interfaces;

import edu.unimet.edd.hash.HashTable;
import edu.unimet.edd.listeners.HashTableListener;
import edu.unimet.edd.tree.GenericLinkedList;
import edu.unimet.edd.tree.GenericNode;
import edu.unimet.edd.tree.Tree;
import edu.unimet.edd.tree.TreeNode;
import edu.unimet.edd.utils.LoadJson;
import edu.unimet.edd.utils.Person;
import edu.unimet.edd.utils.PersonLinkedList;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swing_viewer.SwingViewer;  // Use SwingViewer instead of Viewer
import org.graphstream.ui.view.Viewer;
import org.graphstream.algorithm.Toolkit;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.math3.util.Pair;
import org.graphstream.graph.Node;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swing_viewer.DefaultView;

/**
 * GenealogyGUI is the main graphical interface for visualizing and interacting
 * with a genealogy tree. It uses Swing for the user interface and GraphStream
 * for rendering genealogical graphs.
 * <p>
 * This class includes functionalities for:
 * <ul>
 * <li>Loading genealogy data from a JSON file.</li>
 * <li>Displaying relationships and forefathers in a graph format.</li>
 * <li>Highlighting title holders and interacting with nodes via clicks.</li>
 * </ul>
 * Implements the {@link HashTableListener} interface to react to changes in the
 * genealogy data stored in the hash table.
 * </p>
 *
 * @author [PC]
 * @version 1.0
 */
public class GenealogyGUI extends JFrame implements HashTableListener {

    /**
     * The genealogy tree structure.
     */
    private Tree tree;

    /**
     * The panel used to display the graph visualization.
     */
    private JPanel graphPanel;

    /**
     * The GraphStream viewer for rendering the graph.
     */
    private Viewer viewer;

    /**
     * The hash table containing the genealogy data.
     */
    private HashTable table;

    /**
     * Flag indicating if a JSON file has been successfully loaded.
     */
    private boolean jsonLoaded = false;

    /**
     * The graph representation of the genealogy tree.
     */
    private Graph graph;

    /**
     * Flag indicating if forefathers are to be displayed in the graph.
     */
    private boolean foreFathersNeeded = false;

    /**
     * Constructs the GenealogyGUI interface. Initializes the components, sets
     * up the layout, and configures event listeners.
     */
    public GenealogyGUI() {
        // Initialize the tree and other components
        this.tree = new Tree();
        setTitle("Genealogy Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        table = HashTable.getInstance();
        table.addListener(this);

        // Set up the layout
        setLayout(new BorderLayout());

        // Initialize the graph panel
        graphPanel = new JPanel(new BorderLayout());
        add(graphPanel, BorderLayout.CENTER);

        // Set up the UI components (buttons, etc.)
        JPanel controlsPanel = new JPanel();
        JButton loadButton = new JButton("Load Tree");
        loadButton.addActionListener(e -> loadTree());  // Action listener for the button
        controlsPanel.add(loadButton);

        add(controlsPanel, BorderLayout.SOUTH);

        // Add a "See Register" button
        JButton seeRegisterButton = new JButton("See Register");
        seeRegisterButton.addActionListener(e -> {
            if (jsonLoaded) {
                updateGraphDisplay(null, false, null, null);
            } else {
                JOptionPane.showMessageDialog(rootPane, "You need to Load a JSON file first");
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Click a node in the graph to view its details.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        controlsPanel.add(seeRegisterButton);

        JButton showForefathersButton = new JButton("Show Forefathers");
        showForefathersButton.addActionListener(e -> {
            if (jsonLoaded) {
                showForefathers();
            } else {
                JOptionPane.showMessageDialog(rootPane, "You need to Load a JSON file first");
            }
        });
        controlsPanel.add(showForefathersButton);

        // Button to execute event for showing title holders
        JButton showTitleHoldersButton = new JButton("Show title holders");
        showTitleHoldersButton.addActionListener(e -> {
            if (jsonLoaded) {
                showTitleHolders();
            } else {
                JOptionPane.showMessageDialog(rootPane, "You need to Load a JSON file first");
            }
        });
        controlsPanel.add(showTitleHoldersButton);

        // Button to execute event for showing title holders
        JButton showGenerationButton = new JButton("Show Generation");
        showGenerationButton.addActionListener(e -> {
            if (jsonLoaded) {
                showGenerationMembers();
            } else {
                JOptionPane.showMessageDialog(rootPane, "You need to Load a JSON file first");
            }
        });
        controlsPanel.add(showGenerationButton);

    }

    /**
     * Triggered when the hash table is updated. Synchronizes the local hash
     * table instance with the singleton instance.
     */
    @Override
    public void onHashTableUpdated() {
        // Synchronize the local HashTable instance with the singleton instance
        this.table.syncData(HashTable.getInstance());
    }

    /**
     * Loads a genealogy tree from a JSON file selected by the user and
     * visualizes it.
     */
    private void loadTree() {
        // Open a file chooser dialog to select the JSON file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Genealogy JSON File");
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            // Get the selected file
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Read the content of the selected file into a String
                String jsonContent = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));

                // Load the genealogy data into the tree
                LoadJson loadJson = new LoadJson();
                loadJson.loadGenealogy(jsonContent, tree);
                jsonLoaded = true;
                JOptionPane.showMessageDialog(rootPane, "JSON file correctly loaded");

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to load tree file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Updates the graph display with the current genealogy tree data.
     * <p>
     * This method initializes a new {@link GraphStream} viewer and replaces the
     * graph panel content with the updated graph.
     * </p>
     *
     * @param personToLookFor The name of the person to highlight, or null for
     * the entire tree.
     * @param foreFathersNeeded Flag to display only forefathers.
     * @param titleName List of title holders to display, or null for none.
     */
    private void updateGraphDisplay(String personToLookFor, boolean foreFathersNeeded, PersonLinkedList titleName, Integer generationNumber) {
        try {
            // Create the graph based on the current tree data
            Graph graph = tree.createGraph(personToLookFor, foreFathersNeeded, titleName, generationNumber);

//        System.out.println("Table size in GenealogyGUI: " + table.size());
            // If a viewer already exists, close its previous view to avoid conflicts
            if (viewer != null) {
                try {
                    viewer.close();  // Ensure previous viewer is closed properly
                } catch (Exception e) {
//                System.out.println("Error closing the viewer: " + e.getMessage());
                }
            }

            // Create a new viewer for the graph using SwingViewer
            viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
            viewer.enableAutoLayout();

            // Create a ProxyPipe to retrieve updates from the viewer
            ProxyPipe pipe = viewer.newViewerPipe();
            pipe.addAttributeSink(graph);

            // Start consuming events from the pipe and updating the graph
            new Thread(() -> {
                try {
                    while (true) {
                        Thread.sleep(100); // Avoid full CPU usage
                        pipe.pump();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            // Add a click listener to the graph nodes
            Component view = (Component) viewer.addDefaultView(false);
            if (view != null) {
                view.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        handleNodeClick(graph, e);
                    }
                });
            } else {
                System.out.println("Error: view is null, unable to add mouse listener.");
            }

            // Remove the old graph view and add the new one
            graphPanel.removeAll();
            graphPanel.add(view, BorderLayout.CENTER);
            graphPanel.revalidate();
            graphPanel.repaint();
//        } catch (NullPointerException e) {
//            System.out.println("Null Pointer Error: " + e.getMessage());
//            JOptionPane.showMessageDialog(this, "Unable to retrieve graph data. Please ensure the data is correctly initialized.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.out.println("Unexpected Error: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Assigns a generation number to each person in the genealogy tree based on
     * their level in the tree and updates the HashTable with the changes.
     */
    private void showGenerationMembers() {
        if (!jsonLoaded) {
            JOptionPane.showMessageDialog(this, "You need to load a JSON file first.");
            return;
        }

        // Retrieve the root of the tree
        TreeNode root = tree.getRoot();

        if (root == null) {
            JOptionPane.showMessageDialog(this, "The tree is empty.");
            return;
        }

        tree.setFatherForUndetailedChildren();

        // Use the BFS method from the Tree class
        Integer totalGenerations = tree.BFS(node -> {

        });
        
        // Due to the iteration of bfs, less 1 to the max value of generations
        totalGenerations = totalGenerations -1;
        

        // Show the total number of generations (equal to the depth of the tree)
        JOptionPane.showMessageDialog(this,
                "Generations have been assigned based on the " + totalGenerations + " levels.",
                "Generations Info", JOptionPane.INFORMATION_MESSAGE);
        

        // Prompt the user to select a generation
        Integer selectedGeneration = -1;
        int userInput = -1; 
        boolean validInput = false;
        
        while (!validInput) {
        try {
            String input = JOptionPane.showInputDialog(
                    this, "Select a generation number that must be less or equal to: " + (totalGenerations),
                    "Select Generation", JOptionPane.QUESTION_MESSAGE
            );
            
            
            if (input != null) {
                selectedGeneration = Integer.parseInt(input);
            }
            
            if (input == null) {
                JOptionPane.showMessageDialog(this, "Operation cancelled.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
   
            userInput = Integer.parseInt(input);
            //if (input > totalGenerations){
            
            // Check if the input is numeric and within the valid range
            if (input.matches("\\d+")) {
                userInput = Integer.parseInt(input);

                // Validate if the number is within the valid range
                if (userInput > 0 && userInput <= totalGenerations) {
                    validInput = true; // Exit loop if valid input
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid! Please enter a number between 1 and " + totalGenerations + ".",
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid generation number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid generation number.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

//        if (selectedGeneration != -1) {
//            updateGraphDisplay(null, false, null, selectedGeneration);
//        } else {
//            return;
        }
         // At this point, validInput is true and we can proceed
        updateGraphDisplay(null, false, null, userInput); // Assuming the method accepts generation number
    }

    /**
     * Displays the persons holding a specific title as nodes in the genealogy
     * graph.
     * <p>
     * This method prompts the user to enter the name of a title, and then
     * searches through the genealogy data to find all persons who hold that
     * title. The matching persons are added to a list, and a graph is displayed
     * with these persons as nodes. If the title is not found or if the input is
     * invalid, an error message is shown.
     * <p>
     * Once the graph is displayed, the user is informed that they can click on
     * any node to view more details about the selected person.
     *
     * @throws NullPointerException if the person data in the HashTable is not
     * properly initialized.
     */
    private void showTitleHolders() {
        try {
            PersonLinkedList titleHolders = new PersonLinkedList();
            String titleName = JOptionPane.showInputDialog(
                    this,
                    "Enter the name of the title that you are looking for",
                    "Get persons by title",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (titleName == null || titleName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Person person : table.getAllPeople()) {
                if (person.getTitle() == null) {
                    continue;
                }

                if (person.getTitle().equalsIgnoreCase(titleName.trim())) {
                    titleHolders.addPerson(person);
                    System.out.println("Person with title: " + person.getName() + " " + person.getTitle() + " added succesfully");
                } else {
//                System.out.println("Person: " + person.getName() + " Person Title: " + person.getTitle() + " Introduced Title: " + titleName);
                }
            }

            try {
                updateGraphDisplay(null, false, titleHolders, null);
            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }

            JOptionPane.showMessageDialog(this,
                    "Click a node in the graph to view its details.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (NullPointerException e) {
            System.out.println("NullPointerException: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Person data not properly initialized.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.out.println("Unexpected error raro: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "An unexpected error occurred, Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays the forefathers (ancestors) of a specific person in the
     * genealogy graph.
     * <p>
     * This method prompts the user to enter the name of a person along with the
     * number indicating their position in the lineage (e.g., "robert baratheon
     * first of his name"). The program then searches for the person in the
     * HashTable and if found, it displays the person's forefathers (ancestors)
     * in the genealogy graph. If the person is not found or the input is
     * invalid, an error message is shown.
     * <p>
     * Once the graph is updated, the user is informed that they can click on
     * any node to view details about the forefathers.
     *
     * @throws NullPointerException if the person data in the HashTable is not
     * properly initialized.
     */
    private void showForefathers() {
        try {
            // Prompt the user for a name
            String personName = JOptionPane.showInputDialog(
                    this,
                    "Enter the name of the person followed by the number "
                    + "of that\n person in your lineage, "
                    + "example: robert baratheon first of his name:",
                    "Show Forefathers",
                    JOptionPane.QUESTION_MESSAGE
            );

//            if (personName == null || personName.trim().isEmpty()) {
//                JOptionPane.showMessageDialog(this, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }

            // Handle the case where the user closed the input dialog or clicked "Cancel"
            if (personName == null) {
                JOptionPane.showMessageDialog(this, "Operation cancelled. No name entered.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Check if the name is empty or contains only spaces
            if (personName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return; 
            }
            
            // Check if the name contains only valid characters (alphabet and spaces)
            if (!personName.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(this, "Invalid name format. Only alphabets and spaces are allowed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
                
            }

            // Search for the person in the HashTable
            Person person = table.get(personName.trim());
            if (person == null) {
                JOptionPane.showMessageDialog(this, "Person not found in the genealogy tree.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            foreFathersNeeded = true;

            // Display the graph using the existing method
            updateGraphDisplay(personName.trim(), foreFathersNeeded, null, null);

            JOptionPane.showMessageDialog(this,
                    "Click a node in the graph to view its details.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (NullPointerException e) {
            System.out.println("NullPointerException: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Person data not found. Please ensure the person exists in the genealogy.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles the logic when a node in the genealogy graph is clicked.
     * <p>
     * This method is triggered when a user clicks on a node in the graph. It
     * calculates the distance between the mouse click position and the position
     * of the nodes in the graph. If the distance is within a predefined
     * threshold, the corresponding node is selected and highlighted. The
     * details of the person associated with the node are displayed in a dialog
     * box.
     * <p>
     * If no node is found within the threshold, a message is logged indicating
     * that no node was selected.
     *
     * @param graph The graph that contains the nodes.
     * @param e The mouse event containing the click coordinates.
     * @throws NullPointerException if the graph or the node positions are not
     * properly initialized.
     */
    private void handleNodeClick(Graph graph, java.awt.event.MouseEvent e) {
        double threshold = 10.0; // Define the threshold distance for node selection
        boolean nodeFound = false;

        // Iterate over all nodes in the graph
        for (Node node : graph) {
            // Retrieve node coordinates in graph units (GU)
            // Retrieve node coordinates in graph units (GU)
            Object xyzObject = node.getAttribute("xyz");
            if (xyzObject == null) {
//                            System.out.println("Node " + node.getId() + " position is null. Skipping.");
                continue;
            }

            Object[] xyz = (Object[]) xyzObject; // Cast explicitly to Object[]
            double nodeX = (double) xyz[0];
            double nodeY = (double) xyz[1];

            // Convert graph units (GU) to pixels
            Point3 pixels = viewer.getDefaultView().getCamera().transformGuToPx(nodeX, nodeY, 0);
            double pixelX = pixels.x;
            double pixelY = pixels.y;

//                        System.out.printf("Node %s: Graph position (%.3f, %.3f) --> Pixel position (%.0f, %.0f)%n",
//                                node.getId(), nodeX, nodeY, pixelX, pixelY);
            // Calculate the distance between the click and the node in pixel space
            double distance = Math.sqrt(Math.pow(e.getX() - pixelX, 2) + Math.pow(e.getY() - pixelY, 2));
//                        System.out.println("Distance from click to node " + node.getId() + ": " + distance);

            // If the distance is within the threshold, change the node's color
            if (distance < threshold) {
//                            System.out.println("Node " + node.getId() + " clicked. Changing color to green.");
                node.setAttribute("ui.style", "fill-color: green;");
                Person person = table.get(node.getId());
                String details = person.getDetailsByName(person.getName());
                JOptionPane.showMessageDialog(rootPane, details);

                nodeFound = true;
                break;
            }
        }

        if (!nodeFound) {
            System.out.println("No node found close to the click position.");
        }
    }

    public static void main(String[] args) {
        // Run the GUI application
        SwingUtilities.invokeLater(() -> {
            GenealogyGUI gui = new GenealogyGUI();
            gui.setVisible(true);
        });
    }
}
