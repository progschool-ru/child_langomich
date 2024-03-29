package org.smdme;

public class Languages extends Records
{
	private final char ID_PREFIX = '_';
	private final int LANGUAGE_NAME = SINGLE_RECORD;
	private final int LANGUAGE_ID   = LANGUAGE_NAME + 1;
	private final int THE_LAST_COLUMN = LANGUAGE_ID;
	
        public final String NAME = "Languages";

        Languages()
        {
                recordStoreInit(NAME, null, null);
        }
		
	public Language[] getLanguages()
	{
		int numRecords = getNumRecords();
		Language [] languages = new Language[numRecords];
		
		for(int i = 0; i < numRecords; i++)
		{
			languages[i] = new Language(getFullRecord(getId(i+1), THE_LAST_COLUMN));
		}
		
		return languages;
	}
	
	public boolean isInternalId(String languageId)
	{
		return languageId.charAt(0) == ID_PREFIX;
	}
	
	public Language getLanguage(int row)
	{
		int rowId = getId(row);
		String [] record = getFullRecord(rowId, THE_LAST_COLUMN);
		return new Language(record);
	}
	
	public Language getLanguageById(String languageId)
	{
		Language[] languages = getLanguages();
		
		for(int i = 0; i < languages.length; i++)
		{
			if(languages[i].getId().equals(languageId))
				return languages[i];
		}
		
		return null;
	}
	
	public String newLanguage(String languageName)
	{
		Language[] languages = getLanguages();
		String languageId = ID_PREFIX + Integer.toString(languages.length);
		
		for(int i = 0; i < languages.length; i++)
		{
			if(languageName.equals(languages[i].getName()))
			{
				return null;
			}
		}
		
		String [] languageRecord = {languageName, languageId};
		addRecord(languageRecord);
		return languageId;
	}
	
	public String addLanguageAndGetOldLanguageId(String languageName, String languageId, Dictionary dictionary)
	{
		String[] languageRecord = {languageName, languageId};
		Language[] languages = getLanguages();

		for(int i = 0; i < languages.length; i++)
		{
			String oldLanguageId = languages[i].getId();
			String oldLanguageName = languages[i].getName();

			if(languageId.equals(oldLanguageId))
			{
				setRecord(languageRecord, getId(i+1));
				return null;
			}
			else if(isInternalId(oldLanguageId) && languageName.equals(oldLanguageName))
			{
				Dictionary oldDictionary = new Dictionary(oldLanguageId);
				int numWords = oldDictionary.getNumRecords();
				for(int j = 0; j < numWords; j++)
				{
					int id = oldDictionary.getId(j+1);
					String [] wordRecord = oldDictionary.getFullRecord(id, oldDictionary.THE_LAST_COLUMN);
					wordRecord[oldDictionary.LANGUAGE - 1] = languageId;
					dictionary.addRecord(wordRecord);
					oldDictionary.deleteRecord(j+1);
				}
				oldDictionary.destroy();

				setRecord(languageRecord, getId(i+1));
				return oldLanguageId;
			}
		}
		addRecord(languageRecord);
		return null;
	}

        public void deleteLanguage(int row)
	{
            super.deleteRecord(getId(row));
        }	
}