package simulator.view;

public class LawsInfo {

	private String key;
	private String value;
	private String description;

	public LawsInfo(String k, String v, String d)
	{
		key=k;
		value=v;
		description=d;
	}

	public String getKey()
	{
		return key;
	}

	public String getValue()
	{
		return value;
	}
	public String getDescription()
	{
		return description;
	}


	public void setKey(String key)
	{
		this.key=key;
	}

	public void setValue(String value)
	{
		this.value=value;
	}
	public void setDescription(String desc)
	{
		this.description=desc;
	}
}
