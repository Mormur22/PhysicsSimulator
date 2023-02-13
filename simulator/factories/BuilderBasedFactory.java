package simulator.factories;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {
	
	private List<Builder<T>> builders;
	private List<JSONObject> elems;
	
	public BuilderBasedFactory(List<Builder<T>> builders)
	{
		this.builders = new ArrayList<>(builders);
		elems = new ArrayList<>();
		for(Builder<T> b: builders)
		{
			elems.add(b.getBuilderInfo());
		}
	}
	
	
	
	public T createInstance(JSONObject info) throws IllegalArgumentException
	
	{
		
		 if(info==null) throw new IllegalArgumentException("Create Instance invalid value : null");
		
		 for(Builder<T> b: builders)
		 {	 
			T object = b.createInstance(info);
			if(object!=null)
				return object; 
		 }
		
		throw new IllegalArgumentException("Create Instance error: no command recognised"+info.toString());
	}

	@Override
	
	public List<JSONObject> getInfo() {
		 return elems;
	}

}
