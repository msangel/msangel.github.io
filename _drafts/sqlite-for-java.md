---
title: SQLite for java
date: 2023-02-08 20:40:00 Z
toc: true
---

## What is SQLite
Best answer can be found on [wiki](https://en.wikipedia.org/wiki/SQLite). In short - this database most frequently used as embeddable or a library and all the data stored in single file.

## SQLite in sample console java application
For connecting java with SQLite all is needed just add the library to classpath and start working with it. The database storage can be either file, either memory. As for now exists only viable java library for working with SQLite: `org.xerial:sqlite-jdbc:+`([latest here](https://search.maven.org/artifact/org.xerial/sqlite-jdbc)).
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
Generally two ways: 1) via `PRAGMA` ([list of all known](https://www.sqlite.org/pragma.html)); 2) via `org.sqlite.SQLiteConfig` ([source](https://github.com/xerial/sqlite-jdbc/blob/master/src/main/java/org/sqlite/SQLiteConfig.java))
### Via PRAGMA on connection
```java
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
        log.info("fkPragma: [{}]", fkPragma); // fkPragma: [1]
    }
}
```

### As properties for connection
```java
Class.forName("org.sqlite.JDBC");
SQLiteConfig config = new SQLiteConfig();
config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
config.enforceForeignKeys(true);
config.setJournalMode(SQLiteConfig.JournalMode.TRUNCATE);

try (Connection con = DriverManager.getConnection("jdbc:sqlite::memory:", config.toProperties())) {
    try (Statement stmt = con.createStatement()) {
        ResultSet rs = stmt.executeQuery("PRAGMA foreign_keys");
        String fkPragma = rs.getString(1);
        log.info("fkPragma: {}", fkPragma); // fkPragma: [1]
    }
}
```
### As properties for Datasource/Connection pool
```java
SQLiteConfig config = new SQLiteConfig();
config.setSynchronous(SQLiteConfig.SynchronousMode.OFF);
config.enforceForeignKeys(true);
config.setJournalMode(SQLiteConfig.JournalMode.TRUNCATE);

// can be SQLiteConnectionPoolDataSource if should be recycled
SQLiteDataSource ds = new SQLiteDataSource(config);
ds.setUrl("jdbc:sqlite::memory:");

try (Connection con = ds.getConnection()) {
    try (Statement stmt = con.createStatement()) {
        ResultSet rs = stmt.executeQuery("PRAGMA foreign_keys");
        String fkPragma = rs.getString(1);
        log.info("fkPragma: {}", fkPragma);
    }
}
```

## Concurrency in SQLite
So we have two modes of SQLite: inmemory and file-backed. Concurrency and options to tune for these modes are very different due to their nature. SQLite is a mature software that [meet ACID requirements](https://www.sqlite.org/transactional.html) and so this also put some limitations/requirements on how the software works.

### In memory mode
TBD
https://www.sqlite.org/inmemorydb.html
https://stackoverflow.com/questions/3267077/can-i-achieve-scalable-multi-threaded-access-to-an-in-memory-sqlite-database
https://stackoverflow.com/questions/2566904/can-in-memory-sqlite-databases-scale-with-concurrency

### File-backed defaults
In short: it is "Serialized". 

Reading from the same database [is concurrent](https://www.sqlite.org/faq.html#q5) no matter whenever reader are different threads or processes. But at same time only one process/thread can write to database, locking db for that time. So other will get `java.sql.SQLException: [SQLITE_BUSY]  The database file is locked (database is locked)` if they will access db at exact writing time. Let's simulate that! [Code](https://github.com/msangel/msangel.github.io/tree/master/samples/sqlite/sqlite-concurrent-tester). This sample will be used further for testing on how different options affect concurrency.


https://stackoverflow.com/questions/2493331/how-can-i-avoid-concurrency-problems-when-using-sqlite-on-android/2493839#2493839
https://stackoverflow.com/questions/13891006/getting-sqlite-busy-database-file-is-locked-with-select-statements
https://www.google.com/search?q=sqlite_busy+java
https://www.sqlite.org/faq.html#q5
https://www.sqlite.org/faq.html#q6
https://www.sqlite.org/threadsafe.html
https://martinfowler.com/articles/patterns-of-distributed-systems/wal.html
https://www.sqlite.org/wal.html
https://www.sqlite.org/cgi/src/doc/wal2/doc/wal2.md
https://stackoverflow.com/questions/4060772/sqlite-concurrent-access
https://stackoverflow.com/questions/35804884/sqlite-concurrent-writing-performance
https://www.sqlite.org/cgi/src/doc/begin-concurrent/doc/begin_concurrent.md

Physically behind database there's single file, and existing design of disc operations says that you can write only once at a time. So SQLite relay on that limitations too, providing write permissions to single thread at a time, and read concurrency can


###
## Connection pooling
Connection pooling is about two things that usually came together:
1) connection reusing, when instead of closing one it simply returns to pool
2) having a pool of multiple connections, so each new consumer may take one

So this question is hardly depends on concurrency. But technically 

From code point of view these options are available. 

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
