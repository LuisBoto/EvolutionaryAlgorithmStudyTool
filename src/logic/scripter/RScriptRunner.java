package logic.scripter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class RScriptRunner {

	public static void runRScript(String script) {
		ScriptEngineManager factory = new ScriptEngineManager();
		// Create a Renjin engine
		ScriptEngine engine = factory.getEngineByName("Renjin");
		String[] lines = script.split("\n");
		try {
			for (int i = 0; i < lines.length; i++)
				engine.eval(lines[i]);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

}
