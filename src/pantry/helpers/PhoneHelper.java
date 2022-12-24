package pantry.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PhoneHelper represents class that provides helper methods for Phone numbers
 */
public class PhoneHelper {
    /**
     * Check whether a number is valid phone number or not
     * @param str phone number
     * @return true if valid phone number otherwise false
     */
    public static boolean isValidNumber(String str){
        // The given argument to compile() method
        // is regular expression.
        // With the help of regular expression we
        // can validate a phone number.
        Pattern phonePattern = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
        Matcher match = phonePattern.matcher(str);
        return (match.find() && match.group().equals(str));
    }
}
