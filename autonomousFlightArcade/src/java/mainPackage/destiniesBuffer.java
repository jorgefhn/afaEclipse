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

public class destiniesBuffer implements JsonObject{
	
	// buffer to be sent to Unity 
	public JsonArray pos1;
	public JsonArray pos2;
	
	
	public void setTarget(String droneName, Point3D point) {
		
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder()
    			.add(point.getX())
    			.add(point.getY())
    			.add(point.getZ());
		
		if (droneName == "drone1") {
			this.pos1 = arrayBuilder.build();
		}
		else {
			this.pos2 = arrayBuilder.build();
		}
	
	}
	
	@Override
	public String toString() {
		return pos1.toString()+ "?"+pos2.toString();
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

	// Buffer to be sent 
}
