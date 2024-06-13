package hr.fer.oprpp2.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Provider class for JPA entity manager factory.
 */
public class JPAEMFProvider {

	/**
	 * Entity manager factory
	 */
	public static EntityManagerFactory emf;
	
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}

}