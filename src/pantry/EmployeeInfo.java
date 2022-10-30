/**
 * 
 */
package pantry;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * Employee information
 *
 */
public class EmployeeInfo extends PersonInfo  {
	/**
	 * Version Id for serialization
	 */
	private static final long serialVersionUID = 1L;
	
	private JComboBox<Employee.EmployeeRole> rolelist;
    private DefaultComboBoxModel<Employee.EmployeeRole> rolelistModel;
    private Employee.EmployeeRole Role;
    
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
	protected void addControlsToTopPanel(JPanel containerPanel) {		
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
		
		super.addControlsToTopPanel(containerPanel);
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
			e = new Employee(pInfo.personName, pInfo.Role);
			e.setAddress(pInfo.personAddress);
			e.setContactPhone(pInfo.personContact);
		}

		pInfo.dispose();
		return e;
	}
}
