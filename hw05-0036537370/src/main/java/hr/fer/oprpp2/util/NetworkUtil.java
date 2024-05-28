package hr.fer.oprpp2.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Network utility class.
 */
public class NetworkUtil {

    /**
     * Method for gently throwing a network exception.
     * @param req Servlet HTTP request
     * @param resp Servlet HTTP response
     * @param message Error message
     * @throws ServletException Request processing exception
     * @throws IOException File manipulation exception
     */
    public static void throwNetworkError(HttpServletRequest req, HttpServletResponse resp, String message)
            throws ServletException, IOException {
        req.setAttribute("errMessage", message);
        req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
    }

    /**
     * Method for gently throwing a network exception.
     * @param req Servlet HTTP request
     * @param resp Servlet HTTP response
     * @param message Error message
     * @param e Exception
     * @throws ServletException Request processing exception
     * @throws IOException File manipulation exception
     */
    public static void throwNetworkError(HttpServletRequest req, HttpServletResponse resp, String message, Exception e)
            throws ServletException, IOException {
        e.printStackTrace();
        req.setAttribute("errMessage", message);
        req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
    }

}