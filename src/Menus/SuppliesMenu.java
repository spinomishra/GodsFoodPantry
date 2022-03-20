package Menus;
import Pantry.*;

public class SuppliesMenu extends Menu {
  public SuppliesMenu()
  {
  }
  
  static String[] options = {      
      "Add item to supplies",
      "Remove item from supplies",
  };

  public void showOptions()
  {    
    getUserChoice("Supply Management",
                  "Choose one of the management option", 
                  options, 
                  options[0]);        
  }

  public boolean Process() {
    switch (currentChoice) {
      case 0: 
        {
          Pantry p = Pantry.getInstance();
          p.Load() ;
        }
        return true;
        
      case 1:
        {
          Pantry p = Pantry.getInstance();
          p.Save() ;
        }
        return true;
        
      default: return false;  // incorrect choice
    }
  }
}