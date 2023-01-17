package pantry.volunteer.ui;

import pantry.Pantry;
import pantry.helpers.PhoneHelper;
import pantry.helpers.StringHelper;
import pantry.person.ui.PersonInfo;
import pantry.volunteer.ActivityInfo;
import pantry.volunteer.Volunteer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Class represents UI to capture Volunteer information
 */
public class VolunteerInfo extends PersonInfo {
    /**
     * Activities list combo box
     */
    private JComboBox<ActivityInfo.Activities> activitiesComboBox;

    /**
     * Recent Activity information
     */
    private ActivityInfo recentActivity;

    /**
     * Check in time control
     */
    JTextField checkInTime;

    /**
     * Constructor
     *
     * @param frame - parent component
     * @param title - dialog title box
     */
    public VolunteerInfo(Window frame, String title) {
        super(frame, title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addTabs(JTabbedPane tabbedPane) {
        super.addTabs(tabbedPane);

        if (recentActivity == null)
            recentActivity = new ActivityInfo();

        JPanel identityPanel = createNewTab(tabbedPane, "Time Entry");
        identityPanel.setPreferredSize(new Dimension(375, 150));

        JLabel checkInTimeLbl = new JLabel("Check-in Time");
        checkInTimeLbl.setFocusable(false);
        checkInTimeLbl.setBounds(10, 20, 120, 20);
        identityPanel.add(checkInTimeLbl);

        String formattedDateTime = recentActivity.getStartTime().format(DateTimeFormatter.ofPattern("MMM dd, uuuu hh:mm a"));
        checkInTime = new JTextField(formattedDateTime);
        checkInTime.getDocument().addDocumentListener(this);
        checkInTime.setBounds(135, 20, 220, 20);
        identityPanel.add(checkInTime);

        JLabel activityLabel = new JLabel("Volunteer Activity");
        activityLabel.setFocusable(false);
        activityLabel.setBounds(10, 45, 120, 20);
        identityPanel.add(activityLabel);

        // ActivitiesList combo box model
        DefaultComboBoxModel<ActivityInfo.Activities> activitiesModel = new DefaultComboBoxModel<>();
        for (ActivityInfo.Activities r : ActivityInfo.Activities.values())
            activitiesModel.addElement(r);

        //Create the list box to show the volunteers names
        activitiesComboBox = new JComboBox<>(activitiesModel);
        activitiesComboBox.setActionCommand("activitiesList");
        activitiesComboBox.setSelectedIndex(0);
        activitiesComboBox.addActionListener(this);
        activitiesComboBox.setBounds(135, 45, 220, 20);
        identityPanel.add(activitiesComboBox);
    }

    /**
     * Get volunteer's activity
     *
     * @return Volunteer activity information
     */
    public ActivityInfo getRecentActivity() {
        return recentActivity;
    }

    /**
     * element insert handler
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        String name = nameTextBox.getText();
        String phone = contactTextBox.getText();
        LocalDateTime checkinTimeObj = null;
        try {
            checkinTimeObj = LocalDateTime.parse(checkInTime.getText(), DateTimeFormatter.ofPattern("MMM dd, uuuu hh:mm a"));
        } catch (Exception ex) {

        }

        okButton.setEnabled(!StringHelper.isNullOrEmpty(name) && !PhoneHelper.isNullOrEmpty(phone) && checkinTimeObj != null);
    }

    /**
     * element remove handler
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        String name = nameTextBox.getText();
        String phone = contactTextBox.getText();
        LocalDateTime checkinTimeObj = null;
        try {
            checkinTimeObj = LocalDateTime.parse(checkInTime.getText(), DateTimeFormatter.ofPattern("MMM dd, uuuu hh:mm a"));
        } catch (Exception ex) {

        }

        okButton.setEnabled(!StringHelper.isNullOrEmpty(name) && !PhoneHelper.isNullOrEmpty(phone) && checkinTimeObj != null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getActionCommand().equals("OK") || e.getActionCommand().equals("activitiesList"))
            recentActivity.setActivity(activitiesComboBox.getItemAt(activitiesComboBox.getSelectedIndex()));
    }

    /**
     * Find existing records corresponding to this volunteer information
     *
     * @return list of volunteer records
     */
    public ArrayList<Volunteer> findExistingRecords() {
        return Pantry.getInstance().get_Data().searchVolunteers(this.personName, this.personContact);
    }

    /**
     * Create and show the UI for Volunteer information
     *
     * @param frame - parent component
     * @param title - title of the dialog box
     * @return Volunteer object
     */
    public static Volunteer createAndShowGUI(Window frame, String title) {
        var pInfo = new VolunteerInfo(frame, title);
        pInfo.setVisible(true);

        Volunteer e = null;
        if (pInfo.option == JOptionPane.OK_OPTION) {
            ArrayList<Volunteer> volunteers = pInfo.findExistingRecords();
            if (volunteers.size() == 0) {
                e = new Volunteer(pInfo);
                // add the volunteer object to the pantry data
                Pantry.getInstance().get_Data().get_Volunteers().add(e);
            } else {
                // pick one record and log volunteer activity
                e = volunteers.get(0);
                if (e != null)
                    e.Update(pInfo);
            }
        }

        pInfo.dispose();
        return e;
    }
}
