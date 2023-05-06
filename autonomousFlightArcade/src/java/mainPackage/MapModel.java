package mainPackage;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import java.util.Random;

/** class that implements the Model of Domestic Robot application */
public class MapModel extends GridWorldModel {

    
    // constants for the grid objects
    public static final int HEALTH = 16;

    // the grid size
    public static final int GSize = 7;

    boolean healthPicked   = false; // whether the health package is picked up



    Location lLeftUp = new Location(0,0); // left top corner 
    Location lRightUp = new Location(GSize-1,0); // right top corner
    Location lLeftDown = new Location(0,GSize-1); // left bottom corner 
    Location lRightDown = new Location(GSize-1,GSize-1); // right bottom corner 
    Location lHealth  = new Location(GSize/2,GSize/2);

   

    



   
    public MapModel() {
        // create a 7x7 grid with one mobile agent
        super(GSize, GSize, 2);

        // ag code 0 means the drone1
        setAgPos(0, 0, 0);
		// ag code 1 means the drone2
		Location dLoc2 = new Location(GSize-1,GSize-1);
        setAgPos(1, dLoc2);

        // adding location to the health package
        add(HEALTH,lHealth);
                
        
    }


    boolean pickHealth(){
        if (!healthPicked){
            healthPicked = true;
            return true;
        }else{
            return false;
        }
    }
	
	Point3D getSafePosition(Point3D A, Point3D B){
		// método auxiliar para calcular punto simétrico
		System.out.println("Getting safe position: "+A+" "+B);
		Point3D AB = new Point3D(A.x-B.x,A.y-B.y,A.z-B.z); // coordenadas del vector AB
		Point3D AC = new Point3D(A.x+ 2*AB.x,A.y +2*AB.y,A.z+2*AB.z); // coordenadas del vector AC
		Point3D C = new Point3D(A.x + AC.x, A.y + AC.y, A.z + AC.z); // este es el nuevo punto a devolver 
		System.out.println("New safe position "+C);

		return C;
		
	}
	
	Point3D getNewPosition(){
		// método auxiliar para calcular un nuevo punto aleatorio
		// genera una nueva posición aleatoria en un rango de 40, con coordenada z positiva 
		Random rand = new Random();
        double x = rand.nextDouble() * 80 - 40; // Rango [-40, 40]
        double y = rand.nextDouble() * 80 - 40; // Rango [-40, 40]
        double z = rand.nextDouble() * 40; // Rango [0, 40]
		
		Point3D randomPoint = new Point3D(x,y,z); // este es el nuevo punto a devolver 
		return randomPoint;
		
	}

    

    


   

    
}