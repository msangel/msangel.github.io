---
title: SQLite for java
date: 2023-02-08 20:40:00 Z
toc: true
---

## What is SQLite
Best answer can be found on [wiki](https://en.wikipedia.org/wiki/SQLite). In short - this database most frequently used as embeddable or a library and all the data stored in single file.

## SQLite in sample console java application
For connecting java with SQLite all is needed just add the library to classpath and start working with it. The database storage can be either file, either memory. As for now exists only viable java library for working with SQLite: `org.xerial:sqlite-jdbc:+`([take latest here](https://search.maven.org/artifact/org.xerial/sqlite-jdbc)).
Dependency sample:
```xml
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.40.1.0</version>
    </dependency>
```

Sample code:
```java

import java.sql.*;

public class Main {
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
```

Running this will print:
```text
id=1, username=admin, firstName=null, secondName=null
```
Source code together for above here: [sqlite-console](https://github.com/msangel/msangel.github.io/tree/master/samples/sqlite/sqlite-console)
## Connection properties
In general case the connection properties are set by standard sql `PRAGMA` keyword. Even if we can use that via standard jdbc queries mechanism, there also other options available:
- set properties as connection string query parameters
- set programmatically properties of `org.sqlite.SQLiteConfig` object

https://stackoverflow.com/questions/9958382/sqlite-jdbc-pragma-setting
https://stackoverflow.com/questions/24513576/opening-database-connection-with-the-sqlite-open-nomutex-flag-in-java/24536144#24536144
https://github.com/search?l=Java&p=3&q=execute+pragma&type=Code
https://stackoverflow.com/questions/9774923/how-do-you-enforce-foreign-key-constraints-in-sqlite-through-java

## Connection pooling

## SQLite as Spring Boot datasource
https://www.google.com/search?q=spring+boot+sqlite+&sxsrf=AJOqlzXzcn7A1K-wxkxbbiixRGdbndozIg%3A1677071690147&ei=ShX2Y9PPCI78qwG7i7S4Bw&ved=0ahUKEwjTnOrymqn9AhUO_ioKHbsFDXcQ4dUDCA8&uact=5&oq=spring+boot+sqlite+&gs_lcp=Cgxnd3Mtd2l6LXNlcnAQAzIECCMQJzIECCMQJ0oECEEYAFAAWABgqgloAHABeACAAXyIAXySAQMwLjGYAQCgAQHAAQE&sclient=gws-wiz-serp
https://www.baeldung.com/spring-boot-sqlite

### Autoconfiguration

### Connection pool
https://stackoverflow.com/questions/26490967/how-do-i-configure-hikaricp-in-my-spring-boot-app-in-my-application-properties-f
https://stackoverflow.com/questions/9958382/sqlite-jdbc-pragma-setting


### Connection properties passing


## JPA with SQLite in spring-boot
https://stackoverflow.com/questions/67456579/org-sqlite-sqliteexception-sqlite-error-sql-error-or-missing-database-near
https://github.com/bharat0126/springboot-sqlite-app
