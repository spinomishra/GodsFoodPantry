package pantry.employee.ui;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import pantry.Home;
import pantry.Pantry;
import pantry.helpers.PrintHelper;
import pantry.employee.Employee;

import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;

/**
 * Class to manage employees
 */
public class EmployeeManagerCard extends JPanel implements ListSelectionListener, TableModelListener, ActionListener {
    /**
     * Card Title for Employee Management
     */
    public static final String Title = "Employee Manager";

    /**
     * Add label text
     */
    private static final String addNew = "Add";

    /**
     * Remove label text
     */
    private static final String removeLabel = "Delete";

    /**
     * Print label text
     */
    private static final String printLabel = "Print";

    /**
     * Employee object model
     */
    private EmployeeTableModel employeeModel;

    /**
     * Model use for employee role list
     */
    private ListSelectionModel employeeTableListModel;

    /**
     * Employee Table control
     */
    private JTable employeeTable ;

    /**
     * New button control
     */
    private	JButton newButton;

    /**
     * Remove button control
     */
    private JButton removeButton;

    /**
     * Print button control
     */
    private JButton printButton;

    /**
     * Reference to the employees list
     */
    private ArrayList<Employee> employees;

    /**
     * Parent window object
     */
    private Window  parentWindow;

    /**
     * Constructor
     */
    public EmployeeManagerCard(JFrame parent) {
        super(new BorderLayout());
        setOpaque(true);
        parentWindow = parent;

        // list of employees
        employees = Pantry.getInstance().get_Data().get_Employees();

        // table control
        JScrollPane dataScrollPane = CreateTableForData();

        // Action buttons
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        // "Add New Employee" button
        newButton = new JButton("Add New Employee");
        newButton.setActionCommand(addNew);
        newButton.addActionListener(this);
        newButton.setEnabled(true);

        // "Remove Employee" button
        removeButton = new JButton("Remove Employee");
        removeButton.setActionCommand(removeLabel);
        removeButton.addActionListener(this);
        removeButton.setEnabled(false);

        // "Print Report" button
        printButton = new JButton("Print Report");
        printButton.setActionCommand(printLabel);
        printButton.addActionListener(this);
        printButton.setEnabled(true);

        //Create a panel that uses BoxLayout.
        buttonPane.add(removeButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(newButton);
        buttonPane.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPane.add(printButton);

        // add all controls to container
        add(dataScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    /**
     * Adds a JTable object with a table model
     * @return
     */
    private JScrollPane CreateTableForData() {
        employeeModel = new EmployeeTableModel();
        if (employees != null) {
            for (int i=0; i<employees.size(); i++){
                employeeModel.addRow(employees.get(i));
            }
        }
        // Create the list box to show the volunteers names
        employeeTable = new JTable(employeeModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if(!comp.getBackground().equals(getSelectionBackground())) {
                    Color c = (row % 2 == 0 ? new Color(210, 231, 255) : Color.WHITE);
                    comp.setBackground(c);
                    c = null;
                }
                return comp;
            }
        };
        employeeTable.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS);
        employeeTable.setSelectionBackground(new Color(110, 181, 155));
        employeeTable.setSelectionForeground(new Color(255, 255, 255));
        employeeTableListModel = employeeTable.getSelectionModel();
        employeeTableListModel.addListSelectionListener(this);
        employeeTable.setSelectionModel(employeeTableListModel);

        employeeTable.setRowHeight(40);
        int nCols =  employeeTable.getColumnModel().getColumnCount();
        for (int i = 0; i < nCols; i++) {
            var column = employeeTable.getColumnModel().getColumn(i);
            switch (i) {
                case 0: column.setPreferredWidth(150); break ;
                case 1: column.setPreferredWidth(80); break ;
                case 2: column.setPreferredWidth(100); break ;
                case 3: column.setPreferredWidth(100); break ;
                case 4: column.setPreferredWidth(300); break ;
            }
        }

        setRoleColumn(employeeTable, 1);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        employeeTable.setFillsViewportHeight(true);
        return scrollPane;
    }

    /**
     *
     * @param table
     * @param col
     */
    void setRoleColumn(JTable table, int col){
        TableColumn roleColumn = table.getColumnModel().getColumn(col);

        List<Employee.EmployeeRole> roleList = Arrays.asList(Employee.EmployeeRole.values());
        table.setDefaultRenderer(Employee.EmployeeRole.class, new EmployeeRoleCellRenderer());
        table.setDefaultEditor(Employee.EmployeeRole.class, new EmployeeRoleCellEditor(roleList));

        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        employeeTable.setColumnSelectionAllowed(false);
    }

    /**
     * {@inheritDoc}
     * Action handler for buttons used to manage employees
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == addNew)
        {
            // New Employee button has been clicked
            // prompt to acquire new employee information
            Employee newPerson = EmployeeInfo.createAndShowGUI(this.parentWindow, "New Employee");
            if (newPerson != null)
            {
                // add to employees list
                employees.add(newPerson) ;

                // add to employee table
                employeeModel.addRow(newPerson);
            }
        }
        else if (e.getActionCommand() == removeLabel)
        {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int[] selectedRowIndex = employeeTable.getSelectedRows();
            if (selectedRowIndex.length != 0){
                for (int idx : selectedRowIndex) {
                    Employee emp = employeeModel.getRow(idx);
                    if (emp != null)
                        employees.remove(emp);
                }
                employeeModel.removeRows(selectedRowIndex);
            }

            int size = employeeTable.getRowCount();
            if (size == 0) { //Nobody's left, disable firing.
                removeButton.setEnabled(false);
            }
        }
        else if (e.getActionCommand() == printLabel){
            PrintHelper.Print(this.parentWindow, employeeTable, Home.getPantryName() + " - Employees");
        }
    }

    /**
     * {@inheritDoc}
     * When selection changes, enable button to remove employees data from records
     * @param e list selection change event
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == employeeTableListModel) {
            int[] selectedRowIndex = employeeTable.getSelectedRows();
            //Selection, enable the button.
            removeButton.setEnabled(selectedRowIndex.length > 0);
        }
    }

    /**
     * {@inheritDoc}
     *
     * This fine grain notification tells listeners the exact range
     * of cells, rows, or columns that changed.
     *
     * @param e a {@code TableModelEvent} to notify listener that a table model
     *          has changed
     */
    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getSource() == employeeTableListModel){
            int firstRow = e.getFirstRow();
            int lastRow = e.getLastRow();
            int index = e.getColumn();

            switch (e.getType()){
                case TableModelEvent.UPDATE:
                    if (firstRow == TableModelEvent.HEADER_ROW) {
                    if (index == TableModelEvent.ALL_COLUMNS) {
                        System.out.println("A column was added");
                    } else {
                        System.out.println(index + "in header changed");
                    }
                } else {
                    for (int i = firstRow; i <= lastRow; i++) {
                        if (index == TableModelEvent.ALL_COLUMNS) {
                            System.out.println("ROW: "+ i + ": All columns have changed");
                        } else {
                            System.out.println(index);
                        }
                    }
                }
                break;
            }
        }
    }
