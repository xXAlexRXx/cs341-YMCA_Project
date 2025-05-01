import java.awt.EventQueue;

import javax.swing.JFrame;

import gui.HomePage;

public class Driver {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                JFrame frame = new HomePage();
                frame.setSize(1280, 720);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}