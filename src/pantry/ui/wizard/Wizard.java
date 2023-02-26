package pantry.ui.wizard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Wizard {
    // Outer shell of the wizard
    private JDialog wizard;
    // Wizard model
    private WizardModel wizardModel;
    // Wizard controller
    private WizardController wizardController;

    private JPanel cardPanel;
    private CardLayout cardLayout;

    // Back button
    private JButton backButton;

    // Next Button
    private JButton nextButton;

    // Cancel Button
    private JButton cancelButton;

    private int returnCode;

    public Wizard(Frame owner) {

        //wizardModel = new WizardModel();
        wizard = new JDialog(owner);
        wizardController = new WizardController(this);

        initComponents();
    }

    private void initComponents() {

        JPanel buttonPanel = new JPanel();
        Box buttonBox = new Box(BoxLayout.X_AXIS);

        cardPanel = new JPanel();
        cardPanel.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));

        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        backButton = new JButton();
        nextButton = new JButton();
        cancelButton = new JButton();

        backButton.addActionListener(wizardController);
        nextButton.addActionListener(wizardController);
        cancelButton.addActionListener(wizardController);

        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(new JSeparator(), BorderLayout.NORTH);

        buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
        buttonBox.add(backButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(nextButton);
        buttonBox.add(Box.createHorizontalStrut(30));
        buttonBox.add(cancelButton);
        buttonPanel.add(buttonBox, java.awt.BorderLayout.EAST);

        wizard.getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
        wizard.getContentPane().add(cardPanel, java.awt.BorderLayout.CENTER);
    }

    public void addWizardPage(Object id, WizardPanelDescriptor panel)
    {
        cardPanel.add(panel.getPanelComponent(), id);
        wizardModel.registerPanel(id, panel);
    }
}
