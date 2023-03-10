package ua.k.co;

import lombok.extern.slf4j.Slf4j;
import org.sqlite.SQLiteConfig;

import java.sql.*;

@Slf4j
public class ConnectionPropsViaProps {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        SQLiteConfig config = new SQLiteConfig();
        config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
        config.enforceForeignKeys(false);
        config.setJournalMode(SQLiteConfig.JournalMode.TRUNCATE);
        try (Connection con = DriverManager.getConnection("jdbc:sqlite::memory:", config.toProperties())) {
            try (Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery("PRAGMA foreign_keys");
                String fkPragma = rs.getString(1);
                log.info("fkPragma: {}", fkPragma);
            }
        }
    }
}
