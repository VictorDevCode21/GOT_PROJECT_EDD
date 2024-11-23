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
                updateGraphDisplay(null, false, null, null, null);
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

        // Button to execute event for showing title holders
        JButton showByNameButton = new JButton("Find by name");
        showByNameButton.addActionListener(e -> {
            if (jsonLoaded) {
                showByName();
            } else {
                JOptionPane.showMessageDialog(rootPane, "You need to Load a JSON file first");
            }
        });
        controlsPanel.add(showByNameButton);

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
                table.removeDuplicates();

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
    private void updateGraphDisplay(String personToLookFor, boolean foreFathersNeeded, PersonLinkedList titleName, Integer generationNumber, String personToFind) {
        try {
            // Create the graph based on the current tree data
            Graph graph = tree.createGraph(personToLookFor, foreFathersNeeded, titleName, generationNumber, personToFind);

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
                        try {
                            handleNodeClick(graph, e);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(rootPane, "Error trying to select person because its null: " + e);
                        }
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
            System.out.println("Unexpected Error Here: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Assigns a generation number to each person in the genealogy tree based on
     * their level in the tree and updates the HashTable with the changes.
     */
    private void showGenerationMembers() {
        try {
            // Run long-running tasks in a background thread
            System.out.println("ESTO SE IMPRIME");
            new Thread(() -> {
                System.out.println("ESTO TAMBIEN");
                try {
                    TreeNode root = null;
                    try {
                        root = tree.getRoot();
                        System.out.println("Root successfully retrieved.");
                    } catch (Exception e) {
                        System.out.println("Error retrieving root: " + e.getMessage());
                        e.printStackTrace(System.out);;
                    }

                    if (root != null) {
                        System.out.println("Tree root: " + root.getPerson().getName());
                    } else {
                        System.out.println("Tree root is null.");
                    }
                    System.out.println("Tree contains " + tree.countTreeNodes() + " nodes.");

                    // Use the BFS method from the Tree class
                    Integer totalGenerations = tree.BFS(node -> {
                    });

                    // Prompt for user input in the EDT
                    SwingUtilities.invokeLater(() -> {
                        try {
                            String input = JOptionPane.showInputDialog(
                                    this, "Select a generation number that must be less or equal to: " + totalGenerations,
                                    "Select Generation", JOptionPane.QUESTION_MESSAGE);

                            if (input != null) {
                                int selectedGeneration = Integer.parseInt(input);

                                if (selectedGeneration <= totalGenerations && selectedGeneration >= 0) {
                                    updateGraphDisplay(null, false, null, selectedGeneration, null);
                                } else {
                                    JOptionPane.showMessageDialog(this, "Generation number out of range.",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid generation number.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE));
                }
            }).start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showByName() {
        try {
            // Solicita al usuario que introduzca un nombre o parte de un nombre
            String name = JOptionPane.showInputDialog(
                    this,
                    "Introduce a name: ",
                    "Find by name",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (name == null || name.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Introduce a name please.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Busca coincidencias en el árbol
            GenericLinkedList coincidences = tree.findPersonByName(name);

            if (coincidences == null || coincidences.getSize() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "No matches found for the name: " + name,
                        "No Matches",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // Convierte las coincidencias a un array de Strings para el JList
            String[] names = coincidences.toArray();

            // Crea un JList para mostrar los nombres
            JList<String> nameList = new JList<>(names);
            nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            nameList.setVisibleRowCount(5); // Número visible de filas

            // Añade el JList a un JScrollPane
            JScrollPane scrollPane = new JScrollPane(nameList);

            // Muestra el cuadro de diálogo para que el usuario seleccione un nombre
            int result = JOptionPane.showConfirmDialog(
                    this,
                    scrollPane,
                    "Select a name",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            // Verifica si el usuario seleccionó un nombre
            if (result == JOptionPane.OK_OPTION) {
                String selectedName = nameList.getSelectedValue();
                if (selectedName != null) {
                    JOptionPane.showMessageDialog(
                            this,
                            "You selected: " + selectedName,
                            "Selection",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // Aquí puedes agregar la lógica para manejar la selección
                    // Update graph display with the name of the person to find its descent
                    updateGraphDisplay(null, false, null, null, selectedName);

                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "No name selected.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred. Please try again." + e,
                    "Error: " + e,
                    JOptionPane.ERROR_MESSAGE
            );
        }
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
                updateGraphDisplay(null, false, titleHolders, null, null);
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

            if (personName == null || personName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
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
            updateGraphDisplay(personName.trim(), foreFathersNeeded, null, null, null);

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
