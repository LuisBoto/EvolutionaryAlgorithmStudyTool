package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import logic.fileManager.FileParser;
import logic.scripter.Metric;
import logic.scripter.graphs.GraphCommand;

public class MainFrameController {

	private MainFrame mf;
	private List<Metric> metrics;

	public MainFrameController(MainFrame mf) {
		this.mf = mf;
		this.metrics = new ArrayList<Metric>();
	}

	public void initialize() {
		mf.getBtnGenerateScript().setEnabled(false);
		mf.getBtnRunScript().setEnabled(false);
		mf.getBtnExportScript().setEnabled(false);

		mf.getTextAreaScript().setText("No script generated yet");
		mf.getTextAreaScript().setEditable(false);
		mf.getBtnEditSave().setText("Edit");
		mf.getBtnEditSave().setEnabled(false);

		mf.getBtnAddPlot().setEnabled(false);
		mf.getBtnRemovePlot().setEnabled(false);

		mf.getMetricSelectPn().removeAll();
		mf.getPlotsSelectPn().removeAll();
		for (int i = 1; i < mf.getPlotListPn().getComponentCount(); i++)
			mf.getPlotListPn().remove(i);
		
		this.metrics = new ArrayList<Metric>();
	}

	public void openFile() {
		JFileChooser fc = new JFileChooser();
		if (fc.showOpenDialog(mf) != JFileChooser.APPROVE_OPTION)
			return;
		// A file has been selected
		File f = fc.getSelectedFile();
		String filepath = f.getPath();
		List<Metric> parsedMetrics = FileParser.parseMetrics(filepath);
		this.populateMetricPanel(parsedMetrics);
		this.populatePlotSelectPanel();
		this.enableButtons();
	}

	public void populateMetricPanel(List<Metric> parsedMetrics) {
		this.metrics.addAll(parsedMetrics);
		JPanel metricsPanel = mf.getMetricSelectPn();
		metricsPanel.removeAll();
		for (int i = 0; i < this.metrics.size(); i++) {
			JCheckBox checkMet = new JCheckBox(this.metrics.get(i).getName());
			metricsPanel.add(checkMet);
		}
		mf.getPlotsPn().validate();
	}

	public void populatePlotSelectPanel() {
		JPanel plotsPanel = mf.getPlotsSelectPn();
		plotsPanel.removeAll();
		for (int i = 0; i < GraphCommand.GRAPHS.values().length; i++) {
			JRadioButton btnPlot = new JRadioButton(GraphCommand.GRAPHS.values()[i].toString());
			plotsPanel.add(btnPlot);
		}
		mf.getPlotsPn().validate();
	}

	public void enableButtons() {
		mf.getBtnGenerateScript().setEnabled(true);
		mf.getBtnRunScript().setEnabled(true);
		mf.getBtnExportScript().setEnabled(true);

		mf.getBtnEditSave().setEnabled(true);

		mf.getBtnAddPlot().setEnabled(true);
		mf.getBtnRemovePlot().setEnabled(true);
	}

	public void closeProgram() {
		System.exit(0);
	}

}