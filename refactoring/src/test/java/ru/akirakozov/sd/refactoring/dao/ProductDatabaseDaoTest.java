package ru.akirakozov.sd.refactoring.dao;

import org.junit.jupiter.api.*;
import ru.akirakozov.sd.refactoring.model.Product;
import ru.akirakozov.sd.refactoring.servlet.DatabaseTestUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductDatabaseDaoTest {
    private static Connection connection;
    private static ProductDatabaseDao productDao;

    @BeforeAll
    static void createConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        productDao = new ProductDatabaseDao(connection);
    }

    @AfterAll
    static void closeConnection() throws SQLException {
        connection.close();
    }

    @BeforeEach
    void beforeEach() throws SQLException {
        DatabaseTestUtils.createAndFillDatabase(connection);
    }

    @Test
    @DisplayName("getProducts test")
    void getProducts() {
        List<Product> products = productDao.getProducts();
        List<Product> expected = Arrays.asList(
                new Product("iphone6", 300L),
                new Product("galaxy9", 400L),
                new Product("htc10", 200L)
        );

        assertThat(products).isEqualTo(expected);
    }

    @Test
    @DisplayName("getProducts test (empty database)")
    void getProductsEmpty() throws SQLException {
        DatabaseTestUtils.clearDatabase(connection);
        List<Product> products = productDao.getProducts();

        assertThat(products).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("addProduct test")
    void addProduct() throws SQLException {
        DatabaseTestUtils.clearDatabase(connection);
        Product product = new Product("xiaomi", 100L);
        productDao.addProduct(product);

        assertThat(productDao.getProducts()).isEqualTo(Collections.singletonList(product));
    }

    @Test
    @DisplayName("getProductWithMaxPrice test")
    void getProductWithMaxPrice() {
        Product product = productDao.getProductWithMaxPrice();

        assertThat(product).isEqualTo(new Product("galaxy9", 400L));
    }

    @Test
    @DisplayName("getProductWithMaxPrice test (empty database)")
    void getProductWithMaxPriceEmpty() throws SQLException {
        DatabaseTestUtils.clearDatabase(connection);
        Product product = productDao.getProductWithMaxPrice();

        assertThat(product).isNull();
    }

    @Test
    @DisplayName("getProductWithMinPrice test")
    void getProductWithMinPrice() {
        Product product = productDao.getProductWithMinPrice();

        assertThat(product).isEqualTo(new Product("htc10", 200L));
    }

    @Test
    @DisplayName("getProductWithMinPrice test (empty database)")
    void getProductWithMinPriceEmpty() throws SQLException {
        DatabaseTestUtils.clearDatabase(connection);
        Product product = productDao.getProductWithMinPrice();

        assertThat(product).isNull();
    }

    @Test
    @DisplayName("getProductsTotalPrice test")
    void getProductsTotalPrice()  {
        long totalPrice = productDao.getProductsTotalPrice();

        assertThat(totalPrice).isEqualTo(900L);
    }

    @Test
    @DisplayName("getProductsTotalPrice test (empty database)")
    void getProductsTotalPriceEmpty() throws SQLException {
        DatabaseTestUtils.clearDatabase(connection);
        long totalPrice = productDao.getProductsTotalPrice();

        assertThat(totalPrice).isEqualTo(0L);
    }

    @Test
    @DisplayName("getProductsCount test")
    void getProductsCount() {
        long count = productDao.getProductsCount();

        assertThat(count).isEqualTo(3L);
    }

    @Test
    @DisplayName("getProductsCount test (empty database)")
    void getProductsCountEmpty() throws SQLException {
        DatabaseTestUtils.clearDatabase(connection);
        long count = productDao.getProductsCount();

        assertThat(count).isEqualTo(0L);
    }
}