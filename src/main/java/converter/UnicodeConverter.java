package converter;

import java.util.HashMap;
import java.util.Map;


public class UnicodeConverter {
    public static String convertCyrillic(String input) {
        String unescape = "\\";
        String escape = "n";
        char[] inputChars = input.toCharArray();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < inputChars.length; i ++) {
            String currentChar = String.valueOf(inputChars[i]);
            String nextChar = (i + 1 >= inputChars.length) ? "" : String.valueOf(inputChars[i + 1]);
            if (currentChar.equals(unescape) && !nextChar.equals(escape)) {
                int startIndex = i + 2;
                int hexLength = 4;
                String hex = input.substring(startIndex, startIndex + hexLength);
                int decimalValue = Integer.valueOf(hex, 16);
                result.append((char) decimalValue);
            }
            result.append(currentChar);
        }
        return result.toString().replaceAll("\\\\u04\\w{2}", "");
    }
}