package simulator.model;
import org.json.JSONObject;
import simulator.misc.Vector2D;

public class Body {
	
	protected String id;
	protected Vector2D velocity;
	protected Vector2D force;
	protected Vector2D position;
	protected double mass;
	
	
	
	public Body(String id,Vector2D velocity,Vector2D position,Double mass) {
		this.id =id;
		this.velocity=velocity;
		this.force= new Vector2D();
		this.position=position;
		this.mass=mass;
		
	}

	
	public String getId(){return this.id;} //devuelve el identificador del cuerpo.
	
	public Vector2D getVelocity(){return this.velocity;} //devuelve el vector de velocidad.
	
	public Vector2D getForce(){return this.force;} //devuelve el vector de fuerza.
	
	public Vector2D getPosition(){return this.position;} //devuelve el vector de posición.
	
	public double getMass() {return this.mass;} //devuelve la masa del cuerpo.
	
	void addForce(Vector2D f){this.force=this.force.plus(f);} //añade la fuerza f al vector de fuerza del cuerpo (usando)el método plus de la clase Vector2D).
	
	void resetForce(){this.force= new Vector2D();} //pone el valor del vector de fuerza a (0, 0).
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Body other = (Body) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void move(double t) {

		Vector2D a;		// Vector acceleration

		if(mass == 0)
			a = new Vector2D();
		else {
			a = force.scale(1d/mass);
		}
		position = position.plus(velocity.scale(t).plus(a.scale(t*t*0.5d)));
		velocity = velocity.plus(a.scale(t));


	} //mueve el cuerpo durante t segundos utilizando los atributos del mismo
	
	public JSONObject getState() {
		
		JSONObject body = new JSONObject();
	
		body.put("id", id);
		body.put("m", mass);
		body.put("p", position.asJSONArray());
		body.put("v", velocity.asJSONArray());
		body.put("f", force.asJSONArray());
		
		return body;

	}
	
	
}