package org.smdserver.maintenance;

import java.util.ArrayList;
import java.util.List;

public class DropDBAction extends DBAction
{
	protected List<String> getQueries (String dbName, String host,
			                           String user, String password,
			                           String tablesPrefix)
	{
		List<String> queries = new ArrayList<String>();
		
		String [] templates = {
			"drop database %1$s;",
			"revoke all privileges, grant option from '%1$s'@'%2$s';",
			"drop user '%1$s'@'%2$s';"
		};
		
		queries.add(String.format(templates[1], user, host));
		queries.add(String.format(templates[0], dbName));
		queries.add(String.format(templates[2], user, host));
		return queries;
	}
}
