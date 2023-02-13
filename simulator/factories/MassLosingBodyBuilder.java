package simulator.factories;
import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MassLossingBody;

public class MassLosingBodyBuilder extends Builder<Body> {
	
	
	public MassLosingBodyBuilder()
	{
		this.typeTag= "mlb";
		this.desc ="Mass Losing Body Builder";
	}
	
	protected Body createTheInstance(JSONObject data) {
		
		JSONArray pos = data.getJSONArray("p");
		Vector2D p = new Vector2D(pos.getDouble(0),pos.getDouble(1));
		
		JSONArray vel = data.getJSONArray("v");
		Vector2D v = new Vector2D(vel.getDouble(0),vel.getDouble(1));
		
		String id = data.getString("id");
		double m = data.getDouble("m");
		double lfa = data.getDouble("factor");
		double lfr = data.getDouble("freq");
		return new MassLossingBody(id,v,p,m,lfa,lfr);
	}
	
	protected JSONObject createData()
	{
		JSONObject data = new JSONObject();
		
		//Hacer
		data.put("id","the identifier");
		data.put("v","the velocity");
		data.put("p","the position");
		data.put("m","the mass");
		data.put("lfa","the lose factor");
		data.put("lfr","the lose frequency");
		
		
		return data;
	}
	
}
