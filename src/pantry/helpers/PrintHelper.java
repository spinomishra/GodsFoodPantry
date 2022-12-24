package pantry.helpers;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.awt.*;
import java.awt.print.PrinterJob;

/**
 * Supporting Printing reports from Pantryware
 */
public class PrintHelper {
    public static PrintService PrepareForPrint(Window window){
        final int offset = 50;

        //Window window = SwingUtilities.getWindowAncestor(container);
        GraphicsConfiguration graphicsConfiguration = window == null ? null : window.getGraphicsConfiguration();
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
        PrintRequestAttributeSet attrib = new HashPrintRequestAttributeSet();
        return ServiceUI.printDialog(graphicsConfiguration,
                window.getX() + offset,
                window.getY() + offset,
                printServices, defaultPrintService,
                DocFlavor.SERVICE_FORMATTED.PRINTABLE,  // format
                attrib);
    }

    /**
     * Retrieve the specified Print Service; will return null if not found.
     * @return PrintService object
     */
    public static PrintService findPrintService(String printerName) {

        PrintService service = null;

        // Get array of all print services - sort order NOT GUARANTEED!
        PrintService[] services = PrinterJob.lookupPrintServices();

        // Retrieve specified print service from the array
        for (int index = 0; service == null && index < services.length; index++) {

            if (services[index].getName().equalsIgnoreCase(printerName)) {

                service = services[index];
            }
        }

        // Return the print service
        return service;
    }

    /**
     * Retrieve a PrinterJob instance set with the PrinterService using the printerName.
     *
     * @return
     * @throws Exception IllegalStateException if expected printer is not found.
     */
    public static PrinterJob findPrinterJob(String printerName) throws Exception {

        // Retrieve the Printer Service
        PrintService printService = PrintHelper.findPrintService(printerName);

        // Validate the Printer Service
        if (printService == null) {

            throw new IllegalStateException("Unrecognized Printer Service \"" + printerName + '"');
        }

        // Obtain a Printer Job instance.
        PrinterJob printerJob = PrinterJob.getPrinterJob();

        // Set the Print Service.
        printerJob.setPrintService(printService);

        // Return Print Job
        return printerJob;
    }
}
