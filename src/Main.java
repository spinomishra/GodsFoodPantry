import java.util.*;
import javax.swing.*;
import java.io.*;
import Pantry.*;

class Main {
  public static void main(String[] args) {    
    // Create and show UI
    JFrame frame = new JFrame("Pantry Manager");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(560, 300);
    Menus.MainMenu mainMenu = new Menus.MainMenu(frame);
    mainMenu.showOptions();
    frame.setLocationRelativeTo(null);      
    frame.setVisible(true);    
  }
  
  /**
  * 
  * 
  */
  public void loadFile(){
    String fileName = JOptionPane.showInputDialog("Enter the file to load from", "itemList.dat");          
    if (fileName != null)
    {
      Scanner saved;
      try
      {
        saved = new Scanner (new File(fileName));
        while(saved.hasNextLine())
        {
          String line = saved.nextLine();
          if (line != null && line.length() > 0)
          {
            line = line.trim();
            System.out.println(line);                    
            if (line.length() > 0)
            {
              
            }
          }
        }
      }
      catch (IOException e){
        System.out.println ("input read failed " + e);
      }
    }
  }

  public void saveFile(){
    Scanner in = new Scanner(System.in);
    String fileName = in.next();
          if (fileName != null)
          {
            try
            {
              PrintWriter out = new PrintWriter(fileName);
              out.println();
              out.close();
            }
            catch(IOException exception)
            {
              System.out.println("Information could not be saved");
            }
          }
  }
  
  public static Object[] printSearchPersonMenu()
  {
	  Object[] menu = 
    {
      "0: Quit", 
      "1: Search By Name",
      "2: Search By Activity",
      "3: Search by Date",
    };
    return menu;
  } 

  public static Object[] printSortPersonMenu()
  {
	  Object[] menu = 
    {
      "0: Quit", 
      "1: Sort Alphabetically",
      "2: Sort By Activity",
      "3: Sort by Date",
    };
    return menu;
  } 

  public static Object[] printSearchObjectMenu()
  {
	  Object[] menu = 
    {
      "0: Quit", 
      "1: Search By Name",
      "2: Search By Amount",
      "3: Search by Date Given",
    };
    return menu;
  } 

  public static Object[] printSortObjectMenu()
  {
	  Object[] menu = 
    {
      "0: Quit", 
      "1: Sort Alphabetically",
      "2: Sort By Amount",
      "3: Sort by Date Given",
    };
    return menu;
  } 
  
}