package logic.scripter;

import java.util.ArrayList;
import java.util.List;

public class Metric {

	private String name;
	private List<String> values;

	public Metric(String name, List<String> values) {
		this.name = name;
		this.values = values;
	}

	public Metric(String name) {
		this.name = name;
		this.values = new ArrayList<String>();
	}

	public String getName() {
		return this.name;
	}

	public List<String> getValues() {
		return this.values;
	}
	
	public void addValue(String value) {
		this.values.add(value);
	}

	public int getSize() {
		return this.values.size();
	}

	@Override
	public String toString() {
		// Returns name<-c(value, value, value...) type string
		StringBuilder res = new StringBuilder(this.name).append(" <- c(");
		for (int i = 0; i < this.getSize() - 1; i++) {
			res.append(this.values.get(i)).append(",");
		}
		res.append(this.values.get(this.getSize() - 1)).append(")");
		return res.toString();
	}
	
	private double[] getDoubleValues() {
		double[] valuesDouble = new double[this.values.size()];
		for (int i=0; i<valuesDouble.length; i++) {
			valuesDouble[i] = Double.parseDouble(this.values.get(i));
		}		
		return valuesDouble;
	}
}
