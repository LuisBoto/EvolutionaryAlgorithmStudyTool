package logic.fileManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import logic.scripter.Metric;

public class FileParser {

	private static List<Metric> parsedMetrics = new ArrayList<Metric>();

	public static boolean parseMetrics(String fileUrl) {
		parsedMetrics = new ArrayList<Metric>();
		FileReader fr;
		try {
			fr = new FileReader(fileUrl);
			BufferedReader bf = new BufferedReader(fr);
			String line = bf.readLine();
			// First line is composed of metric names
			for (int i = 0; i < line.split(";").length; i++) {
				parsedMetrics.add(new Metric(line.split(";")[i]));
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
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static void validateParsedMetrics() {
		for (int i = 0; i < parsedMetrics.size(); i++) {
			Metric metric = parsedMetrics.get(i);
			for (int j = 0; j < metric.getValues().size(); j++) {
				if (!isNumeric(metric.getValues().get(j))) {
					System.out.println("Removing " + parsedMetrics.get(i).getName());
					parsedMetrics.remove(i);
					i -= 1;
					break;
				}
			}
		}
	}

	private static boolean isNumeric(String value) {
		// Checks if a metric value is numeric
		return value.matches("-?\\d+(\\.\\d+)?");
	}

	public static List<Metric> getParsedMetrics() {
		return parsedMetrics;
	}
}
