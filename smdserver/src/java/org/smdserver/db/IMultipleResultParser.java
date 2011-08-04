package org.smdserver.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IMultipleResultParser
{
	/**
	 * @param result
	 * @return Count of parsed rows.
	 * @throws SQLException
	 */
	public int parse(ResultSet result) throws SQLException;
}
