package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

public class HomeWorker implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) {
        try {
            String pparam = context.getPersistentParameter("bgcolor");
            if (pparam == null) pparam = "7F7F7F";
            context.setTemporaryParameter("background", pparam);
            context.getDispatcher().dispatchRequest("/private/pages/home.smscr");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}