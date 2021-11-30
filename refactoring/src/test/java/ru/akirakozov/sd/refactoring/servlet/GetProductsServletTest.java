package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GetProductsServletTest {
    private static Connection connection;
    private final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    private final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

    @BeforeAll
    static void createConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:test.db");
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
    @DisplayName("GetProductsServlet should correctly get products from database")
    void GetProductsServletPositiveWorkflow() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        new GetProductsServlet().doGet(httpServletRequest, httpServletResponse);
        String response = stringWriter.toString();

        assertThat(response).isEqualToNormalizingNewlines("<html><body>\n" +
                "iphone6\t300</br>\n" +
                "galaxy9\t400</br>\n" +
                "htc10\t200</br>\n" +
                "</body></html>\n");

        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("GetProductsServlet empty database")
    void GetProductsServletNegativeWorkflow() throws SQLException, IOException {
        DatabaseTestUtils.clearDatabase(connection);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        new GetProductsServlet().doGet(httpServletRequest, httpServletResponse);
        String response = stringWriter.toString();

        assertThat(response).isEqualToNormalizingNewlines("<html><body>\n</body></html>\n");

        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }
}