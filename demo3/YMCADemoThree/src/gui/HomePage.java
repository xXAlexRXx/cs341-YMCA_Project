package gui;

import database.Database;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HomePage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel startUpPane;

    public HomePage() {
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("YMCA Home");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        startUpPane = new JPanel();
        startUpPane.setBackground(new Color(49, 49, 49));
        startUpPane.setLayout(null);
        setContentPane(startUpPane);

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {

        // Title label in the center at the top
        JLabel titleLabel = new JLabel("<html><strong>Welcome to the <em style=\"color:white\">YMCA</html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 60));
        titleLabel.setBounds(290, 39, 683, 80);
        startUpPane.add(titleLabel);
        
        // Get started label below the title
        JLabel getStarted = new JLabel("<html><center>Click one of the buttons below, to get started</html>");
        getStarted.setFont(new Font("Tahoma", Font.PLAIN, 24));
        getStarted.setForeground(new Color(255, 255, 255));
        getStarted.setBounds(490, 102, 284, 161);
        startUpPane.add(getStarted);
        
        // Create Account Button
        JButton accountCreateBtn = new JButton("<html><center><middle>Create <br>an <br>Account</html>");
        accountCreateBtn.setFont(new Font("Tahoma", Font.PLAIN, 30));
        accountCreateBtn.setBounds(419, 260, 200, 200);
        startUpPane.add(accountCreateBtn);

        accountCreateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                // new AccountCreationPage();
            }
        });
        
        // Log In Button
        JButton logInBtn = new JButton("<html><center><middle>Log <br>In</html>");
        logInBtn.setForeground(new Color(0, 0, 0));
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
        
        // Programs Button
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
        
        // Navigation Guide Button
        JButton navGuideBtn = new JButton("<html><center><middle>Navigation <br>Guide</html>");
        navGuideBtn.setFont(new Font("Tahoma", Font.PLAIN, 30));
        navGuideBtn.setBounds(629, 471, 200, 200);
        startUpPane.add(navGuideBtn);

        navGuideBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // new NavGuidePage();
            }
        });
        
        // Initially load all programs
        loadPrograms("");
    }

    /**
     * Loads programs from the database.
     * If searchTerm is empty, all programs are loaded.
     * Otherwise, only programs with names matching the search term are loaded.
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
