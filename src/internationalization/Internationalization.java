package internationalization;

import java.util.HashMap;

public class Internationalization {

	private static HashMap<String, String> keys = new HashMap<String, String>();

	public static void setEnglish() {
		keys.clear();
		keys.put("SUBSTANCE_FAIL", "Substance look and feel failed to initialize");
		keys.put("TITLE_TOOLNAME", "Evolutive algorithm study tool");
		keys.put("FILE", "File");
		keys.put("HELP", "Help");
		keys.put("EXIT", "Exit");
		keys.put("NEW", "New");
		keys.put("ABOUT", "About...");
		keys.put("LOAD_FILE", "Load File...");
		keys.put("MERGE_FILES", "Merge Files...");
		keys.put("GENERATE_SCRIPT", "Generate Script");
		keys.put("RUN_SCRIPT", "Run Script");
		keys.put("EXPORT_SCRIPT", "Export Script...");
		keys.put("FILE_NONE", "File: None");
		keys.put("ADD_PLOT", "Add Plot...");
		keys.put("REMOVE_PLOT", "Remove Plot");
		keys.put("PLOTS", "Plots:");
		keys.put("GENERATED_SCRIPT", "Generated Script");
		keys.put("EDIT", "Edit");
		keys.put("NO_SCRIPT_YET", "No script generated yet");
		keys.put("PLOT_PREVIEW", "Plot Preview");
		keys.put("STATISTICS", "Statistics");
		keys.put("CLEAR", "Clear");
		keys.put("NON_SUPPORTED_FORMAT", "Non supported file format");
		keys.put("ERROR", "Error");
		keys.put("ERROR_OPENING_FILE", "An error has ocurred opening the file");
		keys.put("ERROR_READING_FILE", "Error reading file");
		keys.put("LOADED_FILES", "Loaded files");
		keys.put("SAVE", "Save");
		keys.put("EXECUTE_CONFIRMATION", "Execute current script?");
		keys.put("EXECUTION_COMPLETED", "Execution completed successfully");
		keys.put("EXECUTION_COMPLETED_TITLE", "Script execution completed");
		keys.put("EXECUTION_ERROR", "An exception ocurred while executing the script:");
		keys.put("EXECUTION_ERROR_TITLE", "Script execution error");
		keys.put("EXPORT_COMPLETED", "Export completed successfully");
		keys.put("EXPORT_COMPLETED_PATH", "Script export completed to ");
		keys.put("EXPORT_ERROR_TITLE", "Script export error");
		keys.put("EXPORT_ERROR", "An error ocurred exporting the script:");
		keys.put("AVERAGE_FILES", "Average files");
		keys.put("MERGE_BY_LINE", "Merge by last line");
		keys.put("CANCEL", "Cancel");
		keys.put("MERGE_METHOD", "Select file merging method");
		keys.put("MERGE_METHOD_TITLE", "Merging files");
		keys.put("DIRECTORY_TO_MERGE", "Select directory to merge");
		keys.put("MERGE_COMPLETED", "Merging completed successfully");
		keys.put("MERGE_COMPLETED_TITLE", "File merge completed");
		keys.put("MERGE_ERROR", "File merging error");
		keys.put("MERGE_ERROR_TITLE", "An error ocurred performing the merge:");
		keys.put("PLOT_DATA", "Plot data");
		keys.put("FINISH", "Finish");
		keys.put("PLOT_NAME", "Plot name:");
		keys.put("ADD_PARAMETER", "Add parameter");
		keys.put("REMOVE_PARAMETER", "Remove parameter");
		keys.put("PARAMETER_NAME", "Parameter name:");
		keys.put("PARAMETER_VALUE", "Parameter value:");
		keys.put("PARAMETERS", "Parameters: ");
		keys.put("PLOT_NAME_ERROR", "Please input a plot name");
		keys.put("MAXIMUM", "Maximum");
		keys.put("MINIMUM", "Minimum");
		keys.put("AVERAGE", "Average");
		keys.put("STANDARD_DEVIATION", "Standard Deviation");
		keys.put("LANGUAGE", "Language...");
		keys.put("ENGLISH", "English");
		keys.put("SPANISH", "Spanish");
		keys.put("SELECT_LANGUAGE", "Select language");
		keys.put("ABOUT_MESSAGE",
				"Evolutive algorithm study tool.\nUniversity of Oviedo, Software Engineering, end of degree project.\nLuis Boto Fernández 2021");
	}

