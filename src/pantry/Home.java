package pantry;

import pantry.helpers.StringHelper;
import pantry.interfaces.IHome;

import javax.swing.*;
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
            e.g. pantry title
         */
        String pantryName = "";

        try {
            String configFilePath = "src/config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);

            Properties prop = new Properties();
            prop.load(propsInput);

            pantryName = prop.getProperty("PANTRY_NAME");
            mode = prop.getProperty("EXECUTION_MODE");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (StringHelper.isNullOrEmpty(mode)) {
            int response = JOptionPane.showConfirmDialog(null, "Are you an employee? Press Cancel if you want to exit. ", "Pantryware", JOptionPane.YES_NO_CANCEL_OPTION);
            switch (response) {
                case JOptionPane.YES_OPTION: {
                    mode = "manage";
                }
                break;

                case JOptionPane.NO_OPTION: {
                    mode = "volunteer";
                }
                break;

                case JOptionPane.CANCEL_OPTION:
                    System.exit(0);
                    break;
            }
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
}
