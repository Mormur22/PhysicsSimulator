package simulator.control;

import org.json.JSONObject;

public interface StateComparator {
	
	JSONObject obj1 = new JSONObject();
	JSONObject obj2 = new JSONObject();
	
	boolean equal(JSONObject s1, JSONObject s2);
}
