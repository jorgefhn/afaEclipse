import jason.asSyntax.Literal;


public interface declareLiterals {
	
	// Drone 1 percepts 
	
	// Safezone	
	public static final Literal sz1 = Literal.parseLiteral("safezone(drone1)");
	
	// Drone 1 needs charge, health ammo
	public static final Literal nc1 = Literal.parseLiteral("needs_charge(drone1)"); 
	public static final Literal nh1 = Literal.parseLiteral("needs_health(drone1)");
	public static final Literal na1 = Literal.parseLiteral("needs_ammo(drone1)"); 

	
	// Drone 2 percepts
	
	// Drone 2 needs charge, health ammo
	public static final Literal nc2 = Literal.parseLiteral("needs_charge(drone2)"); 
	public static final Literal nh2 = Literal.parseLiteral("needs_health(drone2)");
	public static final Literal na2 = Literal.parseLiteral("needs_ammo(drone2)"); 
	
	public static final Literal sz2 = Literal.parseLiteral("safezone(drone2)");
	
	// Drones plans: they decide their new positions 
	public static final Literal np1 = Literal.parseLiteral("new_position(drone1)");
	public static final Literal np2 = Literal.parseLiteral("new_position(drone2)"); 

}