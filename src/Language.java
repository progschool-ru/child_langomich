public class Language 
{
	private String name;
	private String id;
	
	public Language(String name, String id)
	{
		this.name = name;
		this.id = id;
	}
	
	public Language(String[] languageRecord)
	{
		this.name = languageRecord[0];
		this.id = languageRecord[1];
	}

	public String getId() 
	{
		return id;
	}

	public String getName() 
	{
		return name;
	}
}
