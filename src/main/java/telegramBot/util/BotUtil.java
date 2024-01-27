package telegramBot.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class BotUtil {
    public static int DAY_MILLISECONDS = 86400000;
    public static int EXECUTE_NOTICE_TIMEOUT = 420000;
    public static String botToken = getProperty("bot.token");
    public static String botUsername = getProperty("bot.username");



    private static Properties getApplicationProperties() throws IOException {
        String rootPath = Objects.requireNonNull(Thread.currentThread()
                .getContextClassLoader().getResource("")).getPath();
        String appConfigPath = rootPath + "application.properties";

        Properties properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));
        return properties;
    }

    private static String getProperty(String key) {
        String property = null;
        try {
            property = getApplicationProperties().getProperty(key);
        } catch (IOException e) {
            System.out.println(e.getCause().getMessage());
        }
        return property;
    }
}
