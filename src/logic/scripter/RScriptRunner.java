package logic.scripter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.renjin.eval.EvalException;
import org.renjin.sexp.ListVector;

public class RScriptRunner {

	public static void main(String[] args) throws EvalException, ScriptException {
		// Main method to test individual scripts

		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("Renjin");

		engine.eval("require(tidyverse)");
		engine.eval("require(xlsx)");
		engine.eval("require(reshape)");
		engine.eval("require(PMCMRplus)");
		engine.eval("data<-c(1,2,3,4,5,6,7,8,9)");
		ListVector res = (ListVector) engine.eval("shapiro.test(data)");
		System.out.println(res);
		System.out.println(res.getElementAsDouble(1)); // p value
	}

	public static void runRScript(String script) throws ScriptException, EvalException {
		System.out.println("Initializing R parsing engine...");
		ScriptEngineManager factory = new ScriptEngineManager();
		// Create a Renjin engine
		ScriptEngine engine = factory.getEngineByName("Renjin");
		String[] lines = script.split("\n");
		System.out.println("Executing R script");
		for (int i = 0; i < lines.length; i++) {
			engine.eval(lines[i]);
		}
		System.out.println("R execution finished");
	}

	public static double normalityTest(Metric metric) throws ScriptException {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("Renjin");
		engine.eval("require(tidyverse)");
		engine.eval("require(xlsx)");
		engine.eval("require(reshape)");
		engine.eval("require(PMCMRplus)");
		engine.eval(metric.toString());
		ListVector res = (ListVector) engine.eval("shapiro.test(" + metric.getName() + ")");
		return res.getElementAsDouble(1);
	}

}
