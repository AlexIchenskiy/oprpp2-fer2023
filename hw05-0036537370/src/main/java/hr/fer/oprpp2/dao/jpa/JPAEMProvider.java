package hr.fer.oprpp2.dao.jpa;

import hr.fer.oprpp2.dao.DAOException;

import javax.persistence.EntityManager;

/**
 * Provider class for JPA entity manager.
 */
public class JPAEMProvider {

	/**
	 * Available entity managers.
	 */
	private static ThreadLocal<EntityManager> locals = new ThreadLocal<>();

	/**
	 * Method for retrieving or creating a single entity manager.
	 * @return Entity manager
	 */
	public static EntityManager getEntityManager() {
		EntityManager em = locals.get();
		if(em == null) {
			em = JPAEMFProvider.getEmf().createEntityManager();
			em.getTransaction().begin();
			locals.set(em);
		}
		return em;
	}

	/**
	 * Closing method.
	 * @throws DAOException DAO exception
	 */
	public static void close() throws DAOException {
		EntityManager em = locals.get();
		if(em == null) {
			return;
		}
		DAOException dex = null;
		try {
			em.getTransaction().commit();
		} catch(Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			em.close();
		} catch(Exception ex) {
			if(dex != null) { // popraviti!
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if(dex != null) throw dex;
	}
	
}