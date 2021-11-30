package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ResponseBuilder formatter = new ResponseBuilder(response);

        try {
            try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");

                while (rs.next()) {
                    String name = rs.getString("name");
                    int price = rs.getInt("price");
                    formatter.addLine(name + "\t" + price, true);
                }

                rs.close();
                stmt.close();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        formatter.setResponseInfo("text/html", SC_OK);
        formatter.print();
    }
}
