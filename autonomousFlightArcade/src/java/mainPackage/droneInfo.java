import java.io.StringReader;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonNumber;



public class droneInfo implements JsonObject{
	
	// default values 

	public int health = 100;
	public int ammo = 100;
	public int charge = 100;
	
	public JsonArray position;

	
	public void setPosition(Point3D point) {
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder()
    			.add(point.getX())
    			.add(point.getY())
    			.add(point.getZ());
		
		this.position = arrayBuilder.build();
	
	}
	
	public void init() {
		
		Point3D origin = new Point3D(0.0,0.0,0.0);
		this.setPosition(origin); 


		
	}
	
	public void setHealthLevel(JsonValue health) {

		int entero = Integer.parseInt(health.toString());
		this.health = entero;
		
	}
	
	public void setChargeLevel(JsonValue charge) {
		int entero = Integer.parseInt(charge.toString());
		this.charge = entero;
		
	}
	
	public void setAmmoLevel(JsonValue ammo) {
		int entero = Integer.parseInt(ammo.toString());
		this.ammo = entero;
		
	}
	
	@Override
	public String toString() {
		return "Health: "+this.health + " Ammo: " + this.ammo + " Charge: " + this.charge;
	}

	

	public ValueType getValueType() {
		// TODO Auto-generated method stub
		return null;
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	public JsonValue get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	public JsonValue put(String key, JsonValue value) {
		// TODO Auto-generated method stub
		return null;
	}

	public JsonValue remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	public void putAll(Map<? extends String, ? extends JsonValue> m) {
		// TODO Auto-generated method stub
		
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<JsonValue> values() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Entry<String, JsonValue>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getBoolean(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getBoolean(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getInt(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getInt(String arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	public JsonArray getJsonArray(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public JsonNumber getJsonNumber(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public JsonObject getJsonObject(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public JsonString getJsonString(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getString(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getString(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isNull(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
