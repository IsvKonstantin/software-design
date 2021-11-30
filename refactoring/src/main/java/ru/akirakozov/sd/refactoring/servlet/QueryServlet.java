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
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ResponseBuilder formatter = new ResponseBuilder(response);
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
                    formatter.addHeader(1, "Product with max price: ");

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
        } else if ("min".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
                    formatter.addHeader(1, "Product with min price: ");

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
        } else if ("sum".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
                    formatter.addLine("Summary price: ", false);

                    if (rs.next()) {
                        formatter.addLine(String.valueOf(rs.getInt(1)), false);
                    }

                    rs.close();
                    stmt.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if ("count".equals(command)) {
            try {
                try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                    Statement stmt = c.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");
                    formatter.addLine("Number of products: ", false);

                    if (rs.next()) {
                        formatter.addLine(String.valueOf(rs.getInt(1)), false);
                    }

                    rs.close();
                    stmt.close();
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            formatter.addLine("Unknown command: " + command, false);
        }

        formatter.setResponseInfo("text/html", SC_OK);
        formatter.print();
    }

}
