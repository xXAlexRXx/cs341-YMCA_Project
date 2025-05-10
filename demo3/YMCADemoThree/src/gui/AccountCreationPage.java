package gui;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.*;

import database.Database;
import model.User;
import security.PasswordHash;

/**
 * GUI window for user account creation.
 * Allows input for username, password (with confirmation), and email,
 * includes validation checks, and inserts new user into the database.
 */
public class AccountCreationPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel creationPane;
	private JTextField userNameText;
	private JTextField emailText;
	private JPasswordField passwordText;
	private JPasswordField passConfirmText;
	private JLabel uniqueUserLabel = new JLabel("<html>*must be unique*</html>");
	private JLabel uniqueEmailLabel = new JLabel("<html>*must be unique*</html>");
	private boolean goodPass = false;
	private boolean matchPass = false;
	private boolean goodUser;
	private boolean goodEmail;
	private User user = new User();

	public AccountCreationPage() {
		setResizable(false);
		setTitle("Account Creation");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		creationPane = new JPanel();

		creationPane.setBackground(new Color(49, 49, 49));
		setContentPane(creationPane);
		creationPane.setLayout(null);

		initializeComponents();

		setSize(1280, 720); // <<< ADD THIS
		setLocationRelativeTo(null); // <<< ADD THIS to center it on screen
		setVisible(true);
	}

	/**
     * Initializes and lays out all form components.
     * Adds input validation and create button logic.
     */
	private void initializeComponents() {
		//UI layout and labels
		JComponent navBar = new NavBar();
		navBar.setBounds(0, 0, 1280, 30);
		creationPane.add(navBar);

		JLabel lblNewLabel = new JLabel("<html><center>Create an Account!</html>");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblNewLabel.setBounds(553, 125, 157, 84);
		creationPane.add(lblNewLabel);

		//Username input
		JLabel userNameLabel = new JLabel("Username:");
		userNameLabel.setForeground(new Color(255, 255, 255));
		userNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		userNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		userNameLabel.setBounds(407, 243, 115, 47);
		creationPane.add(userNameLabel);
		
		userNameText = new JTextField();
		userNameText.setFont(new Font("Tahoma", Font.PLAIN, 18));
		userNameText.setBounds(533, 251, 198, 30);
		creationPane.add(userNameText);
		userNameText.setColumns(10);

		//email input
		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		emailLabel.setForeground(Color.WHITE);
		emailLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		emailLabel.setBounds(407, 417, 115, 47);
		creationPane.add(emailLabel);

		emailText = new JTextField();
		emailText.setFont(new Font("Tahoma", Font.PLAIN, 18));
		emailText.setColumns(10);
		emailText.setBounds(532, 425, 198, 30);
		creationPane.add(emailText);

		//Password Inputs
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		passwordLabel.setBounds(407, 301, 115, 47);
		creationPane.add(passwordLabel);

		passwordText = new JPasswordField();
		passwordText.setBounds(533, 312, 198, 30);
		creationPane.add(passwordText);

		JLabel passConfirmLabel = new JLabel("Confirm Password:");
		passConfirmLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		passConfirmLabel.setForeground(Color.WHITE);
		passConfirmLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		passConfirmLabel.setBounds(324, 359, 198, 47);
		creationPane.add(passConfirmLabel);

		passConfirmText = new JPasswordField();
		passConfirmText.setBounds(533, 370, 198, 30);
		creationPane.add(passConfirmText);

		//Validation error labels
		JLabel passLengthLabel = new JLabel("<html>*must be between 8-15 character long*</html>");
		passLengthLabel.setForeground(new Color(255, 0, 0));
		passLengthLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passLengthLabel.setBounds(741, 302, 513, 47);
		creationPane.add(passLengthLabel);
		passLengthLabel.setVisible(false);

		JLabel passMatchLabel = new JLabel("<html>*must match*</html>");
		passMatchLabel.setForeground(Color.RED);
		passMatchLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passMatchLabel.setBounds(741, 360, 513, 47);
		creationPane.add(passMatchLabel);
		passMatchLabel.setVisible(false);

		uniqueEmailLabel.setForeground(Color.RED);
		uniqueEmailLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		uniqueEmailLabel.setBounds(740, 418, 514, 47);
		creationPane.add(uniqueEmailLabel);
		uniqueEmailLabel.setVisible(false);

		uniqueUserLabel.setForeground(Color.RED);
		uniqueUserLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		uniqueUserLabel.setBounds(741, 244, 513, 47);
		creationPane.add(uniqueUserLabel);
		uniqueUserLabel.setVisible(false);

		//Create account button
		JButton createButton = new JButton("Create Account");
		createButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		createButton.setBounds(553, 492, 157, 23);
		creationPane.add(createButton);
		createButton.setEnabled(false);

		//Username validation pattern
		String pattern1 = "[A-Za-z0-9]{5,16}";

		userNameText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!userNameText.getText().matches(pattern1)) {
					uniqueUserLabel.setText("<html>*must be length between 5-15 characters, "
							+ "with letters, numbers, and no spaces*</html>");
					uniqueUserLabel.setVisible(true);
					goodUser = false;
				} else {
					if (goodPass && matchPass && goodEmail) {
						createButton.setEnabled(true);
					}
					goodUser = true;
					uniqueUserLabel.setVisible(false);
				}
			}
		});

		passwordText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				char[] input = passwordText.getPassword();
				if (input.length < 8) {
					passLengthLabel.setVisible(true);
					goodPass = false;
				} else {
					passLengthLabel.setVisible(false);
					goodPass = true;
				}
			}
		});

		passConfirmText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!Arrays.equals(passwordText.getPassword(), passConfirmText.getPassword())) {
					passMatchLabel.setVisible(true);
					matchPass = false;
				} else {
					passMatchLabel.setVisible(false);
					matchPass = true;
				}
			}
		});

		//Email validation pattern
		String pattern2 = "[A-Za-z0-9._!$?&%+-]+@[A-Za-z0-9.-]+\\.(com|org|edu|example|mail){1}";

		emailText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!emailText.getText().matches(pattern2)) {
					uniqueEmailLabel.setText("<html>*must contain [localName] \"@\" [domainName] "
							+ "and either \".com\", \".org\", \".edu\",\".example\", or \".mail\"*</html>");
					uniqueEmailLabel.setVisible(true);
					goodEmail = false;
				} else {
					if (goodPass && matchPass && goodUser) {
						createButton.setEnabled(true);
					}
					goodEmail = true;
					uniqueEmailLabel.setVisible(false);
				}
			}
		});

		createButton.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (createButton.isEnabled()) {
					addUserToDatabase();
				}
			}
		});
	}

	/**
     * Checks for username/email uniqueness and adds user to the database if valid.
     */
	public void addUserToDatabase() {
		boolean validUser = false;
		boolean validEmail = false;
		Database db = new Database();
		try {
			db.connect();
			ResultSet stmt = db.getUsernameAndEmail(userNameText.getText(), emailText.getText());
			if (stmt.next()) {
				if (stmt.getString(1) != null) {
					if (stmt.getString(1).equals(userNameText.getText())) {
						uniqueUserLabel.setText("Must be Unique");
						uniqueUserLabel.setVisible(true);
						validUser = false;
					} else {
						uniqueUserLabel.setVisible(false);
						validUser = true;
					}
				}
				if (stmt.getString(2) != null) {
					if (stmt.getString(2).equals(emailText.getText())) {
						uniqueEmailLabel.setText("Must be Unique");
						uniqueEmailLabel.setVisible(true);
						validEmail = false;
					} else {
						uniqueEmailLabel.setVisible(false);
						validEmail = true;
					}
				}
			} else {
				validUser = true;
				validEmail = true;
			}
			// If both username and email are unique, encrypt password and insert user
			if (validUser && validEmail) {
				passConfirmText.setText(null); // clear so it cannot intercepted
				String stringPass = new String(passwordText.getPassword());
				passwordText.setText(null); // clear so it cannot be intercepted
				String hashed = PasswordHash.encrypt(stringPass);
				user.setPassword(hashed);
				stringPass = null; // clear so it cannot be intercepted
				hashed = null; // clear so it cannot be intercepted

				user.setUsername(userNameText.getText());
				user.setEmail(emailText.getText());
				user.setUserType("User");
				user.setUserBalance(0.00);
				user.setStatus("active");
				db.addUser(user);
				dispose();
				new LoginPage();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			db.disconnect();
		}
	}
}
