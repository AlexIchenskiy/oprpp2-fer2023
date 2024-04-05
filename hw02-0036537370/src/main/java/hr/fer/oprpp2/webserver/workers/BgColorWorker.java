package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

/**
 * Class representing a web worker for persistent background color change.
 */
public class BgColorWorker implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) {
        try {
            String color = context.getParameter("bgcolor");
            boolean isErr = color.length() != 6;

            try {
                Long.parseLong(color, 16);
            } catch (Exception e) {
                isErr = true;
            }

            if (!isErr) context.setPersistentParameter("bgcolor", color);

            context.write("<html><body>");
            context.write("<div><h1>Color is " + (isErr ? "not" : "") + " updated</h1></div>" +
                    "<a href=\"/index2.html\">Home</a>");
            context.write("</body></html>");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
