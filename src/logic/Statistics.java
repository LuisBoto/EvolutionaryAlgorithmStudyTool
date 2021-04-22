package logic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;

import internationalization.Internationalization;
import logic.scripter.Metric;
import logic.scripter.RScriptRunner;
import logic.scripter.ScriptResult;

public class Statistics {

	public static final String[] STATISTICS_BASIC = { Internationalization.get("MAXIMUM"),
			Internationalization.get("MINIMUM"), Internationalization.get("AVERAGE"),
			Internationalization.get("STANDARD_DEVIATION") };
	private static List<ScriptResult> scriptResults = new ArrayList<ScriptResult>();

	public static String getStatistic(int statisticCode, List<Metric> metrics) {
		if (metrics.size() <= 0)
			return "";
		double result = -1;
		switch (statisticCode) {
		case 0: // Max
			result = best(metrics, true);
			break;
		case 1: // Min
			result = best(metrics, false);
			break;
		case 2: // Average
			result = average(metrics);
			break;
		case 3: // Standard Deviation
			result = standardDeviation(metrics);
			break;
		}
		DecimalFormat numberFormat = new DecimalFormat("#.######");
		return numberFormat.format(result).replace(',', '.');
	}

	public static String getAdvancedStatistic(int statisticCode, List<Metric> metrics) throws ScriptException {
		if (metrics.size() <= 0)
			return Internationalization.get("SELECT_METRIC_ERROR");
		switch (statisticCode) {
		case 0: // Normality Test
			scriptResults.add(RScriptRunner.normalityTest(metrics.get(0)));
			break;
		case 1: // T test
			if (metrics.size() != 2)
				return Internationalization.get("SELECT_METRIC_TWO_ERROR");
			scriptResults.add(RScriptRunner.tTest(metrics.get(0), metrics.get(1)));
			break;
		case 2: // WilcoxF
			if (metrics.size() != 2)
				return Internationalization.get("SELECT_METRIC_TWO_ERROR");
			scriptResults.add(RScriptRunner.wilcoxonMannTest(metrics.get(0), metrics.get(1), false));
			break;
		case 3: // WilcoxT
			if (metrics.size() != 2)
				return Internationalization.get("SELECT_METRIC_TWO_ERROR");
			scriptResults.add(RScriptRunner.wilcoxonMannTest(metrics.get(0), metrics.get(1), true));
			break;
		case 4: // Kruskal
			if (metrics.size() <= 1)
				return Internationalization.get("SELECT_METRIC_TWO_MORE_ERROR");
			scriptResults.add(RScriptRunner.kruskalWalisTest(metrics));
			break;
		case 5: // Friedman
			if (metrics.size() <= 1)
				return Internationalization.get("SELECT_METRIC_TWO_MORE_ERROR");
			if (checkLenght(metrics)) { // All metrics must be of same dimension
				scriptResults.add(RScriptRunner.friedmanTest(metrics));
				break;
			}
			return Internationalization.get("METRIC_LENGTH_ERROR");
		}
		return scriptResults.get(scriptResults.size() - 1).getResult();
	}

	private static boolean checkLenght(List<Metric> metrics) {
		if (metrics.size() <= 0)
			return false;
		int dimension = metrics.get(0).getSize();
		for (Metric m : metrics)
			if (m.getSize() != dimension)
				return false;
		return true;
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

	public static void cleanResults() {
		scriptResults = new ArrayList<ScriptResult>();
	}

	public static List<ScriptResult> getResults() {
		if (scriptResults.size()<=0)
			return new ArrayList<ScriptResult>();
		ArrayList<ScriptResult> copyToReturn = new ArrayList<ScriptResult>(scriptResults);
		ScriptResult firstElement = copyToReturn.get(0);
		copyToReturn.set(0, new ScriptResult(firstElement.getCode(), firstElement.getResult()));
		
		// Appending libraries right at the script's beggining
		StringBuilder libs = new StringBuilder("");
		for (String lib : RScriptRunner.getLibraries()) {
			libs.append("library(" + lib + ")\n");
		}
		libs.append(copyToReturn.get(0).getCode());
		copyToReturn.get(0).setCode(libs.toString());
		return copyToReturn;
	}

}
