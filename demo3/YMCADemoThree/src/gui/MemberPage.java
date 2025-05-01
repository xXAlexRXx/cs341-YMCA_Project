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

    private JTextField searchField;
    private JButton searchButton;

    public MemberPage(User user) {
        this.currentUser = user;
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Member Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startUpPane = new JPanel();
        startUpPane.setBackground(new Color(49, 49, 49));
        startUpPane.setLayout(null);
        setContentPane(startUpPane);

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        // Navbar
        UserNavBar userNavBar = new UserNavBar(currentUser);
        userNavBar.setBounds(0, 0, 1280, 50);
        startUpPane.add(userNavBar);

        JLabel titleLabel = new JLabel("Welcome to the YMCA");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
        titleLabel.setBounds(10, 64, 472, 50);
        startUpPane.add(titleLabel);

        // Scrollable program list
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(24, 167, 740, 300);
        startUpPane.add(scrollPane);

        programTable = new JTable();
        scrollPane.setViewportView(programTable);

        // Search field
        searchField = new JTextField();
        searchField.setBounds(24, 512, 200, 30);
        startUpPane.add(searchField);

        // Search button
        searchButton = new JButton("Search");
        searchButton.setBounds(265, 511, 100, 30);
        startUpPane.add(searchButton);

        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String searchTerm = searchField.getText().trim();
                loadPrograms(searchTerm);
            }
        });

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(408, 511, 100, 30);
        startUpPane.add(registerButton);

        registerButton.addActionListener(e -> {
            Database db = new Database();
            try {
                db.connect();
                ResultSet rs = db.getDependentsForUser(currentUser.getUserId());

                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
                model.addElement(currentUser.getUsername() + " (You)");

                while (rs.next()) {
                    String depName = rs.getString("name");
                    long depId = rs.getLong("dependent_id");
                    model.addElement(depName + " (Dependent ID: " + depId + ")");
                }

                JComboBox<String> participantPicker = new JComboBox<>(model);
                int result = JOptionPane.showConfirmDialog(null, participantPicker, "Who are you registering?", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    String selection = (String) participantPicker.getSelectedItem();
                    if (selection.contains("Dependent ID:")) {
                        long depId = Long.parseLong(selection.split(":")[1].replace(")", "").trim());
                        new RegistrationPage(currentUser, null, depId).setVisible(true);
                    } else {
                        new RegistrationPage(currentUser, currentUser.getUserId(), null).setVisible(true);
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                db.disconnect();
            }
        });
        
        JButton dropButton = new JButton("Drop");
        dropButton.setBounds(520, 511, 100, 30);
        startUpPane.add(dropButton);

        dropButton.addActionListener(e -> {
            new DropProgramPage(currentUser).setVisible(true);
        });

        // Labels
        JLabel tableLabel = new JLabel("Available Programs");
        tableLabel.setForeground(Color.WHITE);
        tableLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tableLabel.setBounds(36, 124, 147, 33);
        startUpPane.add(tableLabel);

        JLabel lookupLabel = new JLabel("Enter Keyword");
        lookupLabel.setForeground(Color.WHITE);
        lookupLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lookupLabel.setBounds(64, 477, 92, 23);
        startUpPane.add(lookupLabel);

        loadPrograms("");
    }

    private void loadPrograms(String searchTerm) {
        Database db = new Database();
        try {
            db.connect();
            String query = (searchTerm == null || searchTerm.isEmpty()) ?
                db.getAllPrograms() :
                "SELECT * FROM Program WHERE program_name LIKE '%" + searchTerm + "%'";

            ResultSet rs = db.runQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            DefaultTableModel model = new DefaultTableModel();
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(rsmd.getColumnLabel(i));
            }

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

