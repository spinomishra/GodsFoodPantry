package pantry.person.ui;

import pantry.helpers.DateHelper;
import pantry.helpers.State;
import pantry.helpers.StringHelper;
import pantry.person.Identity;
import pantry.ui.wizard.WizardPage;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class IdentityInfoPage  extends WizardPage {
    public final static String Title = "Identity Info";

    /**
     * Identity type combo box
     */
    private transient JComboBox idTypeBox;

    /**
     * Identity document number control
     */
    private transient JTextField idNumber;

    /**
     * Identity document issued on control
     */
    private transient JFormattedTextField issuedOn;

    /**
     * Identity document expires on control
     */
    private transient JFormattedTextField expiryFld;

    /**
     * Identity document issued by State control
     */
    private transient JComboBox stateBox;

    public IdentityInfoPage() {
        super(Title);
        createControls();
        arrangeControls();
    }

    public Identity.IDType IdType() {return (Identity.IDType) idTypeBox.getSelectedItem();}
    public String  IdNumber() {return idNumber.getText().trim(); }
    public Date IssuedOn() {return (Date)issuedOn.getValue();}
    public Date ExpiresOn() {return (Date)expiryFld.getValue();}
    public State IssuedBy() {return (State)stateBox.getSelectedItem();}

    public void ReadControlsInto(Identity identity) {
        if (identity != null) {
            identity.IdentityType = IdType().ordinal() ;
            identity.Number = IdNumber();

            try {
                identity.IssuedOn = DateHelper.getDateFormatter().format(IssuedOn());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(getWizardController().getWizard().getWizardPageContainer(), "Invalid date in Issued On field");
            }

            try {
                identity.ExpiresOn = DateHelper.getDateFormatter().format(ExpiresOn());
/*                        if (info.expiryFld.getValue() != null) {
                            var c = info.expiryFld.getValue().getClass();
                            if (c == Date.class) {
                                var formatter = DateHelper.getDateFormatter();
                                this.identityInfo.ExpiresOn = formatter.format((Date) info.expiryFld.getValue());
                            }
                        }*/
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(getWizardController().getWizard().getWizardPageContainer(), "Invalid date in Expires On field");
            }

            identity.IssuedByState = IssuedBy().toString();
        }
    }

    private void createControls() {
        // Identity type
        {
            idTypeBox = new JComboBox();
            idTypeBox.setModel(new DefaultComboBoxModel(Identity.IDType.values()));
            idTypeBox.setActionCommand("IdType");
        }

        // Id number
        {
            idNumber = new JTextField();
            idNumber.setActionCommand("idNumber");
            idNumber.getDocument().addDocumentListener(this);
        }

        // Issued On
        {
            try {
                //MaskFormatter dateFmt = new MaskFormatter("##/##/####");
                issuedOn = new JFormattedTextField(DateHelper.getDateFormatter());
                issuedOn.setActionCommand("issuedOn");
                issuedOn.getDocument().addDocumentListener(this);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(getParent(), ex.getMessage());
            }
        }

        // Expire On
        {
            try {
                //MaskFormatter dateFmt = new MaskFormatter("##/##/####");
                expiryFld = new JFormattedTextField(DateHelper.getDateFormatter());
                expiryFld.setActionCommand("expiresOn");
                expiryFld.getDocument().addDocumentListener(this);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(getParent(), ex.getMessage());
            }
        }

        // Issued by
        {
            stateBox = new JComboBox();
            stateBox.setModel(new DefaultComboBoxModel(State.values()));
            stateBox.setActionCommand("State");
            stateBox.setSelectedItem(State.OT);
        }
    }

    /**
     * Arrange controls within gridbag layout
     */
    protected void arrangeControls(){
        GridBagLayout layout = new GridBagLayout();
        layout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
        layout.columnWeights = new double[]{0.0, 1.0};
        layout.rowHeights = new int[] {0, 0, 0, 0, 0 , 0};
        layout.columnWidths = new int[] {0, 0};
        setLayout(layout);

        addControlWithConstraints(new JLabel("Identity Type"), 0,0, GridBagConstraints.BOTH);
        addControlWithConstraints(idTypeBox, 1,0, GridBagConstraints.HORIZONTAL);

        addControlWithConstraints(new JLabel("Identity Number"), 0,1, GridBagConstraints.BOTH);
        addControlWithConstraints(idNumber, 1,1, GridBagConstraints.HORIZONTAL);

        addControlWithConstraints(new JLabel("IssuedOn (mm/dd/yyyy)"), 0,2, GridBagConstraints.BOTH);
        addControlWithConstraints(issuedOn, 1,2, GridBagConstraints.HORIZONTAL);

        addControlWithConstraints(new JLabel("ExpiresOn (mm/dd/yyyy)"), 0,3, GridBagConstraints.BOTH);
        addControlWithConstraints(expiryFld, 1,3, GridBagConstraints.HORIZONTAL);

        addControlWithConstraints(new JLabel("Issued By (State)"), 0,4, GridBagConstraints.BOTH);
        addControlWithConstraints(stateBox, 1,4, GridBagConstraints.HORIZONTAL);
    }

    @Override
    protected boolean isNextAllowed() {
        boolean allowed = super.isNextAllowed();

        allowed = allowed && !StringHelper.isNullOrEmpty(idNumber.getText()) ;
        allowed = allowed && !DateHelper.isEmptyDateString(issuedOn.getText()) ;
        allowed = allowed && !DateHelper.isEmptyDateString(expiryFld.getText()) ;
        allowed = allowed && (State.OT != stateBox.getSelectedItem()) ;

        return allowed;
    }

    @Override
    protected boolean isFinishAllowed() {
        return false;
    }

    @Override
    protected boolean isPreviousAllowed() {
        return true;
    }
}
