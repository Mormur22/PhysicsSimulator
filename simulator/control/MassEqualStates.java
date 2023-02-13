package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;

public class MassEqualStates implements StateComparator {

	@Override
	public boolean equal(JSONObject s1, JSONObject s2) {
		return s1.get("time") != s2.get("time") || !equalkeys(s1, s2);
	}

	public boolean equalkeys(JSONObject s1, JSONObject s2) {
		// TODO Revise
		if(s1.length() != s2.length()) return false;

		JSONArray a1 = s1.getJSONArray("id");	// array of s1's ids
		JSONArray a2 = s2.getJSONArray("id");  // array of s2's ids
		JSONArray b1 = s1.getJSONArray("m");	// mass of the first element
		JSONArray b2 = s2.getJSONArray("m");	// mass of the second element
		
		for(int i = 0; i < a1.length(); i++){
			if (!a1.getString(i).equals(a2.getString(i)) && !b1.getString(i).equals(b2.getString(i))) {
				return false;
			}
		}
		return true;
	}
}