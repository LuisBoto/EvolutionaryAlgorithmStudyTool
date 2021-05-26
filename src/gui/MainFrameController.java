package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import internationalization.Internationalization;
import logic.Statistics;
import logic.fileManager.FileMerger;
import logic.fileManager.FileParser;
import logic.scripter.Metric;
import logic.scripter.RScriptRunner;
import logic.scripter.ScriptResult;
import logic.scripter.Scripter;
import logic.scripter.graphs.GraphCommand;
import logic.scripter.graphs.GraphFactory;
import logic.scripter.graphs.Parameter;

public class MainFrameController {

	private MainFrame mf;
	private List<Metric> metrics;
	private List<Metric> selectedMetrics;
	private List<String> loadedFileNames;
	private List<GraphCommand> plots;
	private String script;
	private StatisticThread statsThread;

	public MainFrameController(MainFrame mf) {
		this.mf = mf;
		this.metrics = new ArrayList<Metric>();
		this.selectedMetrics = new ArrayList<Metric>();
		this.loadedFileNames = new ArrayList<String>();
		this.plots = new ArrayList<GraphCommand>();
		this.script = "";
		this.statsThread = null;
		updateStatisticsPanel(); // To create empty components
		updateAdvancedStatsButtons();
	}

	public void initialize() {
		this.showPanels(false);
		mf.getBtnGenerateScript().setEnabled(false);
		mf.getBtnRunScript().setEnabled(false);
		mf.getBtnExportScript().setEnabled(false);

		mf.getTextAreaScript().setText(Internationalization.get("NO_SCRIPT_YET"));
		mf.getTextAreaScript().setEditable(false);
		mf.getBtnEditSave().setText(Internationalization.get("EDIT"));
		mf.getBtnEditSave().setEnabled(false);
		mf.getBtnClear().setEnabled(false);

		mf.getBtnAddPlot().setEnabled(false);
		mf.getBtnRemovePlot().setEnabled(false);

		mf.getMetricSelectPn().removeAll();
		mf.getPlotsSelectPn().removeAll();
		mf.getPlotListPn().removeAll();
		mf.getLblPlotImage().setIcon(new ImageIcon(MainFrame.class.getResource("/gui/img/graph.jpg")));
		updateStatisticsPanel(); // To empty previous values
		updateAdvancedStatsButtons();
		mf.getTxtAreaAdvancedStats().setText("");
		mf.getLblStatCalcStatus().setText("");

		this.metrics = new ArrayList<Metric>();
		this.selectedMetrics = new ArrayList<Metric>();
		this.plots = new ArrayList<GraphCommand>();
		this.loadedFileNames = new ArrayList<String>();
		this.updateLoadedFileLabel();
		this.script = "";
		if (this.statsThread != null)
			this.statsThread.interrupt();
		this.statsThread = null;
		Statistics.cleanResults();
	}

