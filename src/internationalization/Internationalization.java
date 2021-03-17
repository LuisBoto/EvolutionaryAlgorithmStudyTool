package internationalization;

import java.util.HashMap;

public class Internationalization {

	public static HashMap<String, String> keys = new HashMap<String, String>();

	public static HashMap<String, String> setEnglish() {
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
		
		return keys;
	}
}
