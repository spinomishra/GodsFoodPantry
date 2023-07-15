package pantry.distribution.ui;

import pantry.distribution.*;

import integrisign.IGrabber;
import pantry.person.ui.IdentityInfoPage;
import pantry.person.ui.PersonInfoPage;
import pantry.ui.wizard.AbstractWizardPage;
import pantry.ui.wizard.WizardPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * This class represents UI to capture consumer information about individuals/groups that receive the food distribution.
 * To confirm receiving the food distribution this page implements integrisign's IDocInfo interface. This interface
 * defines methods that needs to be implemented by the host application to bind the content to the
 * signature that is being captured.
 */
public class DistributionConfirmationPage extends WizardPage implements integrisign.IDocInfo {
    public final static String Title = "DistributionConfirmation";

    /**
     * Group Check box
     */
    public transient JCheckBox groupCheckBox;

    /**
     * Spinner control to capture member count
     */
    public transient JSpinner spinner;

    /**
     * SignNow button
     */
    private transient JButton signNow;

    /**
     * InkPad Signature control
     */
    public transient integrisign.desktop.DeskSign deskSign1;


    public DistributionConfirmationPage() {
        super(Title);
        createControls();
        arrangeControls();
    }

    private void createControls() {
        {
            groupCheckBox = new JCheckBox("", false);

            // Group count
            SpinnerModel model = new SpinnerNumberModel(0, 0, 20, 1);
            spinner = new JSpinner(model);
            spinner.setEnabled(false);
            spinner.setMinimumSize(new Dimension(40,60));

            groupCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    spinner.setEnabled(e.getStateChange() == 1);
                    spinner.setValue(e.getStateChange() == 1 ? 1 : 0);
                }
            });
        }

        {
            deskSign1 = new integrisign.desktop.DeskSign();
            deskSign1.setOpaque(true);

            Dimension minimalSize = new Dimension(160, 120);
            deskSign1.setPreferredSize(minimalSize);
            deskSign1.setSize(minimalSize);
            deskSign1.setBounds(2, 2, Short.MAX_VALUE, Short.MAX_VALUE);

            var tablePanelBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
            deskSign1.setBorder(tablePanelBorder);
        }

        {
            Dimension btnSize = new Dimension(108, 30);
            signNow = new JButton("Sign Now");
            signNow.setActionCommand("SignNow");
            signNow.setMinimumSize(btnSize);
            signNow.setMaximumSize(btnSize);
            signNow.setPreferredSize(btnSize);
            signNow.setSize(btnSize);
            signNow.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SignNow();
                }
            });
            //signNow.setEnabled(false);
        }
    }

    /**
     * Arrange controls within gridbag layout
     */
    protected void arrangeControls(){
        GridBagLayout layout = new GridBagLayout();
        layout.rowWeights = new double[]{0.1, 0.1, 1.0, 0.1};
        layout.columnWeights = new double[]{0.0, 1.0};
        layout.rowHeights = new int[] {0, 0, 0, 0, 0};
        layout.columnWidths = new int[] {0, 0};
        setLayout(layout);

        addControlWithConstraints(new JLabel("Group"), 0,0, GridBagConstraints.BOTH);
        addControlWithConstraints(groupCheckBox, 1,0, GridBagConstraints.BOTH);

        addControlWithConstraints(new JLabel("Group Member Count"), 0,1, GridBagConstraints.BOTH);
        addControlWithConstraints(spinner, 1,1, GridBagConstraints.BOTH);

        addControlWithConstraints(new JLabel("Signature"), 0,2, GridBagConstraints.BOTH);
        addControlWithConstraints(deskSign1, 1,2, GridBagConstraints.BOTH);

        addControlWithConstraints(new JLabel(""), 0,3, GridBagConstraints.BOTH);
        addControlWithConstraints(signNow, 1,3, GridBagConstraints.NONE);
    }

    /**
     * Enables inkpad component to generate MD5
     * hash of the content to be authenticated.
     * Once the signature is captured or opened
     * using existing signature info, user details
     * like Name, Designation, Organization, Address can
     * be queried if they are set.
     *
     * @param iGrabber IGrabber object
     */
    @Override
    public void feedGrabber(IGrabber iGrabber) {
        AbstractWizardPage page = getWizardController().getPage(PersonInfoPage.Title);
        PersonInfoPage infoPage = page != null ? (PersonInfoPage) page : null;
        String name = infoPage.getPersonName();
        iGrabber.grabBytes(name.getBytes());
        iGrabber.finishGrab();
    }

    /**
     * Get the version for the signature data
     *
     * @return version info
     */
    @Override
    public byte getVersion() {
        return 1;
    }

    /**
     * Get document IDd
     *
     * @return empty string as doc ID
     */
    @Override
    public String getDocID() {
        return "";
    }

    /**
     * Read information from controls
     * @param consumer object where information will be set
     */
    public void ReadControlsInto(Consumer consumer) {
        consumer.setGroupFlag(groupCheckBox.isSelected());
        consumer.setGroupNumber((short) Integer.parseInt(spinner.getValue().toString()));

        // collect signature data
        if (deskSign1.isSigned()) {
            String signature = deskSign1.getString();
            String png = deskSign1.getPNGString(signature, 160, 120, false);
            consumer.setSignatureInfo(signature, png) ;
        }
    }

    /**
     * When SignNowEx is called a SignPad appears for capturing the
     * signature. User can choose a different signature color
     * from options button, depending on the privileges set
     * to him in signNowEx method.
     */
    private void SignNow() {
        try {
            deskSign1.setSignThickness((byte) 1);
            deskSign1.setForeground(Color.BLUE);
            deskSign1.setOpaque(false);

            AbstractWizardPage page = getWizardController().getPage(PersonInfoPage.Title);
            PersonInfoPage infoPage = page != null ? (PersonInfoPage) page : null;
            page = getWizardController().getPage(IdentityInfoPage.Title);
            IdentityInfoPage identityInfoPage = page != null ? (IdentityInfoPage) page : null;

            if (deskSign1.isSigned()) {
                deskSign1.clear();
            }

            if (identityInfoPage != null && infoPage != null)
                deskSign1.signNowEx(identityInfoPage.IdNumber(), infoPage.getPersonName(), "", "", infoPage.getPersonAddress(), "", false, this);

            deskSign1.setOpaque(true);
            // update the state for OK button
            insertUpdate(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Signature", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected boolean isFinishAllowed() {
        return deskSign1.isSigned();
    }

    @Override
    protected boolean isPreviousAllowed() {
        return true;
    }
}
