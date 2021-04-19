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
		// lines(value1, value3) etc
		StringBuilder res;
		boolean lines = this.getMetrics().size() > 2;
		int mainCallMetrics = 2; // Will either be 2, or 1 on invalid calls
		if (this.getMetrics().size() < 2) {// Plot can only use two metrics minimum...
			res = new StringBuilder("#plot(");
			mainCallMetrics = 1;
		} else
			res = new StringBuilder("plot(");

		// Concatenation of data parameters on main call
		for (int i = 0; i < mainCallMetrics - 1; i++) {
			res.append(this.getMetrics().get(i).getName().concat(","));
		}
		res.append(this.getMetrics().get(mainCallMetrics - 1).getName());
		res.append(this.stringifyParameters() + ")");

		// Concatenation of lines() calls
		if (lines) {
			for (int i = 2; i < this.getMetrics().size(); i++) {
				res.append("\nlines(" + this.getMetrics().get(0).getName() + ", " + this.getMetrics().get(i).getName()
						+ ")");
			}
		}

		return res.toString();
	}

}
