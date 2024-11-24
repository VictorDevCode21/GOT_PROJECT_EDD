package edu.unimet.got;

import edu.unimet.edd.interfaces.GenealogyGUI;

/**
 * Main class that launches the "Game of Thrones" genealogy program.
 * It initializes and displays the genealogy GUI.
 * @author PC
 */
public class GAME_OF_THRONES {
    
    
    /**
     * Starts the program and shows the genealogy GUI.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {

        GenealogyGUI gui = new GenealogyGUI();
        gui.show();
    }
}
