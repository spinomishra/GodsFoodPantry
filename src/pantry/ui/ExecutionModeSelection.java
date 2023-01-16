package pantry.ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * This application works in 3 different modes
 * 1. Privileged Management\
 * 2. Volunteer checkin/checkout
 * 3. Food Distribution record keeping
 * This class presents users an opportunity to choose one of the modes for application
 * execution. User can opt to auto-launch application in that specific mode upon next
 * execution.
 */
public class ExecutionModeSelection extends JDialog implements ActionListener {
    /**
     * Remember Me flag
     */
    public boolean rememberMe;
    /**
     * Selected execution mode
     */
    public String executionMode;

    /**
     * Constructor
     *
     * @param mode  current execution mode
     * @param title Title of this dialog
     */
    public ExecutionModeSelection(String mode, String title) {
        super(null, title, Dialog.ModalityType.DOCUMENT_MODAL);
        executionMode = (mode != null ? mode : "");
        initComponents();
    }

    /**
     * Initializes dialogs child components/controls
     */
    private void initComponents() {
        //setSize(new Dimension(400, 300));
        setResizable(false);

        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        var dialogPane = new JPanel();

        // set borders for the dialog pane
        dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
        dialogPane.setBorder(new CompoundBorder(
                new TitledBorder(new EmptyBorder(0, 0, 0, 0), "",
                        TitledBorder.CENTER, TitledBorder.BOTTOM,
                        new Font("Dialog", java.awt.Font.BOLD, 12),
                        Color.RED), dialogPane.getBorder()));
        dialogPane.setLayout(new BorderLayout());

        JLabel label = new JLabel();
        String info = "<br/>PantryWare executes in 3 modes. <br/>Please choose one of the modes for this execution instance!";
        String msg = "<html><body><p style='width: 300px;'>" + info + "</p><br></body></html>";
        label.setForeground(new Color(0xD77500));
        label.setText(msg);
        label.setBorder(BorderFactory.createRaisedBevelBorder());
        dialogPane.add(label, BorderLayout.NORTH);

        JPanel choicePanel = getOptionsPanel();
        dialogPane.add(choicePanel, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel(new FlowLayout());
        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(80, 30));
        okButton.setActionCommand("OK");
        okButton.addActionListener(this);
        buttonPane.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(80, 30));
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);
        buttonPane.add(cancelButton);

        dialogPane.add(buttonPane, BorderLayout.SOUTH);

        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Creates a panel containing different options
     *
     * @return The JPanel object
     */
    private JPanel getOptionsPanel() {
        JPanel choicePanel = new JPanel(new GridLayout(5, 1));
        JRadioButton privMangement = new JRadioButton("Privileged Management (for Employees)", (executionMode.compareTo("manage") == 0));
        privMangement.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1)
                    executionMode = "manage";
            }
        });

        //JLabel label = new JLabel() ;
        //choicePanel.add(label);

        JRadioButton volunteerTimeLog = new JRadioButton("Volunteer Checkin/Checkout (for Volunteers)", (executionMode.compareTo("volunteer") == 0));
        volunteerTimeLog.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1)
                    executionMode = "volunteer";
            }
        });

        JRadioButton foodDistributionLog = new JRadioButton("Food Distribution Event (for Consumers)", (executionMode.compareTo("distribution") == 0));

        foodDistributionLog.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1)
                    executionMode = "distribution";
            }
        });

        choicePanel.add(privMangement);
        choicePanel.add(volunteerTimeLog);
        choicePanel.add(foodDistributionLog);
        ButtonGroup bg = new ButtonGroup();
        bg.add(privMangement);
        bg.add(volunteerTimeLog);
        bg.add(foodDistributionLog);

        Dimension minSize = new Dimension(100, 2);
        choicePanel.add(new Box.Filler(minSize, minSize, minSize));

        JCheckBox checkBox = new JCheckBox("Remember my execution mode selection");
        Font font = new Font("Serif", Font.BOLD | Font.ITALIC, 14);
        checkBox.setFont(font);
        //checkBox.setForeground(new Color(0xD78200));
        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                rememberMe = e.getStateChange() == 1;
            }
        });
        choicePanel.add(checkBox);
        return choicePanel;
    }

    /**
     * Action handler for OK and Cancel button
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "Cancel") {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            executionMode = ""; // clear execution mode
        }

        if (e.getActionCommand() == "OK")
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}