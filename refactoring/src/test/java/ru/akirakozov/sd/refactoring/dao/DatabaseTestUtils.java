package ru.akirakozov.sd.refactoring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseTestUtils {
    public static void createAndFillDatabase(Connection connection) throws SQLException {
        String deleteSql = "DROP TABLE IF EXISTS product";
        String createSql = "CREATE TABLE product" +
                "(id    INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " name  TEXT    NOT NULL," +
                " price INT     NOT NULL)";
        String insertSql = "INSERT INTO product (name, price) VALUES (?, ?)";

        PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
        deleteStatement.executeUpdate();

        PreparedStatement createStatement = connection.prepareStatement(createSql);
        createStatement.executeUpdate();

        PreparedStatement insertStatement = connection.prepareStatement(insertSql);
        insertStatement.setString(1, "iphone6");
        insertStatement.setLong(2, 300L);
        insertStatement.addBatch();

        insertStatement.setString(1, "galaxy9");
        insertStatement.setLong(2, 400L);
        insertStatement.addBatch();

        insertStatement.setString(1, "htc10");
        insertStatement.setLong(2, 200L);
        insertStatement.addBatch();
        insertStatement.executeBatch();

        deleteStatement.close();
        createStatement.close();
        insertStatement.close();
    }

    public static void clearDatabase(Connection connection) throws SQLException {
        String deleteSql = "DROP TABLE IF EXISTS product";
        String createSql = "CREATE TABLE product" +
                "(id    INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " name  TEXT    NOT NULL," +
                " price INT     NOT NULL)";

        PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
        deleteStatement.executeUpdate();

        PreparedStatement createStatement = connection.prepareStatement(createSql);
        createStatement.executeUpdate();

        deleteStatement.close();
        createStatement.close();
    }
}
