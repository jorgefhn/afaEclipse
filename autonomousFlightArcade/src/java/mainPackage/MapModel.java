 
import java.util.Random;

/** class that implements the Model of Domestic Robot application */
public class MapModel {

     
   
	
	Point3D getSafePosition(Point3D A, Point3D B){
		// método auxiliar para calcular punto simétrico
		System.out.println("Getting safe position: "+A+" "+B);
		Point3D AB = new Point3D(A.x-B.x,A.y-B.y,A.z-B.z); // coordenadas del vector AB
		Point3D AC = new Point3D(A.x+ 2*AB.x,A.y +2*AB.y,A.z+2*AB.z); // coordenadas del vector AC
		Point3D C = new Point3D(A.x + AC.x, A.y + AC.y, A.z + AC.z); // este es el nuevo punto a devolver 
		System.out.println("New safe position "+C);

		return C;
		
	}
	
	Point3D getNewPosition(Point3D position){
		// método auxiliar para calcular un nuevo punto aleatorio en su zona
		// genera una nueva posición aleatoria en un rango de 40, con coordenada z positiva 
		Random rand = new Random();
        double x = position.getX() + (rand.nextDouble() * 80 - 40 ) + 10; // Rango [-40, 40]
        double y = position.getX() + (rand.nextDouble() * 80 - 40) + 10; // Rango [-40, 40]
        double z = position.getX() +(rand.nextDouble() * 80 - 40) + 10; // Rango [-40, 40]
		
		Point3D randomPoint = new Point3D(x,y,z); // este es el nuevo punto a devolver 
		return randomPoint;
		
	}

    

    


   

    
}
