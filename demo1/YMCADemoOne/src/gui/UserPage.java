package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import database.DatabaseYMCA;
import model.Registration;
import model.User;

public class UserPage extends JFrame {

	 private static final long serialVersionUID = 1L;
	    private JPanel userPane;
	    private JTable programTable;
	    private User currentUser;
	    private JTextField searchField;
	    private JButton searchButton;
	    // New fields for Program ID & Username
	    private JTextField programIdField;
	    private JTextField userNameField;

	    public UserPage(User user) {
	        this.currentUser = user;
	        setSize(1280, 720);
	        setLocationRelativeTo(null);
	        setResizable(false);
	        setTitle("User Page");
	        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	        userPane = new JPanel();
	        userPane.setBackground(new Color(49, 49, 49));
	        userPane.setLayout(null);
	        setContentPane(userPane);

	        initializeComponents();

	        setVisible(true);
	    }

	    private void initializeComponents() {
	        // NavBar at the top
	        NavBar navBar = new NavBar();
	        navBar.setBounds(0, 0, 1280, 50);
	        userPane.add(navBar);

	        // Label showing the user type in the top-right corner
	        JLabel userTypeLabel = new JLabel("Type: " + currentUser.getUserType());
	        userTypeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
	        userTypeLabel.setForeground(Color.WHITE);
	        userTypeLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
	        userTypeLabel.setBounds(1100, 55, 150, 30);
	        userPane.add(userTypeLabel);

	        // Table (scroll pane) on the left side
	        JScrollPane scrollPane = new JScrollPane();
	        scrollPane.setBounds(50, 200, 600, 300);
	        userPane.add(scrollPane);

	        programTable = new JTable();
	        scrollPane.setViewportView(programTable);

	        // Labels & text fields for Program ID, Username
	        JLabel programIdLabel = new JLabel("Program ID:");
	        programIdLabel.setForeground(Color.WHITE);
	        programIdLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
	        programIdLabel.setBounds(800, 200, 100, 30);
	        userPane.add(programIdLabel);

	        programIdField = new JTextField();
	        programIdField.setBounds(900, 200, 100, 30);
	        userPane.add(programIdField);

	        JLabel userNameLabel = new JLabel("Username:");
	        userNameLabel.setForeground(Color.WHITE);
	        userNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
	        userNameLabel.setBounds(800, 240, 100, 30);
	        userPane.add(userNameLabel);

	        userNameField = new JTextField(currentUser.getUsername());
	        // Pre-fill with currentUser's username, or leave blank
	        userNameField.setBounds(900, 240, 100, 30);
	        userPane.add(userNameField);

	        // Smaller "Join Program" button next to the text fields
	        JButton joinProgramBtn = new JButton("Join Program");
	        joinProgramBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
	        joinProgramBtn.setBounds(800, 280, 200, 30);
	        userPane.add(joinProgramBtn);

	        // On click, insert into Registration table & show a "RegistrationCompletePage"
	        joinProgramBtn.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // Parse the program ID from the text field
	                String programIdStr = programIdField.getText().trim();
	                long programId;
	                try {
	                    programId = Long.parseLong(programIdStr);
	                } catch (NumberFormatException ex) {
	                    JOptionPane.showMessageDialog(userPane,
	                        "Invalid Program ID. Please enter a valid numeric value.",
	                        "Error", JOptionPane.ERROR_MESSAGE);
	                    return;
	                }

	                DatabaseYMCA db = new DatabaseYMCA();
	                db.connect();
	                try {
	                    // Retrieve the program's requirements and price
	                    String reqPriceQuery = "SELECT requirements, price FROM Program WHERE program_id = " + programId;
	                    ResultSet rsProg = db.runQuery(reqPriceQuery);
	                    int requirement = 0; // Assume 0 means no prerequisite
	                    double programPrice = 0.0;
	                    if (rsProg.next()) {
	                        requirement = rsProg.getInt("requirements");
	                        programPrice = rsProg.getDouble("price");
	                    }
	                    rsProg.close();

	                    // If there is a requirement, check if the user has completed it.
	                    if (requirement != 0) {
	                        String checkReqQuery = "SELECT * FROM Registration WHERE user_id = "
	                                + currentUser.getUserId() + " AND program_id = " + requirement;
	                        ResultSet rsCheck = db.runQuery(checkReqQuery);
	                        if (!rsCheck.next()) {
	                            // User hasn't registered for the required program.
	                            JOptionPane.showMessageDialog(userPane,
	                                "Registration failed. You must complete the required program (ID: "
	                                + requirement + ") before registering for this program.",
	                                "Registration Error",
	                                JOptionPane.ERROR_MESSAGE);
	                            rsCheck.close();
	                            return; // Abort registration
	                        }
	                        rsCheck.close();
	                    }

	                    // Determine charge amount based on user type
	                    double chargeAmount = programPrice; // default full price
	                    if (currentUser.getUserType().equalsIgnoreCase("Member")) {
	                        chargeAmount = programPrice * 0.5;
	                    }
	                    // Optionally add different pricing logic for Staff or other types here

	                    // Instead of checking for sufficient balance, we now simply add the charge to the balance.
	                    String updateBalanceQuery = "UPDATE User SET balance = balance + " + chargeAmount
	                                                + " WHERE user_id = " + currentUser.getUserId();
	                    db.runUpdate(updateBalanceQuery);

	                    // Proceed with inserting the registration record
	                    Registration registration = new Registration();
	                    registration.setUserId(currentUser.getUserId());
	                    registration.setProgramId(programId);
	                    registration.setRegistrationDate(LocalDate.now());
	                    db.addRegistration(registration);

	                    // Optionally update the Program table's current_capacity via trigger or manually here

	                    // Open the registration complete page
	                    new RegistrationPage(programId, currentUser.getUserId());

	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                } finally {
	                    db.disconnect();
	                }
	            }
	        });


	        searchField = new JTextField();
	        searchField.setBounds(700, 410, 200, 30);
	        userPane.add(searchField);

	        searchButton = new JButton("Search");
	        searchButton.setBounds(910, 410, 100, 30);
	        userPane.add(searchButton);

	        // When the search button is clicked, reload the table based on the search term
	        searchButton.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                String searchTerm = searchField.getText().trim();
	                loadPrograms(searchTerm);
	            }
	        });

	        JButton accountButton = new JButton("Account");
	        accountButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
	        accountButton.setBounds(800, 320, 200, 30);
	        userPane.add(accountButton);

	        // When clicked, open the AccountPage
	        accountButton.addActionListener(e -> {
	            new AccountPage(currentUser); // Pass the logged-in user
	        });

        // 3) Load data from the database into the table
        loadPrograms("");
    }

	    private void loadPrograms(String searchTerm) {
	        DatabaseYMCA db = new DatabaseYMCA();
	        db.connect();

	        try {
	            String query;
	            if (searchTerm == null || searchTerm.isEmpty()) {
	                query = db.getAllPrograms();
	            } else {
	                // Use SQL LIKE for filtering by program name (case-sensitive; adjust as needed)
	                query = "SELECT * FROM Program WHERE program_name LIKE '%" + searchTerm + "%'";
	            }

	            ResultSet rs = db.runQuery(query);
	            ResultSetMetaData rsmd = rs.getMetaData();
	            int columnCount = rsmd.getColumnCount();

	            DefaultTableModel model = new DefaultTableModel();
	            // Add column names from the ResultSet metadata
	            for (int i = 1; i <= columnCount; i++) {
	                model.addColumn(rsmd.getColumnLabel(i));
	            }

	            // Add each row from the ResultSet to the model
	            while (rs.next()) {
	                Object[] rowData = new Object[columnCount];
	                for (int i = 1; i <= columnCount; i++) {
	                    rowData[i - 1] = rs.getObject(i);
	                }
	                model.addRow(rowData);
	            }

	            rs.close();
	            programTable.setModel(model);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            db.disconnect();
	        }
	    }

	    class TextAreaRenderer extends JTextArea implements TableCellRenderer {
	        /**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public TextAreaRenderer() {
	            setLineWrap(true);
	            setWrapStyleWord(true);
	            setOpaque(true);
	        }

	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value,
	                boolean isSelected, boolean hasFocus, int row, int column) {
	            setText(value == null ? "" : value.toString());
	            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);

	            if (table.getRowHeight(row) != getPreferredSize().height) {
	                table.setRowHeight(row, getPreferredSize().height);
	            }

	            if (isSelected) {
	                setBackground(table.getSelectionBackground());
	                setForeground(table.getSelectionForeground());
	            } else {
	                setBackground(table.getBackground());
	                setForeground(table.getForeground());
	            }
	            return this;
	        }
	    }
}
