package hr.fer.oprpp2.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for generating trigonometric functions data
 */
@WebServlet(name = "Trigonometric", urlPatterns = "/trigonometric")
public class TrigonometricServlet extends HttpServlet {

    /**
     * Method for generating trigonometric functions data from A to B where A and B are
     * URL parameters (or else default values)
     * @param req HTTP Request
     * @param resp HTTP Response
     * @throws ServletException Request processing exception
     * @throws IOException File manipulation exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int a, b;

        try {
            a = Integer.parseInt(req.getParameter("a"));
        } catch (Exception e) {
            a = 0;
        }

        try {
            b = Integer.parseInt(req.getParameter("b"));
        } catch (Exception e) {
            b = 360;
        }

        if (a > b) {
            int temp = a;
            a = b;
            b = temp;
        }

        if (b > a + 720) b = a + 720;

        List<Double> sin = new ArrayList<>();
        List<Double> cos = new ArrayList<>();

        for (int i = a; i <= b; i++) {
            sin.add(Math.sin(1.0 * i * Math.PI / 180));
            cos.add(Math.cos(1.0 * i * Math.PI / 180));
        }

        req.setAttribute("start", a);
        req.setAttribute("sin", sin);
        req.setAttribute("cos", cos);

        req.getRequestDispatcher("/WEB-INF/pages/trigonometric.jsp").forward(req, resp);
    }

}
