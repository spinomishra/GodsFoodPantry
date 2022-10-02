package pantry;
import java.time.*;

class ActivityInfo{
  private Activities     activity;
  private LocalDateTime  start_time;
  private LocalDateTime  end_time;

  public ActivityInfo(){
    activity = Activities.Serving;
    start_time = LocalDateTime.now();
    end_time = LocalDateTime.now();
  }

  public  ActivityInfo(Activities act, LocalDateTime start, int hours){
    activity = act;
    start_time = start;
    end_time = start_time.plusHours(hours);
  }

  public void setActivity(Activities act)
  {
    activity = act;
  }

  public void setActivityHours(LocalDateTime start, int hours)
  {
    start_time = start;
    end_time = start_time.plusHours(hours);
  }

  public Activities getActivity()
  {
    return activity;
  }

  public LocalDateTime getStartTime()
  {
    return start_time ;
  }

  public LocalDateTime getEndTime()
  {
    return end_time;
  }

  public String toString()
  {
    return activity.toString() + "," + start_time.toString() + "," + end_time.toString() ;
  }
}