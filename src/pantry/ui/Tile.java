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
    private static final int DefaultColor = 0xE3C7A6;   // wheat color
    private static final int MouseHoverColor = 0xDA9100;    // harvest gold color
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
        setFont(titleFont);

        // control Size
        this.setMinimumSize(new Dimension(50, 50));
        this.setPreferredSize(new Dimension(300, 300));

        // Color - foreground and background
        if (background instanceof ImageIcon){
            ImageIcon icon = (ImageIcon)background;
            this.setIcon(icon);
        }

        if (background instanceof Color){
            setBackground((Color) background);
        }
        else
            setBackground(new Color(DefaultColor));

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
