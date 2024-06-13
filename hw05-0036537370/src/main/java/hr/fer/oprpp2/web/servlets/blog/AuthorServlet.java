package hr.fer.oprpp2.web.servlets.blog;

import hr.fer.oprpp2.dao.entry.EntryDAOProvider;
import hr.fer.oprpp2.dao.user.UserDAOProvider;
import hr.fer.oprpp2.model.BlogEntry;
import hr.fer.oprpp2.model.BlogUser;
import hr.fer.oprpp2.util.NetworkUtil;
import hr.fer.oprpp2.util.ValidateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Author servlet for handling the main blog entries logic.
 */
@WebServlet(name = "author", urlPatterns = { "/servleti/author/*" })
public class AuthorServlet extends HttpServlet {

    /**
     * Method that handles the blog posts data logic.
     * @param req HTTP Request
     * @param resp HTTP Response
     * @throws ServletException Request processing exception
     * @throws IOException File manipulation exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String path = req.getPathInfo();

        if (path.startsWith("/")) path = path.replaceFirst("/", "");

        String[] args = path.split("/");

        BlogUser user = UserDAOProvider.getDAO().getUserByNickname(args[0]);

        if (user == null) {
            log("Author does not exist on author GET request.");
            NetworkUtil.throwNetworkError(req, resp, "User does not exist.");
            return;
        }

        req.setAttribute("author", args[0]);

        if (args.length == 1) {
            log("Showing posts of author " + args[0] + ".");
            List<BlogEntry> entries = EntryDAOProvider.getDAO().getEntriesByUser(user);

            req.setAttribute("entries", entries);

            req.setAttribute("isAuthor", session.getAttribute("current.user.nick") != null
                    && session.getAttribute("current.user.nick").equals(args[0]));

            req.getRequestDispatcher("/WEB-INF/pages/author.jsp").forward(req, resp);

            return;
        }

        if (args.length == 2 || args.length == 3) {
            if (args[1].equals("new")) {
                log("Redirecting to entry creation.");
                req.getRequestDispatcher("/WEB-INF/pages/new.jsp").forward(req, resp);

                return;
            }

            if (args[1].equals("edit")) {
                log("Redirecting to entry editing.");
                try {
                    req.setAttribute("entry", EntryDAOProvider.getDAO().getEntryById(
                            Long.parseLong(args[2])));
                } catch (Exception e) {
                    NetworkUtil.throwNetworkError(req, resp, "Invalid entry ID.");
                    return;
                }

                req.getRequestDispatcher("/WEB-INF/pages/edit.jsp").forward(req, resp);

                return;
            }

            try {
                log("Redirecting to entry.");
                req.setAttribute("entry", EntryDAOProvider.getDAO().getEntryById(Long.parseLong(args[1])));
                req.setAttribute("isAuthor", session.getAttribute("current.user.nick") != null
                        && session.getAttribute("current.user.nick").equals(args[0]));

                req.getRequestDispatcher("/WEB-INF/pages/entry.jsp").forward(req, resp);

                return;
            } catch (Exception e) {
                log("Could not parse entry arguments.", e);
                NetworkUtil.throwNetworkError(req, resp, "Invalid arguments.");
            }
        }

        NetworkUtil.throwNetworkError(req, resp, "Invalid arguments.");
    }

    /**
     * Method for creating or editing an entry.
     * @param req HTTP Request
     * @param resp HTTP Response
     * @throws ServletException Request processing exception
     * @throws IOException File manipulation exception
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        if (session.getAttribute("current.user.id") == null) {
            NetworkUtil.throwNetworkError(req, resp, "Unauthenticated.");
            return;
        }

        String title = req.getParameter("title");
        String text = req.getParameter("text");

        String path = req.getPathInfo();

        if (path.startsWith("/")) path = path.replaceFirst("/", "");

        String[] args = path.split("/");

        if (args.length != 2 && args.length != 3) {
            NetworkUtil.throwNetworkError(req, resp, "Invalid number of arguments.");
        }

        BlogUser user = UserDAOProvider.getDAO().getUserByNickname(args[0]);

        if (user == null) {
            log("Author does not exist on author POST request.");
            NetworkUtil.throwNetworkError(req, resp, "Author does not exist.");
            return;
        }

        if (args[1].equals("new")) {
            String err = ValidateUtil.validateNewEntry(title, text);
            if (err != null) {
                log("An error \"" + err + "\" occurred in the author POST handler.");
                this.doError(req, resp, err, "new", user.getNick());
                return;
            }

            if (!EntryDAOProvider.getDAO().createEntry(new BlogEntry(
                    new Date(), new Date(), title, text, user
            ))) {
                log("Entry could not be created.");
                this.doError(req, resp, "Could not create entry.", "new", user.getNick());
                return;
            }

            resp.sendRedirect(req.getContextPath() + "/servleti/author/" + user.getNick());
            return;
        }

        if (args[1].equals("edit")) {
            try {
                long id = Long.parseLong(args[2]);

                BlogEntry entry = EntryDAOProvider.getDAO().getEntryById(id);
                req.setAttribute("entry", entry);

                String err = ValidateUtil.validateNewEntry(title, text);
                if (err != null) {
                    log("An error \"" + err + "\" occurred in the author POST handler.");
                    this.doError(req, resp, err, "edit", user.getNick());
                    return;
                }

                entry.setTitle(title);
                entry.setText(text);

                if (!EntryDAOProvider.getDAO().editEntry(entry)) {
                    log("Entry could not be edited.");
                    this.doError(req, resp, "Could not edit entry.", "edit", user.getNick());
                    return;
                }

                resp.sendRedirect(req.getContextPath() + "/servleti/author/" + user.getNick() + "/" + id);
            } catch (Exception e) {
                NetworkUtil.throwNetworkError(req, resp, "Could not edit post.", e);
            }
            return;
        }

        NetworkUtil.throwNetworkError(req, resp, "Invalid arguments.");
    }

    /**
     * Method that redirects users to the error page.
     * @param req HTTP Request
     * @param resp HTTP Response
     * @throws ServletException Request processing exception
     * @throws IOException File manipulation exception
     */
    protected void doError(HttpServletRequest req, HttpServletResponse resp, String message,
                           String option, String author)
            throws IOException, ServletException {
        log("Sending an error \"" + message + "\" to the form from the author POST handler.");

        req.setAttribute("error", message);
        req.setAttribute("author", author);

        req.getRequestDispatcher("/WEB-INF/pages/" + option + ".jsp").forward(req, resp);
    }

}
