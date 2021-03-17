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
	}

	public static String get(String key) {
		return keys.get(key);
	}
}
