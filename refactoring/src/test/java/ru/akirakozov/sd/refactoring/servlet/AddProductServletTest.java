package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.dao.ProductDatabaseDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AddProductServletTest {
    private final ProductDao productDao = mock(ProductDatabaseDao.class);
    private final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    private final HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

    @Test
    @DisplayName("AddProductServlet should correctly add new product into database")
    void AddProductServletPositiveWorkflow() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(httpServletRequest.getParameter("name")).thenReturn("xiaomi");
        when(httpServletRequest.getParameter("price")).thenReturn("500");
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        new AddProductServlet(productDao).doGet(httpServletRequest, httpServletResponse);
        String response = stringWriter.toString();

        assertThat(response).isEqualToNormalizingNewlines("<html><body>\n" +
                "OK\n" +
                "</body></html>\n");

        verify(httpServletRequest).getParameter("name");
        verify(httpServletRequest).getParameter("price");
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("AddProductServlet wrong / missing parameters in request")
    void AddProductServletNegativeWorkflow() {
        assertThatThrownBy(
                () -> new AddProductServlet(productDao).doGet(httpServletRequest, httpServletResponse)
        ).isInstanceOf(NumberFormatException.class)
         .hasMessage("null");

        verify(httpServletRequest).getParameter("name");
        verify(httpServletRequest).getParameter("price");
        verifyNoMoreInteractions(httpServletRequest, httpServletResponse);
    }
}