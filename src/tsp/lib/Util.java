package tsp.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

	public static double[] normalize(double[] probDist) {
		int len = probDist.length;
		double total = 0.0;
		for (double d : probDist) {
			total = total + d;
		}

		double[] normalized = new double[len];
		if (total != 0) {
			for (int i = 0; i < len; i++) {
				normalized[i] = probDist[i] / total;
			}
		}

		return normalized;
	}

	public static List<Double> normalize(List<Double> values) {
		double[] valuesAsArray = new double[values.size()];
		for (int i = 0; i < valuesAsArray.length; i++)
			valuesAsArray[i] = values.get(i);
		double[] normalized = normalize(valuesAsArray);
		List<Double> results = new ArrayList<>();
		for (double aNormalized : normalized)
			results.add(aNormalized);
		return results;
	}

	public static List<Double> normalizeFromMeanAndStdev(List<Double> values, double mean, double stdev) {
		return values.stream().map(d -> (d - mean) / stdev).collect(Collectors.toList());
	}

}