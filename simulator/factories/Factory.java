package simulator.factories;

import java.util.List;

import org.json.JSONObject;

public interface Factory<T> {
	// Por defecto son public
	T createInstance(JSONObject info)throws IllegalArgumentException;
	List<JSONObject> getInfo();
}
