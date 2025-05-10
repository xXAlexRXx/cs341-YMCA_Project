package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import database.Database;
import model.User;

/**
 * MemberPage serves as the dashboard for standard users (members).
 * It displays available programs, allows for keyword search,
 * and provides options to register, drop, or refresh listings.
 */
public class MemberPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel startUpPane;
    private JTable programTable;
    private User currentUser;
    DefaultTableCellRenderer centerRender;
    private JTextField searchField;
    private JButton searchButton;

    public MemberPage(User user) {
        this.currentUser = user;
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Member Dashboard");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        startUpPane = new JPanel();
        startUpPane.setBackground(new Color(49, 49, 49));
        startUpPane.setLayout(null);
        setContentPane(startUpPane);

        initializeComponents();
        setVisible(true);
    }

    /**
     * Initializes the UI components including tables, buttons, labels, and listeners.
     */
    private void initializeComponents() {
        // Navbar with user session
        UserNavBar userNavBar = new UserNavBar(currentUser);
        userNavBar.setBounds(0, 0, 1280, 50);
        startUpPane.add(userNavBar);

        // Welcome title
        JLabel titleLabel = new JLabel("Welcome to the YMCA");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
        titleLabel.setBounds(10, 64, 1244, 50);
        startUpPane.add(titleLabel);

        // Center-aligned cell renderer
        centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);

        // Table view
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(119, 167, 1042, 300);
        startUpPane.add(scrollPane);

        programTable = new JTable();
        programTable.setShowVerticalLines(false);
        programTable.setRowHeight(30);
        programTable.setRowSelectionAllowed(true);
        scrollPane.setViewportView(programTable);

        // Search input field
        searchField = new JTextField();
        searchField.setBounds(119, 512, 200, 30);
        startUpPane.add(searchField);

        // Search button
        searchButton = new JButton("Search");
        searchButton.setBounds(329, 512, 100, 30);
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
        registerButton.setBounds(512, 512, 100, 30);
        startUpPane.add(registerButton);

        // Load dependents and allow selection for registration
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

        // Drop button for canceling registration
        JButton dropButton = new JButton("Drop");
        dropButton.setBounds(622, 512, 100, 30);
        startUpPane.add(dropButton);
        dropButton.addActionListener(e -> {
            new DropProgramPage(currentUser).setVisible(true);
        });

        // Refresh table button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBounds(732, 512, 100, 30);
        startUpPane.add(refreshButton);
        refreshButton.addActionListener(e -> {
            String currentSearch = searchField.getText().trim();
            loadPrograms(currentSearch);
        });

        // Table and input labels
        JLabel tableLabel = new JLabel("Available Programs");
        tableLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tableLabel.setForeground(Color.WHITE);
        tableLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        tableLabel.setBounds(119, 124, 1042, 33);
        startUpPane.add(tableLabel);

        JLabel lookupLabel = new JLabel("Enter Keyword");
        lookupLabel.setForeground(Color.WHITE);
        lookupLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lookupLabel.setBounds(119, 478, 200, 23);
        startUpPane.add(lookupLabel);

        loadPrograms("");
    }

    /**
     * Loads all programs from the database or filters by a search term.
     */
    private void loadPrograms(String searchTerm) {
        Database db = new Database();
        try {
            db.connect();
            String query = (searchTerm == null || searchTerm.isEmpty()) ?
                "SELECT program_name, start_date, end_date, start_time, end_time, days, price, current_capacity, capacity FROM Program" :
                "SELECT program_name, start_date, end_date, start_time, end_time, days, price, current_capacity, capacity FROM Program WHERE program_name LIKE '%" + searchTerm + "%'";

            ResultSet rs = db.runQuery(query);

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Course");
            model.addColumn("Start Date");
            model.addColumn("End Date");
            model.addColumn("Start Time");
            model.addColumn("End Time");
            model.addColumn("Days Offered");
            model.addColumn("Price");
            model.addColumn("Capacity (Filled/Max)");

            while (rs.next()) {
                Object[] rowData = new Object[8];
                rowData[0] = rs.getString("program_name");
                rowData[1] = rs.getDate("start_date");
                rowData[2] = rs.getDate("end_date");
                rowData[3] = rs.getTime("start_time");
                rowData[4] = rs.getTime("end_time");
                rowData[5] = rs.getString("days");
                rowData[6] = rs.getDouble("price");
                rowData[7] = rs.getInt("current_capacity") + " / " + rs.getInt("capacity");
                model.addRow(rowData);
            }

            rs.close();
            programTable.setModel(model);
            for(int i = 0; i < 8; i++) {
                programTable.getColumnModel().getColumn(i).setCellRenderer(centerRender);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.disconnect();
        }
    }
}
