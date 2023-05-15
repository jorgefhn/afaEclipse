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



public class droneInfo implements JsonObject{
	
	// default values 

	public float health = 100f;
	public float ammo = 100f;
	public float charge = 100f;
	public float speed = 20f; // by default
	
	
	public JsonArray position;

	
	public void setPosition(String pointString) {
		
		Point3D point = new Point3D(0.0,0.0,0.0);
		point.toPoint3D(pointString);
		
		
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder()
    			.add(point.getX())
    			.add(point.getY())
    			.add(point.getZ());
		
		this.position = arrayBuilder.build();
	
	}
	
	
	
	public void setHealthLevel(JsonValue health) {

		float salud = Float.parseFloat(health.toString());
		this.health = salud;
		
	}
	
	public void setChargeLevel(JsonValue charge) {
		float carga = Float.parseFloat(charge.toString());
		this.charge = carga;
		
	}
	
	public void setAmmoLevel(JsonValue ammo) {
		Float municion = Float.parseFloat(ammo.toString());
		this.ammo = municion;
		
	}
	
	@Override
	public String toString() {
		return "Health: "+this.health + " Ammo: " + this.ammo + " Charge: " + this.charge + " Speed: "+this.speed;
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
