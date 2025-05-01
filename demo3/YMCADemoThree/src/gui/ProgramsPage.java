package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import database.Database;
import model.Program;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProgramsPage extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
    private JTable programsTable;
    private JButton detailsBtn;
    private ArrayList<Long> pids;
    private Program selectedProgram;
    private User currentUser;

    public ProgramsPage() {
        this(null); // Default constructor for guests
    }

    public ProgramsPage(User user) {
        this.currentUser = user;

        pids = new ArrayList<>();
        selectedProgram = new Program();

        setTitle("Programs Page");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(49, 49, 49));
        setContentPane(mainPanel);

        // ✅ NavBar swap depending on login
        JComponent navBar = (currentUser == null) ? new NavBar() : new UserNavBar(currentUser);
        navBar.setBounds(0, 0, 1280, 50);
        mainPanel.add(navBar);

        detailsBtn = new JButton("More Details");
        detailsBtn.setBounds(115, 80, 120, 23);
        mainPanel.add(detailsBtn);

        JLabel titleLabel = new JLabel("Upcoming Programs");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 60, 1280, 50);
        mainPanel.add(titleLabel);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 130, 1180, 500);
        mainPanel.add(scrollPane);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);

        programsTable = new JTable();
        programsTable.setAutoCreateRowSorter(true);
        programsTable.setShowVerticalLines(false);
        programsTable.setRowHeight(30);
        programsTable.setRowSelectionAllowed(true);

        scrollPane.setViewportView(programsTable);

        loadPrograms();

        // Center each column's text
        for (int i = 0; i < 4; i++) {
            programsTable.getColumnModel().getColumn(i).setCellRenderer(centerRender);
        }

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

    private void showProgramDetails(Program p) {
        Box b = Box.createVerticalBox();
        JDialog jd = new JDialog();
        JLabel name, desc, cap, sdate, edate, time, mprice, nmprice;

        jd.setTitle("Program Details");
        b.setAlignmentY(Component.CENTER_ALIGNMENT);

        name = new JLabel(p.getProgramName());
        desc = new JLabel(p.getDescription());
        cap = new JLabel((p.getCapacity() - p.getCurrentCapacity()) + " Spots left");
        sdate = new JLabel("Program Starts: " + p.getStartDate().toString());
        edate = new JLabel("Program Ends: " + p.getEndDate().toString());
        time = new JLabel(p.getStartTime().toString() + " - " + p.getEndTime().toString());

        // ✅ Personalized pricing
        if (currentUser != null && currentUser.getUserType().equalsIgnoreCase("Member")) {
            mprice = new JLabel(String.format("Your Price: $%.2f", p.getPrice() / 2));
            nmprice = new JLabel(String.format("Full Price: $%.2f", p.getPrice()));
        } else {
            mprice = new JLabel(String.format("Member Price: $%.2f", p.getPrice() / 2));
            nmprice = new JLabel(String.format("Non-Member Price: $%.2f", p.getPrice()));
        }

        name.setFont(new Font("Verdana", Font.PLAIN, 50));
        desc.setFont(new Font("Verdana", Font.PLAIN, 20));
        cap.setFont(new Font("Verdana", Font.PLAIN, 15));
        sdate.setFont(new Font("Verdana", Font.PLAIN, 15));
        edate.setFont(new Font("Verdana", Font.PLAIN, 15));
        time.setFont(new Font("Verdana", Font.PLAIN, 15));
        mprice.setFont(new Font("Verdana", Font.PLAIN, 15));
        nmprice.setFont(new Font("Verdana", Font.PLAIN, 15));

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
        b.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        jd.setVisible(true);
    }
}
