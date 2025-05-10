package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import database.DatabaseYMCA;
import model.User;

public class LoginPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel loginPane;
    private JPasswordField password;
    private JLabel passwordLabel;
    private JLabel usernameLabel;
    private JFormattedTextField username;
    private JButton loginBtn;
    private JLabel passLength;

    public LoginPage() {
        setResizable(false);
        setTitle("YMCA Homepage");
        setSize(1280, 720);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        loginPane = new JPanel();
        loginPane.setBackground(new Color(49, 49, 49));
        setContentPane(loginPane);
        loginPane.setLayout(null);

        initializeComponents(loginPane);
    }

    private void initializeComponents(JPanel contentPane) {
        // Username field
        username = new JFormattedTextField();
        username.setFont(new Font("Tahoma", Font.PLAIN, 15));
        username.setBounds(550, 343, 150, 30);
        contentPane.add(username);

        // Password field
        password = new JPasswordField();
        password.setFont(new Font("Tahoma", Font.PLAIN, 15));
        password.setBounds(550, 384, 150, 30);
        contentPane.add(password);

        // Login button
        loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
        loginBtn.setBounds(582, 438, 89, 23);
        contentPane.add(loginBtn);
        loginBtn.setEnabled(false);

        // Labels
        usernameLabel = new JLabel("Username");
        usernameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(452, 343, 75, 30);
        contentPane.add(usernameLabel);

        passwordLabel = new JLabel("Password");
        passwordLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(452, 383, 75, 30);
        contentPane.add(passwordLabel);

        // Welcome label
        JLabel welcome = new JLabel("Welcome to");
        welcome.setForeground(new Color(66, 160, 255));
        welcome.setHorizontalAlignment(SwingConstants.TRAILING);
        welcome.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
        welcome.setBounds(10, 129, 644, 93);
        contentPane.add(welcome);

        JLabel logo = new JLabel("");
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        Image img = new ImageIcon(LoginPage.class.getResource("/images/ymca-logo.png")).getImage();
        Image newImg = img.getScaledInstance(250, 200, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(newImg));
        logo.setBounds(664, 61, 259, 200);
        contentPane.add(logo);

        // Wrong login message
        JLabel wrongLogin = new JLabel("*Incorrect Username or Password Input*");
        wrongLogin.setHorizontalAlignment(SwingConstants.CENTER);
        wrongLogin.setFont(new Font("Tahoma", Font.PLAIN, 14));
        wrongLogin.setForeground(Color.RED);
        wrongLogin.setBounds(493, 472, 275, 14);
        contentPane.add(wrongLogin);
        wrongLogin.setVisible(false);

        // Password length message
        passLength = new JLabel("Password must contain at least 8 characters");
        passLength.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passLength.setForeground(Color.RED);
        passLength.setBounds(710, 393, 300, 14);
        contentPane.add(passLength);
        passLength.setVisible(false);

        // Custom focus traversal policy
        setFocusTraversalPolicy(new FocusTraversalPolicy() {
            @Override
            public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
                if (aComponent.equals(username)) {
                    return password;
                } else if (aComponent.equals(password)) {
                    return loginBtn;
                }
                return username;
            }

            @Override
            public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
                if (aComponent.equals(loginBtn)) {
                    return password;
                } else if (aComponent.equals(password)) {
                    return username;
                }
                return loginBtn;
            }

            @Override
            public Component getDefaultComponent(Container focusCycleRoot) {
                return username;
            }

            @Override
            public Component getFirstComponent(Container focusCycleRoot) {
                return username;
            }

            @Override
            public Component getLastComponent(Container focusCycleRoot) {
                return loginBtn;
            }
        });

        // Password key listener for enabling login button
        password.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                char[] input = password.getPassword();
                if (input.length < 8) {
                    loginBtn.setEnabled(false);
                    passLength.setVisible(true);
                } else {
                    loginBtn.setEnabled(true);
                    passLength.setVisible(false);
                }
            }
        });
        password.setText("");

        // Login button action listener
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Retrieve the username and password from the form
                String userText = username.getText();
                String passText = new String(password.getPassword());

                // Create an instance of your DatabaseYMCA
                DatabaseYMCA db = new DatabaseYMCA();
                try {
                    db.connect();
                    // Attempt to retrieve a user matching the given credentials
                    User user = db.getUserByUsernameAndPassword(userText, passText);

                    if (user != null) {
                        // Successful login - check user type and redirect accordingly
                        if (user.getUserType().equalsIgnoreCase("Staff")) {
                            dispose();
                            new StaffPage();
                        } else if (user.getUserType().equalsIgnoreCase("Member") ||
                                   user.getUserType().equalsIgnoreCase("User")) {
                            dispose();
                            new UserPage(user);
                        } else {
                            // Unrecognized user type – display error message
                            wrongLogin.setVisible(true);
                        }
                    } else {
                        // No matching user found
                        wrongLogin.setVisible(true);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    wrongLogin.setVisible(true);
                } finally {
                    db.disconnect();
                }
            }
        });
        // logo mouse listener
        logo.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		logo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        	}
        	@Override
			public void mouseExited(MouseEvent e) {
        		logo.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        	}
        	@Override
			public void mouseReleased(MouseEvent e) {
        		dispose();
        		new HomePage();
        	}
        });
    }
}
