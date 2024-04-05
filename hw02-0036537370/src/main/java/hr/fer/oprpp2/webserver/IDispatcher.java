package hr.fer.oprpp2.webserver;

/**
 * Interface providing a method for HTTP requests dispatching.
 */
public interface IDispatcher {

    void dispatchRequest(String urlPath) throws Exception;

}
