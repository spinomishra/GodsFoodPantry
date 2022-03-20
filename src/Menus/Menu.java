package Menus;

import javax.swing.*;
import java.awt.*;

/*
Abstract menu class
*/
public abstract class Menu {
  // option chosen by user
  int currentChoice;
  JFrame parent;
  // Constructor
  public Menu()
  {
    currentChoice = -1;  
    parent = null;
  }

  public Menu(JFrame frame)
  {
    parent = frame;  
  }
  
  // Shows menu options to the user using JOptionPane.
  // Inputs: title of the panel dialog box, label for the input control, options and the default option.
  // Output: Object chosen by the user
  public Object showMenuOptions(String title, String label, Object[] options, Object defaultOption)
  {
    return JOptionPane.showInputDialog(parent,
                                      label, 
                                      title,
                                      JOptionPane.INFORMATION_MESSAGE, 
                                      null, 
                                      options, 
                                      defaultOption);
  }

  // Shows menu options to the user using JOptionPane.
  // Inputs: title of the panel dialog box, label for the input control, options and the default option.
  // Output: index (in options array)  of the option chosen by the user
  public int getUserChoice(String title, String label, Object[] options, Object defaultOption)
  {
    Object choice = JOptionPane.showInputDialog(parent,
                                      label, 
                                      title,
                                      JOptionPane.INFORMATION_MESSAGE, 
                                      null, 
                                      options, 
                                      defaultOption);
    if (choice != null){
      currentChoice = determineOptionIndex(options, choice);    
    }
    else 
      currentChoice = -1;

    return currentChoice;
  }

  // abstract method that must be implemented by subclasses
  public abstract void showOptions();
  // abstract method that must be implemented by subclasses
  public abstract boolean Process();

  // determines the index of an option in the options array
  // Output: index in options array
  protected int determineOptionIndex(Object[] options, Object option)
  {
    int index = -1 ;
    for (int i=0; i< options.length; i++){
      if (options[i].equals(option)){
        index = i; 
        break;
      }        
    }

    return index;
  }

  // determines the index of an option in the options array
  // Output: index in options array
  protected int determineOptionIndex(String[] options, String option)
  {
    int index = -1 ;
    for (int i=0; i<options.length; i++){
      if (options[i].equals(option)){
        index = i; 
        break;
      }        
    }

    return index;
  }
} 