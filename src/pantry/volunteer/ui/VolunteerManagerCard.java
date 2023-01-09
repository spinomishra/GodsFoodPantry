package pantry.volunteer.ui;

import pantry.Home;
import pantry.Pantry;
import pantry.data.PantryData;
import pantry.helpers.DateHelper;
import pantry.helpers.PrintHelper;
import pantry.interfaces.ITableSelectionChangeListener;
import pantry.volunteer.Volunteer;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Volunteer management card
 */
public class VolunteerManagerCard extends JPanel implements  ActionListener, ITableSelectionChangeListener, TableModelListener {
    /**
     * Remove button label
     */
    private static final String removeLabel = "Delete";

    /**
     * Remove button control
     */
    private final JButton removeButton;

    /**
     * Volunteer records table control
     */
    private VolunteerTable volunteerTable ;

    /**
     * Record choice combo box
     */
    JComboBox comboBox;

    /**
     * Filter text control
     */
    JTextField searchNameField;

    /**
     * Title of the volunteer page
     */
    public static final String Title = "Volunteer Manager";

    /**
     * Constructor
     * @param parentWindow The Parent window
     */
    public VolunteerManagerCard(JFrame parentWindow) {
        super(new BorderLayout());
        setOpaque(true);

        var volunteers = Pantry.getInstance().get_Data().get_Volunteers();

        {
            JPanel panel = new JPanel();
            JLabel label = new JLabel("Show Volunteer Records");

            Font labelFont = new Font("Serif", Font.BOLD|Font.ITALIC, 14);
            label.setFont(labelFont);
            label.setHorizontalTextPosition(JLabel.CENTER);
            panel.add(label, BorderLayout.CENTER);
            String[] recordChoices = {"All", "Today's", "Last 30 days"};
            comboBox = new JComboBox(recordChoices);
            comboBox.setSelectedIndex(0);
            comboBox.setMaximumSize(new Dimension(200, 80));
            comboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeButton.setEnabled(false);
                    // apply filter
                    // https://stackoverflow.com/questions/56119820/jtable-date-filter-not-working-the-way-it-should
                    switch (comboBox.getSelectedIndex()){
                        case 0:
                            // all days
                            var sorter = (TableRowSorter<VolunteerTableModel>)volunteerTable.getRowSorter();
                            sorter.setRowFilter(null);
                            break ;

                        case 1: {
                            // filter all records for last 1 day
                            Date fromDate =  DateHelper.fromLocalDateTime(LocalDate.now().atStartOfDay().minusDays(1));
                            Date toDate = DateHelper.fromLocalDateTime(LocalDateTime.now());

                            filterDates(fromDate, toDate);
                        }
                        break ;

                        case 2: {
                            // filter all records for last 30 days
                            Date fromDate =  DateHelper.fromLocalDateTime(LocalDate.now().atStartOfDay().minusDays(30));
                            Date toDate = DateHelper.fromLocalDateTime(LocalDateTime.now());

                            filterDates(fromDate, toDate);
                        }
                        break ;
                    }
                }
            });
            panel.add(comboBox, BorderLayout.SOUTH);
            add(panel, BorderLayout.NORTH);
        }

        //Create the table and put it in a scroll pane and add scroll panel to the card
        {
            volunteerTable = new VolunteerTable(volunteers);
            JScrollPane scrollPane = new JScrollPane(volunteerTable,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            volunteerTable.setFillsViewportHeight(true);
            volunteerTable.addSelectionChangeListener(this);
            volunteerTable.getModel().addTableModelListener(this);
            add(scrollPane, BorderLayout.CENTER);
        }

        { // Action buttons

            //Create a panel that uses BoxLayout.
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));

            removeButton = new JButton(removeLabel);
            removeButton.setActionCommand(removeLabel);
            removeButton.addActionListener(this);
            removeButton.setEnabled(false);
            var removeButtonIcon = getImageIcon("/images/recyclebin.png", 12, 12);
            removeButton.setIcon(removeButtonIcon);
            removeButton.setEnabled(false);
            buttonPane.add(removeButton);

            buttonPane.add(Box.createHorizontalStrut(5));
            buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
            buttonPane.add(Box.createHorizontalStrut(5));
            buttonPane.add(new JLabel(" Search : "));
            buttonPane.add(Box.createRigidArea(new Dimension(5, 0)));
            searchNameField = new JTextField();
            searchNameField.setPreferredSize(new Dimension(100, 30));
            searchNameField.getDocument().addDocumentListener(
                    new DocumentListener() {
                        public void changedUpdate(DocumentEvent e) {
                            filterByName();
                        }
                        public void insertUpdate(DocumentEvent e) {
                            filterByName();
                        }
                        public void removeUpdate(DocumentEvent e) {
                            filterByName();
                        }
                    });
            buttonPane.add(searchNameField);
            buttonPane.add(Box.createRigidArea(new Dimension(5, 0)));
            JButton printButton = new JButton("Print Report");
            printButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PrintHelper.Print(parentWindow, volunteerTable, Home.getPantryName() + " - Volunteer Report");
                }
            });
            buttonPane.add(printButton);
            buttonPane.add(Box.createRigidArea(new Dimension(5, 0)));

            buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            add(buttonPane, BorderLayout.PAGE_END);
        }
    }

    /**
     * Update the row filter regular expression from the expression in the text box
     */
    private void filterByName() {
        // For more info on sorter filter see -
        // https://docs.oracle.com/javase/7/docs/api/javax/swing/RowFilter.html#regexFilter(java.lang.String,%20int...)
        RowFilter<VolunteerTableModel, Object> rf = null;

        //If current expression doesn't parse, don't update.
        try {
            // (?i) is case-insensitive flag for regular expression
            // See https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html#CASE_INSENSITIVE
            rf = RowFilter.regexFilter("(?i)"+searchNameField.getText());
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }

        var sorter = (TableRowSorter<VolunteerTableModel>)this.volunteerTable.getRowSorter();
        sorter.setRowFilter(rf);
    }

    /**
     * Update the table view based on the dates based on filtering
     */
    private void filterDates(Date from, Date to){
        ArrayList<RowFilter<Object,Object>> filters = new ArrayList<RowFilter<Object,Object>>(2);
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, from, 4,5 ));
        filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, to,4,5));
        RowFilter<Object,Object> dateFilter = RowFilter.andFilter(filters);

        var sorter = (TableRowSorter<VolunteerTableModel>)this.volunteerTable.getRowSorter();
        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == removeButton) {
            //This method can be called only if
            //there's a valid selection so go
            //ahead and remove whatever is selected.
            volunteerTable.deleteSelectedRows();
            removeButton.setEnabled(false);
        }
    }

    /**
     * Helper method to construct image icon using the given image path
     * @param imagePath The image path
     * @param w image icon width
     * @param h image icon height
     * @return ImageIcon object
     */
    private  ImageIcon getImageIcon(String imagePath, int w, int h)
    {
        var resource = getClass().getResource(imagePath);
        if (resource != null) {
            var img = ((new ImageIcon(resource)).getImage()).getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }

    /**
     * Volunteer Table selection change listener
     * @param table Table raising the event
     * @param row selected row
     * @param col selected col
     */
    @Override
    public void SelectionChanged(JTable table, int row, int col) {
        removeButton.setEnabled(true);
    }


    /**
     * Table model listener method implementation
     * @param e a {@code TableModelEvent} to notify listener that a table model
     *          has changed
     */
    @Override
    public void tableChanged(TableModelEvent e) {
        int firstRow = e.getFirstRow();
        int lastRow = e.getLastRow();
        int index = e.getColumn();

        switch (e.getType()){
            case TableModelEvent.DELETE:
            {
                var volunteers = Pantry.getInstance().get_Data().get_Volunteers();
                VolunteerTableModel model = (VolunteerTableModel)e.getSource();
                if (firstRow < model.getRowCount()) {
                    Volunteer v = model.getRow(firstRow);
                    volunteers.remove(v);
                }
                // save the records
                Pantry.getInstance().get_Data().Save();
            }
            break ;
        }
    }
}
