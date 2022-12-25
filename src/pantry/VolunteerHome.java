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

import static java.awt.Toolkit.getDefaultToolkit;

/**
 * Home screen for Volunteering mode for food pantry
 */
public class VolunteerHome extends JFrame implements IHome, ActionListener {
    // pantry name
    String pantryName;

    // check-in Tile
    private Tile checkinTile ;

    // check-out Tile
    private Tile checkoutTile;

    // list of volunteers that recently checked-in
    private ArrayList<Volunteer> recentVolunteers;

    // dimension for the volunteer home screen
    private static final Dimension homeDimension = getDefaultToolkit().getScreenSize();

    /**
     * Constructor
     * @param pn Person Name
     */
    public VolunteerHome(String pn) {
        pantryName = pn;
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

        checkinTile = new Tile("Check-in", new Color(0xA38F77), getImageIcon("../images/check-in.png") );
        checkinTile.addActionListener(this);

        mainPanel.add(checkinTile);
        checkoutTile = new Tile("Check-out", new Color(0xA38F77), getImageIcon("../images/check-out.png") );
        checkoutTile.addActionListener(this);

        mainPanel.add(checkoutTile);
        mainPanel.setVisible(true);

        pane.add(mainPanel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void Run() {
        setTitle("PantryWare - "+ pantryName);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

        setMinimumSize(homeDimension);
        setVisible(true);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setResizable(false);
    }

    /**
     * Helper method to construct image icon using the given image path
     * @param imagePath The image path
     * @return ImageIcon object
     */
    private  ImageIcon getImageIcon(String imagePath)
    {
        var resource = getClass().getResource(imagePath);
        if (resource != null)
            return new ImageIcon(((new ImageIcon(resource)).getImage()).getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH));

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
