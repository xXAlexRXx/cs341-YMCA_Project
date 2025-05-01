package gui;

import java.awt.*;
import javax.swing.*;

import database.Database;
import model.User;
import security.PasswordHash;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        loginPane = new JPanel();
        loginPane.setBackground(new Color(49, 49, 49));
        setContentPane(loginPane);
        loginPane.setLayout(null);

        initializeComponents(loginPane);
        
        setSize(1280, 720);      // <<< ADD THIS
        setLocationRelativeTo(null);  // <<< ADD THIS to center it on screen
        setVisible(true);
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
        welcome = new JLabel("Welcome to");
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
        wrongLogin = new JLabel("*Incorrect Username or Password Input*");
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
                
                String encrypted = PasswordHash.encrypt(passText);

                // Create an instance of your DatabaseYMCA
                Database db = new Database();
                try {
                    db.connect();
                    // Attempt to retrieve a user matching the given credentials
                    User user = db.getUserByUsernameAndPassword(userText, encrypted);
                    
                    
                    
                    if (user != null) {
                    	if (user.getStatus() != null && user.getStatus().equalsIgnoreCase("suspended")) {
                            JOptionPane.showMessageDialog(null, "Your account is suspended.");
                            return;
                        }
                    	
                        // Successful login - check user type and redirect accordingly
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
        	public void mouseExited(MouseEvent e) {
        		logo.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        	}
        	public void mouseReleased(MouseEvent e) {
        		dispose();
        		new HomePage();
        	}
        });
    }
}
