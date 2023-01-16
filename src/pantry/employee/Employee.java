package pantry.employee;

import pantry.dataprotection.Hash;
import pantry.helpers.PhoneHelper;
import pantry.person.Person;


/**
 * Class representing an Employee. This class extends Person class with additional information for the employee.
 */
public class Employee extends Person {
    /**
     * Version number for serialization purposes
     */
    private static final long serialVersionUID = 1L;

    /**
     * Employee Role enum
     */
    public enum EmployeeRole {Owner, Manager, Cook, Cleaner, Assistant, Validator, Accountant, Driver, Undecided}

    /**
     * Employee Role
     */
    private EmployeeRole Role;

    /**
     * One way hash of Employee's Social Security NUmber
     */
    private String SSNHash;

    /**
     * Constructs a Employee object with data
     *
     * @param name the name of the employee
     */
    public Employee(String name) {
        super(name);
        Role = EmployeeRole.Undecided;
    }

    /**
     * constructs a Employee object with data
     *
     * @param name the name of the employee
     * @param role employee role
     */
    public Employee(String name, EmployeeRole role) {
        super(name);
        setRole(role);
    }

    /**
     * Gets employee role
     *
     * @return employee role
     */
    public EmployeeRole getRole() {
        return Role;
    }

    /**
     * Sets employee role
     *
     * @param role Employee role
     */
    public void setRole(EmployeeRole role) {
        Role = role;
    }

    /**
     * Gets social security number.
     * Since Social security number is never preserved, we do not return a valid SSN.
     *
     * @return anonymized social security
     */
    public String getSSN() {
        // display SSN in clear only if super admin
        return "**-**-****";
    }

    /**
     * Set SSN hash for the employee
     *
     * @param hash
     */
    public void setSSN(String hash) {
        SSNHash = hash;
    }

    /**
     * Validate Social Security Number
     *
     * @param ssn Social Security Number
     * @return true if hash compares to known SSN
     */
    public boolean validateSSN(String ssn) {
        if (ssn != null && !ssn.isEmpty()) {
            String ssnHash = Hash.Sha2Hash(ssn.toCharArray());
            return (ssnHash.compareTo(SSNHash) == 0);
        }

        return false;
    }

    /**
     * compares the Employee object with another Person object
     *
     * @param obj the object to be compared with
     * @return the lexigraphical comparison between the two objects
     */
    public int compareTo(Object obj) {
        if (this == obj)
            return 0;

        int comp = -1;
        if (obj != null) {
            if (!(obj instanceof Employee))
                return -1;

            Person p = (Person) obj;
            comp = super.compareTo(obj);

        }

        return comp;
    }

    /**
     * checks if two Employees are the same
     *
     * @param obj the object to be compared with
     * @return whether the two are the same
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj != null)
            return super.equals(obj);

        return false;
    }

    /**
     * formats the data of the Volunteer as a string
     *
     * @return the string format of the data
     */
    public String toString() {
        String address = getAddress();
        String phoneNo = getContactNumber();

        String str = getName();
        str = str + ", " + Role.toString();
        if (!PhoneHelper.isNullOrEmpty(phoneNo))
            str = str + ": ";

        return str;
    }
}