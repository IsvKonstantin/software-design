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
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GetProductsServletTest {
    private final ProductDao productDao = mock(ProductDatabaseDao.class);
    private final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    private final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

    @Test
    @DisplayName("GetProductsServlet should correctly get products from database")
    void GetProductsServletPositiveWorkflow() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(httpServletResponse.getWriter()).thenReturn(printWriter);
        when(productDao.getProducts()).thenReturn(Arrays.asList(
                new Product("iphone6", 300L),
                new Product("galaxy9", 400L),
                new Product("htc10", 200L)
        ));

        new GetProductsServlet(productDao).doGet(httpServletRequest, httpServletResponse);
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
    void GetProductsServletNegativeWorkflow() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(httpServletResponse.getWriter()).thenReturn(printWriter);
        when(productDao.getProducts()).thenReturn(Collections.emptyList());

        new GetProductsServlet(productDao).doGet(httpServletRequest, httpServletResponse);
        String response = stringWriter.toString();

        assertThat(response).isEqualToNormalizingNewlines("<html><body>\n</body></html>\n");

        verify(httpServletResponse).setContentType("text/html");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }
}