/*
    private void GenerateReport() {
        try {
            JasperReport jr;
            InputStream in=getClass().getResourceAsStream(resourcefile + ".ser");
            if (in == null) {
                JasperDesign jd= JRXmlLoader.load(getClass().getResourceAsStream(resourcefile + ".jrxml"));
                jr= JasperCompileManager.compileReport(jd);
            }
            else {
                ObjectInputStream oin=new ObjectInputStream(in);
                jr=(JasperReport)oin.readObject();
                oin.close();
            }
            Map reportparams=new HashMap();
            try {
                reportparams.put("REPORT_RESOURCE_BUNDLE",ResourceBundle.getBundle(resourcefile + ".properties"));
            }
            catch (    MissingResourceException e) {
            }
            reportparams.put("TAXESLOGIC",taxeslogic);
            Map reportfields=new HashMap();
            reportfields.put("TICKET",ticket);
            reportfields.put("PLACE",ticketext);
            JasperPrint jp= JasperFillManager.fillReport(jr,reportparams,new JRMapArrayDataSource(new Object[]{reportfields}));
            //PrintService service=PrintHelper.getPrintService(getProperties().getProperty("machine.printername"));
            //JRPrinterAWT300.printPages(jp,0,jp.getPages().size() - 1,service);
        }
        catch (  Exception e) {
            //MessageInf msg=new MessageInf(MessageInf.SGN_WARNING,AppLocal.getIntString("message.cannotloadreport"),e);
            ///msg.show(this);
        }
    }*/
}
