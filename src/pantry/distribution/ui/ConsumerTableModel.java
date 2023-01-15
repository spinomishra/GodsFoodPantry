package pantry.distribution.ui;

import pantry.data.RowTableModel;
import pantry.distribution.Consumer;
import pantry.helpers.DateHelper;
import pantry.helpers.PhoneHelper;
import pantry.person.Identity;
import pantry.volunteer.Volunteer;

import java.util.Arrays;

/**
 * This class extends RowTableModel and implements a
 * custom row column model for the consumer table
 */
public class ConsumerTableModel extends RowTableModel<Consumer> {
    /**
     * Consumer table columns
     */
    private static String[] COLUMN_NAMES = {
            "#",
            "Full Name",
            "DOB",
            "Phone #",
            "Address",
            "ID Type",
            "ID #",
            "ID Issued On",
            "ID Expires On",
            "Group",
            "Members"
    };

    /**
     * Constructor
     */
    ConsumerTableModel() {
        super(Arrays.asList(COLUMN_NAMES));
        setRowClass(Consumer.class);
    }

    /**
     * Makes column cell editable
     * @param row the row whose value is to be queried
     * @param col the col which will be editable or otherwise
     * @return true if editable, else false
     */
    public boolean isCellEditable(int row, int col) {
        return  (col == 2 || col==4 || col == 3) ? true : false;
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
        Consumer consumer = getRow(rowIndex);

        switch (columnIndex){
            case 0: return rowIndex+1;
            case 1: return consumer.getName();
            case 2: return consumer.getDateOfBirth();
            case 3: return consumer.getContactNumber();
            case 4: return consumer.getAddress();
            case 5: return Identity.IDType.values()[consumer.getIdentityInfo().IdentityType].toString();
            case 6: return consumer.getIdentityInfo().Number;
            case 7: return consumer.getIdentityInfo().IssuedOn;
            case 8: return consumer.getIdentityInfo().ExpiresOn;
            case 9: return consumer.isGroupFlagged() ? "Yes" : "No";
            case 10: return consumer.getGroupMemberCount();
            default: return null;
        }
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
            Consumer c = getRow(row);

            switch (column){
                case 2: //
                    if (!DateHelper.isEmptyDateString((String) value))
                        c.setDateOfBirth((String) value);
                    break ;

                case 3:// phone number
                    if (!PhoneHelper.isNullOrEmpty((String)value) && PhoneHelper.isValidNumber((String)value))
                        c.setContactPhone((String)value);
                    break;

                case 4: // address
                    c.setAddress((String) value);
                    break ;
            }

            fireTableCellUpdated(row, column);
        }
    }
}
