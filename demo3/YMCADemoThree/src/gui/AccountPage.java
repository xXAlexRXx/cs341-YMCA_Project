// AccountPage.java (cleaned to only show user info and registrations)
package gui;

import database.Database;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AccountPage extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User currentUser;
    private JTable registrationTable;

    public AccountPage(User user) {
        this.currentUser = user;
        setTitle("Account Page");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        UserNavBar navBar = new UserNavBar(currentUser);
        navBar.setBounds(0, 0, 1280, 50);
        add(navBar);

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

        registrationTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(registrationTable);
        scrollPane.setBounds(50, 200, 800, 300);
        add(scrollPane);

        JButton programsButton = new JButton("View Programs");
        programsButton.setBounds(50, 530, 150, 30);
        programsButton.addActionListener(e -> {
            this.dispose();
            new MemberPage(currentUser);
        });
        add(programsButton);

        JButton inboxButton = new JButton("View Inbox");
        inboxButton.setBounds(220, 530, 150, 30);
        inboxButton.addActionListener(e -> new InboxPage(currentUser).setVisible(true));
        add(inboxButton);

        JButton familyButton = new JButton("Family Management");
        familyButton.setBounds(390, 530, 180, 30);
        familyButton.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> new FamilyPage(currentUser).setVisible(true));
        });
        add(familyButton);

        getContentPane().setBackground(new Color(49, 49, 49));

        loadUserRegistrations();
        setVisible(true);
    }

    private void loadUserRegistrations() {
        Database db = new Database();
        try {
            db.connect();
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Participant");
            model.addColumn("Program Name");
            model.addColumn("Start Date");
            model.addColumn("End Date");
            model.addColumn("Location");

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
