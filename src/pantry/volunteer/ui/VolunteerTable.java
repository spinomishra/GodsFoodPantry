package pantry.volunteer.ui;

import pantry.JTableEx;
import pantry.helpers.PhoneHelper;
import pantry.volunteer.Volunteer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class extends JTable to support consumer record display
 */
public class VolunteerTable extends JTableEx {
    /**
     * Employee object model
     */
    private VolunteerTableModel volunteerModel;

    /**
     * Table sorting object
     */
    private TableRowSorter<VolunteerTableModel> sorter;

    /**
     * Constructors
     */
    public VolunteerTable(ArrayList<Volunteer> volunteers) {
        super(new VolunteerTableModel(volunteers));

        setColumnWidths();
        createRowSorter();
    }

    /**
     * Sets table column widths
     */
    private void setColumnWidths() {
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        int nCols = getColumnModel().getColumnCount();
        for (int i = 0; i < nCols; i++) {
            var column = getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setMaxWidth(30);
                    break;

                // Name column
                case 1:
                    column.setPreferredWidth(250);
                    break;

                // Phone column
                case 2:
                    column.setMinWidth(120);
                    column.setMaxWidth(120);
                    try {
                        // setting cell editor for phone column
                        MaskFormatter mask = PhoneHelper.getFormatterMask();
                        JFormattedTextField ftext = new JFormattedTextField(mask);
                        column.setCellEditor(new DefaultCellEditor(ftext));
                    } catch (Exception e) {
                        // do nothing
                    }

                    column.setCellRenderer(centerRenderer);
                    break;

                case 4: // checkin column
                case 6:
                    column.setCellRenderer(centerRenderer);
                    break;

                case 5: // checkout column
                    // Allows to add x hours to the checkin time
                    try {
                        MaskFormatter mask = new MaskFormatter("+ ##:## hours");
                        JFormattedTextField fText = new JFormattedTextField(mask);
                        column.setCellEditor(new DefaultCellEditor(fText));
                    } catch (Exception e) {

                    }
                    column.setCellRenderer(centerRenderer);

                    break;
            }
        }
    }

    /**
     * Creates custom sorter for the table with capability to compare phone numbers
     */
    void createRowSorter() {
        VolunteerTableModel model = (VolunteerTableModel) getModel();
        sorter = new TableRowSorter<VolunteerTableModel>(model);

        class PhoneComparator implements Comparator {
            public int compare(Object o1, Object o2) {
                return PhoneHelper.compare((String) o1, (String) o2);
            }

            public boolean equals(Object o2) {
                return this.equals(o2);
            }
        }

        class IndexComparator implements Comparator {
            public int compare(Object o1, Object o2) {
                int idx1 = (int) o1;
                int idx2 = (int) o2;

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
     * Adds a new consumer object to the table model
     *
     * @param v New Volunteer object
     */
    public void add(Volunteer v) {
        VolunteerTableModel model = (VolunteerTableModel) getModel();
        model.addRow(v);
        model.fireTableDataChanged();
    }

    /**
     * Change datamodel associated with table
     *
     * @param newModel new list of volunteer records
     */
    public void ChangeDataModel(ArrayList<Volunteer> newModel) {
        VolunteerTableModel model = (VolunteerTableModel) getModel();
        int nRows = model.getRowCount();
        if (nRows > 0) {
            model.removeRowRange(0, nRows - 1);
        }

        for (Volunteer consumer : newModel) {
            model.addRow(consumer);
        }

        // raise the data table change notification
        model.fireTableDataChanged();
    }

    /**
     * Deleted selected records
     */
    public void deleteSelectedRows() {
        VolunteerTableModel model = (VolunteerTableModel) getModel();
        int[] selectedRows = getSelectedRows();
        if (selectedRows.length > 0) {
            // first raise the data table change notification.. this would give the chance for the original data to be modified
            model.fireTableDataChanged();
            // now delete the data from the table model which is referencing the objects in the original data list.
            model.removeRows(selectedRows);
        }
    }
}
