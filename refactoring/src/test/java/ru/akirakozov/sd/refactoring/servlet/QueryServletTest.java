package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.dao.ProductDatabaseDao;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class QueryServletTest {
    private static final ProductDao productDao = mock(ProductDatabaseDao.class);
    private final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    private final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

    @Test
    @DisplayName("QueryServlet max query positive test")
    public void queryServletMaxTest() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(httpServletRequest.getParameter("command")).thenReturn("max");
        when(httpServletResponse.getWriter()).thenReturn(printWriter);
        when(productDao.getProductWithMaxPrice()).thenReturn(new Product("galaxy9", 400L));

        new QueryServlet(productDao).doGet(httpServletRequest, httpServletResponse);
        String response = stringWriter.toString();

        assertThat(response).isEqualToNormalizingNewlines("<html><body>\n" +
                "<h1>Product with max price: </h1>\n" +
                "galaxy9\t400</br>\n" +
                "</body></html>\n");

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("QueryServlet min query positive test")
    public void queryServletMinTest() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(httpServletRequest.getParameter("command")).thenReturn("min");
        when(httpServletResponse.getWriter()).thenReturn(printWriter);
        when(productDao.getProductWithMinPrice()).thenReturn(new Product("htc10", 200L));

        new QueryServlet(productDao).doGet(httpServletRequest, httpServletResponse);
        String response = stringWriter.toString();

        assertThat(response).isEqualToNormalizingNewlines("<html><body>\n" +
                "<h1>Product with min price: </h1>\n" +
                "htc10\t200</br>\n" +
                "</body></html>\n");

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("QueryServlet sum query positive test")
    public void queryServletSumTest() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(httpServletRequest.getParameter("command")).thenReturn("sum");
        when(httpServletResponse.getWriter()).thenReturn(printWriter);
        when(productDao.getProductsTotalPrice()).thenReturn(900L);

        new QueryServlet(productDao).doGet(httpServletRequest, httpServletResponse);
        String response = stringWriter.toString();

        assertThat(response).isEqualToNormalizingNewlines("<html><body>\n" +
                "Summary price: \n" +
                "900\n" +
                "</body></html>\n");

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("QueryServlet count query positive test")
    public void queryServletCountTest() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(httpServletRequest.getParameter("command")).thenReturn("count");
        when(httpServletResponse.getWriter()).thenReturn(printWriter);
        when(productDao.getProductsCount()).thenReturn(3L);

        new QueryServlet(productDao).doGet(httpServletRequest, httpServletResponse);
        String response = stringWriter.toString();

        assertThat(response).isEqualToNormalizingNewlines("<html><body>\n" +
                "Number of products: \n" +
                "3\n" +
                "</body></html>\n");

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("QueryServlet unknown query test")
    public void queryServletUnknownTest() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(httpServletRequest.getParameter("command")).thenReturn("unknown");
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        new QueryServlet(productDao).doGet(httpServletRequest, httpServletResponse);
        String response = stringWriter.toString();

        assertThat(response).isEqualToNormalizingNewlines("<html><body>\n" +
                "Unknown command: unknown\n" +
                "</body></html>\n");

        verify(httpServletRequest).getParameter("command");
        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }
}