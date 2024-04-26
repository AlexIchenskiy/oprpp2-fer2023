package hr.fer.oprpp2.servlets;

import hr.fer.oprpp2.util.NetworkUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for generating an Excel data table with number powers
 */
@WebServlet(name = "Powers", urlPatterns = "/powers")
public class PowersServlet extends HttpServlet {

    /**
     * Method for generating N (1-5) sheets of powers of numbers from A (1-100) to B (1-100)
     * where N, A, B are URL parameters
     * @param req HTTP Request
     * @param resp HTTP Response
     * @throws ServletException Request processing exception
     * @throws IOException File manipulation exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer a = null, b = null, n = null;

        try {
            a = Integer.parseInt(req.getParameter("a"));
            b = Integer.parseInt(req.getParameter("b"));
            n = Integer.parseInt(req.getParameter("n"));
        } catch (Exception ignored) {
        }

        if (a == null || a < -100 || a > 100) {
            NetworkUtil.throwNetworkError(req, resp,
                    "Please provide \"a\" from range [-100, 100]");
            return;
        }

        if (b == null || b < -100 || b > 100) {
            NetworkUtil.throwNetworkError(req, resp,
                    "Please provide \"b\" from range [-100, 100]");
            return;
        }

        if (n == null || n < 1 || n > 5) {
            NetworkUtil.throwNetworkError(req, resp,
                    "Please provide \"n\" from range [1, 5]");
            return;
        }

        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            for (int i = 1; i <= n; i++) {
                HSSFSheet sheet = workbook.createSheet("Power " + i);

                HSSFRow rowHead = sheet.createRow((short) 0);
                rowHead.createCell((short) 0).setCellValue("Number");
                rowHead.createCell((short) 1).setCellValue("Number's " + i + "-th power");

                int rowCount = 1;

                for (int j = a; j <= b; j++) {
                    HSSFRow row = sheet.createRow((short) rowCount++);

                    row.createCell((short) 0).setCellValue(j);
                    row.createCell((short) 1).setCellValue(Math.pow(j, i));
                }
            }

            resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
            resp.setContentType("application/vnd.ms-excel");

            workbook.write(resp.getOutputStream());
        } catch (Exception e) {
            NetworkUtil.throwNetworkError(req, resp, e.getMessage());
        }
    }

}
