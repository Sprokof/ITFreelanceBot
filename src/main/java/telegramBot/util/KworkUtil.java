package telegramBot.util;

import java.util.regex.Pattern;

public class KworkUtil {
    public static final String SPLIT_PATTERN = "\"userAlreadyWork\":null";

    public static final Pattern SKIP_PATTERN = Pattern.compile("\"success\":true");

    public static String convertToCyrillic(String input) {
        Pattern unicodeCyrillicPattern = Pattern.compile("4(\\d{2}|(\\d\\w))");
        String splitPattern = "(\\\\u0|\\\"u0)";
        String prefix = "0";
        String[] inputItems = input.split(splitPattern);
        StringBuilder result = new StringBuilder(inputItems[0]);
        for (int i = 1; i < inputItems.length; i ++) {
            String item = inputItems[i];
            if (unicodeCyrillicPattern.matcher(item).find()) {
                String hex = (prefix + item.substring(0, 3));
                String trimPart = "";
                if (item.length() > 3) {
                    trimPart = item.substring(3);
                }
                int asciiCode = Integer.parseInt(hex, 16);
                char asciiChar = (char) asciiCode;
                result.append(asciiChar).append(trimPart);
            } else {
                result.append(item);
            }
        }
        return result.toString();
    }
}
