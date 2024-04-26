package hr.fer.oprpp2.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NetworkUtil {

    /**
     * Dispatch error to the prepared JSP page
     * @param req HTTP Request
     * @param resp HTTP Response
     * @param errMessage Error message to be shown
     * @throws ServletException Servlet exception
     * @throws IOException File manipulation exception
     */
    public static void throwNetworkError(HttpServletRequest req, HttpServletResponse resp, String errMessage)
            throws ServletException, IOException {
        req.setAttribute("errMessage", errMessage);
        req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
    }

}
