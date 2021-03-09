package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
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
	private List<String> loadedFileNames;

	public MainFrameController(MainFrame mf) {
		this.mf = mf;
		this.metrics = new ArrayList<Metric>();
		this.loadedFileNames = new ArrayList<String>();
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
		mf.getMetricsPlotsPn().repaint();
		mf.getMetricsPlotsPn().validate();
	}

	public void populatePlotSelectPanel() {
		JPanel plotsPanel = mf.getPlotsSelectPn();
		plotsPanel.removeAll();
		ButtonGroup group = new ButtonGroup();
		for (int i = 0; i < GraphCommand.GRAPHS.values().length; i++) {
			JRadioButton btnPlot = new JRadioButton(GraphCommand.GRAPHS.values()[i].toString());
			group.add(btnPlot);
			plotsPanel.add(btnPlot);
		}
		mf.getMetricsPlotsPn().repaint();
		mf.getMetricsPlotsPn().validate();
	}

	public void enableButtons() {
		//mf.getBtnRunScript().setEnabled(true);
		//mf.getBtnExportScript().setEnabled(true);
		//mf.getBtnEditSave().setEnabled(true);

		mf.getBtnAddPlot().setEnabled(true);
		mf.getBtnRemovePlot().setEnabled(true);
	}
	
	public void addPlot() {
		List<Metric> plotMetrics = new ArrayList<Metric>();
		for (int i=0; i<mf.getMetricSelectPn().getComponentCount(); i++) {
			if (((JCheckBox)mf.getMetricSelectPn().getComponent(i)).isSelected())
				plotMetrics.add(this.metrics.get(i));
		}
		System.out.println(plotMetrics.get(0));
		//((JRadioButton)mf.getPlotsSelectPn().getComponent(0)).
		mf.getBtnGenerateScript().setEnabled(true);
	}

	public void closeProgram() {
		System.exit(0);
	}

}