package hr.fer.oprpp2.dao.user;

import hr.fer.oprpp2.model.BlogUser;

import java.util.List;

public interface UserDAO {

    BlogUser getUserByNickname(String nickname);

    boolean createUser(BlogUser user);

    List<BlogUser> getUserList();

}
