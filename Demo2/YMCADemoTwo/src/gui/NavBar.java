package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NavBar extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NavBar() {
        // Use a layout manager to automatically align components.
        // Here we use FlowLayout with left alignment.
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        setBackground(new Color(60, 63, 65));  // A dark background color
        
        // Create navigation buttons
        
       
        
        JButton logoutButton = new JButton("Logout");
        JButton loginButton = new JButton("Log In");
        
        
        // Optionally, add action listeners to these buttons to perform navigation.
        // For example:
        // homeButton.addActionListener(e -> { /* navigate to home page */ });
        // logoutButton.addActionListener(e -> { /* log out action */ });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Dispose the current window (ending the current session)
                Window window = SwingUtilities.getWindowAncestor(NavBar.this);
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
                Window window = SwingUtilities.getWindowAncestor(NavBar.this);
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
        
     
        
        
        
        // Add buttons to the nav bar
        add(loginButton);
        add(logoutButton);
        //add(homeLogo);
    }
}
