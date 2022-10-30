package pantry.ui;

import pantry.Pantry;
import pantry.Volunteer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Volunteer manager card
 */
public class VolunteerManagerCard
        extends JPanel
        implements ListSelectionListener, ActionListener, DocumentListener {
    private final JList list;
    private final DefaultListModel listModel;

    //Labels to identify the fields
    private static final String addNew = "New";
    private final JButton newButton;

    private static final String removeLabel = "Delete";
    private final JButton removeButton;

    private final JTextField nameTextBox;

    ArrayList<Volunteer> Volunteers ;
    private final JButton scheduleActivityButton  ;

    public static final String Title = "Volunteer Manager";
    public VolunteerManagerCard(JFrame parentWindow) {
        super(new BorderLayout());
        setOpaque(true);

        listModel = new DefaultListModel();
        Volunteers = Pantry.getInstance().get_Data().get_Volunteers();

        if (Volunteers != null) {
            for (int i=0; i<Volunteers.size(); i++){
                listModel.addElement(Volunteers.get(i).getName());
            }
        }

        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        newButton = new JButton(addNew);
        newButton.setActionCommand(addNew);
        newButton.addActionListener(this);
        newButton.setEnabled(false);

        removeButton = new JButton(removeLabel);
        removeButton.setActionCommand(removeLabel);
        removeButton.addActionListener(this);
        removeButton.setEnabled(false);

        nameTextBox = new JTextField(10);
        nameTextBox.getDocument().putProperty("owner", nameTextBox);
        nameTextBox.getDocument().addDocumentListener(this);

        scheduleActivityButton = new JButton("Schedule Activity");
        scheduleActivityButton.setActionCommand(removeLabel);
        scheduleActivityButton.addActionListener(this);
        scheduleActivityButton.setEnabled(false);

        int selection = list.getSelectedIndex() ;
        if (selection != -1) {
            removeButton.setEnabled(true);
            scheduleActivityButton.setEnabled(true);
        }

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(removeButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(nameTextBox);
        buttonPane.add(newButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(scheduleActivityButton);

        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == removeButton) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            if (index != -1)
                listModel.remove(index);

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
        else if (e.getSource() == scheduleActivityButton) {

        }
        else if (e.getSource() == newButton) {
            String name = nameTextBox.getText();

            //User didn't type in a unique name...
            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                nameTextBox.requestFocusInWindow();
                nameTextBox.selectAll();
                return;
            }

            int index = list.getSelectedIndex(); //get selected index
            if (index == -1) { //no selection, so insert at beginning
                index = 0;
            } else {           //add after the selected item
                index++;
            }

            //listModel.insertElementAt(nameTextBox.getText(), index);
            //If we just wanted to add to the end, we'd do this:
            listModel.addElement(nameTextBox.getText());
            Volunteers.add(new Volunteer(nameTextBox.getText()));

            //Reset the text field.
            nameTextBox.requestFocusInWindow();
            nameTextBox.setText("");

            //Select the new item and make it visible.
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
            scheduleActivityButton.setEnabled(true);
        }
    }

    //This method tests for string equality. You could certainly
    //get more sophisticated about the algorithm.  For example,
    //you might want to ignore white space and capitalization.
    protected boolean alreadyInList(String name) {
        return listModel.contains(name);
    }


    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
                //No selection, disable button.
                removeButton.setEnabled(false);
                scheduleActivityButton.setEnabled(false);

            } else {
                //Selection, enable the button.
                removeButton.setEnabled(true);
                scheduleActivityButton.setEnabled(true);
            }
        }
    }

    public void insertUpdate(DocumentEvent e) {
        handleDocumentEvent(e);
    }

    //Required by DocumentListener.
    public void removeUpdate(DocumentEvent e) {
        handleDocumentEvent(e);
    }

    //Required by DocumentListener.
    public void changedUpdate(DocumentEvent e) {
            handleDocumentEvent(e);
    }

    private void handleDocumentEvent(DocumentEvent e) {
        Object owner = e.getDocument().getProperty("owner");
        if (owner == nameTextBox) {
            newButton.setEnabled(e.getDocument().getLength() > 0);
        }
    }
}
