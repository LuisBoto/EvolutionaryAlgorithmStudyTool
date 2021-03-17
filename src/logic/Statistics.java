package logic;

import java.text.DecimalFormat;
import java.util.List;

import logic.scripter.Metric;

public class Statistics {

	public static final String[] STATISTICS = { "Best", "Average", "Standard Deviation" };

	public static String getStatistic(String name, List<Metric> metrics) {
		if (metrics.size() <= 0)
			return "";
		double result = -1;
		switch (name) {
		case "Best":
			result = best(metrics);
			break;
		case "Average":
			result = average(metrics);
			break;
		case "Standard Deviation":
			result = standardDeviation(metrics);
			break;
		}
		DecimalFormat numberFormat = new DecimalFormat("#.######");
		return numberFormat.format(result);
	}

	private static double best(List<Metric> metrics) {
		double[] v = getValues(metrics);
		double max = 0.0;
		for (double value : v) {
			if (value > max)
				max = value;
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
