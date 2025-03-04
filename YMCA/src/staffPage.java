import java.awt.*;
import javax.swing.*;

public class staffPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel staffPane;

	/**
	 * Create the frame.
	 */
	public staffPage() {
		JFrame frame = this;
		frame.setSize(1280, 720);
		frame.setLocationRelativeTo(null);

		setResizable(false);
		setTitle("Staff Page");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		staffPane = new JPanel();
		staffPane.setBackground(new Color(49, 49, 49));
		setContentPane(staffPane);
		staffPage();
		staffPane.setLayout(null);
		
		frame.setVisible(true);
	}
	
	public void staffPage() {
		
		JLabel staffPageTitle = new JLabel("Staff Page");
		staffPageTitle.setHorizontalAlignment(SwingConstants.CENTER);
		staffPageTitle.setForeground(new Color(255, 255, 255));
		staffPageTitle.setFont(new Font("Tahoma", Font.PLAIN, 50));
		staffPageTitle.setBounds(471, 110, 270, 93);
		staffPane.add(staffPageTitle);
		
		JButton createProgram = new JButton("Create Program");
		createProgram.setFont(new Font("Tahoma", Font.PLAIN, 25));
		createProgram.setBounds(471, 223, 270, 53);
		staffPane.add(createProgram);
	}
}
