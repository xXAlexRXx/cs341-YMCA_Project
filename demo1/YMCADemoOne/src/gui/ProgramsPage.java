package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import database.DatabaseYMCA;

public class ProgramsPage extends JFrame {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
    private JTable programsTable;

    public ProgramsPage() {
        setTitle("Programs Page");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(49, 49, 49));
        setContentPane(mainPanel);

        // Add NavBar at the top
        NavBar navBar = new NavBar();
        navBar.setBounds(0, 0, 1280, 50);
        mainPanel.add(navBar);

        // Title label for the page
        JLabel titleLabel = new JLabel("Upcoming Programs");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 60, 1280, 50);
        mainPanel.add(titleLabel);

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 130, 1180, 500);
        mainPanel.add(scrollPane);

        // Create the table and enable row sorting
        programsTable = new JTable();
        programsTable.setAutoCreateRowSorter(true);
        scrollPane.setViewportView(programsTable);

        // Load programs from the database
        loadPrograms();

        setVisible(true);
    }

    /**
     * Loads programs from the database and sets a custom renderer for the
     * "description" column so that long descriptions wrap text.
     */
    private void loadPrograms() {
        DatabaseYMCA db = new DatabaseYMCA();
        db.connect();

        try {
            // Append ORDER BY clause to sort by date (assuming column name is "date")
            String query = db.getAllPrograms() + " ORDER BY date ASC";
            ResultSet rs = db.runQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            DefaultTableModel model = new DefaultTableModel();

            // Add column names to the model
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(rsmd.getColumnLabel(i));
            }

            // Add rows to the model
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                model.addRow(rowData);
            }

            rs.close();
            programsTable.setModel(model);

            // Set custom cell renderer for the "description" column to wrap text
            for (int i = 0; i < model.getColumnCount(); i++) {
                if ("description".equalsIgnoreCase(model.getColumnName(i))) {
                    programsTable.getColumnModel().getColumn(i).setCellRenderer(new TextAreaRenderer());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.disconnect();
        }
    }

    /**
     * A custom cell renderer that uses a JTextArea to wrap text.
     */
    class TextAreaRenderer extends JTextArea implements TableCellRenderer {
        /**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);

            // Adjust the row height to fit the content
            if (table.getRowHeight(row) != getPreferredSize().height) {
                table.setRowHeight(row, getPreferredSize().height);
            }

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
    }


}
