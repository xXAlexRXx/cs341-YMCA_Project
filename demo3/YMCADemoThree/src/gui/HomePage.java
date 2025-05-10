package gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import database.Database;

/**
 * HomePage is the initial window for the YMCA application.
 * Users can navigate to create an account, log in, view available programs,
 * or open the navigation guide PDF.
 */
public class HomePage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel startUpPane;

    public HomePage() {
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("YMCA Home");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        startUpPane = new JPanel();
        startUpPane.setBackground(new Color(49, 49, 49));
        startUpPane.setLayout(null);
        setContentPane(startUpPane);

        initializeComponents();
        setVisible(true);
    }

    /**
     * Initializes all UI components: labels, buttons, and action listeners.
     */
    private void initializeComponents() {

        // YMCA logo image
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/ymca-logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setBounds(30, 20, 120, 120);
        startUpPane.add(logoLabel);

        // Title text at the top center
        JLabel titleLabel = new JLabel("<html><strong>Welcome to the <em style=\"color:white\">YMCA</html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 60));
        titleLabel.setBounds(290, 39, 683, 80);
        startUpPane.add(titleLabel);

        // Description prompt below title
        JLabel getStarted = new JLabel("<html><center>Click one of the buttons below, to get started</html>");
        getStarted.setFont(new Font("Tahoma", Font.PLAIN, 24));
        getStarted.setForeground(Color.WHITE);
        getStarted.setBounds(490, 102, 284, 161);
        startUpPane.add(getStarted);

        // Button: Create Account
        JButton accountCreateBtn = new JButton("<html><center><middle>Create <br>an <br>Account</html>");
        accountCreateBtn.setFont(new Font("Tahoma", Font.PLAIN, 30));
        accountCreateBtn.setBounds(419, 260, 200, 200);
        startUpPane.add(accountCreateBtn);
        accountCreateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AccountCreationPage();
            }
        });

        // Button: Log In
        JButton logInBtn = new JButton("<html><center><middle>Log <br>In</html>");
        logInBtn.setForeground(Color.BLACK);
        logInBtn.setFont(new Font("Tahoma", Font.PLAIN, 30));
        logInBtn.setBounds(629, 260, 200, 200);
        startUpPane.add(logInBtn);
        logInBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPage();
            }
        });

        // Button: View Programs
        JButton programsBtn = new JButton("<html><center><middle>Our <br>Programs</html>");
        programsBtn.setFont(new Font("Tahoma", Font.PLAIN, 30));
        programsBtn.setBounds(419, 471, 200, 200);
        startUpPane.add(programsBtn);
        programsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ProgramsPage();
            }
        });

        // Button: Navigation Guide PDF
        JButton navGuideBtn = new JButton("<html><center><middle>Navigation <br>Guide</html>");
        navGuideBtn.setFont(new Font("Tahoma", Font.PLAIN, 30));
        navGuideBtn.setBounds(629, 471, 200, 200);
        startUpPane.add(navGuideBtn);
        navGuideBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = "src/images/CS341 YMCA User Manual.pdf";
                File userManual = new File(filePath);

                if (userManual.exists()) {
                    try {
                        Desktop.getDesktop().open(userManual);
                    } catch (IOException ioe) {
                        JOptionPane.showMessageDialog(null, "Error opening user manual.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "User manual not found:\n" + userManual.getAbsolutePath());
                }
            }
        });

        // Optional preload of programs (not visible here, but backend logic)
        loadPrograms("");
    }

    /**
     * Loads programs from the database. Intended for internal use.
     * If searchTerm is empty, all programs are loaded.
     */
    private void loadPrograms(String searchTerm) {
        Database db = new Database();

        try {
            db.connect();
            String query;
            if (searchTerm == null || searchTerm.isEmpty()) {
                query = db.getAllPrograms();
            } else {
                query = "SELECT * FROM Program WHERE program_name LIKE '%" + searchTerm + "%'";
            }

            ResultSet rs = db.runQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            DefaultTableModel model = new DefaultTableModel();
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(rsmd.getColumnLabel(i));
            }

            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                model.addRow(rowData);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.disconnect();
        }
    }
}
