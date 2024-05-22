package hr.fer.oprpp2.servlets.glasanje;

import hr.fer.oprpp2.dao.DAOProvider;
import hr.fer.oprpp2.model.PollOption;
import hr.fer.oprpp2.util.NetworkUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Voting results servlet.
 */
@WebServlet(name = "Rezultati", urlPatterns = "/servleti/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

    /**
     * Method for generating and dispatching voting results
     * @param req HTTP Request
     * @param resp HTTP Response
     * @throws ServletException Request processing exception
     * @throws IOException File manipulation exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long id = Long.parseLong(req.getParameter("pollID"));

            List<PollOption> data = DAOProvider.getDao().getPollOptions(id, "votesCount");

            Collections.reverse(data);

            req.setAttribute("pollID", id);
            req.setAttribute("names", data.stream().map(PollOption::getOptionTitle).toList());
            req.setAttribute("res", data.stream().map(PollOption::getVotesCount).toList());
            req.setAttribute("videos", DAOProvider.getDao().getWinnerVideos(id));

            req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
        } catch (Exception e) {
            NetworkUtil.throwNetworkError(req, resp, e.getMessage(), e);
        }
    }

}
