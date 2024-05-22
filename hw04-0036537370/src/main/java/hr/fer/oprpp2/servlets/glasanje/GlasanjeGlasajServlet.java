package hr.fer.oprpp2.servlets.glasanje;

import hr.fer.oprpp2.dao.DAOProvider;
import hr.fer.oprpp2.util.NetworkUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Voting servlet.
 */
@WebServlet(name = "Glasaj", urlPatterns = "/servleti/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

    /**
     * Method for adding a single vote and redirecting to the voting results page
     * @param req HTTP Request
     * @param resp HTTP Response
     * @throws ServletException Request processing exception
     * @throws IOException File manipulation exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long pollId, optionId;

        try {
            pollId = Long.parseLong(req.getParameter("pollID"));
            optionId = Long.parseLong(req.getParameter("optionID"));
            DAOProvider.getDao().vote(optionId);

            resp.sendRedirect(req.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + pollId);
        } catch (Exception e) {
            NetworkUtil.throwNetworkError(req, resp, e.getMessage(), e);
        }
    }

}
