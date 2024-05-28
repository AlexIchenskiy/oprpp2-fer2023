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

@WebServlet(name = "register", urlPatterns = { "/servleti/register" })
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstname = req.getParameter("firstname");
        String lastname = req.getParameter("lastname");
        String email = req.getParameter("email");
        String nickname = req.getParameter("nick");
        String password = req.getParameter("pass");

        req.setAttribute("firstname", firstname);
        req.setAttribute("lastname", lastname);
        req.setAttribute("email", email);
        req.setAttribute("nick", nickname);

        log("Received POST request on register with nickname: " + nickname + ".");

        String err = ValidateUtil.validateRegisterForm(firstname, lastname, email, nickname, password);
        if (err != null) {
            log("An error \"" + err + "\" occurred in the main POST handler.");
            this.doError(req, resp, err);
            return;
        }

        try {
            BlogUser user = UserDAOProvider.getDAO().getUserByNickname(nickname);

            if (user != null) {
                this.doError(req, resp, "User with provided nickname already exists.");
                return;
            }

            log("Attempting to create a new user with nickname " + nickname + ".");
            if (!UserDAOProvider.getDAO().createUser(new BlogUser(
                    firstname, lastname, nickname, email, CryptoUtil.encrypt(password)
            ))) {
                log("User could not be created.");
                this.doError(req, resp, "Could not create user.");
                return;
            }

            user = UserDAOProvider.getDAO().getUserByNickname(nickname);

            HttpSession session = req.getSession();

            session.setAttribute("current.user.id", user.getId());
            session.setAttribute("current.user.fn", user.getFirstName());
            session.setAttribute("current.user.ln", user.getLastName());
            session.setAttribute("current.user.nick", user.getNick());
        } catch (Exception e) {
            log("An error while parsing user occurred in the register POST handler.");
            log(e.getMessage(), e);
            this.doError(req, resp, "Could not create user.");
            return;
        }

        resp.sendRedirect("/servleti/main");
    }

    protected void doError(HttpServletRequest req, HttpServletResponse resp, String message) throws IOException, ServletException {
        log("Sending an error \"" + message + "\" to the form from the register POST handler.");
        req.setAttribute("error", message);
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }

}
