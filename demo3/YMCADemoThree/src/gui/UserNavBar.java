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

import model.User;

public class UserNavBar extends JPanel {
	private static final long serialVersionUID = 1L;
	private User currentUser;

    public UserNavBar(User user) {
        // Load and scale YMCA logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/ymca-logo.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        add(logoLabel);

        this.currentUser = user;

        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        setBackground(new Color(60, 63, 65));  // Dark background

        JButton logoutButton = new JButton("Logout");
        JButton loginButton = new JButton("Log In");
        JButton accountButton = new JButton("Account");
        JButton inboxButton = new JButton("Inbox");

        // Logout returns to home
        logoutButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(UserNavBar.this);
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

        // Login page
        loginButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(UserNavBar.this);
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

        // User account page
        accountButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(UserNavBar.this);
            if (window != null) {
				window.dispose();
			}
            SwingUtilities.invokeLater(() -> {
                AccountPage accountPage = new AccountPage(currentUser);
                accountPage.setSize(1280, 720);
                accountPage.setLocationRelativeTo(null);
                accountPage.setVisible(true);
            });
        });

        // Inbox page
        inboxButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                InboxPage inboxPage = new InboxPage(currentUser);
                inboxPage.setSize(1280, 720);
                inboxPage.setLocationRelativeTo(null);
                inboxPage.setVisible(true);
            });
        });

        // Add buttons
        add(loginButton);
        add(logoutButton);
        add(accountButton);
        add(inboxButton);
    }
}
