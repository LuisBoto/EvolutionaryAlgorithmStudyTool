package logic.scripter;

import java.util.List;

import logic.scripter.graphs.GraphCommand;

public class Scripter {

	public static String createScript(List<Metric> metrics, List<GraphCommand> graphs) {
		// TODO: Work directory cannot be changed from Renjin
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
