package hr.fer.oprpp2.dao.entry;

/**
 * Provider class for entry DAO.
 */
public class EntryDAOProvider {

    private static final EntryDAO dao = new EntryDAOImpl();

    public static EntryDAO getDAO() {
        return dao;
    }

}
