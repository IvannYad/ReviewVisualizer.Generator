package com.example.reviewvisualizer.config;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfig {
  @Primary
  @Bean
  public DataSource sqlServerDataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl("jdbc:sqlserver://localhost:1433;databaseName=ReviewVisualizerDb;trustServerCertificate=true;encrypt=true;");
    dataSource.setUsername("sa2");
    dataSource.setPassword("Password1");
    dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    return dataSource;
  }

  @Bean(name = "sqliteDataSource")
  public DataSource sqliteDataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl("jdbc:sqlite:C:/Users/User/Desktop/IT/ReviewVisualizer/reviews.db");
    dataSource.setDriverClassName("org.sqlite.JDBC");
    return dataSource;
  }

  @Bean(name = "sqliteJdbcTemplate")
  public JdbcTemplate sqliteJdbcTemplate(@Qualifier("sqliteDataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}