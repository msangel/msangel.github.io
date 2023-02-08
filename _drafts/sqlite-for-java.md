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
## SQLite as Spring Boot datasource

## JPA with SQLite in spring-boot