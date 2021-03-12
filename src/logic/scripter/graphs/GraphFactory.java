package logic.scripter.graphs;

import java.util.List;

import logic.scripter.Metric;

public class GraphFactory {

	public static GraphCommand createGraphObject(String graphType, String pdfName, List<Metric> plotMetrics, List<Parameter> params) {
		// Factory method that creates GraphCommand objects
		switch (graphType) {
		case "BoxPlot":
			return new BoxPlot(pdfName, plotMetrics, params);
		case "Plot":
			return new Plot(pdfName, plotMetrics, params);
		default:
			return null;
		}
	}

}
