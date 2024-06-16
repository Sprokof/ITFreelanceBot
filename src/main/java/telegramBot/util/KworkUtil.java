package telegramBot.util;

import java.util.regex.Pattern;

public class KworkUtil {
    public static final String SPLIT_PATTERN = "\"userAlreadyWork\":null";

    public static final Pattern SKIP_PATTERN = Pattern.compile("\"success\":true");
}
