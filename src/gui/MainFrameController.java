package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

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
		mf.getPlotListPn().removeAll();

		this.metrics = new ArrayList<Metric>();
	}

	public void openFile() {
		JFileChooser fc = new JFileChooser();
		if (fc.showOpenDialog(mf) != JFileChooser.APPROVE_OPTION)
			return;
		// A file has been selected
		// TODO: Load file names to file label
		File f = fc.getSelectedFile();
		String filepath = f.getPath();
		this.loadedFileNames.add(f.getName());
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
		this.refreshUI(mf.getMetricsPlotsPn());
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
		this.refreshUI(mf.getMetricsPlotsPn());
	}

	public void enableButtons() {
		// mf.getBtnRunScript().setEnabled(true);
		// mf.getBtnExportScript().setEnabled(true);
		// mf.getBtnEditSave().setEnabled(true);

		mf.getBtnAddPlot().setEnabled(true);
		mf.getBtnRemovePlot().setEnabled(true);
	}

	public void removePlot() {
		// TODO: Remove graph command from list
		mf.getPlotListPn().remove(mf.getPlotListPn().getComponentCount() - 1);
		this.refreshUI(mf.getPlotManagerPn());
	}

	public void addPlot() {
		// TODO: Ask for pdf name/parameters
		List<Metric> plotMetrics = new ArrayList<Metric>();
		for (int i = 0; i < mf.getMetricSelectPn().getComponentCount(); i++) {
			if (((JCheckBox) mf.getMetricSelectPn().getComponent(i)).isSelected())
				plotMetrics.add(this.metrics.get(i));
		}

		String plotName = "";
		for (int i = 0; i < mf.getPlotsSelectPn().getComponentCount(); i++) {
			if (((JRadioButton) mf.getPlotsSelectPn().getComponent(i)).isSelected()) {
				plotName = ((JRadioButton) mf.getPlotsSelectPn().getComponent(i)).getText();
			}
		}

		if (plotName.equals("") || plotMetrics.size() <= 0)
			return;

		this.createGraphObject(plotName, plotName, plotMetrics);
		this.addPlotLabel(plotName);
		mf.getBtnGenerateScript().setEnabled(true);
	}

	public void createGraphObject(String graphType, String pdfName, List<Metric> plotMetrics) {
		// TODO: Create Graph command and add to plot list to send to scripter
	}

	public void addPlotLabel(String name) {
		JPanel plotListPane = mf.getPlotListPn();
		JLabel plotLabel = new JLabel(" " + mf.getPlotListPn().getComponentCount() + ": " + name + " ");
		plotLabel.setOpaque(true);
		plotLabel.setBackground(new Color(255, 204, 255));
		plotLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		plotLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		plotLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		plotListPane.add(plotLabel);
		this.refreshUI(mf.getPlotManagerPn());
	}

	private void refreshUI(JPanel panel) {
		panel.repaint();
		panel.validate();
	}

	public void closeProgram() {
		System.exit(0);
	}

}