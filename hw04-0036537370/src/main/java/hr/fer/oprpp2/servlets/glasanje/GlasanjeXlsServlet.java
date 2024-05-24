package hr.fer.oprpp2.servlets.glasanje;

import hr.fer.oprpp2.dao.DAOProvider;
import hr.fer.oprpp2.model.PollOption;
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
import java.util.Collections;
import java.util.List;

/**
 * Servlet for generating voting Excel data table
 */
@WebServlet(name = "Glasanje XLS", urlPatterns = "/servleti/glasanje-xls")
public class GlasanjeXlsServlet extends HttpServlet {

    /**
     * Method for generating a voting Excel data table
     * @param req HTTP Request
     * @param resp HTTP Response
     * @throws ServletException Request processing exception
     * @throws IOException File manipulation exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            List<PollOption> data = DAOProvider.getDao().getPollOptions(Long.parseLong(req.getParameter("pollID")),
                    "votesCount");
            Collections.reverse(data);
            HSSFSheet sheet = workbook.createSheet("Results");

            HSSFRow rowHead = sheet.createRow((short) 0);
            rowHead.createCell((short) 0).setCellValue("Score");
            rowHead.createCell((short) 1).setCellValue("Name");
            rowHead.createCell((short) 2).setCellValue("ID");
            rowHead.createCell((short) 3).setCellValue("Link");

            for (int i = 0; i < data.size(); i++) {
                HSSFRow row = sheet.createRow((short) (i + 1));

                row.createCell((short) 0).setCellValue(data.get(i).getVotesCount());
                row.createCell((short) 1).setCellValue(data.get(i).getOptionTitle());
                row.createCell((short) 2).setCellValue(data.get(i).getId());
                row.createCell((short) 3).setCellValue(data.get(i).getOptionLink());
            }

            resp.setHeader("Content-Disposition", "attachment; filename=\"results.xls\"");
            resp.setContentType("application/vnd.ms-excel");

            workbook.write(resp.getOutputStream());
        } catch (Exception e) {
            NetworkUtil.throwNetworkError(req, resp, e.getMessage(), e);
        }
    }

}
