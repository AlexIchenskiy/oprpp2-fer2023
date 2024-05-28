package hr.fer.oprpp2.dao.entry;

import hr.fer.oprpp2.dao.jpa.JPAEMProvider;
import hr.fer.oprpp2.model.BlogComment;
import hr.fer.oprpp2.model.BlogEntry;
import hr.fer.oprpp2.model.BlogUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Date;
import java.util.List;

public class EntryDAOImpl implements EntryDAO {

    @Override
    public List<BlogEntry> getEntriesByUser(BlogUser user) {
        return JPAEMProvider.getEntityManager()
                .createQuery("SELECT e FROM BlogEntry e WHERE e.creator = :creator", BlogEntry.class)
                .setParameter("creator", user)
                .getResultList();
    }

    @Override
    public BlogEntry getEntryById(Long id) {
        return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
    }

    @Override
    public boolean createEntry(BlogEntry entry) {
        try {
            JPAEMProvider.getEntityManager().persist(entry);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean editEntry(BlogEntry entry) {
        try {
            entry.setLastModifiedAt(new Date());
            JPAEMProvider.getEntityManager().merge(entry);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean addComment(BlogComment comment) {
        try {
            JPAEMProvider.getEntityManager().persist(comment);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
