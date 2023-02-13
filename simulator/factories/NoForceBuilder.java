package simulator.factories;

import org.json.JSONObject;
import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws> {

	public NoForceBuilder()
	{
		this.typeTag="nf";
		this.desc="No force applied";
	}
	protected ForceLaws createTheInstance(JSONObject jsonObject) {
		
		return new NoForce();
	}

}
