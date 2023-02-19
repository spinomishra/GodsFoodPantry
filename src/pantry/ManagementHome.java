package pantry;

import pantry.auth.Login;
import pantry.employee.ui.EmployeeManagerCard;
import pantry.interfaces.IHome;
import pantry.ui.MainPanel;
import pantry.ui.SideMenuItem;
import pantry.ui.SideMenuPanel;
import pantry.volunteer.ui.VolunteerManagerCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Home screen for Food Pantry Management
 */
public class ManagementHome extends JFrame implements IHome {
    /**
     * Main Panel
     */
    MainPanel mainPanel;

    /**
     * Map of cards for main panel
     */
    Map<String, JPanel> mainPanelCards;

    /**
     * Side Menu Panel
     */
    SideMenuPanel sideMenuPanel;

    /**
     * Panel1
     */
    JPanel jPanel1;

    /**
     * Constructor
     */
    ManagementHome() {
        mainPanelCards = new HashMap<String, JPanel>();
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
        // setting default close operation to do nothing.. so that I can handle it with windowClosing action handler
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * @param e window event
             */
            @Override
            public void windowClosing(WindowEvent e) {
                handleClosing();
            }
        });

        setVisible(true);

        Login login = new Login();
        if (login.Show(this)) {
            // authenticated successfully
            mainPanel.addManagementCards();
            mainPanel.Show(EmployeeManagerCard.Title);
        }
    }

    /**
     * Invoked when a window is in the process of being closed.
     */
    private void handleClosing() {
        // save pantry data
        Pantry.getInstance().get_Data().Save();
        var result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to close this window?", "Close Window ?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            Pantry.getInstance().Close();
            System.exit(0);
        }
    }

    /**
     * Initializes home screen
     */
    private void initHome() {
        mainPanel = new MainPanel(this);
        ShowDashboard();
    }

    /**
     * Shows management dashboard
     */
    private void ShowDashboard() {
        setTitle(Home.getDefaultPageTitle() + " - Management");
        setSize(800, 600);

        // main screen
        mainPanel.initComponent();
        mainPanel.setBackground(new java.awt.Color(210, 231, 255));

        // side menu bar
        sideMenuPanel = new SideMenuPanel(this, mainPanel);
        sideMenuPanel.setSpeed(4);
        sideMenuPanel.setResponsiveMinWidth(100);
        sideMenuPanel.addMenu(CreateMenuList());
        sideMenuPanel.setMaxWidth(300);

        // content panel
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
     *
     * @return list of menu items
     */
    private List<SideMenuItem> CreateMenuList() {
        JFrame mainFrame = this;
        List<SideMenuItem> menuItems = new java.util.ArrayList<SideMenuItem>();
        try {
            menuItems.add(new SideMenuItem("", "", SideMenuItem.MENUITEM_WIDTH, 10));

            menuItems.add(new SideMenuItem("Employees", "/images/employees.png", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainPanel.Show(EmployeeManagerCard.Title);
                }
            }));

            menuItems.add(new SideMenuItem("Volunteers", "/images/volunteer.png", new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mainPanel.Show(VolunteerManagerCard.Title);
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return menuItems;
    }
}
