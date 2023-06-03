



import jason.environment.Environment;
import java.util.logging.Logger;

import jason.asSyntax.Literal;
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
	public destiniesBuffer destinies = new destiniesBuffer();
	
	
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
					System.out.println("I am going to send: "+destiniesString);
					byte[] bytes = destiniesString.getBytes();	
					DatagramPacket packet = new DatagramPacket(bytes, destiniesString.length(),host,port); 
					mySocket.send(packet);
					Thread.sleep(1000);
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
	

    public class Listener extends Thread{
    	// Listener method: it periodically receives a JSON with all the information 
	
		
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
	                Thread.sleep(1000);

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
    	
    		
        model = new MapModel();
        
        // First, listener 
		Listener listener = new Listener();
		listener.start();
		
		 
		Sender sender = new Sender();
		sender.start();

        


    }

	

    /** creates the agents percepts based on the MapModel */
    void updatePercepts() {
        // clear the percepts of the agents
        clearPercepts("drone1");
        clearPercepts("drone2");
        
        //Add percepts of charge, battery an ammo to both drones
        String h1string = "health("+game.drone1.health+")";
        String c1string = "charge("+game.drone1.charge+")";
        String a1string = "ammo("+game.drone1.ammo+")";

        String h2string = "health("+game.drone2.health+")";
        String c2string = "charge("+game.drone2.charge+")";
        String a2string = "ammo("+game.drone2.ammo+")";
        
    	Literal h1 = Literal.parseLiteral(h1string);
    	Literal c1 = Literal.parseLiteral(c1string);
    	Literal a1 = Literal.parseLiteral(a1string);
    	
     	Literal h2 = Literal.parseLiteral(h2string);
    	Literal c2 = Literal.parseLiteral(c2string);
    	Literal a2 = Literal.parseLiteral(a2string);


       
        addPercept("drone1",h1);
        addPercept("drone1",c1);
        addPercept("drone1",a1);
        addPercept("drone2",h2);
        addPercept("drone2",c2);
        addPercept("drone2",a2);
		
     

        
   
        // drone1 and drone2 locations
        Point3D d1pos = arrayToPoint3D("drone1");
        Point3D d2pos = arrayToPoint3D("drone2");
        

        // After calculating the security distance, we will set a threshold of 50 to add a percept
		double securityDistance = d1pos.distanceBetweenVectors(d2pos);
		System.out.println("Security distance: "+securityDistance);
		
		// Safezone
		
		if (securityDistance > 200.0){ // if the security distance is over 200, safezone.
			addPercept("drone1", sz1);	
			addPercept("drone2", sz2);	
			
		} else {
			addPercept("drone1", nsz1);	
			addPercept("drone2", nsz2);	

		}
		

		System.out.println("[drone1]: "+consultPercepts("drone1"));
		System.out.println("[drone2]: "+consultPercepts("drone2"));

		
		

		  
    }

  
    @Override
    public boolean executeAction(String ag, Structure action ) {
    	
    	System.out.println("["+ag+"] doing "+action);
    	// drone1 and drone2 locations
        Point3D d1pos = arrayToPoint3D("drone1");
        Point3D d2pos = arrayToPoint3D("drone2");
        
        
    	
    	boolean result = false;
    	
		
		
		if (action.getFunctor().equals("move_towards"))
		{
			Point3D dest1 = model.target(d2pos);
			Point3D dest2 = model.target(d1pos);
			destinies.setTarget("drone1", dest1);		
			destinies.setTarget("drone2", dest2);	
			result = true;
		}
		

		// flee from the other drone
		if (action.getFunctor().equals("flee")) {
			Point3D dest1 = model.getSafePositiond1();
			Point3D dest2 = model.getSafePositiond2();

			destinies.setTarget("drone1", dest1);		
			destinies.setTarget("drone2", dest2);					

			result = true;			
		}
		
		System.out.println("Destinies: "+destinies+ "\n");

		 
		
		
		
		/*
		if (action.getFunctor().equals("findhealth") && game.healthPackages != null){ // aunque podríamos encapsular esto dentro de decide new position 
			Point3D dest = model.getPackagePos(game.healthPackages);
			destinies.setTarget(ag,dest);
			result = true;
		}
		
		if (action.getFunctor().equals("findammo") && game.ammoPackages != null){ // aunque podríamos encapsular esto dentro de decide new position 
			Point3D dest = model.getPackagePos(game.ammoPackages);
			destinies.setTarget(ag,dest);
			result = true;
		}
		
		if (action.getFunctor().equals("findbat") && game.chargePackages != null){ // aunque podríamos encapsular esto dentro de decide new position 
			Point3D dest = model.getPackagePos(game.chargePackages);
			destinies.setTarget(ag,dest);
			result = true;
		}
		*/
		
		
          
        if (result) {

            updatePercepts();
			informAgsEnvironmentChanged();
            try {
                Thread.sleep(200);
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
    

