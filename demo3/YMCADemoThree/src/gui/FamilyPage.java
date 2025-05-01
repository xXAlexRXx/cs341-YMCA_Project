package gui;

import database.Database;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FamilyPage extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User currentUser;
    private JTable familyTable, dependentsTable;
    private JTextField familyUsernameField;
    private DefaultTableModel dependentsModel;

    private JTextField depNameField, depBirthField, depRelField;

    public FamilyPage(User user) {
        this.currentUser = user;

        setTitle("Family Management");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(49, 49, 49));

        UserNavBar navBar = new UserNavBar(currentUser);
        navBar.setBounds(0, 0, 1280, 50);
        getContentPane().add(navBar);

        JLabel title = new JLabel("Family and Dependent Management");
        title.setFont(new Font("Tahoma", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBounds(50, 60, 500, 30);
        getContentPane().add(title);

        // 🔹 Add Family Section
        JLabel famLabel = new JLabel("Add Family Member (Username):");
        famLabel.setForeground(Color.WHITE);
        famLabel.setBounds(50, 110, 250, 20);
        getContentPane().add(famLabel);

        familyUsernameField = new JTextField();
        familyUsernameField.setBounds(50, 135, 200, 25);
        getContentPane().add(familyUsernameField);

        JButton addFamBtn = new JButton("Add Family Member");
        addFamBtn.setBounds(270, 135, 160, 25);
        getContentPane().add(addFamBtn);
        addFamBtn.addActionListener(e -> addFamilyMember());

        familyTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(familyTable);
        scrollPane.setBounds(50, 180, 600, 250);
        getContentPane().add(scrollPane);

        // 🔹 Add Dependent Section
        JLabel depLabel = new JLabel("Add Dependent:");
        depLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        depLabel.setForeground(Color.WHITE);
        depLabel.setBounds(700, 110, 200, 20);
        getContentPane().add(depLabel);

        depNameField = new JTextField();
        depBirthField = new JTextField();
        depRelField = new JTextField();

        depNameField.setBounds(700, 140, 180, 25);
        depBirthField.setBounds(700, 170, 180, 25);
        depBirthField.setToolTipText("YYYY-MM-DD");
        depRelField.setBounds(700, 200, 180, 25);

        getContentPane().add(depNameField);
        getContentPane().add(depBirthField);
        getContentPane().add(depRelField);

        JLabel depNameLabel = new JLabel("Name");
        depNameLabel.setForeground(Color.WHITE);
        depNameLabel.setBounds(890, 140, 80, 25);
        getContentPane().add(depNameLabel);

        JLabel depBirthLabel = new JLabel("Birthdate (yyyy-mm-dd)");
        depBirthLabel.setForeground(Color.WHITE);
        depBirthLabel.setBounds(890, 170, 173, 25);
        getContentPane().add(depBirthLabel);

        JLabel depRelLabel = new JLabel("Relationship");
        depRelLabel.setForeground(Color.WHITE);
        depRelLabel.setBounds(890, 200, 80, 25);
        getContentPane().add(depRelLabel);

        JButton addDepBtn = new JButton("Add Dependent");
        addDepBtn.setBounds(700, 240, 180, 25);
        getContentPane().add(addDepBtn);
        addDepBtn.addActionListener(e -> addDependent());

        // 🔹 Dependents Table
        JLabel depTableLabel = new JLabel("Your Dependents:");
        depTableLabel.setForeground(Color.WHITE);
        depTableLabel.setBounds(700, 280, 200, 20);
        getContentPane().add(depTableLabel);

        dependentsModel = new DefaultTableModel(new Object[]{"Name", "Birthdate", "Relationship"}, 0);
        dependentsTable = new JTable(dependentsModel);
        JScrollPane depScrollPane = new JScrollPane(dependentsTable);
        depScrollPane.setBounds(700, 310, 400, 200);
        getContentPane().add(depScrollPane);

        JButton backBtn = new JButton("Back to Account");
        backBtn.setBounds(50, 500, 200, 30);
        backBtn.addActionListener(e -> {
            this.dispose();
            new AccountPage(currentUser);
        });
        getContentPane().add(backBtn);

        loadFamilyMembers();
        loadDependents();
    }

    private void loadFamilyMembers() {
        Database db = new Database();
        try {
            db.connect();
            String sql = "SELECT U.username, U.user_id " +
                         "FROM FamilyMember F " +
                         "JOIN User U ON F.family_member_id = U.user_id " +
                         "WHERE F.user_id = ?";
            PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
            pstmt.setLong(1, currentUser.getUserId());
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Family Member Username");
            model.addColumn("User ID");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("username"),
                        rs.getLong("user_id")
                });
            }

            familyTable.setModel(model);
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading family members.");
        } finally {
            db.disconnect();
        }
    }

    private void loadDependents() {
        dependentsModel.setRowCount(0);
        Database db = new Database();
        try {
            db.connect();
            String sql = "SELECT name, birthdate, relationship FROM Dependent WHERE user_id = ?";
            PreparedStatement stmt = db.getConnection().prepareStatement(sql);
            stmt.setLong(1, currentUser.getUserId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dependentsModel.addRow(new Object[]{
                        rs.getString("name"),
                        rs.getDate("birthdate"),
                        rs.getString("relationship")
                });
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading dependents.");
        } finally {
            db.disconnect();
        }
    }

    private void addFamilyMember() {
        String famUsername = familyUsernameField.getText().trim();
        if (famUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username field is empty.");
            return;
        }

        Database db = new Database();
        try {
            db.connect();
            long userId = currentUser.getUserId();

            PreparedStatement checkStmt = db.getConnection().prepareStatement("SELECT COUNT(*) FROM FamilyMember WHERE user_id = ?");
            checkStmt.setLong(1, userId);
            ResultSet rsCount = checkStmt.executeQuery();
            if (rsCount.next() && rsCount.getInt(1) >= 10) {
                JOptionPane.showMessageDialog(this, "You cannot have more than 10 family members.");
                return;
            }
            rsCount.close();
            checkStmt.close();

            PreparedStatement findStmt = db.getConnection().prepareStatement("SELECT user_id FROM User WHERE username = ?");
            findStmt.setString(1, famUsername);
            ResultSet rs = findStmt.executeQuery();

            if (rs.next()) {
                long familyMemberId = rs.getLong("user_id");
                if (familyMemberId == userId) {
                    JOptionPane.showMessageDialog(this, "You can't add yourself.");
                    return;
                }

                db.addFamilyMember(userId, familyMemberId, "Family");
                JOptionPane.showMessageDialog(this, "Family member added.");
                loadFamilyMembers();
                familyUsernameField.setText("");

            } else {
                JOptionPane.showMessageDialog(this, "Username not found.");
            }

            rs.close();
            findStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding family member.");
        } finally {
            db.disconnect();
        }
    }

    private void addDependent() {
        String name = depNameField.getText().trim();
        String birthdate = depBirthField.getText().trim();
        String relation = depRelField.getText().trim();

        if (name.isEmpty() || birthdate.isEmpty() || relation.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill out all dependent fields.");
            return;
        }

        if (!birthdate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Birthdate must be in format YYYY-MM-DD (e.g., 2015-06-25)");
            return;
        }

        Database db = new Database();
        try {
            db.connect();
            PreparedStatement stmt = db.getConnection().prepareStatement(
                    "INSERT INTO Dependent (user_id, name, birthdate, relationship) VALUES (?, ?, ?, ?)"
            );
            stmt.setLong(1, currentUser.getUserId());
            stmt.setString(2, name);
            stmt.setDate(3, Date.valueOf(birthdate));
            stmt.setString(4, relation);
            stmt.executeUpdate();
            stmt.close();

            JOptionPane.showMessageDialog(this, "Dependent added.");
            depNameField.setText("");
            depBirthField.setText("");
            depRelField.setText("");

            loadDependents(); // Refresh table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding dependent.");
        } finally {
            db.disconnect();
        }
    }
}
