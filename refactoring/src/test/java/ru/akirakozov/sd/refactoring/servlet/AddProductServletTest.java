package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AddProductServletTest {
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
    @DisplayName("AddProductServlet should correctly add new product into database")
    void AddProductServletPositiveWorkflow() throws IOException, SQLException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        String sql = "SELECT EXISTS(SELECT 1 FROM product WHERE name=? and price=?) AS result";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "xiaomi");
        preparedStatement.setLong(2, 500L);

        when(httpServletRequest.getParameter("name")).thenReturn("xiaomi");
        when(httpServletRequest.getParameter("price")).thenReturn("500");
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        new AddProductServlet().doGet(httpServletRequest, httpServletResponse);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();

        assertThat(stringWriter.toString().contains("OK")).isTrue();
        assertThat(rs.getBoolean("result")).isTrue();

        verify(httpServletRequest).getParameter("name");
        verify(httpServletRequest).getParameter("price");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);

        preparedStatement.close();
    }

    @Test
    @DisplayName("AddProductServlet wrong / missing parameters in request")
    void AddProductServletNegativeWorkflow() {
        assertThatThrownBy(
                () -> new AddProductServlet().doGet(httpServletRequest, httpServletResponse)
        ).isInstanceOf(NumberFormatException.class)
         .hasMessage("null");

        verify(httpServletRequest).getParameter("name");
        verify(httpServletRequest).getParameter("price");
        verifyNoMoreInteractions(httpServletRequest, httpServletResponse);
    }
}