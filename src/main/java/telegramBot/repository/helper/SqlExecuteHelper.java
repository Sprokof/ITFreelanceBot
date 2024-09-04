package telegramBot.repository.helper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import telegramBot.repository.helper.jdbc.ConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

// class for executing init tables script

@Component
public final class SqlExecuteHelper implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        executeInitDatabase();
    }
    private static final String DATABASE_ALREADY_INIT_QUERY = "SELECT EXISTS (" +
            "   SELECT FROM information_schema.tables" +
            "   WHERE  table_schema = 'public'" +
            "   AND    table_name   = 'exchange'" +
            "   );";


    //executing script for init db
    private void executeInitDatabase() {
        try(Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement(DATABASE_ALREADY_INIT_QUERY);
            ResultSet resultSet = statement.executeQuery();
            // init if database already not
            if (resultSet.next()) {
               boolean init = resultSet.getBoolean(1);
               if (!init) {
                   String sql = loadSql();
                   connection.prepareStatement(sql).execute();
               }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // loading sql file content to String
    private String loadSql() {
        String sql = "";
        try(InputStream inputStream = SqlExecuteHelper.class.getClassLoader()
                .getResourceAsStream("initDB.sql")) {
            if (inputStream != null) {
                byte[] bytes = inputStream.readAllBytes();
                sql = new String(bytes, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sql;
    }
}


