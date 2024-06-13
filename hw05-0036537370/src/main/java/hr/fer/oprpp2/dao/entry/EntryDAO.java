package hr.fer.oprpp2.dao.entry;

import hr.fer.oprpp2.model.BlogComment;
import hr.fer.oprpp2.model.BlogEntry;
import hr.fer.oprpp2.model.BlogUser;

import java.util.List;

/**
 * Interface describing entry DAO methods.
 */
public interface EntryDAO {

    /**
     * Get user' entries.
     * @param user User whose entries to be retrieved
     * @return List of user' entries
     */
    List<BlogEntry> getEntriesByUser(BlogUser user);

    /**
     * Get a single entry by its ID.
     * @param id Entry ID
     * @return Corresponding entry
     */
    BlogEntry getEntryById(Long id);

    /**
     * Create a single entry.
     * @param entry Entry data
     * @return Boolean representing if operation was successful
     */
    boolean createEntry(BlogEntry entry);

    /**
     * Edit a single entry.
     * @param entry Entry data
     * @return Boolean representing if operation was successful
     */
    boolean editEntry(BlogEntry entry);

    /**
     * Create a single comment.
     * @param comment Comment data
     * @return Boolean representing if operation was successful
     */
    boolean addComment(BlogComment comment);

}
