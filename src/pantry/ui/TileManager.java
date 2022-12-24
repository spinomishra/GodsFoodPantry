package pantry.ui;

import javax.swing.*;
import java.awt.*;

/**
 * TileManager class represents m`anagement of Tiles aka Cards on a screen
 */
public class TileManager extends JPanel {
    /**
     * Tile manager panel title
     */
    public static final String Title = "Tile Manager";

    /**
     * Parent Window
     */
    private Window  parentWindow;

    /**
     * Constructor
     */
    public TileManager(JFrame parent){
        parentWindow = parent;
        initLayout();
    }

    /**
     * initialize layout for the TileManager
     */
    private void initLayout(){
        var layout = new GridBagLayout();
        setLayout(layout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component add(Component comp) {
        addComponentAtFirstAvailablePosition(comp);

        var component = super.add(comp);
        return component;
    }

    /**
     * Adds specified component relative to the last component added to the gridbaglayout.
     * @param comp component to be added
     */
    private void addComponentAtFirstAvailablePosition(Component comp) {
        GridBagLayout layout = (GridBagLayout)this.getLayout();

        // it is possible to reuse the same GridBagConstraints instance for multiple components, even if the
        // components have different constraints. However, it is recommended that you do not reuse GridBagConstraints,
        // as this can very easily lead to you introducing subtle bugs if you forget to reset the fields for each
        // new instance.
        var gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
        gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
        //gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.weighty = 0.25;
        gridBagConstraints.insets = new Insets(5,5,5,5);  //top padding
        layout.setConstraints(comp, gridBagConstraints);
    }
}
