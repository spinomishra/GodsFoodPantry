package pantry.employee.ui;

import pantry.employee.Employee;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * A custom editor for cells in the Role column.
 */
public class EmployeeRoleCellEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {
    /**
     * Employee's Role
     */
    private Employee.EmployeeRole role;

    /**
     * List of possible roles for Employee
     */
    private final List<Employee.EmployeeRole> listRole;

    /**
     * Constructs cell editor object for employee role
     *
     * @param listRole
     */
    public EmployeeRoleCellEditor(List<Employee.EmployeeRole> listRole) {
        this.listRole = listRole;
    }

    /**
     * {@inheritDoc}
     *
     * @return Empolyee role
     */
    @Override
    public Object getCellEditorValue() {
        return this.role;
    }

    /**
     * {@inheritDoc}
     *
     * @param table      JTable instance
     * @param value      Employee role object
     * @param isSelected is object select
     * @param row        row number in table
     * @param column     column number in the row
     * @return ComboBox component
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (value instanceof Employee.EmployeeRole) {
            this.role = (Employee.EmployeeRole) value;
        }

        JComboBox<Employee.EmployeeRole> comboRole = new JComboBox<Employee.EmployeeRole>();

        for (Employee.EmployeeRole aRole : listRole) {
            comboRole.addItem(aRole);
        }

        comboRole.setSelectedItem(role);
        comboRole.addActionListener(this);

        if (isSelected) {
            comboRole.setBackground(table.getSelectionBackground());
        } else {
            comboRole.setBackground(table.getSelectionForeground());
        }

        return comboRole;
    }

    /**
     * {@inheritDoc}
     *
     * @param event action event
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        JComboBox<Employee.EmployeeRole> comboRole = (JComboBox<Employee.EmployeeRole>) event.getSource();
        this.role = (Employee.EmployeeRole) comboRole.getSelectedItem();
    }
}