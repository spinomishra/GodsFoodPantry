package pantry.ui.wizard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;

import pantry.interfaces.IWizardCloseListener;
import se.gustavkarlsson.gwiz.Wizard;

/**
 * A very simple <code>Wizard</code> implementation that suits the most basic needs. Extends {@link JFrame} and has
 * navigation buttons at the bottom.
 *
 */
public class WizardDialog extends JDialog implements Wizard {
    private static final long serialVersionUID = 2818290889333414291L;

    private static final Dimension defaultMinimumSize = new Dimension(500, 400);

    private final JPanel wizardPageContainer = new JPanel(new GridLayout(1, 1));

    /**
     * Cancel Button
     */
    private final JButton cancelButton = new JButton("Cancel");

    /**
     * Previous Button
     */
    private final JButton previousButton = new JButton("Previous");

    /**
     * Next Button
     */
    private final JButton nextButton = new JButton("Next");

    /**
     * Finish Button
     */
    private final JButton finishButton = new JButton("Finish");

    /**
     * listener that receives closing notification
     */
    private final ArrayList<IWizardCloseListener> closingListeners = new ArrayList<IWizardCloseListener>();


    /**
     * Creates an <code>JFrameWizard</code> with a title and <code>GraphicsConfiguration</code>.
     * @param parent
     *          the parent of the dialog
     *
     * @param title
     *            the title of the frame
     * @param gc
     *            the <code>GraphicsConfiguration</code> of the frame
     * @see JDialog
     */
    public WizardDialog(Window parent,  String title, GraphicsConfiguration gc) {
        super(parent, title, ModalityType.DOCUMENT_MODAL, gc);
        setupWizard();
    }

    /**
     * Creates an <code>JFrameWizard</code> with a title.
     *
     * @param parent
     *          the parent of the dialog
     *
     * @param title
     *            the title of the frame
     * @see JFrame
     */
    public WizardDialog(Window parent,  String title) {
        super(parent, title, ModalityType.DOCUMENT_MODAL);
        setupWizard();
    }

    /**
     * Sets up wizard upon construction.
     */
    private void setupWizard() {
        setupComponents();
        layoutComponents();

        setMinimumSize(defaultMinimumSize);

        // Center on screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int xPosition = (screenSize.width / 2) - (defaultMinimumSize.width / 2);
        int yPosition = (screenSize.height / 2) - (defaultMinimumSize.height / 2);
        setLocation(xPosition, yPosition);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Sets up the components of the wizard with listeners and mnemonics.
     */
    private void setupComponents() {
        WizardDialog wizardDialog = this ;
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (closingListeners != null){
                    for (IWizardCloseListener listener : closingListeners) {
                        if (listener != null)
                            listener.wizardClosing(wizardDialog, false);
                    }
                }
                dispose();
            }
        });

        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (closingListeners != null){
                    for (IWizardCloseListener listener : closingListeners) {
                        if (listener != null)
                            listener.wizardClosing(wizardDialog, true);
                    }
                }
                dispose();
            }
        });

        cancelButton.setMnemonic(KeyEvent.VK_C);
        previousButton.setMnemonic(KeyEvent.VK_P);
        nextButton.setMnemonic(KeyEvent.VK_N);
        finishButton.setMnemonic(KeyEvent.VK_F);

        wizardPageContainer.addContainerListener(new MinimumSizeAdjuster());
    }

    /**
     * Lays out the components in the wizards content pane.
     */
    private void layoutComponents() {
        GridBagLayout layout = new GridBagLayout();
        layout.rowWeights = new double[]{1.0, 0.0, 0.0};
        layout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0};
        layout.rowHeights = new int[] {0, 0, 0};
        layout.columnWidths = new int[] {0, 0, 0, 0, 0};
        getContentPane().setLayout(layout);

        GridBagConstraints wizardPageContainerConstraint = new GridBagConstraints();
        wizardPageContainerConstraint.gridwidth = 5;
        wizardPageContainerConstraint.fill = GridBagConstraints.BOTH;
        wizardPageContainerConstraint.gridx = 0;
        wizardPageContainerConstraint.gridy = 0;
        wizardPageContainerConstraint.insets = new Insets(5, 5, 5, 5);
        getContentPane().add(wizardPageContainer, wizardPageContainerConstraint);

        GridBagConstraints separatorConstraints = new GridBagConstraints();
        separatorConstraints.gridwidth = 5;
        separatorConstraints.fill = GridBagConstraints.HORIZONTAL;
        separatorConstraints.gridx = 0;
        separatorConstraints.gridy = 1;
        separatorConstraints.insets = new Insets(5, 5, 5, 5);
        getContentPane().add(new JSeparator(), separatorConstraints);

        GridBagConstraints cancelButtonConstraints = new GridBagConstraints();
        cancelButtonConstraints.gridx = 1;
        cancelButtonConstraints.gridy = 2;
        cancelButtonConstraints.insets = new Insets(5, 5, 5, 0);
        getContentPane().add(cancelButton, cancelButtonConstraints);

        GridBagConstraints previousButtonConstraints = new GridBagConstraints();
        previousButtonConstraints.gridx = 2;
        previousButtonConstraints.gridy = 2;
        previousButtonConstraints.insets = new Insets(5, 5, 5, 0);
        getContentPane().add(previousButton, previousButtonConstraints);

        GridBagConstraints nextButtonConstraints = new GridBagConstraints();
        nextButtonConstraints.gridx = 3;
        nextButtonConstraints.gridy = 2;
        nextButtonConstraints.insets = new Insets(5, 5, 5, 0);
        getContentPane().add(nextButton, nextButtonConstraints);

        GridBagConstraints finishButtonConstraints = new GridBagConstraints();
        finishButtonConstraints.gridx = 4;
        finishButtonConstraints.gridy = 2;
        finishButtonConstraints.insets = new Insets(5, 5, 5, 5);
        getContentPane().add(finishButton, finishButtonConstraints);
    }

    @Override
    public JPanel getWizardPageContainer() {
        return wizardPageContainer;
    }

    @Override
    public AbstractButton getCancelButton() {
        return cancelButton;
    }

    @Override
    public JButton getPreviousButton() {
        return previousButton;
    }

    @Override
    public JButton getNextButton() {
        return nextButton;
    }

    @Override
    public JButton getFinishButton() {
        return finishButton;
    }

    public void addCloseListener(IWizardCloseListener listener) {
        closingListeners.add(listener) ;
    }

    private class MinimumSizeAdjuster implements ContainerListener {
        @Override
        public void componentAdded(ContainerEvent e) {
            Dimension currentSize = getSize();
            Dimension preferredSize = getPreferredSize();

            Dimension newSize = new Dimension(currentSize);
            newSize.width = Math.max(currentSize.width, preferredSize.width);
            newSize.height = Math.max(currentSize.height, preferredSize.height);

            setMinimumSize(newSize);
        }

        @Override
        public void componentRemoved(ContainerEvent e) {
        }
    }
}
