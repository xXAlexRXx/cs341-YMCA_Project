package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.Database;
import model.Program;
import model.Registration;
import model.User;

public class RegistrationPage extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable programTable;
    private JTextField participantField;
    private JButton registerButton;
    private User currentUser;

    public RegistrationPage(User currentUser) {
        this.currentUser = currentUser;

        setTitle("Program Registration");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        setLayout(new BorderLayout());

        // Top: Program Table
        programTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(programTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom: Registration Form
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        bottomPanel.add(new JLabel("Participant Username:"));
        participantField = new JTextField(10);
        bottomPanel.add(participantField);

        registerButton = new JButton("Register");
        bottomPanel.add(registerButton);

        add(bottomPanel, BorderLayout.SOUTH);

        loadPrograms();
        setupListeners();
    }

    private void loadPrograms() {
        Database db = new Database();
        try {
            db.connect();
            String query = db.getAllPrograms();
            ResultSet rs = db.runQuery(query);

            DefaultTableModel model = new DefaultTableModel(
                new String[]{"Program ID", "Name", "Description", "Capacity", "Current Capacity", "Start Date", "End Date", "Location", "Price"}, 0
            );

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
                model.addRow(row);
            }

            programTable.setModel(model);
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading programs: " + e.getMessage());
        } finally {
            db.disconnect();
        }
    }

    private void setupListeners() {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerForProgram();
            }
        });
    }

    private void registerForProgram() {
        int selectedRow = programTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a program to register.");
            return;
        }

        String participantUsername = participantField.getText().trim();
        if (participantUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the Participant Username.");
            return;
        }

        try {
            Long programId = (Long) programTable.getValueAt(selectedRow, 0);

            Database db = new Database();
            db.connect();
            Long participantId = db.getUserIdByUsername(participantUsername);

            if (participantId == null) {
                JOptionPane.showMessageDialog(this, "No user found with username: " + participantUsername);
                db.disconnect();
                return;
            }

            // Load the program the user is trying to register for
            Program desiredProgram = db.getProgramByID(programId);

            // Load all programs the participant is currently registered for
            ResultSet registeredProgramsRs = db.runQuery(db.getProgramsForParticipant(participantId));
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

                // --- Check for conflicts ---
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

            // --- Check for prerequisite ---
            if (desiredProgram.getRequirements() != 0 && desiredProgram.getRequirements() != -1) {
                // Load the prerequisite program
                Program prerequisiteProgram = db.getProgramByID(desiredProgram.getRequirements());
                boolean isRegistered = db.isUserRegisteredForProgram(participantId, prerequisiteProgram.getProgramId());

                if (!desiredProgram.meetsPrerequisite(prerequisiteProgram, isRegistered)) {
                    JOptionPane.showMessageDialog(this,
                        "You must complete " + prerequisiteProgram.getProgramName() + " before taking " + desiredProgram.getProgramName(),
                        "Prerequisite Not Met",
                        JOptionPane.ERROR_MESSAGE);
                    db.disconnect();
                    return;
                }
            }

            // --- If all checks passed, register the user ---
            Registration registration = new Registration();
            registration.setUserId(currentUser.getUserId());   // Who is registering
            registration.setParticipantId(participantId);      // Who is participating (based on username lookup)
            registration.setProgramId(programId);
            registration.setRegistrationDate(java.time.LocalDate.now());

            db.addRegistration(registration);

            // ✅ NEW: Send confirmation to inbox
            db.sendMessageToInbox(
                participantId,
                "You have successfully registered for: " + desiredProgram.getProgramName()
            );

            JOptionPane.showMessageDialog(this, "Registration successful!");
            db.disconnect();
            this.dispose(); // Close after registration

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }


}


