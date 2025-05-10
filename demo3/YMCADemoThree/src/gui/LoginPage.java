package gui;

import java.awt.Color;
import java.awt.Cursor;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import database.Database;
import model.User;
import security.PasswordHash;

/**
 * LoginPage allows users to authenticate with a username and password.
 * It verifies login credentials and redirects based on user type.
 */
public class LoginPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel loginPane;
    private JPasswordField password;
    private JLabel passwordLabel;
    private JLabel usernameLabel;
    private JFormattedTextField username;
    private JButton loginBtn;
    private JLabel passLength;
    private JLabel welcome;
    private JLabel wrongLogin;

    public LoginPage() {
        setResizable(false);
        setTitle("YMCA Login");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        loginPane = new JPanel();
        loginPane.setBackground(new Color(49, 49, 49));
        setContentPane(loginPane);
        loginPane.setLayout(null);

        initializeComponents(loginPane);

        setSize(1280, 720);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Initializes all form elements, event listeners, and visual labels.
     */
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
        loginBtn.setFont(new Font("Tahoma", Font.PLAIN, 18));
        loginBtn.setBounds(582, 438, 89, 23);
        contentPane.add(loginBtn);
        loginBtn.setEnabled(false);

        // Username label
        usernameLabel = new JLabel("Username");
        usernameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(431, 343, 96, 30);
        contentPane.add(usernameLabel);

        // Password label
        passwordLabel = new JLabel("Password");
        passwordLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(431, 383, 96, 30);
        contentPane.add(passwordLabel);

        // Welcome label text
        welcome = new JLabel("Welcome to");
        welcome.setForeground(new Color(66, 160, 255));
        welcome.setHorizontalAlignment(SwingConstants.TRAILING);
        welcome.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
        welcome.setBounds(10, 129, 644, 93);
        contentPane.add(welcome);

        // YMCA logo
        JLabel logo = new JLabel("");
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        Image img = new ImageIcon(LoginPage.class.getResource("/images/ymca-logo.png")).getImage();
        Image newImg = img.getScaledInstance(250, 200, Image.SCALE_SMOOTH);
        logo.setIcon(new ImageIcon(newImg));
        logo.setBounds(664, 61, 259, 200);
        contentPane.add(logo);

        // Error label for wrong credentials
        wrongLogin = new JLabel("*Incorrect Username or Password Input*");
        wrongLogin.setHorizontalAlignment(SwingConstants.CENTER);
        wrongLogin.setFont(new Font("Tahoma", Font.PLAIN, 15));
        wrongLogin.setForeground(Color.RED);
        wrongLogin.setBounds(493, 472, 275, 23);
        contentPane.add(wrongLogin);
        wrongLogin.setVisible(false);

        // Password validation message
        passLength = new JLabel("Password must contain at least 8 characters");
        passLength.setFont(new Font("Tahoma", Font.PLAIN, 15));
        passLength.setForeground(Color.RED);
        passLength.setBounds(710, 393, 300, 14);
        contentPane.add(passLength);
        passLength.setVisible(false);

        // Account creation prompt
        JLabel createLabel = new JLabel("<html><center>Don't have an account?</html>");
        createLabel.setForeground(Color.WHITE);
        createLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        createLabel.setBounds(552, 497, 159, 93);
        loginPane.add(createLabel);

        // Button to navigate to account creation page
        JButton createButton = new JButton("<html>Create An Account</html>");
        createButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        createButton.setBounds(527, 578, 215, 38);
        loginPane.add(createButton);

        // Password length enforcement on key release
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

        // Main login process logic on button click
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String userText = username.getText();
                String passText = new String(password.getPassword());
                String encrypted = PasswordHash.encrypt(passText);

                Database db = new Database();
                try {
                    db.connect();
                    User user = db.getUserByUsernameAndPassword(userText, encrypted);

                    if (user != null) {
                        if (user.getStatus() != null && user.getStatus().equalsIgnoreCase("suspended")) {
                            JOptionPane.showMessageDialog(null, "Your account is suspended.");
                            return;
                        }

                        // Redirect based on role
                        if (user.getUserType().equalsIgnoreCase("Admin")) {
                            setVisible(false);
                            dispose();
                            new AdminPage(user);
                        } else if (user.getUserType().equalsIgnoreCase("Staff")) {
                            setVisible(false);
                            dispose();
                            new StaffPage(user);
                        } else if (user.getUserType().equalsIgnoreCase("Member") || user.getUserType().equalsIgnoreCase("User")) {
                            setVisible(false);
                            dispose();
                            new MemberPage(user);
                        } else {
                            wrongLogin.setVisible(true);
                        }
                    } else {
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

        // Logo as clickable link back to homepage
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

        // Navigate to account creation
        createButton.addMouseListener(new MouseAdapter() {
            @Override
			public void mouseReleased(MouseEvent e) {
                dispose();
                new AccountCreationPage();
            }
        });
    }
}