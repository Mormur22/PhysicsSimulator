package simulator.factories;
import org.json.JSONObject;
import simulator.control.EpsilonEqualStates;
import simulator.control.StateComparator;

public class EpsilonEqualStateBuilder extends Builder<StateComparator> {

	public EpsilonEqualStateBuilder()
	{
		this.typeTag="epseq";
		this.desc="Epsilon-Equal states comparator";
	}
	protected StateComparator createTheInstance(JSONObject jsonObject) {
		
		double eps= jsonObject.has("eps") ? jsonObject.getDouble("eps") : 0.0;
		return new EpsilonEqualStates(eps);
	}
	
	protected JSONObject createData()
	{
		JSONObject data = new JSONObject();
		data.put("epseq","the allowed error");
		return data;
	}
}
