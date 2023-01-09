package pantry.distribution.ui;

import integrisign.IGrabber;
import pantry.distribution.Consumer;
import pantry.helpers.DateHelper;
import pantry.helpers.State;
import pantry.person.Identity;
import pantry.person.ui.PersonInfo;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * ConsumerInfo class represents UI to capture consumer information about individuals/groups that receive the food distribution.
 * This class implements integrisign's IDocInfo interface. This interface defines methods that needs to be implemented by
 * the host application to bind the content to the signature that is being captured.
 */
public class ConsumerInfo extends PersonInfo implements integrisign.IDocInfo{
    ////////////////// Controls Begin /////////////////////////////
    /**
     * Identity type combo box
     */
    public transient JComboBox idTypeBox;

    /**
     * Identity document number control
     */
    public transient JTextField idNumber;

    /**
     * Identity document issued on control
     */
    public transient JFormattedTextField issuedOn;

    /**
     * Identity document expires on control
     */
    public transient JFormattedTextField expiryFld;

    /**
     * Identity document issued by State control
     */
    public transient JComboBox stateBox;

    /**
     * SignNow button
     */
    private transient JButton signNow;

    /**
     * InkPad Signature control
     */
    public transient integrisign.desktop.DeskSign deskSign1;

    /**
     * Group Check box
     */
    public transient JCheckBox groupCheckBox;

    /**
     * Spinner control to capture member count
     */
    public transient JSpinner spinner;

    ////////////////// Controls End /////////////////////////////

    /**
     * Constructor
     * @param frame - parent component
     * @param title - dialog title box
     */
    public ConsumerInfo(Window frame, String title) {
        super(frame, title);
    }

