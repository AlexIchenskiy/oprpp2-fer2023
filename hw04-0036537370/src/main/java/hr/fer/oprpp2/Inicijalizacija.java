package hr.fer.oprpp2;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import hr.fer.oprpp2.util.DBUtil;

@WebListener
public class Inicijalizacija implements ServletContextListener {

	private final String propertiesPath = "WEB-INF/dbsettings.properties";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String connectionURL = DBUtil.getConnectionUrl(propertiesPath, sce);

		ComboPooledDataSource cpds = new ComboPooledDataSource();

		try {
			cpds.setDriverClass("org.apache.derby.client.ClientAutoloadedDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
		}

		cpds.setJdbcUrl(connectionURL);

		DBUtil.initializeData(cpds);

		sce.getServletContext().setAttribute("hr.fer.oprpp2", cpds);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.oprpp2");

		if(cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
