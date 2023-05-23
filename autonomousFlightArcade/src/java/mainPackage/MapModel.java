 
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
	 
	Point3D getSafePosition(String agent){
		Point3D destiny = new Point3D(0.0,0.0,0.0);
		
		if (agent.equals("drone1"))
		{
			// move towards start point
			destiny.setX(974.0);
			destiny.setY(122.0);
			destiny.setZ(-454.0);
			
		}
		if (agent.equals("drone2"))
		{
			// move towards end point
			destiny.setX(-494.1);
			destiny.setY(100.0);
			destiny.setZ(994.7);
		}
		return destiny;
		
	}
	
	Point3D getNewPosition(String agent,Point3D d1pos, Point3D d2pos){
		
		// Method to calculate the new position. The target of the drone will be the rival's position
		Point3D destiny = new Point3D(0.0,0.0,0.0);

		if (agent.equals("drone1"))
		{
			destiny.setX(d2pos.getX());
			destiny.setY(d2pos.getY());
			destiny.setZ(d2pos.getZ());
			
		}
		if (agent.equals("drone2"))
		{
			destiny.setX(d1pos.getX());
			destiny.setY(d1pos.getY());
			destiny.setZ(d1pos.getZ());
		}
		return destiny;
	}

    

    


   

    
}
