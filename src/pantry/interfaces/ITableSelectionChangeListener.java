package pantry.interfaces;

import javax.swing.*;

/**
 * ITableSelectionChangeListener
 * Implemented by classes that want to receive table's row selection change notifications
 */
public interface ITableSelectionChangeListener {
    /**
     * Selection has changed notification
     *
     * @param table Table raising the event
     * @param row   selected row
     * @param col   selected col
     */
    void SelectionChanged(JTable table, int row, int col);
}
