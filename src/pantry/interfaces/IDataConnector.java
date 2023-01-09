package pantry.interfaces;

import java.io.InputStream;

/**
 * IDataConnector is an interface that will be implemented by any pantry.data adapters that supports pantry records persistence
 */
public interface IDataConnector {
    /**
     * Load persistence records
     */
    void Load();

    /**
     * Persist records
     */
    void Save();

    /**
     * Close the connection
     */
    void Close() ;
}
