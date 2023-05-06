package mainPackage;



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
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import java.io.StringReader;




public class MapEnv extends Environment implements declareLiterals {
	
	public JsonObject initiateLocations() {
		// Auxiliar method to initiate array of locations (and destinies)
		
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
    	
    	
    	locationsBuilder
    		.add("drone1",d1locations)
    		.add("drone2",d2locations);
	
    	JsonObject positions = locationsBuilder.build();
    	
		return positions;
	}
	
	
	JsonObject locations = initiateLocations();
	JsonObject destinies = initiateLocations();
	static Logger logger = Logger.getLogger(MapEnv.class.getName());
	MapModel model; // the model of the grid
	
	
	HashMap<String,Point3D> droneLocations = new HashMap<String,Point3D>(); 
	public HashMap<String,Point3D> droneDestinies = new HashMap<String,Point3D>();
	
	
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
                    updatePositions(mensaje);
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

		private void updatePositions(String mensaje) {
			JsonReader jsonReader = Json.createReader(new StringReader(mensaje));
			JsonObject jsonObject = jsonReader.readObject();
			jsonReader.close();
			JsonObject drone1 = jsonObject.getJsonObject("drone1");
			JsonObject drone2 = jsonObject.getJsonObject("drone2");
			// actualizar posiciones en locations
			JsonObjectBuilder locationsBuilder = Json.createObjectBuilder(locations);
			locationsBuilder.add("drone1", drone1);
			locationsBuilder.add("drone2", drone2);
			locations = locationsBuilder.build();
		}
    }
   
	
  

    @Override
    public void init(String[] args) {

    	JsonObject locations = initiateLocations();
    	JsonObject destinies = initiateLocations();
    			
		 Receiver listener = new Receiver();
		 listener.start();
		 
		 Sender sender = new Sender();
		 sender.start();

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
			
			droneDestinies.put(ag,newPos);	
			result = true;
		}
		
		// flee from the other drone
		if (action.getFunctor().equals("flee")) {
			// aquí es donde se le envía a Unity el plan para que se mueva
			
			
			String currentPos = "";
			String fleeFrom = "";

			
			if (ag.equals("drone1")){ // tiene que huir del dron2
				currentPos = locations.getString("drone1");
				fleeFrom = locations.getString("drone2");
 			}
			
			if (ag.equals("drone2")){ // tiene que huir del dron2
				currentPos = locations.getString("drone2");
				fleeFrom = locations.getString("drone1");
			}
			
			
			// create two points
			Point3D current = new Point3D(0.0,0.0,0.0);
			Point3D flee = new Point3D(0.0,0.0,0.0);

			current.toPoint3D(currentPos);
			flee.toPoint3D(fleeFrom);
			
			
			Point3D newPos = model.getSafePosition(current,flee);
			droneDestinies.put(ag,newPos);	
			System.out.println("New drone destinies: "+droneDestinies);
			
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
    
	 
	
}
