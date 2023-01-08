package pantry.volunteer.ui;

import pantry.data.RowTableModel;
import pantry.distribution.Consumer;
import pantry.distribution.ui.ConsumerTableModel;
import pantry.employee.Employee;
import pantry.helpers.DateHelper;
import pantry.helpers.PhoneHelper;
import pantry.helpers.StringHelper;
import pantry.person.Identity;
import pantry.volunteer.ActivityInfo;
import pantry.volunteer.Volunteer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class VolunteerTableModel extends RowTableModel<Volunteer> {
    /**
     * Volunteer table columns
     */
    private static String[] COLUMN_NAMES = {
            "#",
            "Full Name",
            "Phone #",
            "Address",
            "Check-in",
            "Check-out",
            "Activity",
    };

    /**
     * Constructor
     */
    VolunteerTableModel() {
        super(Arrays.asList(COLUMN_NAMES));
        setRowClass(Volunteer.class);

        setColumnClass(0, Integer.class);
        setColumnClass(1, String.class);
        setColumnClass(2, String.class);
        setColumnClass(4, Date.class);
        setColumnClass(5, Date.class);
        setColumnClass(6, ActivityInfo.Activities.class);
    }

    /**
     * Constructor
     * @param volunteers List of volunteers
     */
    VolunteerTableModel(ArrayList<Volunteer> volunteers) {
        super(Arrays.asList(COLUMN_NAMES));
        setRowClass(Volunteer.class);

        if (volunteers != null) {
            for (Volunteer volunteer : volunteers) {
                addRow(volunteer);
            }
        }
    }

    /**
     * Makes column cell editable
     * @param row the row whose value is to be queried
     * @param col the col which will be editable or otherwise
     * @return true if editable, else false
     */
    public boolean isCellEditable(int row, int col) {
        return (col == 2 || col == 3) ? true : false;
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
        if (rowIndex < modelData.size()) {
            Volunteer volunteer = getRow(rowIndex);

            switch (columnIndex) {
                case 0:
                    return rowIndex + 1;
                case 1:
                    return volunteer.getName();
                case 2:
                    return (PhoneHelper.isNullOrEmpty(volunteer.getContactNumber())) ? StringHelper.Empty : volunteer.getContactNumber();
                case 3:
                    return volunteer.getAddress();
                case 4:
                case 5:
                case 6:
                    if (volunteer.getActivityHistory() != null) {
                        var activityInfo = volunteer.getActivityHistory().get(0);
                        if (activityInfo != null) {
                            if (columnIndex == 4) {
                                return (activityInfo.getStartTime() != null) ? DateHelper.fromLocalDateTime(activityInfo.getStartTime()) : null;
                            } else if (columnIndex == 5)
                                return (activityInfo.getEndTime() != null) ? DateHelper.fromLocalDateTime(activityInfo.getEndTime()) : null;
                            else if (columnIndex == 6)
                                return activityInfo.getActivity();
                        }
                    }
                    return "";

                default:
                    return "";
            }
        }
        return null;
    }

    /**
     * When column cells are
     * @param value   value to assign to cell
     * @param row   row of cell
     * @param column  column of cell
     */
    @Override
    public void setValueAt(Object value, int row, int column)
    {
        if (row < modelData.size()) {
            Volunteer e = getRow(row);

            switch (column) {
                case 2:
                    e.setContactPhone((String)value);
                    break;
                case 3:
                    e.setAddress((String) value);
                    break;
                case 4: {
                    e.getActivityHistory();
                }
                break;
            }

            fireTableCellUpdated(row, column);
        }
    }
}
