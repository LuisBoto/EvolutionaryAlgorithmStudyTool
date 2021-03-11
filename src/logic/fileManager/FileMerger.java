package logic.fileManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class FileMerger {

	protected static String directory;
	protected static final String MERGED_OUTPUT_FOLDER = "/merged/";
	protected static List<String> fileNames = new ArrayList<String>();
	protected static List<String> fileContents = new ArrayList<String>();

	public static void mergeByLastLine(String dir) {
		// dir = directory containing csv files to be merged
		directory = dir+"/";
		cargarFicheros();
		String[] lineasFichero = fileContents.get(0).split("\n");
		String fichero = "Cabecera;";
		// Adding column names
		for (int j = 0; j < lineasFichero[0].split(";").length; j++)
			fichero += lineasFichero[0].split(";")[j] + ";";
		fichero += "\n";
		// Adding last line
		for (int i = 0; i < fileNames.size(); i++) {
			fichero += fileNames.get(i) + ";"; // File name
			String[] lines = fileContents.get(i).split("\n");
			fichero += lines[lines.length - 1]; // Last line
			fichero += "\n";
		}
		guardarFichero(fichero, "resumenUltimaLinea");
	}

	public static void mergeByAverage(String dir) throws IllegalArgumentException {
		// dir = directory containing csv files to be merged
		directory = dir+"/";
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
		for (int i = 1; i < size; i++) { // Current row
			for (int k = 0; k < columnNumber; k++) { // Current column
				for (int j = 0; j < fileNames.size(); j++) { // Current file
					values[j] = Double.parseDouble(fileContents.get(j).split("\n")[i].split(";")[k]);
				}
				fichero += average(values) + ";";
			}
			fichero += "\n";
		}
		guardarFichero(fichero, "resumenPromedios");
	}

	private static List<String> cargarFicheros() {
		File file = new File(directory);
		if (file.isDirectory()) {
			File[] ficheros = file.listFiles();
			for (File fichero : ficheros)
				if (!fichero.isDirectory())
					fileNames.add(directory + fichero.getName());
		}
		for (String fichero : fileNames)
			fileContents.add(cargaFichero(fichero));
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
	private static void guardarFichero(String fichero, String nombre) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(new File(directory + MERGED_OUTPUT_FOLDER + nombre + " "
					+ new Date().toGMTString().replace(':', '-') + ".csv")));
			bw.write(fichero);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static double average(double[] values) {
		double prom = 0.0;
		for (int i = 0; i < values.length; i++)
			prom += values[i];
		return prom / (double) values.length;
	}

}
