package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;

public class EpsilonEqualStates implements StateComparator {
	
	private Double eps;
	public static JSONObject obj1,obj2;
	
	public EpsilonEqualStates(double eps) {
		this.eps = eps;
	}

	@Override
    public boolean equal(JSONObject s1, JSONObject s2) {
		
		obj1=s1;
		obj2=s2;
		
        if (!(s1.getDouble("time") == s2.getDouble("time"))) return false;
        
        else if(s1.length()!=s2.length()) return false;
        
        else {
        	
            for (int i = 0; i < s1.getJSONArray("bodies").length(); i++) {
            	
                if (!(equalEpsilon(s1.getJSONArray("bodies").getJSONObject(i).getDouble("m"), s2.getJSONArray("bodies").getJSONObject(i).getDouble("m"))))
                		return false;
                
                if(!s1.getJSONArray("bodies").getJSONObject(i).get("id").equals(s1.getJSONArray("bodies").getJSONObject(i).get("id")))
                
                	return false;

                JSONArray p1 = s1.getJSONArray("bodies").getJSONObject(i).getJSONArray("p");
                JSONArray p2 = s2.getJSONArray("bodies").getJSONObject(i).getJSONArray("p");
                
                if (!(equalEpsilon(p1, p2)))
                    return false; 
                

               JSONArray v1 = s1.getJSONArray("bodies").getJSONObject(i).getJSONArray("v");
               JSONArray v2 = s2.getJSONArray("bodies").getJSONObject(i).getJSONArray("v");
                
                if (!(equalEpsilon(v1, v2)))
                    return false;
                

                JSONArray f1 = s1.getJSONArray("bodies").getJSONObject(i).getJSONArray("f");
                JSONArray f2 = s2.getJSONArray("bodies").getJSONObject(i).getJSONArray("f");
                
                if (!(equalEpsilon(f1, f2)))
                    return false;
                
            }
        }
        
        return true;
    }
	
	private boolean equalEpsilon(double double1, double double2) {

		return Math.abs(double1 - double2) <= eps;
	}
	
	private boolean equalEpsilon(JSONArray j1, JSONArray j2) {
		
		
		Vector2D f1 = new Vector2D(j1.getDouble(0), j1.getDouble(1));
        Vector2D f2 = new Vector2D(j2.getDouble(0), j2.getDouble(1));
		return f1.distanceTo(f2) <= eps;
	}
	
}