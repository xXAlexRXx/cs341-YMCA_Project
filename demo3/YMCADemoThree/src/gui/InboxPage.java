package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.Database;
import model.User;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InboxPage extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable inboxTable;
    private User currentUser;

    public InboxPage(User currentUser) {
        this.currentUser = currentUser;

        setTitle("Inbox");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("Your Inbox", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Message table
        inboxTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(inboxTable);
        add(scrollPane, BorderLayout.CENTER);

        loadInboxMessages();
    }

    private void loadInboxMessages() {
        Database db = new Database();
        try {
            db.connect();
            String sql = "SELECT message, date_sent FROM Inbox WHERE user_id = ? ORDER BY date_sent DESC";
            var pstmt = db.getConnection().prepareStatement(sql);
            pstmt.setLong(1, currentUser.getUserId());
            ResultSet rs = pstmt.executeQuery();

            // Table model with columns
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Message");
            model.addColumn("Date Sent");

            while (rs.next()) {
                Object[] row = new Object[2];
                row[0] = rs.getString("message");
                row[1] = rs.getTimestamp("date_sent").toLocalDateTime();  // Converts SQL Timestamp to Java LocalDateTime
                model.addRow(row);
            }

            inboxTable.setModel(model);

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading inbox: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            db.disconnect();
        }
    }
}

