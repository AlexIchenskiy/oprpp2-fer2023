package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class representing a hello-name web worker for generating a simple HTML page
 * with query parameters.
 */
public class HelloWorker implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        context.setMimeType("text/html");
        String name = context.getParameter("name");
        try {
            context.write("<html><body>");
            context.write("<h1>Hello!!!</h1>");
            context.write("<p>Now is: "+sdf.format(now)+"</p>");
            if(name==null || name.trim().isEmpty()) {
                context.write("<p>You did not send me your name!</p>");
            } else {
                context.write("<p>Your name has "+name.trim().length()
                        +" letters.</p>");
            }
            context.write("</body></html>");
        } catch(IOException ex) {
            // Log exception to servers log...
            ex.printStackTrace();
        }
    }

}