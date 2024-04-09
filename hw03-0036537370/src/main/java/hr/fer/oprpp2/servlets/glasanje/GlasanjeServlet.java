package hr.fer.oprpp2.servlets.glasanje;

import hr.fer.oprpp2.util.NetworkUtil;
import hr.fer.oprpp2.util.glasanje.GlasanjeData;
import hr.fer.oprpp2.util.glasanje.GlasanjeUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@WebServlet(name = "Glasanje", urlPatterns = "/glasanje")
public class GlasanjeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<GlasanjeData> data = GlasanjeUtil.getDataSortedById(req);

            req.setAttribute("ids", data.stream().map((GlasanjeData::getId)).toList());
            req.setAttribute("names", data.stream().map((GlasanjeData::getName)).toList());

            req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
        } catch (Exception e) {
            NetworkUtil.throwNetworkError(req, resp, e.getMessage());
        }
    }

}
