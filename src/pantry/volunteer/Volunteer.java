package pantry.volunteer;

import pantry.helpers.DateHelper;
import pantry.person.Person;
import pantry.person.ui.PersonInfo;
import pantry.volunteer.ui.VolunteerInfo;

import java.util.ArrayList;
import java.util.ListIterator;

public class Volunteer extends Person
{
  /**
   * Version number for serialization purposes
   */
  private static final long serialVersionUID = -4310261792334880336L;

  /**
   * Volunteer activity history
   */
  private ArrayList<ActivityInfo> activityHistory ;

  /**
  * Constructs a Volunteer object
  * @param volName the name of the volunteer
  */
  public Volunteer(String volName){
    super(volName);
    activityHistory = new ArrayList<>();
  }

  /**
   * Constructor
   * @param vInfo Volunteer Info
   */
  public Volunteer(VolunteerInfo vInfo){
    super((PersonInfo) vInfo);
    activityHistory = new ArrayList<>();
    activityHistory.add(vInfo.getRecentActivity());
  }

  /**
  * returns the activity the volunteer did
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

  /**
   * Gets ongoing volunteering activity
   * @return The activity information
   */
  public ActivityInfo getOngoingActivity() {
    if (!activityHistory.isEmpty())
    {
      // search activity from reverse to identify activities that
      // started today but haven't been recorded as complete
      ListIterator<ActivityInfo> it = activityHistory.listIterator(activityHistory.size());
      while (it.hasPrevious()){
        ActivityInfo aInfo = it.previous();
        if (DateHelper.isToday(DateHelper.fromLocalDateTime(aInfo.getStartTime())))
          return aInfo ;
      }
    }

    return null;
  }

  /**
   * Gets activity history for this volunteer
   * @return The activity history
   */
  public ArrayList<ActivityInfo> getActivityHistory() {return activityHistory;}
  /**
   * Records new activity information
   * @param activityInfo
   */
  public void recordActivity(ActivityInfo activityInfo){
    activityHistory.add(activityInfo);
  }

  /**
   * Updates volunteer information with information from VolunteerInfo object
   * @param vInfo
   */
  public void Update(VolunteerInfo vInfo){
    if (vInfo != null)
    {
        setContactPhone(vInfo.getPersonContact());
        recordActivity(vInfo.getRecentActivity());
    }
  }
}