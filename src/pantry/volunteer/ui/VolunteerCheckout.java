package pantry.volunteer.ui;

import pantry.volunteer.Volunteer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * VolunteerCheckout represents process for a volunteer to record checkout time
 */
public class VolunteerCheckout implements ActionListener {
    /**
     * Recent Volunteers Combo Box
     */
    private JComboBox<Volunteer> recentVolunteersComboBox = null;
    /**
     * Volunteer selected for checkout
     */
    private Volunteer checkoutVolunteer;
    /**
     * Model dialog box
     */
    private JDialog modelDialog = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "OK": {
                checkoutVolunteer = (Volunteer) recentVolunteersComboBox.getSelectedItem();
                // close dialog
                modelDialog.dispose();
            }
            break;

            case "Cancel": {
                checkoutVolunteer = null;
                // close dialog
                modelDialog.dispose();
            }
            break;
        }
    }

    /**
     * Show Volunteer Checkout dialog box
     *
     * @param frame            parent window to the dialog box
     * @param recentVolunteers list of volunteers recently checked in and have not checked out
     */
    public void Checkout(final JFrame frame, ArrayList<Volunteer> recentVolunteers) {
        if (recentVolunteers == null || recentVolunteers.size() == 0) {
            JOptionPane.showMessageDialog(frame,
                    "No recent checkins found for checkout!",
                    "Checkout",
                    JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        modelDialog = new JDialog(frame, "Volunteer Checkout", Dialog.ModalityType.DOCUMENT_MODAL);

        // set dialog box size
        modelDialog.setSize(400, 200);
        modelDialog.setResizable(false);

        // center dialog relative to its parent
        modelDialog.setLocationRelativeTo(frame);
        InitComponent(modelDialog, recentVolunteers);

        // this is a blocking call and will return only after the dialog box is closes.
        modelDialog.setVisible(true);

        if (checkoutVolunteer != null) {
            checkoutVolunteer.getOngoingActivity().setCheckoutTime(LocalDateTime.now());
            recentVolunteers.remove(checkoutVolunteer);
        }
    }

    /**
     * Initialize components
     *
     * @param dialog container Dialog
     */
    private void InitComponent(JDialog dialog, ArrayList<Volunteer> recentVolunteers) {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        int leftx = 20;
        JLabel thanksLabel = new JLabel("THANK YOU for volunteering with us!!");
        thanksLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 20));
        thanksLabel.setForeground(new Color(0xffbd03));
        thanksLabel.setFocusable(false);
        thanksLabel.setBounds(leftx, 20, 380, 40);
        panel.add(thanksLabel);

        JLabel cmbLabel = new JLabel("Choose your name from recent volunteers list");
        cmbLabel.setBounds(leftx, 70, 320, 20);
        panel.add(cmbLabel);

        // Role list combo box model
        DefaultComboBoxModel<Volunteer> volunteerModel = new DefaultComboBoxModel<>();
        recentVolunteersComboBox = new JComboBox<Volunteer>(volunteerModel);
        recentVolunteersComboBox.setBounds(leftx, 92, 345, 20);

        if (recentVolunteers != null) {
            for (Volunteer v : recentVolunteers)
                volunteerModel.addElement(v);
        }

        recentVolunteersComboBox.setSelectedIndex(-1);

        // single item can be selected
        recentVolunteersComboBox.setActionCommand("volList");
        recentVolunteersComboBox.addActionListener(this);

        panel.add(cmbLabel);
        panel.add(recentVolunteersComboBox);

        JButton checkoutBtn = new JButton("Checkout");
        checkoutBtn.setBounds(95, 124, 90, 25);
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setBackground(new Color(0x4681f4));
        checkoutBtn.addActionListener(this);
        checkoutBtn.setActionCommand("OK");
        panel.add(checkoutBtn);

        var cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(190, 124, 90, 25);
        cancelBtn.addActionListener(this);
        cancelBtn.setActionCommand("Cancel");
        panel.add(cancelBtn);

        dialog.add(panel);
    }
}
