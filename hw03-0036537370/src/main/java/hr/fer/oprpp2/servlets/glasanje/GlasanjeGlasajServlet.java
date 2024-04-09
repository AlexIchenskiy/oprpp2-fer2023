package hr.fer.oprpp2.servlets.glasanje;

import hr.fer.oprpp2.util.NetworkUtil;
import hr.fer.oprpp2.util.glasanje.GlasanjeUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Glasaj", urlPatterns = "/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            GlasanjeUtil.vote(req);
        } catch (Exception e) {
            NetworkUtil.throwNetworkError(req, resp, e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
    }

}
