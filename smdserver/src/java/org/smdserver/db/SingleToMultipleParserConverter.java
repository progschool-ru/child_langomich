package org.smdserver.db;

import java.sql.ResultSet;
import java.sql.SQLException;

class SingleToMultipleParserConverter implements IMultipleResultParser
{
	private IResultParser parser;

	public SingleToMultipleParserConverter(IResultParser parser)
	{
		this.parser = parser;
	}
	public int parse(ResultSet set) throws SQLException
	{
		if(set.next())
		{
			boolean result = parser.parse(set);
			return result ? 1 : -1;
		}
		return -1;
	}
}
