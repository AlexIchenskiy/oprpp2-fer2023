package hr.fer.oprpp2.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Server context listener.
 */
@WebListener
public class ServerObserver implements ServletContextListener {

    /**
     * Action on server context initialization (setting server starting time attribute)
     * @param servletContextEvent Context event
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute("start", System.currentTimeMillis());
    }

    /**
     * Action on server context destroy
     * @param servletContextEvent Context event
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

}
