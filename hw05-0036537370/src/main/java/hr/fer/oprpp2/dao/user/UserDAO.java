package hr.fer.oprpp2.dao.user;

import hr.fer.oprpp2.model.BlogUser;

import java.util.List;

/**
 * Interface describing user DAO methods.
 */
public interface UserDAO {

    /**
     * Method for getting a user by nickname.
     * @param nickname User nickname
     * @return Corresponding blog user
     */
    BlogUser getUserByNickname(String nickname);

    /**
     * Method for adding a single user.
     * @param user User data
     * @return Boolean representing if creation was successful
     */
    boolean createUser(BlogUser user);

    /**
     * Method for retrieving all users list.
     * @return All users list
     */
    List<BlogUser> getUserList();

}
