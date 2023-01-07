package pantry.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Tile class represents a tile aka card object
 */
public class Tile extends JButton {
    /**
     * Default color for the tile - Wheat Color constant value
     */
    private static final int DefaultColor = 0xE3C7A6;   // wheat color
    /**
     * Harvest Gold color constant value used when mouse hovers over the tile
     */
    private static final int MouseHoverColor = 0xDA9100;    // harvest gold color
    /**
     * Blue shade color constant value used when mouse is pressed over the tile
     */
    private static final int MousePressedColor = 0x0056D9; // blue shade color

    /**
     * Constructor
     * @param title Tile's title
     * @param background Background color or image
     */
    public Tile(String title, Color textColor, Object background) {
        this(title, new Font("Serif", Font.BOLD|Font.ITALIC, 54), textColor, background);
    }

    /**
     * Construct with option to set title font
     * @param title  Tile title text
     * @param titleFont Title font
     * @param titleColor Title text color
     * @param background Tile's background
     */
    public Tile(String title, Font titleFont, Color titleColor, Object background) {
        super(title);
        setOpaque(true);

        // text font
        if (titleFont != null)
            setFont(titleFont);

        if (background != null) {
            // Color - foreground and background
            if (background instanceof ImageIcon){
                ImageIcon icon = (ImageIcon)background;
                this.setIcon(icon);
            }

            setBackground(background instanceof Color ? (Color) background : new Color(DefaultColor));
        }
        else
            setBackground(new Color(DefaultColor));

        if (titleColor != null)
            setForeground(titleColor);

        // border for the tile
        Border edge = BorderFactory.createRaisedBevelBorder();
        setBorder(edge);

        // text positioning and alignment
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalAlignment(JLabel.BOTTOM);

        // mouse listener
        this.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt)
            {

                setBackground(new Color(MouseHoverColor));
            }
            public void mouseExited(MouseEvent evt)
            {
                setBackground( background instanceof Color ? (Color) background : new Color(DefaultColor));
            }

            public void mousePressed(MouseEvent e){
                setBackground(new Color(MouseHoverColor).brighter());
            }
            public void mouseReleased(MouseEvent e){
                setBackground( background instanceof Color ? (Color) background : new Color(DefaultColor));
            }
        });
    }
}
