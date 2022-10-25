package pantry.food;

import pantry.Pantry;
import pantry.interfaces.IHome;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * home class - entry point for the application
 * This singleton class controls the main dashboard of the application
 */
public abstract class Home {
    /**
     * entry method for application
     * @param args - command line arguments
     *             -mmode=volunteer | manage
     */
    public static void main(String[] args) {
        IHome home = null;
        String mode = System.getProperty("mode");
        /*
            Load properties from config.properties
            properties e.g. pantry title
         */
        String pantryName = "";

        try {
            String configFilePath = "src/config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);

            Properties prop = new Properties();
            prop.load(propsInput);

            pantryName = prop.getProperty("PANTRY_NAME");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if((mode == null) || !mode.trim().isEmpty())
            mode = "manage";

        // show the application in the specific mode as requested by caller
        switch (mode){
            // home screen is volunteer home
            case "volunteer":
                //home = new VolunteerHome();
                break;

            default:
                home =new ManagementHome(pantryName);
                break;
        }

        Pantry.getInstance().Open();

        assert home != null;
        home.ShowHome() ;

        /* Create and display the form */
        IHome finalHome = home;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                finalHome.Run();
            }
        });

        Pantry.getInstance().Close();
    }
}
