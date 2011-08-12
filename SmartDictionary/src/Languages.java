import java.util.Date;

public class Languages extends Records
{
	private final char ID_PREFIX = '_';
	private final int LANGUAGE_NAME = SINGLE_RECORD;
	private final int LANGUAGE_ID   = LANGUAGE_NAME + 1;
	private final int LANGUAGE_MODIFIED   = LANGUAGE_NAME + 2;
	private final int THE_LAST_COLUMN = LANGUAGE_MODIFIED;
	
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
		
		String [] languageRecord = {languageName, languageId, Long.toString(new Date().getTime())};
		addRecord(languageRecord);
		return languageId;
	}
	
	public void addLanguage(String languageName, String languageId, long modified)
	{
		String[] languageRecord = {languageName, languageId, Long.toString(modified)};
		Language[] languages = getLanguages();

		for(int i = 0; i < languages.length; i++)
		{
			if(languageId.equals(languages[i].getId()) 
					|| isInternalId(languages[i].getId()) && languageName.equals(languages[i].getName()))
			{
				setRecord(languageRecord, getId(i+1));
				return;
			}
		}
		addRecord(languageRecord);		
	}

        public void deleteLanguage(int row)
	{
            super.deleteRecord(getId(row));
        }	
}