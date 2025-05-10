package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import database.Database;
import model.User;

/**
 * InboxPage displays all messages sent to a user in a table.
 * Messages are loaded from the database and ordered by most recent.
 */
public class InboxPage extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTable inboxTable;
    private User currentUser;

    public InboxPage(User currentUser) {
        this.currentUser = currentUser;

        setTitle("Inbox");
        setSize(600, 400);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title label at the top of the window
        JLabel titleLabel = new JLabel("Your Inbox", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Scrollable table to display messages
        inboxTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(inboxTable);
        add(scrollPane, BorderLayout.CENTER);

        loadInboxMessages();
    }

    /**
     * Connects to the database, retrieves inbox messages for the current user,
     * and populates the table with them.
     */
    private void loadInboxMessages() {
        Database db = new Database();
        try {
            db.connect();
            String sql = "SELECT message, date_sent FROM Inbox WHERE user_id = ? ORDER BY date_sent DESC";
            var pstmt = db.getConnection().prepareStatement(sql);
            pstmt.setLong(1, currentUser.getUserId());
            ResultSet rs = pstmt.executeQuery();

            // Setup table columns
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Message");
            model.addColumn("Date Sent");

            // Populate rows with data from the result set
            while (rs.next()) {
                Object[] row = new Object[2];
                row[0] = rs.getString("message");
                row[1] = rs.getTimestamp("date_sent").toLocalDateTime();
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