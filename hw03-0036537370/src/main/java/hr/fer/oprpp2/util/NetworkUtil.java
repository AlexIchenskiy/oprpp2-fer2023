package hr.fer.oprpp2.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NetworkUtil {

    public static void throwNetworkError(HttpServletRequest req, HttpServletResponse resp, String errMessage)
            throws ServletException, IOException {
        req.setAttribute("errMessage", errMessage);
        req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
    }

}
