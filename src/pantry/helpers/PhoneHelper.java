package pantry.helpers;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PhoneHelper represents class that provides helper methods for Phone numbers
 */
public class PhoneHelper {
    /**
     * Check whether a number is valid phone number or not
     *
     * @param str phone number
     * @return true if valid phone number otherwise false
     */
    public static boolean isValidNumber(String str) {
        // The given argument to compile() method
        // is regular expression.
        // With the help of regular expression we
        // can validate a phone number.
        Pattern phonePattern = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
        Matcher match = phonePattern.matcher(str);
        return (match.find() && match.group().equals(str));
    }

    /**
     * Gets MaskFormatter for controls that may contain phone numbers
     *
     * @return MaskFormatter object
     */
    public static MaskFormatter getFormatterMask() {
        MaskFormatter mask = null;
        try {
            mask = new MaskFormatter("(###)###-####");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mask;
    }

    /**
     * Checks if a given phone number is an empty string
     *
     * @param phoneNumber The Phone Number
     * @return true if phone number string is empty, else false
     */
    public static boolean isNullOrEmpty(String phoneNumber) {
        if (phoneNumber != null) {
            String temp = phoneNumber;
            temp = temp.replace("(", StringHelper.Empty);
            temp = temp.replace(")", StringHelper.Empty);
            temp = temp.replace("+", StringHelper.Empty);
            temp = temp.replace("-", StringHelper.Empty);
            temp = temp.replace(" ", StringHelper.Empty);
            temp = temp.replace(".", StringHelper.Empty);

            return temp.trim().isEmpty();
        }
        return true;
    }

    /**
     * Compare 2 phone number strings
     *
     * @param ph1 Phone number 1
     * @param ph2 Phone number 2
     * @return 0 if Phone  number 1 is same as Phone number 2,
     * -1 if Phone  number 1 is less than Phone number 2
     * 1 if Phone  number 1 is greater than Phone number 2
     */
    public static int compare(String ph1, String ph2) {
        class OptimizeNumber {
            String Optimize(String phoneNumber) {
                String actualNumber = phoneNumber;

                actualNumber = actualNumber.replace("(", StringHelper.Empty);
                actualNumber = actualNumber.replace(")", StringHelper.Empty);
                actualNumber = actualNumber.replace("+", StringHelper.Empty);
                actualNumber = actualNumber.replace("-", StringHelper.Empty);
                actualNumber = actualNumber.replace(" ", StringHelper.Empty);
                actualNumber = actualNumber.replace(".", StringHelper.Empty);

                return actualNumber;
            }
        }

        OptimizeNumber o = new OptimizeNumber();
        return (o.Optimize(ph1).compareTo(o.Optimize(ph2)));
    }
}
