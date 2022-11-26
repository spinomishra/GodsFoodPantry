/**
 * 
 */
package pantry.employee.ui;

import pantry.dataprotection.Hash;
import pantry.employee.Employee;
import pantry.person.ui.PersonInfo;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;

/**
 * Employee information
 */
public class EmployeeInfo extends PersonInfo {
	/**
	 * Version Id for serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Roles list combo box
	 */
	private JComboBox<Employee.EmployeeRole> rolelist;
	/**
	 * Rolelist combobox model
	 */
    private DefaultComboBoxModel<Employee.EmployeeRole> rolelistModel;
	/**
	 * Employee Role
	 */
    private Employee.EmployeeRole Role;
	/**
	 * SSN text box control
	 */
	JFormattedTextField ssnField = null;

	/**
	 * Employment Start date
	 */
	JFormattedTextField empStart = null;

	/**
	 * Constructor
	 * @param frame - parent window
	 * @param title - title
	 */
	public EmployeeInfo(Window frame, String title)
	{	
		super(frame, title) ;
	}
	
	/**
	 * add controls to top panel of the UI
	 */
	@Override
	protected JPanel addControlsToTopPanel()  {
		JPanel containerPanel = super.addControlsToTopPanel();
		GridLayout gridLayout = (GridLayout) containerPanel.getLayout();
		gridLayout.setRows(gridLayout.getRows()+2);

		JLabel empStartLabel = new JLabel("Employment Start (mm/dd/yyyy)", JLabel.LEFT);
		JLabel ssnLabel = new JLabel("Social Security #", JLabel.LEFT);
		try {
			MaskFormatter datefmt = new MaskFormatter("##/##/####");
			empStart = new JFormattedTextField(datefmt);
			empStart.setColumns(11);

			MaskFormatter fmt = new MaskFormatter("###-##-####");
			ssnField = new JFormattedTextField(fmt);
			ssnField.setColumns(12);
		} catch (ParseException e) {
			// exception handler
		}
		containerPanel.add(empStartLabel);
		containerPanel.add(empStart);

		containerPanel.add(ssnLabel);
		containerPanel.add(ssnField);

		rolelistModel = new DefaultComboBoxModel<Employee.EmployeeRole>();           
		for (Employee.EmployeeRole r : Employee.EmployeeRole.values())
		       rolelistModel.addElement(r);
		
		JLabel roleLabel = new JLabel("Employee Role", JLabel.LEFT);	    	     
		
		//Create the listbox to show the volunteers names		 
		rolelist = new JComboBox<Employee.EmployeeRole>(rolelistModel);
		
		// single item can be selected
		rolelist.setActionCommand("rolelist");
		rolelist.setSelectedIndex(0);
		rolelist.addActionListener(this);

		containerPanel.add(roleLabel);
		containerPanel.add(rolelist);

		return containerPanel;
	}
	
	/**
	 * @return - employee's role
	 */
	public Employee.EmployeeRole getRole() {
		return Role;
	}
	
	/**
	 * action handler for the UI controls
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getActionCommand() == "OK") {
			Role = rolelist.getItemAt(rolelist.getSelectedIndex());
		}
		else if (e.getActionCommand() == "rolelist")
		{
			Role = rolelist.getItemAt(rolelist.getSelectedIndex());
		}
	}

	/**
	 * @param frame - parent component
	 * @param title - title of the dialog box
	 * @return EmployeeInfo object
	 */
	public static Employee createAndShowGUI(Window frame, String title) {
		EmployeeInfo pInfo = new EmployeeInfo(frame, title);
      
		//Display the window.
		pInfo.pack();
		pInfo.setVisible(true);

		Employee e = null;
		if (pInfo.option == JOptionPane.OK_OPTION) {
			String ssn = pInfo.ssnField.getText();
			String ssnHash = Hash.Sha2Hash(ssn.toCharArray());

			e = new Employee(pInfo.personName, pInfo.Role);
			e.setAddress(pInfo.personAddress);
			e.setContactPhone(pInfo.personContact);
			e.setSSN(ssnHash);
		}

		pInfo.dispose();
		return e;
	}
}
