package pantry.volunteer;
import pantry.volunteer.Activities;

import java.time.*;

/**
 * Class ActivityInfo represents Activity information for the volunteers
 */
class ActivityInfo{
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
    activity = Activities.Serving;
    start_time = LocalDateTime.now();
    end_time = LocalDateTime.now();
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