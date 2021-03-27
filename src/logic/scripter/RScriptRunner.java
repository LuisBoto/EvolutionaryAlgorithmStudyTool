package logic.scripter;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.renjin.eval.EvalException;
import org.renjin.sexp.ListVector;

public class RScriptRunner {

	private static ScriptEngine commonEngine;

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

	public static String normalityTest(Metric metric) throws ScriptException {
		// Only one metric
		cleanEngine();
		commonEngine.eval(metric.toString());
		ListVector res = (ListVector) commonEngine.eval("shapiro.test(" + metric.getName() + ")");
		return "shapiro.test:\np.value=" + res.getElementAsString("p.value"); // P value
	}

	public static String tTest(Metric m1, Metric m2) throws ScriptException {
		cleanEngine();
		List<Metric> metrics = new ArrayList<Metric>();
		metrics.add(m1);
		metrics.add(m2);
		createGroupData(metrics);
		commonEngine.eval("ttest<-pairwise.t.test(data, group)");
		ListVector res = (ListVector) commonEngine.eval("ttest");
		return "pairwise.t.test:\np.value="+res.getElementAsString("p.value");
	}

	public static String kruskalWalisTest(List<Metric> metrics) throws ScriptException {
		/**
		 * @return String containing chi-squared and p value
		 */
		cleanEngine();
		createGroupData(metrics);
		commonEngine.eval("krus<-kruskal.test(data, group)");
		// System.out.println(commonEngine.eval("krus"));
		// commonEngine.eval("df_kruskal<-data.frame(krus$method,krus$data.name,krus$statistic,krus$parameter,krus$p.value)");
		// System.out.println(commonEngine.eval("df_kruskal"));
		// commonEngine.eval("names(df_kruskal)<-c('Method', 'Name', 'chi-squared',
		// 'Statistic', 'p-value')");
		ListVector res = (ListVector) commonEngine.eval("krus");
		return "kruskal.test:\nchi-squared=" + res.getElementAsString("statistic") + ", p.value="
				+ res.getElementAsString("p.value");
	}

	public static String friedmanTest(List<Metric> metrics) throws ScriptException {
		// Al metrics must have same length
		cleanEngine();
		StringBuilder st = new StringBuilder("y = c(");
		for (Metric m : metrics) {
			commonEngine.eval(m.toString());
			st.append(m.getName() + ",");
		}
		st.deleteCharAt(st.length() - 1).append(")");
		commonEngine.eval(st.toString());
		commonEngine.eval("matrixFriedman<-matrix(y, nrow=" + metrics.get(0).getSize() + ", ncol="
				+ metrics.get(0).getSize() + ")");
		commonEngine.eval("fried<-friedman.test(matrixFriedman)");
		// System.out.println(commonEngine.eval("fried"));
		ListVector res = (ListVector) commonEngine.eval("fried");
		return "friedman.test:\np.value=" + res.getElementAsString("p.value");
	}

	public static String wilcoxonMannTest(Metric m1, Metric m2, boolean paired) throws ScriptException {
		/**
		 * @return String containing p-value, pointprob and paired
		 */
		cleanEngine();
		commonEngine.eval(m1.toString());
		commonEngine.eval(m2.toString());
		String pairedText = paired ? "TRUE" : "FALSE";

		ListVector res = (ListVector) commonEngine.eval("test<-wilcox.exact(" + m1.getName() + ", " + m2.getName()
				+ ", paired = " + pairedText + ", exact = T, alternative = 't', conf.int = 0.95)");
		return "wilcox.exact:\np.value=" + res.getElementAsString("p.value") + ", pointprob="
				+ res.getElementAsString("pointprob") + ", paired=" + pairedText;
	}

	private static void cleanEngine() throws ScriptException {
		commonEngine = new ScriptEngineManager().getEngineByName("Renjin");
		commonEngine.eval("rm(list=ls())");
		commonEngine.eval("graphics.off()");
		commonEngine.eval("library(reshape)");
		commonEngine.eval("library(PMCMR)"); // PMCMRplus not available yet on Renjin so PMCMR will do
		commonEngine.eval("library(exactRankTests)");
	}
	
	private static void createGroupData(List<Metric> metrics) throws ScriptException {
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

		commonEngine.eval(group.toString());
		for (Metric m : metrics)
			commonEngine.eval(m.toString());
		commonEngine.eval(data.toString());
	}

}
