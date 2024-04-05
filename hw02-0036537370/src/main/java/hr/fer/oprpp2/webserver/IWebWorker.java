package hr.fer.oprpp2.webserver;

/**
 * Interface providing a method for web worker processing request with a provided context.
 */
public interface IWebWorker {

    /**
     * Method for processing an HTTP request by a web worker.
     * @param context HTTP request context
     * @throws Exception Error while processing a request
     */
    void processRequest(RequestContext context) throws Exception;

}
