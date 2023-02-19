package pantry.interfaces;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * IPantryData interface
 * This interface implementation is used by Data holders that save or load serialized data from files
 */
public interface IPantryData {
    /**
     * Read from input stream
     *
     * @param ois ObjectInputStream object
     */
    void ReadFrom(ObjectInputStream ois) throws IOException, ClassNotFoundException;

    /**
     * Write to output stream
     *
     * @param oos output stream
     * @throws IOException input output exception
     */
    void WriteTo(ObjectOutputStream oos) throws IOException;
}
