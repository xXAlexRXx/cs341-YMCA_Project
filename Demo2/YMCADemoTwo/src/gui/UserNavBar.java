package gui;

import javax.swing.*;

import model.User;

import java.awt.*;
import java.awt.event.*;

public class UserNavBar extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User currentUser;
	public UserNavBar(User user) {
		currentUser = user;
        // Use a layout manager to automatically align components.
        // Here we use FlowLayout with left alignment.
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        setBackground(new Color(60, 63, 65));  // A dark background color
        
        // Create navigation buttons
        
//        JLabel homeLogo = new JLabel("");
//        homeLogo.setHorizontalAlignment(SwingConstants.CENTER);
//        Image img = new ImageIcon(LoginPage.class.getResource("/images/ymca-logo.png")).getImage();
//        Image newImg = img.getScaledInstance(60, 48, Image.SCALE_SMOOTH);
//        homeLogo.setIcon(new ImageIcon(newImg));
        
        JButton logoutButton = new JButton("Logout");
        JButton loginButton = new JButton("Log In");
        JButton accountButton = new JButton("Account");
        
        
        // Optionally, add action listeners to these buttons to perform navigation.
        // For example:
        // homeButton.addActionListener(e -> { /* navigate to home page */ });
        // logoutButton.addActionListener(e -> { /* log out action */ });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Dispose the current window (ending the current session)
                Window window = SwingUtilities.getWindowAncestor(UserNavBar.this);
                if (window != null) {
                    window.dispose();
                }
                
                // Schedule creation and display of the login screen on the EDT
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                    HomePage page = new HomePage();
                page.setSize(1280, 720);
                page.setLocationRelativeTo(null);
                page.setVisible(true);
                    }
                });
            }
        });
        
//        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Dispose the current window (ending the current session)
                Window window = SwingUtilities.getWindowAncestor(UserNavBar.this);
                if (window != null) {
                    window.dispose();
                }
                
                // Schedule creation and display of the login screen on the EDT
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        LoginPage loginPage = new LoginPage();
                        loginPage.setSize(1280, 720);
                        loginPage.setLocationRelativeTo(null);
                        loginPage.setVisible(true);
                    }
                });
            }
        });
        
        accountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Dispose the current window (ending the current session)
                Window window = SwingUtilities.getWindowAncestor(UserNavBar.this);
                if (window != null) {
                    window.dispose();
                }
                
                // Schedule creation and display of the login screen on the EDT
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        AccountPage accountPage = new AccountPage(currentUser);
                        accountPage.setSize(1280, 720);
                        accountPage.setLocationRelativeTo(null);
                        accountPage.setVisible(true);
                    }
                });
            }
        });
        
        
        
     
        
        
        
        // Add buttons to the nav bar
        add(loginButton);
        add(logoutButton);
        add(accountButton);
    }
}
