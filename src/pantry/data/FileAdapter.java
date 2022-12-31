package pantry.data;

import pantry.interfaces.IDataConnector;
import pantry.interfaces.IPantryData;

import java.io.*;
import java.util.Objects;

/**
 * Persistent records in a local file
 */
public class FileAdapter implements IDataConnector {
    String fileName ;
    FileInputStream fis;
    IPantryData pantryData;
    ObjectInputStream ois;

    /**
     * Constructor
     * @param fn filename
     * @param data pantry data
     */
    public FileAdapter(String fn, IPantryData data){
        Objects.requireNonNull(fn, "Requires filename");
        Objects.requireNonNull(data, "Requires pantry data object");

        fileName = fn;
        pantryData = data;
    }

    /**
     * Load persistence records
     */
    @Override
    public void Load() {
        try
        {
            fis = new FileInputStream(fileName);
            ois = new ObjectInputStream(fis);

            if (ois != null) {
                pantryData.ReadFrom(ois);
            }

            ois.close();
            fis.close();
        }
        catch (FileNotFoundException fife){
            System.out.println("No records exist");
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Persist records
     */
    @Override
    public void Save() {
        try
        {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            pantryData.WriteTo(oos);

            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    /**
     * Close the connection
     */
    @Override
    public void Close() {
        Save() ;
    }
}
