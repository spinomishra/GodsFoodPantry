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
 * UI to collect and display employee information
 */
public class EmployeeInfo extends PersonInfo {
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
	protected void addTabs(JTabbedPane tabbedPane)  {
		super.addTabs(tabbedPane);

		JPanel containerPanel = createNewTab(tabbedPane, "Employment Info", null);

		JPanel panel ;

		{
			{
				panel = addNewItemPanel("Social Security Number");
				try {
					MaskFormatter fmt = new MaskFormatter("###-##-####");
					ssnField = new JFormattedTextField(fmt);
					ssnField.setColumns(12);
					panel.add(ssnField, BorderLayout.CENTER);
				} catch (ParseException e) {
					// exception handler
				}
				containerPanel.add(panel);
			}

			{
				panel = addNewItemPanel("Employment Start (mm/dd/yyyy)");
				try {
					MaskFormatter dateFmt = new MaskFormatter("##/##/####");
					empStart = new JFormattedTextField(dateFmt);
					empStart.setColumns(11);
					panel.add(empStart, BorderLayout.CENTER);
				} catch (ParseException e) {
					// exception handler
				}
				containerPanel.add(panel);
			}

			{
				panel = addNewItemPanel("Employee Role");

				// Role list combo box model
				DefaultComboBoxModel<Employee.EmployeeRole> roleListModel = new DefaultComboBoxModel<>();
				for (Employee.EmployeeRole r : Employee.EmployeeRole.values())
					roleListModel.addElement(r);

				//Create the list box to show the volunteers names
				roleList = new JComboBox<Employee.EmployeeRole>(roleListModel);

				// single item can be selected
				roleList.setActionCommand("roleList");
				roleList.setSelectedIndex(0);
				roleList.addActionListener(this);

				panel.add(roleList, BorderLayout.CENTER);
				containerPanel.add(panel);
			}

		}
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
