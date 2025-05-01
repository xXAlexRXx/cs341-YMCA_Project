package gui;

import database.Database;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StaffProgramPage {
    private JFrame frame;
    private JPanel staffProgramPane;
    private JTable programTable;
    private JTable detailsTable;
    private DefaultTableModel programTableModel;
    private DefaultTableModel detailsTableModel;
    private Database db;
    private User staffUser;
    private JTextField searchTextField;

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
        createMembersTable(400, 80, 300, 400);
        createActionButtons();

        UserNavBar navBar = new UserNavBar(staffUser);
        navBar.setBounds(0, 0, 1280, 50);
        staffProgramPane.add(navBar);

        loadPrograms();

        frame.setVisible(true);
    }

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

    private void createMembersTable(int x, int y, int width, int height) {
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
        searchTextField = new JTextField();
        searchTextField.setBounds(400, 500, 200, 30);
        staffProgramPane.add(searchTextField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(610, 500, 120, 30);
        searchButton.addActionListener(e -> searchUserPrograms());
        staffProgramPane.add(searchButton);

        JButton deleteButton = new JButton("Delete Program");
        deleteButton.setBounds(200, 500, 150, 30);
        deleteButton.addActionListener(e -> deleteProgram());
        staffProgramPane.add(deleteButton);
        
        JButton usersButton = new JButton("Users for Program");
        usersButton.setBounds(30, 500, 160, 30);
        usersButton.addActionListener(e -> showUsersForSelectedProgram());
        staffProgramPane.add(usersButton);
    }

    private void searchUserPrograms() {
        String searchTerm = searchTextField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a participant's username.");
            return;
        }

        detailsTableModel.setRowCount(0);

        try {
            long userID = db.getUserIdByUsername(searchTerm);
            ResultSet rs = db.getProgramsForParticipant(userID);
            

            while (rs.next()) {
                String programName = rs.getString("program_name");
                detailsTableModel.addRow(new Object[]{programName});
            }

            if (detailsTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(frame, "No programs found for participant: " + searchTerm);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPrograms() {
        try {
            String query = db.getProgramsForStaff(staffUser.getUserId());
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

                // Notify participants
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

                // 💥 DELETE registrations before deleting the program
                PreparedStatement deleteRegs = db.getConnection().prepareStatement("DELETE FROM Registration WHERE program_id = ?");
                deleteRegs.setLong(1, programId);
                deleteRegs.executeUpdate();
                deleteRegs.close();

                // Now delete the program
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
    
    private void showUsersForSelectedProgram() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a program first.");
            return;
        }

        Long programId = (Long) programTableModel.getValueAt(selectedRow, 0);
        detailsTableModel.setRowCount(0); // Clear old results

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
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error retrieving users: " + e.getMessage());
        }
    }
}
