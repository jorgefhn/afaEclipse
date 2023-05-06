/* Initial beliefs and rules */

/* Plans */
	
/* El dron quiere calcular la nueva posición. Lo hará en base a varias prioridades:
1) no se encuentra en la zona segura 
2) tiene sus barras de carga, salud y munición vacías
3) está perfecto y va a calcular una nueva posición */

// 1) el dron no se encuentra en la zona segura, tiene que huir 
+new_position(drone2)
	: not safezone(drone2)
	<- flee(drone2).
	
// Resto de casos: el dron está en la zona segura

// 2.1) no tiene batería, va a por ella
+new_position(drone2)
	: safezone(drone2)& needs_charge(drone2)
	<- move_towards(drone2,charge). 

// 2.2) no tiene salud, va a por ella
+new_position(drone2)
	: safezone(drone2)& needs_health(drone2)
	<- move_towards(drone2,health). 	

// 2.3) no tiene munición, va a por ella
+new_position(drone2)
	: safezone(drone2)& needs_ammo(drone2)
	<- move_towards(drone2,ammo). 		

// 3) está en la zona segura y tiene los niveles cubiertos, calcula posición
+new_position(drone2)
	: safezone(drone2)
	<- decide_position(drone2).
	
	
// Esto es para ver si el dron se encuentra en una posición o no
	
+notat(drone2,P) : at(drone2,P) <- true.
+notat(drone2,P) : not at(drone2,P)
  <- move_towards(P);
     not at(drone2,P).
	 
