package mainPackage;

import java.util.Random;

/** class that implements the Model of Domestic Robot application */
public class MapModel  {

  
	Point3D getSafePosition(Point3D a, Point3D b){
		// método auxiliar para calcular punto simétrico
		System.out.println("Getting safe position: "+a+" "+b);
		Point3D ab = new Point3D(a.x-b.x,a.y-b.y,a.z-b.z); // coordenadas del vector ab
		Point3D ac = new Point3D(a.x+ 2*ab.x,a.y +2*ab.y,a.z+2*ab.z); // coordenadas del vector ac
		Point3D c = new Point3D(a.x + ac.x, a.y + ac.y, a.z + ac.z); // este es el nuevo punto a devolver 
		System.out.println("New safe position "+c);
		return c;
		
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
    
    
