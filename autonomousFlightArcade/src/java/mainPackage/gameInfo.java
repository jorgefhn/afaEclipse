
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


public class gameInfo implements JsonObject{
	
	public droneInfo drone1;
	public droneInfo drone2;
	public JsonArray healthPackages;
	public JsonArray ammoPackages;
	public JsonArray chargePackages;
	
	public void init() {
		
		// drone init
		this.drone1 = new droneInfo();
		this.drone2 = new droneInfo();

	}
	
	public void updateHealthPackages(JsonArray hPack) {
		
		
		// create JsonArray
		JsonArrayBuilder healthArrayBuilder = Json.createArrayBuilder();
		
		for (JsonValue h :  hPack) {
			// tratar el paquete de munición y añadirlo a la lista
			addPackage(healthArrayBuilder, h);
		}
		
		this.healthPackages = healthArrayBuilder.build();
		
		
	}

	public void addPackage(JsonArrayBuilder arrayBuilder, JsonValue jsonValue) {
		Point3D p = new Point3D(0.0,0.0,0.0);
		
		
		// string --> Point3D
		p.toPoint3D(jsonValue.toString());
		
		// create JsonObject from Point3D
		JsonObject obj = Json.createObjectBuilder()
				.add("x", p.getX())
				.add("y", p.getY())
				.add("z", p.getZ())
				.build();

		// append to arrayBuilder
		arrayBuilder.add(obj);
	}
	
	public void updateAmmoPackages(JsonArray aPack) {
		
		
		// create JsonArray
		JsonArrayBuilder ammoArrayBuilder = Json.createArrayBuilder();
		
		for (JsonValue a : aPack) {
			// tratar el paquete de munición y añadirlo a la lista
			addPackage(ammoArrayBuilder, a);
		}
		
		this.ammoPackages = ammoArrayBuilder.build();
		
	}
	

	public void updateChargePackages(JsonArray cPack) {
		
		// create JsonArray
		JsonArrayBuilder chargeArrayBuilder = Json.createArrayBuilder();
		
		for (JsonValue c :  cPack) {
			// tratar el paquete de munición y añadirlo a la lista
			addPackage(chargeArrayBuilder, c);
		}
		
		this.chargePackages = chargeArrayBuilder.build();
	
	}

	
	@Override
	public String toString() {
		return "Game Info: drone1=" + drone1 + ", drone2=" + drone2+ " Health Packages: "+healthPackages + " Ammo Packages: "+ammoPackages + " Charge Packages: "+chargePackages;
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