	public void openFile() {
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		fc.setCurrentDirectory(new java.io.File("."));
		if (fc.showOpenDialog(mf) != JFileChooser.APPROVE_OPTION)
			return;
		// One file (or more) has been selected
		File[] fs = fc.getSelectedFiles();
		// File f = fc.getSelectedFile();
		for (File f : fs) {
			if (!f.getName().endsWith(".csv")) {
				JOptionPane.showMessageDialog(this.mf, Internationalization.get("NON_SUPPORTED_FORMAT"),
						Internationalization.get("ERROR"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			String filepath = f.getPath();
			this.loadedFileNames.add(f.getName());
			List<Metric> parsedMetrics;
			try {
				parsedMetrics = FileParser.parseMetrics(loadedFileNames.size(), filepath);
				this.populateMetricPanel(parsedMetrics, f.getName());
				this.populatePlotSelectPanel();
				this.updateLoadedFileLabel();
				this.enableButtons();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this.mf, Internationalization.get("ERROR_OPENING_FILE"),
						Internationalization.get("ERROR_READING_FILE"), JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}

	public void updateLoadedFileLabel() {
		if (this.loadedFileNames.size() <= 0) {
			mf.getLblFile().setText(Internationalization.get("FILE_NONE"));
			mf.getLblFile().setToolTipText(null); // Turns off
			return;
		}
		mf.getLblFile().setText(Internationalization.get("LOADED_FILES"));
		StringBuilder names = new StringBuilder("<html><b>" + Internationalization.get("LOADED_FILES") + ":</b><br>");
		for (String name : this.loadedFileNames)
			names.append("-" + name + "<br>");
		names.append("</html>");
		mf.getLblFile().setToolTipText(names.toString());
	}

	public void populateMetricPanel(List<Metric> parsedMetrics, String newMetricsFilename) {
		this.metrics.addAll(parsedMetrics);
		JPanel metricsPanel = mf.getMetricSelectPn();

		if (this.loadedFileNames.size() > 1)
			metricsPanel.add(new JSeparator());

		int maxNameLength = 25;
		int cut = newMetricsFilename.length() < maxNameLength ? newMetricsFilename.length() : maxNameLength;
		String fileNameLabel = newMetricsFilename.substring(0, cut);
		if (newMetricsFilename.length() > maxNameLength)
			fileNameLabel += "...";
		JLabel fileName = new JLabel(fileNameLabel);
		fileName.setFont(new Font("Tahoma", Font.PLAIN, 10));
		metricsPanel.add(fileName);
		for (int i = 0; i < parsedMetrics.size(); i++) {
			Metric currentMetric = parsedMetrics.get(i);
			JCheckBox checkMet = new JCheckBox(currentMetric.getName());
			checkMet.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					// Adding/removing metric to selected metrics
					if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED)
						selectedMetrics.add(currentMetric);
					else
						selectedMetrics.remove(currentMetric);
					updateStatisticsPanel();
					updateAdvancedStatsButtons();
				}
			});
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
		((JRadioButton) plotsPanel.getComponent(0)).setSelected(true);
		this.refreshUI(mf.getMetricsPlotsPn());
	}

	public void enableButtons() {
		this.showPanels(true);
		mf.getBtnAddPlot().setEnabled(true);
		mf.getBtnRemovePlot().setEnabled(true);
		mf.getBtnGenerateScript().setEnabled(true);
	}

	public void removePlot() {
		if (this.plots.size() <= 0)
			return;
		this.plots.remove(this.plots.size() - 1);
		mf.getPlotListPn().remove(mf.getPlotListPn().getComponentCount() - 1);
		this.refreshUI(mf.getPlotManagerPn());
	}

	public void addPlot() {
		List<Metric> plotMetrics = this.getSelectedMetrics(false); // Only uncheck after plot creation
		String plotName = this.getSelectedPlot();
		if (plotName.equals("") || plotMetrics.size() <= 0)
			return;

		PlotParameterDialog dialog = new PlotParameterDialog(this.mf);
		dialog.setLocationRelativeTo(this.mf);
		dialog.setVisible(true);
	}

	protected void createPlotObject(String plotType, String pdfName, List<Metric> metrics, List<Parameter> params) {
		this.plots.add(GraphFactory.createGraphObject(plotType, pdfName, metrics, params));
		this.addPlotLabel(pdfName);
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
		if (mf.getBtnEditSave().getText().equals(Internationalization.get("EDIT"))) {
			mf.getTextAreaScript().setEditable(true);
			mf.getBtnEditSave().setText(Internationalization.get("SAVE"));
			mf.getTextAreaScript().setBackground(new Color(199, 190, 165));
		} else { // Saving functionality
			mf.getTextAreaScript().setEditable(false);
			mf.getBtnEditSave().setText(Internationalization.get("EDIT"));
			this.script = mf.getTextAreaScript().getText();
			mf.getTextAreaScript().setBackground(new Color(255, 247, 230));
		}
	}

	public void generateScript() {
		this.script = Scripter.createScript(this.metrics, this.plots);
		mf.getTextAreaScript().setText(this.script);
		mf.getBtnEditSave().setEnabled(true);
		mf.getBtnClear().setEnabled(true);
		mf.getBtnRunScript().setEnabled(true);
		mf.getBtnExportScript().setEnabled(true);
	}

	public void runScript() {
		if (this.script.equals("") || isScriptEditing())
			return;

		int res = JOptionPane.showConfirmDialog(this.mf, Internationalization.get("EXECUTE_CONFIRMATION"));
		if (res != 0)
			return;
		try {
			RScriptRunner.runRScript(this.script, false);
			JOptionPane.showMessageDialog(this.mf, Internationalization.get("EXECUTION_COMPLETED"),
					Internationalization.get("EXECUTION_COMPLETED_TITLE"), JOptionPane.INFORMATION_MESSAGE);
			showLastPDFPreview();
		} catch (Exception e) {
			this.showExceptionDialog(Internationalization.get("EXECUTION_ERROR_TITLE"),
					Internationalization.get("EXECUTION_ERROR"), e.getMessage());
			return;
		}
	}

	private boolean isScriptEditing() {
		if (!mf.getBtnEditSave().getText().equals(Internationalization.get("EDIT")))
			JOptionPane.showMessageDialog(mf, Internationalization.get("SAVE_FIRST"));
		return !mf.getBtnEditSave().getText().equals(Internationalization.get("EDIT"));
	}

	private void showLastPDFPreview() {
		if (this.plots.size() <= 0) // No plots on script
			return;
		String pathPNG = "./" + this.plots.get(this.plots.size() - 1).getPdfName() + ".png";
		File pngPlot = new File(pathPNG);
		if (!pngPlot.exists()) // No plot file or png generated
			return;
		JLabel label = mf.getLblPlotImage();
		ImageIcon imageIcon = new ImageIcon(pathPNG);
		Image image = imageIcon.getImage(); // transform it
		Image newimg = image.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(newimg); // transform it back
		label.setIcon(imageIcon);
		this.refreshUI(mf.getPlotPreviewPn());
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
		if (isScriptEditing())
			return;
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("."));
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
			JOptionPane.showMessageDialog(this.mf, Internationalization.get("EXPORT_COMPLETED_PATH") + path,
					Internationalization.get("EXPORT_COMPLETED"), JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			this.showExceptionDialog(Internationalization.get("EXPORT_ERROR_TITLE"),
					Internationalization.get("EXPORT_ERROR"), e.getMessage());
		}
	}

	public List<Metric> getSelectedMetrics(boolean unSelectAfter) {
		List<Metric> metricsToReturn = new ArrayList<Metric>(this.selectedMetrics);
		for (Component comp : mf.getMetricSelectPn().getComponents()) {
			if (!(comp instanceof JCheckBox))
				continue;
			if (((JCheckBox) comp).isSelected()) {
				if (unSelectAfter)
					((JCheckBox) comp).setSelected(false);
			}
		}
		return metricsToReturn;
	}

	public String getSelectedPlot() {
		String plotName = "";
		for (int i = 0; i < mf.getPlotsSelectPn().getComponentCount(); i++) {
			if (((JRadioButton) mf.getPlotsSelectPn().getComponent(i)).isSelected()) {
				plotName = ((JRadioButton) mf.getPlotsSelectPn().getComponent(i)).getText();
			}
		}
		return plotName;
	}

	public void updateStatisticsPanel() {
		JPanel statsPanel = mf.getStatisticsPreviewPn();
		statsPanel.removeAll();
		int index = 0;
		List<Metric> selected = getSelectedMetrics(false);
		for (String statistic : Statistics.STATISTICS_BASIC) {
			JLabel label = new JLabel(statistic + ":");
			JTextField txtField = new JTextField(Statistics.getStatistic(index, selected));
			txtField.setEditable(false);
			txtField.setColumns(8);
			label.setLabelFor(txtField);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			statsPanel.add(label);
			statsPanel.add(txtField);
			index++;
		}
		this.refreshUI(statsPanel);
	}

	public void mergeFiles() {
		String[] options = { Internationalization.get("AVERAGE_FILES"), Internationalization.get("MERGE_BY_LINE"),
				Internationalization.get("CANCEL") };
		int optionSelected = JOptionPane.showOptionDialog(this.mf, Internationalization.get("MERGE_METHOD"),
				Internationalization.get("MERGE_METHOD_TITLE"), JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		if (optionSelected == 2) // Cancel
			return;

		// Selecting directory to merge
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle(Internationalization.get("DIRECTORY_TO_MERGE"));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(this.mf) != JFileChooser.APPROVE_OPTION)
			return;
		File directory = chooser.getSelectedFile();
		String mergePath = directory.getPath().replace('\\', '/') + "/";

		try {
			// Selecting where to save merged result
			JFileChooser chooserSave = new JFileChooser();
			chooserSave.setCurrentDirectory(new java.io.File("."));
			chooserSave.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (chooserSave.showSaveDialog(mf) != JFileChooser.APPROVE_OPTION)
				return;
			File fileSave = chooserSave.getSelectedFile();
			String pathSave = fileSave.getPath().replace('\\', '/') + "/";

			// Merging
			if (optionSelected == 0) // Merge by average
				FileMerger.mergeByAverage(mergePath, pathSave);
			if (optionSelected == 1) {// Merge by line
				// Selecting line parameter to merge
				int topBound = FileMerger.getLineMergeUpperBound(mergePath);
				String input = JOptionPane
						.showInputDialog(Internationalization.get("SELECTED_LINE_DIALOG") + topBound + "]");
				if (input == null || input.equals(""))
					return;
				int selectedLine = Integer.parseInt(input);
				FileMerger.mergeByLine(mergePath, pathSave, selectedLine);
			}
			JOptionPane.showMessageDialog(this.mf, Internationalization.get("MERGE_COMPLETED"),
					Internationalization.get("MERGE_COMPLETED_TITLE"), JOptionPane.INFORMATION_MESSAGE);
		} catch (IllegalArgumentException | IOException e) {
			this.showExceptionDialog(Internationalization.get("MERGE_ERROR_TITLE"),
					Internationalization.get("MERGE_ERROR"), e.getMessage());
		}
	}

	public void clearScript() {
		this.script = "";
		mf.getTextAreaScript().setText(Internationalization.get("NO_SCRIPT_YET"));
	}

	private void refreshUI(JPanel panel) {
		panel.repaint();
		panel.validate();
	}

	private void showPanels(boolean show) {
		mf.getPlotButtonsPn().setVisible(show);
		mf.getMetricsPlotsPn().setVisible(show);
		mf.getPreviewPn().setVisible(show);
	}

	public void changeLanguage() {
		String[] options = { Internationalization.get("ENGLISH"), Internationalization.get("SPANISH"),
				Internationalization.get("CANCEL") };
		int optionSelected = JOptionPane.showOptionDialog(this.mf, Internationalization.get("SELECT_LANGUAGE"),
				Internationalization.get("LANGUAGE"), JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
				options, options[0]);
		switch (optionSelected) {
		case 2: // Cancel
			return;
		case 0:
			Internationalization.setEnglish();
			break;
		case 1:
			Internationalization.setSpanish();
			break;
		}
		String[] args = { optionSelected + "" };
		this.mf.dispose();
		MainFrame.main(args);
	}

	public void showAbout() {
		JOptionPane.showMessageDialog(this.mf, Internationalization.get("ABOUT_MESSAGE"),
				Internationalization.get("ABOUT"), JOptionPane.INFORMATION_MESSAGE);
	}

	public void closeProgram() {
		System.exit(0);
	}

	public void calculateAdvancedStatistic(int statisticCode) {
		if (this.statsThread != null) {
			this.statsThread.interrupt();
			// this.statsThread.stop();
		}
		this.statsThread = new StatisticThread(statisticCode, getSelectedMetrics(false));
		mf.getLblStatCalcStatus().setText(Internationalization.get("ONGOING_CALCULATION"));
		this.statsThread.start();
	}

	private void updateAdvancedStatsButtons() {
		JButton normal = mf.getBtnNormality();
		JButton ttest = mf.getBtnTTest();
		JButton wilcoxF = mf.getBtnWilcoxF();
		JButton wilcoxT = mf.getBtnWilcoxT();
		JButton kruskal = mf.getBtnKruskal();
		JButton friedman = mf.getBtnFriedman();

		normal.setEnabled(false);
		ttest.setEnabled(false);
		wilcoxF.setEnabled(false);
		wilcoxT.setEnabled(false);
		kruskal.setEnabled(false);
		friedman.setEnabled(false);

		switch (getSelectedMetrics(false).size()) {
		case 0:
			return;
		case 1:
			normal.setEnabled(true);
			break;
		case 2:
			ttest.setEnabled(true);
			wilcoxT.setEnabled(true);
			wilcoxF.setEnabled(true);
			break;
		default:
			kruskal.setEnabled(true);
			friedman.setEnabled(true);
			break;
		}

		boolean allSelectedAreNormal = true;
		for (Metric m : getSelectedMetrics(false)) {
			if (!m.isNormal())
				allSelectedAreNormal = false;
		}

		if (allSelectedAreNormal) { // Disable non parametric tests
			wilcoxT.setEnabled(false);
			wilcoxF.setEnabled(false);
			kruskal.setEnabled(false);
			friedman.setEnabled(false);
		} else { // Disable parametric tests
			ttest.setEnabled(false);
		}
	}

	public void exportStatisticScript() {
		if (Statistics.getResults().size() <= 0)
			return;
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("."));
		if (fc.showSaveDialog(mf) != JFileChooser.APPROVE_OPTION)
			return;
		File file = fc.getSelectedFile();
		String path = file.getPath();
		FileWriter fw;
		try {
			fw = new FileWriter(path);
			BufferedWriter bf = new BufferedWriter(fw);
			for (ScriptResult sr : Statistics.getResults()) {
				bf.write(sr.toString());
			}
			bf.close();
			fw.close();
			JOptionPane.showMessageDialog(this.mf, Internationalization.get("EXPORT_COMPLETED_PATH") + path,
					Internationalization.get("EXPORT_COMPLETED"), JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			this.showExceptionDialog(Internationalization.get("EXPORT_ERROR_TITLE"),
					Internationalization.get("EXPORT_ERROR"), e.getMessage());
		}
	}

	private class StatisticThread extends Thread {

		private int statCode;
		private List<Metric> selected;

		public StatisticThread(int statCode, List<Metric> selectedMetrics) {
			this.statCode = statCode;
			this.selected = selectedMetrics;
		}

		public void run() {
			try {
				String result = Statistics.getAdvancedStatistic(statCode, selected);
				JTextArea area = mf.getTxtAreaAdvancedStats();
				area.setText(area.getText() + result + "\n");
				mf.getLblStatCalcStatus().setText("");
			} catch (Exception e) {
				if (isInterrupted())
					return;
				e.printStackTrace();
				mf.getLblStatCalcStatus().setText(Internationalization.get("STAT_CALCULATION_ERROR"));
			}
		}
	}
}