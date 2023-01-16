package pantry.person;

import pantry.helpers.DateHelper;
import pantry.helpers.PhoneHelper;
import pantry.helpers.StringHelper;
import pantry.person.ui.PersonInfo;

import javax.swing.*;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

/**
 * Person class represents any human. It collects all necessary information like name and contact info.
 * No personally identifiable information is collected.
 */
public abstract class Person implements Serializable {
    private static final long serialVersionUID = -41842788452212300L;
    /**
     * Unique identifier assigned to the person
     */
    private int Id;

    /**
     * First Name
     */
    private String fName;
    /**
     * Last Name
     */
    private String lName;
    /**
     * Mailing address
     */
    private String Address;
    /**
     * Contact Mobile number
     */
    private String Mobile_number;

    /**
     * Date of Birth
     */
    private Date DOB;

    /**
     * Constructor
     *
     * @param fname first name
     * @param lname last name
     */
    public Person(String fname, String lname) {
        generateId();
        fName = fname;
        lName = lname;
    }

    /**
     * Constructor
     *
     * @param fullname Full name
     */
    public Person(String fullname) {
        String[] tokens = fullname.split(" ");
        if (tokens == null)
            fName = fullname;
        else {
            if (tokens.length >= 1)
                fName = tokens[0];
            if (tokens.length >= 2)
                lName = tokens[1];
        }

        generateId();
    }

    /**
     * Constructor
     *
     * @param fname   First name
     * @param lname   Last name
     * @param address Mailing address
     */
    public Person(String fname, String lname, String address) {
        this(fname, lname);
        setAddress(address);
    }

    /**
     * Constructor
     *
     * @param fname    First name
     * @param lname    Last name
     * @param address  Mailing address
     * @param phone_no Contact mobile number
     */
    public Person(String fname, String lname, String address, String phone_no) {
        this(fname, lname);
        setAddress(address);
        setContactPhone(phone_no);
    }

    /**
     * Constructor
     *
     * @param pInfo Personal Information
     */
    public Person(PersonInfo pInfo) {
        this(pInfo.getPersonName());

        setAddress(pInfo.getPersonAddress());
        setContactPhone(pInfo.getPersonContact());
        setDateOfBirth(pInfo.getDateOfBirth());
    }

    /**
     * sets address for the person
     *
     * @param address address
     */
    public void setAddress(String address) {
        if (!StringHelper.isNullOrEmpty(address))
            Address = address;
    }

    /**
     * sets contact phone number for the person
     *
     * @param phone_no contact mobile phone number
     */
    public void setContactPhone(String phone_no) {
        if (!PhoneHelper.isNullOrEmpty(phone_no))
            Mobile_number = phone_no;
    }

    /**
     * Sets person's Date of Birth
     *
     * @param dobStr Date of Birth
     */
    public void setDateOfBirth(String dobStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        setDateOfBirth(dobStr, formatter);
    }

    /**
     * Sets person's Date of Birth
     *
     * @param dobStr    Date of Birth
     * @param formatter Formatter to use to part string DOB
     */
    public void setDateOfBirth(String dobStr, DateTimeFormatter formatter) {
        try {
            if (!StringHelper.isNullOrEmpty(dobStr)) {
                LocalDate date = LocalDate.parse(dobStr, formatter);
                DOB = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Pantryware - Exception", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * returns the name of the person
     *
     * @return person's name
     */
    public String getName() {
        if (StringHelper.isNullOrEmpty(lName))
            return fName;
        else
            return (fName + " " + lName).trim();
    }

    /**
     * returns person's address
     *
     * @return person's address
     */
    public String getAddress() {
        return Address;
    }

    /**
     * returns person's contact number
     *
     * @return person's phone number
     */
    public String getContactNumber() {
        return Mobile_number;
    }

    /**
     * Gets Person's date of birth
     *
     * @return Date of Birth for the person
     */
    public String getDateOfBirth() {
        if (DOB == null)
            return StringHelper.Empty;

        Format formatter = new SimpleDateFormat("MMM dd yyyy");
        return formatter.format(DOB);
    }

    /**
     * Get person's age
     *
     * @return Persons age if DOB is available, else returns -1
     */
    public int getAge() {
        if (DOB == null)
            return -1;

        return DateHelper.YearsFromDate(DOB);
    }

    /**
     * Gets unique Id assigned to the person
     *
     * @return The unique Identifier
     */
    public int getUniqueId() {
        return Id;
    }

    /**
     * Securely generates an identity number for the person
     */
    private void generateId() {
        // The java.security.SecureRandom class is widely used for generating cryptographically strong random numbers.
        // Deterministic random numbers have been the source of much software security breaches. The idea is that an
        // adversary (hacker) should not be able to determine the original seed given several samples of random numbers.
        // If this restriction is violated, all future random numbers may be successfully predicted by the adversary.
        try {
            SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");

            // Get 128 random bytes
            byte[] randomBytes = new byte[128];
            secureRandomGenerator.nextBytes(randomBytes);

            //Get random long
            Id = secureRandomGenerator.nextInt();
        } catch (NoSuchAlgorithmException e) {
            generateUnsecureId();
        } catch (NoSuchProviderException e) {
            generateUnsecureId();
        }
    }

    /**
     * Generates random unique identifier.
     * THis is cryptographically weak random number
     *
     * @return The unique identifier
     */
    private void generateUnsecureId() {
        Random random = new Random();
        Id = random.nextInt();
        if (Id == 0)
            Id = random.nextInt();
    }

    /**
     * formats the person object as a string
     *
     * @return the string format of object
     */
    public String toString() {
        String s = getName();
        if (!StringHelper.isNullOrEmpty(getContactNumber()))
            s = s + "   " + getContactNumber();
        return s;
    }

    /**
     * compares the Persons object with another Person object
     *
     * @param obj the object to be compared with
     * @return the lexigraphical comparison between the two objects
     */
    public int compareTo(Object obj) {
        if (this == obj)
            return 0;

        int comp = -1;
        if (obj != null) {
            if (!(obj instanceof Person))
                return -1;

            Person p = (Person) obj;
            comp = Long.compare(Id, p.Id);

            if (comp == 0) {
                comp = fName.compareTo(p.fName);
                if (comp == 0)
                    comp = lName.compareTo(p.lName);
            }
        }

        return comp;
    }

    /**
     * checks if two Persons are the same
     *
     * @param obj the object to be compared with
     * @return whether the two are the same
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj != null) {
            if (!(obj instanceof Person))
                return false;

            return this.Id == ((Person) obj).Id;
        }

        return false;
    }
}