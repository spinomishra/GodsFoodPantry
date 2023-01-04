package pantry.volunteer.ui;

import pantry.Pantry;
import pantry.helpers.DateHelper;
import pantry.interfaces.ITableSelectionChangeListener;
import pantry.volunteer.Volunteer;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Volunteer management card
 */
public class VolunteerManagerCard extends JPanel implements  ActionListener, ITableSelectionChangeListener {
    /**
     * Remove button label
     */
    private static final String removeLabel = "Delete";

    /**
     * Remove button control
     */
    private final JButton removeButton;

    /**
     * Volunteer records table control
     */
    private VolunteerTable volunteerTable ;

    /**
     * Record choice combo box
     */
    JComboBox comboBox;

    /**
     * Title of the volunteer page
     */
    public static final String Title = "Volunteer Manager";

    /**
     * Constructor
     * @param parentWindow The Parent window
     */
    public VolunteerManagerCard(JFrame parentWindow) {
        super(new BorderLayout());
        setOpaque(true);

        var volunteers = Pantry.getInstance().get_Data().get_Volunteers();

        {
            JPanel panel = new JPanel();
            JLabel label = new JLabel("Show Volunteer Records");

            Font labelFont = new Font("Serif", Font.BOLD|Font.ITALIC, 14);
            label.setFont(labelFont);
            label.setHorizontalTextPosition(JLabel.CENTER);
            panel.add(label, BorderLayout.CENTER);
            String[] recordChoices = {"All", "Today's", "Last 30 days"};
            comboBox = new JComboBox(recordChoices);
            comboBox.setSelectedIndex(0);
            comboBox.setMaximumSize(new Dimension(200, 80));
            comboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeButton.setEnabled(false);
                    switch (comboBox.getSelectedIndex()){
                        case 0:
                            volunteerTable.ChangeDataModel(volunteers);
                            break ;

                        case 1: {
                            // search all records for last 1 day
                            var records = Pantry.getInstance().get_Data().searchVolunteers(1);
                            volunteerTable.ChangeDataModel(records);
                        }
                        break ;

                        case 2: {
                            // search all records for last 30 days
                            var records = Pantry.getInstance().get_Data().searchVolunteers(30);
                            volunteerTable.ChangeDataModel(records);
                        }
                        break ;
                    }
                }
            });
            panel.add(comboBox, BorderLayout.SOUTH);
            add(panel, BorderLayout.NORTH);
        }

        //Create the table and put it in a scroll pane and add scroll panel to the card
        {
            volunteerTable = new VolunteerTable(volunteers);
            JScrollPane scrollPane = new JScrollPane(volunteerTable,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            volunteerTable.setFillsViewportHeight(true);
            volunteerTable.addSelectionChangeListener(this);

            add(scrollPane, BorderLayout.CENTER);
        }

        { // Action buttons

            //Create a panel that uses BoxLayout.
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));

            removeButton = new JButton(removeLabel);
            removeButton.setActionCommand(removeLabel);
            removeButton.addActionListener(this);
            removeButton.setEnabled(false);
            var removeButtonIcon = getImageIcon("/images/recyclebin.png", 12, 12);
            removeButton.setIcon(removeButtonIcon);
            removeButton.setEnabled(false);

            buttonPane.add(removeButton);
            buttonPane.add(Box.createHorizontalStrut(5));
            buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
            buttonPane.add(Box.createHorizontalStrut(5));

            buttonPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            add(buttonPane, BorderLayout.PAGE_END);
        }
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
            //there's a valid selection so go
            //ahead and remove whatever is selected.
            volunteerTable.deleteSelectedRows();
            removeButton.setEnabled(false);
        }
    }

    /**
     * Helper method to construct image icon using the given image path
     * @param imagePath The image path
     * @param w image icon width
     * @param h image icon height
     * @return ImageIcon object
     */
    private  ImageIcon getImageIcon(String imagePath, int w, int h)
    {
        var resource = getClass().getResource(imagePath);
        if (resource != null) {
            var img = ((new ImageIcon(resource)).getImage()).getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }

    /**
     * Volunteer Table selection change listener
     * @param table Table raising the event
     * @param row selected row
     * @param col selected col
     */
    @Override
    public void SelectionChanged(JTable table, int row, int col) {
        removeButton.setEnabled(true);
    }
}
