package telegramBot.repository.helper.jdbc;

import telegramBot.util.PropertiesUtil;

import java.rmi.AccessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {

    private static final String URL_KEY = "spring.datasource.url";
    private static final String USERNAME_KEY = "spring.datasource.username";
    private static final String PASSWORD_KEY = "spring.datasource.password";

    private ConnectionManager () throws AccessException {
        throw new AccessException("Constructor can't be created");
    }

    static {
        loadDriver();
    }

    // load jdbc driver in first class load
    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    // method for get db connection
    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY));
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
