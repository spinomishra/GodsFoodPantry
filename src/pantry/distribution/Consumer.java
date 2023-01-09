package pantry.distribution;

import pantry.distribution.ui.ConsumerInfo;
import pantry.helpers.DateHelper;
import pantry.helpers.State;
import pantry.helpers.StringHelper;
import pantry.person.Identity;
import pantry.person.Person;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.Objects;

/**
 * Consumer class represents information about the people who are collecting food from the food pantry.
 */
public class Consumer extends Person {
    /**
     * version number for serialization purposes
     */
    private static final long serialVersionUID = 1L;

    /**
     * Consumer Identity information
     */
    private Identity identityInfo ;

    /**
     * Consumer collecting food for a group
     */
    private boolean group;

    /**
     *  Consumer could be collecting food for all the members of the family or for a community group.
     *  If collecting food for a group, group member states that number of members in the group
     */
    private short   groupMember;

    /**
     * Encrypted and Base64 encoded string for the raw signature data captured from inkpad
     */
    private String  signature;

    /**
     *  Image of the signature... this could be a security risk if stored in decrypted format
     */
    private String  signaturePNG;

    /**
     * Constructor
     * @param name Full name of the consumer
     */
    public Consumer(String name) {
        super(name);
        identityInfo = new Identity();
    }

    /**
     * Constructor
     * @param info The Consumer UI object
     */
    public Consumer(ConsumerInfo info){
        super(info);

        this.identityInfo = new Identity();
        this.identityInfo.IdentityType = ((Identity.IDType) info.idTypeBox.getItemAt(info.idTypeBox.getSelectedIndex())).ordinal();
        this.identityInfo.Number = info.idNumber.getText().trim();

        //if (!StringHelper.isNullOrEmpty(issuedOn.getText()))
        try {
            if (info.issuedOn.getValue() != null) {
                var c = info.issuedOn.getValue().getClass();
                if (c == Date.class)
                {
                    var formatter = DateHelper.getDateFormatter();
                    this.identityInfo.IssuedOn = formatter.format((Date)info.issuedOn.getValue());
                }
            }
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(info.getParent(), "Invalid date in Issued On field");
        }

        try {
            if (info.expiryFld.getValue() != null) {
                var c = info.expiryFld.getValue().getClass();
                if (c == Date.class)
                {
                    var formatter = DateHelper.getDateFormatter();
                    this.identityInfo.ExpiresOn = formatter.format((Date)info.expiryFld.getValue());
                }
            }
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(info.getParent(), "Invalid date in Expires On field");
        }

        int selectedIndex = info.stateBox.getSelectedIndex();
        if (selectedIndex != -1) {
            var  selectedState = (State) info.stateBox.getItemAt(selectedIndex);
            this.identityInfo.IssuedByState = selectedState.toString();
        }

        group = info.groupCheckBox.isSelected();
        groupMember = (short) Integer.parseInt(info.spinner.getValue().toString());

        // collect signature data
        if (info.deskSign1.isSigned()){
            signature = info.deskSign1.getString();
            signaturePNG = info.deskSign1.getPNGString(signature, 160, 120, false);
        }
    }

    /**
     * Gets consumer's identity information object
     * @return The Identity Information
     */
    public Identity getIdentityInfo() {return identityInfo;}

    /**
     * Gets Group flag for the consumer
     * @return true if food is collected on behalf of group
     */
    public boolean isGroupFlagged() {return group;}

    /**
     * Gets group member count information
     * @return Group member count
     */
    public int getGroupMemberCount() { return groupMember;}

    /**
     * Gets signature image
     * @return Image object
     */
    public Image getSignatureImage() {
        Image image = null;
        if (!StringHelper.isNullOrEmpty(signaturePNG)){
            try {
                var b64fmt = new integrisign.desktop.Base64Format();
                byte[] pngBytes = b64fmt.decode64(signaturePNG);
                var inStream = new ByteArrayInputStream(pngBytes);
                if (inStream != null){
                    image = ImageIO.read(inStream);
                    //ImageIO.write((BufferedImage)image, "png", new File("image.png"));
                }
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }

        return image ;
    }

    public String getData(String dataName) throws UnsupportedOperationException{
        Objects.requireNonNull(dataName);
        switch (dataName){
            case "Name": return this.getName();
            case "Phone": return this.getContactNumber();
            case "Address": return this.getAddress();
            case "Expiry": return this.identityInfo.ExpiresOn;
            default:
                throw new UnsupportedOperationException();
        }
    }
}