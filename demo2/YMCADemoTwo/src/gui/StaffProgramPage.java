package gui;

import java.awt.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.Database;
import model.User;

public class StaffProgramPage {
    private JFrame frame;
    private JPanel staffProgramPane;
    private JTable programTable;
    private JTable detailsTable;
    private DefaultTableModel programTableModel;
    private DefaultTableModel detailsTableModel;
    private Database db;
    private User staffUser; // You should pass the staff user to this class

    public StaffProgramPage(User staffUser) {
        this.staffUser = staffUser;
        this.db = new Database();
        try {
            db.connect();  // Ensure you have connected to the database
            initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        frame = new JFrame("Staff's Programs");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);



        staffProgramPane = new JPanel();
        staffProgramPane.setBackground(new Color(50, 50, 50));
        staffProgramPane.setLayout(null);
        frame.getContentPane().add(staffProgramPane);

        createTopButtons();
        createProgramTable(50, 80, 300, 400);
        createDetailsTable(400, 80, 300, 400);
        createActionButtons();

        NavBar navBar = new NavBar();
        navBar.setBounds(0, 0, 1280, 50);
        staffProgramPane.add(navBar);

        loadPrograms();  // Load programs from the database

        frame.setVisible(true);
    }

    private void createTopButtons() {
        JButton myProgramsButton = new JButton("Staff Work Page");
        myProgramsButton.setBounds(827, 61, 200, 30);
        myProgramsButton.addActionListener(e -> {
            new StaffPage(staffUser);  // <-- jump to staffPage class with the user
            frame.dispose();
        });
        staffProgramPane.add(myProgramsButton);

        JButton accountPageButton = new JButton("Personal Account Page");
        accountPageButton.setBounds(1040, 61, 200, 30);
        accountPageButton.addActionListener(e -> {
            // Swap to your Personal Account Page class here
            new StaffPersonalPage(staffUser);
            frame.dispose();
        });
        staffProgramPane.add(accountPageButton);
    }

    private void createProgramTable(int x, int y, int width, int height) {
        JLabel titleLabel = new JLabel("My Programs");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        titleLabel.setBounds(x, y - 30, width, 20);
        staffProgramPane.add(titleLabel);

        programTableModel = new DefaultTableModel(new Object[]{"Program ID", "Program Name"}, 0);
        programTable = new JTable(programTableModel);
        JScrollPane scrollPane = new JScrollPane(programTable);
        scrollPane.setBounds(x, y, width, height);
        staffProgramPane.add(scrollPane);
    }

    private void createDetailsTable(int x, int y, int width, int height) {
        JLabel detailsLabel = new JLabel("Program Details");
        detailsLabel.setForeground(Color.WHITE);
        detailsLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        detailsLabel.setBounds(x, y - 30, width, 20);
        staffProgramPane.add(detailsLabel);

        detailsTableModel = new DefaultTableModel(new Object[]{"Participant Names"}, 0);
        detailsTable = new JTable(detailsTableModel);
        JScrollPane scrollPane = new JScrollPane(detailsTable);
        scrollPane.setBounds(x, y, width, height);
        staffProgramPane.add(scrollPane);
    }

    private void createActionButtons() {
        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setBounds(50, 500, 120, 30);
        viewDetailsButton.addActionListener(e -> viewProgramDetails());
        staffProgramPane.add(viewDetailsButton);

        JButton deleteButton = new JButton("Delete Program");
        deleteButton.setBounds(180, 500, 150, 30);
        deleteButton.addActionListener(e -> deleteProgram());
        staffProgramPane.add(deleteButton);
    }

    private void loadPrograms() {
        try {
            String query = db.getProgramsForUser(staffUser.getUserId());
            ResultSet rs = db.runQuery(query);
            while (rs.next()) {
                Long programId = rs.getLong("program_id");
                String programName = rs.getString("program_name");
                programTableModel.addRow(new Object[]{programId, programName});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewProgramDetails() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a program to view details.");
            return;
        }

        Long programId = (Long) programTableModel.getValueAt(selectedRow, 0);

        // Clear old details
        detailsTableModel.setRowCount(0);

        // Fetch participant details from the database
        try {
            String query = db.getUsersForProgram(programId);
            ResultSet rs = db.runQuery(query);
            while (rs.next()) {
                String participantName = rs.getString("username");
                detailsTableModel.addRow(new Object[]{participantName});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteProgram() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a program to delete.");
            return;
        }

        Long programId = (Long) programTableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this program?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                db.deleteProgram(programId);
                programTableModel.removeRow(selectedRow);
                detailsTableModel.setRowCount(0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Assuming you have a User object representing the logged-in staff
        User staffUser = new User(); // Replace with actual user info
        new StaffProgramPage(staffUser);
    }
}