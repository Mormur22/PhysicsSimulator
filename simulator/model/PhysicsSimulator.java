package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator  {
	
	protected double real_time;
	protected double step_time;
	protected ForceLaws laws;
	protected List<Body> list;
	private List<SimulatorObserver> observers;
	
	public PhysicsSimulator(double	step_time, ForceLaws laws) throws IllegalArgumentException {
		
		this.step_time = step_time;
		this.laws = laws;
		real_time= 0.0;
		list = new ArrayList<Body>();
		observers = new ArrayList<SimulatorObserver>();
		
	}
	
	// Applies 1 step on the simulation
	public void advance() {
		for(Body b:list) {
			b.resetForce();
		}
		laws.apply(list);
		for(Body b:list) {
			b.move(step_time);
		}
		real_time+=step_time;
		
		for(SimulatorObserver o: observers)
		{
			o.onAdvance(list, real_time);
		}
	}
	
	// Add b to simulator
	public void addBody(Body b) throws IllegalArgumentException {
		
		if(list.contains(b))
			throw new IllegalArgumentException();
		else
		{
			list.add(b);
			
			for(SimulatorObserver o: observers)
			{
				o.onBodyAdded(list, b);
			}
		}
	}
	
	public JSONObject getState() {
		
		 JSONObject value = new JSONObject().put("time", real_time);
		 JSONArray bodyArray = new JSONArray();
		 for(Body b: list)
			 bodyArray.put(b.getState());
		 value.put("bodies", bodyArray);
		 return value;
	}
	
	public String toString() {
		return getState().toString();
	}
	
	
	public void reset()
	{
		list.clear();
		real_time=0.0;
		
		for(SimulatorObserver o: observers)
		{
			o.onReset(this.list, this.real_time, this.step_time, this.laws.toString());
		}
	}
	
	public void setDeltatime(Double dt) throws IllegalArgumentException
	{
		if(dt<=0.0)
		{
			throw new IllegalArgumentException();
		}
		
		else
		{
			this.step_time=dt;
			
			for(SimulatorObserver o: observers)
			{
				o.onDeltaTimeChanged(dt);
			}
		}
	}
	
	
	public void  setForceLawsLaws(ForceLaws forcelaws)
	{
		this.laws=forcelaws;
		
		for(SimulatorObserver o: observers)
		{
			o.onForceLawsChanged(forcelaws.toString());
		}
	}
	
	
	public void addObserver(SimulatorObserver o) {
		if(!this.observers.contains(o))
			this.observers.add(o);
		o.onRegister(list, real_time, step_time, laws.toString());
	}
	
	
	
	
	
	
	
	
}