package logic.scripter.graphs;

import java.util.ArrayList;
import java.util.List;

import logic.scripter.Metric;

public abstract class GraphCommand {

	public static enum GRAPHS {
		BoxPlot, Plot
	};

	private String pdfName;
	private List<Metric> metrics;
	private List<Parameter> parameters;

	public GraphCommand(String pdfName, List<Metric> metrics) {
		this.setPdfName(pdfName);
		this.setMetrics(metrics);
		this.parameters = new ArrayList<Parameter>();
	}

	public GraphCommand(String pdfName, List<Metric> metrics, List<Parameter> params) {
		this.setPdfName(pdfName);
		this.setMetrics(metrics);
		this.setParameters(params);
	}

	public String generateScriptCode() {
		StringBuilder res = new StringBuilder("pdf('").append(this.pdfName).append(".pdf')\n");
		res.append(pdfName + "Device<-dev.cur()\n");
		res.append("png('").append(this.pdfName).append(".png')\n");
		res.append("dev.control('enable')\n");
		res.append(this.execute()).append("\n");
		res.append("dev.copy(which=" + pdfName + "Device)\n");
		res.append("dev.off()\n");
		res.append("dev.off()");
		return res.toString();
	}

	public abstract String execute();

	public List<Metric> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<Metric> metrics) {
		this.metrics = metrics;
	}

	public String getPdfName() {
		return pdfName;
	}

	public void setPdfName(String pdfName) {
		this.pdfName = pdfName;
	}

	public void addParameter(Parameter param) {
		this.parameters.add(param);
	}

	public void addParameter(String name, String value) {
		this.parameters.add(new Parameter(name, value));
	}

	public List<Parameter> getParameters() {
		return this.parameters;
	}

	public void setParameters(List<Parameter> params) {
		if (params == null)
			this.parameters = new ArrayList<Parameter>();
		else
			this.parameters = params;
	}

	protected String stringifyParameters() {
		StringBuilder pars = new StringBuilder("");
		for (Parameter param : this.parameters) {
			pars.append(param.toString());
		}
		return pars.toString();
	}
}