    /**
     * Add tab control to capture Consumer information
     * @param tabbedPane The tabbed pane control.
     */
    protected void addTabs(JTabbedPane tabbedPane){
        super.addTabs(tabbedPane);

        JPanel containerPanel = createNewTab(tabbedPane, "Identity", null);
        if (containerPanel == null)
            return;

        JPanel panel;

        // Identity type
        {
            panel = addNewItemPanel("Identity Type");
            idTypeBox = new JComboBox();
            idTypeBox.setModel(new DefaultComboBoxModel(Identity.IDType.values()));
            idTypeBox.setActionCommand("IdType");
            panel.add(idTypeBox, BorderLayout.CENTER);
            containerPanel.add(panel);
        }

        // Id number
        {
            panel = addNewItemPanel("Identity Number");

            idNumber = new JTextField();
            idNumber.setActionCommand("idNumber");
            idNumber.getDocument().addDocumentListener(this);
            panel.add(idNumber, BorderLayout.CENTER);
            containerPanel.add(panel);
        }

        // Issued On
        {
            panel = addNewItemPanel("IssuedOn (mm/dd/yyyy)");
            try {
                //MaskFormatter dateFmt = new MaskFormatter("##/##/####");
                issuedOn = new JFormattedTextField(DateHelper.getDateFormatter());
                issuedOn.setActionCommand("issuedOn");
                issuedOn.getDocument().addDocumentListener(this);
                panel.add(issuedOn, BorderLayout.CENTER);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(getParent(), ex.getMessage());
            }

            containerPanel.add(panel);
        }

        // Expire On
        {
            panel = addNewItemPanel("ExpiresOn (mm/dd/yyyy)");
            try {
                //MaskFormatter dateFmt = new MaskFormatter("##/##/####");
                expiryFld = new JFormattedTextField(DateHelper.getDateFormatter());
                expiryFld.setActionCommand("expiresOn");
                expiryFld.getDocument().addDocumentListener(this);
                panel.add(expiryFld, BorderLayout.CENTER);
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(getParent(), ex.getMessage());
            }

            containerPanel.add(panel);
        }

        // Issued by
        {
            panel = addNewItemPanel("Issued By (State)");
            stateBox = new JComboBox();
            stateBox.setSelectedIndex(-1);
            stateBox.setModel(new DefaultComboBoxModel(State.values()));
            stateBox.setActionCommand("State");
            panel.add(stateBox, BorderLayout.CENTER);
            containerPanel.add(panel);
        }

        // Group information
        {
            panel = addNewItemPanel("Group?");

            groupCheckBox = new JCheckBox("", false);
            panel.add(groupCheckBox, BorderLayout.CENTER);

            // Group count
            SpinnerModel model = new SpinnerNumberModel(0, 0, 20, 1);
            spinner = new JSpinner(model);
            spinner.setEnabled(false);
            panel.add(spinner, BorderLayout.EAST);
            groupCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    spinner.setEnabled(e.getStateChange() == 1);
                    spinner.setValue(e.getStateChange() == 1 ? 1 : 0);
                }
            });

            containerPanel.add(panel);
        }

        {
            panel = addNewItemPanel("Signature");

            deskSign1 = new integrisign.desktop.DeskSign();
            deskSign1.setOpaque(true);

            Dimension minimalSize = new Dimension(160, 120);
            deskSign1.setPreferredSize(minimalSize);
            deskSign1.setSize(minimalSize);
            deskSign1.setBounds(2, 2, Short.MAX_VALUE, Short.MAX_VALUE) ;

            var tablePanelBorder = BorderFactory.createEmptyBorder(5,5,5,5);
            deskSign1.setBorder(tablePanelBorder);
            panel.add(deskSign1, BorderLayout.CENTER);

            containerPanel.add(panel);
        }
    }

    /**
     * Add action buttons to the UI
     * This specific override adds "Sign Now" to initialize inkPad
     * @return The JPanel object
     */
    @Override
    protected JPanel addActionControls(){
        JPanel actionPanel = super.addActionControls();

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
                SignNow() ;
            }
        });
        signNow.setEnabled(false);

        actionPanel.add(signNow);

        return actionPanel;
    }

    /**
     * Enables inkpad component to generate MD5
     * hash of the content to be authenticated.
     * Once the signature is captured or opened
     * using existing signature info, user details
     * like Name, Designation, Organization, Address can
     * be queried if they are set.
     * @param iGrabber IGrabber object
     */
    @Override
    public void feedGrabber(IGrabber iGrabber) {
        String name = nameTextBox.getText().trim();
        iGrabber.grabBytes(name.getBytes());
        iGrabber.finishGrab();
    }

    /**
     * Get the version for the signature data
     * @return version info
     */
    @Override
    public byte getVersion() {
        return 1;
    }

    /**
     * Get document IDd
     * @return empty string as doc ID
     */
    @Override
    public String getDocID() {
        return "";
    }

    /**
     * element insert handler
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        okButton.setEnabled(deskSign1.isSigned());
        signNow.setEnabled(nameTextBox.getText().trim().length() != 0 &&
                idNumber.getText().trim().length() != 0);
    }

    /**
     * element remove handler
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        okButton.setEnabled(deskSign1.isSigned());
        signNow.setEnabled(nameTextBox.getText().trim().length() != 0 &&
                idNumber.getText().trim().length() != 0);
    }

    /**
     * When SignNowEx is called a SignPad appears for capturing the
     * signature. User can choose a different signature color
     * from options button, depending on the privileges set
     * to him in signNowEx method.
     */
    private void SignNow() {
        try {
            deskSign1.setSignThickness((byte)1);
            deskSign1.setForeground(Color.BLUE);
            deskSign1.setOpaque(false);

            if (deskSign1.isSigned()) {
                deskSign1.clear();
                deskSign1.signNowEx(idNumber.getText().trim(), nameTextBox.getText().trim(),"", "", addressTextBox.getText().trim(),"", false,  (integrisign.IDocInfo)this);
            } else {
                deskSign1.signNowEx(idNumber.getText().trim(), nameTextBox.getText().trim(),"", "", addressTextBox.getText().trim(),"", false,  (integrisign.IDocInfo)this);
            }

            deskSign1.setOpaque(true);
            // update the state for OK button
            insertUpdate(null);
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(),"Signature", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles mouse click event on OK button
     * @param e The ActionEvent object
     */
    protected void onOKButtonClicked(ActionEvent e){
        super.onOKButtonClicked(e);
    }

    /**
     * Prompt to record consumer information before consumer receives food distribution
     * @param parent Parent frame object
     * @return Consumer object
     */
    public static Consumer RecordInformationManually(JFrame parent) {
        var  pInfo = new ConsumerInfo(parent, "Food Distribution-Consumer Info");
        pInfo.pack();
        pInfo.setVisible(true);

        Consumer consumer = (pInfo.option == JOptionPane.OK_OPTION) ?
                                    consumer = new Consumer(pInfo)
                                    : null;

        // dispose pInfo NOW
        pInfo.dispose();

        return consumer;
    }


}
