
package org.smdserver.maintenance;

import java.util.ArrayList;
import java.util.List;


public class DropDBTablesAction extends DBAction
{
	protected List<String> getQueries (String dbName, String host,
			                           String user, String password,
			                           String tablesPrefix)
	{
		String [] templates = {
			"DROP TABLE %1$swords;",
			"DROP TABLE %1$slanguages;",
			"DROP TABLE %1$susers;",
			"DROP TABLE %1$sregistration_requests"
		};
		
		List<String> queries = new ArrayList<String>();
		queries.add(String.format(templates[0], tablesPrefix));
		queries.add(String.format(templates[1], tablesPrefix));
		queries.add(String.format(templates[2], tablesPrefix));
		queries.add(String.format(templates[3], tablesPrefix));
		
		return queries;
	}	
}
