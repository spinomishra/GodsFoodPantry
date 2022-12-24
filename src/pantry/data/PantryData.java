package pantry.data;

import pantry.donor.Donor;
import pantry.employee.Employee;
import pantry.helpers.StringHelper;
import pantry.volunteer.Volunteer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Pantry data structure
 */
public class PantryData {
    // pantry data is stored in pantry.rec file
    String recordsFileName = "pantry.rec";

    // employees list
    ArrayList<Employee> employees = null;
    // donors list
    ArrayList<Donor> donors  = null;
    // volunteers list
    ArrayList<Volunteer> volunteers  = null;

    /**
     * Constructor
     */
    public PantryData(){
        Reset();
    }

    /**
     * Gets Employees list
     * @return employee list
     */
    public ArrayList<Employee>    get_Employees() {return employees;}

    /**
     * Gets Donors list
     * @return donors list
     */
    public ArrayList<Donor>     get_Donors() {return donors;}

    /**
     * Gets volunteers list
     * @return volunteer list
     */
    public ArrayList<Volunteer> get_Volunteers() {return volunteers;}

    /**
     * Load data
     */
    public void Load() {
        FileAdapter recordsFile = new FileAdapter(recordsFileName, this);
        recordsFile.Load();
    }

    /**
     * Save records in the file
     */
    public void Save() {
        FileAdapter recordsFile = new FileAdapter(recordsFileName, this);
        recordsFile.Save();
    }

    /**
     * Search Volunteers in the volunteer record set
     * @param name Volunteer name to be searched
     * @param phone_no Volunteer phone number
     * @return list of volunteers (return empty list if no match found)
     */
    public ArrayList<Volunteer> searchVolunteers(String name, String phone_no){
        ArrayList<Volunteer> matchList = new ArrayList<Volunteer>();
        boolean usePhoneNumberToo = !StringHelper.isNullOrEmpty(phone_no);
        for (Volunteer v:volunteers) {
            if (v.getName().compareToIgnoreCase(name) == 0 &&
                    (usePhoneNumberToo ? v.getContactNumber().compareToIgnoreCase(phone_no) == 0 : true)){
                matchList.add(v);
            }
        }

        return matchList;
    }

    /**
     * Read from input stream
     * @param ois ObjectInputStream type
     * @throws IOException Input output exception
     * @throws ClassNotFoundException Class not found exception
     */
    protected void ReadFrom(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        if (ois != null) {
            Reset() ;
            volunteers.addAll((ArrayList<Volunteer>) ois.readObject());
            donors.addAll((ArrayList<Donor>) ois.readObject());
            employees.addAll((ArrayList<Employee>) ois.readObject());
        }
    }

    /**
     * Write to output stream
     * @param oos output stream
     * @throws IOException input output exception
     */
    protected void WriteTo(ObjectOutputStream oos) throws IOException {
        if (oos != null) {
            oos.writeObject(volunteers);
            oos.writeObject(donors);
            oos.writeObject(employees);
        }
    }

    /**
     * Reset the data
     */
    private void Reset(){
        if (employees == null)
            employees = new ArrayList<Employee>();
        else
            employees.clear();

        if (donors == null)
            donors  = new ArrayList<Donor>();
        else
            donors.clear();

        if (volunteers == null)
            volunteers  = new ArrayList<Volunteer>();
        else
            volunteers.clear();
    }
}

