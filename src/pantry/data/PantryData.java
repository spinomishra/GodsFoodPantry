package pantry.data;

import pantry.employee.Employee;
import pantry.helpers.DateHelper;
import pantry.helpers.StringHelper;
import pantry.interfaces.IPantryData;
import pantry.volunteer.ActivityInfo;
import pantry.volunteer.Volunteer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * This class represents Pantry Data structures
 */
public class PantryData implements IPantryData {
    /**
     * Pantry data filename - pantry.rec
     */
    private final String recordsFileName = "pantry.rec";

    /**
     * Employees record list
     */
    private ArrayList<Employee> employees;

    /**
     * Volunteers record list
     */
    private ArrayList<Volunteer> volunteers;

    /**
     * Constructor
     */
    public PantryData() {
        Reset();
    }

    /**
     * Gets Employees list
     *
     * @return employee list
     */
    public ArrayList<Employee> get_Employees() {
        return employees;
    }

    /**
     * Gets volunteers list
     *
     * @return volunteer list
     */
    public ArrayList<Volunteer> get_Volunteers() {
        return volunteers;
    }

    /**
     * Load data from pantry records file
     */
    public void Load() {
        FileAdapter recordsFile = new FileAdapter(recordsFileName, this);
        recordsFile.Load();
    }

    /**
     * Save pantry records in the file
     */
    public void Save() {
        FileAdapter recordsFile = new FileAdapter(recordsFileName, this);
        recordsFile.Save();
    }

    /**
     * Search Volunteers in the volunteer record set
     *
     * @param name     Volunteer name to be searched
     * @param phone_no Volunteer phone number
     * @return list of volunteers (return empty list if no match found)
     */
    public ArrayList<Volunteer> searchVolunteers(String name, String phone_no) {
        ArrayList<Volunteer> matchList = new ArrayList<Volunteer>();
        boolean usePhoneNumberToo = !StringHelper.isNullOrEmpty(phone_no);
        for (Volunteer v : volunteers) {
            if (v.getName().compareToIgnoreCase(name) == 0 &&
                    (!usePhoneNumberToo || v.getContactNumber().compareToIgnoreCase(phone_no) == 0)) {
                matchList.add(v);
            }
        }

        return matchList;
    }


    /**
     *
     * @return list of volunteers (return empty list if no match found)
     */
    /**
     * Search Volunteers in the volunteer record set that volunteered in that last x days
     *
     * @param xDays Last n number of days
     * @return List of volunteers that volunteered in last xDays. The list could be empty in case no one volunteered in last x days.
     */
    public ArrayList<Volunteer> searchVolunteers(int xDays) {
        LocalDateTime todayDT = LocalDateTime.now();
        Date lastDay = DateHelper.fromLocalDateTime(todayDT.minusDays(xDays + 1));
        ArrayList<Volunteer> matchList = new ArrayList<Volunteer>();
        for (Volunteer v : volunteers) {
            var activityHistory = v.getActivityHistory();
            if (activityHistory != null) {
                for (ActivityInfo activityInfo : activityHistory) {
                    if (activityInfo.getStartTime() != null) {
                        Date checkinDate = DateHelper.fromLocalDateTime(activityInfo.getStartTime());
                        if (checkinDate.after(lastDay))
                            matchList.add(v);
                    }
                }
            }
        }

        return matchList;
    }

    /**
     * Read from input stream
     *
     * @param ois ObjectInputStream type
     * @throws IOException            Input output exception
     * @throws ClassNotFoundException Class not found exception
     */
    public void ReadFrom(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        Objects.requireNonNull(ois);

        Reset();
        volunteers.addAll((ArrayList<Volunteer>) ois.readObject());
        employees.addAll((ArrayList<Employee>) ois.readObject());
    }

    /**
     * Write to output stream
     *
     * @param oos output stream
     * @throws IOException input output exception
     */
    public void WriteTo(ObjectOutputStream oos) throws IOException {
        Objects.requireNonNull(oos);

        oos.writeObject(volunteers);
        oos.writeObject(employees);
    }

    /**
     * Reset the data
     */
    private void Reset() {
        if (employees == null)
            employees = new ArrayList<Employee>();
        else
            employees.clear();

        if (volunteers == null)
            volunteers = new ArrayList<Volunteer>();
        else
            volunteers.clear();
    }
}