package gui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.Database;
import model.User;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MemberPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel startUpPane;
    private JTable programTable;
    private User currentUser;
    
    // New search components:
    private JTextField searchField;
    private JButton searchButton;

    public MemberPage(User user) {
    	this.currentUser = user;
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Start Up Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startUpPane = new JPanel();
        startUpPane.setBackground(new Color(49, 49, 49));
        startUpPane.setLayout(null);
        setContentPane(startUpPane);

        initializeComponents();

        setVisible(true);
    }

    private void initializeComponents() {
        // NavBar at the top
        UserNavBar userNavBar = new UserNavBar(currentUser);
        userNavBar.setBounds(0, 0, 1280, 50);
        startUpPane.add(userNavBar);

        // Title label in the center at the top
        JLabel titleLabel = new JLabel("Welcome to the YMCA");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
        titleLabel.setBounds(10, 64, 472, 50);
        startUpPane.add(titleLabel);

        // Scroll pane for the program table on the left
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(24, 167, 740, 300);
        startUpPane.add(scrollPane);

        programTable = new JTable();
        scrollPane.setViewportView(programTable);

        // Create a text field for search next to the table (to the right of the table)
        searchField = new JTextField();
        searchField.setBounds(24, 512, 200, 30);
        startUpPane.add(searchField);

        // Create a search button next to the search field
        searchButton = new JButton("Search");
        searchButton.setBounds(265, 511, 100, 30);
        startUpPane.add(searchButton);
        
        JLabel tableLable = new JLabel("Available Programs");
        tableLable.setForeground(new Color(255, 255, 255));
        tableLable.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tableLable.setBounds(36, 124, 147, 33);
        startUpPane.add(tableLable);
        
        JLabel lookupLabel = new JLabel("Enter Keyword");
        lookupLabel.setForeground(new Color(255, 255, 255));
        lookupLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lookupLabel.setBounds(64, 477, 92, 23);
        startUpPane.add(lookupLabel);
        
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(408, 511, 100, 30);
        startUpPane.add(registerButton);
        
        registerButton.addActionListener(e -> {
            new RegistrationPage(currentUser).setVisible(true);
        });
        
        

        // When the search button is clicked, reload the table based on the search term
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String searchTerm = searchField.getText().trim();
                loadPrograms(searchTerm);
            }
        });

        // Initially load all programs
        loadPrograms("");
    }

    /**
     * Loads programs from the database.
     * If searchTerm is empty, all programs are loaded.
     * Otherwise, only programs with names matching the search term are loaded.
     */
    private void loadPrograms(String searchTerm) {
        Database db = new Database();
       
        try {
        	db.connect();
            String query;
            if (searchTerm == null || searchTerm.isEmpty()) {
                query = db.getAllPrograms();
            } else {
                // Use SQL LIKE for filtering by program name (case-sensitive; adjust as needed)
                query = "SELECT * FROM Program WHERE program_name LIKE '%" + searchTerm + "%'";
            }
            
            ResultSet rs = db.runQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            
            DefaultTableModel model = new DefaultTableModel();
            // Add column names from the ResultSet metadata
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(rsmd.getColumnLabel(i));
            }
            
            // Add each row from the ResultSet to the model
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                model.addRow(rowData);
            }
            
            rs.close();
            programTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.disconnect();
        }
    }
}
