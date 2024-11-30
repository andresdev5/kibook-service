package ec.edu.espe.kibook.util;

public class TextUtils {
    public static String wrapText(String string, int limit, String delimiter) {
        if (string.length() <= limit) {
            return string;
        }

        int spaceIndex = string.substring(0, limit).lastIndexOf(" ");

        if (spaceIndex == -1) {
            return string.substring(0, limit) + delimiter + wrapText(string.substring(limit), limit, delimiter);
        }

        return string.substring(0, spaceIndex) + delimiter + wrapText(string.substring(spaceIndex + 1), limit, delimiter);
    }
}
