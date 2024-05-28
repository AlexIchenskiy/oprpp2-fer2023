package hr.fer.oprpp2.dao.entry;

public class EntryDAOProvider {

    private static final EntryDAO dao = new EntryDAOImpl();

    public static EntryDAO getDAO() {
        return dao;
    }

}
