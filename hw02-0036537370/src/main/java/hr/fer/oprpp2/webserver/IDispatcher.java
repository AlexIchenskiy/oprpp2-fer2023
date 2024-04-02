package hr.fer.oprpp2.webserver;

public interface IDispatcher {

    void dispatchRequest(String urlPath) throws Exception;

}
