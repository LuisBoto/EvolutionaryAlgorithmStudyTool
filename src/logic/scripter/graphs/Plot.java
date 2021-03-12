package logic.scripter.graphs;

import java.util.List;

import logic.scripter.Metric;

public class Plot extends GraphCommand {

	public Plot(String pdfName, List<Metric> metrics, List<Parameter> params) {
		super(pdfName, metrics, params);
	}

	@Override
	public String execute() {
		// plot method call, should return
		// plot(value1, value2, parameters...)
		StringBuilder res;
		if (this.getMetrics().size() != 2) // TODO: Plot can only use two metrics...
			res = new StringBuilder("#plot(");
		else
			res = new StringBuilder("plot(");
		// Concatenation of data parameters
		int metricSize = this.getMetrics().size();
		for (int i = 0; i < metricSize - 1; i++) {
			res.append(this.getMetrics().get(i).getName().concat(","));
		}
		res.append(this.getMetrics().get(metricSize - 1).getName());
		res.append(this.stringifyParameters() + ")");

		return res.toString();
	}

}
