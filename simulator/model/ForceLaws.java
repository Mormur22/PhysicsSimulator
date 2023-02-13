package simulator.model;
import java.util.List;


public interface ForceLaws {
	void apply(List<Body> bs);
}
