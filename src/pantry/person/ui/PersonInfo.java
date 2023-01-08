/**
 * 
 */
package pantry.person.ui;

import pantry.helpers.PhoneHelper;
import pantry.helpers.StringHelper;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.text.ParseException;

/**
 * Person information class
 *
 */
public class PersonInfo extends JDialog implements ActionListener, DocumentListener{
	/**
	 * Person Name
	 */
	protected String personName ;
	/**
	 * Person's contact number
	 */
	protected String personContact ;
	/**
	 * Person's mailing address
	 */
	protected String personAddress ;

	////////////////// CONTROLS ////////////////
	/**
	 * Name text field
	 */
	protected transient JTextField nameTextBox;
	/**
	 * address text area
	 */
	protected transient JTextArea addressTextBox;
	/**
	 * Formatted text field for Phone number
	 */
	protected transient JFormattedTextField contactTextBox;
	/**
	 * OK Button
	 */
	protected transient JButton okButton;
	/**
	 * Cancel button
	 */
	protected transient JButton cancelButton;

	/**
	 * Action option
	 */
	protected transient int option = JOptionPane.CANCEL_OPTION;
	/**
	 * JPanel object inside the dialog box. This panel would contain various tabs
	 */
	transient protected JPanel dialogPane;
	/**
	 * Tabbed pane where tabs will be added
	 */
	protected transient JTabbedPane tabbedPane;

	/**
	 * Constructor
	 * @param frame - parent component
	 * @param title - dialog title box
	 */
	public PersonInfo(Window frame, String title)
	{	
		super(frame, title, Dialog.ModalityType.DOCUMENT_MODAL);

		initializeDialog();
		adjustSizeConstraints();
	}

	/**
	 * Set size constraints for the dialog box
	 * This can be overridden by subclasses
	 */
	protected void adjustSizeConstraints(){
	//	setSize(460, 300);
	//	setMinimumSize(new Dimension(760, 500));
	//	setPreferredSize(new Dimension(760, 500));
	//	setResizable(false);
	}

	/**
	 * Dialog initialization - adds controls to the dialog, sets style and size for the dialog box.
	 */
	protected void initializeDialog()
	{
		var contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		dialogPane = new JPanel();

		// set borders for the dialog pane
		dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
		dialogPane.setBorder(new CompoundBorder(
				new TitledBorder(new EmptyBorder(0,0,0,0), "",
						TitledBorder.CENTER, TitledBorder.BOTTOM,
						new Font("Dialog", java.awt.Font.BOLD,12),
						Color.red),dialogPane. getBorder()));
		dialogPane.setLayout(new BorderLayout());

		// tabbed pane
		tabbedPane = new JTabbedPane();

		// add tabs to the tabbed pane
		addTabs(tabbedPane) ;
		dialogPane.add(tabbedPane, BorderLayout.NORTH);

		// add buttons
		JPanel buttonPanel = addActionControls() ;
		dialogPane.add(buttonPanel, BorderLayout.SOUTH);

		// add dialog pane to content
		contentPane.add(dialogPane, BorderLayout.CENTER);

		pack();
		setLocationRelativeTo(getParent());
	}

	/**
	 * Add default action buttons (OK and Cancel) to the dialog box.
	 * @return  The Button container panel object
	 */
	protected JPanel addActionControls(){
		//Create a panel that uses BoxLayout.
		JPanel buttonPane = new JPanel();

		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		Dimension btnSize = new Dimension(78, 30);
		okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.setMinimumSize(btnSize);
		okButton.setMaximumSize(btnSize);
		okButton.setPreferredSize(btnSize);
		okButton.setSize(btnSize);
		okButton.addActionListener(this);
		okButton.setEnabled(false);

		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setEnabled(true);
		cancelButton.setMinimumSize(btnSize);
		cancelButton.setMaximumSize(btnSize);
		cancelButton.setPreferredSize(btnSize);
		cancelButton.setSize(btnSize);

		buttonPane.add(okButton);
		buttonPane.add(cancelButton);

		return buttonPane;
	}

	/**
	 * Creates new panel and adds it as a pane to the specified tabbed pane.
	 *
	 * @param tabbedPane The tabbed pane
	 * @param tabTitle The tab title
	 * @param layout a layout manager, if null BoxLayout manager is set as default layout for the pabel
	 * @return a JPanel object where controls for this tab could be added
	 */
	protected JPanel createNewTab(JTabbedPane tabbedPane, String tabTitle, LayoutManager layout){
		JPanel contentPanel = new JPanel();

		// By default, use boxlayout for the content panel along Y Axis
		if (layout == null)
			contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		else
			contentPanel.setLayout(layout);

		tabbedPane.addTab(tabTitle, contentPanel);

		return contentPanel;
	}

