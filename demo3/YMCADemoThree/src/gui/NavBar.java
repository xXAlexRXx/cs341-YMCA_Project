package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * NavBar is a reusable top navigation component used across various pages.
 * It provides quick links to the Home page and Login page.
 */
public class NavBar extends JPanel {
	private static final long serialVersionUID = 1L;

	public NavBar() {
        // Load YMCA logo and scale it for navbar
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/ymca-logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        add(logoLabel);

        // Set layout and background color
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        setBackground(new Color(60, 63, 65));

        // Create Home and Login buttons
        JButton homeButton = new JButton("Home");
        JButton loginButton = new JButton("Log In");

        // Navigate to Home page
        homeButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(NavBar.this);
            if (window != null) {
				window.dispose();
			}

            SwingUtilities.invokeLater(() -> {
                HomePage page = new HomePage();
                page.setSize(1280, 720);
                page.setLocationRelativeTo(null);
                page.setVisible(true);
            });
        });

        // Navigate to Login page
        loginButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(NavBar.this);
            if (window != null) {
				window.dispose();
			}

            SwingUtilities.invokeLater(() -> {
                LoginPage loginPage = new LoginPage();
                loginPage.setSize(1280, 720);
                loginPage.setLocationRelativeTo(null);
                loginPage.setVisible(true);
            });
        });

        // Add buttons to navbar
        add(loginButton);
        add(homeButton);
    }
}
