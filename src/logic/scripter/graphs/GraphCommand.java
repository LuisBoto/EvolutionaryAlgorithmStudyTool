package logic.scripter.graphs;

import java.util.List;

import logic.scripter.Metric;

public abstract class GraphCommand {

	public static enum GRAPHS {BoxPlot, Plot};
	private String pdfName;
	private List<Metric> metrics;

	public GraphCommand(String pdfName, List<Metric> metrics) {
		this.setPdfName(pdfName);
		this.setMetrics(metrics);
	}
	
	public String generateScriptCode() {
		StringBuilder res = new StringBuilder("pdf('").append(this.pdfName).append("')\n");
		res.append(this.execute()).append("\n");
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

}
