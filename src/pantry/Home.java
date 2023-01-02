package pantry;

import pantry.helpers.StringHelper;
import pantry.interfaces.IHome;
import pantry.ui.ExecutionModeSelection;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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
            e.g. pantry title
         */
        String pantryName = "";
        boolean rememberMe=false;

        Properties prop = LoadConfiguration() ;
        if (prop != null) {
            pantryName = prop.getProperty("PANTRY_NAME");
            mode = prop.getProperty("EXECUTION_MODE");
            var tempStr = prop.getProperty("REMEMBER_ME");
            if (!StringHelper.isNullOrEmpty(tempStr)){
                rememberMe = (Integer.parseInt(tempStr) == 1) ? true : false;
            }
        }

        if (StringHelper.isNullOrEmpty(mode) || rememberMe == false) {
            ExecutionModeSelection modeSelector = new ExecutionModeSelection(mode, "Pantryware");
            modeSelector.setVisible(true);
            mode = modeSelector.executionMode;

            if (!StringHelper.isNullOrEmpty(mode)) {
                prop.setProperty("EXECUTION_MODE", mode);

                if (modeSelector.rememberMe) {
                    // persist the mode information
                    if (prop == null)
                        prop = new Properties();

                    prop.setProperty("REMEMBER_ME", "1");
                }

                SaveConfiguration(prop);
            }

            modeSelector.dispose();
        }


        switch (mode){
            case "manage": {
                home = new ManagementHome(pantryName);
            }
            break;

            case "volunteer": {
                home = new VolunteerHome(pantryName);
            }
            break;

            case "distribution": {
                home = new DistributionHome(pantryName);
            }
            break;

            default:
                System.exit(0);
                break;
        }

        Pantry.getInstance().Open();
        home.ShowHome() ;

        /* Create and display the form */
        IHome finalHome = home;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                finalHome.Run();
            }
        });
    }

    /**
     * Load configuration properties from file config.properties
     * @return Properties object
     */
    private static Properties LoadConfiguration(){
        String currentDirectory = System.getProperty("user.dir");
        Properties prop = null;
        try {
            String configFilePath = "config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            prop = new Properties();
            prop.load(propsInput);
            propsInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;
    }

    private static void SaveConfiguration(Properties prop){
        try {
            String configFilePath = "config.properties";
            var propsOutput = new FileOutputStream(configFilePath);
            prop.store(propsOutput, null);
            propsOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
