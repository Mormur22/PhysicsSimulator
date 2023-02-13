package simulator.factories;
import org.json.JSONObject;
import simulator.control.MassEqualStates;
import simulator.control.StateComparator;

public class MassEqualStateBuilder extends Builder<StateComparator> {

	
	public MassEqualStateBuilder()
	{
		this.typeTag="masseq";
		this.desc= "Mass-Equal states comparator";
	}
	
	protected StateComparator createTheInstance(JSONObject jsonObject) {
		return new MassEqualStates();
	}
	

}
