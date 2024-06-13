package hr.fer.oprpp2.web.init;

import hr.fer.oprpp2.dao.jpa.JPAEMFProvider;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * The initialization class for the web server.
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("baza.podataka.za.blog");
        sce.getServletContext().setAttribute("my.application.emf", emf);
        JPAEMFProvider.setEmf(emf);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAEMFProvider.setEmf(null);
        EntityManagerFactory emf = (EntityManagerFactory)sce.getServletContext().getAttribute("my.application.emf");
        if(emf != null) {
            emf.close();
        }
    }

}
