package simulator.model;
import java.util.List;
import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws {
	
	private double g;
	private Vector2D center;
	
	public MovingTowardsFixedPoint(Vector2D center,double g) {
		this.g=g;
		this.center=center;
	}

	@Override
	public void apply(List<Body> bs) {

		for(Body b : bs) {
			b.addForce(center.minus(b.getPosition()).direction().scale(g*b.getMass()));
		}
	}
	
	@Override
	public String toString() {
		return "Moving towards " + center + " with constant acceleration "+ g;
	}

}
