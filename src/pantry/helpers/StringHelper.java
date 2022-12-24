package pantry.helpers;

/**
 * StringHelper provides methods supporting additional helper methods
 */
public class StringHelper {
    /**
     * Is String null or empty?
     * @param str input string
     * @return true if input string is null or empty; otherwise false
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null)
            return true;

        if (str.trim().isEmpty())
            return true;

        return false;
    }
}