	public static void setSpanish() {
		keys.clear();
		keys.put("SUBSTANCE_FAIL", "Substance look and feel falló al inicializarse");
		keys.put("TITLE_TOOLNAME", "Herramienta de estudio de algoritmos evolutivos");
		keys.put("FILE", "Archivo");
		keys.put("HELP", "Ayuda");
		keys.put("EXIT", "Salir");
		keys.put("NEW", "Nuevo");
		keys.put("ABOUT", "Acerca de...");
		keys.put("LOAD_FILE", "Cargar Archivo...");
		keys.put("MERGE_FILES", "Unir Archivos...");
		keys.put("GENERATE_SCRIPT", "Generar Script");
		keys.put("RUN_SCRIPT", "Ejecutar Script");
		keys.put("EXPORT_SCRIPT", "Exportar Script...");
		keys.put("FILE_NONE", "Archivo: Ninguno");
		keys.put("ADD_PLOT", "Añadir Gráfico...");
		keys.put("REMOVE_PLOT", "Eliminar Gráfico");
		keys.put("PLOTS", "Gráfico:");
		keys.put("GENERATED_SCRIPT", "Script Generado");
		keys.put("EDIT", "Editar");
		keys.put("NO_SCRIPT_YET", "Ningún script generado aún");
		keys.put("PLOT_PREVIEW", "Vista previa de Gráfico");
		keys.put("STATISTICS", "Estadísticos");
		keys.put("CLEAR", "Limpiar");
		keys.put("NON_SUPPORTED_FORMAT", "Formato de archivo no soportado");
		keys.put("ERROR", "Error");
		keys.put("ERROR_OPENING_FILE", "Ha ocurrido un error abriendo el archivo");
		keys.put("ERROR_READING_FILE", "Error leyendo archivo");
		keys.put("LOADED_FILES", "Ficheros cargados");
		keys.put("SAVE", "Guardar");
		keys.put("EXECUTE_CONFIRMATION", "¿Ejecutar script actual?");
		keys.put("EXECUTION_COMPLETED", "Ejecución completada correctamente");
		keys.put("EXECUTION_COMPLETED_TITLE", "Ejecución completada");
		keys.put("EXECUTION_ERROR", "Ocurrió una excepción durante la ejecución del script:");
		keys.put("EXECUTION_ERROR_TITLE", "Error de ejecución");
		keys.put("EXPORT_COMPLETED", "Script exportado correctamente");
		keys.put("EXPORT_COMPLETED_PATH", "Exportación del script completada a ");
		keys.put("EXPORT_ERROR_TITLE", "Error exportando");
		keys.put("EXPORT_ERROR", "Ha ocurrido un error exportando el script:");
		keys.put("AVERAGE_FILES", "Promedio de ficheros");
		keys.put("MERGE_BY_LINE", "Unir por última línea");
		keys.put("CANCEL", "Cancelar");
		keys.put("MERGE_METHOD", "Seleccionar método de unión");
		keys.put("MERGE_METHOD_TITLE", "Uniendo archivos");
		keys.put("DIRECTORY_TO_MERGE", "Seleciona directorio a unir");
		keys.put("MERGE_COMPLETED", "Unión completada correctamente");
		keys.put("MERGE_COMPLETED_TITLE", "Unión completada");
		keys.put("MERGE_ERROR", "Error de unión");
		keys.put("MERGE_ERROR_TITLE", "Ha ocurrido un error realizando la unión de archivos:");
		keys.put("PLOT_DATA", "Datos de gráfico");
		keys.put("FINISH", "Finalizar");
		keys.put("PLOT_NAME", "Nombre de gráfico:");
		keys.put("ADD_PARAMETER", "Añadir parámetro");
		keys.put("REMOVE_PARAMETER", "Eliminar parámetro");
		keys.put("PARAMETER_NAME", "Nombre parámetro:");
		keys.put("PARAMETER_VALUE", "Valor parámetro:");
		keys.put("PARAMETERS", "Parámetros: ");
		keys.put("PLOT_NAME_ERROR", "Por favor, introduce un nombre de gráfico");
		keys.put("MAXIMUM", "Máximo");
		keys.put("MINIMUM", "Mínimo");
		keys.put("AVERAGE", "Promedio");
		keys.put("STANDARD_DEVIATION", "Desviación estándar");
		keys.put("LANGUAGE", "Idioma...");
		keys.put("ENGLISH", "Inglés");
		keys.put("SPANISH", "Español");
		keys.put("SELECT_LANGUAGE", "Seleccionar idioma");
		keys.put("ABOUT_MESSAGE",
				"Herramienta de estudio de algoritmos evolutivos.\nUniversidad de Oviedo, Ingeniería del Software, trabajo de fin de grado.\nLuis Boto Fernández 2021");
	}

	public static String get(String key) {
		return keys.get(key);
	}
}
