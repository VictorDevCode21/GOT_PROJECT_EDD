package edu.unimet.edd.interfaces;

import edu.unimet.edd.tree.Tree;
import edu.unimet.edd.utils.LoadJson;
import edu.unimet.edd.utils.Person;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swing_viewer.SwingViewer;  // Use SwingViewer instead of Viewer
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GenealogyGUI extends JFrame {

    private Tree tree;
    private JPanel graphPanel;
    private Viewer viewer;
    private JTextField searchField;
    private JButton searchButton;

    public GenealogyGUI() {
        // Initialize the tree and other components
        this.tree = new Tree();
        setTitle("Genealogy Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
        
        // Adding search components
        searchField = new JTextField(20); // Create the search text field
        searchButton = new JButton("Search"); // Create the search button

        // Add the search components to the controls panel
        controlsPanel.add(new JLabel("Search Name:"));
        controlsPanel.add(searchField);
        controlsPanel.add(searchButton);

        add(controlsPanel, BorderLayout.SOUTH);
        
        // Search button action
        searchButton.addActionListener(e -> searchPerson());
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
 * Searches for a person by name and displays the result.
 */
private void searchPerson() {
    try {
        // Retrieve the name entered in the search field, trimming extra spaces.
        String nameToSearch = searchField.getText().trim(); 
        
        // Search for the person in the tree using the name provided.
        Person foundPerson = tree.SearchTest(nameToSearch); 

        if (foundPerson != null) {
            // If the person is found, build a string containing their details.
            String personDetails = "Name: " + foundPerson.getName() + "\n"
                                 + "Title: " + (foundPerson.getTitle() != null ? foundPerson.getTitle() : "N/A") + "\n"
                                 + "Father: " + (foundPerson.getFather() != null && !foundPerson.getFather().isEmpty() ? foundPerson.getFather() : "N/A") + "\n"
                                 + "Mother: " + (foundPerson.getMother() != null && !foundPerson.getMother().isEmpty() ? foundPerson.getMother() : "N/A") + "\n"
                                 + "Fate: " + (foundPerson.getFate() != null ? foundPerson.getFate() : "N/A") + "\n";

            // Display the person's details in a popup dialog.
            JOptionPane.showMessageDialog(this, personDetails, "Person Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // If the person is not found, show an error message dialog.
            JOptionPane.showMessageDialog(this, "Person not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        // If an exception occurs, display an error message with the exception details.
        JOptionPane.showMessageDialog(this, "An error occurred while searching for the person: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        // Print the stack trace for debugging purposes.
        e.printStackTrace();}
}
  
    

    /**
     * Updates the graph display with the current genealogy tree.
     */
    private void updateGraphDisplay() {
        // Create the graph based on the current tree data
        Graph graph = tree.createGraph();

        // Create a new viewer for the graph using SwingViewer
        viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();

        // Remove the old graph view and add the new one
        graphPanel.removeAll();
        graphPanel.add((Component) viewer.addDefaultView(false), BorderLayout.CENTER);
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