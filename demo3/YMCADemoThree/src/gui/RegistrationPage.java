package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Vector;

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
import model.Program;
import model.Registration;
import model.User;

/**
 * RegistrationPage allows users or dependents to register for YMCA programs.
 * It lists available programs, checks for conflicts, prerequisites, and handles registration.
 */
public class RegistrationPage extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTable programTable;
    private JButton registerButton;
    private User currentUser;
    private Long participantUserId;       // may be null
    private Long participantDependentId;  // may be null
    DefaultTableCellRenderer centerRender;

    public RegistrationPage(User currentUser, Long participantUserId, Long participantDependentId) {
        this.currentUser = currentUser;
        this.participantUserId = participantUserId;
        this.participantDependentId = participantDependentId;

        setTitle("Program Registration");
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create cell renderer that centers rows
        centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);

        // Program table setup
        programTable = new JTable();
        programTable.setShowVerticalLines(false);
        programTable.setRowHeight(30);
        programTable.setRowSelectionAllowed(true);
        JScrollPane scrollPane = new JScrollPane(programTable);
        add(scrollPane, BorderLayout.CENTER);

        // Register button on bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        registerButton = new JButton("Register");
        bottomPanel.add(registerButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadPrograms();
        setupListeners();
    }

    // Loads all programs from the database into the table
    private void loadPrograms() {
        Database db = new Database();
        try {
            db.connect();
            String query = db.getAllPrograms();
            ResultSet rs = db.runQuery(query);

            DefaultTableModel model = new DefaultTableModel(
                new String[]{"Program ID", "Name", "Description", "Capacity", "Current Capacity", "Start Date", "End Date", "Location", "Price", "Days"}, 0);

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getLong("program_id"));
                row.add(rs.getString("program_name"));
                row.add(rs.getString("description"));
                row.add(rs.getInt("capacity"));
                row.add(rs.getInt("current_capacity"));
                row.add(rs.getDate("start_date"));
                row.add(rs.getDate("end_date"));
                row.add(rs.getString("location"));
                row.add(rs.getDouble("price"));
                row.add(rs.getString("days"));
                model.addRow(row);
            }

            programTable.setModel(model);

            // Center each column value
            for (int i = 0; i < 10; i++) {
                programTable.getColumnModel().getColumn(i).setCellRenderer(centerRender);
            }

            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading programs: " + e.getMessage());
        } finally {
            db.disconnect();
        }
    }

    // Binds register button to action
    private void setupListeners() {
        registerButton.addActionListener(e -> registerForProgram());
    }

    // Core registration logic with validation, prerequisite, and capacity checks
    private void registerForProgram() {
        int selectedRow = programTable.getSelectedRow();

        if (currentUser.getStatus() != null && currentUser.getStatus().equalsIgnoreCase("suspended")) {
            JOptionPane.showMessageDialog(this, "Suspended users cannot register for programs.");
            return;
        }

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a program to register.");
            return;
        }

        try {
            Long programId = (Long) programTable.getValueAt(selectedRow, 0);
            Database db = new Database();
            db.connect();

            long participantId = (participantUserId != null) ? participantUserId : participantDependentId;
            Program desiredProgram = db.getProgramByID(programId);

            // Capacity check
            if (desiredProgram.getCurrentCapacity() >= desiredProgram.getCapacity()) {
                JOptionPane.showMessageDialog(this, "This program is already full.");
                db.disconnect();
                return;
            }

            // Check for scheduling conflicts
            ResultSet registeredProgramsRs = (participantUserId != null) ?
                db.runQuery(db.getProgramsForUser(participantUserId)) :
                db.runQuery(db.getProgramsForDependent(participantDependentId));

            while (registeredProgramsRs.next()) {
                Program existingProgram = new Program();
                existingProgram.setProgramId(registeredProgramsRs.getLong("program_id"));
                existingProgram.setProgramName(registeredProgramsRs.getString("program_name"));
                existingProgram.setDescription(registeredProgramsRs.getString("description"));
                existingProgram.setCapacity(registeredProgramsRs.getInt("capacity"));
                existingProgram.setCurrentCapacity(registeredProgramsRs.getInt("current_capacity"));
                existingProgram.setStartDate(registeredProgramsRs.getDate("start_date").toLocalDate());
                existingProgram.setEndDate(registeredProgramsRs.getDate("end_date").toLocalDate());
                existingProgram.setStartTime(registeredProgramsRs.getTime("start_time").toLocalTime());
                existingProgram.setEndTime(registeredProgramsRs.getTime("end_time").toLocalTime());
                existingProgram.setLocation(registeredProgramsRs.getString("location"));
                existingProgram.setPrice(registeredProgramsRs.getDouble("price"));
                existingProgram.setRequirements(registeredProgramsRs.getInt("requirements"));
                existingProgram.setDays(registeredProgramsRs.getString("days"));

                if (desiredProgram.conflictsWith(existingProgram)) {
                    JOptionPane.showMessageDialog(this,
                        "Schedule conflict detected with program: " + existingProgram.getProgramName(),
                        "Conflict",
                        JOptionPane.ERROR_MESSAGE);
                    db.disconnect();
                    return;
                }
            }
            registeredProgramsRs.close();

            // Check prerequisites if any
            if (desiredProgram.getRequirements() != 0 && desiredProgram.getRequirements() != -1) {
                Program prerequisiteProgram = db.getProgramByID(desiredProgram.getRequirements());
                boolean isRegistered = db.isUserRegisteredForProgram(participantId, prerequisiteProgram.getProgramId());

                if (!desiredProgram.meetsPrerequisite(prerequisiteProgram, isRegistered)) {
                    JOptionPane.showMessageDialog(this,
                        "You must complete " + prerequisiteProgram.getProgramName() + " before registering.",
                        "Prerequisite Not Met",
                        JOptionPane.ERROR_MESSAGE);
                    db.disconnect();
                    return;
                }
            }

            // Proceed with registration
            Registration registration = new Registration();
            registration.setRegisteredByUserId(currentUser.getUserId());
            registration.setProgramId(programId);
            registration.setRegistrationDate(LocalDate.now());
            registration.setParticipantUserId(participantUserId);
            registration.setParticipantDependentId(participantDependentId);

            db.addRegistration(registration);
            db.incrementCurrentCapacity(programId);

            db.sendMessageToInbox(participantId,
                "You have successfully registered for: " + desiredProgram.getProgramName());

            JOptionPane.showMessageDialog(this, "Registration successful!");
            loadPrograms();
            db.disconnect();
            this.dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
}
