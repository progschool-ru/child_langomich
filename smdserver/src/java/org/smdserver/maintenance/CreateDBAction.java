package org.smdserver.maintenance;

import java.util.ArrayList;
import java.util.List;

public class CreateDBAction extends DBAction
{
	protected List<String> getQueries (String dbName, String host,
			                           String user, String password,
			                           String tablesPrefix)
	{
		String [] templates = {
			"create database %1$s CHARACTER SET utf8 COLLATE utf8_general_ci;",
			"grant all on %1$s.* to '%2$s'@'%4$s' identified by '%3$s' with grant option"
		};
		
		List<String> queries = new ArrayList<String>();
		queries.add(String.format(templates[0], dbName));
		queries.add(String.format(templates[1], dbName, user, password, host));
		
		return queries;
	}
}
