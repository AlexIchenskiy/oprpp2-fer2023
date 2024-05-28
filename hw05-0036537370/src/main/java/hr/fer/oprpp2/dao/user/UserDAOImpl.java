package hr.fer.oprpp2.dao.user;

import hr.fer.oprpp2.dao.jpa.JPAEMFProvider;
import hr.fer.oprpp2.dao.jpa.JPAEMProvider;
import hr.fer.oprpp2.model.BlogUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;

public class UserDAOImpl implements UserDAO {

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

    @Override
    public boolean createUser(BlogUser user) {
        EntityManager em = JPAEMProvider.getEntityManager();
        EntityTransaction et = null;

        try {
            et = em.getTransaction();
            et.begin();
            em.persist(user);
            et.commit();

            return true;
        } catch (Exception e) {
            if (et != null && et.isActive()) {
                et.rollback();
            }

            return false;
        }
    }

    @Override
    public List<BlogUser> getUserList() {
        return JPAEMProvider.getEntityManager()
                .createQuery("SELECT u FROM BlogUser u", BlogUser.class).getResultList();
    }

}
