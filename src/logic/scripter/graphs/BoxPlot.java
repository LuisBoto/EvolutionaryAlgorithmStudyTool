package logic.scripter.graphs;

import java.util.List;

import logic.scripter.Metric;

public class BoxPlot extends GraphCommand {

	public BoxPlot(String pdfName, List<Metric> metrics) {
		super(pdfName, metrics);
	}

	@Override
	public String execute() {
		// Boxplot method call, should return
		// boxplot(value, value..., names=c('value','value'))
		StringBuilder res = new StringBuilder("boxplot(");
		// Concatenation of data parameters
		for (int i = 0; i < this.getMetrics().size(); i++) {
			res.append(this.getMetrics().get(i).getName().concat(","));
		}
		// Concatenation of data names
		res.append(" names = c('");
		for (int i = 0; i < this.getMetrics().size() - 1; i++) {
			res.append(this.getMetrics().get(i).getName().concat("','"));
		}
		res.append(this.getMetrics().get(this.getMetrics().size() - 1).getName()).append("')");
		res.append(this.stringifyParameters()+")");
		
		return res.toString();
	}

}
