package pantry.ui;

import pantry.Employee;
import pantry.EmployeeInfo;
import pantry.Pantry;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Class to manage employees
 */
public class EmployeeManagerCard extends JPanel implements ListSelectionListener, ActionListener {
    public static final String Title = "Employee Manager";

    // Version Id for serialization
    private static final long serialVersionUID = 1L;

    // list box that will hold employees
    private JList<Employee> list;

    // model for the list box
    private DefaultListModel<Employee> listModel;

    // Labels for various buttons
    private static final String addNew = "Add";
    private static final String removeLabel = "Delete";

    // buttons
    private	JButton newButton;
    private JButton removeButton;

    // reference to employees list
    private ArrayList<Employee> Employees;

    // parent window
    private Window  parentWindow;

    /**
     * Constructor
     */
    public EmployeeManagerCard(JFrame parent) {
        super(new BorderLayout());
        setOpaque(true);
        parentWindow = parent;

        Employees = Pantry.getInstance().get_Data().get_Employees();

        listModel = new DefaultListModel<Employee>();
        if (Employees != null) {
            for (int i=0; i<Employees.size(); i++){
                listModel.addElement(Employees.get(i));
            }
        }

        //Create the list box to show the volunteers names
        list = new JList<Employee>(listModel);

        // single item can be selected
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        // put the list in a scroll pane.
        JScrollPane listScrollPane = new JScrollPane(list);

        // "Add New Employee" button
        newButton = new JButton("Add New Employee");
        newButton.setActionCommand(addNew);
        newButton.addActionListener(this);
        newButton.setEnabled(true);

        // "Remove Employee" button
        removeButton = new JButton("Remove Employee");
        removeButton.setActionCommand(removeLabel);
        removeButton.addActionListener(this);
        removeButton.setEnabled(false);

        int selection = list.getSelectedIndex() ;
        if (selection != -1) {
            removeButton.setEnabled(true);
        }

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,BoxLayout.LINE_AXIS));
        buttonPane.add(removeButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(newButton);

        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        // add all controls to container
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    /**
     * Action handler for buttons etc
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == addNew)
        {
            // New Employee button has been clicked

            // prompt to acquire new employee information
            Employee newPerson = EmployeeInfo.createAndShowGUI(this.parentWindow, "New Employee");
            if (newPerson != null)
            {
                // add to employees list
                Employees.add(newPerson) ;

                // add to list box
                String strPerson = newPerson.toString();
                listModel.addElement(newPerson);
            }
        }
        else if (e.getActionCommand() == removeLabel)
        {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            if (index != -1)
            {
                Employee selected = list.getSelectedValue();
                listModel.remove(index);
                Employees.remove(selected);
            }

            int size = listModel.getSize();
            if (size == 0) { //Nobody's left, disable firing.
                removeButton.setEnabled(false);
            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    /**
     * This method is required by ListSelectionListener.
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
                //No selection, disable button.
                removeButton.setEnabled(false);

            } else {
                //Selection, enable the button.
                removeButton.setEnabled(true);
            }
        }
    }
}
