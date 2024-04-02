package hr.fer.oprpp2.demo;

import hr.fer.oprpp2.webserver.SmartHttpServer;

public class SmartHttpServerDemo {

    public static void main(String[] args) {
        new SmartHttpServer("./config/server.properties");
    }

}
