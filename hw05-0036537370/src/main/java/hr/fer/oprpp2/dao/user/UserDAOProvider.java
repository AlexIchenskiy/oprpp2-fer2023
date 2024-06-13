package hr.fer.oprpp2.dao.user;

/**
 * Class representing a provider for user DAO.
 */
public class UserDAOProvider {

    private static final UserDAO dao = new UserDAOImpl();

    public static UserDAO getDAO() {
        return dao;
    }

}
