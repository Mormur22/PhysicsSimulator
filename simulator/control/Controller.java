package simulator.control;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.json.*;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {
	
	private PhysicsSimulator sim;
	private Factory<Body> bodiesFac;
	private Factory<ForceLaws> forcelawsFac;
	
	public Controller(PhysicsSimulator sim, Factory<Body> bodiesFac, Factory<ForceLaws> forcelawsFac )
	{
		this.sim=sim;
		this.bodiesFac=bodiesFac;
		this.forcelawsFac= forcelawsFac;
	}
	
	
	
	public void loadBodies(InputStream in)
	{
		JSONObject jsonInp = new JSONObject (new JSONTokener(in));
		JSONArray bodies = jsonInp.getJSONArray("bodies");
		
		for(int i =0; i<bodies.length();i++)
		{
			sim.addBody(bodiesFac.createInstance(bodies.getJSONObject(i)));
		}
	}
	
	
	public void run(int n)
	{
		for(int i=0; i<n;i++)
		{
			sim.advance();
		}
	}

	
	public void run(int n, OutputStream out, InputStream expOut, StateComparator cmp) throws NotEqualStatesException
	{
		JSONObject expOutJO = null;
		
		if(expOut != null)
		{
			expOutJO= new JSONObject (new JSONTokener(expOut));
		}
		
		//en caso de que no imprima nada por pantalla
		if(out==null)
		{
			out = new OutputStream()
			{	
				//Sobreescribe el metodo write de la clase OutputStream
				public void write (int b) throws IOException{}
			};
		}
		
		PrintStream p = new PrintStream(out);
		p.println("{");
		p.println("\"states\": [");
		
		
		
		JSONObject currentState= null;
		JSONObject expState= null;
		
		currentState = sim.getState();
		p.println(currentState);
		
		if(expOutJO!= null)
		{
			expState = expOutJO.getJSONArray("states").getJSONObject(0);
				if(!cmp.equal(expState,currentState))
					throw new NotEqualStatesException(expState,currentState,StateComparator.obj1, StateComparator.obj2,0);
		}

		for(int i=1;i<n + 1;i++)
		{	
			p.print(",");
			sim.advance();
			currentState=sim.getState();
			p.println(currentState);
			
			if(expOutJO!= null)
			{
				expState = expOutJO.getJSONArray("states").getJSONObject(i);
				if(!cmp.equal(expState,currentState))
					throw new NotEqualStatesException(expState,currentState,StateComparator.obj1,StateComparator.obj2,i);
			}
			
		}
		
		p.println("]");
		p.println("}");
		
	}
	
	
	public void reset()
	{
		this.sim.reset();
	}
	
	public void setDeltaTime(Double dt)
	{
		this.sim.setDeltatime(dt);
	}
	
	public void addObserver(SimulatorObserver o)
	{
		this.sim.addObserver(o);
	}
	
	
	public List <JSONObject>getForceLawsInfo()
	{	
		return forcelawsFac.getInfo();
	}
	
	
	public void setForceLaws(JSONObject info)
	{
		ForceLaws newlaws = forcelawsFac.createInstance(info);
		this.sim.setForceLawsLaws(newlaws);
	}

	
}
