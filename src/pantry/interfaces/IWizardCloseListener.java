package pantry.interfaces;

import se.gustavkarlsson.gwiz.Wizard;

/**
 * IWizardCloseListener
 * Implemented by classes that want to listen to wizard's close notifications
 */
public interface IWizardCloseListener {
    /**
     * WizardClosing notification
     * @param wizard wizard raising the notification
     * @param finish true if wizard is closing because "Finish" button is clicked, otherwise because "Close" button
     */
    void wizardClosing(Wizard wizard, boolean finish);
}
