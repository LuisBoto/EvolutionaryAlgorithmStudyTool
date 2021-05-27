package logic.scripter.graphs;

import java.util.List;

import logic.Statistics;
import logic.scripter.Metric;

public class Plot extends GraphCommand {

	public Plot(String pdfName, List<Metric> metrics, List<Parameter> params) {
		super(pdfName, metrics, params);
	}

	@Override
	public String execute() {
		// plot method call, should return
		// plot(value1, value2, parameters... color)
		// lines(value1, value3, color)
		// legend(x="topleft", legend=c("xxx", "yyy"), fill=c(color, color)) etc
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
		res.append(this.stringifyParameters());

		if (mainCallMetrics == 2) { // Y axis limits parameter
			String ymin = Statistics.getStatistic(1, getMetrics().subList(1, this.getMetrics().size())).replace(',',
					'.');
			String ymax = Statistics.getStatistic(0, getMetrics().subList(1, this.getMetrics().size())).replace(',',
					'.');
			res.append(", type='l', ylim=c(" + ymin + "," + ymax + ")");
			res.append(", col=" + calculateColor(getMetrics().get(1).getName()) + ")"); // Must be 1º position metric
																						// since 0º is axis
		}

		// Concatenation of lines() calls
		if (lines) {
			for (int i = 2; i < this.getMetrics().size(); i++) {
				res.append("\nlines(" + this.getMetrics().get(0).getName() + ", " + this.getMetrics().get(i).getName()
						+ ", col=" + calculateColor(getMetrics().get(i).getName()) + ")");
			}
		}

		// Concatenation of legend call
		res.append("\nlegend(x='topleft', legend=c('" + getMetrics().get(1).getName() + "'");
		for (int i = 2; i < this.getMetrics().size(); i++) {
			res.append(", '" + getMetrics().get(i).getName() + "'");
		}
		res.append("), fill=c(" + calculateColor(getMetrics().get(1).getName()));
		for (int i = 2; i < this.getMetrics().size(); i++) {
			res.append(", " + calculateColor(getMetrics().get(i).getName()));
		}
		res.append("))");

		return res.toString();
	}

	private String calculateColor(String varName) {
		// Return an hexadecimal color string
		String hex = String.format("#%02x%02x%02x", 224, 224, 224);
		if (varName != null && !varName.equals("")) {
			// We'll calculate a color from the given name
			int r = varName.hashCode() % 254;
			r = Math.abs(r);
			int g = varName.substring(1, varName.length()).hashCode() % 254;
			g = Math.abs(g);
			int b = varName.substring(0, varName.length() - 1).hashCode() % 254;
			b = Math.abs(b);
			hex = String.format("'#%02X%02X%02X'", r, g, b);
		}
		return hex;
	}

}
