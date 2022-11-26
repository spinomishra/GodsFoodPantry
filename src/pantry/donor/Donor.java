package pantry.donor;

import pantry.person.Person;

/**
 * Donor class represents donors. It captures donor information like name and contact info
 */
public class Donor extends Person
{

  /**
  * Constructs a Donor object with inputted pantry.data
  * @param donorName the name of the donor
  */
  public Donor(String donorName){
    super(donorName);
  }
}