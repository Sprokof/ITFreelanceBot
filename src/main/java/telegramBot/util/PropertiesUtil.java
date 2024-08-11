package telegramBot.util;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.AccessException;
import java.util.Properties;

public class PropertiesUtil {

    private PropertiesUtil () throws AccessException {
        throw new AccessException("Constructor can't be created");
    }

    static {
        init();
    }
    static Properties PROPERTIES;

    private static void init() {
        try (InputStream is = PropertiesUtil
                .class.getClassLoader()
                .getResourceAsStream("application.properties")){
            PROPERTIES.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}