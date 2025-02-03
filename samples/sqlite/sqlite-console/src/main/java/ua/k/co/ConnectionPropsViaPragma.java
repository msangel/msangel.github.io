package ua.k.co;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class ConnectionPropsViaPragma {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        try (Connection con = DriverManager.getConnection("jdbc:sqlite::memory:")) {
            try (Statement stmt = con.createStatement()) {
                stmt.execute("PRAGMA synchronous = OFF");
                stmt.execute("PRAGMA foreign_keys = ON");
                stmt.execute("PRAGMA journal_mode = TRUNCATE");
            }

            try (Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery("PRAGMA foreign_keys");
                String fkPragma = rs.getString(1);
                log.info("fkPragma: {}", fkPragma);
            }
        }
    }
}
