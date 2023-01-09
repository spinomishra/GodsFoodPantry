package pantry.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

/**
 * Menu items on the sidebar
 */
public class SideMenuItem extends JButton {
    /**
     * Menu item's height (constant)
     */
    public static int MENUITEM_HEIGHT = 45;
    /**
     * Menu item's width (constant)
     */
    public static int MENUITEM_WIDTH = 70;

    /**
     * background color for the menu item
     */
    private final Color backgroundColor = new Color(16,84,129);
    /**
     * Foreground color for the menu item
     */
    private final Color foregroundColor = new Color(195, 217, 233);
    /**
     * Color when mouse hovers over the menu item
     */
    private final Color hoverColor = new Color(151,83,74);

    /**
     * Image icon for the menu item
     */
    private ImageIcon icon ;
    /**
     * Image to be displayed when mouse hovers over menu item
     */
    private ImageIcon icon_hover;
    /**
     * Image to be displayed when icon is pressed
     */
    private ImageIcon icon_pressed;

    /**
     * Constructor
     * @param menuText menu Text
     * @param menuImgPath menu Image path
     * @throws Exception
     */
    public SideMenuItem(String menuText, String menuImgPath) throws Exception {
        this(menuText, menuImgPath, MENUITEM_WIDTH, MENUITEM_HEIGHT);
    }

    public SideMenuItem(String menuText, String menuImgPath, int width, int height) throws Exception {
        this.setFont(new java.awt.Font("Microsoft PhagsPa", 0, 14));
        this.setForeground(foregroundColor);
        this.setBackground(backgroundColor);

        if (menuText !=null && !menuText .trim().isEmpty()) {
            //setText(menuText);
            this.setToolTipText(menuText);
        }

        //this.setPreferredSize(new java.awt.Dimension(150, 32));

        this.setMargin(new java.awt.Insets(2, 5, 2, 5));

        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setHideActionText(true);

        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setHorizontalTextPosition(SwingConstants.RIGHT);

        this.setIconTextGap(20);
        this.setPreferredSize(new Dimension(width, height));
//        this.setMaximumSize(new Dimension(MENUITEM_WIDTH, MENUITEM_HEIGHT));
//        this.setMinimumSize(new Dimension(MENUITEM_WIDTH, MENUITEM_HEIGHT));

        setIconImage(menuImgPath);
    }

    /**
     * Constructor
     * @param menuText
     * @param menuImgPath
     * @param actionListener
     */
    public SideMenuItem(String menuText, String menuImgPath, ActionListener actionListener) throws Exception {
        this(menuText, menuImgPath);
        this.addActionListener(actionListener);
    }

    /**
     * Constructor
     * @param menuText
     * @param actionListener
     */
    public SideMenuItem(String menuText, ActionListener actionListener) throws Exception {
        this(menuText, "");
        this.addActionListener(actionListener);
    }

    /**
     * sets image for the menu Icon
     * @param imagePath path of the image
     * @throws FileNotFoundException
     */
    public void setIconImage(String imagePath) throws FileNotFoundException {
        Dimension d = this.getPreferredSize() ;
        if (imagePath!=null && !imagePath.trim().isEmpty()) {
            icon = new ImageIcon(((new ImageIcon(getClass().getResource(imagePath))).getImage()).getScaledInstance(
                    d.height-2, d.height-2, java.awt.Image.SCALE_SMOOTH)) ;
            setIcon(icon);

            icon_hover = new ImageIcon(((new ImageIcon(getClass().getResource(imagePath))).getImage()).getScaledInstance(
                    d.height + 5, d.height + 5, java.awt.Image.SCALE_SMOOTH));
            this.setRolloverEnabled(true);  // without enabling rollover, rollover icon is not rendered
            this.setRolloverIcon(icon_hover);

            icon_pressed = new ImageIcon(((new ImageIcon(getClass().getResource(imagePath))).getImage()).getScaledInstance(
                    d.height - 3, d.height - 3, java.awt.Image.SCALE_SMOOTH));
            this.setPressedIcon(icon_pressed);
        }
    }

    /**
     * Set icons for normal, hover and pressed states
     * @param imgNormal  Normal Image
     * @param imgHover  Hover Image
     * @param imgPressed Menu item pressed image
     * @return the menu item object
     * @throws FileNotFoundException
     */
    public SideMenuItem setIcons(String imgNormal, String imgHover, String imgPressed) throws FileNotFoundException {
        if (imgNormal!=null && !imgNormal.trim().isEmpty()) {
            icon = new ImageIcon(((new ImageIcon(getClass().getResource(imgNormal))).getImage()).getScaledInstance(
                    MENUITEM_HEIGHT - 2, MENUITEM_HEIGHT - 2, java.awt.Image.SCALE_SMOOTH));
        }

        if (imgHover!=null && !imgHover.trim().isEmpty()) {
            icon_hover = new ImageIcon(((new ImageIcon(getClass().getResource(imgHover))).getImage()).getScaledInstance(
                    MENUITEM_HEIGHT - 2, MENUITEM_HEIGHT - 2, java.awt.Image.SCALE_SMOOTH));
        }

        if (imgPressed!=null && !imgPressed.trim().isEmpty()) {
            icon_pressed = new ImageIcon(((new ImageIcon(getClass().getResource(imgPressed))).getImage()).getScaledInstance(
                    MENUITEM_HEIGHT - 2, MENUITEM_HEIGHT - 2, java.awt.Image.SCALE_SMOOTH));
        }

        if (icon != null)
            setIcon(icon);

        if (icon_hover != null) {
            this.setRolloverEnabled(true);
            this.setRolloverIcon(icon_hover);
        }

        if (icon_pressed != null)
            this.setPressedIcon(icon_pressed);

        return this;
    }

    /**
     * Mouse event listener
     */
    void setMouseListener(){
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // enables to set the background color for the button. If set to false, the background color doesn't take effect
                setOpaque(true);
                setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                // disable the background color for the button.
                setOpaque(false);
                setBackground(backgroundColor);
            }
        });
    }
}
