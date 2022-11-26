package pantry.employee.ui;

import pantry.employee.Employee;
import pantry.data.RowTableModel;

import java.util.Arrays;

public class EmployeeTableModel extends RowTableModel<Employee> {
    private static String[] COLUMN_NAMES = {
            "Full Name",
            "Role",
            "Phone #",
            "SSN",
            "Address"
    };

    EmployeeTableModel() {
        super(Arrays.asList(COLUMN_NAMES));
        setRowClass(Employee.class);

        setColumnClass(1, Employee.EmployeeRole.class);
    }

    /**
     * Makes column cell editable
     * @param row the row whose value is to be queried
     * @param col the col which will be editable or otherwise
     * @return true if editable, else false
     */
    public boolean isCellEditable(int row, int col) {
        return (col != 0);
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee e = getRow(rowIndex);

        switch (columnIndex){
            case 0: return e.getName();
            case 1: return e.getRole();
            case 2: return e.getContactNumber();
            case 3: return e.getSSN();
            case 4: return e.getAddress();
            default: return null;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column)
    {
        Employee e = getRow(row);

        switch (column)
        {
            case 1: e.setRole((Employee.EmployeeRole)value); break;
            case 2: e.setContactPhone((String)value); break;
            case 3: break;
            case 4: e.setAddress((String)value); break;
        }

        fireTableCellUpdated(row, column);
    }
}
