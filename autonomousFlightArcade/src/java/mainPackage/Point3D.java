


public class Point3D {
    public double x, y, z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
	
	public void toPoint3D(String vector){
		// Function to cast a String input with the form (x,y,z) to Point3D.
		
		if (vector != null) 
		{
		
		System.out.println("Vector: "+vector);
		
		// First, we split the string with commas
		
		String input = vector.substring(1,vector.length()-1); // we eliminate the parenthesis
		String[] coordinates = input.split(",");
		this.x = Double.parseDouble(coordinates[0]);
		this.y = Double.parseDouble(coordinates[1]);
		this.z = Double.parseDouble(coordinates[2]);
		}
		
		}
	
	public double distanceBetweenVectors(Point3D other){
		return Math.sqrt((Math.pow((other.x-this.x),2) + Math.pow((other.y-this.y),2) + Math.pow((other.z-this.z),2)));
	}
	

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }
}
