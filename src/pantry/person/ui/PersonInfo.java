/**
 * 
 */
package pantry.person.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.*;

/**
 * Person information class
 *
 */
public class PersonInfo extends JDialog implements ActionListener, DocumentListener{
	/**
	 * Version Id for serialization
	 */
	private static final long serialVersionUID = 1L;
	protected String personName ;
	protected String personContact ;
	protected String personAddress ;
	
	JTextField nameTextBox; 
	JTextArea addressTextBox;
	JTextField contactTextBox; 
	JButton okButton;
	JButton cancelButton;
	protected int option = JOptionPane.CANCEL_OPTION;

	/**
	 * @param frame - parent component
	 * @param title - dialog title box
	 */
	public PersonInfo(Window frame, String title)
	{	
		super(frame, title, Dialog.ModalityType.DOCUMENT_MODAL);
								
		JPanel container = new JPanel();
		BorderLayout borderLayout = new BorderLayout();
		container.setLayout(borderLayout);
		
		initComponent(container);
		
		container.setOpaque(true); //content panes must be opaque
	    setContentPane(container);	    	      
	}
	
	/**
	 * component initialization
	 * @param containerComponent
	 */
	protected void initComponent(JPanel containerComponent)
	{
		setSize(460, 200);
		setResizable(false); 
		setLocationRelativeTo(getParent());

		JPanel containerPanel = addControlsToTopPanel();
		
		JPanel containerPanel2 = new JPanel();		
		GridLayout layout2 = new GridLayout(1,2,5,5);
		containerPanel2.setLayout(layout2);
		containerPanel2.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));		
		
	    JLabel addressLabel = new JLabel("Address ", JLabel.LEFT);	    	     
	    addressTextBox = new JTextArea(3,1);	    
	    JScrollPane sp = new JScrollPane(addressTextBox);
	    Dimension minimalSize = new Dimension(50, 80);
	    addressTextBox.setWrapStyleWord(true);
	    addressTextBox.setPreferredSize(minimalSize);
	    addressTextBox.setSize(minimalSize);                    
	    addressTextBox.getDocument().addDocumentListener(this);
	    containerPanel2.add(addressLabel);
	    containerPanel2.add(sp);
	    
		//Create a panel that uses BoxLayout.
		JPanel buttonPane = new JPanel();	    
		okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		okButton.setEnabled(false);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setEnabled(true);		
		
		buttonPane.add(okButton);
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(cancelButton);
		buttonPane.add(Box.createHorizontalStrut(5));
		  
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
				
		containerComponent.add(containerPanel, BorderLayout.NORTH);
		containerComponent.add(containerPanel2, BorderLayout.CENTER);
		containerComponent.add(buttonPane, BorderLayout.PAGE_END);	
	}

	/**
	 * adding controls to top panel
	 * @param containerPanel
	 */
	protected JPanel addControlsToTopPanel() {
		JPanel containerPanel = new JPanel();
		GridLayout layout = new GridLayout(3,2,5,5);
		containerPanel.setLayout(layout);
		containerPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		JLabel nameLabel = new JLabel("Name", JLabel.LEFT);
	    nameTextBox = new JTextField(10);
		nameTextBox.addActionListener(this);
		nameTextBox.getDocument().addDocumentListener(this);
		containerPanel.add(nameLabel);
		containerPanel.add(nameTextBox);
		
	    JLabel phoneLabel = new JLabel("Contact Phone #", JLabel.LEFT);
	    contactTextBox = new JTextField(20);
	    contactTextBox.addActionListener(this);
	    contactTextBox.getDocument().addDocumentListener(this);
	    containerPanel.add(phoneLabel);
	    containerPanel.add(contactTextBox );

		return containerPanel;
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
			personName = nameTextBox.getText().trim();
			personContact = contactTextBox.getText().trim();
			personAddress = addressTextBox.getText().trim();

			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}		
	}

	/**
	 * element insert handler
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		okButton.setEnabled(nameTextBox.getText().trim().length() != 0);
	}

	/**
	 * element remove handler
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
		okButton.setEnabled(nameTextBox.getText().trim().length() != 0);
	}	
	  
	/**
	 * change handler
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub		
	}
}
