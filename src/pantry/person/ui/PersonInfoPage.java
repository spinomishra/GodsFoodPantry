/**
 *
 */
package pantry.person.ui;

import pantry.helpers.DateHelper;
import pantry.helpers.PhoneHelper;
import pantry.helpers.StringHelper;
import pantry.person.IdentityDataEnum;
import pantry.person.Person;
import pantry.person.Race;
import pantry.ui.wizard.WizardPage;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.util.EnumSet;

/**
 * Person information class
 *
 */
public class PersonInfoPage extends WizardPage{
    private EnumSet<IdentityDataEnum>  optionalIdentities;

    ////////////////// CONTROLS ////////////////
    /**
     * Name text field
     */
    protected transient JTextField nameTextBox;
    /**
     * address text area
     */
    protected transient JTextArea addressTextBox;

    /**
     * Formatted text field for Phone number
     */
    protected transient JFormattedTextField contactTextBox;

    /**
     * Race combo box
     */
    protected JComboBox<Race> raceComboBox;

    /**
     * Formatted text field to capture Date of Birth
     */
    protected transient JFormattedTextField dobField;

    protected JScrollPane scrollPane;

    public final static String Title = "Personal Info";

    /**
     * Constructor
     *
     */
    public PersonInfoPage() {
        super(Title);
        createControls();
        arrangeControls();
    }

    /**
     * Constructor
     * @param optional - optional identity
     */
    public PersonInfoPage(EnumSet<IdentityDataEnum> optional) {
        this();
        optionalIdentities = optional;
    }

    /**
     * Read controls
     * @param person person object
     */
    public void ReadControlsInto(Person person) {
        if (person != null) {
            person.setDateOfBirth(getDateOfBirth());
            person.setAddress(getPersonAddress());
            person.setContactPhone(getPersonContact());
        }
    }

    private void createControls(){
        nameTextBox = new JTextField(30);
        nameTextBox.getDocument().addDocumentListener(this);

        try
        {
            MaskFormatter dateFmt = new MaskFormatter("##/##/####");
            dobField = new JFormattedTextField(dateFmt);
        }
        catch(Exception ex){
            dobField = new JFormattedTextField();
        }
        dobField.setColumns(12);
        dobField.getDocument().addDocumentListener(this);

        try {
            MaskFormatter fmt = PhoneHelper.getFormatterMask();
            contactTextBox = new JFormattedTextField(fmt);
        }
        catch (Exception ex){

        }
        contactTextBox.setColumns(12);
        contactTextBox.getDocument().addDocumentListener(this);

        Dimension minimalSize = new Dimension(120, 70);
        addressTextBox = new JTextArea(4, 1);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        addressTextBox.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        addressTextBox.getDocument().addDocumentListener(this);

        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(120, 70));
        scrollPane.setViewportView(addressTextBox);

        // Role list combo box model
        DefaultComboBoxModel<Race> raceListModel = new DefaultComboBoxModel<>();
        for (Race r : Race.values())
            raceListModel.addElement(r);

        raceComboBox = new JComboBox<Race>(raceListModel);
        // single item can be selected
        raceComboBox.setActionCommand("roleList");
        raceComboBox.setSelectedItem(Race.NONE);
    }

    /**
     * Arrange controls within gridbag layout
     */
    protected void arrangeControls(){
        GridBagLayout layout = new GridBagLayout();
        layout.rowWeights = new double[]{0.0, 0.0, 0.10, 0.0, 0.0, 1.0};
        layout.columnWeights = new double[]{0.0, 1.0};
        layout.rowHeights = new int[] {0, 0, 0, 0, 0 , 0};
        layout.columnWidths = new int[] {0, 0};
        setLayout(layout);

        addControlWithConstraints(new JLabel("Name"), 0,0, GridBagConstraints.BOTH);
        addControlWithConstraints(nameTextBox, 1,0, GridBagConstraints.HORIZONTAL);

        addControlWithConstraints(new JLabel("Date Of Birth"), 0,1, GridBagConstraints.BOTH);
        addControlWithConstraints(dobField, 1,1, GridBagConstraints.HORIZONTAL);

        addControlWithConstraints(new JLabel("Race"), 0,2, GridBagConstraints.BOTH);
        addControlWithConstraints(raceComboBox, 1,2, GridBagConstraints.HORIZONTAL);

        addControlWithConstraints(new JLabel("Phone#"), 0,3, GridBagConstraints.BOTH);
        addControlWithConstraints(contactTextBox, 1,3, GridBagConstraints.HORIZONTAL);

        addControlWithConstraints(new JLabel("Address"), 0,4, GridBagConstraints.BOTH);
        addControlWithConstraints(scrollPane, 1,4, GridBagConstraints.HORIZONTAL);
    }

    /**
     * gets person name
     * @return name
     */
    public String getPersonName() {
        return nameTextBox.getText().trim();
    }

    /**
     * gets person's phone number
     * @return phone number
     */
    public String getPersonContact() {
        return contactTextBox.getText().trim();
    }

    /**
     * gets person address
     * @return address
     */
    public String getPersonAddress() {
        return addressTextBox.getText().trim();
    }

    /**
     * Gets person's date of Birth
     * @return Date of Birth as String
     */
    public String getDateOfBirth() {
        String dob = StringHelper.Empty;
        if (dobField != null) {
            String temp = dobField.getText().trim();
            if (StringHelper.isNullOrEmpty(dob)) {
                temp = temp.replace("/", "");
                if (!DateHelper.isEmptyDateString(temp)) {
                    dob = dobField.getText().trim();;
                }
            }
        }

        return dob;
    }

    /**
     * Get person's race
     * @return Race
     */
    public Race getRace(){
        return (Race) raceComboBox.getSelectedItem();
    }

    @Override
    protected boolean isNextAllowed() {
        boolean allowed = (getNextPage() != null) ;

        allowed = allowed && !StringHelper.isNullOrEmpty(nameTextBox.getText()) ;
        allowed = allowed && !PhoneHelper.isNullOrEmpty(contactTextBox.getText()) ;

        if (raceComboBox.isVisible())
            allowed = allowed && (raceComboBox.getSelectedIndex() != -1) ;

        if (dobField.isVisible())
            allowed = allowed && !DateHelper.isEmptyDateString(dobField.getText()) ;

        return allowed;
    }

    @Override
    protected boolean isFinishAllowed() {
        boolean allowed = (getNextPage() == null) ;

        allowed = allowed && !StringHelper.isNullOrEmpty(nameTextBox.getText()) ;
        allowed = allowed && !PhoneHelper.isNullOrEmpty(contactTextBox.getText()) ;

        if (raceComboBox.isVisible())
            allowed = allowed && (raceComboBox.getSelectedIndex() != -1) ;

        if (dobField.isVisible())
            allowed = allowed && !DateHelper.isEmptyDateString(dobField.getText()) ;

        return allowed;
    }
}