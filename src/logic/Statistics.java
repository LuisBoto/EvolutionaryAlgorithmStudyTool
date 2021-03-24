package logic;

import java.text.DecimalFormat;
import java.util.List;

import javax.script.ScriptException;

import internationalization.Internationalization;
import logic.scripter.Metric;
import logic.scripter.RScriptRunner;

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

	public static String getAdvancedStatistic(int statisticCode, List<Metric> metrics) throws ScriptException {
		if (metrics.size() <= 0)
			return Internationalization.get("SELECT_METRIC_ERROR"); 
		switch (statisticCode) {
		case 0: // Normality Test
			return RScriptRunner.normalityTest(metrics.get(0));
		case 1: // Anova
			return "Not implemented yet";
		case 2: // WilcoxF
			if (metrics.size() != 2)
				return Internationalization.get("SELECT_METRIC_TWO_ERROR"); 
			return RScriptRunner.wilcoxonMannTest(metrics.get(0), metrics.get(1), false);
		case 3: // WilcoxT
			if (metrics.size() != 2)
				return Internationalization.get("SELECT_METRIC_TWO_ERROR"); 
			return RScriptRunner.wilcoxonMannTest(metrics.get(0), metrics.get(1), true);
		case 4: // Kruskal
			if (metrics.size() <= 1)
				return Internationalization.get("SELECT_METRIC_TWO_MORE_ERROR"); 
			return RScriptRunner.kruskalWalisTest(metrics);
		case 5: // Friedman
			if (metrics.size() <= 1)
				return Internationalization.get("SELECT_METRIC_TWO_MORE_ERROR"); 
			return RScriptRunner.friedmanTest(metrics);
		}
		return "";
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
