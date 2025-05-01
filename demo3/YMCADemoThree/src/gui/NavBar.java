package gui;

import javax.swing.*;
import java.awt.*;

public class NavBar extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NavBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));
        setBackground(new Color(60, 63, 65));  // Dark background

        JButton homeButton = new JButton("Home");
        JButton loginButton = new JButton("Log In");

        homeButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(NavBar.this);
            if (window != null) window.dispose();

            SwingUtilities.invokeLater(() -> {
                HomePage page = new HomePage();
                page.setSize(1280, 720);
                page.setLocationRelativeTo(null);
                page.setVisible(true);
            });
        });

        loginButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(NavBar.this);
            if (window != null) window.dispose();

            SwingUtilities.invokeLater(() -> {
                LoginPage loginPage = new LoginPage();
                loginPage.setSize(1280, 720);
                loginPage.setLocationRelativeTo(null);
                loginPage.setVisible(true);
            });
        });

        add(loginButton);
        add(homeButton);
    }
}
