package pantry.ui.wizard;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

@SuppressWarnings("serial")
public class WizardPage extends AbstractWizardPage implements DocumentListener {
    // Next Page in order
    private AbstractWizardPage nextPage ;

    public WizardPage(String title){
        pageTitle = title;
    }

    /**
     * Add controls with constraints
     * @param component component to be added
     * @param gridx row # in grid bag
     * @param gridy column # in grid bag
     * @param gridFill how the control need to fill the grid row, column
     */
    protected void addControlWithConstraints(JComponent component, int gridx, int gridy, int gridFill){
        GridBagConstraints gbConstraint = new GridBagConstraints();
        gbConstraint.fill = gridFill;
        gbConstraint.gridx = gridx;
        gbConstraint.gridy = gridy;
        gbConstraint.insets = new Insets(5, 5, 5, 5);
        add(component, gbConstraint);
    }

    /**
     * element insert handler
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        this.updateWizardButtons();
    }

    /**
     * element remove handler
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        this.updateWizardButtons();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    public void setNextPage(AbstractWizardPage page) {
        nextPage = page;
    }

    @Override
    protected AbstractWizardPage getNextPage() {
        return nextPage;
    }

    @Override
    protected boolean isCancelAllowed() {
        return true;
    }

    @Override
    protected boolean isPreviousAllowed() {
        return false;
    }

    @Override
    protected boolean isNextAllowed() {
        return (nextPage == null) ? false : true;
    }

    @Override
    protected boolean isFinishAllowed() {
        return (nextPage==null);
    }
}
