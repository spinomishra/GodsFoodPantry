package pantry.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

/**
 * Side Menu Panel
 * This class is motivated from https://github.com/iandiv/Side-Menu-Panel.
 * Original class has been modified for generalization. Additional methods
 * have been written to suit my needs according to my architecture
 */
public class SideMenuPanel {
    private int x = 0;
    private final int a = 0;
    private int minWidth = 60;
    private int maxWidth = 200;
    private boolean isEnabled;
    private int speed = 2;
    private int responsiveMinWidth = 600;
    private boolean isOpen = false;

    private GroupLayout gl;
    private JPanel menuBar;
    private JPanel sideBar;
    private JPanel mainContentPanel;

    private final JFrame frame;

    /**
     * Constructor
     * @param frame - parent frame
     */
    public SideMenuPanel(JFrame frame) {

        frame.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent arg0) {
                x = 0;
            }
        });

        this.frame = frame;
        setMainAnimation(true);
    }

    /**
     * Constructor
     * @param frame - parent frame
     * @param content - content panel
     */
    public SideMenuPanel(JFrame frame, JPanel content) {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent arg0) {
                x = 0;
            }
        });

        this.frame = frame;

        createSideBar();
        setMainAnimation(true);
        setMainContentPanel(content);
    }

    /**
     * gets open state of the menu panel
     * @return true if menu panel is Open, else false
     */
    public boolean getIsOpen() {
        return isOpen;
    }

    /**
     * Set side menu panel as open or closed
     * @param isOpen true if side menu panel is Open, else false
     */
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * gets the minimum responsive width of the side menu panel
     * @return minimim responsive width
     */
    public int getResponsiveMinWidth() {
        return responsiveMinWidth;
    }

    /**
     *
     * @param responsiveWidth
     */
    public void setResponsiveMinWidth(int responsiveWidth) {
        this.responsiveMinWidth = responsiveWidth;
    }

    /**
     *
     * @return
     */
    public int getSpeed() {
        return speed;
    }

    /**
     *
     * @param speed
     */
    public void setSpeed(int speed) {
        if (speed == 0) {
            speed = maxWidth;
        }
        this.speed = speed;
    }

    /**
     *
     * @return
     */
    public int getMinWidth() {
        return minWidth;
    }

    /**
     *
     * @return
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     *
     * @return
     */
    public JPanel getSideBar() {
        return sideBar;
    }

    /**
     *
     * @return
     */
    public JPanel getMainContentPanel() {
        return mainContentPanel;
    }

    /**
     *
     * @return
     */
    public boolean setMainAnimation() {
        return isEnabled;
    }

    /**
     *
     * @param min
     */
    public void setMinWidth(int min) {
        this.minWidth = min;
    }

    /**
     *
     * @param max
     */
    public void setMaxWidth(int max) {
        this.maxWidth = max;
    }

    /**
     *
     * @param mainContentPanel
     */
    public void setMainContentPanel(JPanel mainContentPanel) {
        this.mainContentPanel = mainContentPanel;
    }

    /**
     *
     * @param isEnabled
     */
    public void setMainAnimation(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     *
     */
    public void onSideMenu() {
        int b = maxWidth % speed;
        if (x == maxWidth) {

            Thread th = new Thread() {
                @Override
                public void run() {
                    for (int i = maxWidth; i >= 0; i -= speed) {
                        try {
                            if (b == i) {
                                i = 0;
                            }
                            TimeUnit.NANOSECONDS.sleep(1);
                            sideBar.setSize(new Dimension(minWidth + i, mainContentPanel.getHeight()));

                            if (sideBar instanceof Container) {
                                for (Component child : ((Container) sideBar).getComponents()) {
                                    child.setSize(new Dimension(maxWidth + minWidth, child.getHeight()));
                                }
                            }
                            if (frame.getWidth() >= responsiveMinWidth) {
                                if (isEnabled) {
                                    mainContentPanel.setLocation(minWidth + i, mainContentPanel.getY());
                                }
                            }
                        } catch (NullPointerException e) {

                            System.out.println("Unknown Side or Main panel \n setSide() and setMain() ");
                            return;

                        } catch (InterruptedException ex) {
                            Logger.getLogger(SideMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

            };
            th.start();

            x = 0;
        } else if (x == 0) {
            Thread th = new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i <= x; i += speed) {
                        try {

                            TimeUnit.NANOSECONDS.sleep(1);
                            if (maxWidth - b == i) {
                                i += b;
                            }

                            sideBar.setSize(new Dimension(minWidth + i, mainContentPanel.getHeight()));

                            if (sideBar instanceof Container) {
                                for (Component child : ((Container) sideBar).getComponents()) {
                                    child.setSize(new Dimension(maxWidth + minWidth, child.getHeight()));
                                }
                            }
                            if (frame.getWidth() >= responsiveMinWidth) {
                                if (isEnabled) {
                                    mainContentPanel.setLocation(minWidth + i, mainContentPanel.getY());
                                }
                            }
                        } catch (NullPointerException e) {
                            System.out.println("ERROR: Unknown value for setSide() or setMain()");

                            return;

                        } catch (InterruptedException ex) {
                            Logger.getLogger(SideMenuPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            };
            th.start();
            x = maxWidth;
        }

    }

    public void closeMenu() {
        if (getIsOpen()) {
            sideBar.setSize(new Dimension(minWidth, sideBar.getHeight()));
            mainContentPanel.setLocation(minWidth, mainContentPanel.getY());
            setGLSize(minWidth);
//            close();
            isOpen = false;
            x = 0;
        }
    }

    public void openMenu() {
        if (!getIsOpen()) {
            sideBar.setSize(new Dimension(minWidth + maxWidth, sideBar.getHeight()));
            mainContentPanel.setLocation(minWidth + maxWidth, mainContentPanel.getY());
            setGLSize(minWidth + maxWidth);
            x = maxWidth;
            isOpen = true;
        }
    }

    private void setGLSize(int size) {
        gl = new GroupLayout(mainContentPanel.getParent());
        mainContentPanel.getParent().setLayout(gl);
        gl.setHorizontalGroup(
                gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl.createSequentialGroup()
                                .addComponent(sideBar, GroupLayout.PREFERRED_SIZE, size, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(mainContentPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );
        gl.setVerticalGroup(
                gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(sideBar, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                        .addComponent(mainContentPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }

    /**
     * Create SideBar
     */
    private void createSideBar(){
        sideBar = new javax.swing.JPanel();
        int defaultWidth = 100;

        sideBar.setBackground(new java.awt.Color(16, 84, 129));
        sideBar.setPreferredSize(new Dimension(defaultWidth, 800));
        //sideBar.setSize(new Dimension(defaultWidth, 800));
    }

    /**
     * Add menu items to the side bar
     * @param menuItems list of menu items
     */
    public void addMenu(List<SideMenuItem> menuItems)  {
        int nItems = menuItems.size();

        GridLayout sidebarLayout = new SideMenuGridLayout(nItems, 1, 7, 15);
        sideBar.setLayout(sidebarLayout);
        //sideBar.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

        for (SideMenuItem menuItem : menuItems ){
            sideBar.add(menuItem);
        }
        sidebarLayout.layoutContainer(sideBar);
    }
}