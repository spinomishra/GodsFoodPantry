package pantry.volunteer;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Class ActivityInfo represents Activity information for the volunteers
 */
public class ActivityInfo implements Serializable {
  /**
   * Version Id for serialization
   */
  private static final long serialVersionUID = 1L;

  /**
   * Activities enumeration
   */
  public enum Activities {
    /**
     * Generic
     */
    Generic,
    /**
     * Food and other content stacking
     */
    Stacking,
    /**
     * Food Packaging
     */
    Packaging,
    /**
     * Food Serving
     */
    Food_Serving,
    /**
     * Cleaning operations
     */
    Cleaning,
    /**
     * Food Delivery - Meals on wheels
     */
    FoodDelivery,
    /**
     * Fresh Produce Harvesting
     */
    Harvesting,
    /**
     * Fund raising
     */
    FundRaising,
  };

  /**
   * Activity
   */
  private Activities activity;
  /**
   * Activity start date time
   */
  private LocalDateTime  start_time;
  /**
   * Activity end date time
   */
  private LocalDateTime  end_time;

  /**
   * Constructor
   */
  public ActivityInfo(){
    activity = Activities.Food_Serving;
    start_time = LocalDateTime.now();
  }

  /**
   * Constructor
   * @param act activity enum
   * @param start start date time
   * @param hours end date time
   */
  public  ActivityInfo(Activities act, LocalDateTime start, int hours){
    activity = act;
    start_time = start;
    end_time = start_time.plusHours(hours);
  }

  /**
   * Constructor
   * @param act activity enum
   * @param checkinTime start date time
   * @param checkoutTime end date time
   */
  public  ActivityInfo(Activities act, LocalDateTime checkinTime, LocalDateTime checkoutTime){
    activity = act;
    start_time = checkinTime;
    end_time = checkoutTime;
  }

  /**
   * Set Activity
   * @param act activity
   */
  public void setActivity(Activities act)
  {
    activity = act;
  }

  /**
   * Set Activity start date time and the length of activiy
   * @param start start date time
   * @param hours end date time
   */
  public void setActivityHours(LocalDateTime start, int hours)
  {
    start_time = start;
    end_time = start_time.plusHours(hours);
  }

  /**
   * Sets activity end time
   * @param end end datetime stamp for the activity
   */
  public void setCheckoutTime(LocalDateTime end){
    end_time = end;
  }

  /**
   * getActivity
   * @return activity for this instance
   */
  public Activities getActivity()
  {
    return activity;
  }

  /**
   * Get activity start date time
   * @return start date time
   */
  public LocalDateTime getStartTime()
  {
    return start_time ;
  }

  /**
   * get activity end time
   * @return end date time
   */
  public LocalDateTime getEndTime()
  {
    return end_time;
  }

  /**
   * convert to string format
   * @return string
   */
  public String toString()
  {
    return activity.toString() + "," + start_time.toString() + "," + end_time.toString() ;
  }
}