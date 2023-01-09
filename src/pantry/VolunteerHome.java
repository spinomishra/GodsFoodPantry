package pantry;

import pantry.interfaces.IHome;
import pantry.ui.Tile;
import pantry.ui.TileManager;
import pantry.volunteer.Volunteer;
import pantry.volunteer.ui.VolunteerCheckout;
import pantry.volunteer.ui.VolunteerInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Home screen for Volunteering mode for food pantry
 */
public class VolunteerHome extends JFrame implements IHome, ActionListener {
    /**
     * Check-in Tile control
     */
    private Tile checkinTile ;

    /**
     * Check-out Tile control
     */
    private Tile checkoutTile;

    /**
     * List of volunteers that recently checked-in
     */
    private ArrayList<Volunteer> recentVolunteers;


    /**
     * Constructor
     */
    public VolunteerHome() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ShowHome() {
        // create a panel
        Container pane = this.getContentPane();

        // main Panel
        JPanel mainPanel = new TileManager(this);

        final int w = 300;
        final int h = 300;

        checkinTile = new Tile("Check-in", new Color(0xA38F77), getImageIcon("/images/check-in.png", w, h) );
        checkinTile.addActionListener(this);
        checkinTile.setMinimumSize(new Dimension(50, 50));
        checkinTile.setPreferredSize(new Dimension(300, 300));

        mainPanel.add(checkinTile);
        checkoutTile = new Tile("Check-out", new Color(0xA38F77), getImageIcon("/images/check-out.png",w ,h) );
        checkoutTile.addActionListener(this);
        checkoutTile.setMinimumSize(new Dimension(50, 50));
        checkoutTile.setPreferredSize(new Dimension(300, 300));

        mainPanel.add(checkoutTile);

        pane.add(mainPanel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void Run() {
        setTitle(Home.getDefaultPageTitle() +  " - Volunteer Check-in/out");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * @param e window event
             */
            @Override
            public void windowClosing(WindowEvent e) {
                Pantry.getInstance().get_Data().Save();
            }
        });

        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    /**
     * Helper method to construct image icon using the given image path
     * @param imagePath The image path
     * @param w image icon width
     * @param h image icon height
     * @return ImageIcon object
     */
    private  ImageIcon getImageIcon(String imagePath, int w, int h)
    {
        var resource = getClass().getResource(imagePath);
        if (resource != null)
            return new ImageIcon(((new ImageIcon(resource)).getImage()).getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH));

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == checkinTile){
            Volunteer v = VolunteerInfo.createAndShowGUI(this, "Volunteer Check-in");
            if (v != null) {
                if (recentVolunteers == null)
                    recentVolunteers = new ArrayList<>();
                recentVolunteers.add(v);
            }
        }
        else if (e.getSource() == checkoutTile) {
            VolunteerCheckout checkout = new VolunteerCheckout();
            checkout.Checkout(this, recentVolunteers);
        }
    }
}
