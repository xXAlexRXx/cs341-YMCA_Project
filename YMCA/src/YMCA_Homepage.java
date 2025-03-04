import java.awt.*;
import javax.swing.*;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class YMCA_Homepage extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel loginPane;
	private JPasswordField password;
	private JLabel passwordLabel;
	private JLabel usernameLabel;
	private JFormattedTextField username;
	private JButton loginBtn;
	private JLabel passLength;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				JFrame frame = new YMCA_Homepage();
				frame.setSize(1280, 720);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public YMCA_Homepage() {
		
		setResizable(false);
		setTitle("YMCA Homepage");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginPane = new JPanel();
		loginPane.setBackground(new Color(49, 49, 49));
		setContentPane(loginPane);
		loginPane.setLayout(null);
		
		loginPage(loginPane);
	}
	
	public void loginPage(JPanel contentPane) {
		username = new JFormattedTextField();
		username.setFont(new Font("Tahoma", Font.PLAIN, 15));
		username.setBounds(550, 343, 150, 30);
		contentPane.add(username);
		
		password = new JPasswordField();
		password.setFont(new Font("Tahoma", Font.PLAIN, 15));
		password.setBounds(550, 384, 150, 30);
		contentPane.add(password);
		
		loginBtn = new JButton("Login");
		loginBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		loginBtn.setBounds(582, 438, 89, 23);
		contentPane.add(loginBtn);
		loginBtn.setEnabled(false);
		
		usernameLabel = new JLabel("Username");
		usernameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		usernameLabel.setForeground(new Color(255, 255, 255));
		usernameLabel.setBounds(452, 343, 75, 30);
		contentPane.add(usernameLabel);
		
		passwordLabel = new JLabel("Password");
		passwordLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		passwordLabel.setForeground(new Color(255, 255, 255));
		passwordLabel.setBounds(452, 383, 75, 30);
		contentPane.add(passwordLabel);
		
		JLabel welcome = new JLabel("Welcome to");
		welcome.setForeground(new Color(66, 160, 255));
		welcome.setHorizontalAlignment(SwingConstants.TRAILING);
		welcome.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
		welcome.setBounds(10, 129, 644, 93);
		contentPane.add(welcome);
		
		JLabel logo = new JLabel("");
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		Image img = new ImageIcon(YMCA_Homepage.class.getResource("/images/ymca-logo.png")).getImage();
		Image newImg = img.getScaledInstance(250, 200, Image.SCALE_SMOOTH);
		logo.setIcon(new ImageIcon(newImg));
		logo.setBounds(668, 11, 265, 299);
		contentPane.add(logo);
		
		JLabel wrongLogin = new JLabel("*Incorrect Username or Password Input*");
		wrongLogin.setHorizontalAlignment(SwingConstants.CENTER);
		wrongLogin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		wrongLogin.setForeground(new Color(255, 0, 0));
		wrongLogin.setBounds(493, 472, 275, 14);
		contentPane.add(wrongLogin);
		wrongLogin.setVisible(false);
		
		passLength = new JLabel("password must contain at least 8 characters");
		passLength.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passLength.setForeground(new Color(255, 0, 0));
		passLength.setBounds(710, 393, 300, 14);
		contentPane.add(passLength);
		passLength.setVisible(false);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{username, password, loginBtn}));
		
		password.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				char[] input = password.getPassword();
				if (input.length < 7) {
					if (loginBtn.isEnabled()) {
						loginBtn.setEnabled(false);
					}
					passLength.setVisible(true);
				} else {
					if(!loginBtn.isEnabled()) {
						loginBtn.setEnabled(true);
					}
					passLength.setVisible(false);
				}
			}
		});
		
		password.setText("");
		
		loginBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (username.getText().equals("Staff") && loginBtn.isEnabled()) {
					setVisible(false);
					dispose();
					new staffPage();
				} else if ((username.getText().equals("Member") || username.getText().equals("User")) && loginBtn.isEnabled()) {
					setVisible(false);
					dispose();
					new userPage();
				} else {
					if (loginBtn.isEnabled()) {
						System.out.println("Username: " + username.getText() + "; Password: " + password.getPassword() + "; Input: ");
						wrongLogin.setVisible(true);
					}
				}
			}
		});
	}
}
