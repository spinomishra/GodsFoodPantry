package pantry.helpers;

/**
 * StringHelper provides methods supporting additional helper methods
 */
public class StringHelper {
    /**
     * Empty string literal
     */
    public static final String Empty = "";

    /**
     * Is String null or empty?
     *
     * @param str input string
     * @return true if input string is null or empty; otherwise false
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null)
            return true;

        return str.trim().isEmpty();
    }
}
