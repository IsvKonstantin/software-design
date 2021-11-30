package ru.akirakozov.sd.refactoring.dao;

import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDatabaseDao implements ProductDao {
    Connection connection;

    public ProductDatabaseDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addProduct(Product product) {
        String sql = "INSERT INTO product (name, price) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            return rs.next() ? new Product(rs.getString("name"), rs.getLong("price")) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product getProductWithMinPrice() {
        String sql = "SELECT * FROM product ORDER BY price LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            return rs.next() ? new Product(rs.getString("name"), rs.getLong("price")) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long getProductsTotalPrice() {
        String sql = "SELECT SUM(price) FROM product";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long getProductsCount() {
        String sql = "SELECT COUNT(*) FROM product";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
