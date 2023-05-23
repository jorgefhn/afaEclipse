/* Initial beliefs and rules */
safezone(drone1).
/* Plans */
	
/* El dron quiere calcular la nueva posición. Lo hará en base a varias prioridades:
1) no se encuentra en la zona segura 
2) tiene sus barras de carga, salud y munición vacías
3) está perfecto y va a calcular una nueva posición */

// 1) el dron no se encuentra en la zona segura, tiene que huir 
+new_position(drone1)
	: not safezone(drone1)
	<- flee(drone1).
	
// Resto de casos: el dron está en la zona segura

// 2.1) no tiene batería, va a por ella
+new_position(drone1)
	: safezone(drone1)& charge(0)
	<- findbat(drone1).

// 2.2) no tiene salud, va a por ella
+new_position(drone1)
	: safezone(drone1)& health(20)
	<- findhealth(drone1).

// 2.3) no tiene munición, va a por ella
+new_position(drone1)
	: safezone(drone1)& ammo(0)
	<- findammo(drone1).

// 3) está en la zona segura y tiene los niveles cubiertos, calcula posición
+new_position(drone1)
	: safezone(drone1)
	<- decide_position(drone1).
	

