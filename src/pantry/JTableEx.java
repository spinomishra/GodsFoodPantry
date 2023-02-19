package pantry;

import pantry.interfaces.ITableSelectionChangeListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * JTableEx class extends Java's JTable control to provide common capabilities across PantryWare application
 */
public class JTableEx extends JTable {
    /**
     * Color for selected row
     */
    protected static final Color selectedRowColor = new Color(110, 181, 155);

    /**
     * Selection Change listener
     */
    protected ITableSelectionChangeListener selectionChangeListener;

    /**
     * Model use for list selection
     */
    protected ListSelectionModel tableListSelectionModel;

    /**
     * Constructors
     */
    public JTableEx(TableModel model) {
        super(model);

        setCellSelectionEnabled(false);
        setRowSelectionAllowed(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowHeight(40);

        setSelectionBackground(selectedRowColor);
        setSelectionForeground(Color.WHITE);

        tableListSelectionModel = this.getSelectionModel();
        var table = this;
        tableListSelectionModel.addListSelectionListener(new ListSelectionListener() {
            /**
             * When selection changes, enable button to remove employees data from records
             * @param e list selection change event
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getSource() == tableListSelectionModel) {
                    int selectedRowIndex = table.getSelectedRow();

                    if (selectedRowIndex != -1) {
                        // Following 2 lines of code allows me to highlight selected row using selected row Color
                        addRowSelectionInterval(selectedRowIndex, selectedRowIndex);
                        setColumnSelectionInterval(0, table.getColumnCount() - 1);

                        if (selectionChangeListener != null)
                            selectionChangeListener.SelectionChanged(table, selectedRowIndex, 0);
                    }
                }
            }
        });
        setSelectionModel(tableListSelectionModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component comp = super.prepareRenderer(renderer, row, column);
        if (row != -1 && row == getSelectedRow()) {
            comp.setBackground(selectedRowColor);
            comp.setForeground(Color.WHITE);
        } else {
            Color c = (row % 2 == 0 ? new Color(210, 231, 255) : Color.WHITE);
            comp.setBackground(c);
            comp.setForeground(Color.BLACK);
        }
        return comp;
    }

    /**
     * Adds selection change listener object
     *
     * @param listener The listener object
     */
    public void addSelectionChangeListener(ITableSelectionChangeListener listener) {
        selectionChangeListener = listener;
    }
}
