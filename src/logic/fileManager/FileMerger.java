package logic.fileManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import logic.scripter.Metric;

public class FileMerger {

	protected static String directory;// = "./executionResults" + "/";
	public static final String MERGED_OUTPUT_FOLDER = "merged/";
	protected static List<String> nombres = new ArrayList<String>();
	protected static List<String> ficheros = new ArrayList<String>();	

	public static void mergeFiles(String dir) {
		// dir = directory containing csv files to be merged
		directory = dir;
		List<Metric> metrics = new ArrayList<Metric>();
		cargarFicheros();
		String[] lineasFichero = ficheros.get(0).split("\n");
		String fichero = "Cabecera;";
		// Adding column names
		for (int j = 0; j < lineasFichero[0].split(";").length; j++)
			fichero += lineasFichero[0].split(";")[j] + ";";
		fichero += "\n";
		// todas las lineas salvo la primera
//		String fichero = lineasFichero[0] + "\n";
		for (int i = 0; i < nombres.size(); i++) {	
			fichero+= nombres.get(i)+";";
			metrics = FileParser.parseMetrics(nombres.get(i));
			for (int k=0; k<metrics.size(); k++) {
				fichero += metrics.get(k).average()+";";
			}
			fichero+="\n";
		}
		guardarFichero(fichero, "resumen");
	}

	private static List<String> cargarFicheros() {
		File file = new File(directory);
		if (file.isDirectory()) {
			File[] ficheros = file.listFiles();
			for (File fichero : ficheros)
				if (!fichero.isDirectory())
					nombres.add(directory + fichero.getName());
		}
		for (String fichero : nombres)
			ficheros.add(cargaFichero(fichero));
		return ficheros;
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
			bw = new BufferedWriter(new FileWriter(new File(
					directory + MERGED_OUTPUT_FOLDER + nombre + new Date().toGMTString().replace(':', '-') + ".csv")));
			bw.write(fichero);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
