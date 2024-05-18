package telegramBot.util;

import java.io.*;
import java.rmi.AccessException;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class BotUtil {

    private BotUtil () throws AccessException {
        throw new AccessException("Constructor can't be created");
    }

    public static int DAY_MILLISECONDS = 86400000;

    public static int EXECUTE_NOTICE_TIMEOUT = 600000;

    public static String BOT_TOKEN = getProperty("bot.token");

    public static String BOT_USERNAME = getProperty("bot.username");

    public static String ADMIN_CHAT_ID = getProperty("admin.chat.id");

    public static final String UNKNOWN_COMMAND = "Unknown input /info";

    public static final int MAX_ORDERS_COUNT = 25;

    public static final int SIZE = Integer.parseInt(getProperty("size"));

    private static Properties getApplicationProperties() throws IOException {
        try (InputStream is = BotUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties properties = new Properties();
            properties.load(is);
            return properties;
        }
    }

    public static String getProperty(String key) {
        String property = null;
        try {
            property = getApplicationProperties().getProperty(key);
        } catch (IOException e) {
            System.out.println(e.getCause().getMessage());
        }
        return property;
    }


}
