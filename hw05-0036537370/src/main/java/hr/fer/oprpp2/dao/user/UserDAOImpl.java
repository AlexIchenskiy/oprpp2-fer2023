package hr.fer.oprpp2.dao.user;

import hr.fer.oprpp2.dao.jpa.JPAEMProvider;
import hr.fer.oprpp2.model.BlogUser;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * Implementation of the user DAO.
 */
public class UserDAOImpl implements UserDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public BlogUser getUserByNickname(String nickname) {
        try {
            return JPAEMProvider.getEntityManager()
                    .createQuery("SELECT u FROM BlogUser u WHERE u.nick=:nick", BlogUser.class)
                    .setParameter("nick", nickname)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createUser(BlogUser user) {
        try {
            JPAEMProvider.getEntityManager().persist(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BlogUser> getUserList() {
        return JPAEMProvider.getEntityManager()
                .createQuery("SELECT u FROM BlogUser u", BlogUser.class).getResultList();
    }

}
