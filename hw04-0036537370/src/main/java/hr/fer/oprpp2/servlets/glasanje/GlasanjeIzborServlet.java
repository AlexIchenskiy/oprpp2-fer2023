package hr.fer.oprpp2.servlets.glasanje;

import hr.fer.oprpp2.dao.DAO;
import hr.fer.oprpp2.dao.DAOProvider;
import hr.fer.oprpp2.model.Poll;
import hr.fer.oprpp2.model.PollOption;
import hr.fer.oprpp2.util.NetworkUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for showing one poll results.
 */
@WebServlet(name = "Glasanje", urlPatterns = "/servleti/glasanje")
public class GlasanjeIzborServlet extends HttpServlet {

    /**
     * Method for showing a poll results.
     * @param req HTTP Request
     * @param resp HTTP Response
     * @throws ServletException Request processing exception
     * @throws IOException File manipulation exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            DAO dao = DAOProvider.getDao();
            long id = Long.parseLong(req.getParameter("pollID"));

            Poll poll = dao.getPollById(id);
            List<PollOption> data = dao.getPollOptions(id);

            req.setAttribute("poll", poll);
            req.setAttribute("ids", data.stream().map((PollOption::getId)).toList());
            req.setAttribute("names", data.stream().map((PollOption::getOptionTitle)).toList());

            req.getRequestDispatcher("/WEB-INF/pages/glasanjeGlasaj.jsp").forward(req, resp);
        } catch (Exception e) {
            NetworkUtil.throwNetworkError(req, resp, e.getMessage(), e);
        }
    }

}
