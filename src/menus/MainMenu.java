package menus;
import pantry.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class MainMenu extends Menu implements ActionListener{  
  public MainMenu()
  {
  }

  public MainMenu(JFrame frame)
  {
    super(frame);
  }
  
  // static String[] options = {
  //     "Exit Pantry Management System",
  //     "Load Pantry records",
  //     "Save Pantry records",
  //     "Manage Supplies",
  //     "Manage Funds",
  //     "Manage Volunteers",
  //     "Manage Donors",
  //     "Manage Employees"
  // };

  static String[] options = {
    "Manage Employees",
    "Manage Volunteers",
    "Manage Supplies",
    "Manage Donors",    
    "Reload",
    "Save",              
  };
  
  public void showOptions()
  {    
    // getUserChoice("Pantry Manager",
    //                        "Choose one of the options", 
    //                        options, 
    //                        options[0]);

    JPanel buttonPane = new JPanel();
    GridLayout gridLayout = new GridLayout(3,3);
    buttonPane.setLayout(gridLayout);   
    buttonPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    gridLayout.setHgap(5);
    gridLayout.setVgap(5);
    for (int i=0; i<options.length; i++)
    {
      JButton button = new JButton(options[i]);
      button.setBorder( new LineBorder(Color.BLUE) );
      Dimension btnSize = button.getPreferredSize();
      btnSize.setSize(btnSize.getWidth()/2+6, btnSize.getWidth()/2+6);
      button.setPreferredSize(btnSize);

      button.setActionCommand(options[i]);
      button.addActionListener(this);
      button.setEnabled(true);  
      buttonPane.add(button);
    }
    
    parent.add(buttonPane, BorderLayout.PAGE_START);    
  }

  public void actionPerformed(ActionEvent e) {
    currentChoice = determineOptionIndex(options, e.getActionCommand());
    Process();
  }  
  
  public boolean Process() {
    switch (currentChoice) {
        case 0:  // Employees
          break;
        
        
        case 1:  // Volunteers
        {
          Pantry pantry = Pantry.getInstance();
          VolunteerDialogbox.createAndShowGUI(parent, pantry.volunteers);          
          //Schedule a job for the event-dispatching thread:
          //creating and showing this application's GUI.
          // javax.swing.SwingUtilities.invokeLater(new Runnable() {
          //     public void run() {
          //         VolunteerDialogbox.createAndShowGUI(parent, pantry.volunteers);
          //     }
          // });
        }
        break;

      case 2:
        {
           SuppliesMenu suppliesMenu = new SuppliesMenu();
      		 do   
      		 {	
             // display main menu
             suppliesMenu.showOptions();
           } while(suppliesMenu.Process());          
        }
        break;
        
      case 4: 
        {
          Pantry p = Pantry.getInstance();
          p.Load() ;
        }
        break;
        
      case 5:
        {
          Pantry p = Pantry.getInstance();
          p.Save() ;
        }
        break;
        
      default: return false;  // incorrect choice
    }

    return true;
  }
}