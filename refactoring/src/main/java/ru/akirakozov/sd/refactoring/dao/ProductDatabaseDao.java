package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDatabaseDao implements ProductDao {
    private final String url;

    public ProductDatabaseDao(String url) {
        this.url = url;
    }

    @Override
    public void addProduct(Product product) {
        String sql = "INSERT INTO product (name, price) VALUES (?, ?)";

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement statement = c.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setLong(2, product.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getProducts() {
        String sql = "SELECT * FROM product";
        List<Product> products = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement statement = c.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                long price = rs.getLong("price");
                products.add(new Product(name, price));
            }

            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product getProductWithMaxPrice() {
        String sql = "SELECT * FROM product ORDER BY price DESC LIMIT 1";

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement statement = c.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            return rs.next() ? new Product(rs.getString("name"), rs.getLong("price")) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product getProductWithMinPrice() {
        String sql = "SELECT * FROM product ORDER BY price LIMIT 1";

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement statement = c.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            return !rs.next() ? null : new Product(rs.getString("name"), rs.getLong("price"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long getProductsTotalPrice() {
        String sql = "SELECT SUM(price) FROM product";

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement statement = c.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long getProductsCount() {
        String sql = "SELECT COUNT(*) FROM product";

        try (Connection c = DriverManager.getConnection(url);
             PreparedStatement statement = c.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
