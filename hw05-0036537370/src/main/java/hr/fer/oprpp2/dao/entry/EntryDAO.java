package hr.fer.oprpp2.dao.entry;

import hr.fer.oprpp2.model.BlogComment;
import hr.fer.oprpp2.model.BlogEntry;
import hr.fer.oprpp2.model.BlogUser;

import java.util.List;

public interface EntryDAO {

    List<BlogEntry> getEntriesByUser(BlogUser user);

    BlogEntry getEntryById(Long id);

    boolean createEntry(BlogEntry entry);

    boolean editEntry(BlogEntry entry);

    boolean addComment(BlogComment comment);

}
