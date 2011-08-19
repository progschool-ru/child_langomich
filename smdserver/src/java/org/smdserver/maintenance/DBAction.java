package org.smdserver.maintenance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.db.IDBConfig;

public abstract class DBAction extends MaintenanceAction
{
	protected String doAction (HttpServletRequest request)
	{
		boolean anyHost = "true".equals(request.getParameter(ActionParams.ANY_HOST));
		String rootDBUser = request.getParameter(ActionParams.DB_USER);
		String rootDBPassword = request.getParameter(ActionParams.DB_PASSWORD);
		
		IDBConfig config = getServletContext().getDBConfig();
		String dbUrl = config.getDBUrl();
		String user = config.getDBUser();
		String password = config.getDBPassword();
		String tablesPrefix = config.getTablesPrefix();
		
		String regexString = "^(jdbc:\\w*://)(\\w*)(:\\d*)?/(\\w*)(.*$)";
		Pattern regex = Pattern.compile(regexString);
		Matcher matcher = regex.matcher(dbUrl);
		matcher.find();
		
		String protocol = matcher.group(1);
		String dbHost = matcher.group(2);
		String port = matcher.group(3);
		String dbName = matcher.group(4);
		String params = matcher.group(5);
		
		String host = anyHost ? "%" : dbHost;
		
		List<String> queries = getQueries(dbName, host, user, password, tablesPrefix);
		
		boolean success;
		int count;

		try
		{
			if(rootDBUser != null && rootDBPassword != null)
			{
				dbUrl = protocol + dbHost + (port != null ? port : "");// + (params != null ? params : "");
				count = doQueries(dbUrl, rootDBUser, rootDBPassword, queries);
			}
			else
			{
				count = doQueries(dbUrl, user, password, queries);
			}
			success = (count == queries.size());
		}
		catch(Exception e)
		{
			log(e.getMessage());
			success = false;
		}
		
		setAnswerParam(ActionParams.SUCCESS, success);
		
		return null;
	}
	
	abstract protected List<String> getQueries (String dbName, String host,
			                                    String user, String password,
			                                    String tablesPrefix);
	
	
	private int doQueries(String url, String user, String password, 
			              List<String> queries) throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connection = DriverManager.getConnection(url, user, password);
		connection.setAutoCommit(false);

		int count = 0;
		Statement statement = connection.createStatement();
		try
		{
			for(String query : queries)
			{
				statement.execute(query);
				count ++;
			}
			
			connection.commit();
		}
		catch(SQLException e)
		{
			connection.rollback();
			throw e;
		}
		statement.close();
		connection.close();
		
		return count;
	}
}
