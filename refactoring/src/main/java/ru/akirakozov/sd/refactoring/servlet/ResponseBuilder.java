package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ResponseBuilder {
    HttpServletResponse response;
    List<String> body;

    public ResponseBuilder(HttpServletResponse response) {
        this.response = response;
        this.body = new ArrayList<>();
    }

    public void print() {
        try {
            PrintWriter writer = response.getWriter();

            writer.println("<html><body>");
            body.forEach(writer::println);
            writer.println("</body></html>");

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void addHeader(int level, String header) {
        body.add("<h" + level + ">" + header + "</h" + level + ">");
    }

    public void addLine(String line, boolean brTag) {
        body.add(brTag ? line + "</br>" : line);
    }

    public void addStatus(String status) {
        body.add(status);
    }

    public void setResponseInfo(String contentType, int status) {
        response.setContentType(contentType);
        response.setStatus(status);
    }
}
