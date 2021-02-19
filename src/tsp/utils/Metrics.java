package tsp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * Stores key-value pairs for efficiency analysis.
 * 
 * @author Ravi Mohan
 * @author Ruediger Lunde
 */
public class Metrics {
	private Hashtable<String, String> hash;
	private HashMap<String, List<String>> map;

	public Metrics() {
		this.hash = new Hashtable<String, String>();
		this.map = new HashMap<String, List<String>>();
	}
	
	//Metric storage methods
	
	public boolean existsMetrics(String name) {
		return map.containsKey(name);
	}
	
	public void createMetric(String name) {
		map.put(name, new ArrayList<String>());
	}

	public void saveMetric(String name, int value) {
		map.get(name).add(Integer.toString(value));
	}
	
	public void saveMetric(String name, double value) {
		map.get(name).add(Double.toString(value));
	}
	
	public void saveMetric(String name, long value) {
		map.get(name).add(Long.toString(value));
	}
	
	public int getIntLastMetric(String name) {
		return Integer.parseInt(map.get(name).get(map.get(name).size()-1));
	}
	
	public double getDoubleLastMetric(String name) {
		return Double.parseDouble(map.get(name).get(map.get(name).size()-1));
	}
	
	public long getLongLastMetric(String name) {
		return Long.parseLong(map.get(name).get(map.get(name).size()-1));
	}
	
	//Individual values storage methods
	
	public void setValue(String name, int i) {
		hash.put(name, Integer.toString(i));
	}

	public void setValue(String name, double d) {
		hash.put(name, Double.toString(d));
	}
	
	public void setValue(String name, long l) {
		hash.put(name, Long.toString(l));
	}
	
	public void incrementIntValue(String name) {
		setValue(name, getInt(name) + 1);
	}

	public int getInt(String name) {
		String value = hash.get(name);
		return value != null ? Integer.parseInt(value) : 0;
	}

	public double getDouble(String name) {
		String value = hash.get(name);
		return value != null ? Double.parseDouble(value) : Double.NaN;
	}

	public long getLong(String name) {
		String value = hash.get(name);
		return value != null ? Long.parseLong(value) : 0l;
	}

	public String getValue(String name) {
		return hash.get(name);
	}

	public Set<String> keySet() {
		return hash.keySet();
	}

	/** Sorts the key-value pairs by key names and formats them as equations. */
	public String toString() {
		TreeMap<String, String> map = new TreeMap<String, String>(hash);
		return map.toString();
	}
}
