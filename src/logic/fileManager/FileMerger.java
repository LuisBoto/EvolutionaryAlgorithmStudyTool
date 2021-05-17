package logic.fileManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class FileMerger {

	protected static String directory;
	protected static List<String> fileNames;
	protected static List<String> fileContents;
	protected static List<Triple<String, List<String>, List<String>>> fileGroups;

	public static int getLineMergeUpperBound(String dir) throws IOException {
		directory = dir;
		loadFiles();
		int min = fileContents.get(0).split("\n").length - 1;
		for (String contents : fileContents) {
			if (contents.split("\n").length - 1 < min)
				min = contents.split("\n").length - 1;
		}
		return min;
	}

	public static void mergeByLine(String dir, String saveDir, int selectedLine)
			throws IllegalArgumentException, IOException {
		if (selectedLine < 0)
			throw new IllegalArgumentException("Out of bounds line parameter");
		// dir = directory containing csv files to be merged
		directory = dir;
		loadFiles();
		String[] lineasFichero = fileContents.get(0).split("\n");
		String fichero = "Cabecera;";
		// Adding column names
		for (int j = 0; j < lineasFichero[0].split(";").length; j++)
			fichero += lineasFichero[0].split(";")[j] + ";";
		fichero += "\n";
		// Adding selected line
		for (int i = 0; i < fileNames.size(); i++) {
			fichero += fileNames.get(i) + ";"; // File name
			String[] lines = fileContents.get(i).split("\n");
			if (selectedLine >= lines.length)
				throw new IllegalArgumentException("Out of bounds line parameter on file " + fileNames.get(i));
			fichero += lines[selectedLine]; // Line parameter
			fichero += "\n";
		}
		saveFile(fichero, "resumenLinea", saveDir);
	}

	public static void mergeByAverage(String dir, String saveDir) throws IllegalArgumentException, IOException {
		// dir = directory containing csv files to be merged
		directory = dir;
		loadFiles();
		for (Triple<String, List<String>, List<String>> group : fileGroups) {
			fileNames = (List<String>) group.getSecond();
			fileContents = (List<String>) group.getThird();
			mergeIndividualByAverage(saveDir, "mergeAverage_" + group.getFirst());
		}
	}

	private static void mergeIndividualByAverage(String saveDir, String resultFileName) throws IOException {
		String[] lineasFichero = fileContents.get(0).split("\n");
		String fichero = "";

		// Adding column names
		int columnNumber = lineasFichero[0].split(";").length;
		for (int j = 0; j < columnNumber; j++)
			fichero += lineasFichero[0].split(";")[j] + ";";
		fichero += "\n";

		// Checking all files are equal in row number
		int size = fileContents.get(0).split("\n").length;
		for (int i = 1; i < fileNames.size(); i++) {
			if (fileContents.get(i).split("\n").length != size)
				throw new IllegalArgumentException("Files are not equal in row number");
		}

		// Adding average of lines
		double[] values = new double[fileNames.size()];
		String val = "";
		DecimalFormat numberFormat = new DecimalFormat("#.###############"); // 15 decimals as upper bound

		String[] columns;
		for (int i = 1; i < size; i++) { // Current row i
			for (int k = 0; k < columnNumber; k++) { // Current column k
				for (int j = 0; j < fileNames.size(); j++) { // Current file j
					columns = fileContents.get(j).split("\n")[i].split(";");
					if (columns.length != columnNumber)
						throw new IllegalArgumentException("Files are not equal in column number");
					val = columns[k].replace(',', '.'); // Replacing input commas
					values[j] = Double.parseDouble(val);
				}
				fichero += numberFormat.format(average(values)).replace(',', '.') + ";"; // Replacing output commas
			}
			fichero += "\n";
		}
		saveFile(fichero, resultFileName, saveDir);
	}

	private static void loadFiles() throws IOException {
		fileNames = new ArrayList<String>();
		fileContents = new ArrayList<String>();
		File file = new File(directory);
		if (file.isDirectory()) {
			File[] ficheros = file.listFiles();
			for (File fichero : ficheros)
				if (!fichero.isDirectory() && fichero.getName().endsWith(".csv"))
					fileNames.add(directory + fichero.getName());
		}
		for (String fichero : fileNames)
			fileContents.add(loadIndividualFile(fichero));
		if (fileContents.size() <= 0 || fileNames.size() <= 0)
			throw new IllegalArgumentException("Directory contains no valid files");

		// Grouping
		fileGroups = new ArrayList<Triple<String, List<String>, List<String>>>();
		for (String fileName : fileNames) {
			String realName = fileName.split("/")[fileName.split("/").length - 1]; // Removing path ahead
			String fileGroupName = realName.substring(0, realName.length() / 2); // Groups by first half of filename...
			boolean foundGroup = false;
			for (Triple<String, List<String>, List<String>> group : fileGroups) {
				if (group.getFirst().equals(fileGroupName)) {
					group.getSecond().add(fileName);
					group.getThird().add(loadIndividualFile(fileName));
					foundGroup = true;
					break;
				}
			}
			if (!foundGroup) {
				List<String> names = new ArrayList<String>();
				List<String> contents = new ArrayList<String>();
				names.add(fileName);
				contents.add(loadIndividualFile(fileName));
				fileGroups.add(new Triple<String, List<String>, List<String>>(fileGroupName, names, contents));
			}
		}
	}

	private static String loadIndividualFile(String fichero) {
		Scanner kbd;
		String text = "";
		try {
			kbd = new Scanner(new File(fichero));
			while (kbd.hasNext()) {
				text += kbd.nextLine() + "\n";
			}
			kbd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}

	@SuppressWarnings("deprecation")
	private static void saveFile(String contents, String name, String saveDir) throws IOException {
		BufferedWriter bw;
		bw = new BufferedWriter(
				new FileWriter(new File(saveDir + name + " " + new Date().toGMTString().replace(':', '-') + ".csv")));
		bw.write(contents);
		bw.close();

	}

	private static double average(double[] values) {
		double prom = 0.0;
		for (int i = 0; i < values.length; i++)
			prom += values[i];
		return prom / (double) values.length;
	}

	private static class Triple<T, U, V> {

		private T first;
		private U second;
		private V third;

		public Triple(T first, U second, V third) {
			this.first = first;
			this.second = second;
			this.third = third;
		}

		public T getFirst() {
			return first;
		}

		public U getSecond() {
			return second;
		}

		public V getThird() {
			return third;
		}
	}

}
