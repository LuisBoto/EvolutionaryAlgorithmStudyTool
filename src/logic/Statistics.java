package logic;

import java.text.DecimalFormat;
import java.util.List;

import internationalization.Internationalization;
import logic.scripter.Metric;

public class Statistics {

	public static final String[] STATISTICS_BASIC = { Internationalization.get("MAXIMUM"),
			Internationalization.get("MINIMUM"), Internationalization.get("AVERAGE"),
			Internationalization.get("STANDARD_DEVIATION") };

	public static String getStatistic(int statisticCode, List<Metric> metrics) {
		if (metrics.size() <= 0)
			return "";
		double result = -1;
		switch (statisticCode) {
		case 0:
			result = best(metrics, true);
			break;
		case 1:
			result = best(metrics, false);
			break;
		case 2:
			result = average(metrics);
			break;
		case 3:
			result = standardDeviation(metrics);
			break;
		}
		DecimalFormat numberFormat = new DecimalFormat("#.######");
		return numberFormat.format(result);
	}

	private static double best(List<Metric> metrics, boolean isMax) {
		double[] v = getValues(metrics);
		double best = v[0];
		for (double value : v) {
			if (isMax) {
				if (value > best)
					best = value;
			} else if (value < best)
				best = value;
		}
		return best;
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
