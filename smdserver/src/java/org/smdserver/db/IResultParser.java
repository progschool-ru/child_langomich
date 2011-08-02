package org.smdserver.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IResultParser
{
	public boolean parse(ResultSet result) throws SQLException;
}
