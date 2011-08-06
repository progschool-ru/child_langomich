package org.smdserver.db;

import java.sql.ResultSet;
import java.sql.SQLException;


public class FirstArgParser implements IResultParser
{
	private Object value;

	public boolean parse(ResultSet set) throws SQLException
	{
		value = set.getObject(1);
		return true;
	}

	public Object getValue()
	{
		return value;
	}
}
