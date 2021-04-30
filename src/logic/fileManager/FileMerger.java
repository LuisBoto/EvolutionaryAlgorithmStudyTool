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

	public static int getLineMergeUpperBound(String dir) throws IOException {
		directory = dir;
		cargarFicheros();
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
		cargarFicheros();
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
		guardarFichero(fichero, "resumenUltimaLinea", saveDir);
	}

	public static void mergeByAverage(String dir, String saveDir) throws IllegalArgumentException, IOException {
		// dir = directory containing csv files to be merged
		directory = dir;
		cargarFicheros();
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
		DecimalFormat numberFormat = new DecimalFormat("#.############"); // 12 decimals as upper bound

		for (int i = 1; i < size; i++) { // Current row i
			for (int k = 0; k < columnNumber; k++) { // Current column k
				for (int j = 0; j < fileNames.size(); j++) { // Current file j
					val = fileContents.get(j).split("\n")[i].split(";")[k].replace(',', '.'); // Replacing input commas
					values[j] = Double.parseDouble(val);
				}
				fichero += numberFormat.format(average(values)).replace(',', '.') + ";"; // Replacing output commas
			}
			fichero += "\n";
		}
		guardarFichero(fichero, "resumenPromedios", saveDir);
	}

	private static List<String> cargarFicheros() throws IOException {
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
			fileContents.add(cargaFichero(fichero));
		if (fileContents.size() <= 0 || fileNames.size() <= 0)
			throw new IllegalArgumentException("Directory contains no valid files");
		return fileContents;
	}

	private static String cargaFichero(String fichero) {
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
	private static void guardarFichero(String contents, String name, String saveDir) throws IOException {
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

}
