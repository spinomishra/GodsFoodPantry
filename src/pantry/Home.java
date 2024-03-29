package pantry;

import pantry.helpers.StringHelper;
import pantry.interfaces.IHome;
import pantry.ui.ExecutionModeSelection;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Home class is the entry point for the PantryWare application
 * This singleton class controls the main dashboard of the application
 */
public abstract class Home {
    /**
     * entry method for application
     *
     * @param args - command line arguments
     *             -mmode=volunteer | manage
     */
    public static void main(String[] args) {
        Pantry pantry = Pantry.getInstance();

        /*
            Load properties from config.properties
            e.g. pantry title
         */
        boolean rememberMe = false;
        String mode = System.getProperty("mode");

        Properties prop = LoadConfiguration();
        if (prop != null) {
            pantry.setPantryName(prop.getProperty("PANTRY_NAME"));
            mode = prop.getProperty("EXECUTION_MODE");
            var tempStr = prop.getProperty("REMEMBER_ME");
            if (!StringHelper.isNullOrEmpty(tempStr)) {
                rememberMe = Integer.parseInt(tempStr) == 1;
            }
        }

        // command line arguments override properties defined in the config.properties
        boolean overrideProperties = false;
        if (args != null) {
            String nextOptionValue = "";
            for (String option : args) {
                switch (option) {
                    case "-m":
                        nextOptionValue = "mode";
                        break;

                    case "-p":
                        nextOptionValue = "title";
                        break;

                    default:
                        if (StringHelper.isNullOrEmpty(nextOptionValue))
                            continue;

                        if (nextOptionValue == "title") {
                            pantry.setPantryName(option);
                        } else if (nextOptionValue == "mode") {
                            mode = option;
                            overrideProperties = true;
                        }
                        nextOptionValue = StringHelper.Empty;
                        break;
                }
            }
        }

        IHome home = null;
        if (!overrideProperties && (StringHelper.isNullOrEmpty(mode) || !rememberMe)) {
            ExecutionModeSelection modeSelector = new ExecutionModeSelection(mode, "Pantryware");
            modeSelector.setVisible(true);
            mode = modeSelector.executionMode;

            // persist properties
            if (!StringHelper.isNullOrEmpty(mode)) {
                if (prop == null)
                    prop = new Properties();

                prop.setProperty("EXECUTION_MODE", mode);

                if (modeSelector.rememberMe) {
                    prop.setProperty("REMEMBER_ME", "1");
                }

                SaveConfiguration(prop);
            }

            modeSelector.dispose();
        }


        switch (mode) {
            case "manage": {
                home = new ManagementHome();
            }
            break;

            case "volunteer": {
                home = new VolunteerHome();
            }
            break;

            case "distribution": {
                home = new DistributionHome();
            }
            break;

            default:
                System.exit(0);
                break;
        }

        // Open pantry now... this will load pantry data
        Pantry.getInstance().Open();
        home.ShowHome();

        /* Create and display the form */
        IHome finalHome = home;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                finalHome.Run();
            }
        });
    }

    /**
     * Gets default tile for various application windows
     */
    public static String getDefaultPageTitle() {
        String pantryName = Pantry.getInstance().getPantryName();
        return (StringHelper.isNullOrEmpty(pantryName)) ? "PantryWare" : "PantryWare - " + pantryName;
    }

    /**
     * Load configuration properties from file config.properties
     *
     * @return Properties object
     */
    private static Properties LoadConfiguration() {
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
            JOptionPane.showMessageDialog(null, "Config.properties couldn't be found at " + currentDirectory);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return prop;
    }

    /**
     * Save application configuration properties
     *
     * @param prop Configuration properties
     */
    private static void SaveConfiguration(Properties prop) {
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
