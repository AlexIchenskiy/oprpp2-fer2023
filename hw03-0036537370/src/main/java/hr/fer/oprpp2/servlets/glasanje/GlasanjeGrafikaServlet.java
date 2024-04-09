package hr.fer.oprpp2.servlets.glasanje;

import hr.fer.oprpp2.util.NetworkUtil;
import hr.fer.oprpp2.util.glasanje.GlasanjeData;
import hr.fer.oprpp2.util.glasanje.GlasanjeUtil;
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
import java.util.Collections;
import java.util.List;

@WebServlet(name = "Glasanje Graf", urlPatterns = "/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            resp.setContentType("image/png");

            OutputStream os = resp.getOutputStream();

            List<GlasanjeData> data = GlasanjeUtil.getData(req);

            JFreeChart chart = getChart(data.stream().map(GlasanjeData::getName).toList(),
                    data.stream().map(GlasanjeData::getScore).toList());
            int width = 500;
            int height = 350;

            ChartUtilities.writeChartAsPNG(os, chart, width, height);
        } catch (Exception e) {
            NetworkUtil.throwNetworkError(req, resp, e.getMessage());
        }
    }

    public JFreeChart getChart(List<String> names, List<Integer> res) {
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