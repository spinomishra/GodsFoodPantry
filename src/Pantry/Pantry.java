package Pantry;
import java.util.*;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Pantry {
  // Singleton instance of pantry manager object
  static Pantry instance ;

  public ArrayList<String> employees;
  public ArrayList<Donor> donors;
  public ArrayList<Volunteer> volunteers;
  public ArrayList<Supply> supplies;
  public ArrayList<Fund> funds;
  String pantryRecords = "pantry.rec";
  
  public Pantry()
  {
    Load() ;

    // Create empty lists if none was loaded
    if (donors == null)
      donors = new ArrayList<Donor>();
    if (volunteers == null)
      volunteers = new ArrayList<Volunteer>();
    if (supplies == null)
      supplies = new ArrayList<Supply>();
    if (funds == null)
      funds = new ArrayList<Fund>();     
    
    if (employees == null)
      employees = new ArrayList<String>();         
  }
  
  // Static method to get singleton pantry manager object
  public static Pantry getInstance()
  {
    if (instance == null)
      instance = new Pantry();

    return instance;
  }

  public void Load(){    
    try
    {
        FileInputStream fis = new FileInputStream(pantryRecords);
        ObjectInputStream ois = new ObjectInputStream(fis);
    
        volunteers = (ArrayList<Volunteer>) ois.readObject();
        supplies   = (ArrayList<Supply>) ois.readObject();
        donors     = (ArrayList<Donor>) ois.readObject();        
        employees  = (ArrayList<String>) ois.readObject();
      
        ois.close();
        fis.close();
    } 
    catch (IOException ioe) 
    {
        ioe.printStackTrace();
        return;
    } 
    catch (ClassNotFoundException c) 
    {
        System.out.println("Class not found");
        c.printStackTrace();
        return;
    }    
  }

  public void Save(){
    try
    {
        FileOutputStream fos = new FileOutputStream(pantryRecords);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(volunteers);
        oos.writeObject(supplies);
        oos.writeObject(donors);      
        oos.writeObject(employees);  
        oos.close();
        fos.close();
    } 
    catch (IOException ioe) 
    {
        ioe.printStackTrace();
    } 
  }
}