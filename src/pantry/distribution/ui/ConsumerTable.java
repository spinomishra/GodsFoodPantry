package pantry.distribution.ui;

import pantry.distribution.Consumer;
import pantry.interfaces.ITableSelectionChangeListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class extends JTable to support consumer record display
 */
public class ConsumerTable extends JTable implements ListSelectionListener, TableModelListener {
    /**
     * Employee object model
     */
    private ConsumerTableModel consumerModel;

    /**
     * Signature display control
     */
    private JLabel signControl ;

    /**
     * Color for selected row
     */
    private static final Color selectedRowColor = new Color(110, 181, 155);

    /**
     * Selection Change listener
     */
    private ITableSelectionChangeListener selectionChangeListener;

    /**
     * Reference to today's recorded consumers
     */
    private ArrayList<Consumer> consumers;

    /**
     * Constructors
     */
    public ConsumerTable(ArrayList<Consumer> consumers){
        super (new ConsumerTableModel());
        this.consumers = consumers;

        setRowSelectionAllowed(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowHeight(40);

        consumerModel = (ConsumerTableModel) this.getModel();
        if (consumers != null) {
            for (Consumer consumer : consumers) {
                consumerModel.addRow(consumer);
            }
        }

        setSelectionBackground(selectedRowColor);
        setSelectionForeground(Color.WHITE);

        var selectionModel = this.getSelectionModel();
        selectionModel.addListSelectionListener(this);

        setColumnWidths();
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
                case 0: column.setMaxWidth(30); break;
                case 1: column.setPreferredWidth(250); break ;
                case 2: column.setMinWidth(120);
                        column.setMaxWidth(120);
                        column.setCellRenderer(centerRenderer);
                        break ;

                case 3: column.setMinWidth(300);
                        break;

                case 4: column.setMaxWidth(100);
                        column.setPreferredWidth(100);
                        column.setCellRenderer(centerRenderer);
                        break ;

                case 5: column.setPreferredWidth(120);
                        column.setCellRenderer(centerRenderer);
                        break ;

                case 6:
                case 7: column.setPreferredWidth(90);
                        column.setCellRenderer(centerRenderer);
                        break ;

                case 8:
                case 9: column.setMinWidth(70);
                        column.setMaxWidth(70);
                        column.setCellRenderer(centerRenderer);
                        break ;
            }
        }
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
     * {@inheritDoc}
     * When selection changes, enable button to remove employees data from records
     * @param e list selection change event
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == this.getSelectionModel()) {
            int selectedRowIndex = getSelectedRow();

            // show the picture for this selected object
            if (selectedRowIndex != -1)
            {
                // Following 2 lines of code allows me to highlight selected row using selected row Color
                addRowSelectionInterval(selectedRowIndex, selectedRowIndex);
                setColumnSelectionInterval(0, this.getColumnCount()-1);
                selectionChangeListener.SelectionChanged(this, selectedRowIndex, 0);

                Consumer consumer =  consumerModel.getRow(selectedRowIndex);
                Image image = consumer.getSignatureImage();
                if (signControl != null){
                    signControl.setIcon(new ImageIcon(image));
                }
            }
        }
    }

    /**
     * Adds a new consumer object to the table model
     * @param consumer New consumer object
     */
    public void add(Consumer consumer)
    {
        consumerModel.addRow(consumer);
        consumerModel.fireTableDataChanged();
    }

    /**
     * Sets control where a consumer's captured signature can be displayed
     * @param signatureDisplayControl The control
     */
    public void setSignatureDisplayControl(JLabel signatureDisplayControl) {
        signControl = signatureDisplayControl;
    }

    /**
     * Change datamodel associated with table
     * @param oldConsumers list of consumer records
     */
    public void ChangeDataModel(ArrayList<Consumer> oldConsumers) {
        int nRows = consumerModel.getRowCount();
        if(nRows > 0) {
            consumerModel.removeRowRange(0, nRows - 1);
        }

        for (Consumer consumer : oldConsumers) {
            consumerModel.addRow(consumer);
        }

        // raise the data table change notification
        consumerModel.fireTableDataChanged();
    }

    /**
     * Adds selection change listener object
     * @param listener The listener object
     */
    public void addSelectionChangeListener(ITableSelectionChangeListener listener){
        selectionChangeListener = listener;
    }

    public void deleteSelectedRows() {
        int[] selectedRows = getSelectedRows();
        if (selectedRows.length > 0){
            consumerModel.removeRows(selectedRows);

            // raise the data table change notification
            consumerModel.fireTableDataChanged();
        }
    }
}
