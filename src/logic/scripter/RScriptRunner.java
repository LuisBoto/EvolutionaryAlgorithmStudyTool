package logic.scripter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class RScriptRunner {

	public static void runRScript(String script) {
		System.out.println("Initializing R parsing engine...");
		ScriptEngineManager factory = new ScriptEngineManager();
		// Create a Renjin engine
		ScriptEngine engine = factory.getEngineByName("Renjin");
		String[] lines = script.split("\n");
		System.out.println("Executing R script");
		try {
			for (int i = 0; i < lines.length; i++)
				engine.eval(lines[i]);
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		System.out.println("R execution finished");
	}

}
