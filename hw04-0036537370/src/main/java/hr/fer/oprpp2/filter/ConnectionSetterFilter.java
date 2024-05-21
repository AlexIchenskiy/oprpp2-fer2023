package hr.fer.oprpp2.filter;

import hr.fer.oprpp2.dao.sql.SQLConnectionProvider;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebFilter(filterName="f1",urlPatterns={"/servleti/*"})
public class ConnectionSetterFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        DataSource ds = (DataSource) request.getServletContext().getAttribute("hr.fer.oprpp2");
        Connection con;

        try {
            con = ds.getConnection();
        } catch (SQLException e) {
            throw new IOException("Baza podataka nije dostupna.", e);
        }

        SQLConnectionProvider.setConnection(con);

        try {
            chain.doFilter(request, response);
        } finally {
            SQLConnectionProvider.setConnection(null);
            try {
                con.close();
            } catch (SQLException ignored) {}
        }
    }

}