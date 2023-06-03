 
import javax.json.JsonArray;

/** class that implements the Model of Domestic Robot application */
public class MapModel {

     
   Point3D getPackagePos(JsonArray packageList)
   {
	   System.out.println(packageList);
	   System.out.println(packageList.get(0));

	   Point3D dest = new Point3D(0.0,0.0,0.0);
	   
	   return dest;
	   
   }
	 
	Point3D getSafePositiond1(){
		Point3D destinyd1 = new Point3D(0.0,0.0,0.0);
	
		// d1 to base
		destinyd1.setX(974.0);
		destinyd1.setY(122.0);
		destinyd1.setZ(-454.0);
			
	
		return destinyd1;
		
	}
	
	Point3D getSafePositiond2(){
		Point3D destinyd2 = new Point3D(0.0,0.0,0.0);

	
		// d2 to base
		destinyd2.setX(-494.1);
		destinyd2.setY(100.0);
		destinyd2.setZ(994.7);
			
		return destinyd2;
		
	}
	
	Point3D target(Point3D dpos){
		Point3D destiny = new Point3D(0.0,0.0,0.0);
	
		destiny.setX(dpos.getX());
		destiny.setY(dpos.getY());
		destiny.setZ(dpos.getZ());
			
	
		return destiny;
		
	}
	

	
	
	
}
    

    


   

    