	/**
	 * Creates new panel and adds it as a pane to the specified tabbed pane.
	 *
	 * @param tabbedPane The tabbed pane
	 * @param tabTitle The tab title
	 * @return a JPanel object where controls for this tab could be added. This object has no default layout manager.
	 */
	protected JPanel createNewTab(JTabbedPane tabbedPane, String tabTitle){
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(null);
		tabbedPane.addTab(tabTitle, contentPanel);
		return contentPanel;
	}

	/**
	 * Add tabs to the dialog box. Additional tabs could be added by overriding this method.
	 * @param tabbedPane The tabbed pane control.
	 */
	protected void addTabs(JTabbedPane tabbedPane) {
		JPanel contentPanel = createNewTab(tabbedPane, "Basic Information");

		if (contentPanel == null)
			return;

		contentPanel.setPreferredSize(new Dimension(375, 150));

		{
			JLabel label = new JLabel("Name:");
			label.setFocusable(false);
			label.setBounds(10, 20, 120, 20);
			contentPanel.add(label);

			nameTextBox = new JTextField(30);
			nameTextBox.addActionListener(this);
			nameTextBox.getDocument().addDocumentListener(this);
			nameTextBox.setBounds(135, 20, 220, 20);

			contentPanel.add(nameTextBox);
		}

		{
			JLabel label = new JLabel("Phone #:");
			label.setFocusable(false);
			label.setBounds(10, 45, 120, 20);
			contentPanel.add(label);

			MaskFormatter fmt = PhoneHelper.getFormatterMask();

			contactTextBox = new JFormattedTextField(fmt);
			contactTextBox.setColumns(12);
			contactTextBox.addActionListener(this);
			contactTextBox.getDocument().addDocumentListener(this);
			contactTextBox.setBounds(135, 45, 220, 20);
			contentPanel.add(contactTextBox);
		}

		{
			JLabel label = new JLabel("Mailing Address:");
			label.setFocusable(false);
			label.setBounds(10, 70, 120, 20);
			contentPanel.add(label);

			Dimension minimalSize = new Dimension(120, 40);

			addressTextBox = new JTextArea(3,1);
			addressTextBox.setWrapStyleWord(true);
			addressTextBox.setPreferredSize(minimalSize);
			addressTextBox.setAutoscrolls(true);
			addressTextBox.getDocument().addDocumentListener(this);
			addressTextBox.setBounds(135, 70, 220, 60);
			contentPanel.add(addressTextBox);
		}
	}

	/**
	 * Adds a new label (with default size of (60X16) inside a new JPanel object that uses Borderlayout manager.
	 * @param itemLabelText The title of the label
	 * @return A JPanel object
	 */
	protected JPanel addNewItemPanel(String itemLabelText){
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));

		// add some vertical space at the beginning
		panel.add(new JPanel(null), BorderLayout.NORTH);

		var lblDim = new Dimension(160, 16);

		JLabel label = new JLabel(itemLabelText, JLabel.LEFT);
		label.setMinimumSize(lblDim);
		label.setPreferredSize(lblDim);
		panel.add(label, BorderLayout.WEST);

		return panel;
	}

	/**
	 * gets person name
	 * @return name 
	 */
	public String getPersonName() {
		return personName;
	}

	/**
	 * gets person's phone number
	 * @return phone number
	 */
	public String getPersonContact() {
		return personContact;
	}

	/**
	 * gets person address
	 * @return address
	 */
	public String getPersonAddress() {
		return personAddress;
	}

	/**
	 * ui component handler
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Cancel")
		{
			option = JOptionPane.CANCEL_OPTION;
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		
		if (e.getActionCommand() == "OK") {
			option = JOptionPane.OK_OPTION;

			onOKButtonClicked(e) ;

			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}		
	}

	/**
	 * Handles OK Button click
	 * Always call super before you override this
	 * @param e The ActionEvent object
	 */
	protected void onOKButtonClicked(ActionEvent e) {
		personName = nameTextBox.getText().trim();
		String phone = contactTextBox.getText().trim();
		personContact = PhoneHelper.isNullOrEmpty(phone) ? StringHelper.Empty : phone;
		personAddress = addressTextBox.getText().trim();
	}

	/**
	 * element insert handler
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		String name =  nameTextBox.getText();
		String phone = contactTextBox.getText();

		okButton.setEnabled(!StringHelper.isNullOrEmpty(name) && !PhoneHelper.isNullOrEmpty(phone));
	}

	/**
	 * element remove handler
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
		String name =  nameTextBox.getText();
		String phone = contactTextBox.getText();

		okButton.setEnabled(!StringHelper.isNullOrEmpty(name) && !PhoneHelper.isNullOrEmpty(phone));
	}

	/**
	 * Change handler - called when name, address or phone number controls text changes
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		// Nothing to handle
	}
}
