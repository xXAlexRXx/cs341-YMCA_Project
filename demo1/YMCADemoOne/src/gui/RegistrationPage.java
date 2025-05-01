package gui;

import javax.swing.*;

import database.DatabaseYMCA;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationPage extends JFrame {
    
    private static final long serialVersionUID = 1L;

    public RegistrationPage(long programId, long userId) {
        setSize(600, 400);
        setLocationRelativeTo(null);
        setTitle("Registration Complete");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(49, 49, 49));
        panel.setLayout(null);
        setContentPane(panel);

        // Optional: If you want to display the program name, fetch it from DB:
        String programName = fetchProgramName(programId);

        JLabel confirmationLabel = new JLabel(
            "<html><div style='text-align:center;'>" +
            "Registration Complete!<br/>" +
            "You have registered for Program ID: " + programId + "<br/>" +
            (programName != null ? "Program Name: " + programName : "") +
            "</div></html>"
        );
        confirmationLabel.setForeground(Color.WHITE);
        confirmationLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        confirmationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        confirmationLabel.setBounds(50, 100, 500, 100);
        panel.add(confirmationLabel);

        setVisible(true);
    }

    /**
     * Example method to retrieve the program name by ID.
     * Make sure your Program table has "program_id" and "program_name" columns.
     */
    private String fetchProgramName(long programId) {
        DatabaseYMCA db = new database.DatabaseYMCA();
        db.connect();
        String programName = null;
        try {
            String query = "SELECT program_name FROM Program WHERE program_id = " + programId;
            ResultSet rs = db.runQuery(query);
            if (rs.next()) {
                programName = rs.getString("program_name");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.disconnect();
        }
        return programName;
    }
}
