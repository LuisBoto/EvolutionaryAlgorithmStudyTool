package logic.scripter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.renjin.eval.EvalException;
import org.renjin.sexp.ListVector;

public class RScriptRunner {

	private static ScriptEngine commonEngine = new ScriptEngineManager().getEngineByName("Renjin");

	public static void main(String[] args) throws EvalException, ScriptException {
		// TODO: Main method to test individual scripts, remove later

		String[] fitness = { "0.0789", "0.0799", "0.0801", "0.0818", "0.0822" };
		Metric m1 = new Metric("Fitness1", Arrays.asList(fitness));
		String[] crosses = { "100", "200", "300", "400", "500" };
		Metric m2 = new Metric("Crosses", Arrays.asList(crosses));
		List<Metric> ms = new ArrayList<Metric>();
		ms.add(m1);
		ms.add(m2);

		// Double r = kruskalWalisTest(ms);
		friedmanTest(ms);
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
		// Only one metric
		cleanEngine();
		commonEngine.eval(metric.toString());
		ListVector res = (ListVector) commonEngine.eval("shapiro.test(" + metric.getName() + ")");
		return res.getElementAsDouble(1); // P value
	}

	public static String kruskalWalisTest(List<Metric> metrics) throws ScriptException {
		/**
		 * @return String containing chi-squared and p value
		 */
		cleanEngine();
		StringBuilder group = new StringBuilder("group<-factor(c(");
		int index = 0;
		// Building group factor with metric names
		for (Metric metric : metrics) {
			group.append("rep('" + metric.getName() + "'," + metric.getSize() + ")");
			if (index != metrics.size() - 1)
				group.append(",");
			else
				group.append("))");
			index++;
		}
		StringBuilder data = new StringBuilder("data<-c(");
		index = 0;
		// Building data array with all metrics
		for (Metric metric : metrics) {
			data.append(metric.getName());
			if (index != metrics.size() - 1)
				data.append(",");
			else
				data.append(")");
			index++;
		}

		group.toString();
		commonEngine.eval(group.toString());
		for (Metric m : metrics)
			commonEngine.eval(m.toString());
		commonEngine.eval(data.toString());

		commonEngine.eval("krus<-kruskal.test(data, group)");
		// System.out.println(commonEngine.eval("krus"));
		// commonEngine.eval("df_kruskal<-data.frame(krus$method,krus$data.name,krus$statistic,krus$parameter,krus$p.value)");
		// System.out.println(commonEngine.eval("df_kruskal"));
		// commonEngine.eval("names(df_kruskal)<-c('Method', 'Name', 'chi-squared',
		// 'Statistic', 'p-value')");
		ListVector res = (ListVector) commonEngine.eval("krus");
		return "chi-squared: " + res.getElementAsDouble(0) + ", p-value: " + res.getElementAsDouble(2);
	}
	
	public static String friedmanTest(List<Metric> metrics) throws ScriptException {
		cleanEngine();
		StringBuilder group = new StringBuilder("group<-factor(c(");
		int index = 0;
		// Building group factor with metric names
		for (Metric metric : metrics) {
			group.append("rep('" + metric.getName() + "'," + metric.getSize() + ")");
			if (index != metrics.size() - 1)
				group.append(",");
			else
				group.append("))");
			index++;
		}
		StringBuilder data = new StringBuilder("data<-c(");
		index = 0;
		// Building data array with all metrics
		for (Metric metric : metrics) {
			data.append(metric.getName());
			if (index != metrics.size() - 1)
				data.append(",");
			else
				data.append(")");
			index++;
		}

		group.toString();
		commonEngine.eval(group.toString());
		for (Metric m : metrics)
			commonEngine.eval(m.toString());
		commonEngine.eval(data.toString());

		commonEngine.eval("fried<-friedman.test(data, group, group)"); // TODO: figure this out
		System.out.println(commonEngine.eval("fried"));
		return "";
	}

	public static String wilcoxonMannTest(Metric m1, Metric m2, boolean paired) throws ScriptException {
		/**
		 * @return String containing p-value, pointprob and paired
		 */
		cleanEngine();
		commonEngine.eval(m1.toString());
		commonEngine.eval(m2.toString());
		String pairedText = paired ? "TRUE" : "FALSE";

		ListVector res = (ListVector) commonEngine.eval("test<-wilcox.exact(" + m1.toString() + ", " + m2.toString()
				+ ", paired = " + pairedText + ", exact = T, alternative = 't', conf.int = 0.95)");
		return "p-value: " + res.getElementAsDouble(2) + ", pointprob: " + res.getElementAsDouble(1) + ", paired: "
				+ pairedText;
	}

	private static void cleanEngine() throws ScriptException {
		commonEngine.eval("rm(list=ls())");
		commonEngine.eval("graphics.off()");
		// commonEngine.eval("library(tidyverse)"); // Seems to be unused for our purposes
		commonEngine.eval("library(reshape)");
		commonEngine.eval("library(PMCMR)"); // PMCMRplus not available yet on Renjin so PMCMR will do
		commonEngine.eval("library(exactRankTests)");
	}

}
