package hr.fer.oprpp2.dao.user;

public class UserDAOProvider {

    private static final UserDAO dao = new UserDAOImpl();

    public static UserDAO getDAO() {
        return dao;
    }

}
