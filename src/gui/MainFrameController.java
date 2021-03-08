package gui;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import logic.fileManager.FileParser;
import logic.scripter.Metric;
import logic.scripter.graphs.GraphCommand;

public class MainFrameController {

	private MainFrame mf;

	public MainFrameController(MainFrame mf) {
		this.mf = mf;
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
	}

	public void openFile() {
		JFileChooser fc = new JFileChooser();
		if (fc.showOpenDialog(mf) != JFileChooser.APPROVE_OPTION)
			return;
		// A file has been selected
		//this.initialize();
		File f = fc.getSelectedFile();
		String filepath = f.getPath();
		List<Metric> metrics = FileParser.parseMetrics(filepath);
		this.populateMetricPanel(metrics);
		this.populatePlotSelectPanel();
		this.enableButtons();
	}

	public void populateMetricPanel(List<Metric> metrics) {
		// TODO: Load metrics
		JPanel metricsPanel = mf.getMetricSelectPn();
		for (int i = 0; i < metrics.size(); i++) {
		}
	}

	public void populatePlotSelectPanel() {
		// TODO: Load available plots
		for (int i = 0; i < GraphCommand.GRAPHS.values().length; i++) {

		}
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