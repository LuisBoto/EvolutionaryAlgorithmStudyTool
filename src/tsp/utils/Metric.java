package tsp.utils;

public class Metric {

	private int valueInt;
	private double valueDouble;
	private long valueLong;
	private int type;
	
	public Metric(int value) {
		this.valueInt = value;
		this.type = 0;
	}
	
	public Metric(double value) {
		this.valueDouble = value;
		this.type = 1;
	}
	
	public Metric(long value) {
		this.valueLong = value;
		this.type = 2;
	}
	
	public void setValue(int val) {
		this.valueInt = val;
	}
	
	public void setValue(double val) {
		this.valueDouble = val;
	}
	
	public void setValue(long val) {
		this.valueLong = val;
	}
	
	public int getType() {
		return this.type;
	}
	
	public int getValueInt() {
		return this.valueInt;
	}
	
	public double getValueDouble() {
		return this.valueDouble;
	}
	
	public long getValueLong() {
		return this.valueLong;
	}
	
}
