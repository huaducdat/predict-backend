package com.ducdathua.prediction_app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Configuration
public class DatabaseInit {
    @Bean
    CommandLineRunner initDataBase() {
        return args -> {
            try {
                String url = "jdbc:mysql://localhost:3306";
                String username = "root";
                String password = "123456";

                Connection conn = DriverManager.getConnection(url, username, password);
                Statement stmt = conn.createStatement();

                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS prediction_db");
                System.out.println("DB created or already exists!");
                stmt.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
