package hr.fer.oprpp2.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "SetColor", urlPatterns = "/setcolor")
public class SetColorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String color = req.getParameter("pickedBgCol");
        if (color == null) color = "FFFFFF";

        session.setAttribute("pickedBgCol", color);

        resp.sendRedirect("/index.jsp");
    }

}
