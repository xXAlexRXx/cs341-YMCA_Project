package gui;

import java.awt.Color;
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
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import database.Database;
import model.User;

/**
 * AccountPage displays a summary of the logged-in user's information and
 * a table of all current and dependent registrations. It also provides
 * navigation buttons to programs, inbox, and family management.
 */
public class AccountPage extends JFrame {
	private static final long serialVersionUID = 1L;
	private User currentUser;
    private JTable registrationTable;

    /**
     * Constructs the account page UI and loads registration data.
     */
    public AccountPage(User user) {
        this.currentUser = user;
        setTitle("Account Page");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Navigation bar at top
        UserNavBar navBar = new UserNavBar(currentUser);
        navBar.setBounds(0, 0, 1280, 50);
        add(navBar);

        // Display basic user info
        JLabel titleLabel = new JLabel("My Account");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLabel.setBounds(50, 60, 300, 30);
        add(titleLabel);

        JLabel usernameLabel = new JLabel("Username: " + currentUser.getUsername());
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(50, 100, 300, 20);
        add(usernameLabel);

        JLabel userTypeLabel = new JLabel("Type: " + currentUser.getUserType());
        userTypeLabel.setForeground(Color.WHITE);
        userTypeLabel.setBounds(50, 125, 300, 20);
        add(userTypeLabel);

        JLabel balanceLabel = new JLabel("Balance: $" + currentUser.getUserBalance());
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setBounds(50, 150, 300, 20);
        add(balanceLabel);

        // Table to show user's and dependents' registrations
        registrationTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(registrationTable);
        scrollPane.setBounds(50, 200, 800, 300);
        add(scrollPane);

        // Button to view programs
        JButton programsButton = new JButton("View Programs");
        programsButton.setBounds(50, 530, 150, 30);
        programsButton.addActionListener(e -> {
            this.dispose();
            new MemberPage(currentUser);
        });
        add(programsButton);

        // Button to view inbox
        JButton inboxButton = new JButton("View Inbox");
        inboxButton.setBounds(220, 530, 150, 30);
        inboxButton.addActionListener(e -> new InboxPage(currentUser).setVisible(true));
        add(inboxButton);

        // Button to manage family accounts
        JButton familyButton = new JButton("Family Management");
        familyButton.setBounds(390, 530, 180, 30);
        familyButton.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> new FamilyPage(currentUser).setVisible(true));
        });
        add(familyButton);

        // Background color and load data
        getContentPane().setBackground(new Color(49, 49, 49));
        loadUserRegistrations();
        setVisible(true);
    }

    /**
     * Loads all program registrations for the user and their dependents into the table.
     */
    private void loadUserRegistrations() {
        Database db = new Database();
        try {
            db.connect();

            // Table setup
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Participant");
            model.addColumn("Program Name");
            model.addColumn("Start Date");
            model.addColumn("End Date");
            model.addColumn("Location");

            // Add entries for the user
            ResultSet rs = db.runQuery(db.getProgramsForUser(currentUser.getUserId()));
            while (rs.next()) {
                model.addRow(new Object[]{
                    currentUser.getUsername(),
                    rs.getString("program_name"),
                    rs.getDate("start_date"),
                    rs.getDate("end_date"),
                    rs.getString("location")
                });
            }

            // Add entries for each dependent
            String sql = "SELECT * FROM Dependent WHERE user_id = ?";
            PreparedStatement stmt = db.getConnection().prepareStatement(sql);
            stmt.setLong(1, currentUser.getUserId());
            ResultSet deps = stmt.executeQuery();

            while (deps.next()) {
                String depName = deps.getString("name");
                long depId = deps.getLong("dependent_id");
                ResultSet depPrograms = db.runQuery(db.getProgramsForDependent(depId));
                while (depPrograms.next()) {
                    model.addRow(new Object[]{
                        depName,
                        depPrograms.getString("program_name"),
                        depPrograms.getDate("start_date"),
                        depPrograms.getDate("end_date"),
                        depPrograms.getString("location")
                    });
                }
                depPrograms.close();
            }

            // Set table model
            registrationTable.setModel(model);
            deps.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading registrations.");
        } finally {
            db.disconnect();
        }
    }
}
