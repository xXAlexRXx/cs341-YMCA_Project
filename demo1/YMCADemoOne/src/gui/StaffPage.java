package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import database.DatabaseYMCA;
import model.Program;

public class StaffPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel staffPane;
    private JTextField nameField;
    private JTextField dateField;
    private JTextArea descriptionArea;
    private JTextField timeField;
    private JTextField locationField;
    private JTextField priceField;
    private JTextField maxParticipantsField;
    private JTextField prerequisitesField;
    public StaffPage() {
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Staff Page");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        staffPane = new JPanel();
        staffPane.setBackground(new Color(49, 49, 49));
        staffPane.setLayout(null);
        setContentPane(staffPane);

        initializeComponents();

        setVisible(true);
    }

    private void initializeComponents() {
    	NavBar navBar = new NavBar();
        navBar.setBounds(0, 0, 1280, 50);
        staffPane.add(navBar);

        JLabel staffPageTitle = new JLabel("Staff Page");
        staffPageTitle.setHorizontalAlignment(SwingConstants.CENTER);
        staffPageTitle.setForeground(Color.WHITE);
        staffPageTitle.setFont(new Font("Tahoma", Font.PLAIN, 50));
        staffPageTitle.setBounds(471, 70, 270, 93);
        staffPane.add(staffPageTitle);

        // Create Program button
        JButton createProgram = new JButton("Create Program");
        createProgram.setFont(new Font("Tahoma", Font.PLAIN, 25));
        createProgram.setBounds(490, 540, 300, 50);
        staffPane.add(createProgram);

        createProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createProgramInDatabase();
            }
        });

        // Sample input fields and labels (you can expand these as needed)
        JLabel programNameLabel = new JLabel("Name: ");
        programNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        programNameLabel.setForeground(Color.WHITE);
        programNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        programNameLabel.setBounds(100, 200, 150, 36);
        staffPane.add(programNameLabel);

        nameField = new JTextField();
        nameField.setBounds(260, 200, 250, 36);
        staffPane.add(nameField);
        nameField.setColumns(10);

        JLabel dateLabel = new JLabel("Date: ");
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        dateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        dateLabel.setBounds(100, 250, 150, 36);
        staffPane.add(dateLabel);

        dateField = new JTextField();
        dateField.setBounds(260, 250, 250, 36);
        staffPane.add(dateField);
        dateField.setColumns(10);


        // Description box area
        JLabel descriptionLabel = new JLabel("Description: ");
        descriptionLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        descriptionLabel.setBounds(650, 300, 150, 36);
        staffPane.add(descriptionLabel);

        descriptionArea = new JTextArea();
        descriptionArea.setBounds(810, 300, 300, 100);
        descriptionArea.setWrapStyleWord(true);  // Wrapping
        descriptionArea.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBounds(810, 300, 300, 100);
        staffPane.add(scrollPane);


        // Time text field
        JLabel timeLabel = new JLabel("Time: ");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        timeLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        timeLabel.setBounds(100, 300, 150, 36);
        staffPane.add(timeLabel);

        timeField = new JTextField();
        timeField.setBounds(260, 300, 250, 36);
        staffPane.add(timeField);
        timeField.setColumns(10);



        // Location text field
        JLabel locationLabel = new JLabel("Location: ");
        locationLabel.setForeground(Color.WHITE);
        locationLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        locationLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        locationLabel.setBounds(100, 350, 150, 36);
        staffPane.add(locationLabel);

        locationField = new JTextField();
        locationField.setBounds(260, 350, 250, 36);
        staffPane.add(locationField);
        locationField.setColumns(10);



        // Price text field
        JLabel priceLabel = new JLabel("Price: ");
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        priceLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        priceLabel.setBounds(100, 400, 150, 36);
        staffPane.add(priceLabel);

        priceField = new JTextField();
        priceField.setBounds(260, 400, 250, 36);
        staffPane.add(priceField);
        priceField.setColumns(10);



        // Max participants text field
        JLabel maxParticipantsLabel = new JLabel("Max Participants: ");
        maxParticipantsLabel.setForeground(Color.WHITE);
        maxParticipantsLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        maxParticipantsLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        maxParticipantsLabel.setBounds(650, 200, 150, 36);
        staffPane.add(maxParticipantsLabel);

        maxParticipantsField = new JTextField();
        maxParticipantsField.setBounds(810, 200, 250, 36);
        staffPane.add(maxParticipantsField);
        maxParticipantsField.setColumns(10);


        // Prerequisites text field
        JLabel prerequisitesLabel = new JLabel("Prerequisites: ");
        prerequisitesLabel.setForeground(Color.WHITE);
        prerequisitesLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        prerequisitesLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        prerequisitesLabel.setBounds(650, 250, 150, 36);
        staffPane.add(prerequisitesLabel);

        prerequisitesField = new JTextField();
        prerequisitesField.setBounds(810, 250, 250, 36);
        staffPane.add(prerequisitesField);
        prerequisitesField.setColumns(10);







    }

    // Create program in ymcatest database Program table
    private void createProgramInDatabase() {
        String programName = nameField.getText().trim();
        String date = dateField.getText().trim();
        String description = descriptionArea.getText().trim();
        String time = timeField.getText().trim();
        String location = locationField.getText().trim();
        String priceText = priceField.getText().trim();
        String maxParticipantsText = maxParticipantsField.getText().trim();
        String prerequisites = prerequisitesField.getText().trim();

        // Check if empty
        if (programName.isEmpty() || date.isEmpty() || time.isEmpty() || maxParticipantsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Missing one: (Name, Date, Time, Max Participants).");
            return;
        }

        // Convert price and max participants
        int price = priceText.isEmpty() ? 0 : Integer.parseInt(priceText); // Default to 0 if empty
        int maxParticipants = Integer.parseInt(maxParticipantsText);

        // Convert requirements, 0 if empty
        int requirements = 0;
        if (!prerequisites.isEmpty()) {
            try {
                requirements = Integer.parseInt(prerequisites);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid prerequisites.");
                return;  // Exit the method if prerequisites are invalid
            }
        }

        // Parse date: "MM/dd/yyyy - MM/dd/yyyy"
        LocalDate startDate = null;
        LocalDate endDate = null;
        try {
            String[] dateRange = date.split(" - ");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            startDate = LocalDate.parse(dateRange[0], dateFormatter);  // Parse the start date
            endDate = LocalDate.parse(dateRange[1], dateFormatter);  // Parse the end date
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date, needs to be: xx/xx/xx - xx/xx/xx");
            return;
        }

        // Parse time: "xx:xx AM/PM - xx:xx AM/PM"
        LocalTime startTime = null;
        LocalTime endTime = null;
        try {
            String[] timeRange = time.split(" - ");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            startTime = LocalTime.parse(timeRange[0], timeFormatter);  // Parse the start time
            endTime = LocalTime.parse(timeRange[1], timeFormatter);    // Parse the end time
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid time format, needs to be: xx:xx AM/PM - xx:xx AM/PM");
            return;
        }

        // Create Program
        Program program = new Program();
        program.setProgramName(programName);
        program.setDescription(description);
        program.setCapacity(maxParticipants);
        program.setCurrentCapacity(0);
        program.setStartDate(startDate);  // Set the start date
        program.setEndDate(endDate);      // Set the end date
        program.setStartTime(startTime);  // Set the start time
        program.setEndTime(endTime);      // Set the end time
        program.setLocation(location);    // Store location as a string now
        program.setRequirements(requirements);
        program.setPrice(price);

        // Insert the program into database
        DatabaseYMCA db = new DatabaseYMCA();
        db.connect();
        try {
            db.addProgram(program);
            JOptionPane.showMessageDialog(this, "Program created");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed: " + ex.getMessage());
        } finally {
            db.disconnect();
        }
    }
}