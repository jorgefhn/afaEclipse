



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
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

import java.io.StringReader;


  

public class MapEnv extends Environment implements declareLiterals {

    
	 
	
	public JsonObject initiateLocations() {
		// Auxiliar method to initiate array of locations (and destinies
		
		

		// ERASE positions
		
		Point3D d1Loc = new Point3D(0.0,0.0,0.0);
    	Point3D d2Loc = new Point3D(0.0,0.0,0.0);
    	JsonObjectBuilder locationsBuilder = Json.createObjectBuilder();
    	JsonArrayBuilder drone1ArrayBuilder = Json.createArrayBuilder()
    			.add(d1Loc.getX())
    			.add(d1Loc.getY())
    			.add(d1Loc.getZ());
    	
    	JsonArray d1locations = drone1ArrayBuilder.build();

    	JsonArrayBuilder drone2ArrayBuilder = Json.createArrayBuilder()
    			.add(d2Loc.getX())
    			.add(d2Loc.getY())
    			.add(d2Loc.getZ());
    	
    	JsonArray d2locations = drone2ArrayBuilder.build();
    	
    	// health, ammo and charge 
    	
    	JsonObjectBuilder drone1builder = Json.createObjectBuilder();
    	drone1builder
    		.add("health",100)
    		.add("ammo",100)
    		.add("charge",100)
    		.add("position",d1locations);
    	
    	JsonObject drone1 = drone1builder.build();
    	
    	JsonObjectBuilder drone2builder = Json.createObjectBuilder();
    	drone2builder
    		.add("health",100)
    		.add("ammo",100)
    		.add("charge",100)
    		.add("position",d2locations);

    	JsonObject drone2 = drone2builder.build();

    	
    	locationsBuilder
    		.add("drone1",drone1)
    		.add("drone2",drone2);
	
    	JsonObject positions = locationsBuilder.build();
    	
		return positions;
	}
	
	public void updateLocation(String ag, Point3D newPos) {
		
		
		
		
		// ERASE: Method that takes locations array, agent (drone1, drone2) and updates its position
		
		String other = ag.equals("drone1") ? ag : "drone2"; 
		
		JsonObjectBuilder builder = Json.createObjectBuilder();
		
		// Agent locations
		JsonArrayBuilder agArrayBuilder = Json.createArrayBuilder()
    			.add(newPos.getX())
    			.add(newPos.getY())
    			.add(newPos.getZ());
    	
    	JsonArray agLocations = agArrayBuilder.build();
    	
    	// Other locations
    	String otherPosString = locations.getString(other);
    	Point3D otherPos = vectorFromString(otherPosString);
    	
    	JsonArrayBuilder otherArrayBuilder = Json.createArrayBuilder()
    			.add(otherPos.getX())
    			.add(otherPos.getY())
    			.add(otherPos.getZ());
    	
    	JsonArray otherLocations = otherArrayBuilder.build();
    	
    	// get values and update them
    	// health, ammo and charge 
    	
    	JsonObjectBuilder droneagbuilder = Json.createObjectBuilder();
    	
    	JsonObject agdrone = locations.getJsonObject(ag);
    	JsonObject otherdrone = locations.getJsonObject(other);

    	droneagbuilder
    		.add("health",agdrone.get("health"))
    		.add("ammo",agdrone.get("ammo"))
    		.add("charge",agdrone.get("charge"))
    		.add("position",agLocations);
    	
    	JsonObject droneag = droneagbuilder.build();
    	
    	JsonObjectBuilder droneotherbuilder = Json.createObjectBuilder();
    	
    	droneotherbuilder
    		.add("health",otherdrone.get("health"))
    		.add("ammo",otherdrone.get("ammo"))
    		.add("charge",otherdrone.get("charge"))
    		.add("position",otherLocations);
    	
    	JsonObject droneother = droneotherbuilder.build();
    	
    	
    	builder
    		.add("drone1",droneag)
    		.add("drone2",droneother);
	
    	locations = builder.build();
    	    	
   
    	
		
		
	}
	
	gameInfo game = new gameInfo();
	
	
	JsonObject locations = initiateLocations();
	JsonObject destinies = initiateLocations();
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
	

