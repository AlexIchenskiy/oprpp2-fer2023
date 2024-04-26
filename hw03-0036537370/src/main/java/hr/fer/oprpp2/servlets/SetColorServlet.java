package hr.fer.oprpp2.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for persistent session-based color change using the session attributes.
 */
@WebServlet(name = "SetColor", urlPatterns = "/setcolor")
public class SetColorServlet extends HttpServlet {

    /**
     * Method for setting a persistent session-based bg color
     * @param req HTTP Request
     * @param resp HTTP Response
     * @throws ServletException Request processing exception
     * @throws IOException File manipulation exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String color = req.getParameter("pickedBgCol");
        if (color == null) color = "FFFFFF";

        session.setAttribute("pickedBgCol", color);

        resp.sendRedirect("/index.jsp");
    }

}
