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
     * @return
     */
    public ArrayList<String>    get_Employees() {return employees;}

    /**
     * Gets Donors list
     * @return
     */
    public ArrayList<Donor>     get_Donors() {return donors;}

    /**
     * Gets volunteers list
     * @return
     */
    public ArrayList<Volunteer> get_Volunteers() {return volunteers;}

    /**
     * Load data
     */
    public void Load() {
        FileAdapter recordsFile = new FileAdapter(recordsFileName, this);
        recordsFile.Load();
    }

    public void Save() {
        FileAdapter recordsFile = new FileAdapter(recordsFileName, this);
        recordsFile.Save();
    }

    /**
     * Read from input stream
     * @param ois
     * @throws IOException
     * @throws ClassNotFoundException
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
     * @param oos
     * @throws IOException
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

