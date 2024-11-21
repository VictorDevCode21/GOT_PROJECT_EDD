package edu.unimet.edd.interfaces;

import edu.unimet.edd.hash.HashTable;
import edu.unimet.edd.listeners.HashTableListener;
import edu.unimet.edd.tree.Tree;
import edu.unimet.edd.utils.LoadJson;
import edu.unimet.edd.utils.Person;
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
import org.graphstream.graph.Node;
import org.graphstream.stream.ProxyPipe;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swing_viewer.DefaultView;

public class GenealogyGUI extends JFrame implements HashTableListener {

    private Tree tree;
    private JPanel graphPanel;
    private Viewer viewer;
    private HashTable table;

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
            JOptionPane.showMessageDialog(this,
                    "Click a node in the graph to view its details.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        controlsPanel.add(seeRegisterButton);
    }

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
                updateGraphDisplay();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to load tree file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Updates the graph display with the current genealogy tree.
     */
    private void updateGraphDisplay() {
        // Create the graph based on the current tree data
        Graph graph = tree.createGraph();

        System.out.println("Table size in GenealogyGUI: " + table.size());

        // If a viewer already exists, close its previous view to avoid conflicts
        if (viewer != null) {
            try {
                viewer.close();  // Ensure previous viewer is closed properly
            } catch (Exception e) {
                System.out.println("Error closing the viewer: " + e.getMessage());
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
                    System.out.println("\nMouse clicked at: x=" + e.getX() + ", y=" + e.getY());

                    double threshold = 10.0; // Define the threshold distance for node selection
                    boolean nodeFound = false;

                    // Iterate over all nodes in the graph
                    for (Node node : graph) {
                        // Retrieve node coordinates in graph units (GU)
                        // Retrieve node coordinates in graph units (GU)
                        Object xyzObject = node.getAttribute("xyz");
                        if (xyzObject == null) {
                            System.out.println("Node " + node.getId() + " position is null. Skipping.");
                            continue;
                        }

                        Object[] xyz = (Object[]) xyzObject; // Cast explicitly to Object[]
                        double nodeX = (double) xyz[0];
                        double nodeY = (double) xyz[1];

                        // Convert graph units (GU) to pixels
                        Point3 pixels = viewer.getDefaultView().getCamera().transformGuToPx(nodeX, nodeY, 0);
                        double pixelX = pixels.x;
                        double pixelY = pixels.y;

                        System.out.printf("Node %s: Graph position (%.3f, %.3f) --> Pixel position (%.0f, %.0f)%n",
                                node.getId(), nodeX, nodeY, pixelX, pixelY);

                        // Calculate the distance between the click and the node in pixel space
                        double distance = Math.sqrt(Math.pow(e.getX() - pixelX, 2) + Math.pow(e.getY() - pixelY, 2));
                        System.out.println("Distance from click to node " + node.getId() + ": " + distance);

                        // If the distance is within the threshold, change the node's color
                        if (distance < threshold) {
                            System.out.println("Node " + node.getId() + " clicked. Changing color to green.");
                            node.setAttribute("ui.style", "fill-color: green;");
                            nodeFound = true;
                            break;
                        }
                    }

                    if (!nodeFound) {
                        System.out.println("No node found close to the click position.");
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
    }

    public static void main(String[] args) {
        // Run the GUI application
        SwingUtilities.invokeLater(() -> {
            GenealogyGUI gui = new GenealogyGUI();
            gui.setVisible(true);
        });
    }
}
