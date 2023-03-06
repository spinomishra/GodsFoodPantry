package pantry;

import pantry.data.FileAdapter;
import pantry.distribution.Consumer;
import pantry.distribution.ui.ConsumerInfo;
import pantry.distribution.ui.ConsumerTable;
import pantry.distribution.ui.ConsumerTableModel;
import pantry.helpers.PrintHelper;
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
    private ArrayList<Consumer> oldConsumers = null;

    /**
     * Allows record to be deleted
     */
    JButton deleteBtn;

    /**
     * Record choice combo box
     */
    JComboBox comboBox;

    /**
     * Signature display control
     */
    JLabel signatureDisplayControl;

    /**
     * Constructor
     */
    public DistributionHome() {
        todaysConsumers = new ArrayList<Consumer>();
        LoadConsumers();
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
        setTitle(Home.getDefaultPageTitle() + " - Food Distribution Management");
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
     *
     * @return The button panel object
     */
    private JPanel addActionButtons() {
        var This = this;

        var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(new EmptyBorder(6, 6, 6, 6));
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        deleteBtn = new JButton("Delete Record");
        deleteBtn.setEnabled(false);
        deleteBtn.setMinimumSize(new Dimension(200, 100));
        var deleteButtonIcon = getImageIcon("/images/recyclebin.png", 12, 12);
        deleteBtn.setIcon(deleteButtonIcon);

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
                } else {
                    String info = "Previous records are now sealed and cannot be altered. Only Today's records can be deleted.";
                    String msg = "<html><body><p style='width: 300px; '>" + info + "</p></body></html>";

                    JOptionPane.showMessageDialog(This, msg, "Consumer Records", JOptionPane.INFORMATION_MESSAGE | JOptionPane.OK_OPTION);
                }
            }
        });

        buttonPanel.add(deleteBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        JButton printButton = new JButton("Print Records");
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrintHelper.Print(This, consumerTable, " - Food Distribution Report");
            }
        });
        buttonPanel.add(printButton);

        return buttonPanel;
    }

    /**
     * Adds Consumer records table to home page
     *
     * @return The Table panel
     */
    private JPanel addTable() {
        var screenSize = getDefaultToolkit().getScreenSize();

        var tablePanel = new JPanel();
        var topPanelLayout = new BorderLayout();
        tablePanel.setLayout(topPanelLayout);
        tablePanel.setOpaque(true);
        tablePanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(5.0f)));

        {
            consumerTable = new ConsumerTable(todaysConsumers);

            JScrollPane scrollPane = new JScrollPane(consumerTable,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            consumerTable.setFillsViewportHeight(true);
            consumerTable.addSelectionChangeListener(this);
            tablePanel.add(scrollPane, BorderLayout.CENTER);

            {
                JPanel panel = new JPanel();
                JLabel label = new JLabel("Food Distribution Consumer Records");
                Font labelFont = new Font("Serif", Font.BOLD | Font.ITALIC, 14);
                label.setFont(labelFont);
                label.setHorizontalTextPosition(JLabel.CENTER);
                panel.add(label, BorderLayout.CENTER);

                String[] orderChoices = {"", "Name", "Address", "Age"};
                JComboBox orderBox = new JComboBox(orderChoices);

                String[] recordChoices = {"Today's", "Previous unconsolidated records"};
                comboBox = new JComboBox(recordChoices);
                comboBox.setSelectedIndex(0);
                comboBox.setMaximumSize(new Dimension(200, 80));
                comboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        deleteBtn.setEnabled(false);
                        switch (comboBox.getSelectedIndex()) {
                            case 0:
                                consumerTable.ChangeDataModel(todaysConsumers);
                                orderBox.setVisible(false);
                                break;

                            case 1:
                                if (oldConsumers == null)
                                    oldConsumers = LoadOldConsumers();
                                consumerTable.ChangeDataModel(oldConsumers);
                                orderBox.setVisible(true);
                                break;
                        }
                    }
                });
                panel.add(comboBox, BorderLayout.SOUTH);

                orderBox.setSelectedIndex(0);
                orderBox.setMaximumSize(new Dimension(200, 80));
                orderBox.setVisible(false);

                orderBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (oldConsumers == null)
                            oldConsumers = LoadOldConsumers();

                        switch (orderBox.getSelectedIndex()) {
                            case 0: {
                                consumerTable.ChangeDataModel(oldConsumers);
                            }
                            break;

                            case 1: {
                                var sortedData = Sort(oldConsumers, "Name BY ASC");
                                consumerTable.ChangeDataModel(sortedData);
                            }
                            break;

                            case 2: {
                                var sortedData = Sort(oldConsumers, "Name BY ASC, Address BY ASC");
                                consumerTable.ChangeDataModel(sortedData);
                            }
                            break;

                            case 3: {
                                var sortedData = Sort(oldConsumers, "Age BY ASC, Name BY DESC");
                                consumerTable.ChangeDataModel(sortedData);
                            }
                            break;
                        }
                    }
                });
                panel.add(orderBox, BorderLayout.EAST);


                tablePanel.add(panel, BorderLayout.NORTH);
            }
        }

        {
            JPanel signaturePanel = new JPanel(new BorderLayout());
            signaturePanel.setOpaque(true);
            signaturePanel.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.2f)));

            {
                JPanel panel = new JPanel();
                signatureDisplayControl = new JLabel();
                Dimension dim = new Dimension(200, 200);
                signatureDisplayControl.setMinimumSize(dim);
                signatureDisplayControl.setPreferredSize(dim);
                panel.add(signatureDisplayControl, BorderLayout.CENTER);
                signaturePanel.add(panel, BorderLayout.CENTER);
            }

            {
                JPanel panel = new JPanel();
                JLabel label = new JLabel("Signature Display");
                Font labelFont = new Font("Serif", Font.BOLD | Font.ITALIC, 14);
                label.setFont(labelFont);
                label.setHorizontalTextPosition(JLabel.CENTER);
                panel.add(label, BorderLayout.CENTER);
                signaturePanel.add(panel, BorderLayout.NORTH);
            }

            tablePanel.add(signaturePanel, BorderLayout.EAST);
        }

        return tablePanel;
    }

    /**
     * Add tiles to the home page
     *
     * @return The Tile Manager object
     */
    private JPanel addTiles() {
        final int w = 100;
        final int h = 100;

        JPanel tilePanel = new TileManager(this);
        Dimension tilePanelDim = new Dimension(400, 150);

        tilePanel.setMaximumSize(tilePanelDim);
        tilePanel.setPreferredSize(tilePanelDim);

        Font tileFont = new Font("Serif", Font.BOLD | Font.ITALIC, 14);
        Dimension minDim = new Dimension(50, 50);
        Dimension prefDim = new Dimension(w, h);
        Tile manualEntryTile = new Tile("Manual Entry", Color.BLACK, getImageIcon("/images/manual-entry.png", w - 20, h - 20));
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

        Tile scanEntryTile = new Tile("Scan Identity", Color.BLACK, getImageIcon("/images/scan-id.png", w - 20, h - 20));
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
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Manual-Entry": {
                Consumer consumer = ConsumerInfo.RecordInformationManually(this);
                if (consumer != null) {
                    try {
                        todaysConsumers.add(consumer);
                        SaveConsumers();
                        consumerTable.add(consumer);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Food Distribution - Exception", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            break;

            case "ScanId":
                String info = "This option requires Barcode scanner with support to scan IDs";
                String msg = "<html><body><p style='width: 200px;'>" + info + "</p></body></html>";
                JOptionPane.showMessageDialog(this, msg, "Food Distribution", JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param ois ObjectInputStream object
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
     *
     * @param oos output stream
     * @throws IOException
     */
    @Override
    public void WriteTo(ObjectOutputStream oos) throws IOException {
        Objects.requireNonNull(oos);
        oos.writeObject(todaysConsumers);
    }

    /**
     * Helper methoe d to construct image icon using thgiven image path
     *
     * @param imagePath The image path
     * @param w         image icon width
     * @param h         image icon height
     * @return ImageIcon object
     */
    private ImageIcon getImageIcon(String imagePath, int w, int h) {
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
        if (todaysConsumers == null) {
            todaysConsumers = new ArrayList<>();
        } else
            todaysConsumers.clear();

        String filename = "food.distribution.consumers." + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".rec";
        FileAdapter recordsFile = new FileAdapter(filename, this);
        recordsFile.Load();
    }

    /**
     * Save consumers to file
     */
    private void SaveConsumers() {
        if (todaysConsumers != null && todaysConsumers.size() > 0) {
            String filename = "food.distribution.consumers." + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".rec";
            FileAdapter recordsFile = new FileAdapter(filename, this);
            recordsFile.Save();
        }
    }

    /**
     * Load previous consumer records that have still not been consolidated
     *
     * @return list of old consumers
     */
    private ArrayList<Consumer> LoadOldConsumers() {
        String todaysFile = "food.distribution.consumers." + LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".rec";

        File dir = new File(".\\");
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".rec") && name.startsWith("food.distribution.consumers.");
            }
        });

        ArrayList<Consumer> records = new ArrayList<>();
        for (File file : files) {
            if (!todaysFile.equals(file.getName())) {
                try {
                    var fis = new FileInputStream(file);
                    var ois = new ObjectInputStream(fis);

                    if (ois != null) {
                        records.addAll((ArrayList<Consumer>) ois.readObject());
                    }

                    ois.close();
                    fis.close();
                } catch (FileNotFoundException fife) {
                    System.out.println("No records exist");
                } catch (IOException ioe) {
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
     *
     * @param table Table raising the event
     * @param row   selected row
     * @param col   selected col
     */
    @Override
    public void SelectionChanged(JTable table, int row, int col) {
        deleteBtn.setEnabled(true);

        Consumer consumer = ((ConsumerTableModel) table.getModel()).getRow(row);
        if (consumer != null) {
            Image image = consumer.getSignatureImage();
            if (image != null && signatureDisplayControl != null) {
                signatureDisplayControl.setIcon(new ImageIcon(image));
            }
        }
    }

    /**
     * Sorts the consumers based on the input criteria in specified order
     *
     * @param dataModel       input data model that needs to be sorted
     * @param sortingCriteria Criteria used for sorting  e.g. "Name or Address BY ASC, NAME BY DESC" or "Address BY asc"
     * @return sorted data model
     */
    public ArrayList<Consumer> Sort(ArrayList<Consumer> dataModel, String sortingCriteria) {
        Objects.requireNonNull(sortingCriteria);
        ArrayList<Consumer> sortedModel = new ArrayList<>(dataModel);
        String[] criteriaArray = sortingCriteria.split(",");

        /**
         * Represents sorting criteria
         */
        class SortCriteria {
            public String dataName;
            public boolean ascending;

            /**
             * Constructor
             * @param criteria
             */
            public SortCriteria(String criteria) {
                String[] splitTokens = criteria.trim().split("BY");
                if (splitTokens.length == 2) {
                    dataName = splitTokens[0].trim();
                    String order = splitTokens[1].trim();
                    ascending = order.compareToIgnoreCase("asc") == 0;
                } else if (splitTokens.length == 0) {
                    dataName = criteria.trim();
                    ascending = true;
                }
            }
        }

        /**
         * Sorter class. This class provides method to sort data at multiple levels
         */
        class Sorter {
            /**
             * Sort method
             * @param dataModel data to sort
             * @param idxStart index to start sorting from
             * @param idxEnd index to stop sorting at
             * @param sortCriteria sorting criteria list
             * @param sortLevel sorting level
             */
            private void Sort(ArrayList<Consumer> dataModel, int idxStart, int idxEnd, ArrayList<SortCriteria> sortCriteria, int sortLevel) {
                if (dataModel.size() == 1)
                    return;    // nothing to sort

                if (sortLevel > sortCriteria.size())   // incorrect sort level
                    return;

                //
                // 1. Sort on the level.
                // 2. Remember the starting locations of each level after sorting
                // 3. Repeat for next sorting criteria, only sorting the subsections.
                //
                String dataName = sortCriteria.get(sortLevel - 1).dataName;
                boolean ascending = sortCriteria.get(sortLevel - 1).ascending;

                for (int x = idxStart; x < idxEnd; x++) {
                    for (int i = idxStart; i < idxEnd - x - 1; i++) {
                        if (sortLevel == 2) {
                            SortCriteria criteria = sortCriteria.get(0);
                            String levelData1 = dataModel.get(i).getData(criteria.dataName);
                            String levelData2 = dataModel.get(i + 1).getData(criteria.dataName);
                            if (levelData1 != levelData2)
                                continue;
                        }

                        try {
                            String data1 = dataModel.get(i).getData(dataName);
                            String data2 = dataModel.get(i + 1).getData(dataName);

                            if (ascending) {
                                if (data1.compareTo(data2) > 0) {
                                    Consumer temp = dataModel.get(i);
                                    dataModel.set(i, dataModel.get(i + 1));
                                    dataModel.set(i + 1, temp);
                                }
                            } else {
                                if (data1.compareTo(data2) < 0) {
                                    Consumer temp = dataModel.get(i);
                                    dataModel.set(i, dataModel.get(i + 1));
                                    dataModel.set(i + 1, temp);
                                }
                            }
                        } catch (UnsupportedOperationException ex) {

                        }
                    }
                }
            }
        }

        var sortCriteriaList = new java.util.ArrayList<SortCriteria>();
        for (String c : criteriaArray) {
            SortCriteria sortCriteria = new SortCriteria(c);
            sortCriteriaList.add(sortCriteria);
        }

        Sorter sorter = new Sorter();
        sorter.Sort(sortedModel, 0, sortedModel.size(), sortCriteriaList, 1);
        sorter.Sort(sortedModel, 0, sortedModel.size(), sortCriteriaList, 2);
        return sortedModel;
    }
}
