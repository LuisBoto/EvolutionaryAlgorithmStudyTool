package logic.scripter;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.renjin.eval.EvalException;

public class RScriptRunner {

	private static List<String> auxiliarLines = new ArrayList<String>();
	private static boolean saving = false;

	public static void runRScript(String script) throws ScriptException, EvalException {
		System.out.println("Initializing R parsing engine...");
		ScriptEngineManager factory = new ScriptEngineManager();
		// Create a Renjin engine
		ScriptEngine engine = factory.getEngineByName("Renjin");
		String[] lines = script.split("\n");
		System.out.println("Executing R script");
		for (int i = 0; i < lines.length; i++) {
			if (saving)
				auxiliarLines.add(lines[i]);
			if (lines[i].contains("pdf(") && !saving) {
				String name = lines[i].replace("pdf", "png");
				auxiliarLines.add(name);
				saving = true;
			}

			engine.eval(lines[i]);

			if (lines[i].contains("dev.off") && saving) {
				saving = false;
				for (String lineAux : auxiliarLines)
					engine.eval(lineAux);
				auxiliarLines = new ArrayList<String>();
			}
		}
		System.out.println("R execution finished");
	}

}
