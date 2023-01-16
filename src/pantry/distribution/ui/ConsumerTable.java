package pantry.distribution.ui;

import pantry.JTableEx;
import pantry.distribution.Consumer;
import pantry.helpers.PhoneHelper;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.MaskFormatter;
import java.util.ArrayList;

/**
 * This class extends JTable to support consumer record display
 */
public class ConsumerTable extends JTableEx {
    /**
     * Employee object model
     */
    private final ConsumerTableModel consumerModel;

    /**
     * Reference to today's recorded consumers
     */
    private final ArrayList<Consumer> consumers;

    /**
     * Constructors
     */
    public ConsumerTable(ArrayList<Consumer> consumers) {
        super(new ConsumerTableModel());
        this.consumers = consumers;

        consumerModel = (ConsumerTableModel) this.getModel();
        if (consumers != null) {
            for (Consumer consumer : consumers) {
                consumerModel.addRow(consumer);
            }
        }

        setColumnWidths();
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
                case 1:
                    column.setPreferredWidth(250);
                    break;
                case 2:
                    column.setPreferredWidth(90);
                    // setting cell editor for DOB column
                    try {
                        MaskFormatter mask = new MaskFormatter("##/##/####");
                        JFormattedTextField fText = new JFormattedTextField(mask);
                        column.setCellEditor(new DefaultCellEditor(fText));
                    } catch (Exception e) {

                    }
                    column.setCellRenderer(centerRenderer);

                    break;

                case 3:
                    column.setMinWidth(120);
                    column.setMaxWidth(120);

                    // setting cell editor for phone column
                    try {
                        MaskFormatter mask = PhoneHelper.getFormatterMask();
                        JFormattedTextField ftext = new JFormattedTextField(mask);
                        column.setCellEditor(new DefaultCellEditor(ftext));
                    } catch (Exception e) {

                    }
                    column.setCellRenderer(centerRenderer);
                    break;

                case 4:
                    column.setMinWidth(300);
                    break;

                case 5:
                    column.setMaxWidth(100);
                    column.setPreferredWidth(100);
                    column.setCellRenderer(centerRenderer);
                    break;

                case 6:
                    column.setPreferredWidth(120);
                    column.setCellRenderer(centerRenderer);
                    break;

                case 7:
                case 8:
                    column.setPreferredWidth(90);
                    column.setCellRenderer(centerRenderer);
                    break;

                case 9:
                case 10:
                    column.setMinWidth(70);
                    column.setMaxWidth(70);
                    column.setCellRenderer(centerRenderer);
                    break;
            }
        }
    }

    /**
     * Adds a new consumer object to the table model
     *
     * @param consumer New consumer object
     */
    public void add(Consumer consumer) {
        consumerModel.addRow(consumer);
        consumerModel.fireTableDataChanged();
    }

    /**
     * Change datamodel associated with table
     *
     * @param oldConsumers list of consumer records
     */
    public void ChangeDataModel(ArrayList<Consumer> oldConsumers) {
        int nRows = consumerModel.getRowCount();
        if (nRows > 0) {
            consumerModel.removeRowRange(0, nRows - 1);
        }

        for (Consumer consumer : oldConsumers) {
            consumerModel.addRow(consumer);
        }

        // raise the data table change notification
        consumerModel.fireTableDataChanged();
    }

    /**
     * Delete Selected Rows
     */
    public void deleteSelectedRows() {
        int[] selectedRows = getSelectedRows();
        if (selectedRows.length > 0) {
            consumerModel.removeRows(selectedRows);

            // raise the data table change notification
            consumerModel.fireTableDataChanged();
        }
    }
}
