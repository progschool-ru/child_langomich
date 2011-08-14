package org.smdserver.words.helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.smdserver.db.DbException;
import org.smdserver.db.IMultipleResultParser;
import org.smdserver.db.ISmdDB;
import org.smdserver.db.ISmdStatement;
import org.smdserver.db.SmdStatement;
import org.smdserver.words.Language;

public class LanguagesDistributor
{
	private static final String GET_LANGUAGES_IN              = "SELECT language_id, name FROM %1$s WHERE user_id = ? AND language_id IN (%2$s)";
	private static final String GET_LANGUAGES_IN_WITH_UNKNOWN = "SELECT language_id, name FROM %1$s WHERE user_id = ? AND (language_id IN (%2$s) OR name IN (%3$s))";
	private static final String GET_LANGUAGES_IN_UNKNOWN_ONLY = "SELECT language_id, name FROM %1$s WHERE user_id = ? AND name IN (%3$s)";

	public static class SimpleLanguage
	{
		public String id;
		public String name;

		public SimpleLanguage(String id, String name)
		{
			this.id = id;
			this.name = name;
		}
	}

	private List<Language> withId = new ArrayList<Language>();
	private List<Language> withName = new ArrayList<Language>();
	private Map<String, SimpleLanguage> byId = new HashMap<String, SimpleLanguage>();
	private Map<String, SimpleLanguage> byName = new HashMap<String, SimpleLanguage>();

	private String inIdString;
	private String inNameString;

	private String languagesTable;
	private String userId;

	public LanguagesDistributor(String languagesTable, String userId)
	{
		this.languagesTable = languagesTable;
		this.userId = userId;
	}

	public void handleList(List<Language> list, ISmdDB db) throws DbException
	{
		if(list.isEmpty())
			return;

		distributeAndCreateInStrings(list);
		
		ISmdStatement st = withName.isEmpty() ? createIdStatement() : createNameStatement();
		Parser parser = new Parser();
		db.select(st, parser);
	}

	public SimpleLanguage getById(String id)
	{
		return byId.get(id);
	}

	public SimpleLanguage getByName(String name)
	{
		return byName.get(name);
	}

	public List<Language> getWithId()
	{
		return withId;
	}

	public List<Language> getWithName()
	{
		return withName;
	}

	private ISmdStatement createIdStatement()
	{
		return createStatement(GET_LANGUAGES_IN);
	}

	private ISmdStatement createNameStatement()
	{
		String template = withId.isEmpty() ? GET_LANGUAGES_IN_UNKNOWN_ONLY : GET_LANGUAGES_IN_WITH_UNKNOWN;
		ISmdStatement st = createStatement(template);
		for(Language l : withName)
		{
			st.addString(l.getName());
		}
		return st;
	}

	private ISmdStatement createStatement(String queryTemplate)
	{
		ISmdStatement st = new SmdStatement();
		String query = String.format(queryTemplate, languagesTable, inIdString, inNameString);
		st.addQuery(query);
		st.startSet(0);
		st.addString(userId);
		for(Language l : withId)
		{
			st.addString(l.getId());
		}
		return st;
	}

	private void distributeAndCreateInStrings(List<Language> list)
	{
		StringBuilder inIdListBuilder = new StringBuilder();
		StringBuilder inNameListBuilder = new StringBuilder();
		boolean isFirstInId = true;
		boolean isFirstInName = true;

		for(Language l : list)
		{
			if(l.getId() != null)
			{
				doStep(withId, inIdListBuilder, l, isFirstInId);
				isFirstInId = false;
			}
			else
			{
				doStep(withName, inNameListBuilder, l, isFirstInName);
				isFirstInName = false;
			}
		}

		inIdString = inIdListBuilder.toString();
		inNameString = inNameListBuilder.toString();
	}

	private void doStep(List<Language> list, StringBuilder sb, Language language, boolean isFirst)
	{
		if(!isFirst)
		{
			sb.append(',');
		}
		sb.append('?');
		list.add(language);
	}

	private class Parser implements IMultipleResultParser
	{
		public int parse(ResultSet result) throws SQLException
		{
			while(result.next())
			{
				SimpleLanguage l = new SimpleLanguage(result.getString(1), result.getString(2));
				byId.put(l.id, l);
				byName.put(l.name, l);
			}
			return byId.size();
		}
	}
}
