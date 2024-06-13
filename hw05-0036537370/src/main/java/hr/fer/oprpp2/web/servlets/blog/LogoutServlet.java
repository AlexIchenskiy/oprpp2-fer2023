package hr.fer.oprpp2.web.servlets.blog;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Logout servlet for handling the logout logic.
 */
@WebServlet(name = "logout", urlPatterns = { "/servleti/logout" })
public class LogoutServlet extends HttpServlet {

    /**
     * Method that handles the user logout.
     * @param req HTTP Request
     * @param resp HTTP Response
     * @throws ServletException Request processing exception
     * @throws IOException File manipulation exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();

        resp.sendRedirect(req.getContextPath() + "/servleti/main");
    }

}
