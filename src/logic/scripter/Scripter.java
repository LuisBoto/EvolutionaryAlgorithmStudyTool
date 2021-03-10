package logic.scripter;

import java.util.ArrayList;
import java.util.List;

import logic.fileManager.FileMerger;
import logic.fileManager.FileParser;
import logic.scripter.graphs.BoxPlot;
import logic.scripter.graphs.GraphCommand;
import logic.scripter.graphs.Plot;

public class Scripter {

	public static void main(String[] args) {
		//FileMerger.mergeByLastLine("./executionResults/");
		//FileMerger.mergeByAverage("./executionResults/");

		List<Metric> metrics = FileParser.parseMetrics("./executionResults/23 Feb 2021 14-44-58 GMT.csv");

		/*GraphCommand box = new BoxPlot("autoBoxPlotTest.pdf", metrics);
		List<Metric> plot2 = new ArrayList<Metric>();
		plot2.add(metrics.get(3));
		plot2.add(metrics.get(4));
		GraphCommand plot = new Plot("autoPlotTest.pdf", plot2);

		List<GraphCommand> graphs = new ArrayList<GraphCommand>();
		graphs.add(box);
		graphs.add(plot);

		String script = Scripter.createScript(metrics, graphs);
		System.out.println(script);
		RScriptRunner.runRScript(script);*/

	}

	public static String createScript(List<Metric> metrics, List<GraphCommand> graphs) {
		// Work directory cannot be changed from Renjin
		StringBuilder script = new StringBuilder("");// "setwd('" + workDirectory + "')\n");

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
