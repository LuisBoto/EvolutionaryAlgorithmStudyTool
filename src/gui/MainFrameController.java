package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import logic.fileManager.FileParser;
import logic.scripter.Metric;
import logic.scripter.RScriptRunner;
import logic.scripter.Scripter;
import logic.scripter.graphs.BoxPlot;
import logic.scripter.graphs.GraphCommand;
import logic.scripter.graphs.Plot;

public class MainFrameController {

	private MainFrame mf;
	private List<Metric> metrics;
	private List<String> loadedFileNames;
	private List<GraphCommand> plots;
	private String script;

	public MainFrameController(MainFrame mf) {
		this.mf = mf;
		this.metrics = new ArrayList<Metric>();
		this.loadedFileNames = new ArrayList<String>();
		this.plots = new ArrayList<GraphCommand>();
		this.script = "";
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
		this.plots = new ArrayList<GraphCommand>();
		this.script = "";
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
		mf.getBtnGenerateScript().setEnabled(true);
	}

	public void removePlot() {
		if (this.plots.size()<=0)
			return;
		this.plots.remove(this.plots.size() - 1);
		mf.getPlotListPn().remove(mf.getPlotListPn().getComponentCount() - 1);
		this.refreshUI(mf.getPlotManagerPn());
	}

	public void addPlot() {
		// TODO: Ask for pdf name/parameters
		List<Metric> plotMetrics = new ArrayList<Metric>();
		for (int i = 0; i < mf.getMetricSelectPn().getComponentCount(); i++) {
			if (((JCheckBox) mf.getMetricSelectPn().getComponent(i)).isSelected()) {
				((JCheckBox) mf.getMetricSelectPn().getComponent(i)).setSelected(false);
				plotMetrics.add(this.metrics.get(i));
			}
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
	}

	private void createGraphObject(String graphType, String pdfName, List<Metric> plotMetrics) {
		// Factory method that creates GraphCommand objects
		switch (graphType) {
		case "BoxPlot":
			this.plots.add(new BoxPlot(pdfName, plotMetrics));
			break;
		case "Plot":
			this.plots.add(new Plot(pdfName, plotMetrics));
			break;
		}
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

	public void editSaveScriptArea() {
		if (mf.getBtnEditSave().getText().equals("Edit")) {
			mf.getTextAreaScript().setEditable(true);
			mf.getBtnEditSave().setText("Save");
			mf.getTextAreaScript().setBackground(new Color(255, 247, 230));
		} else { // Saving functionality
			mf.getTextAreaScript().setEditable(false);
			mf.getBtnEditSave().setText("Edit");
			this.script = mf.getTextAreaScript().getText();
			mf.getTextAreaScript().setBackground(new Color(255, 255, 255));
		}
	}

	public void generateScript() {
		this.script = Scripter.createScript(this.metrics, this.plots);
		mf.getTextAreaScript().setText(this.script);
		mf.getBtnEditSave().setEnabled(true);
		mf.getBtnRunScript().setEnabled(true);
		mf.getBtnExportScript().setEnabled(true);
	}

	public void runScript() {
		int res = JOptionPane.showConfirmDialog(this.mf, "Execute current script?");
		if (res != 0)
			return;
		try {
			RScriptRunner.runRScript(this.script);
			JOptionPane.showMessageDialog(this.mf, "Execution completed successfully", "Script execution completed",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			this.showExceptionDialog("Script execution error", "An exception ocurred while executing the script:",
					e.getMessage());
		}
	}

	private void showExceptionDialog(String title, String message, String exceptionMessage) {
		// Creating a BoxLayout panel with a scrollable textArea to show exception
		// Message on GUI
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel(message));
		panel.add(Box.createRigidArea(new Dimension(10, 10)));
		JScrollPane scroll = new JScrollPane();
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		JTextArea ta = new JTextArea(exceptionMessage);
		ta.setRows(10);
		ta.setColumns(50);
		ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
		ta.setWrapStyleWord(true);
		ta.setLineWrap(true);
		ta.setEditable(false);
		scroll.setViewportView(ta);
		panel.add(scroll);
		JOptionPane.showMessageDialog(this.mf, panel, title, JOptionPane.ERROR_MESSAGE);
	}

	public void exportScript() {
		JFileChooser fc = new JFileChooser();
		if (fc.showSaveDialog(mf) != JFileChooser.APPROVE_OPTION)
			return;
		File file = fc.getSelectedFile();
		String path = file.getPath();
		FileWriter fw;
		try {
			fw = new FileWriter(path);
			BufferedWriter bf = new BufferedWriter(fw);
			bf.write(this.script);
			bf.close();
			fw.close();
			JOptionPane.showMessageDialog(this.mf, "Export completed successfully",
					"Script export completed to " + path, JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			this.showExceptionDialog("Script export error", "An error ocurred exporting the script:", e.getMessage());
		}
	}

	private void refreshUI(JPanel panel) {
		panel.repaint();
		panel.validate();
	}

	public void closeProgram() {
		System.exit(0);
	}

}