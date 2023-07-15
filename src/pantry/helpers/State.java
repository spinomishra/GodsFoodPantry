package pantry.helpers;

/**
 * United States's State Enumerator
 */
public enum State {
    AL("Alabama"),
    MT("Montana"),
    AK("Alaska"),
    NE("Nebraska"),
    AZ("Arizona"),
    NV("Nevada"),
    AR("Arkansas"),
    NH("NewHampshire"),
    CA("California"),
    NJ("NewJersey"),
    CO("Colorado"),
    NM("NewMexico"),
    CT("Connecticut"),
    NY("NewYork"),
    DE("Delaware"),
    NC("NorthCarolina"),
    FL("Florida"),
    ND("NorthDakota"),
    GA("Georgia"),
    OH("Ohio"),
    HI("Hawaii"),
    OK("Oklahoma"),
    ID("Idaho"),
    OR("Oregon"),
    IL("Illinois"),
    PA("Pennsylvania"),
    IN("Indiana"),
    RI("RhodeIsland"),
    IA("Iowa"),
    SC("SouthCarolina"),
    KS("Kansas"),
    SD("SouthDakota"),
    KY("Kentucky"),
    TN("Tennessee"),
    LA("Louisiana"),
    TX("Texas"),
    ME("Maine"),
    UT("Utah"),
    MD("Maryland"),
    VT("Vermont"),
    MA("Massachusetts"),
    VA("Virginia"),
    MI("Michigan"),
    WA("Washington"),
    MN("Minnesota"),
    WV("WestVirginia"),
    MS("Mississippi"),
    WI("Wisconsin"),
    MO("Missouri"),
    WY("Wyoming"),
    OT("");

    private final String state;

    /**
     * Constructor
     *
     * @param state State's string
     */
    State(String state) {
        this.state = state;
    }

    /**
     * Converting the enum to string
     *
     * @return String for the enum
     */
    @Override
    public String toString() {
        return this.state;
    }

    //Enums don't allow to override Equals and CompareTo() as that would violate the
    //principle of least astonishment - It proposes that a component of a system
    //should behave in a way that most users will expect it to behave. The behavior
    //should not astonish or surprise users. The following is a formal statement
    //of the principle: "If a necessary feature has a high astonishment factor, it
    //may be necessary to redesign the feature
    //Two enum constants are expected to be equal if and only if they are the same object
    //and the ability to override this behavior would be error prone.
}