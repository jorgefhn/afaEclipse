



import jason.environment.Environment;
import java.util.logging.Logger;
import jason.asSyntax.Structure;
import java.io.IOException;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.lang.InterruptedException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;


import java.io.StringReader;


public class MapEnv extends Environment implements declareLiterals {
    

	gameInfo game = new gameInfo();
	
	// destinies 
	destiniesBuffer destinies = new destiniesBuffer();
	
	
	static Logger logger = Logger.getLogger(MapEnv.class.getName());
	MapModel model; // the model of the grid
		
	
	public class Sender extends Thread{
		// Sender method: it sends periodically the destinies of the drones
		@Override
        public void run(){
					
			DatagramSocket mySocket = null;
			try {
				
				mySocket = new DatagramSocket();
				InetAddress host = InetAddress.getByName("127.0.0.1"); 
				int port = 11000; 
				
				while ( true ) {
					String destiniesString = destinies.toString();					
					byte[] bytes = destiniesString.getBytes();	
					DatagramPacket packet = new DatagramPacket(bytes, destiniesString.length(),host,port); 
					mySocket.send(packet);
					Thread.sleep(2000);
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
	

    public class Receiver extends Thread{
    	// Receiver method: it periodically receives a JSON with all the information 
	
		
        @Override
        public void run(){
        	
			DatagramSocket mySocket = null;

            try {
            	
            	mySocket = new DatagramSocket(11004);
                byte[] buffer = new byte[1024];
                

                // Update drones info
                while ( true ) {
						
                    DatagramPacket peticion = new DatagramPacket(buffer,buffer.length);
                    mySocket.receive(peticion);
                    String mensaje = new String(peticion.getData(),0,peticion.getLength());
                    System.out.println("Recibido: "+mensaje);
                    
                    updateFromUnity(mensaje);
					updatePercepts();
					TimeUnit.SECONDS.sleep(2);

                }
                
            } catch(SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            mySocket.close();    

        
        }
        

		private void updateFromUnity(String mensaje) {
			// method to update positions with a message received from Unity
			
			
			JsonReader jsonReader = Json.createReader(new StringReader(mensaje));
			JsonObject newLocations= jsonReader.readObject();
			jsonReader.close();
			
			JsonObject drone1 = newLocations.getJsonObject("drone1");
			JsonObject drone2 = newLocations.getJsonObject("drone2");
			
			game.drone1.setPosition(drone1.getString("position"));
			game.drone2.setPosition(drone2.getString("position"));
			
			// obtenemos indicadores de salud, carga y munición de drones
		
			game.drone1.setHealthLevel(drone1.get("health"));
			game.drone1.setChargeLevel(drone1.get("charge"));
			game.drone1.setAmmoLevel(drone1.get("ammo"));

			game.drone2.setHealthLevel(drone2.get("health"));
			game.drone2.setChargeLevel(drone2.get("charge"));
			game.drone2.setAmmoLevel(drone2.get("ammo"));
			

			// packages
			game.updateHealthPackages(newLocations.getJsonArray("healthPackages"));
			game.updateChargePackages(newLocations.getJsonArray("chargePackages"));
			game.updateAmmoPackages(newLocations.getJsonArray("ammoPackages"));
			
			

    }
    }
   
	
  

    @Override
    public void init(String[] args) {
    	
    	game.drone1 = new droneInfo();
    	game.drone2 = new droneInfo();
    	
    	Point3D origin = new Point3D(0.0,0.0,0.0);
    	game.drone1.setPosition(origin.toString()); // default
    	game.drone2.setPosition(origin.toString()); // default

    	
    	// by default, drones will move towards the enemy

    	destinies.setTarget("drone1",arrayToPoint3D("drone2"));
        destinies.setTarget("drone2",arrayToPoint3D("drone1"));
    	
    		
    	
        
        // First, listener 
		Receiver listener = new Receiver();
		listener.start();
		
		 
		Sender sender = new Sender();
		sender.start();

        model = new MapModel();

        updatePercepts();

    }

	

    /** creates the agents percepts based on the MapModel */
    void updatePercepts() {
        // clear the percepts of the agents
        clearPercepts("drone1");
        clearPercepts("drone2");
        
        
        // drone1 and drone2 locations
        Point3D d1pos = arrayToPoint3D("drone1");
        Point3D d2pos = arrayToPoint3D("drone2");


        // After calculating the security distance, we will set a threshold of 50 to add a percept
		double securityDistance = d1pos.distanceBetweenVectors(d2pos);
		
		System.out.println("Security distance: "+securityDistance);
		
		// Safezone
		if (securityDistance > 400.0){ // if the security distance is over 50, safezone.
			addPercept("drone1", sz1);	
			addPercept("drone1", np1);	
			
			addPercept("drone2", sz2);	
			addPercept("drone2", np2);	
			System.out.println("distance > 400");
			
		}
		
		
		
		// Aquí faltaría lo de los cargamentos para nhealth,nammo,ncharge
		System.out.println(consultPercepts("drone1"));
		  
    }

  
    @Override
    public boolean executeAction(String ag, Structure action ) {
    	
    	
    	boolean result = true;
    	
        System.out.println("["+ag+"] doing: "+action);	
		System.out.println(action.getFunctor());
		
		// decide new position 
		if (action.getFunctor().equals("decide_position")){ // aunque podríamos encapsular esto dentro de decide new position 
			Point3D newPos = new Point3D(0.0,0.0,0.0);
			 // drone1 and drone2 locations
	        Point3D d1pos = arrayToPoint3D("drone1");
	        Point3D d2pos = arrayToPoint3D("drone2");
	        
			if (ag == "drone1") {
				newPos = model.getNewPosition(d1pos);
			}
			if (ag == "drone2") {
				newPos = model.getNewPosition(d2pos);
			}
			
			destinies.setTarget(ag,newPos);
			result = true;
		}
		
		// flee from the other drone
		if (action.getFunctor().equals("flee")) {
			// aquí es donde se le envía a Unity el plan para que se mueva
			
			Point3D currentPos = null;
			Point3D fleeFrom = null;

			
			if (ag.equals("drone1")){ // tiene que huir del dron2
				currentPos = arrayToPoint3D("drone1");
				fleeFrom = arrayToPoint3D("drone2");
				Point3D newPos = model.getSafePosition(currentPos,fleeFrom);
				destinies.setTarget("drone1", newPos);
 			}
			
			if (ag.equals("drone2")){ // tiene que huir del dron2
				currentPos = arrayToPoint3D("drone2");
				fleeFrom = arrayToPoint3D("drone1");
				Point3D newPos = model.getSafePosition(currentPos,fleeFrom);
				destinies.setTarget("drone1", newPos);
			}
			
			
			
			System.out.println("New drone destinies: "+destinies);
			
			result = true;
			
			
			
		}
		 
          
        if (result) {

            updatePercepts();
			informAgsEnvironmentChanged();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {}
        }
        
       
        return result;
    }



	



	public Point3D arrayToPoint3D(String droneName) {
		
		
		String agent = droneName.equals("drone1") ? droneName : "drone2"; 
		JsonArray posArray = null;

		if (agent == "drone1") {
			posArray = game.drone1.position;
		}
		
		else{
			posArray = game.drone2.position;
		}
		
		// JsonArray --> String --> Point3D
		Point3D vector = new Point3D(0.0,0.0,0.0);
		vector.toPoint3D(posArray.toString());
		return vector;
	} 
    
	 
	
}
    

