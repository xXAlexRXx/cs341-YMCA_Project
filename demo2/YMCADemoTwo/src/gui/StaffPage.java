package gui;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import database.Database;
import model.Program;
import model.User;


public class StaffPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel staffPane;
    private JTextField nameField, dateField, timeField, locationField, priceField, maxParticipantsField, prerequisitesField;
    private JTextArea descriptionArea;
    private JCheckBox monBox, tueBox, wedBox, thuBox, friBox, satBox, sunBox;
    private User currentUser;

    public StaffPage(User user) {
    	this.currentUser = user;
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Staff Work Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        staffPane = new JPanel();
        staffPane.setBackground(new Color(49, 49, 49));
        staffPane.setLayout(null);
        setContentPane(staffPane);

        initializeComponents();
        setVisible(true);
    }

    private void setPlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setText(placeholder);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });
    }

    private void initializeComponents() {
        createTopButtons();
        createTitleLabel();
        createFormFields();
        createProgramButton();

        NavBar navBar = new NavBar();
        navBar.setBounds(0, 0, 1280, 50);
        staffPane.add(navBar);
    }

    private void createTopButtons() {
        JButton button1 = new JButton("My Programs");
        button1.setFont(new Font("Tahoma", Font.PLAIN, 12));
        button1.setBounds(10, 50, 200, 30);
        button1.addActionListener(e -> {
            dispose();  // close current frame
            new StaffProgramPage(currentUser);  // open StaffProgramPage
        });
        staffPane.add(button1);

        JButton button2 = new JButton("Personal Account");
        button2.setFont(new Font("Tahoma", Font.PLAIN, 12));
        button2.setBounds(220, 50, 200, 30);
        button2.addActionListener(e -> {
            dispose();  // close current frame
           new StaffPersonalPage(currentUser);  // open StaffPersonalPage
        });
        staffPane.add(button2);
    }

    private void createTitleLabel() {
        JLabel titleLabel = new JLabel("Staff Work Page", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
        titleLabel.setBounds(471, 50, 340, 60);
        staffPane.add(titleLabel);
    }

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
        dateField = new JTextField();
        setPlaceholder(dateField, "mm/dd/yyyy - mm/dd/yyyy");
        staffPane.add(createAlignedField("Date:", dateField, rightX, topY, labelWidth, fieldWidth));
        topY += rowHeight;

        timeField = new JTextField();
        setPlaceholder(timeField, "hh:mm AM/PM - hh:mm AM/PM");
        staffPane.add(createAlignedField("Time:", timeField, rightX, topY, labelWidth, fieldWidth));
        topY += rowHeight;

        createDaySelection(rightX, topY, labelWidth, fieldWidth);
        createDescriptionField(rightX, 250 + rowHeight * 3, labelWidth, fieldWidth);
    }

    private void createDaySelection(int x, int y, int labelWidth, int fieldWidth) {
        JLabel daysLabel = new JLabel("Days:");
        daysLabel.setForeground(Color.WHITE);
        daysLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        daysLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        daysLabel.setBounds(x, y , labelWidth, 30);
        staffPane.add(daysLabel);

        // Reduced horizontal gap to 3, vertical gap to 0 for tighter spacing
        JPanel daysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        // Increased fieldWidth to fit all checkboxes in one line
        daysPanel.setBounds(x + labelWidth + 3, y, fieldWidth + 40, 30);
        daysPanel.setBackground(new Color(49, 49, 49));

        monBox = new JCheckBox("Mon");
        tueBox = new JCheckBox("Tue");
        wedBox = new JCheckBox("Wed");
        thuBox = new JCheckBox("Thu");
        friBox = new JCheckBox("Fri");
        satBox = new JCheckBox("Sat");
        sunBox = new JCheckBox("Sun");

        JCheckBox[] boxes = {monBox, tueBox, wedBox, thuBox, friBox, satBox, sunBox};
        for (JCheckBox box : boxes) {
            box.setForeground(Color.WHITE);
            box.setBackground(new Color(49, 49, 49));
            daysPanel.add(box);
        }
        staffPane.add(daysPanel);
    }



    private void createDescriptionField(int x, int y, int labelWidth, int fieldWidth) {
    	//y -= 10;

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

    private void createProgramButton() {
        JButton createProgram = new JButton("Finalize Program");
        createProgram.setFont(new Font("Tahoma", Font.PLAIN, 20));
        createProgram.setBounds(490, 600, 300, 50);
        createProgram.addActionListener(e -> createProgramInDatabase());
        staffPane.add(createProgram);
    }

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




    private void createProgramInDatabase() {
        String programName = nameField.getText().trim();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();
        String location = locationField.getText().trim();
        String priceText = priceField.getText().trim();
        String maxParticipantsText = maxParticipantsField.getText().trim();
        String prerequisites = prerequisitesField.getText().trim();  // Retrieve prerequisites

        // Validate required fields
        if (programName.isEmpty() || date.isEmpty() || time.isEmpty() || maxParticipantsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Missing required fields.");
            return;
        }

        int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText);
        int maxParticipants = Integer.parseInt(maxParticipantsText);

        // Process the selected days
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

        // Assuming you have a way to get the signed-in staff's ID (e.g., from a session or a global variable)
        long staffId = currentUser.getUserId(); // This method should return the staff ID of the signed-in staff member

        // If the staff ID is invalid (e.g., no staff is logged in), show an error message
        if (staffId == -1) {
            JOptionPane.showMessageDialog(this, "No staff member is currently signed in.");
            return;
        }

        int prerequisitesInt = 0;  // Default to 0 if no prerequisites are provided
        if (!prerequisites.isEmpty()) {
            try {
                prerequisitesInt = Integer.parseInt(prerequisites);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Prerequisites must be a valid number.");
                return;
            }
        }

        // Parse the date and time fields (assuming time is in HH:mm format and date in YYYY-MM-DD)
     // First, split the date range
        String[] dates = date.split("-");
        if (dates.length != 2) {
            JOptionPane.showMessageDialog(this, "Date format must be mm/dd/yyyy - mm/dd/yyyy.");
            return;
        }

        // Set up the formatter for MM/dd/yyyy format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        // Parse each side separately
        LocalDate programStartDate = LocalDate.parse(dates[0].trim(), dateFormatter);
        LocalDate programEndDate = LocalDate.parse(dates[1].trim(), dateFormatter);

        if (programStartDate.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "Program cannot start before today's date.");
            return;
        }

        String[] times = time.split("-");
        if (times.length != 2) {
            JOptionPane.showMessageDialog(this, "Time format must be hh:mm AM/PM - hh:mm AM/PM.");
            return;
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a"); // <--- ONLY 1 h
        LocalTime programStartTime = LocalTime.parse(times[0].trim(), timeFormatter);
        LocalTime programEndTime = LocalTime.parse(times[1].trim(), timeFormatter);

        // Create the program object
        Program program = new Program();
        program.setProgramName(programName);
        program.setDescription(descriptionArea.getText().trim());
        program.setCapacity(maxParticipants);
        program.setLocation(location);
        program.setDays(selectedDaysString);
        program.setPrice(price);
        program.setRequirements(prerequisitesInt);  // Set the prerequisites
        program.setStaffId(staffId);  // Set the staff who is hosting the program
        program.setStartDate(programStartDate);
        program.setEndDate(programEndDate);
        program.setStartTime(programStartTime);
        program.setEndTime(programEndTime);

        Database db = new Database();

        try {
            db.connect();

            // Check if there are conflicting programs with the same staff
            if (db.isStaffScheduleConflict(program)) {
                JOptionPane.showMessageDialog(this, "The staff member is already scheduled for another program at the same time.");
                return;
            }

            // Check if there are conflicting programs at the same location
            if (db.isLocationScheduleConflict(program)) {
                JOptionPane.showMessageDialog(this, "The location is already booked at the same time.");
                return;
            }

            // Add the program to the database
            db.addProgram(program);
            JOptionPane.showMessageDialog(this, "Program created successfully!");

            // Clear all fields after program creation
            nameField.setText("");
            dateField.setText("");
            timeField.setText("");
            locationField.setText("");
            priceField.setText("");
            maxParticipantsField.setText("");
            prerequisitesField.setText("");
            descriptionArea.setText("");

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