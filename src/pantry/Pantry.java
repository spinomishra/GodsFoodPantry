package pantry;

import pantry.data.PantryData;
import pantry.helpers.StringHelper;

/**
 * Pantry class ... Singleton class
 */
public class Pantry {
    /**
     * Singleton instance of pantry manager object
     */
    static Pantry instance;

    /**
     * Pantry data records associated with the singleton pantry object
     */
    PantryData pantryRecords;

    /**
     * Pantry name associated with the singleton pantry object
     */
    private String pantryName;

    /**
     * Constructor
     */
    public Pantry() {
        pantryName = StringHelper.Empty;
    }

    /**
     * Static method to get singleton pantry manager object
     *
     * @return The singleton instance of Pantry object
     */
    public static Pantry getInstance() {
        if (instance == null) {
            instance = new Pantry();
        }

        return instance;
    }

    /**
     * Sets pantry name for this object
     *
     * @param name Name of the pantry
     */
    public void setPantryName(String name) {
        pantryName = name;
    }

    /**
     * Gets pantry name for this object
     *
     * @return The name of the pantry
     */
    public String getPantryName() {
        return pantryName;
    }

    /**
     * Get PantryData object
     *
     * @return pantry data object
     */
    public PantryData get_Data() {
        return pantryRecords;
    }

    /**
     * Open Pantry. Loads pantry data
     */
    public void Open() {
        pantryRecords = new PantryData();
        pantryRecords.Load();
    }

    /**
     * Close Pantry object. Ensures that records are saved
     */
    public void Close() {
        pantryRecords.Save();
    }

    /**
     * Overrides toString() for the Pantry object
     *
     * @return Name of the Pantry
     */
    @Override
    public String toString() {
        return pantryName;
    }
}