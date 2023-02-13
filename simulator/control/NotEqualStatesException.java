package simulator.control;

import org.json.JSONObject;

public class NotEqualStatesException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private JSONObject _actual;
    private JSONObject _expected;
    private JSONObject _obj1;
    private JSONObject _obj2;

    // TODO do it with 4 JSONObjects Actual, Expected, Actual Body, Expected Body and int step
    public NotEqualStatesException(JSONObject expState, JSONObject currentState, JSONObject obj1, JSONObject obj2,int step) {
        super("States are different at step: " + step + System.lineSeparator()
                + "Actual: " + currentState + System.lineSeparator()
                + "Expected: " + expState + System.lineSeparator()
                + "Actual BODY: " + obj1 + System.lineSeparator()
                + "Expected BODY: " + obj2 + System.lineSeparator());

        _actual = currentState;
        _expected = expState;
        _obj1 = obj1;
        _obj2 = obj2;
    }
    
    public JSONObject getactual() {
    	return this._actual;
    }
    
    public JSONObject getexpState()
    {
    	return this._expected;
    }
    
    public JSONObject getobj1()
    {
    	return this. _obj1;
    }
    
    public JSONObject getaobj2()
    {
    	return this._obj2;
    }
    
}
