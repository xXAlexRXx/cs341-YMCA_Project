package gui;

import javax.swing.*;

import model.User;

import java.awt.*;

public class UserNavBar extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User currentUser;

    public UserNavBar(User user) {
        this.currentUser = user;

        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        setBackground(new Color(60, 63, 65));  // Dark background

        JButton logoutButton = new JButton("Logout");
        JButton loginButton = new JButton("Log In");
        JButton accountButton = new JButton("Account");
        JButton inboxButton = new JButton("Inbox");

        logoutButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(UserNavBar.this);
            if (window != null) window.dispose();

            SwingUtilities.invokeLater(() -> {
                HomePage page = new HomePage();
                page.setSize(1280, 720);
                page.setLocationRelativeTo(null);
                page.setVisible(true);
            });
        });

        loginButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(UserNavBar.this);
            if (window != null) window.dispose();

            SwingUtilities.invokeLater(() -> {
                LoginPage loginPage = new LoginPage();
                loginPage.setSize(1280, 720);
                loginPage.setLocationRelativeTo(null);
                loginPage.setVisible(true);
            });
        });

        accountButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(UserNavBar.this);
            if (window != null) window.dispose();

            SwingUtilities.invokeLater(() -> {
                AccountPage accountPage = new AccountPage(currentUser);
                accountPage.setSize(1280, 720);
                accountPage.setLocationRelativeTo(null);
                accountPage.setVisible(true);
            });
        });

        inboxButton.addActionListener(e -> {
            
            SwingUtilities.invokeLater(() -> {
                InboxPage inboxPage = new InboxPage(currentUser);
                inboxPage.setSize(1280, 720);
                inboxPage.setLocationRelativeTo(null);
                inboxPage.setVisible(true);
            });
        });

        add(loginButton);
        add(logoutButton);
        add(accountButton);
        add(inboxButton);
    }
}

