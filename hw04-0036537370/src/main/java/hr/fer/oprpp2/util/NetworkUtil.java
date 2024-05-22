package hr.fer.oprpp2.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NetworkUtil {

    public static void throwNetworkError(HttpServletRequest req, HttpServletResponse resp, String message, Exception e)
            throws ServletException, IOException {
        e.printStackTrace();
        req.setAttribute("errMessage", message);
        req.getRequestDispatcher("/WEB-INF/pages/glasanjeError.jsp").forward(req, resp);
    }

}
