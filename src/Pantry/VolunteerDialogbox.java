package Pantry;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.text.*;
import java.io.Serializable;

public class VolunteerDialogbox extends JPanel
                                    implements ListSelectionListener {

    private JList list;
    private DefaultListModel listModel;
                                      
    //Labels to identify the fields
    private JLabel existingVolunteers;
 
    //Strings for the labels
    private static String volunteers = "Volunteers: ";
                                      
    private static final String addNew = "New";
    private static final String removeLabel = "Delete";
    private JButton removeButton;
    private JTextField nameTextBox;
    private JButton scheduleActivityButton  ;
    ArrayList<Volunteer> Volunteers ;
                                      
    public VolunteerDialogbox(ArrayList<Volunteer> volunteers) {
      super(new BorderLayout());
      listModel = new DefaultListModel();
      Volunteers = volunteers;
      
      if (volunteers != null) {
        for (int i=0; i<volunteers.size(); i++){
          listModel.addElement(volunteers.get(i).getName());
        }
      }
      
      //Create the list and put it in a scroll pane.
      list = new JList(listModel);
      list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      list.setSelectedIndex(0);
      list.addListSelectionListener(this);
      list.setVisibleRowCount(5);
      JScrollPane listScrollPane = new JScrollPane(list);

      JButton newButton = new JButton(addNew);
      HireListener newButtonListener = new HireListener(newButton);
      newButton.setActionCommand(addNew);
      newButton.addActionListener(newButtonListener);
      newButton.setEnabled(false);

      removeButton = new JButton(removeLabel);
      removeButton.setActionCommand(removeLabel);
      removeButton.addActionListener(new FireListener());
      removeButton.setEnabled(false);
    
      nameTextBox = new JTextField(10);
      nameTextBox.addActionListener(newButtonListener);
      nameTextBox.getDocument().addDocumentListener(newButtonListener);

      scheduleActivityButton = new JButton("Schedule Activity");
      scheduleActivityButton.setActionCommand(removeLabel);
      scheduleActivityButton.addActionListener(new ScheduleListener());
      scheduleActivityButton.setEnabled(false);
      
      int selection = list.getSelectedIndex() ;
      if (selection != -1) {
        removeButton.setEnabled(true);
        scheduleActivityButton.setEnabled(true);
      }

      //Create a panel that uses BoxLayout.
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new BoxLayout(buttonPane,
                                         BoxLayout.LINE_AXIS));
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
                                      
    class FireListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
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
    }

    class ScheduleListener implements ActionListener {
       public void actionPerformed(ActionEvent e) {
         }
    }
                                      
    //This listener is shared by the text field and the hire button.
    class HireListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;
 
        public HireListener(JButton button) {
            this.button = button;
        }
 
        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
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
 
        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }
 
        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }
 
        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }
 
        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }
 
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }
 
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
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
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public static void createAndShowGUI(JFrame frame, ArrayList<Volunteer> volunteers) {
      //Create and set up the window.
      final JDialog dialog = new JDialog(frame, "Volunteers", Dialog.ModalityType.DOCUMENT_MODAL);
      dialog.setSize(460, 200);
      dialog.setLocationRelativeTo(frame);
      
      //Create and set up the content pane.
      JComponent newContentPane = new VolunteerDialogbox(volunteers);
      newContentPane.setOpaque(true); //content panes must be opaque
      dialog.setContentPane(newContentPane);
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

      //Display the window.
      dialog.pack();
      dialog.setVisible(true);
    }
}