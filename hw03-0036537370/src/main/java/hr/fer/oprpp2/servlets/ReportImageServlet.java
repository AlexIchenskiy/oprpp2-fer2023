package hr.fer.oprpp2.servlets;

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

/**
 * Servlet for generating an OS usage chart image report.
 */
@WebServlet(name = "ReportImage", urlPatterns = "/reportImage")
public class ReportImageServlet extends HttpServlet {

    /**
     * Method for generating a chart of OS usages with random data
     * @param req HTTP Request
     * @param resp HTTP Response
     * @throws IOException File manipulation exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("image/png");

        OutputStream os = resp.getOutputStream();

        JFreeChart chart = getChart();
        int width = 500;
        int height = 350;

        ChartUtilities.writeChartAsPNG(os, chart, width, height);
    }

    public JFreeChart getChart() {

        var dataset = new DefaultPieDataset();

        int linux = (int) (Math.random() * 70);
        int other = (int) (Math.random() * 10);

        dataset.setValue("Linux", linux);
        dataset.setValue("Other", other);
        dataset.setValue("Windows", 100 - linux - other);

        JFreeChart chart = ChartFactory.createPieChart("Most used OS-s",
                dataset, true, false, false);

        chart.setBorderVisible(false);

        return chart;
    }

}
