package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

import java.io.IOException;
import java.util.Set;

public class EchoWorker implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) {
        context.setMimeType("text/html");

        try {
            context.write("<html><body>");
            context.write("<head><style>table {width: 360px;} " +
                    "table, th, td {border: 1px solid black; border-collapse: collapse}" +
                    "</style></head>");
            context.write("<table>");
            Set<String> paramNames = context.getParameterNames();
            if (paramNames.isEmpty()) {
                context.write("<caption>No parameters provided :( </caption>");
                return;
            }

            context.write("<caption>Table of provided parameters</caption><tbody>");
            context.getParameterNames().forEach((paramName) -> {
                if (context.getParameter(paramName) != null) {
                    try {
                        context.write("<tr>");
                        context.write("<td>" + paramName + "</td><td>" + context.getParameter(paramName) + "</td>");
                        context.write("</tr>");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            context.write("</tbody></table></body></html>");
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

}
