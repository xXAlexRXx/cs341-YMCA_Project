package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import database.Database;
import model.User;

/**
 * DropProgramPage allows a user to view their and their dependents' current
 * registrations and remove (drop) them from programs.
 */
public class DropProgramPage extends JFrame {
	private static final long serialVersionUID = 1L;
	private User currentUser;
    private JTable registrationTable;
    private DefaultTableModel model;
    DefaultTableCellRenderer centerRender;

    public DropProgramPage(User user) {
        this.currentUser = user;
        setTitle("Drop Program");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set up registration table with headers
        registrationTable = new JTable();
        registrationTable.setShowVerticalLines(false);
        registrationTable.setRowHeight(30);
        registrationTable.setRowSelectionAllowed(true);
        model = new DefaultTableModel(new Object[]{"Participant", "Program ID", "Program Name", "Start Date", "Location"}, 0);
        registrationTable.setModel(model);

        JScrollPane scrollPane = new JScrollPane(registrationTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel for dropping selected registration
        JButton dropButton = new JButton("Drop Selected Program");
        dropButton.addActionListener(e -> dropSelectedProgram());
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(dropButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadRegistrations(); // populate table

        // Center text in each cell
        centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < registrationTable.getColumnCount(); i++) {
            registrationTable.getColumnModel().getColumn(i).setCellRenderer(centerRender);
        }
    }

    /**
     * Loads registrations for the user and their dependents into the table.
     */
    private void loadRegistrations() {
        Database db = new Database();
        model.setRowCount(0); // clear existing
        try {
            db.connect();

            // User's own registrations
            ResultSet userPrograms = db.runQuery(db.getProgramsForUser(currentUser.getUserId()));
            while (userPrograms.next()) {
                model.addRow(new Object[]{
                        currentUser.getUsername(),
                        userPrograms.getLong("program_id"),
                        userPrograms.getString("program_name"),
                        userPrograms.getDate("start_date"),
                        userPrograms.getString("location")
                });
            }

            // Dependent registrations
            PreparedStatement depStmt = db.getConnection().prepareStatement("SELECT * FROM Dependent WHERE user_id = ?");
            depStmt.setLong(1, currentUser.getUserId());
            ResultSet deps = depStmt.executeQuery();

            while (deps.next()) {
                long depId = deps.getLong("dependent_id");
                String depName = deps.getString("name");

                ResultSet depPrograms = db.runQuery(db.getProgramsForDependent(depId));
                while (depPrograms.next()) {
                    model.addRow(new Object[]{
                            depName,
                            depPrograms.getLong("program_id"),
                            depPrograms.getString("program_name"),
                            depPrograms.getDate("start_date"),
                            depPrograms.getString("location")
                    });
                }
                depPrograms.close();
            }

            db.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading registrations.");
        }
    }

    /**
     * Removes the selected registration from the database and updates the view.
     */
    private void dropSelectedProgram() {
        int row = registrationTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a registration to drop.");
            return;
        }

        String participant = model.getValueAt(row, 0).toString();
        long programId = (Long) model.getValueAt(row, 1);
        String programName = model.getValueAt(row, 2).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to drop: " + programName + "?",
                "Confirm Drop",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
			return;
		}

        Database db = new Database();
        try {
            db.connect();

            Long participantId = null;
            boolean isDependent = false;

            // Determine if dropping user or dependent
            if (participant.equals(currentUser.getUsername())) {
                participantId = currentUser.getUserId();
            } else {
                PreparedStatement stmt = db.getConnection().prepareStatement(
                        "SELECT dependent_id FROM Dependent WHERE name = ? AND user_id = ?");
                stmt.setString(1, participant);
                stmt.setLong(2, currentUser.getUserId());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    participantId = rs.getLong("dependent_id");
                    isDependent = true;
                }
                rs.close();
                stmt.close();
            }

            // Delete registration entry
            String deleteSql = isDependent ?
                    "DELETE FROM Registration WHERE program_id = ? AND participant_dependent_id = ?" :
                    "DELETE FROM Registration WHERE program_id = ? AND participant_user_id = ?";
            PreparedStatement deleteStmt = db.getConnection().prepareStatement(deleteSql);
            deleteStmt.setLong(1, programId);
            deleteStmt.setLong(2, participantId);
            deleteStmt.executeUpdate();
            deleteStmt.close();

            // Update program capacity
            db.decrementCurrentCapacity(programId);

            // Notify user in inbox
            String message = "You have been dropped from: " + programName;
            db.sendMessageToInbox(currentUser.getUserId(), message);

            model.removeRow(row);
            JOptionPane.showMessageDialog(this, "Program successfully dropped.");
            loadRegistrations(); // refresh table

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error dropping program.");
        } finally {
            db.disconnect();
        }
    }
}