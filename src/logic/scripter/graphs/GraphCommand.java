package logic.scripter.graphs;

import java.util.List;

import logic.scripter.Metric;

public abstract class GraphCommand {

	private List<Metric> metrics;

	public GraphCommand(List<Metric> metrics) {
		this.setMetrics(metrics);
	}
	
	public abstract String execute();

	public List<Metric> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}

}
