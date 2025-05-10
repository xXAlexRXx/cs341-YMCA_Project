package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.toedter.calendar.JDateChooser;

import database.Database;
import model.Program;
import model.User;

/**
 * StaffPage provides a UI for staff members to create new YMCA programs.
 * It includes fields for schedule, capacity, description, prerequisites, and validations.
 */
public class StaffPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel staffPane;
    private JTextField nameField, locationField, priceField, maxParticipantsField, prerequisitesField;
    private com.toedter.calendar.JDateChooser startChooser, endChooser;
    private JTextArea descriptionArea;
    private JCheckBox monBox, tueBox, wedBox, thuBox, friBox, satBox, sunBox;
    private User currentUser;
    private JSpinner startTimeSpinner, endTimeSpinner;

    public StaffPage(User user) {
        this.currentUser = user;
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Staff Work Page");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        staffPane = new JPanel();
        staffPane.setBackground(new Color(49, 49, 49));
        staffPane.setLayout(null);
        setContentPane(staffPane);

        initializeComponents();
        setVisible(true);
    }

    // Adds placeholder logic to input fields
    @SuppressWarnings("unused")
	private void setPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
			public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
			public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    // Creates top layout: nav bar, buttons, form
    private void initializeComponents() {
        createTopButtons();
        createTitleLabel();
        createFormFields();
        createProgramButton();

        UserNavBar navBar = new UserNavBar(currentUser);
        navBar.setBounds(0, 0, 1280, 50);
        staffPane.add(navBar);
    }

    // Creates nav-style buttons for navigating to other staff pages
    private void createTopButtons() {
        JButton button1 = new JButton("My Programs");
        button1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        button1.setBounds(10, 50, 200, 30);
        button1.addActionListener(e -> {
            dispose();
            new StaffProgramPage(currentUser);
        });
        staffPane.add(button1);

        JButton button2 = new JButton("Personal Account");
        button2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        button2.setBounds(220, 50, 200, 30);
        button2.addActionListener(e -> {
            dispose();
            new StaffPersonalPage(currentUser);
        });
        staffPane.add(button2);
    }

    // Header label for the page
    private void createTitleLabel() {
        JLabel titleLabel = new JLabel("Staff Work Page", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
        titleLabel.setBounds(471, 50, 340, 60);
        staffPane.add(titleLabel);
    }

    // Lays out and populates all form fields for input
    private void createFormFields() {
        int leftX = 100, rightX = 560, topY = 250, rowHeight = 40, labelWidth = 160, fieldWidth = 340;

        nameField = new JTextField();
        staffPane.add(createAlignedField("Name:", nameField, leftX, topY, labelWidth, fieldWidth));

        locationField = new JTextField();
        staffPane.add(createAlignedField("Location:", locationField, leftX, topY += rowHeight, labelWidth, fieldWidth));

        priceField = new JTextField();
        staffPane.add(createAlignedField("Price ($):", priceField, leftX, topY += rowHeight, labelWidth, fieldWidth));

        maxParticipantsField = new JTextField();
        staffPane.add(createAlignedField("Max Participants:", maxParticipantsField, leftX, topY += rowHeight, labelWidth, fieldWidth));

        prerequisitesField = new JTextField();
        staffPane.add(createAlignedField("Prerequisites:", prerequisitesField, leftX, topY += rowHeight, labelWidth, fieldWidth));

        topY = 250;
        startChooser = new JDateChooser();
        endChooser = new JDateChooser();

        // Date inputs
        JPanel datePanel = new JPanel(null);
        datePanel.setBounds(rightX, topY, labelWidth + fieldWidth + 10, 30);
        datePanel.setBackground(new Color(49, 49, 49));

        JLabel label = new JLabel("Date:", SwingConstants.RIGHT);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label.setBounds(0, 0, labelWidth, 30);
        datePanel.add(label);

        startChooser.setBounds(labelWidth + 10, 0, 150, 30);
        endChooser.setBounds(labelWidth + 190, 0, 150, 30);
        datePanel.add(startChooser);
        datePanel.add(endChooser);
        staffPane.add(datePanel);

        JLabel toLabel = new JLabel("to");
        toLabel.setForeground(Color.WHITE);
        toLabel.setBounds(labelWidth + 165, 0, 20, 30);
        datePanel.add(toLabel);

        topY += rowHeight;

        // Time input fields
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        timePanel.setBounds(rightX, topY, labelWidth + fieldWidth + 10, 30);
        timePanel.setBackground(new Color(49, 49, 49));

        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        timeLabel.setPreferredSize(new Dimension(labelWidth, 30));
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        startTimeSpinner = new JSpinner(new SpinnerDateModel());
        endTimeSpinner = new JSpinner(new SpinnerDateModel());

        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startTimeSpinner, "hh:mm a");
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endTimeSpinner, "hh:mm a");

        startTimeSpinner.setEditor(startEditor);
        endTimeSpinner.setEditor(endEditor);

        startTimeSpinner.setPreferredSize(new Dimension(100, 25));
        endTimeSpinner.setPreferredSize(new Dimension(100, 25));

        JLabel toTimeLabel = new JLabel("to");
        toTimeLabel.setForeground(Color.WHITE);
        toTimeLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));

        timePanel.add(timeLabel);
        timePanel.add(startTimeSpinner);
        timePanel.add(toTimeLabel);
        timePanel.add(endTimeSpinner);
        staffPane.add(timePanel);

        topY += rowHeight;

        createDaySelection(rightX, topY, labelWidth, fieldWidth);
        createDescriptionField(rightX, 250 + rowHeight * 3, labelWidth, fieldWidth);
    }

    // Checkbox panel for selecting days of the week
    private void createDaySelection(int x, int y, int labelWidth, int fieldWidth) {
        JLabel daysLabel = new JLabel("Days:");
        daysLabel.setForeground(Color.WHITE);
        daysLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        daysLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        daysLabel.setBounds(x, y , labelWidth, 30);
        staffPane.add(daysLabel);

        JPanel daysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        daysPanel.setBounds(x + labelWidth + 3, y, fieldWidth + 40, 30);
        daysPanel.setBackground(new Color(49, 49, 49));

        monBox = new JCheckBox("Mon"); tueBox = new JCheckBox("Tue"); wedBox = new JCheckBox("Wed");
        thuBox = new JCheckBox("Thu"); friBox = new JCheckBox("Fri"); satBox = new JCheckBox("Sat"); sunBox = new JCheckBox("Sun");

        JCheckBox[] boxes = {monBox, tueBox, wedBox, thuBox, friBox, satBox, sunBox};
        for (JCheckBox box : boxes) {
            box.setForeground(Color.WHITE);
            box.setBackground(new Color(49, 49, 49));
            daysPanel.add(box);
        }
        staffPane.add(daysPanel);
    }

    // Scrollable multi-line input for program description
    private void createDescriptionField(int x, int y, int labelWidth, int fieldWidth) {
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        descriptionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        descriptionLabel.setBounds(x, y, labelWidth, 60);
        staffPane.add(descriptionLabel);

        descriptionArea = new JTextArea();
        descriptionArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBounds(x + labelWidth + 10, y, fieldWidth, 70);
        staffPane.add(scrollPane);
    }

    // Creates aligned form field pairs
    private JPanel createAlignedField(String labelText, JTextField field, int x, int y, int labelWidth, int fieldWidth) {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(49, 49, 49));
        panel.setBounds(x, y, labelWidth + fieldWidth + 10, 30);

        JLabel label = new JLabel(labelText, SwingConstants.RIGHT);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Tahoma", Font.PLAIN, 12));
        label.setBounds(0, 0, labelWidth, 30);
        panel.add(label);

        field.setFont(new Font("Tahoma", Font.PLAIN, 12));
        field.setBounds(labelWidth + 10, 0, fieldWidth, 30);
        panel.add(field);

        return panel;
    }

    // Button to finalize and submit a new program
    private void createProgramButton() {
        JButton createProgram = new JButton("Finalize Program");
        createProgram.setFont(new Font("Tahoma", Font.PLAIN, 20));
        createProgram.setBounds(490, 600, 300, 50);
        createProgram.addActionListener(e -> createProgramInDatabase());
        staffPane.add(createProgram);
    }

    // Builds and submits a Program object to the database with all validation checks
    private void createProgramInDatabase() {
        String programName = nameField.getText().trim();
        String location = locationField.getText().trim();
        String priceText = priceField.getText().trim();
        String maxParticipantsText = maxParticipantsField.getText().trim();
        String prerequisites = prerequisitesField.getText().trim();

        if (programName.isEmpty() || startChooser.getDate() == null || endChooser.getDate() == null || maxParticipantsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Missing required fields.");
            return;
        }

        int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText);
        int maxParticipants = Integer.parseInt(maxParticipantsText);

        StringBuilder selectedDays = new StringBuilder();
        if (monBox.isSelected()) {
			selectedDays.append("Mon, ");
		}
        if (tueBox.isSelected()) {
			selectedDays.append("Tue, ");
		}
        if (wedBox.isSelected()) {
			selectedDays.append("Wed, ");
		}
        if (thuBox.isSelected()) {
			selectedDays.append("Thu, ");
		}
        if (friBox.isSelected()) {
			selectedDays.append("Fri, ");
		}
        if (satBox.isSelected()) {
			selectedDays.append("Sat, ");
		}
        if (sunBox.isSelected()) {
			selectedDays.append("Sun, ");
		}
        String selectedDaysString = selectedDays.length() > 0 ? selectedDays.substring(0, selectedDays.length() - 2) : "None";

        long staffId = currentUser.getUserId();

        int prerequisitesInt = 0;
        if (!prerequisites.isEmpty()) {
            try {
                prerequisitesInt = Integer.parseInt(prerequisites);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Prerequisites must be a valid number.");
                return;
            }
        }

        if (startChooser.getDate() == null || endChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select both start and end dates.");
            return;
        }

        LocalDate programStartDate = startChooser.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate programEndDate = endChooser.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        if (programStartDate.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "Program cannot start before today's date.");
            return;
        }

        Date startDate = (Date) startTimeSpinner.getValue();
        Date endDate = (Date) endTimeSpinner.getValue();

        LocalTime programStartTime = startDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime();
        LocalTime programEndTime = endDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime();

        Program program = new Program();
        program.setProgramName(programName);
        program.setDescription(descriptionArea.getText().trim());
        program.setCapacity(maxParticipants);
        program.setLocation(location);
        program.setDays(selectedDaysString);
        program.setPrice(price);
        program.setRequirements(prerequisitesInt);
        program.setStaffId(staffId);
        program.setStartDate(programStartDate);
        program.setEndDate(programEndDate);
        program.setStartTime(programStartTime);
        program.setEndTime(programEndTime);

        Database db = new Database();

        try {
            db.connect();

            if (db.isStaffScheduleConflict(program)) {
                JOptionPane.showMessageDialog(this, "The staff member is already scheduled for another program at the same time.");
                return;
            }

            if (db.isLocationScheduleConflict(program)) {
                JOptionPane.showMessageDialog(this, "The location is already booked at the same time.");
                return;
            }

            db.addProgram(program);
            JOptionPane.showMessageDialog(this, "Program created successfully!");

            // Reset form
            nameField.setText("");
            locationField.setText("");
            priceField.setText("");
            maxParticipantsField.setText("");
            prerequisitesField.setText("");
            descriptionArea.setText("");
            startChooser.setDate(null);
            endChooser.setDate(null);

            monBox.setSelected(false);
            tueBox.setSelected(false);
            wedBox.setSelected(false);
            thuBox.setSelected(false);
            friBox.setSelected(false);
            satBox.setSelected(false);
            sunBox.setSelected(false);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        } finally {
            db.disconnect();
        }
    }
}