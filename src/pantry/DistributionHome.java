package pantry;

import pantry.data.FileAdapter;
import pantry.distribution.Consumer;
import pantry.distribution.ui.ConsumerInfo;
import pantry.distribution.ui.ConsumerTable;
import pantry.interfaces.IHome;
import pantry.interfaces.ITableSelectionChangeListener;
import pantry.ui.Tile;
import pantry.ui.TileManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static java.awt.Toolkit.getDefaultToolkit;

/**
 * Home screen for mode where food distribution is managed
 */
public class DistributionHome extends JFrame implements IHome, ActionListener, pantry.interfaces.IPantryData, ITableSelectionChangeListener {
    /**
     * Pantry Name
     */
    private String pantryName;

    /**
     * Today's consumers list
     */
    private ArrayList<Consumer> todaysConsumers = null;

    /**
     * Table of consumer records
     */
    private ConsumerTable consumerTable = null;

    /**
     * Previously recorded food consumers
     */
    private ArrayList<Consumer> oldConsumers=null;

    /**
     * Allows record to be deleted
     */
    JButton deleteBtn;

    /**
     * Record choice combo box
     */
    JComboBox comboBox;

    /**
     * Constructor
     * @param pn Pantry Name
     */
    public DistributionHome(String pn) {
        pantryName = pn;
        todaysConsumers = new ArrayList<Consumer>();
        LoadConsumers() ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ShowHome() {
        // create a panel
        Container pane = this.getContentPane();

        JPanel topPanel = new JPanel();
        var topPanelLayout = new BorderLayout();
        topPanel.setLayout(topPanelLayout);
        topPanel.add(addTiles(), BorderLayout.LINE_START);

        pane.add(topPanel, BorderLayout.NORTH);
        pane.add(addTable(), BorderLayout.CENTER);
        pane.add(addActionButtons(), BorderLayout.SOUTH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void Run() {
        setTitle("PantryWare - Food Distribution Management - "+ pantryName);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            /**
             * Invoked when a window is in the process of being closed.
             * @param e window event
             */
            @Override
            public void windowClosing(WindowEvent e) {
                Pantry.getInstance().get_Data().Save();
                SaveConsumers();
            }
        });

        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    /**
     * Adds panel to contain buttons
     * @return The button panel object
     */
    private JPanel addActionButtons() {
        var buttonPanel = new JPanel(new BorderLayout(5,5)) ;
        buttonPanel.setBorder(new EmptyBorder(6,6,6,6));
        deleteBtn = new JButton("Delete Record");
        deleteBtn.setEnabled(false);
        deleteBtn.setMinimumSize(new Dimension(200, 100));
        var deleteButtonIcon = getImageIcon("/images/recyclebin.png", 12,12);
        deleteBtn.setIcon(deleteButtonIcon);

        consumerTable.addSelectionChangeListener(this);

        var This = this;
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox.getSelectedIndex() == 0) {
                    int[] selectedRows = consumerTable.getSelectedRows();
                    if (selectedRows.length > 0) {
                        for (var i : selectedRows) {
                            todaysConsumers.remove(i);
                        }
                        SaveConsumers();
                        deleteBtn.setEnabled(false);
                    }
                    consumerTable.deleteSelectedRows();
                }
                else {
                    String info = "Previous records are now sealed and cannot be altered. Only Today's records can be deleted.";
                    String msg = "<html><body><p style='width: 300px; '>"+info+"</p></body></html>";

                    JOptionPane.showMessageDialog(This, msg, "Consumer Records", JOptionPane.INFORMATION_MESSAGE| JOptionPane.OK_OPTION);
                }
            }
        });

       buttonPanel.add(deleteBtn, BorderLayout.WEST);

       return buttonPanel;
    }

    /**
     * Adds Consumer records table to home page
     * @return The Table panel
     */
    private JPanel addTable() {
        var screenSize = getDefaultToolkit().getScreenSize();

        var tablePanel = new JPanel() ;
        var topPanelLayout = new BorderLayout();
        tablePanel.setLayout(topPanelLayout);
        tablePanel.setOpaque(true);
        tablePanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(5.0f)));

        {
            consumerTable = new ConsumerTable(todaysConsumers);
            JScrollPane scrollPane = new JScrollPane(consumerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            consumerTable.setFillsViewportHeight(true);
            tablePanel.add(scrollPane, BorderLayout.CENTER);

            {
                JPanel panel = new JPanel();
                JLabel label = new JLabel("Food Distribution Consumer Records");
                Font labelFont = new Font("Serif", Font.BOLD|Font.ITALIC, 14);
                label.setFont(labelFont);
                label.setHorizontalTextPosition(JLabel.CENTER);
                panel.add(label, BorderLayout.CENTER);

                String[] recordChoices = {"Today's", "Previous unconsolidated records"};
                comboBox = new JComboBox(recordChoices);
                comboBox.setSelectedIndex(0);
                comboBox.setMaximumSize(new Dimension(200, 80));
                comboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        deleteBtn.setEnabled(false);
                        switch (comboBox.getSelectedIndex()){
                            case 0:
                                consumerTable.ChangeDataModel(todaysConsumers);
                                break ;

                            case 1:
                                if (oldConsumers == null)
                                    oldConsumers = LoadOldConsumers();
                                consumerTable.ChangeDataModel(oldConsumers);
                                break ;
                        }
                    }
                });
                panel.add(comboBox, BorderLayout.SOUTH);

                tablePanel.add(panel, BorderLayout.NORTH);
            }
        }

        {
            JPanel signaturePanel = new JPanel(new BorderLayout());
            signaturePanel.setOpaque(true);
            signaturePanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.2f)));

            {
                JPanel panel = new JPanel();
                JLabel signatureDisplayControl = new JLabel();
                Dimension dim = new Dimension(200, 200);
                signatureDisplayControl.setMinimumSize(dim);
                signatureDisplayControl.setPreferredSize(dim);
                panel.add(signatureDisplayControl, BorderLayout.CENTER);
                consumerTable.setSignatureDisplayControl(signatureDisplayControl);
                signaturePanel.add(panel, BorderLayout.CENTER);
            }

            {
                JPanel panel = new JPanel();
                JLabel label = new JLabel("Signature Display");
                Font labelFont = new Font("Serif", Font.BOLD|Font.ITALIC, 14);
                label.setFont(labelFont);
                label.setHorizontalTextPosition(JLabel.CENTER);
                panel.add(label, BorderLayout.CENTER);
                signaturePanel.add(panel, BorderLayout.NORTH);
            }

            tablePanel.add(signaturePanel, BorderLayout.EAST);
        }

        return tablePanel ;
    }

    /**
     * Add tiles to the home page
     * @return The Tile Manager object
     */
    private JPanel addTiles() {
        final int w = 100;
        final int h = 100 ;

        JPanel tilePanel = new TileManager(this) ;
        Dimension tilePanelDim = new Dimension(400, 150) ;

        tilePanel.setMaximumSize(tilePanelDim);
        tilePanel.setPreferredSize(tilePanelDim);

        Font tileFont = new Font("Serif", Font.BOLD|Font.ITALIC, 14);
        Dimension minDim = new Dimension(50, 50) ;
        Dimension prefDim = new Dimension(w, h);
        Tile manualEntryTile = new Tile("Manual Entry", Color.BLACK, getImageIcon("/images/manual-entry.png", w-20,h-20) );
        manualEntryTile.setFont(tileFont);
        manualEntryTile.setActionCommand("Manual-Entry");
        manualEntryTile.addActionListener(this);
        manualEntryTile.setMinimumSize(minDim);
        manualEntryTile.setPreferredSize(prefDim);
        // text positioning and alignment
        manualEntryTile.setVerticalTextPosition(JLabel.TOP);
        manualEntryTile.setHorizontalTextPosition(JLabel.CENTER);
        manualEntryTile.setVerticalAlignment(JLabel.BOTTOM);
        tilePanel.add(manualEntryTile);

        Tile scanEntryTile = new Tile("Scan Identity", Color.BLACK, getImageIcon("/images/scan-id.png", w-20,h-20) );
        scanEntryTile.setFont(tileFont);
        scanEntryTile.setActionCommand("ScanId");
        scanEntryTile.addActionListener(this);
        scanEntryTile.setMinimumSize(minDim);
        scanEntryTile.setPreferredSize(prefDim);
        scanEntryTile.setVerticalTextPosition(JLabel.TOP);
        scanEntryTile.setHorizontalTextPosition(JLabel.CENTER);
        scanEntryTile.setVerticalAlignment(JLabel.BOTTOM);
        tilePanel.add(scanEntryTile);

        return tilePanel;
    }

    /**
     * {@inheritDoc}
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Manual-Entry":{
                Consumer consumer = ConsumerInfo.RecordInformationManually(this);
                if (consumer != null){
                    try{
                        todaysConsumers.add(consumer);
                        SaveConsumers();
                        consumerTable.add(consumer);
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Food Distribution - Exception", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            break;

            case "ScanId":
                String info = "This option requires Barcode scanner with support to scan IDs";
                String msg = "<html><body><p style='width: 200px;'>"+info+"</p></body></html>";
                JOptionPane.showMessageDialog(this, msg, "Food Distribution", JOptionPane.OK_OPTION| JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }

    /**
     * {@inheritDoc}
     * @param ois  ObjectInputStream object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void ReadFrom(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        Objects.requireNonNull(ois);
        todaysConsumers = (ArrayList<Consumer>) ois.readObject();
    }

    /**
     * {@inheritDoc}
     * @param oos output stream
     * @throws IOException
     */
    @Override
    public void WriteTo(ObjectOutputStream oos) throws IOException {
        Objects.requireNonNull(oos);
        oos.writeObject(todaysConsumers);
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
        if (resource != null) {
            var img = ((new ImageIcon(resource)).getImage()).getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }

    /**
     * Load consumers from today's food distribution drive
     */
    private void LoadConsumers() {
        if (todaysConsumers == null){
            todaysConsumers = new ArrayList<>();
        }
        else
            todaysConsumers.clear();

        String filename = "food.distribution.consumers." + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddyyyy")).toString() + ".rec";
        FileAdapter recordsFile = new FileAdapter(filename, this);
        recordsFile.Load();
    }

    /**
     * Save consumers to file
     */
    private void SaveConsumers(){
        if (todaysConsumers != null && todaysConsumers.size()>0) {
            String filename = "food.distribution.consumers." + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddyyyy")).toString() + ".rec";
            FileAdapter recordsFile = new FileAdapter(filename, this);
            recordsFile.Save();
        }
    }

    /**
     * Load previous consumer records that have still not been consolidated
     * @return list of old consumers
     */
    private ArrayList<Consumer> LoadOldConsumers() {
        String todaysFile = "food.distribution.consumers." + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddyyyy")).toString() + ".rec";

        File dir = new File(".\\");
        File [] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".rec") && name.startsWith("food.distribution.consumers.");
            }
        });

        ArrayList<Consumer>  records = new ArrayList<>();
        for (File file:files){
            if (!todaysFile.equals(file.getName())){
                try
                {
                    var fis = new FileInputStream(file);
                    var ois = new ObjectInputStream(fis);

                    if (ois != null) {
                        records.addAll ((ArrayList<Consumer>) ois.readObject());
                    }

                    ois.close();
                    fis.close();
                }
                catch (FileNotFoundException fife){
                    System.out.println("No records exist");
                }
                catch (IOException ioe){
                    ioe.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return records;
    }

    /**
     * Consumer Table selection change listener
     * @param table Table raising the event
     * @param row selected row
     * @param col selected col
     */
    @Override
    public void SelectionChanged(JTable table, int row, int col) {
        deleteBtn.setEnabled(true);
    }
}
