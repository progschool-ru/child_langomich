public class Language 
{
	private String name;
	private String id;
	private long modified;
	
	public Language(String name, String id, long modified)
	{
		this.name = name;
		this.id = id;
		this.modified = modified;
	}
	
	public Language(String[] languageRecord)
	{
		this.name = languageRecord[0];
		this.id = languageRecord[1];
		this.modified = Long.parseLong(languageRecord[2]);
	}

	public String getId() 
	{
		return id;
	}

	public String getName() 
	{
		return name;
	}

	public long getModified()
	{
		return modified;
	}
}
