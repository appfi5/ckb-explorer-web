package com.ckb.explorer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigrationConfig implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseMigrationConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if column exists and needs type change

    }
}