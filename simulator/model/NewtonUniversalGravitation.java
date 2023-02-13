package simulator.model;
import java.util.List;
import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws {

	protected double _G;
	
	public  NewtonUniversalGravitation (double g)
	{
		_G=g;
	}
	
	public void apply(List<Body> bs) {
		
		for(Body a: bs)
            for(Body b: bs)
            	if(!a.id.equals(b.id) && a.getMass() != 0.0d)
            		a.addForce(force(a,b));
	}
	
	
	private Vector2D force(Body a, Body b) {

	    Vector2D delta = b.getPosition().minus(a.getPosition());
	    double dist = delta.magnitude();
	    double magnitude = dist>0 ? (_G * a.getMass() * b.getMass()) / (dist*dist): 0.0;
	    return delta.direction().scale(magnitude);

	}
	        
	public String toString()
	{return "Newton Universal Gravitation with constant acceleration "+ _G;}

}
