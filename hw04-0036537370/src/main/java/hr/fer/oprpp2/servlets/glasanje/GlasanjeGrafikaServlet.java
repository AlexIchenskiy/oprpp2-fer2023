package hr.fer.oprpp2.servlets.glasanje;

import hr.fer.oprpp2.dao.DAOProvider;
import hr.fer.oprpp2.model.PollOption;
import hr.fer.oprpp2.util.NetworkUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Voting graphics servlet.
 */
@WebServlet(name = "Glasanje Graf", urlPatterns = "/servleti/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

    /**
     * Method for generating a voting chart
     * @param req HTTP Request
     * @param resp HTTP Response
     * @throws ServletException Request processing exception
     * @throws IOException File manipulation exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setContentType("image/png");

            OutputStream os = resp.getOutputStream();

            List<PollOption> data = DAOProvider.getDao().getPollOptions(Long.parseLong(req.getParameter("pollID")));

            JFreeChart chart = getChart(data.stream().map(PollOption::getOptionTitle).toList(),
                    data.stream().map(PollOption::getVotesCount).toList());
            int width = 500;
            int height = 350;

            ChartUtilities.writeChartAsPNG(os, chart, width, height);
        } catch (Exception e) {
            NetworkUtil.throwNetworkError(req, resp, e.getMessage(), e);
        }
    }

    /**
     * Method for generating a chart from provided data.
     * @param names Poll option names
     * @param res Pol options vote counts
     * @return A chart with poll results
     */
    public JFreeChart getChart(List<String> names, List<Long> res) {
        var dataset = new DefaultPieDataset();

        for (int i = 0; i < names.size(); i++) {
            dataset.setValue(names.get(i), res.get(i));
        }

        JFreeChart chart = ChartFactory.createPieChart("Rezultati glasanja",
                dataset, true, false, false);

        chart.setBorderVisible(false);

        return chart;
    }

}
