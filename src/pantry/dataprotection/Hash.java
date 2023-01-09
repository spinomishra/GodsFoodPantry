package pantry.dataprotection;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Hash class implements various one way hash methods
 */
public class Hash {
    /**
     * Hash input array of characters using SHA256 algorithm
     * @param chars array of characters
     * @return The Hash as string
     */
    public static String Sha2Hash(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data

        // MessageDigest instance for hashing using SHA256
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            var digest = md.digest(bytes);

            //Converting the byte array in to HexString format
            StringBuffer hexString = new StringBuffer();
            for (int i = 0;i<digest.length;i++) {
                hexString.append(Integer.toHexString(0xFF & digest[i]));
            }

            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown : " + e);
        }
        catch (NullPointerException e) {
            System.out.println("Exception thrown : " + e);
        }
        catch (Exception e) {
            System.out.println("Exception thrown : " + e);
        }

        return "";
    }
}
