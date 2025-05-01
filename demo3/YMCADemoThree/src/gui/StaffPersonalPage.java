package gui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import database.Database;
import model.User;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class StaffPersonalPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel startUpPane;
    private JTable programTable;
    private User currentUser;

    private JTextField searchField;
    private JButton searchButton;
    
    private DefaultTableCellRenderer centerRender;

    public StaffPersonalPage(User user) {
        this.currentUser = user;
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Staff Personal Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        startUpPane = new JPanel();
        startUpPane.setBackground(new Color(49, 49, 49));
        startUpPane.setLayout(null);
        setContentPane(startUpPane);

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        // ✅ Use UserNavBar for context
        UserNavBar navBar = new UserNavBar(currentUser);
        navBar.setBounds(0, 0, 1280, 50);
        startUpPane.add(navBar);

        JLabel titleLabel = new JLabel("Welcome to the YMCA");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
        titleLabel.setBounds(10, 64, 472, 50);
        startUpPane.add(titleLabel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(24, 167, 1000, 300);
        startUpPane.add(scrollPane);

        programTable = new JTable();
        scrollPane.setViewportView(programTable);
        
        centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        programTable.setAutoCreateRowSorter(true);
        programTable.setShowVerticalLines(false);
        programTable.setRowHeight(30);

        searchField = new JTextField();
        searchField.setBounds(24, 512, 200, 30);
        startUpPane.add(searchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(265, 511, 100, 30);
        startUpPane.add(searchButton);

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

        // ✅ Register as themselves
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(408, 511, 100, 30);
        startUpPane.add(registerButton);

        registerButton.addActionListener(e -> {
            new RegistrationPage(currentUser, currentUser.getUserId(), null).setVisible(true);
        });

        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String searchTerm = searchField.getText().trim();
                loadPrograms(searchTerm);
            }
        });

        JButton button1 = new JButton("Staff Work Page");
        button1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        button1.setBounds(1027, 61, 200, 30);
        button1.addActionListener(e -> {
            dispose();
            new StaffPage(currentUser);
        });
        startUpPane.add(button1);

        JButton button2 = new JButton("My Programs");
        button2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        button2.setBounds(775, 61, 200, 30);
        button2.addActionListener(e -> {
            dispose();
            new StaffProgramPage(currentUser);
        });
        startUpPane.add(button2);

        loadPrograms("");
    }

    private void loadPrograms(String searchTerm) {
        Database db = new Database();
        try {
            db.connect();
            String query = "SELECT program_name, start_date, end_date, start_time, end_time, days, price, program_id " +
                           "FROM Program " +
                           "WHERE program_name LIKE  '%" + searchTerm + "%' " +
                           "ORDER BY start_date ASC";

            ResultSet rs = db.runQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount() - 1;

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Course");
            model.addColumn("Start Date");
            model.addColumn("End Date");
            model.addColumn("Start Time");
            model.addColumn("End Time");
            model.addColumn("Days Offered");
            model.addColumn("Price");

            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                model.addRow(rowData);
            }

            rs.close();
            programTable.setModel(model);

            for (int i = 0; i < columnCount; i++) {
                programTable.getColumnModel().getColumn(i).setCellRenderer(centerRender);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.disconnect();
        }
    }
}
