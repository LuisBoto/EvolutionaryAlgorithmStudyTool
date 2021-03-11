package logic;

import java.util.List;

import logic.scripter.Metric;

public class Statistics {

	public static final String[] STATISTICS = { "Best", "Average", "Standard Deviation" };

	public static double getStatistic(String name, List<Metric> metrics) {
		switch (name) {
		case "Best":
			return best(metrics);
		case "Average":
			return average(metrics);
		case "Standard Deviation":
			return standardDeviation(metrics);
		default:
			return -1;
		}
	}
	
	private static double best(List<Metric> metrics) {
		double[] v = getValues(metrics);
		double max = 0.0;
		for (double value:v) {
			if (value > max)
				max=value;
		}
		return max;
	}

	private static double standardDeviation(List<Metric> metrics) {
		double[] v = getValues(metrics);
		double prom, sum = 0;
		int i, n = v.length;
		prom = average(metrics);
		for (i = 0; i < n; i++)
			sum += Math.pow(v[i] - prom, 2);
		return Math.sqrt(sum / (double) n);
	}

	private static double average(List<Metric> metrics) {
		double[] v = getValues(metrics);
		double prom = 0.0;
		for (int i = 0; i < v.length; i++)
			prom += v[i];
		return prom / (double) v.length;
	}

	private static double[] getValues(List<Metric> metrics) {
		int totalSize = 0;
		for (Metric metric : metrics)
			totalSize += metric.getSize();

		double[] res = new double[totalSize];
		int index = 0;
		for (Metric metric : metrics) {
			for (double value : metric.getDoubleValues()) {
				res[index] = value;
				index++;
			}
		}
		return res;
	}

}
