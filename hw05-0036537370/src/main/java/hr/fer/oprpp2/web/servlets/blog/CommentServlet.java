package hr.fer.oprpp2.web.servlets.blog;

import hr.fer.oprpp2.dao.entry.EntryDAOProvider;
import hr.fer.oprpp2.dao.user.UserDAOProvider;
import hr.fer.oprpp2.model.BlogComment;
import hr.fer.oprpp2.util.NetworkUtil;
import hr.fer.oprpp2.util.ValidateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "comments", urlPatterns = { "/servleti/comments" })
public class CommentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            log("Trying to add comment.");
            String message = req.getParameter("message");

            String author = req.getParameter("author");
            long entryId = Long.parseLong(req.getParameter("id"));

            String err = ValidateUtil.validateComment(message);
            if (err != null) {
                NetworkUtil.throwNetworkError(req, resp, err);
                return;
            }

            if (!EntryDAOProvider.getDAO().addComment(new BlogComment(
                    EntryDAOProvider.getDAO().getEntryById(entryId),
                    (author != null && !author.isEmpty()) ?
                            UserDAOProvider.getDAO().getUserByNickname(author).getEmail() : "Anonymous",
                    message,
                    new Date()
            ))) {
                log("Could not add comment.");
                NetworkUtil.throwNetworkError(req, resp, "Could not add comment.");
                return;
            }

            log("Redirecting back to the entry page.");
            resp.sendRedirect(req.getContextPath() + "/servleti/author/" + req.getParameter("entryAuthor")
                    + "/" + entryId);
        } catch (Exception e) {
            log("Could not leave comment.", e);
            NetworkUtil.throwNetworkError(req, resp, "Could not leave comment.");
        }
    }

}
