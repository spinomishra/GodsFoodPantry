package pantry.ui;

import pantry.employee.ui.EmployeeManagerCard;
import pantry.volunteer.ui.VolunteerManagerCard;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Main content panel
 */
public class MainPanel extends JPanel {
    // parent window
    private JFrame parentWindow ;
    // list of screens added to the panel
    private Map<String, JPanel> cards ;
    // card layout
    private CardLayout cardLayout;

    /**
     * Constructor
     * @param frame - parent frame
     */
    public MainPanel(JFrame frame) {
        parentWindow = frame;
        cards = new HashMap<String, JPanel>();
    }

    /**
     * Shows a specific card as determined by the card title
     * @param cardTitle card title
     */
    public void Show(String cardTitle){
        cardLayout.show(this, cardTitle);
    }

    /**
     * get respective Card
     * @param cardTitle title of the card
     * @return card object
     */
    public JPanel getCard(String cardTitle) { return cards.get(cardTitle); }

    /**
     * Initializes components of the main panel
     */
    public void initComponent(){
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        JPanel tilesMgr = new TileManager(parentWindow);
        cards.put(TileManager.Title, tilesMgr);
        Tile empTile = new Tile("Volunteers", Color.WHITE, Color.YELLOW);
        tilesMgr.add(empTile);
    }

    /**
     * Add management cards
     */
    public void addManagementCards(){
        JPanel empCard = new EmployeeManagerCard(parentWindow);
        cards.put(EmployeeManagerCard.Title, empCard);

        JPanel volCard = new VolunteerManagerCard(parentWindow);
        cards.put(VolunteerManagerCard.Title, volCard);

        for (Map.Entry<String, JPanel> entry : cards.entrySet()){
            this.add(entry.getValue(), entry.getKey());
        }
    }
}
