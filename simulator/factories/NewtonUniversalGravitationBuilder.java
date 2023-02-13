package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {
	
	private Double G;
	
	public  NewtonUniversalGravitationBuilder()
	{
		this.typeTag= "nlug";
		this.desc= "Newton’s law of universal gravitation";
	}
	
	protected ForceLaws createTheInstance(JSONObject data) {
		
		G = data.has("G") ? data.getDouble("G") : 6.67E-11;
		return new NewtonUniversalGravitation(G);
	}
	
	protected JSONObject createData() {
			
			JSONObject data = new JSONObject();
			data.put("G", "the gravitational constant(A NUMBER)");
			return data;
	}
}