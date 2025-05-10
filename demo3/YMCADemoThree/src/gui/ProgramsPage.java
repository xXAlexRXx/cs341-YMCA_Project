package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import database.Database;
import model.Program;
import model.User;

/**
 * ProgramsPage displays a list of YMCA programs and allows users (or guests)
 * to view additional details about each program.
 */
public class ProgramsPage extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
    private JTable programsTable;
    private JButton detailsBtn;
    private ArrayList<Long> pids;
    private Program selectedProgram;
    private User currentUser;

    public ProgramsPage() {
        this(null); // Load in guest mode if no user
    }

    public ProgramsPage(User user) {
        this.currentUser = user;

        pids = new ArrayList<>(); // program ID tracker for detail access
        selectedProgram = new Program();

        setTitle("Programs Page");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(49, 49, 49));
        setContentPane(mainPanel);

        // Use correct navbar based on login state
        JComponent navBar = (currentUser == null) ? new NavBar() : new UserNavBar(currentUser);
        navBar.setBounds(0, 0, 1280, 50);
        mainPanel.add(navBar);

        // Button to view more information about selected program
        detailsBtn = new JButton("More Details");
        detailsBtn.setBounds(115, 80, 120, 23);
        mainPanel.add(detailsBtn);

        // Title label
        JLabel titleLabel = new JLabel("Upcoming Programs");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 60, 1280, 50);
        mainPanel.add(titleLabel);

        // Scrollable table for program listing
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 130, 1180, 500);
        mainPanel.add(scrollPane);

        // Center-align table content
        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);

        programsTable = new JTable();
        programsTable.setAutoCreateRowSorter(true);
        programsTable.setShowVerticalLines(false);
        programsTable.setRowHeight(30);
        programsTable.setRowSelectionAllowed(true);
        scrollPane.setViewportView(programsTable);

        loadPrograms();

        // Apply centered rendering to first 4 columns
        for (int i = 0; i < 4; i++) {
            programsTable.getColumnModel().getColumn(i).setCellRenderer(centerRender);
        }

        // Event listener for "More Details" button
        detailsBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = programsTable.getSelectedRow();
                if (row >= 0) {
                    Database db = new Database();
                    try {
                        db.connect();
                        selectedProgram = db.getProgramByID(pids.get(row));
                        db.disconnect();
                        showProgramDetails(selectedProgram);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        setVisible(true);
    }

    /**
     * Queries and loads all programs from the database and fills the table.
     */
    private void loadPrograms() {
        Database db = new Database();
        try {
            db.connect();
            String query = "SELECT program_name, start_date, end_date, price, program_id " +
                           "FROM Program ORDER BY start_date ASC";
            ResultSet rs = db.runQuery(query);
            int columnCount = 4;
            DefaultTableModel model = new DefaultTableModel();

            model.addColumn("Course");
            model.addColumn("Start Date");
            model.addColumn("End Date");
            model.addColumn("Price");

            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                pids.add(rs.getLong("program_id"));
                model.addRow(rowData);
            }

            rs.close();
            programsTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.disconnect();
        }
    }

    /**
     * Displays a dialog showing detailed information about a selected program.
     */
    private void showProgramDetails(Program p) {
        Box b = Box.createVerticalBox();
        JDialog jd = new JDialog();
        JLabel name, desc, cap, sdate, edate, time, mprice, nmprice;

        jd.setTitle("Program Details");
        b.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Program detail labels
        name = new JLabel(p.getProgramName());
        desc = new JLabel(p.getDescription());
        cap = new JLabel((p.getCapacity() - p.getCurrentCapacity()) + " Spots left");
        sdate = new JLabel("Program Starts: " + p.getStartDate().toString());
        edate = new JLabel("Program Ends: " + p.getEndDate().toString());
        time = new JLabel(p.getStartTime().toString() + " - " + p.getEndTime().toString());

        // Conditional member pricing
        if (currentUser != null && currentUser.getUserType().equalsIgnoreCase("Member")) {
            mprice = new JLabel(String.format("Your Price: $%.2f", p.getPrice() / 2));
            nmprice = new JLabel(String.format("Full Price: $%.2f", p.getPrice()));
        } else {
            mprice = new JLabel(String.format("Member Price: $%.2f", p.getPrice() / 2));
            nmprice = new JLabel(String.format("Non-Member Price: $%.2f", p.getPrice()));
        }

        // Font styling for detail dialog
        name.setFont(new Font("Verdana", Font.PLAIN, 50));
        desc.setFont(new Font("Verdana", Font.PLAIN, 20));
        cap.setFont(new Font("Verdana", Font.PLAIN, 15));
        sdate.setFont(new Font("Verdana", Font.PLAIN, 15));
        edate.setFont(new Font("Verdana", Font.PLAIN, 15));
        time.setFont(new Font("Verdana", Font.PLAIN, 15));
        mprice.setFont(new Font("Verdana", Font.PLAIN, 15));
        nmprice.setFont(new Font("Verdana", Font.PLAIN, 15));

        // Add all labels to vertical box
        b.add(name);
        b.add(Box.createVerticalStrut(10));
        b.add(desc);
        b.add(Box.createVerticalStrut(5));
        b.add(cap);
        b.add(Box.createVerticalStrut(5));
        b.add(sdate);
        b.add(Box.createVerticalStrut(5));
        b.add(edate);
        b.add(Box.createVerticalStrut(5));
        b.add(time);
        b.add(Box.createVerticalStrut(5));
        b.add(mprice);
        b.add(Box.createVerticalStrut(5));
        b.add(nmprice);

        jd.setLocationRelativeTo(mainPanel);
        jd.getContentPane().add(b);
        jd.pack();
        b.setAlignmentY(Component.CENTER_ALIGNMENT);
        jd.setVisible(true);
    }
}
