package logic.fileManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import logic.scripter.Metric;

public class FileParser {

	private static List<Metric> parsedMetrics = new ArrayList<Metric>();

	public static List<Metric> parseMetrics(int id, String fileUrl) throws IOException {
		parsedMetrics = new ArrayList<Metric>();
		FileReader fr;

		fr = new FileReader(fileUrl);
		BufferedReader bf = new BufferedReader(fr);
		String line = bf.readLine();
		// First line is composed of metric names
		for (int i = 0; i < line.split(";").length; i++) {
			parsedMetrics.add(new Metric(line.split(";")[i].replace(" ", "_") + id + i));
		}
		// Now onto parsing values
		while ((line = bf.readLine()) != null) {
			String[] pieces = line.split(";");
			for (int i = 0; i < pieces.length; i++) {
				parsedMetrics.get(i).addValue(pieces[i]);
			}
		}
		validateParsedMetrics();
		bf.close();
		fr.close();
		return parsedMetrics;
	}

	private static void validateParsedMetrics() {
		for (int i = 0; i < parsedMetrics.size(); i++) {
			Metric metric = parsedMetrics.get(i);
			if (metric.getSize() <= 0) {
				i = removeMetric(i);
				continue;
			}

			for (int j = 0; j < metric.getValues().size(); j++) {
				if (!isNumeric(metric.getValues().get(j)) || metric.getValues().get(j).equals("")) {
					i = removeMetric(i);
					break;
				}
			}
		}
	}

	private static int removeMetric(int index) {
		System.out.println("Removing " + parsedMetrics.get(index).getName());
		parsedMetrics.remove(index);
		return index - 1;
	}

	public static boolean isNumeric(String value) {
		// Checks if a metric value is numeric
		return value.matches("-?\\d+(\\.\\d+)?(E.+)?");
	}
}
