package ua.k.co;

import lombok.extern.slf4j.Slf4j;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class DataSourcePropsViaProps {
    public static void main(String[] args) throws SQLException {
        SQLiteConfig config = new SQLiteConfig();
        config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
        config.enforceForeignKeys(true);
        config.setJournalMode(SQLiteConfig.JournalMode.TRUNCATE);

        SQLiteDataSource ds = new SQLiteDataSource(config);
        // or polled one (is not closed on "close" but just remain open/ready to reuse)
        // SQLiteConnectionPoolDataSource ds = new SQLiteConnectionPoolDataSource(config);
        ds.setUrl("jdbc:sqlite::memory:");

        try (Connection con = ds.getConnection()) {
            try (Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery("PRAGMA foreign_keys");
                String fkPragma = rs.getString(1);
                log.info("fkPragma: {}", fkPragma);
            }
        }
    }
}