    public class Receiver extends Thread{
    	// Receiver method: it periodically receives a JSON with all the information 
	
		
        @Override
        public void run(){
        	
			DatagramSocket mySocket = null;

            try {
                mySocket = new DatagramSocket(11004);
                byte[] buffer = new byte[1024];
                while ( true ) {
						
                    DatagramPacket peticion = new DatagramPacket(buffer,buffer.length);
                    mySocket.receive(peticion);
                    String mensaje = new String(peticion.getData(),0,peticion.getLength());
                    
                    
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
			
			
			// obtenemos posiciones de dron1 y dron2
			String d1pos = drone1.getString("position");
			String d2pos = drone2.getString("position");
			
			Point3D v1 = new Point3D(0.0,0.0,0.0);
			v1.toPoint3D(d1pos);
			
			Point3D v2 = new Point3D(0.0,0.0,0.0);
			v2.toPoint3D(d2pos);
			
			game.drone1.setPosition(v1);
			game.drone2.setPosition(v2);
			
			// obtenemos indicadores de salud, carga y munición de drones
		
			game.drone1.setHealthLevel(drone1.getString("health"));
			game.drone1.setChargeLevel(drone1.getString("charge"));
			game.drone1.setAmmoLevel(drone1.getString("ammo"));

			game.drone2.setHealthLevel(drone2.getString("health"));
			game.drone2.setChargeLevel(drone2.getString("charge"));
			game.drone2.setAmmoLevel(drone2.getString("ammo"));
			
			// Health Packages, Ammo Packages, Charge Packages 



			
			
			System.out.println("Game: "+game.toString());
			
			// ERASE 

			// String --> Point3D --> Array
			
			// String --> Point3D
			
			
			

			// Point3D --> array
			JsonArrayBuilder drone1ArrayBuilder = Json.createArrayBuilder()
	    			.add(v1.getX())
	    			.add(v1.getY())
	    			.add(v1.getZ());
	    	
	    	JsonArray d1locations = drone1ArrayBuilder.build();

	    	JsonArrayBuilder drone2ArrayBuilder = Json.createArrayBuilder()
	    			.add(v2.getX())
	    			.add(v2.getY())
	    			.add(v2.getZ());
	    	
	    	JsonArray d2locations = drone2ArrayBuilder.build();
			
			
			// actualizar atributos en locations
	    	JsonObjectBuilder drone1builder = Json.createObjectBuilder();
	    	drone1builder
	    		.add("position", d1locations)
	    		.add("health", drone1.get("health"))
	    		.add("ammo", drone1.get("ammo"))
	    		.add("charge", drone1.get("charge"));
	    	
	    	JsonObjectBuilder drone2builder = Json.createObjectBuilder();
	    	drone2builder
	    		.add("position", d2locations)
	    		.add("health", drone2.get("health"))
	    		.add("ammo", drone2.get("ammo"))
	    		.add("charge", drone2.get("charge"));

	    	
	    	drone1 = drone1builder.build();
	    	drone2 = drone2builder.build();

	    	// actualizar locations
			JsonObjectBuilder locationsBuilder = Json.createObjectBuilder(locations);
			locationsBuilder.add("drone1", drone1);
			locationsBuilder.add("drone2", drone2);
			locations = locationsBuilder.build();
		}
    }
   
	
  

    @Override
    public void init(String[] args) {
    	
    	game.drone1 = new droneInfo();
    	game.drone2 = new droneInfo();

    	
    	
    			
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
        
        // drone1 and drone2 locations
        System.out.println(locations);
        Point3D d1pos = vectorFromString("drone1");
        Point3D d2pos = vectorFromString("drone2");


        // After calculating the security distance, we will set a threshold of 50 to add a percept
		double securityDistance = d1pos.distanceBetweenVectors(d2pos);
		
		System.out.println("Security distance: "+securityDistance);
		
		if (securityDistance > 50.0){ // if the security distance is over 50, safezone.
			addPercept("drone1", sz1);	
			addPercept("drone1", np1);	
			System.out.println("distance > 50");
					
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
			Point3D newPos = model.getNewPosition();
			
			// updateLocation(ag,newPos);
			result = true;
		}
		
		// flee from the other drone
		if (action.getFunctor().equals("flee")) {
			// aquí es donde se le envía a Unity el plan para que se mueva
			
			Point3D currentPos = null;
			Point3D fleeFrom = null;

			
			if (ag.equals("drone1")){ // tiene que huir del dron2
				currentPos = vectorFromString("drone1");
				fleeFrom = vectorFromString("drone2");
 			}
			
			if (ag.equals("drone2")){ // tiene que huir del dron2
				currentPos = vectorFromString("drone2");
				fleeFrom = vectorFromString("drone1");
			}
			
			
			Point3D newPos = model.getSafePosition(currentPos,fleeFrom);
			// updateLocation(ag,newPos);
			System.out.println("New drone destinies: "+destinies);
			
			result = true;
			
			
			
		}


        if (action.getFunctor().equals("move_towards")) {
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
    }



	



	public Point3D vectorFromString(String droneName) {
		
		JsonObject object = locations.getJsonObject(droneName);
		JsonArray array = object.getJsonArray("position"); // hay que coger las posiciones (1:-1) o quitarle los paréntesis en el split
		
		System.out.println("Array: "+array);
		
		// El problema está en que de Unity se recibe un Vector3 casteado a String. Hay que castearlo a punto,
		Point3D vector = new Point3D(0.0,0.0,0.0);
		vector.toPoint3D(array.toString());
		
		System.out.println("Nuevo vector: "+vector);
		/*
		// get coordinates 
		JsonValue vx = array.get(0);
		JsonValue vy = array.get(1);
		JsonValue vz = array.get(2);
	
		
		// cast to double 
		double x = ((JsonNumber)vx).doubleValue();
		double y = ((JsonNumber)vy).doubleValue();;
		double z = ((JsonNumber)vz).doubleValue();

		 */
		// Point3D vector = new Point3D(x,y,z);
		return vector;

	
	} 
    
	 
	
}
