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
	private JComboBox<Employee.EmployeeRole> roleList;
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
			MaskFormatter dateFmt = new MaskFormatter("##/##/####");
			empStart = new JFormattedTextField(dateFmt);
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

		// Role list combo box model
		DefaultComboBoxModel<Employee.EmployeeRole> roleListModel = new DefaultComboBoxModel<>();
		for (Employee.EmployeeRole r : Employee.EmployeeRole.values())
		       roleListModel.addElement(r);
		
		JLabel roleLabel = new JLabel("Employee Role", JLabel.LEFT);	    	     
		
		//Create the list box to show the volunteers names
		roleList = new JComboBox<Employee.EmployeeRole>(roleListModel);
		
		// single item can be selected
		roleList.setActionCommand("roleList");
		roleList.setSelectedIndex(0);
		roleList.addActionListener(this);

		containerPanel.add(roleLabel);
		containerPanel.add(roleList);

		return containerPanel;
	}
	
	/**
	 * Get employee role
	 * @return - employee's role
	 */
	public Employee.EmployeeRole getRole() {
		return Role;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getActionCommand() == "OK") {
			Role = roleList.getItemAt(roleList.getSelectedIndex());
		}
		else if (e.getActionCommand() == "roleList")
		{
			Role = roleList.getItemAt(roleList.getSelectedIndex());
		}
	}

	/**
	 * Create and show the UI for Employee information
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
