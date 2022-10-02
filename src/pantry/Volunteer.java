package pantry;
import java.util.*;

public class Volunteer extends Person
{
  private ArrayList<ActivityInfo> activityHistory;

  // private int hoursWorked;
  // private int year;
  // private int day;
  // private Month month;
  
  // private Month[] months = 
  // {
  //   Month.JANUARY,
  //   Month.FEBRUARY,
  //   Month.MARCH,
  //   Month.APRIL,
  //   Month.MAY,
  //   Month.JUNE,
  //   Month.JULY,
  //   Month.AUGUST,
  //   Month.SEPTEMBER,
  //   Month.OCTOBER,
  //   Month.NOVEMBER,
  //   Month.DECEMBER
  // };

  /* *
  * constucts a Volunteer object with inputted data
  * @param volName the name of the volunteer
  */
  public Volunteer(String volName){
    super(volName);
  }
  
  /* *
  * returns the actvity the volunteer did
  * @return the activity's name
  */
  public ActivityInfo getUpcomingActivity(){

    if (!activityHistory.isEmpty())
    {
      int len = activityHistory.size();
      return activityHistory.get(len-1);
    }
    
    return null;
  }

  public void setUpcomingActivity(ActivityInfo activityInfo){
    activityHistory.add(activityInfo);
  }
}