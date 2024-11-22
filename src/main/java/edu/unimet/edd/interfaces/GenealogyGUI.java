package edu.unimet.edd.interfaces;

import edu.unimet.edd.hash.Entry;
import edu.unimet.edd.tree.Tree;
import edu.unimet.edd.utils.LoadJson;
import edu.unimet.edd.utils.Person;
import edu.unimet.edd.utils.LinkedList;
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
    private JTextField searchField; // Campo de texto para la búsqueda
    private JButton searchButton; // Botón para activar la búsqueda
    private JButton findMatchesButton; // Botón para encontrar coincidencias

    public GenealogyGUI() {
        // Inicializar componentes
        this.tree = new Tree();
        setTitle("Genealogy Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de grafo
        graphPanel = new JPanel(new BorderLayout());
        add(graphPanel, BorderLayout.CENTER);

        // Panel de controles
        JPanel controlsPanel = new JPanel();
        JButton loadButton = new JButton("Load Tree");
        loadButton.addActionListener(e -> loadTree());
        controlsPanel.add(loadButton);

        // Componentes de búsqueda
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        findMatchesButton = new JButton("Find Matches");

        controlsPanel.add(new JLabel("Search Name:"));
        controlsPanel.add(searchField);
        controlsPanel.add(searchButton);
        controlsPanel.add(findMatchesButton);
        add(controlsPanel, BorderLayout.SOUTH);

        // Acciones de búsqueda
        searchButton.addActionListener(e -> {
            String name = searchField.getText().trim();
            searchPerson(name);
        });

        findMatchesButton.addActionListener(e -> {
            String nameSubstring = searchField.getText().trim();
            findMatches(nameSubstring);
        });
    }

  /**
   *
   * 
   * @param nameSubstring The substring to search for in the names of persons.
   *                      The search is case-insensitive.
   */
private void findMatches(String nameSubstring) { 
    // Verify that there is text to search
    if (nameSubstring.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a name or substring to search.", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Get the matching persons
    edu.unimet.edd.hash.LinkedList matchingPersons = tree.getTable().findMatchesIgnoreCase(nameSubstring);

    // If no matches are found, display a message
    if (matchingPersons.getSize() == 0) {
        JOptionPane.showMessageDialog(this, "No matches found for: " + nameSubstring, "Information", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    // Create a JDialog to display the matches
    JDialog matchesDialog = new JDialog(this, "Matching Persons", true);
    matchesDialog.setSize(400, 300);
    matchesDialog.setLayout(new BorderLayout());

    // Create a model for the JList
    DefaultListModel<String> listModel = new DefaultListModel<>();
    edu.unimet.edd.hash.Node current = matchingPersons.getFirstNode(); // First node in the list
    while (current != null) {
        @SuppressWarnings("unchecked")
        Entry<String, Person> entry = (Entry<String, Person>) current.getValue();
        Person person = entry.getValue();
        // Add full name with title to the list model
        String fullName = person.getName() + " " + person.getOfHisName() + " " + "of his name";
        listModel.addElement(fullName);
        current = current.getNext(); // Move to the next node
    }

    JList<String> matchesList = new JList<>(listModel);
    JScrollPane scrollPane = new JScrollPane(matchesList);
    matchesDialog.add(scrollPane, BorderLayout.CENTER);

    // Button to select and view details of a person
    JButton viewDetailsButton = new JButton("View Details");
    matchesDialog.add(viewDetailsButton, BorderLayout.SOUTH);

    // Action listener for the button to show details of the selected person
    viewDetailsButton.addActionListener(e -> {
        String selectedValue = matchesList.getSelectedValue();
        if (selectedValue != null) {
            // Use the full selected text to search for the person
            matchesDialog.dispose(); // Close the matches window
            searchPerson(selectedValue.trim()); // Show details of the person
        } else {
            JOptionPane.showMessageDialog(matchesDialog, "Please select a person.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    });

    matchesDialog.setVisible(true);
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

        // Create a new viewer for the graph using SwingViewer
        viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();

        // Remove the old graph view and add the new one
        graphPanel.removeAll();
        graphPanel.add((Component) viewer.addDefaultView(false), BorderLayout.CENTER);
        graphPanel.revalidate();
        graphPanel.repaint();
    }

/**
 * Searches for a person by name and displays their details in a popup window.
 *
 * @param name The name of the person to search for.
 */
public void searchPerson(String name) {
    Person person = tree.getPerson(name);

    if (person == null) {
        JOptionPane.showMessageDialog(null, "Person not found: " + name, "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Build the details string to display
    StringBuilder details = new StringBuilder();
    details.append("Name: ").append(person.getName()).append("\n");
    details.append("Title: ").append(person.getTitle() != null ? person.getTitle() : "N/A").append("\n");
    details.append("Nickname: ").append(person.getNickname() != null ? person.getNickname() : "N/A").append("\n");
    details.append("Father: ").append(person.getFather() != null ? person.getFather() : "N/A").append("\n");
    details.append("Mother: ").append(person.getMother() != null ? person.getMother() : "N/A").append("\n");
    details.append("Fate: ").append(person.getFate() != null ? person.getFate() : "N/A").append("\n");
    details.append("Of his name: ").append(person.getOfHisName() != null ? person.getOfHisName() : "N/A").append("\n");
    details.append("Wed to: ").append(person.getWedTo() != null ? person.getWedTo() : "N/A").append("\n");
    details.append("Eye color: ").append(person.getofEyes() != null ? person.getofEyes() : "N/A").append("\n");
    details.append("Hair color: ").append(person.getofHair() != null ? person.getofHair() : "N/A").append("\n");
    details.append("Notes: ").append(person.getNotes() != null ? person.getNotes() : "N/A").append("\n");

    // Append children
    details.append("Children: ");
    if (!person.getChildren().isEmpty()) {
        LinkedList.LinkedListIterator iterator = person.getChildren().iterator();
        while (iterator.hasNext()) {
            details.append(iterator.next());
            if (iterator.hasNext()) {
                details.append(", ");
            }
        }
    } else {
        details.append("None");
    }
    details.append("\n");

    // Display the details in a dialog
    JOptionPane.showMessageDialog(null, details.toString(), "Person Details", JOptionPane.INFORMATION_MESSAGE);
}




    public static void main(String[] args) {
        // Run the GUI application
        SwingUtilities.invokeLater(() -> {
            GenealogyGUI gui = new GenealogyGUI();
            gui.setVisible(true);
        });
    }
}