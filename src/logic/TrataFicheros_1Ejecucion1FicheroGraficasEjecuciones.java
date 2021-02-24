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

public class TrataFicheros_1Ejecucion1FicheroGraficasEjecuciones {

	protected static String directorio = "resumen" + "/";
	protected static Integer[] columnas = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12 };
	protected static int nEjecuciones = 28; // nEjecuciones debe ser igual para todos los experimentos!
	protected static int filaCabecera = 0; // donde esta la fila con los nombres de las columnas
	protected static int filaDatos;
	protected static int instanteEjecucion = 100; 
	protected static List<String> nombres = new ArrayList<String>();
	protected static List<String> ficheros = new ArrayList<String>();

	public static void main(String[] args) {
		// se cargan el nombre y contenido de los ficheros en el directorio
		cargarFicheros(directorio);
		// se agrupan los ficheros por experimento
		Map<String, List<String[]>> datos = extraerFicheros(nombres, ficheros);
		// para cada una de las columnas se extraen los datos y se guardan en un csv
		for (Integer columna : columnas)
			guardarFichero(fichero(columna, datos), "GC" + columna);
	}

	static String fichero(int columna, Map<String, List<String[]>> datos) {
		Map<String, Double[]> matriz = new HashMap<String, Double[]>();
		// 1- se extraen la fila concreta de cada ejecucion
		for (Entry<String, List<String[]>> entry : datos.entrySet()) {
			// 1- ficheros contiene un String[] por cada fichero con sus filas
			List<String[]> ficheros = entry.getValue();
			// 2- el nombre que tendrá la columna / experimento
			String nombreColumna = entry.getKey().replace(";", "_");
			// 3- los datos son el resultado de la metrica de cada ejecucion
			Double[] datosColumna = new Double[nEjecuciones];
			// 4- 
			for (int i=0; i<datosColumna.length; i++)
				datosColumna[i] = Double.parseDouble(ficheros.get(i)[instanteEjecucion].split(";")[columna]);
			// 5- se guarda la columna de cada experimento
			matriz.put(nombreColumna, datosColumna);
		}
		// 2- una vez se tienen las columnas promedio de cada experimento se guardan
		// como una matriz
		String fichero = "";
		// 1- se genera la cabecera con los nombre
		for (Entry<String, Double[]> entry : matriz.entrySet())
			fichero += entry.getKey() + ";";
		fichero += "\n";
		// 2- se van generan las filas
		for (int fila = 0; fila < nEjecuciones; fila++) { // son las filas
			for (Entry<String, Double[]> entry : matriz.entrySet()) {
				fichero += entry.getValue()[fila] + ";";
			}
			fichero += "\n";
		}
		return fichero;
	}

	static Map<String, List<String[]>> extraerFicheros(List<String> nombres, List<String> datos) {
		Map<String, List<String[]>> datos_agrupados = new HashMap<String, List<String[]>>();
		int nParametros = 0;
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
			datos_agrupados.get(nombre).add(datos.get(i).split("\n"));
		}
		return datos_agrupados;
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
