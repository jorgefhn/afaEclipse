package mainPackage;



import jason.environment.Environment;
import java.util.logging.Logger;
import jason.asSyntax.Literal;
import java.io.IOException;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.lang.InterruptedException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;



public class MapEnv extends Environment {

	
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
	
	

	
	// Locations received from drone1 and drone2
	
	// Hashmap to store the locations
	HashMap<String,Point3D> droneLocations = new HashMap<String,Point3D>(); 
	public Point3D d1Loc = new Point3D(0.0,0.0,0.0);
	public Point3D d2Loc = new Point3D(0.0,0.0,0.0);
	

	
	// Hacemos un diccionario con los destinos hacia los que tienen que huir los drones
	public HashMap<String,Point3D> droneDestinies = new HashMap<String,Point3D>();
	
	public String HashMapToString(HashMap<String, Point3D> map) {
		StringBuilder mapAsString = new StringBuilder("{");
		for (String key : map.keySet()) {
			mapAsString.append(key + "=" + map.get(key) + ", ");
		}
			
	// mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");
		return mapAsString.toString();
	}

	
	public class Sender extends Thread{
		// TODO Auto-generated method stub
		
		@Override
        public void run(){
					
			DatagramSocket mySocket = null;
			try {
				mySocket = new DatagramSocket();
				InetAddress host = InetAddress.getByName("127.0.0.1");
				int port = 11000; 
				
				while ( true ) {
					// casteamos hashMap a string
					String map = HashMapToString(droneDestinies);
					
					System.out.println("Destinies: "+map);
					
					// serializamos
					byte[] bytes = map.getBytes();
										
					// enviamos
					DatagramPacket packet = new DatagramPacket(bytes, map.length(),host,port);
					mySocket.send(packet);
					TimeUnit.SECONDS.sleep(2);
				
				}
				
				
				
			} catch(SocketException e){
				e.printStackTrace();			
			} catch(UnknownHostException e){
				e.printStackTrace();			
			} catch(IOException e){
				e.printStackTrace();			
			} catch(InterruptedException e){
				e.printStackTrace();			
			}
			
			mySocket.close();
			

		
		}
		
	}
	

    public class PortListener extends Thread{
	
		public void locationRetrieve(String mensaje){
			
			// position of drone1
			String strD1 = "drone1";
			String strD2 = "drone2";
			String strHP = "healthPackages";

			int startIndexD1 = mensaje.indexOf(strD1); // en la "d" de "drone1"
			int startIndexD2 = mensaje.indexOf(strD2); // en la "d" de "drone2"
			int startIndexHP = mensaje.indexOf(strHP); // en la "h" de "healthPackage"

			// int endIndex = mensaje.indexOf(")");			
			d1Loc.toPoint3D(mensaje.substring((startIndexD1+strD1.length()+3),startIndexD2-3));
			d2Loc.toPoint3D(mensaje.substring((startIndexD2+strD2.length()+3),startIndexHP-3));
			
			droneLocations.put("drone1", d1Loc);
			droneLocations.put("drone2", d2Loc);

		}
        @Override
        public void run(){
        	
            try {
                DatagramSocket mySocket = new DatagramSocket(11004);
                byte[] buffer = new byte[1024];
                while ( true ) {
					
					
                    DatagramPacket peticion = new DatagramPacket(buffer,buffer.length);
                    mySocket.receive(peticion);
                    
                    String mensaje = new String(peticion.getData(),0,peticion.getLength());
					System.out.println("Mensaje: "+mensaje);
				    locationRetrieve(mensaje);
					
					
					updatePercepts();
    					
					TimeUnit.SECONDS.sleep(2);


                mySocket.close();    
                }
                
            } catch(SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        
        }
    }
   
	
    static Logger logger = Logger.getLogger(MapEnv.class.getName());

    MapModel model; // the model of the grid

    @Override
    public void init(String[] args) {
		
		droneLocations.put("drone1",d1Loc);
		droneLocations.put("drone2",d2Loc);
		
		// From the beggining
		droneDestinies.put("drone1",d1Loc);
		droneDestinies.put("drone2",d2Loc);

		// Initiate socket 
		
		 PortListener listener = new PortListener();
		 listener.start();
		 
		 Sender sender = new Sender();
		 sender.start();

		
		// Initiate model 
        model = new MapModel();

   

        updatePercepts();

        // after updating percepts, we check them
    }

    /** creates the agents percepts based on the MapModel */
    void updatePercepts() {
        // clear the percepts of the agents
        clearPercepts("drone1");

        // After calculating the security distance, we will set a threshold of 50 to add a percept
		double securityDistance = droneLocations.get("drone1").distanceBetweenVectors(droneLocations.get("drone2"));
		System.out.println("Security distance: "+securityDistance);
		if (securityDistance > 50.0){ // if the security distance is over 50, safezone.
			// addPercept("drone1", sz1);	
			// addPercept("drone1", np1);	
			System.out.println("distance > 50");
			
			// addPercept("drone2", sz2); 			
			
		}
		
		// Aquí faltaría lo de los cargamentos para nhealth,nammo,ncharge
		System.out.println(consultPercepts("drone1"));
		  
    }

    // public boolean executeAction(String ag, Structure action) {
    
    
    /*
    @Override
    public boolean executeAction(String ag ) {
    	
    	
    	result = true;
    	/*
        System.out.println("["+ag+"] doing: "+action);
        boolean result = false;
        Location dest = null;
		
		
		System.out.println(action.getFunctor());
		
		// decide new position 
		if (action.getFunctor().equals("decide_position")){ // aunque podríamos encapsular esto dentro de decide new position 
			Point3D newPos = model.getNewPosition();
			droneDestinies.put(ag,newPos);	
			result = true;
		}
		
		// flee from the other drone
		if (action.getFunctor().equals("flee")) {
			// aquí es donde se le envía a Unity el plan para que se mueva
			
			Point3D currentPos = new Point3D(0.0,0.0,0.0);
			Point3D fleeFrom = new Point3D(0.0,0.0,0.0);
			
			if (ag.equals("drone1")){ // tiene que huir del dron2
				currentPos = droneLocations.get("drone1");
				fleeFrom = droneLocations.get("drone2");
			}
			
			if (ag.equals("drone2")){ // tiene que huir del dron2
				currentPos = droneLocations.get("drone2");
				fleeFrom = droneLocations.get("drone1");
			}
			
			Point3D newPos = model.getSafePosition(currentPos,fleeFrom);
			droneDestinies.put(ag,newPos);	
			System.out.println("New drone destinies: "+droneDestinies);
			
			result = true;
			
			
			
		}


        if (action.getFunctor().equals("move_towards")) {
            // System.out.println("is going to move towards");
            String l = action.getTerm(0).toString();
            if (l.equals("health")) { // go to pick up the health package
                dest = model.lHealth;
            }

            try {
                // result = model.moverHacia(ag,dest);
				System.out.println("Moving towards in Unity");
                
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
          
        if (result) {

            updatePercepts();
			informAgsEnvironmentChanged();
            try {
                Thread.sleep(100);
            } catch (Exception e) {}
        }
        
       
        return result;
    } */
    
	 
	
}