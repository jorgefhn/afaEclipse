import jason.asSyntax.Literal;


public interface declareLiterals {
	
	// Drone 1 percepts 
	
	
	// Safezone	
	public static final Literal sz1 = Literal.parseLiteral("safezone(drone1)");
	public static final Literal sz2 = Literal.parseLiteral("safezone(drone2)");
	
	public static final Literal nsz1 = Literal.parseLiteral("not_safezone(drone1)");
	public static final Literal nsz2 = Literal.parseLiteral("not_safezone(drone2)");
	
	// Drones plans: they decide their new positions 
	public static final Literal np1 = Literal.parseLiteral("new_position(drone1)");
	public static final Literal np2 = Literal.parseLiteral("new_position(drone2)"); 

}
