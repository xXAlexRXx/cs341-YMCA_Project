import java.awt.*;
import javax.swing.*;

public class userPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel userPane;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public userPage() {
		JFrame frame = this;
		frame.setSize(1280, 720);
		frame.setLocationRelativeTo(null);

		setResizable(false);
		setTitle("User Page");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		userPane = new JPanel();
		userPane.setBackground(new Color(49, 49, 49));
		setContentPane(userPane);
		userPage();
		userPane.setLayout(null);
		
		frame.setVisible(true);
	}
	
	public void userPage() {
		
		JLabel userPageTitle = new JLabel("User Page");
		userPageTitle.setHorizontalAlignment(SwingConstants.CENTER);
		userPageTitle.setForeground(new Color(255, 255, 255));
		userPageTitle.setFont(new Font("Tahoma", Font.PLAIN, 50));
		userPageTitle.setBounds(471, 110, 270, 93);
		userPane.add(userPageTitle);
		
		JButton createProgram = new JButton("Join Program");
		createProgram.setFont(new Font("Tahoma", Font.PLAIN, 25));
		createProgram.setBounds(471, 223, 270, 53);
		userPane.add(createProgram);
	}
}
