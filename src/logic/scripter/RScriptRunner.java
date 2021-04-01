package logic.scripter;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.renjin.eval.EvalException;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.LogicalArrayVector;

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

	public static ScriptResult normalityTest(Metric metric) throws ScriptException {
		StringBuilder code = new StringBuilder(metric.toString() + "\n");
		code.append("shapiro.test(" + metric.getName() + ")\n");
		StringBuilder result = new StringBuilder("");

		// Only one metric
		cleanEngine();
		commonEngine.eval(metric.toString());
		ListVector res = (ListVector) commonEngine.eval("shapiro.test(" + metric.getName() + ")");
		result.append("shapiro.test:\np.value=" + res.getElementAsString("p.value"));
		return new ScriptResult(code.toString(), result.toString());
	}

	public static ScriptResult tTest(Metric m1, Metric m2) throws ScriptException {
		StringBuilder code = new StringBuilder("");
		StringBuilder result = new StringBuilder("");

		cleanEngine();
		List<Metric> metrics = new ArrayList<Metric>();
		metrics.add(m1);
		metrics.add(m2);
		code = createGroupData(metrics, code);
		code.append("\nttest<-pairwise.t.test(data, group)\n");
		commonEngine.eval("ttest<-pairwise.t.test(data, group)");
		code.append("ttest\n");
		ListVector res = (ListVector) commonEngine.eval("ttest");
		result.append("pairwise.t.test:\np.value=" + res.getElementAsString("p.value"));
		return new ScriptResult(code.toString(), result.toString());
	}

	public static ScriptResult kruskalWalisTest(List<Metric> metrics) throws ScriptException {
		/**
		 * @return String containing chi-squared and p value
		 */
		StringBuilder code = new StringBuilder("");
		StringBuilder result = new StringBuilder("");

		cleanEngine();
		code = createGroupData(metrics, code);
		code.append("\nkrus<-kruskal.test(data, group)\n");
		commonEngine.eval("krus<-kruskal.test(data, group)");
		// System.out.println(commonEngine.eval("krus"));
		// commonEngine.eval("df_kruskal<-data.frame(krus$method,krus$data.name,krus$statistic,krus$parameter,krus$p.value)");
		// System.out.println(commonEngine.eval("df_kruskal"));
		// commonEngine.eval("names(df_kruskal)<-c('Method', 'Name', 'chi-squared',
		// 'Statistic', 'p-value')");
		code.append("krus\n");
		ListVector res = (ListVector) commonEngine.eval("krus");
		result.append("kruskal.test:\nchi-squared=" + res.getElementAsString("statistic") + ", p.value="
				+ res.getElementAsString("p.value"));
		return new ScriptResult(code.toString(), result.toString());
	}

	public static ScriptResult friedmanTest(List<Metric> metrics) throws ScriptException {
		StringBuilder code = new StringBuilder("");
		StringBuilder result = new StringBuilder("");

		// All metrics must have same length
		cleanEngine();
		StringBuilder st = new StringBuilder("y = c(");
		for (Metric m : metrics) {
			code.append(m.toString() + "\n");
			commonEngine.eval(m.toString());
			st.append(m.getName() + ",");
		}
		st.deleteCharAt(st.length() - 1).append(")");
		code.append(st.toString() + "\n");
		commonEngine.eval(st.toString());
		code.append("matrixFriedman<-matrix(y, nrow=" + metrics.get(0).getSize() + ", ncol=" + metrics.get(0).getSize()
				+ ")\n");
		commonEngine.eval("matrixFriedman<-matrix(y, nrow=" + metrics.get(0).getSize() + ", ncol="
				+ metrics.get(0).getSize() + ")");
		code.append("fried<-friedman.test(matrixFriedman)\n");
		commonEngine.eval("fried<-friedman.test(matrixFriedman)");
		// System.out.println(commonEngine.eval("fried"));
		code.append("fried\n");
		ListVector res = (ListVector) commonEngine.eval("fried");
		result.append("friedman.test:\np.value=" + res.getElementAsString("p.value"));
		return new ScriptResult(code.toString(), result.toString());
	}

	public static ScriptResult wilcoxonMannTest(Metric m1, Metric m2, boolean paired) throws ScriptException {
		/**
		 * @return String containing p-value, pointprob and paired
		 */
		StringBuilder code = new StringBuilder("");
		StringBuilder result = new StringBuilder("");

		cleanEngine();
		code.append(m1.toString() + "\n" + m2.toString() + "\n");
		commonEngine.eval(m1.toString());
		commonEngine.eval(m2.toString());
		String pairedText = paired ? "TRUE" : "FALSE";

		code.append("wilcox<-wilcox.exact(" + m1.getName() + ", " + m2.getName() + ", paired = " + pairedText
				+ ", exact = T, alternative = 't', conf.int = 0.95)\n");
		code.append("wilcox\n");
		ListVector res = (ListVector) commonEngine.eval("wilcox<-wilcox.exact(" + m1.getName() + ", " + m2.getName()
				+ ", paired = " + pairedText + ", exact = T, alternative = 't', conf.int = 0.95)");
		result.append("wilcox.exact:\np.value=" + res.getElementAsString("p.value") + ", pointprob="
				+ res.getElementAsString("pointprob") + ", paired=" + pairedText);
		return new ScriptResult(code.toString(), result.toString());
	}

	private static void cleanEngine() throws ScriptException {
		commonEngine = new ScriptEngineManager().getEngineByName("Renjin");
		commonEngine.eval("rm(list=ls())");
		commonEngine.eval("graphics.off()");

		int requireCheck;
		for (String lib : getLibraries()) {
			requireCheck = ((LogicalArrayVector) commonEngine.eval("require(" + lib + ")")).getElementAsRawLogical(0);
			if (requireCheck == 0)
				commonEngine.eval("library(" + lib + ")");
		}
	}

	public static List<String> getLibraries() {
		List<String> libs = new ArrayList<String>();
		libs.add("reshape");
		libs.add("PMCMR"); // PMCMRplus not available yet on Renjin so PMCMR will do
		libs.add("exactRankTests");
		return libs;
	}

	private static StringBuilder createGroupData(List<Metric> metrics, StringBuilder code) throws ScriptException {
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

		code.append(group.toString());
		commonEngine.eval(group.toString());
		for (Metric m : metrics) {
			code.append("\n" + m.toString());
			commonEngine.eval(m.toString());
		}
		code.append("\n" + data.toString());
		commonEngine.eval(data.toString());
		return code; // StringBuilder to save code lines
	}
}
