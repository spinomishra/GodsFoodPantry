package pantry.data;

import pantry.Donor;
import pantry.Volunteer;
import pantry.data.FileAdapter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class PantryData {
    String recordsFileName = "pantry.rec";

    ArrayList<String> employees = null;
    ArrayList<Donor> donors  = null;
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
    public ArrayList<String>    get_Employees() {return employees;}

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
            employees.addAll((ArrayList<String>) ois.readObject());
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
    void Reset(){
        if (employees == null)
            employees = new ArrayList<String>();
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

