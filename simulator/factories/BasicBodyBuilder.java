package simulator.factories;
import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body> {

	
	public BasicBodyBuilder()
	{
		this.typeTag= "basic";
		this.desc ="Default body";
	}
	
	protected Body createTheInstance(JSONObject data) {
		
		JSONArray pos = data.getJSONArray("p");
		Vector2D p = new Vector2D(pos.getDouble(0),pos.getDouble(1));
		
		JSONArray vel = data.getJSONArray("v");
		Vector2D v = new Vector2D(vel.getDouble(0),vel.getDouble(1));
		
		String id = data.getString("id");
		double m = data.getDouble("m");
		
		return new Body(id,v,p,m);
	}
	
	protected JSONObject createData()
	{
		JSONObject data = new JSONObject();
		

		data.put("id","the identifier");
		data.put("v","the velocity");
		data.put("p","the position");
		data.put("m","the mass");
		
		
		return data;
	}
	
}
