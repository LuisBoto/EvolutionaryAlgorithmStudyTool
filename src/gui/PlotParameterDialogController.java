package gui;

import java.util.ArrayList;
import java.util.List;

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
		this.parameters.remove(this.parameters.size()-1);
		this.updateParameterLabel();
	}
	
	public void finish() {
		
	}

}
