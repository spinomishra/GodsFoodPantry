package pantry.food;

import pantry.Pantry;
import pantry.VolunteerDialogbox;
import pantry.auth.Login;
import pantry.interfaces.IHome;
import pantry.ui.SideMenuItem;
import pantry.ui.SideMenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Home screen for Food Pantry Management
 */
public class ManagementHome extends JFrame implements IHome {
    /**
     * Panel1
     */
    JPanel jPanel1;
    /**
     * Main Panel
     */
    JPanel mainPanel;
    /**
     * Side Menu Panel
     */
    SideMenuPanel sideMenuPanel;
    String pantryName;

    /**
     * Constructor
     * @param pn Pantry Name
     */
    ManagementHome(String pn){
        pantryName = pn;
    }

    /**
     * Shows home screen
     */
    @Override
    public void ShowHome() {
        initHome();
    }

    /**
     * Home page processor
     */
    @Override
    public void Run() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        Login login = new Login();
        login.Show(this) ;
    }

    /**
     * Initialize home screen
     */
    private  void initHome() {
        mainPanel = new JPanel();
        ShowDashboard();
    }

    /**
     * Show management dashboard
     */
    private void ShowDashboard(){
        setTitle("PantryWare - "+ pantryName);
        setSize(800, 600);

        mainPanel.setBackground(new java.awt.Color(210, 231, 255));

        sideMenuPanel = new SideMenuPanel(this, mainPanel);
        sideMenuPanel.setSpeed(4);
        sideMenuPanel.setResponsiveMinWidth(100);
        sideMenuPanel.addMenu(CreateMenuList());
        sideMenuPanel.setMaxWidth(300);

        jPanel1 = new javax.swing.JPanel();
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        Component sideBar = sideMenuPanel.getSideBar();
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(sideBar, javax.swing.GroupLayout.PREFERRED_SIZE, sideBar.getPreferredSize().width, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(sideBar, javax.swing.GroupLayout.DEFAULT_SIZE, sideBar.getPreferredSize().height, Short.MAX_VALUE)
                        .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }

    /**
     * Create menu items list
     * @return menu item list
     */
    private List<SideMenuItem> CreateMenuList(){
        JFrame mainFrame = this;
        List<SideMenuItem> menuItems = new java.util.ArrayList<SideMenuItem>();
        try {
            menuItems.add(new SideMenuItem("", "", SideMenuItem.MENUITEM_WIDTH, 10));

            menuItems.add(new SideMenuItem("Employees", "../../images/employees.png", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    OnManageEmployees(e);
                }

                private void OnManageEmployees(ActionEvent e) {
                }
            }));

            menuItems.add(new SideMenuItem("Inventory", "../../images/inventory.png", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    OnManageInventory(e);
                }

                private void OnManageInventory(ActionEvent e) {
                }
            }));

            menuItems.add(new SideMenuItem("Donations", "../../images/donations.png", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) { OnManageDonations(e); }

                private void OnManageDonations(ActionEvent e) {
                }
            }));

            menuItems.add(new SideMenuItem("Volunteers", "../../images/volunteer.png", new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    OnManageVolunteers(e);
                }

                private void OnManageVolunteers(ActionEvent e) {
                    Pantry pantry = Pantry.getInstance();
                    VolunteerDialogbox.createAndShowGUI(mainFrame, pantry.get_Data().get_Volunteers());
                    //Schedule a job for the event-dispatching thread:
                    //creating and showing this application's GUI.
                    // javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    //     public void run() {
                    //         VolunteerDialogbox.createAndShowGUI(parent, pantry.volunteers);
                    //     }
                    // });
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return menuItems;
    }
}
