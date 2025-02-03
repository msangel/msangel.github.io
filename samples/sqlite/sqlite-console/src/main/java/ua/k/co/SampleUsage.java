package ua.k.co;

import java.sql.*;

public class SampleUsage {
    public static void main(String[] args) throws Exception {
        // load driver
        Class.forName("org.sqlite.JDBC");

        // since both Connection and Statement are AutoCloseable
        // closing these objects will handle try-with-resource
        try (
                // connect to inmemory db:
                 Connection con = DriverManager.getConnection("jdbc:sqlite::memory:");

                // or to file, using relative file location
                // Connection con = DriverManager.getConnection("jdbc:sqlite:file.sqlite");

                // or to file, using absolute file location
                // Connection con = DriverManager.getConnection("jdbc:sqlite:" + Paths.get("file.sqlite").toAbsolutePath());

                Statement stat = con.createStatement();
        ) {
            stat.executeUpdate("drop table if exists users");
            stat.executeUpdate("create table users(id integer primary key autoincrement,"
                    + "username varchar(30) not null , " +
                    "firstName varchar(30), " +
                    "secondName varchar(30)"
                    + ");");
            stat.executeUpdate("insert into users(username) values ('admin')");


            try(ResultSet rs = stat.executeQuery("select * from users")) {
                // just print data
                ResultSetMetaData metadata = rs.getMetaData();
                int numberOfColumns = metadata.getColumnCount();
                while (rs.next()) {
                    int i = 1;
                    while(i <= numberOfColumns) {
                        System.out.print(metadata.getColumnName(i) + "=" + rs.getString(i));
                        if (i < numberOfColumns){
                            System.out.print(", ");
                        }
                        i++;
                    }
                    System.out.println();
                }
            }
        }
    }
}