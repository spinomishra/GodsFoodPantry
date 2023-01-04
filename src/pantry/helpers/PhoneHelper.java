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

    /**
     * Compare 2 phone number strings
     * @param ph1 Phone number 1
     * @param ph2 Phone number 2
     * @return  0 if Phone  number 1 is same as Phone number 2,
     *          -1 if Phone  number 1 is less than Phone number 2
     *          1 if Phone  number 1 is greater than Phone number 2
     */
    public static int compare(String ph1, String ph2) {
        class OptimizeNumber{
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
