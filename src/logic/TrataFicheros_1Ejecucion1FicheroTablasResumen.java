package logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class TrataFicheros_1Ejecucion1FicheroTablasResumen {

	protected static String directorio = "resumen" + "/";
	protected static int nColumnas;
	protected static int nEjecuciones = 1; // nEjecuciones debe ser igual para todos los experimentos!
	protected static int filaCabecera = 0; // donde esta la fila con los nombres de las columnas
	protected static int filaDatos;
	protected static int numeroTomaTiempos = 30; // numero de tomas de tiempo que tienen los experimentos: todos deben
													// de ser igual
	protected static List<String> nombres = new ArrayList<String>();
	protected static List<String> ficheros = new ArrayList<String>();

	public static void main(String[] args) {
		// se cargan el nombre y contenido de los ficheros en el directorio
		cargarFicheros(directorio);

		for (int i = 1; i <= numeroTomaTiempos; i++) { // la fila de la cabecera se supone que es la cero
			filaDatos = i;
			// se trata el contenido de los ficheros
			String fichero = tratarFicheros(nombres, ficheros);
			// se guardan los datos tratados
			guardarFichero(fichero, "F" + (i));
		}
	}

	static String tratarFicheros(List<String> nombres, List<String> datos) {
		// se extraen los datos concretos y se agrupan
		Map<String, List<String[]>> datos_agrupados = new HashMap<String, List<String[]>>();
		int nParametros = 0;
		String[] columnas = datos.get(0).split("\n")[filaCabecera].split(";");
		String nombre;
		String[] pars;
		for (int i = 0; i < nombres.size(); i++) {
			pars = nombres.get(i).replace(directorio, "").split("_");
			nParametros = pars.length - 2; // porque no consideramos la fecha y hora del experimento
			nombre = "";
			for (int j = 0; j < nParametros; j++)
				nombre += pars[j] + ";";
			if (!datos_agrupados.containsKey(nombre))
				datos_agrupados.put(nombre, new ArrayList<String[]>());
			datos_agrupados.get(nombre).add(datos.get(i).split("\n")[filaDatos].split(";"));
//			System.out.println(datos.get(i).split("\n")[filaDatos-1]);
		}

		// se tratan los datos: best, avg, ... todas las metricas
		String fichero = "";
//		for (int columna = 0; columna < columnas.length - 2; columna++) { // la ultima y penultima columna son la  regla y el monticulo
		for (int columna = 0; columna < columnas.length; columna++) { 
//			if (columna == 0) {
//				for (int i = 0; i < nParametros; i++)
//					fichero += (";");
//				fichero += ("BestTraining;AvgTraining;SDTraining;");
//			} else if (columna == 1)
//				fichero += ("BestTest;BestTTest;AvgTest;SDTest;");
//			else
				fichero += (columnas[columna] + ";");
		}
		fichero += "\n";

		double value;
		double best_TTest = 0;
		for (Entry<String, List<String[]>> entry : datos_agrupados.entrySet()) {
			fichero += (entry.getKey());
//			for (int columna = 0; columna < columnas.length - 2; columna++) { // la ultima y penultima columna son la regla y el monticulo
			for (int columna = 0; columna < columnas.length; columna++) { 
				double min = Double.MAX_VALUE;
				double avg = 0.0;
				int validas = 0;
				double[] values = new double[nEjecuciones];
				for (int ejecucion = 0; ejecucion < nEjecuciones; ejecucion++) {
					try {
						value = Double.parseDouble(entry.getValue().get(ejecucion)[columna]);
//						if (columna < 2) { // Training o Test
//							if (value < min) {
//								min = value;
//								if (columna == 0) // training
//									// tardiness test de la mejor regla en entrenamiento
//									best_TTest = Double.parseDouble(entry.getValue().get(ejecucion)[1]);
//							}
//						}
						avg += value;
						values[ejecucion] = value;
						validas++;
					} catch (Exception e) {
//						System.out.println("-->"+ejecucion +" "+columna);
//						value = Double.MAX_VALUE;
					}
				}
//				if (columna < 2) // BEST Training o Test
//					fichero += (min + ";");
//				if (columna == 1) // BEST T Test
//					fichero += (best_TTest + ";");
				fichero += (avg / validas + ";"); // AVG
//				if (columna < 2) // SD Training o Test
//					fichero += (desviacion(values) + ";");
//				if (columna == (columnas.length - 2))
//					fichero += validas + ";";			
			}
			fichero += "\n";
		}
		return fichero;
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

	static void cargarFicheros(String directorio) {
		// se carga el directorio donde estan los ficheros
		File file = new File(directorio);
		// se saca el nombre de cada fichero
		if (file.isDirectory()) {
			File[] ficheros = file.listFiles();
			for (File fichero : ficheros)
				nombres.add(directorio + fichero.getName());
		}
		// se carga el contenido de cada fichero
		for (String fichero : nombres)
			ficheros.add(cargaFichero(fichero));
	}

	static String cargaFichero(String fichero) {
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

	static void guardarFichero(String fichero, String nombre) {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(new File(nombre + ".csv")));
			bw.write(fichero);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
