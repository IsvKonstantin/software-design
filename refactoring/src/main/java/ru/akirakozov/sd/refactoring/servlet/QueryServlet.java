package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private final ProductDao productDao;

    public QueryServlet(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ResponseBuilder formatter = new ResponseBuilder(response);
        String command = request.getParameter("command");

        switch (command) {
            case "max": {
                Product product = productDao.getProductWithMaxPrice();
                formatter.addHeader(1, "Product with max price: ");
                if (product != null) {
                    formatter.addLine(product.toString(), true);
                }
                break;
            }
            case "min": {
                Product product = productDao.getProductWithMinPrice();
                formatter.addHeader(1, "Product with min price: ");
                if (product != null) {
                    formatter.addLine(product.toString(), true);
                }
                break;
            }
            case "sum": {
                long price = productDao.getProductsTotalPrice();
                formatter.addLine("Summary price: ", false);
                formatter.addLine(String.valueOf(price), false);
                break;
            }
            case "count": {
                long count = productDao.getProductsCount();
                formatter.addLine("Number of products: ", false);
                formatter.addLine(String.valueOf(count), false);
                break;
            }
            default: {
                formatter.addLine("Unknown command: " + command, false);
            }
        }

        formatter.setResponseInfo("text/html", SC_OK);
        formatter.print();
    }
}
