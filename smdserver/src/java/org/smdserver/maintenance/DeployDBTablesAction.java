package org.smdserver.maintenance;

import java.util.List;
import java.util.ArrayList;

public class DeployDBTablesAction extends DBAction
{
	protected List<String> getQueries (String dbName, String host,
			                           String user, String password, 
			                           String tablesPrefix)
	{
		String [] templates = {
			"CREATE TABLE %1$susers" +
				"(" +
					"user_id VARCHAR (36) NOT NULL," +
					"login_key VARCHAR (80) UNIQUE NOT NULL," +
					"login VARCHAR (80) NOT NULL," +
					"psw VARCHAR (32) NOT NULL," +
					"is_blocked BOOLEAN NOT NULL DEFAULT '1'," +
					"email VARCHAR (80) NOT NULL," +
					"about VARCHAR (254) NOT NULL," +
					"time_created DATETIME NOT NULL," +
					"time_modified DATETIME NOT NULL," +
					"PRIMARY KEY (user_id)" +
				") ENGINE = InnoDB CHARACTER SET = utf8;",

			"CREATE TABLE %1$slanguages" +
				"(" +
					"language_id VARCHAR (36) NOT NULL," +
					"name VARCHAR (80) NOT NULL," +
					"user_id VARCHAR(36) NOT NULL," +
					"modified BIGINT NOT NULL," +
					"time_created DATETIME NOT NULL," +
					"time_modified DATETIME NOT NULL," +
					"PRIMARY KEY (language_id)," +
					"FOREIGN KEY (user_id) REFERENCES %1$susers(user_id) ON DELETE CASCADE," +
					"UNIQUE (name, user_id)" +
				")  ENGINE = InnoDB CHARACTER SET = utf8;",

			"CREATE TABLE %1$swords" +
			"(" +
				"language_id VARCHAR (36) NOT NULL," +
				"original VARCHAR (80) NOT NULL," +
				"translation VARCHAR(80) NOT NULL," +
				"rating INTEGER NOT NULL," +
				"modified BIGINT NOT NULL," +
				"time_created DATETIME NOT NULL," +
				"time_modified DATETIME NOT NULL," +
				"PRIMARY KEY (language_id, original)," +
				"FOREIGN KEY (language_id) REFERENCES %1$slanguages(language_id) ON DELETE CASCADE," +
				"INDEX(translation)" +
			")  ENGINE = InnoDB CHARACTER SET = utf8;",
			
			"CREATE TABLE %1$sregistration_requests" +
				"(" +
					"user_id VARCHAR (36) NOT NULL," +
					"login_key VARCHAR (80) UNIQUE NOT NULL," +
					"login VARCHAR (80) NOT NULL," +
					"psw VARCHAR (32) NOT NULL," +
					"is_blocked BOOLEAN NOT NULL DEFAULT '1'," +
					"email VARCHAR (80) NOT NULL," +
					"about VARCHAR (254) NOT NULL," +
					"time_created DATETIME NOT NULL," +
					"time_modified DATETIME NOT NULL," +
					"PRIMARY KEY (user_id)" +
				") ENGINE = InnoDB CHARACTER SET = utf8;"
		};
		
		List<String> queries = new ArrayList<String>();
		queries.add(String.format(templates[0], tablesPrefix));
		queries.add(String.format(templates[1], tablesPrefix));
		queries.add(String.format(templates[2], tablesPrefix));
		queries.add(String.format(templates[3], tablesPrefix));

		return queries;
	}	
}
