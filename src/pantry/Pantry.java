package pantry;

import pantry.data.PantryData;

/**
 * Pantry class ... Singleton class
 */
public class Pantry {
  // Singleton instance of pantry manager object
  static Pantry instance ;
  PantryData pantryRecords ;

  /**
   * Constructor
   */
  public Pantry()
  {
  }

  /**
   * Static method to get singleton pantry manager object
   * @return
   */
  public static Pantry getInstance()
  {
    if (instance == null) {
        instance = new Pantry();
    }

    return instance;
  }

  /**
   * Get PantryData object
   * @return pantry data object
   */
  public PantryData get_Data() {
    return pantryRecords;
  }

  /**
   * Open Pantry. Loads pantry data
   */
  public void Open(){
    pantryRecords = new PantryData() ;
    pantryRecords.Load() ;
  }

  /**
   * Close Pantry object. Ensures that records are saved
   */
  public void Close(){
    pantryRecords.Save();
  }
}