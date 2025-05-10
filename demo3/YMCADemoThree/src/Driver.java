import java.awt.EventQueue;

import javax.swing.JFrame;

import gui.HomePage;

/**
 * The Driver class serves as the entry point for the YMCA GUI application.
 * It initializes the GUI on the Event Dispatch Thread to ensure thread safety.
 */
public class Driver {
    public static void main(String[] args) {
        // Launch the GUI on the Event Dispatch Thread
        EventQueue.invokeLater(() -> {
            try {
                // Create and configure the main frame (HomePage)
                JFrame frame = new HomePage();
                frame.setSize(1280, 720);                 // Set the initial window size
                frame.setLocationRelativeTo(null);        // Center the window on the screen
                frame.setVisible(true);                   // Make the window visible
            } catch (Exception e) {
                // Print any exception that occurs during GUI startup
                e.printStackTrace();
            }
        });
    }
}
