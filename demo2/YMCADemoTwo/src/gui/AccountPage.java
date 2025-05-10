package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import database.Database;
import model.User;

public class AccountPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private User currentUser;
    private JPanel accountPane;
    private JTable registrationTable;
    private JTextField familyNameField;
    private JTable dependentTable;

    public AccountPage(User user) {
        this.currentUser = user;

        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Account Page");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        accountPane = new JPanel();
        accountPane.setBackground(new Color(49, 49, 49));
        accountPane.setLayout(null);
        setContentPane(accountPane);

        initializeComponents();

        setVisible(true);
    }

    private void initializeComponents() {
        // NavBar (if you want to pass user info to NavBar, adjust its constructor accordingly)
        NavBar navBar = new NavBar();
        navBar.setBounds(0, 0, 1280, 50);
        accountPane.add(navBar);

        // Title label
        JLabel titleLabel = new JLabel("My Account");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLabel.setBounds(50, 50, 200, 30);
        accountPane.add(titleLabel);

        // Display basic user info
        JLabel userNameLabel = new JLabel("Username: " + currentUser.getUsername());
        userNameLabel.setForeground(Color.WHITE);
        userNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        userNameLabel.setBounds(50, 100, 300, 30);
        accountPane.add(userNameLabel);

        JLabel userTypeLabel = new JLabel("Type: " + currentUser.getUserType());
        userTypeLabel.setForeground(Color.WHITE);
        userTypeLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        userTypeLabel.setBounds(50, 140, 300, 30);
        accountPane.add(userTypeLabel);

        // New Balance label
        JLabel balanceLabel = new JLabel("Balance: " + currentUser.getUserBalance());
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        balanceLabel.setBounds(50, 180, 300, 30);
        accountPane.add(balanceLabel);

        // Scroll pane + table for showing registrations
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 240, 822, 300);
        accountPane.add(scrollPane);

        registrationTable = new JTable();
        scrollPane.setViewportView(registrationTable);

        // Button to view programs (takes you back to the UserPage)
        JButton backToPrograms = new JButton("View Programs");
        backToPrograms.setFont(new Font("Tahoma", Font.PLAIN, 16));
        backToPrograms.setBounds(50, 560, 200, 30);
        accountPane.add(backToPrograms);

        JLabel addFamilyMember = new JLabel("Add Family Member");
        addFamilyMember.setForeground(new Color(255, 255, 255));
        addFamilyMember.setFont(new Font("Tahoma", Font.PLAIN, 20));
        addFamilyMember.setBounds(1032, 60, 190, 30);
        accountPane.add(addFamilyMember);

        familyNameField = new JTextField();
        familyNameField.setBounds(993, 148, 113, 19);
        accountPane.add(familyNameField);
        familyNameField.setColumns(10);

        JButton addFamilyButton = new JButton("Add Member");
        addFamilyButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        addFamilyButton.setBounds(1116, 147, 140, 21);
        accountPane.add(addFamilyButton);

        addFamilyButton.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
                String familyMemberName = familyNameField.getText().trim();
                if (familyMemberName.isEmpty()) {
                    JOptionPane.showMessageDialog(accountPane, "Family Member Username cannot be empty");
                    return;
                }

                Long userID = currentUser.getUserId();
                Database db = new Database();
                try {
                    db.connect();

                    // Step 1: Check current number of family members
                    String countQuery = "SELECT COUNT(*) AS family_count FROM FamilyMember WHERE user_id = ?";
                    PreparedStatement countStmt = db.getConnection().prepareStatement(countQuery);
                    countStmt.setLong(1, userID);
                    ResultSet countRs = countStmt.executeQuery();

                    int familyCount = 0;
                    if (countRs.next()) {
                        familyCount = countRs.getInt("family_count");
                    }
                    countRs.close();
                    countStmt.close();

                    if (familyCount >= 10) {
                        JOptionPane.showMessageDialog(accountPane, "You cannot have more than 10 family members.");
                        return; // Stop here
                    }

                    // Step 2: Find the family member user ID
                    String sql = "SELECT user_id FROM User WHERE username = ?";
                    PreparedStatement stmt = db.getConnection().prepareStatement(sql);
                    stmt.setString(1, familyMemberName);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        Long familyMemberId = rs.getLong("user_id");

                        // Prevent adding self as family member
                        if (familyMemberId.equals(userID)) {
                            JOptionPane.showMessageDialog(accountPane, "You cannot add yourself as a family member.");
                            return;
                        }

                        // Add the family member relationship
                        db.addFamilyMember(userID, familyMemberId, "Family");

                        JOptionPane.showMessageDialog(accountPane, "Family Member Added");
                        loadUserFamilyMembers(); // Refresh the table
                    } else {
                        JOptionPane.showMessageDialog(accountPane, "User not found with username: " + familyMemberName);
                    }
                    rs.close();
                    stmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(accountPane, "Error adding family member");
                } finally {
                    db.disconnect();
                }
            }
        });



        JLabel familyNameLabel = new JLabel("Family Member Username:");
        familyNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        familyNameLabel.setForeground(new Color(255, 255, 255));
        familyNameLabel.setBounds(968, 125, 177, 13);
        accountPane.add(familyNameLabel);

        JLabel currentFamilyLabel = new JLabel("Current Family Members");
        currentFamilyLabel.setForeground(new Color(255, 255, 255));
        currentFamilyLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        currentFamilyLabel.setBounds(968, 240, 254, 30);
        accountPane.add(currentFamilyLabel);

        dependentTable = new JTable();
        JScrollPane FamilyScrollPane = new JScrollPane(dependentTable);
        FamilyScrollPane.setBounds(926, 280, 318, 263); // Adjust bounds and size as needed
        accountPane.add(FamilyScrollPane);

        JButton viewInboxButton = new JButton("View Inbox");
        viewInboxButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        viewInboxButton.setBounds(300, 560, 200, 30);
        accountPane.add(viewInboxButton);

        viewInboxButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InboxPage(currentUser).setVisible(true);
            }
        });

        // Load the dependent data
        loadUserFamilyMembers();



        backToPrograms.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve the current window containing the accountPane and dispose of it
                Window window = SwingUtilities.getWindowAncestor(accountPane);
                if (window != null) {
                    window.dispose();
                }

                Database db = new Database();
                try {
                    db.connect();
                    // Assumes currentUser is a defined class-level variable representing the logged-in user
                    User user = currentUser;

                    if (user != null) {
                        // Check the user type and open the corresponding page
                        if (user.getUserType().equalsIgnoreCase("Staff")) {
                            new StaffPage(user);
                        } else if (user.getUserType().equalsIgnoreCase("Member")) {
                            // Uncomment the following when MemberPage is implemented
                            new MemberPage(user);
                        } else if (user.getUserType().equalsIgnoreCase("User")) {
                            // Uncomment the following when NonMemberPage is implemented
                            new NonMemberPage(user);
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    db.disconnect();
                }
            }
        });


        loadUserRegistrations();
    }

    /**
     * Loads all programs for which the current user is registered
     * and populates the registrationTable.
     */
    private void loadUserRegistrations() {
        Database db = new Database();


        try {
        	db.connect();
            // Query to join Registration & Program tables for the user
        	String query = db.getProgramsForParticipant(currentUser.getUserId());

            ResultSet rs = db.runQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            DefaultTableModel model = new DefaultTableModel();
            // Add column names from metadata
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(rsmd.getColumnLabel(i));
            }
            // Add row data
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                model.addRow(rowData);
            }

            registrationTable.setModel(model);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                accountPane,
                "Error loading registrations: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        } finally {
            db.disconnect();
        }
    }

    private void loadUserFamilyMembers() {
        Database db = new Database();
        try {
            db.connect();
            // Query family members for the current user using a JOIN
            String sql = "SELECT U.username, U.user_id " +
                         "FROM FamilyMember F " +
                         "JOIN User U ON F.family_member_id = U.user_id " +
                         "WHERE F.user_id = ?";
            PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
            pstmt.setLong(1, currentUser.getUserId());
            ResultSet rs = pstmt.executeQuery();

            // Set up a clean table model
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Family Member Username");
            model.addColumn("Family Member ID");

            while (rs.next()) {
                Object[] row = new Object[2];
                row[0] = rs.getString("username"); // username
                row[1] = rs.getLong("user_id");     // user ID
                model.addRow(row);
            }

            dependentTable.setModel(model);

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(accountPane, "Error loading family members: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            db.disconnect();
        }
    }


}
