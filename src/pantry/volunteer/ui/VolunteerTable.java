package pantry.volunteer.ui;

import pantry.distribution.Consumer;
import pantry.distribution.ui.ConsumerTableModel;
import pantry.helpers.PhoneHelper;
import pantry.interfaces.ITableSelectionChangeListener;
import pantry.volunteer.Volunteer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class extends JTable to support consumer record display
 */
public class VolunteerTable extends JTable implements ListSelectionListener{
    /**
     * Employee object model
     */
    private VolunteerTableModel volunteerModel;

    /**
     * Color for selected row
     */
    private static final Color selectedRowColor = new Color(110, 181, 155);

    /**
     * Selection Change listener
     */
    private ITableSelectionChangeListener selectionChangeListener;

    /**
     * Constructors
     */
    public VolunteerTable(ArrayList<Volunteer> volunteers){
        super (new VolunteerTableModel(volunteers));

        setCellSelectionEnabled(false);
        setRowSelectionAllowed(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowHeight(40);

        setSelectionBackground(selectedRowColor);
        setSelectionForeground(Color.WHITE);

        var selectionModel = this.getSelectionModel();
        selectionModel.addListSelectionListener(this);

        setColumnWidths();
        createRowSorter() ;

        // setting cell editor for phone column
        JFormattedTextField ftext = new JFormattedTextField();
        MaskFormatter mask;
        try {
            mask = new MaskFormatter("(###)###-####");
            mask.install(ftext);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(ftext));
    }

    /**
     * Sets table column widths
     */
    private void setColumnWidths() {
        setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );

        int nCols =  getColumnModel().getColumnCount();
        for (int i = 0; i < nCols; i++) {
            var column = getColumnModel().getColumn(i);
            switch (i) {
                case 0: column.setMaxWidth(30);
                        break;

                case 1: column.setPreferredWidth(250); break ;
                case 2: column.setMinWidth(120);
                        column.setMaxWidth(120);
                        column.setCellRenderer(centerRenderer);
                        break ;

                case 4:
                case 5:

                case 6:
                        column.setCellRenderer(centerRenderer);
                        break ;
            }
        }
    }

    /**
     * Creates custom sorter for the table with capability to compare phone numbers
     */
    void createRowSorter(){
        TableRowSorter sorter = new TableRowSorter(getModel());

        class PhoneComparator implements Comparator {
            public int compare(Object o1, Object o2) {
                return PhoneHelper.compare((String)o1, (String)o2);
            }

            public boolean equals(Object o2) {
                return this.equals(o2);
            }
        }

        class IndexComparator implements Comparator {
            public int compare(Object o1, Object o2) {
                int idx1 = (int)o1;
                int idx2 = (int)o2;

                return Integer.compare(idx1, idx2);
            }

            public boolean equals(Object o2) {
                return this.equals(o2);
            }
        }

        sorter.setComparator(0, new IndexComparator());
        sorter.setComparator(2, new PhoneComparator());

        sorter.setSortable(3, false);   // address column is not sortable
        sorter.setSortable(6, false);   // Activity column is not sortable

        setRowSorter(sorter);
        setAutoCreateRowSorter(false);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
        Component comp = super.prepareRenderer(renderer, row, column);
        if (row != -1 && row ==  getSelectedRow())
        {
            comp.setBackground(selectedRowColor);
            comp.setForeground(Color.WHITE);
        }
        else {
            Color c = (row % 2 == 0 ? new Color(210, 231, 255) : Color.WHITE);
            comp.setBackground(c);
            comp.setForeground(Color.BLACK);
        }
        return comp;
    }

    /**
     * When selection changes, enable button to remove employees data from records
     * @param e list selection change event
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == this.getSelectionModel()) {
            int selectedRowIndex = getSelectedRow();

            if (selectedRowIndex != -1)
            {
                // Following 2 lines of code allows me to highlight selected row using selected row Color
                addRowSelectionInterval(selectedRowIndex, selectedRowIndex);
                setColumnSelectionInterval(0, this.getColumnCount()-1);
                if (selectionChangeListener != null)
                    selectionChangeListener.SelectionChanged(this, selectedRowIndex, 0);
            }
        }
    }

    /**
     * Adds a new consumer object to the table model
     * @param v New Volunteer object
     */
    public void add(Volunteer v)
    {
        VolunteerTableModel model = (VolunteerTableModel)getModel();
        model.addRow(v);
        model.fireTableDataChanged();
    }

    /**
     * Change datamodel associated with table
     * @param newModel new list of volunteer records
     */
    public void ChangeDataModel(ArrayList<Volunteer> newModel) {
        VolunteerTableModel model = (VolunteerTableModel)getModel();
        int nRows = model.getRowCount();
        if(nRows > 0) {
            model.removeRowRange(0, nRows - 1);
        }

        for (Volunteer consumer : newModel) {
            model.addRow(consumer);
        }

        // raise the data table change notification
        model.fireTableDataChanged();
    }

    /**
     * Adds selection change listener object
     * @param listener The listener object
     */
    public void addSelectionChangeListener(ITableSelectionChangeListener listener){
        selectionChangeListener = listener;
    }

    /**
     * Deleted selected records
     */
    public void deleteSelectedRows() {
        VolunteerTableModel model = (VolunteerTableModel)getModel();
        int[] selectedRows = getSelectedRows();
        if (selectedRows.length > 0){
            // first raise the data table change notification.. this would give the chance for the original data to be modified
            model.fireTableDataChanged();
            // now delete the data from the table model which is referencing the objects in the original data list.
            model.removeRows(selectedRows);
        }
    }
}
