package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws>{
	
	private Double g;
	private Vector2D c;
	
    public MovingTowardsFixedPointBuilder(){
        this.typeTag = "mtfp";
        this.desc = "Moving Towards Fixed Point";
    }

    @Override
    protected ForceLaws createTheInstance(JSONObject data){
        try {
        	
        	g = data.has("g") ? data.getDouble("g") : 9.81;
            // Calculate c
            if (data.has("c")) {
                double x = data.getJSONArray("c").getDouble(0);
                double y = data.getJSONArray("c").getDouble(1);
                c = new Vector2D(x, y);
            }
            else c = new Vector2D();
            return new MovingTowardsFixedPoint(c, g);
        } catch (JSONException e) {
            throw new IllegalArgumentException("MovingTowardsFixedPoint");
        }

    }
    @Override
    protected JSONObject createData() {
        return new JSONObject().put("g", "the length of the acceleration vector").put("c", "Start point ");
    }


}