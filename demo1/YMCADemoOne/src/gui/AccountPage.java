package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.DatabaseYMCA;
import model.User;

import java.awt.event.*;

public class AccountPage extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private User currentUser;
    private JPanel accountPane;
    private JTable registrationTable;
    private JButton backToPrograms;
    
    public AccountPage(User user) {
        this.currentUser = user;
        
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Account Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        accountPane = new JPanel();
        accountPane.setBackground(new Color(49, 49, 49));
        accountPane.setLayout(null);
        setContentPane(accountPane);
        
        initializeComponents();
        
        setVisible(true);
    }
    
    private void initializeComponents() {
        // NavBar (if you want to pass user info to NavBar, adjust its constructor accordingly)
        NavBar navBar = new NavBar();
        navBar.setBounds(0, 0, 1280, 50);
        accountPane.add(navBar);
        
        // Title label
        JLabel titleLabel = new JLabel("My Account");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLabel.setBounds(50, 50, 200, 30);
        accountPane.add(titleLabel);

        // Display basic user info
        JLabel userNameLabel = new JLabel("Username: " + currentUser.getUsername());
        userNameLabel.setForeground(Color.WHITE);
        userNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        userNameLabel.setBounds(50, 100, 300, 30);
        accountPane.add(userNameLabel);

        JLabel userTypeLabel = new JLabel("Type: " + currentUser.getUserType());
        userTypeLabel.setForeground(Color.WHITE);
        userTypeLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        userTypeLabel.setBounds(50, 140, 300, 30);
        accountPane.add(userTypeLabel);
        
        // New Balance label
        JLabel balanceLabel = new JLabel("Balance: " + currentUser.getUserBalance());
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        balanceLabel.setBounds(50, 180, 300, 30);
        accountPane.add(balanceLabel);

        // Scroll pane + table for showing registrations
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 240, 1000, 300);
        accountPane.add(scrollPane);

        registrationTable = new JTable();
        scrollPane.setViewportView(registrationTable);
        
        // Button to view programs (takes you back to the UserPage)
        backToPrograms = new JButton("View Programs");
        backToPrograms.setFont(new Font("Tahoma", Font.PLAIN, 16));
        backToPrograms.setBounds(50, 560, 200, 30);
        accountPane.add(backToPrograms);
        backToPrograms.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open UserPage and dispose of the AccountPage window
                Window window = SwingUtilities.getWindowAncestor(accountPane);
                if (window != null) {
                    window.dispose();
                }
                new UserPage(currentUser);
            }
        });
        
        loadUserRegistrations();
    }
    
    /**
     * Loads all programs for which the current user is registered
     * and populates the registrationTable.
     */
    private void loadUserRegistrations() {
        DatabaseYMCA db = new DatabaseYMCA();
        db.connect();
        
        try {
            // Query to join Registration & Program tables for the user
            String query = db.getProgramsForUser(currentUser.getUserId());
            
            ResultSet rs = db.runQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            
            DefaultTableModel model = new DefaultTableModel();
            // Add column names from metadata
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(rsmd.getColumnLabel(i));
            }
            // Add row data
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                model.addRow(rowData);
            }
            
            registrationTable.setModel(model);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                accountPane, 
                "Error loading registrations: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        } finally {
            db.disconnect();
        }
    }
}
