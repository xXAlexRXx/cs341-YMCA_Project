// StaffProgramPage.java
// Provides a GUI for staff to view, manage, and delete their programs
// Includes functionality to see users per program and search for programs by username

package gui;

import java.awt.Color;
import java.awt.Font;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
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

public class StaffProgramPage {
    private JFrame frame;
    private JPanel staffProgramPane;
    private JTable programTable;
    private JTable detailsTable;
    private DefaultTableModel programTableModel;
    private DefaultTableModel detailsTableModel;
    DefaultTableCellRenderer centerRender;
    private Database db;
    private User staffUser;
    private JTextField searchTextField;
    private JLabel detailsLabel;

    public StaffProgramPage(User staffUser) {
        this.staffUser = staffUser;
        this.db = new Database();
        try {
            db.connect();
            initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Initializes the main window, components, and loads program data
    private void initialize() {
        frame = new JFrame("Staff's Programs");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        staffProgramPane = new JPanel();
        staffProgramPane.setBackground(new Color(50, 50, 50));
        staffProgramPane.setLayout(null);
        frame.getContentPane().add(staffProgramPane);

        createTopButtons();
        createProgramTable(50, 80, 300, 400);
        createMembersTable(400, 80, 300, 400);
        createActionButtons();

        UserNavBar navBar = new UserNavBar(staffUser);
        navBar.setBounds(0, 0, 1280, 50);
        staffProgramPane.add(navBar);

        centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);

        loadPrograms();

        frame.setVisible(true);
    }

    // Adds buttons to navigate to the work page and personal account
    private void createTopButtons() {
        JButton myProgramsButton = new JButton("Staff Work Page");
        myProgramsButton.setBounds(827, 61, 200, 30);
        myProgramsButton.addActionListener(e -> {
            new StaffPage(staffUser);
            frame.dispose();
        });
        staffProgramPane.add(myProgramsButton);

        JButton accountPageButton = new JButton("Personal Account Page");
        accountPageButton.setBounds(1040, 61, 200, 30);
        accountPageButton.addActionListener(e -> {
            new StaffPersonalPage(staffUser);
            frame.dispose();
        });
        staffProgramPane.add(accountPageButton);
    }

    // Creates the table for listing staff programs
    private void createProgramTable(int x, int y, int width, int height) {
        JLabel titleLabel = new JLabel("My Programs");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        titleLabel.setBounds(50, 124, 715, 20);
        staffProgramPane.add(titleLabel);

        programTableModel = new DefaultTableModel();
        programTable = new JTable(programTableModel);
        programTable.setAutoCreateRowSorter(true);
        programTable.setShowVerticalLines(false);
        programTable.setRowHeight(30);
        programTable.setRowSelectionAllowed(true);
        JScrollPane scrollPane = new JScrollPane(programTable);
        scrollPane.setBounds(50, 155, 715, 400);
        staffProgramPane.add(scrollPane);
    }

    // Creates the table used for showing participant details or searched user program lists
    private void createMembersTable(int x, int y, int width, int height) {
        detailsLabel = new JLabel("Program Details");
        detailsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        detailsLabel.setForeground(Color.WHITE);
        detailsLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        detailsLabel.setBounds(831, 124, 409, 20);
        staffProgramPane.add(detailsLabel);

        detailsTableModel = new DefaultTableModel(new Object[]{"Participant Names"}, 0);
        detailsTable = new JTable(detailsTableModel);
        detailsTable.setAutoCreateRowSorter(true);
        detailsTable.setShowVerticalLines(false);
        detailsTable.setRowHeight(30);
        detailsTable.setRowSelectionAllowed(true);

        JScrollPane scrollPane = new JScrollPane(detailsTable);
        scrollPane.setBounds(831, 156, 409, 400);
        staffProgramPane.add(scrollPane);
    }

    // Creates action buttons for search, delete, and show users
    private void createActionButtons() {
        searchTextField = new JTextField();
        searchTextField.setBounds(831, 581, 244, 30);
        staffProgramPane.add(searchTextField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(1120, 581, 120, 30);
        searchButton.addActionListener(e -> searchUserPrograms());
        staffProgramPane.add(searchButton);

        JButton deleteButton = new JButton("Delete Program");
        deleteButton.setBounds(451, 581, 246, 30);
        deleteButton.addActionListener(e -> deleteProgram());
        staffProgramPane.add(deleteButton);

        JButton usersButton = new JButton("Users for Program");
        usersButton.setBounds(162, 581, 246, 30);
        usersButton.addActionListener(e -> showUsersForSelectedProgram());
        staffProgramPane.add(usersButton);
    }

    // Executes search for programs that a specific user is registered in
    private void searchUserPrograms() {
        String searchTerm = searchTextField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a participant's username.");
            return;
        }

        detailsLabel.setText("Programs for User: " + searchTerm);
        detailsTableModel.setColumnIdentifiers(new Object[]{"Program Name", "Start Date", "Days"});
        detailsTableModel.setRowCount(0);

        try {
            Long userID = db.getUserIdByUsername(searchTerm);
            if (userID == null) {
                JOptionPane.showMessageDialog(frame, "Username not found: " + searchTerm);
                return;
            }
            ResultSet rs = db.getProgramsForParticipant(userID);
            while (rs.next()) {
                String programName = rs.getString("program_name");
                Date startDate = rs.getDate("start_date");
                String days = rs.getString("days");
                detailsTableModel.addRow(new Object[]{programName, startDate, days});
            }

            if (detailsTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(frame, "No programs found for participant: " + searchTerm);
            }

            for (int i = 0; i < 3; i++) {
                detailsTable.getColumnModel().getColumn(i).setCellRenderer(centerRender);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Loads the staff member's programs into the main table
    private void loadPrograms() {
        try {
            String query = db.getProgramsForStaff(staffUser.getUserId());
            ResultSet rs = db.runQuery(query);
            programTableModel.setColumnIdentifiers(new Object[]{"ID", "Name", "Start", "End", "Days", "Location"});
            while (rs.next()) {
                Long programId = rs.getLong("program_id");
                String programName = rs.getString("program_name");
                Date startDate = rs.getDate("start_date");
                Date endDate = rs.getDate("end_date");
                String days = rs.getString("days");
                String location = rs.getString("location");
                programTableModel.addRow(new Object[]{programId, programName, startDate, endDate, days, location});
            }
            for (int i = 0; i < 6; i++) {
                programTable.getColumnModel().getColumn(i).setCellRenderer(centerRender);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Deletes a selected program, removes it from the table, and notifies registered users
    private void deleteProgram() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a program to delete.");
            return;
        }

        Long programId = (Long) programTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this program?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Database db = new Database();
            try {
                db.connect();
                String programName = db.getProgramNameById(programId);
                ResultSet rs = db.runQuery("SELECT participant_user_id, participant_dependent_id FROM Registration WHERE program_id = " + programId);
                while (rs.next()) {
                    Long userId = rs.getObject("participant_user_id") != null ? rs.getLong("participant_user_id") : null;
                    Long depId = rs.getObject("participant_dependent_id") != null ? rs.getLong("participant_dependent_id") : null;

                    Long inboxRecipientId = userId;
                    if (depId != null) {
                        PreparedStatement stmt = db.getConnection().prepareStatement("SELECT user_id FROM Dependent WHERE dependent_id = ?");
                        stmt.setLong(1, depId);
                        ResultSet parentRs = stmt.executeQuery();
                        if (parentRs.next()) {
                            inboxRecipientId = parentRs.getLong("user_id");
                        }
                        parentRs.close();
                        stmt.close();
                    }
                    if (inboxRecipientId != null) {
                        String message = "A program you or your dependent were registered for (" + programName + ") has been cancelled.";
                        db.sendMessageToInbox(inboxRecipientId, message);
                    }
                }
                rs.close();
                PreparedStatement deleteRegs = db.getConnection().prepareStatement("DELETE FROM Registration WHERE program_id = ?");
                deleteRegs.setLong(1, programId);
                deleteRegs.executeUpdate();
                deleteRegs.close();

                db.deleteProgram(programId);
                programTableModel.removeRow(selectedRow);
                detailsTableModel.setRowCount(0);
                JOptionPane.showMessageDialog(frame, "Program deleted and all participants have been notified.");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error deleting program: " + e.getMessage());
            } finally {
                db.disconnect();
            }
        }
    }

    // Loads and displays users registered for the selected program
    private void showUsersForSelectedProgram() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a program first.");
            return;
        }

        Object rawId = programTableModel.getValueAt(selectedRow, 0);
        Long programId = rawId instanceof Long ? (Long) rawId : Long.parseLong(rawId.toString());
        detailsLabel.setText("Participants in Selected Program");
        detailsTableModel.setColumnIdentifiers(new Object[]{"Participant Name"});
        detailsTableModel.setRowCount(0);

        try {
            ResultSet rs = db.getAllParticipantsForProgram(programId);
            while (rs.next()) {
                String name = rs.getString("participant_name");
                detailsTableModel.addRow(new Object[]{name});
            }
            rs.close();
            if (detailsTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(frame, "No users are registered for this program.");
            }
            detailsTable.getColumnModel().getColumn(0).setCellRenderer(centerRender);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error retrieving users: " + e.getMessage());
        }
    }
}
