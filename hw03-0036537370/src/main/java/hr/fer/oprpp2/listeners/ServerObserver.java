package hr.fer.oprpp2.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServerObserver implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute("start", System.currentTimeMillis());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

}
