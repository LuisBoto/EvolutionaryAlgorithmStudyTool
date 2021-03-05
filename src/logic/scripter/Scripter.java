package logic.scripter;

import java.util.ArrayList;
import java.util.List;

import logic.fileManager.FileParser;
import logic.scripter.graphs.BoxPlot;
import logic.scripter.graphs.GraphCommand;

public class Scripter {

	public static void main(String[] args) {
		FileParser.parseMetrics("./executionResults/merged/resumen27 Feb 2021 09-27-28 GMT.csv");
		List<Metric> metrics = FileParser.getParsedMetrics();

		GraphCommand box = new BoxPlot("autoBoxPlotTest.pdf", metrics);

		List<GraphCommand> graphs = new ArrayList<GraphCommand>();
		graphs.add(box);

		String script = Scripter.createScript(metrics, graphs);
		System.out.println(script);
		RScriptRunner.runRScript(script);
	}

	public static String createScript(List<Metric> metrics, List<GraphCommand> graphs) {
		// Work directory cannot be changed from Renjin
		StringBuilder script = new StringBuilder("");//"setwd('" + workDirectory + "')\n");

		// First declare all metrics that will be used
		for (int i = 0; i < metrics.size(); i++) {
			script.append(metrics.get(i).toString()).append("\n");
		}
		// Then generate all graphs
		for (int i = 0; i < graphs.size(); i++) {
			script.append(graphs.get(i).generateScriptCode()).append("\n");
		}

		return script.toString();
	}

}
