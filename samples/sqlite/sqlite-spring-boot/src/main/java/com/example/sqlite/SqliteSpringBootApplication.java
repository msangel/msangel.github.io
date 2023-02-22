package com.example.sqlite;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;

@SpringBootApplication
public class SqliteSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SqliteSpringBootApplication.class, args);
    }

    @Data
    public static class TodoItem {
        private int id;
        private String message;
    }

    @Controller
    @Slf4j
    public static class HelloController {

        @Autowired
        private NamedParameterJdbcTemplate template;

        @Autowired
        private DataSource dataSource;

        @PostConstruct
        public void init() throws SQLException {
            ResultSet rs = dataSource.getConnection().createStatement().executeQuery("PRAGMA foreign_keys");
            if (rs.next()) {
                int foreignKeys = rs.getInt(1);
                log.info("foreignKeys: {}", foreignKeys);
            }
//            template.update("create table if not exists todos( id int );", Collections.emptyMap());
        }

        @GetMapping(value = "/hello")
        public String hello(Model model) {
//            template.queryForList()
//            log.info("jdbc tables: {}", template.queryForList("SELECT name FROM sqlite_master WHERE type='table'", Collections.emptyMap()));
//            String msg = "Hello there!";
//            model.addAttribute("message", msg);

            return "hello";
        }
    }
}
