package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import database.Database;
import model.User;

public class AdminPage extends JFrame {
	private static final long serialVersionUID = 1L;
	private User currentUser;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton, suspendButton, reportButton;
    private JTextField startDateField, endDateField;
    DefaultTableCellRenderer centerRender;

    public AdminPage(User user) {
        this.currentUser = user;

        setTitle("Admin Panel");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(49, 49, 49));

        // Create cell renderer that centers rows
        centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);

        // Navigation bar
        UserNavBar navBar = new UserNavBar(currentUser);
        navBar.setBounds(0, 0, 1280, 50);
        add(navBar);

        // Title
        JLabel title = new JLabel("Admin Dashboard");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 24));
        title.setBounds(50, 60, 300, 30);
        add(title);

        // Search field
        searchField = new JTextField();
        searchField.setBounds(50, 100, 200, 25);
        add(searchField);

        searchButton = new JButton("Search User");
        searchButton.setBounds(270, 100, 150, 25);
        add(searchButton);

        searchButton.addActionListener(e -> searchUser());

        suspendButton = new JButton("Suspend/Delete User");
        suspendButton.setBounds(440, 100, 180, 25);
        add(suspendButton);

        suspendButton.addActionListener(e -> suspendUser());

        // Date range inputs
        startDateField = new JTextField("2025-01-01");
        startDateField.setBounds(640, 100, 100, 25);
        add(startDateField);

        endDateField = new JTextField("2025-06-30");
        endDateField.setBounds(750, 100, 100, 25);
        add(endDateField);

        reportButton = new JButton("Generate Report");
        reportButton.setBounds(860, 100, 180, 25);
        add(reportButton);

        reportButton.addActionListener(e -> generateReport());

        // Table setup
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"User ID", "Username", "Email", "User Type", "Status"});

        userTable = new JTable(tableModel);
        userTable.setShowVerticalLines(false);
        userTable.setRowHeight(30);
        userTable.setRowSelectionAllowed(true);
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBounds(50, 150, 1000, 400);
        add(scrollPane);

        setVisible(true);
    }

    private void searchUser() {
        String username = searchField.getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a username to search.");
            return;
        }

        tableModel.setRowCount(0);
        Database db = new Database();
        try {
            db.connect();
            String sql = "SELECT * FROM User WHERE username LIKE ?";
            PreparedStatement stmt = db.getConnection().prepareStatement(sql);
            stmt.setString(1, "%" + username + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getLong("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("user_type"),
                        rs.getString("status")
                });
            }

            // Center row values
            for(int i = 0; i < 5; i++) {
                userTable.getColumnModel().getColumn(i).setCellRenderer(centerRender);
            }

            rs.close();
            stmt.close();

            db.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching user.");
        }
    }

    private void suspendUser() {
        int row = userTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to suspend.");
            return;
        }

        Long userId = (Long) tableModel.getValueAt(row, 0);
        String username = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to suspend/delete user: " + username + "?",
                "Confirm Suspension", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
			return;
		}

        Database db = new Database();
        try {
            db.connect();

            // Set status to suspended
            String updateSql = "UPDATE User SET status = 'suspended' WHERE user_id = ?";
            PreparedStatement stmt = db.getConnection().prepareStatement(updateSql);
            stmt.setLong(1, userId);
            stmt.executeUpdate();
            stmt.close();

            // Delete registrations
            String deleteRegs = "DELETE FROM Registration WHERE participant_user_id = ? OR user_id = ?";
            PreparedStatement delStmt = db.getConnection().prepareStatement(deleteRegs);
            delStmt.setLong(1, userId);
            delStmt.setLong(2, userId);
            delStmt.executeUpdate();
            delStmt.close();

            JOptionPane.showMessageDialog(this, "User suspended and registrations removed.");
            tableModel.removeRow(row);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error suspending user.");
        } finally {
            db.disconnect();
        }
    }

    private void generateReport() {
        String startDate = startDateField.getText().trim();
        String endDate = endDateField.getText().trim();

        Database db = new Database();
        try {
            db.connect();
            String sql = "SELECT U.username, P.program_name, R.registration_date " +
                    "FROM Registration R " +
                    "JOIN User U ON R.user_id = U.user_id " +
                    "JOIN Program P ON R.program_id = P.program_id " +
                    "WHERE R.registration_date BETWEEN ? AND ?";
            PreparedStatement stmt = db.getConnection().prepareStatement(sql);
            stmt.setString(1, startDate);
            stmt.setString(2, endDate);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel reportModel = new DefaultTableModel();
            reportModel.setColumnIdentifiers(new String[]{"Username", "Program Name", "Registration Date"});

            while (rs.next()) {
                reportModel.addRow(new Object[]{
                        rs.getString("username"),
                        rs.getString("program_name"),
                        rs.getDate("registration_date")
                });
            }

            JTable reportTable = new JTable(reportModel);
            reportTable.setShowVerticalLines(false);
            reportTable.setRowHeight(30);
            reportTable.setRowSelectionAllowed(false);
            JScrollPane scrollPane = new JScrollPane(reportTable);
            scrollPane.setPreferredSize(new Dimension(800, 400));

            // Center row values
            for(int i = 0; i < 3; i++) {
                reportTable.getColumnModel().getColumn(i).setCellRenderer(centerRender);
            }

            JOptionPane.showMessageDialog(this, scrollPane, "Registrations Report", JOptionPane.INFORMATION_MESSAGE);

            rs.close();
            stmt.close();
            db.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating report.");
        }
    }
}