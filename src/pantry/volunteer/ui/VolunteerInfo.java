package pantry.volunteer.ui;

import pantry.Pantry;
import pantry.person.ui.PersonInfo;
import pantry.volunteer.ActivityInfo;
import pantry.volunteer.Volunteer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Class represents Volunteer information
 */
public class VolunteerInfo  extends PersonInfo {
    /**
     * Activities list combo box
     */
    private JComboBox<ActivityInfo.Activities> activitiesComboBox;

    /**
     * Recent Activity information
     */
    private ActivityInfo recentActivity = new ActivityInfo();

    /**
     * Constructor
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
    protected JPanel addControlsToTopPanel(){
        if (recentActivity == null)
            recentActivity = new ActivityInfo();

        JPanel containerPanel = super.addControlsToTopPanel();
        GridLayout gridLayout = (GridLayout) containerPanel.getLayout();
        gridLayout.setRows(gridLayout.getRows()+2);

        JLabel checkinTimeLabel = new JLabel("Check-in Time", JLabel.LEFT);
        String formattedDateTime = recentActivity.getStartTime().format(DateTimeFormatter.ofPattern("MMM dd, uuuu hh:mm a"));
        JLabel checkinTime = new JLabel(formattedDateTime);
        containerPanel.add(checkinTimeLabel);
        containerPanel.add(checkinTime);

        // ActivitiesList combo box model
        DefaultComboBoxModel<ActivityInfo.Activities> activitiesModel = new DefaultComboBoxModel<>();
        for (ActivityInfo.Activities r : ActivityInfo.Activities.values())
            activitiesModel.addElement(r);

        JLabel activitiesLabel = new JLabel("Volunteer Activity", JLabel.LEFT);

        //Create the list box to show the volunteers names
        activitiesComboBox = new JComboBox<>(activitiesModel);
        activitiesComboBox.setActionCommand("activitiesList");
        activitiesComboBox.setSelectedIndex(0);
        activitiesComboBox.addActionListener(this);

        containerPanel.add(activitiesLabel);
        containerPanel.add(activitiesComboBox);

        return containerPanel;
    }

    /**
     * Get volunteer's activity
     * @return Volunteer activity information
     */
    public ActivityInfo getRecentActivity() { return recentActivity;}

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
     * @return list of volunteer records
     */
    public ArrayList<Volunteer> findExistingRecords(){
        return Pantry.getInstance().get_Data().searchVolunteers(this.personName, this.personContact);
    }

    /**
     * Create and show the UI for Volunteer information
     * @param frame - parent component
     * @param title - title of the dialog box
     * @return Volunteer object
     */
    public static Volunteer createAndShowGUI(Window frame, String title) {
        var  pInfo = new VolunteerInfo(frame, title);
        pInfo.setVisible(true);

        Volunteer e = null;
        if (pInfo.option == JOptionPane.OK_OPTION) {
            ArrayList<Volunteer> volunteers = pInfo.findExistingRecords();
            if (volunteers.size() == 0)
            {
                e = new Volunteer(pInfo);
                // add the volunteer object to the pantry data
                Pantry.getInstance().get_Data().get_Volunteers().add(e);
            }
            else {
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