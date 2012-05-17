package org.omich.lang.SQLite;

import org.omich.lang.words.Language;

public interface IQueryLanguage {
	
	public Language getLanguage();
	public IQueryWords getQueryWords();
	public void updateServerId(String serverId);
	public void updateName(String name);
	public void delete();
}
