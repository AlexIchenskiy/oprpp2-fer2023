package hr.fer.oprpp2.webserver;

public interface IWebWorker {

    public void processRequest(RequestContext context) throws Exception;

}
