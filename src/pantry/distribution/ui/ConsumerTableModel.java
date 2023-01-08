package pantry.distribution.ui;

import pantry.data.RowTableModel;
import pantry.distribution.Consumer;
import pantry.person.Identity;

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
        return false;
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
            case 2: return consumer.getContactNumber();
            case 3: return consumer.getAddress();
            case 4: return Identity.IDType.values()[consumer.getIdentityInfo().IdentityType].toString();
            case 5: return consumer.getIdentityInfo().Number;
            case 6: return consumer.getIdentityInfo().IssuedOn;
            case 7: return consumer.getIdentityInfo().ExpiresOn;
            case 8: return consumer.isGroupFlagged() ? "Yes" : "No";
            case 9: return consumer.getGroupMemberCount();
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
        Consumer e = getRow(row);
        fireTableCellUpdated(row, column);
    }
}
