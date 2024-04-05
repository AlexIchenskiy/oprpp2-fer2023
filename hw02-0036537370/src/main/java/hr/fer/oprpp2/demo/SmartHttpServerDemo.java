package hr.fer.oprpp2.demo;

import hr.fer.oprpp2.webserver.SmartHttpServer;

/**
 * Demo for a smart http server.
 */
public class SmartHttpServerDemo {

    public static void main(String[] args) {
        new SmartHttpServer("./config/server.properties");
    }

}
