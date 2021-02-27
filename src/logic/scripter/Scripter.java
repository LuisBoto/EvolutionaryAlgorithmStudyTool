package logic.scripter;

import java.util.ArrayList;
import java.util.List;

import logic.scripter.graphs.BoxPlot;
import logic.scripter.graphs.GraphCommand;

public class Scripter {

	public static void main(String[] args) {
		List<String> values = new ArrayList<String>();
		values.add("0.0037121212121212135");
		values.add("0.0037878787878787897");
		values.add("0.003674242424242425");
			
		List<String> values2 = new ArrayList<String>();
		values2.add("0.005681818181818182");
		values2.add("0.005781818181818182");
		values2.add("0.005481818181818182");
		
		Metric averageFitness = new Metric("averageFitness", values);
		Metric bestFitness = new Metric("bestFitness", values2);
		List<Metric> metrics = new ArrayList<Metric>();
		metrics.add(averageFitness);
		metrics.add(bestFitness);
		GraphCommand box = new BoxPlot("autoBoxPlotTest", metrics);
		
		List<GraphCommand> graphs = new ArrayList<GraphCommand>();
		graphs.add(box);
		
		System.out.println(Scripter.createScript("./scripts", metrics, graphs));
			
	}

	public static String createScript(String workDirectory, List<Metric> metrics, List<GraphCommand> graphs) {
		// TODO: Figure out directory to put in here
		StringBuilder script = new StringBuilder("setwd('" + workDirectory + "')\n");

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
