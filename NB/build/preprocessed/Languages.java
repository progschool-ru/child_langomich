public class Languages extends Records
{
        public final String NAME = "Languages";

        Languages()
        {
                recordStoreInit(NAME, null, null);
        }
	public String[] getLanguages()
	{
            return getColumn(SINGLE_RECORD);
	}
	public String getLanguage(int row)
	{
            return getRecord(getId(row), SINGLE_RECORD);
	}
        public void newLanguage(String language)
	{
                String languages[] = getLanguages();
                for(int i = 0; i < getNumRecords(); i++)
                    if(language.equals(languages[i]))
                        return;
		addRecord(language);
        }
        public void deleteLanguage(int row)
	{
            super.deleteRecord(getId(row));
        }
}