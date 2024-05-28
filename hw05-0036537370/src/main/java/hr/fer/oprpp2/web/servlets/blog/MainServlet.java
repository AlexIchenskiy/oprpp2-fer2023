package hr.fer.oprpp2.web.servlets.blog;

import hr.fer.oprpp2.dao.user.UserDAOProvider;
import hr.fer.oprpp2.model.BlogUser;
import hr.fer.oprpp2.util.CryptoUtil;
import hr.fer.oprpp2.util.ValidateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "main", urlPatterns = { "/servleti/main" })
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        if (session.getAttribute("current.user.id") == null) {
            log("Redirecting request from an unauthenticated user.");
        } else {
            log("Redirecting request from an authenticated user "
                    + session.getAttribute("current.user.nick") + ".");
        }

        List<BlogUser> authors = UserDAOProvider.getDAO().getUserList();
        req.setAttribute("authors", authors);

        req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nickname = req.getParameter("nick");
        String password = req.getParameter("pass");

        req.setAttribute("nick", nickname);

        log("Received POST request on main with nickname: " + nickname + ".");

        String err = ValidateUtil.validateLoginForm(nickname, password);
        if (err != null) {
            log("An error \"" + err + "\" occurred in the main POST handler.");
            this.doError(req, resp, err);
            return;
        }

        try {
            BlogUser user = UserDAOProvider.getDAO().getUserByNickname(nickname);

            if (user == null) log("User " + nickname + " does not exist.");

            if (user == null || !CryptoUtil.encrypt(password).equals(user.getPasswordHash())) {
                this.doError(req, resp, "Invalid username or password.");
                return;
            }

            HttpSession session = req.getSession();

            session.setAttribute("current.user.id", user.getId());
            session.setAttribute("current.user.fn", user.getFirstName());
            session.setAttribute("current.user.ln", user.getLastName());
            session.setAttribute("current.user.nick", user.getNick());
        } catch (Exception e) {
            log("An error while parsing user occurred in the main POST handler.");
            log(e.getMessage());
            this.doError(req, resp, "Could not parse user.");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/servleti/main");
    }

    protected void doError(HttpServletRequest req, HttpServletResponse resp, String message) throws IOException, ServletException {
        log("Sending an error \"" + message + "\" to the form from the main POST handler.");
        req.setAttribute("error", message);
        req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
    }

}
