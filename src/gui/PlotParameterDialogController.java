package gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import logic.scripter.graphs.Parameter;

public class PlotParameterDialogController {

	private PlotParameterDialog pd;
	private List<Parameter> parameters;

	public PlotParameterDialogController(PlotParameterDialog pd) {
		this.pd = pd;
		this.parameters = new ArrayList<Parameter>();
	}

	public void addParameter() {
		String name = this.pd.getTxtParameterName().getText();
		String value = this.pd.getTxtParameterValue().getText();
		if (name.equals("") || value.equals(""))
			return;
		this.parameters.add(new Parameter(name, value));
		this.pd.getTxtParameterName().setText("");
		this.pd.getTxtParameterValue().setText("");
		this.updateParameterLabel();
	}
	
	public void updateParameterLabel() {
		StringBuilder stb = new StringBuilder("Parameters: ");
		for (Parameter param: this.parameters)
			stb.append(param.toString());
		this.pd.getTxtParameterList().setText(stb.toString());
	}
	
	public void removeParameter() {
		if (this.parameters.size()<=0)
			return;
		this.parameters.remove(this.parameters.size()-1);
		this.updateParameterLabel();
	}
	
	public void finish() {
		String pdfName = this.pd.getTxtName().getText();
		if (pdfName.equals("")) {
			JOptionPane.showMessageDialog(this.pd, "Please input a plot name", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		MainFrameController mfControl = this.pd.getMainFrame().getController();
		mfControl.createPlotObject(mfControl.getSelectedPlot(), pdfName, mfControl.getSelectedMetrics(true), this.parameters);
		this.pd.dispose();
	}
	
	public void cancel() {
		this.pd.dispose();
	}

}
