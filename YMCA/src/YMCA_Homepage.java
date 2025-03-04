import java.awt.*;
import javax.swing.*;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class YMCA_Homepage extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPasswordField password;
	private JLabel passwordLabel;
	private JLabel usernameLabel;

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
		contentPane = new JPanel();
		contentPane.setBackground(new Color(49, 49, 49));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JFormattedTextField username = new JFormattedTextField();
		username.setFont(new Font("Tahoma", Font.PLAIN, 15));
		username.setBounds(550, 343, 150, 30);
		contentPane.add(username);
		
		password = new JPasswordField();
		password.setFont(new Font("Tahoma", Font.PLAIN, 15));
		password.setBounds(550, 384, 150, 30);
		contentPane.add(password);
		
		JButton loginBtn = new JButton("Login");
		loginBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		loginBtn.setBounds(582, 438, 89, 23);
		contentPane.add(loginBtn);
		
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
		
		JLabel welcome = new JLabel("Welcome to the YMCA!");
		welcome.setForeground(new Color(255, 255, 255));
		welcome.setHorizontalAlignment(SwingConstants.CENTER);
		welcome.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 50));
		welcome.setBounds(0, 99, 1264, 93);
		contentPane.add(welcome);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{username, password, loginBtn}));
		
		loginBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		
	}
	
}
