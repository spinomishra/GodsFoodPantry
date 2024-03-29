package pantry.employee.ui;

import pantry.employee.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Class for rendering (displaying) Employee Role cells in a JTable.
 */
public class EmployeeRoleCellRenderer extends DefaultTableCellRenderer {
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Employee.EmployeeRole) {
            Employee.EmployeeRole role = (Employee.EmployeeRole) value;
            setText(role.toString());
        }

        if (isSelected && !isPaintingForPrint()) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getSelectionForeground());
        }

        return this;
    }
}
