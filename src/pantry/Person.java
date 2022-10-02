package pantry;
import java.util.Random;
import java.io.Serializable;

public abstract class Person implements Serializable {
  int Id;
  String fName;
  String lName;
  String Address;
  String Mobile_number;

  public Person(String fname, String lname)
  {
    generateId() ;
    fName = fname ;
    lName = lname ;
  }
  
  public Person(String fullname)
  {
    String[] tokens = fullname.split(" ");
    if (tokens == null)
      fName = fullname;
    else 
    {
      if (tokens.length >= 2)
        fName = tokens[0];
      if (tokens.length >= 2)
        lName = tokens[1];
    }
    
    generateId();
  }

  public Person(String fname, String lname, String address)
  {
    this(fname, lname);
    setAddress(address);
  }

  public Person(String fname, String lname, String address, String phone_no)
  {
    this(fname, lname);
    setAddress(address);
    setContactPhone(phone_no);
  }

  /* *
  * sets address for the person
  */
  public void setAddress(String address)
  {
      Address = address;
  }

  /* *
  * sets contact phone number for the person
  */
  public void setContactPhone(String phone_no)
  {
      Mobile_number = phone_no;
  }

  /* *
  * returns the name of the person
  * @return person's name
  */
  public String getName()
  {
    return (fName + " " + lName).trim();
  }

    /* *
  * returns person's address
  * @return person's address
  */
  public String getAddress()
  {
    return Address;
  }

  /* *
  * returns person's contact number
  * @return person's phone number
  */
  public String getContactNumber()
  {
    return Mobile_number;
  }

  /* *
  * generates an identity number for the person
  */
  void generateId()
  {
    Random random = new Random();
    Id = random.nextInt(32767);
    if (Id == 0)
      Id = random.nextInt(32767);
  }

/**
  * formats the data of the Volunteer as a string
  * @return the string format of the data
  */
  public String toString(){
    return getName() + ":" + getAddress() + ":" + getContactNumber();
  }

  /**
  * compares the Persons object with another Person object
  * @param obj the object to be compared with
  * @return the lexigraphical comparison between the two objects
  */
  public int compareTo(Object obj){
    if (this == obj)
      return 0;

    int comp = -1;
    if (obj != null)
    {
      if (!(obj instanceof Person))
        return -1;

      Person p = (Person) obj;
      comp = Integer.compare(Id, p.Id);

      if (comp == 0) {
        comp = fName.compareTo(p.fName); 
        if (comp == 0)
          comp = lName.compareTo(p.lName);
      }
    }
    
    return comp;
  }

  /**
  * checks if two Persons are the same
  * @param obj the object to be compared with
  * @return whether the two are the same
  */
  public boolean equals(Object obj){    
    if (this == obj)
      return true;
    
    if (obj != null)
    {
      if (!(obj instanceof Person))
        return false;
      
      return this.Id == ((Person)obj).Id;
    }
    
    return false;
  }    
}