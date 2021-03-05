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

	protected static String directory;// = "./executionResults" + "/";
	public static final String MERGED_OUTPUT_FOLDER = "merged/";
	protected static List<String> nombres = new ArrayList<String>();
	protected static List<String> ficheros = new ArrayList<String>();

	public static void mergeFiles(String dir) {
		// dir = directory containing csv files to be merged
		directory = dir;
		cargarFicheros();
		String[] lineasFichero = ficheros.get(0).split("\n");
		// solo ultima linea: suponemos que la cabecera esta en la penultima linea; si
		// solo hay dos linea esta en la primera.
//		String fichero = lineasFichero[lineasFichero.length - 2] + "\n";
		String fichero = "Cabecera;";
		// añadimos nombres de las columnas
		for (int j = 0; j < lineasFichero[0].split(";").length; j++)
			fichero += lineasFichero[0].split(";")[j] + ";";
		fichero += "\n";
		// todas las lineas salvo la primera
//		String fichero = lineasFichero[0] + "\n";
		for (int i = 0; i < nombres.size(); i++) {
			lineasFichero = ficheros.get(i).split("\n");
			// solo una linea
//			fichero += nombres.get(i)+";"+lineasFichero[501] + "\n";
			// solo ultima linea
			fichero += nombres.get(i).replace("_", ";") + ";" + lineasFichero[lineasFichero.length - 1] + "\n";
			// todas las lineas salvo la primera
//			for (int l = 1; l < lineasFichero.length; l++) 
//				fichero += nombres.get(i)+";"+lineasFichero[l] + "\n";
		}
		guardarFichero(fichero, "resumen");
	}

	private static List<String> cargarFicheros() {
		File file = new File(directory);
		if (file.isDirectory()) {
			File[] ficheros = file.listFiles();
			for (File fichero : ficheros)
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

	static double desviacion(double[] v) {
		double prom, sum = 0;
		int i, n = v.length;
		prom = promedio(v);
		for (i = 0; i < n; i++)
			sum += Math.pow(v[i] - prom, 2);
		return Math.sqrt(sum / (double) n);
	}

	static double promedio(double[] v) {
		double prom = 0.0;
		for (int i = 0; i < v.length; i++)
			prom += v[i];
		return prom / (double) v.length;
	}

}
