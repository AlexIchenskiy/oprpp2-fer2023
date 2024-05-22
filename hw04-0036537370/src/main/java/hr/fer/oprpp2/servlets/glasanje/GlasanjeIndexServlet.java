package hr.fer.oprpp2.servlets.glasanje;

import hr.fer.oprpp2.dao.DAOProvider;
import hr.fer.oprpp2.model.Poll;
import hr.fer.oprpp2.util.NetworkUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "PollIndexServlet", urlPatterns = "/servleti/index.html")
public class GlasanjeIndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Poll> polls = DAOProvider.getDao().getPolls();
            req.setAttribute("polls", polls);
            req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
        } catch (Exception e) {
            NetworkUtil.throwNetworkError(req, resp, "Could not load index page.", e);
        }
    }

}
