package pantry.person;

import java.io.Serializable;

/**
 * Identity class represents information used for person identification
 */
public class Identity implements Serializable {
    // version number for serialization purposes
    private static final long serialVersionUID = 1L;

    /**
     * Identity type used for person identification
     */
    public enum IDType {
        DriverLicense, // Driver License
        PassPort, // Passport
        StateId, // State issued Identity
    }

    /**
     * Identity proof type
     */
    public int IdentityType;

    /**
     * Identity number
     */
    public String Number;

    /**
     * Date when ID was issued
     */
    public String IssuedOn;

    /**
     * Dare when ID will expiry
     */

    public String ExpiresOn;
    /**
     * State that issues ID
     */
    public String IssuedByState;
}
