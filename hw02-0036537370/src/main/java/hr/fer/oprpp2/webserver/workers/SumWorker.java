package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

public class SumWorker implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) {
        try {
            int a, b;
            try {
                a = Integer.parseInt(context.getParameter("a"));
            } catch (Exception e) {
                a = 1;
            }

            try {
                b = Integer.parseInt(context.getParameter("b"));
            } catch (Exception e) {
                b = 2;
            }

            context.setTemporaryParameter("varA", Integer.toString(a));
            context.setTemporaryParameter("varB", Integer.toString(b));
            context.setTemporaryParameter("zbroj", Integer.toString(a + b));
            context.setTemporaryParameter("imgName", (a + b) % 2 == 0 ? "images/coffee.png" : "images/heart.png");

            context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
        } catch(Exception ex) {
            // Log exception to servers log...
            ex.printStackTrace();
        }
    }

}
