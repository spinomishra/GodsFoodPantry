package pantry;
import java.util.*;


/**
 * Employee class derived from Person
 *
 */
public class Employee extends Person
{
    // version number for serialization purposes
	private static final long serialVersionUID = 1L;
	
	// Employee Role enum
	public enum EmployeeRole {Owner, Manager, Cook, Cleaner, Assistant, Validator, Accountant, Driver}
	
	private EmployeeRole Role;

	/**
	 * Constructs a Employee object with data
	 * @param name the name of the employee
	 */
	public Employee(String name){
	  super(name);
	}

	/**
	 * constructs a Employee object with data
	 * @param name the name of the employee
	 * @param role employee role
	 */
	public Employee(String name, EmployeeRole role){
	    super(name);
	    setRole(role);
	}

	/**
	 * Gets employee role
	 * @return employee role
	 */
	public EmployeeRole getRole() {
		return Role;
	}
	
	/**
	 * Sets employee role
	 * @param employee role
	 */	
	public void setRole(EmployeeRole role) {
		Role = role;
	}

	/**
	 * formats the data of the Volunteer as a string
	 * @return the string format of the data
	 */
	public String toString(){
		return getName() + ":" + Role.toString() + ":" + getContactNumber() + ":" + getAddress() ;
	}

	/**
	 * compares the Employee object with another Person object
	 * @param obj the object to be compared with
	 * @return the lexigraphical comparison between the two objects
	 */
	public int compareTo(Object obj){
		if (this == obj)
			return 0;

		int comp = -1;
		if (obj != null)
		{
			if (!(obj instanceof Employee))
				return -1;

			Person p = (Person) obj;
			comp = super.compareTo(obj);

		}

		return comp;
	}

	/**
	 * checks if two Employees are the same
	 * @param obj the object to be compared with
	 * @return whether the two are the same
	 */
	public boolean equals(Object obj){
		if (this == obj)
			return true;

		if (obj != null)
		{
			return super.equals(obj);
		}

		return false;
	}
}