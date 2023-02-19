package pantry.person;

/**
 * Class representing human race
 */
public enum Race {
    AMINDIAN(0, "American Indian or Alaska Native", "A person having origins in any of the original peoples of North and South America \n" +
            "(including Central America), and who maintains tribal affiliation or community \n" +
            "attachment."),
    AFRICANAMERICAN(1, "African American", "A person having origins in any of the black racial groups of Africa" ),

    ASIAN(2, "Asian", "A person having origins in any of the original peoples of the Far East, Southeast \n" +
            "Asia, or the Indian subcontinent including, for example, Cambodia, China, In"),
    NATIVEHAWAIIAN(3, "Native Hawaiian or Other Pacific Islander", "A person having origins in any of the original peoples of Hawaii, Guam, Samoa, or \n" +
            "other Pacific Islands."),
    WHITE(4, "White", "A person having origins in any of the original peoples of Europe, the Middle East, or North Africa. "),

    HISPANIC(5, "Hispanic or Latino", "A person of Cuban, Mexican, Puerto Rican, South or Central American, or other \n" +
            "Spanish culture or origin, regardless of race."),

    OTHER(5, "Other", "A person not identifying with any other race."),

    NONE(6, "Don't want to specify", "");

    private final int Value;
    private final String valueName;
    private final String description;

    Race(int numValue, String strId, String description){
        this.Value = numValue;
        this.valueName = strId;
        this.description = description;
    }

    /**
     * gen the numeric id of enum
     *
     * @return int for the enum
     */
    public int getValue() {
        return this.Value;
    }

    /**
     * gen the numeric id of enum
     *
     * @return int for the enum
     */
    public int Value() {
        return this.Value;
    }

    /**
     * Description of the enumeration
     * @return
     */
    public String Description() {return this.description; }

    /**
     * Converting the enum to string
     *
     * @return String for the enum
     */
    @Override
    public String toString() {
        return this.valueName;
    }

    /**
     * Lookup an enum value by string value
     * @param name Race name
     * @return Race enum
     */
    public static Race valueOfLabel(String name){
        for (Race e : values()) {
            if (e.valueName.compareToIgnoreCase(name) == 0) {
                return e;
            }
        }
        return null;
    }

    /**
     * Look up an enum value by race ID
     * @param id Race ID
     * @return Race enum
     */
    public static Race valueOfId(int id){
        for (Race e : values()) {
            if (e.Value == id) {
                return e;
            }
        }
        return null;
    }
}
