package tsp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class fileClean {

	public static void main(String[] args) throws IOException {
		File resultados = new File("./../../experimentos/");
		 //checklongitudes("./../../cleanedExperimentos/");
		// checklongitudes("./../../experimentos/");
		File[] ficheros = resultados.listFiles();
		for (int i = 0; i < ficheros.length; i++) {
			//System.out.println(loadIndividualFile(ficheros[i].getAbsolutePath()).split("\n").length + " " + ficheros[i].getName());
			//procesaFichero(ficheros[i]);
			procesaNombre(ficheros[i]);
		}

	}

	private static void checklongitudes(String url) {
		File resultados = new File(url);
		File[] ficheros = resultados.listFiles();
		for (int i = 0; i < ficheros.length; i++) {
			if (loadIndividualFile(ficheros[i].getAbsolutePath()).split("\n").length < 102) {
				System.out.println(loadIndividualFile(ficheros[i].getAbsolutePath()).split("\n").length + " "
						+ ficheros[i].getName());
			}
		}
	}

	private static void procesaNombre(File fichero) throws IOException {
		String newName = "./../../renamedExperiments/"+fichero.getName().replace("_execution_", "_GMT_");
		fichero.renameTo(new File(newName));
	}
	
	private static void procesaFichero(File fichero) throws IOException {
		String contents = loadIndividualFile(fichero.getAbsolutePath());
		int filas = contents.split("\n").length;
		if (filas < 102)
			return;
		FileWriter fr = new FileWriter("./../../cleanedExperimentos/" + fichero.getName());
		BufferedWriter bf = new BufferedWriter(fr);
		if (filas == 102) {
			bf.write(contents);
		}

		if (filas > 102) {
			for (int i = 0; i < 102; i++) {
				bf.write(contents.split("\n")[i] + "\n");
			}
		}

		bf.close();
		fr.close();
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

}
