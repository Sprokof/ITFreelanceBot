package telegramBot.util;

import java.util.regex.Pattern;

public class KworkUtil {
    public static final String DEFAULT_ORDER_TITLE = "Новый заказ на Kwork:";
    public static final Pattern LINE_WITH_ID_PATTERN = Pattern.compile("\"userAlreadyWork\":");
    public static final Pattern ORDER_KWORK_PATTERN = Pattern.compile("\"files\":");
}
