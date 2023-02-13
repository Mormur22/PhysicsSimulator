package simulator.model;
import simulator.misc.Vector2D;

public class MassLossingBody extends Body {
	
	private double _lossFactor;
	private double _lossFrequency;
	private double c;
	
	public MassLossingBody(String id, Vector2D velocity, Vector2D position, double mass, double lossFactor, double lossFrecuency) {
		super(id, velocity, position, mass);
		this._lossFactor=lossFactor;
		this._lossFrequency=lossFrecuency;
		this.c=0.0;
	}

	
	@Override
	 public void move(double t) {
	   super.move(t);
	   c += t;
	   if(c >= _lossFrequency) {
	       c = 0.0;
	       this.mass= this.mass*(1-_lossFactor);
	    }
	 }
	    
}

