package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import logic.scripter.Metric;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.border.BevelBorder;
import javax.swing.JTextArea;
import java.awt.GridLayout;
import javax.swing.JTable;
import javax.swing.ImageIcon;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 4085389089535850911L;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnHelp;
	private JMenuItem menuFileExit;
	private JMenuItem menuFileNew;
	private JSeparator fileMenuSeparator;
	private JMenuItem mntmAbout;
	private JPanel openFileButtonPn;
	private JPanel scriptGenerationButtonPn;
	private JButton btnOpenFile;
	private JButton btnMergeFiles;
	private JButton btnGenerateScript;
	private JButton btnRunScript;
	private JButton btnExportScript;
	private JPanel mainPn;
	private JPanel plotsPn;
	private JPanel scriptPn;
	private JPanel statisticsPreviewPn;
	private JLabel lblFile;
	private JPanel metricsPlotsPn;
	private JPanel metricSelectPn;
	private JPanel plotsSelectPn;
	private JPanel plotManagerPn;
	private JPanel plotButtonsPn;
	private JButton btnAddPlot;
	private JButton btnRemovePlot;
	private JPanel plotListPn;
	private JLabel lblPlots;
	private JPanel scriptEditPn;
	private JLabel lblGeneratedScript;
	private JButton btnEditSave;
	private JTextArea textAreaScript;
	private JPanel statisticsPn;
	private JPanel plotPreviewPn;
	private JLabel lblStatistics;
	private JTable statisticsTable;
	private JLabel lblPlotPreview;
	private JLabel lblPlotImage;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainFrame() {
		setTitle("Evolutive algorithm study tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 702, 469);
		setJMenuBar(getMenuBar_1());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getOpenFileButtonPn(), BorderLayout.NORTH);
		contentPane.add(getMainPn(), BorderLayout.CENTER);
		contentPane.add(getScriptGenerationButtonPn(), BorderLayout.SOUTH);
	}

	private JMenuBar getMenuBar_1() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getMnFile());
			menuBar.add(getMnHelp());
		}
		return menuBar;
	}

	private JMenu getMnFile() {
		if (mnFile == null) {
			mnFile = new JMenu("File");
			mnFile.add(getMenuFileNew());
			mnFile.add(getFileMenuSeparator());
			mnFile.add(getMenuFileExit());
		}
		return mnFile;
	}

	private JMenu getMnHelp() {
		if (mnHelp == null) {
			mnHelp = new JMenu("Help");
			mnHelp.add(getMntmAbout());
		}
		return mnHelp;
	}

	private JMenuItem getMenuFileExit() {
		if (menuFileExit == null) {
			menuFileExit = new JMenuItem("Exit");
			menuFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		}
		return menuFileExit;
	}

	private JMenuItem getMenuFileNew() {
		if (menuFileNew == null) {
			menuFileNew = new JMenuItem("New");
			menuFileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		}
		return menuFileNew;
	}

	private JSeparator getFileMenuSeparator() {
		if (fileMenuSeparator == null) {
			fileMenuSeparator = new JSeparator();
		}
		return fileMenuSeparator;
	}

	private JMenuItem getMntmAbout() {
		if (mntmAbout == null) {
			mntmAbout = new JMenuItem("About...");
			mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
		}
		return mntmAbout;
	}

	private JPanel getOpenFileButtonPn() {
		if (openFileButtonPn == null) {
			openFileButtonPn = new JPanel();
			FlowLayout flowLayout = (FlowLayout) openFileButtonPn.getLayout();
			flowLayout.setAlignment(FlowLayout.LEADING);
			openFileButtonPn.setBorder(new LineBorder(new Color(0, 0, 0)));
			openFileButtonPn.add(getBtnOpenFile());
			openFileButtonPn.add(getBtnMergeFiles());
		}
		return openFileButtonPn;
	}

	private JPanel getScriptGenerationButtonPn() {
		if (scriptGenerationButtonPn == null) {
			scriptGenerationButtonPn = new JPanel();
			scriptGenerationButtonPn.setBorder(new LineBorder(new Color(0, 0, 0)));
			scriptGenerationButtonPn.add(getBtnGenerateScript());
			scriptGenerationButtonPn.add(getBtnRunScript());
			scriptGenerationButtonPn.add(getBtnExportScript());
		}
		return scriptGenerationButtonPn;
	}

	private JButton getBtnOpenFile() {
		if (btnOpenFile == null) {
			btnOpenFile = new JButton("Open File...");
			btnOpenFile.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return btnOpenFile;
	}

	private JButton getBtnMergeFiles() {
		if (btnMergeFiles == null) {
			btnMergeFiles = new JButton("Merge files...");
			btnMergeFiles.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return btnMergeFiles;
	}

	private JButton getBtnGenerateScript() {
		if (btnGenerateScript == null) {
			btnGenerateScript = new JButton("Generate Script");
			btnGenerateScript.setEnabled(false);
		}
		return btnGenerateScript;
	}

	private JButton getBtnRunScript() {
		if (btnRunScript == null) {
			btnRunScript = new JButton("Run Script");
			btnRunScript.setEnabled(false);
		}
		return btnRunScript;
	}

	private JButton getBtnExportScript() {
		if (btnExportScript == null) {
			btnExportScript = new JButton("Export Script");
			btnExportScript.setEnabled(false);
		}
		return btnExportScript;
	}

	private JPanel getMainPn() {
		if (mainPn == null) {
			mainPn = new JPanel();
			mainPn.setLayout(new BoxLayout(mainPn, BoxLayout.X_AXIS));
			mainPn.add(getPlotsPn());
			mainPn.add(getScriptPn());
			mainPn.add(getStatisticsPreviewPn());
		}
		return mainPn;
	}

	private JPanel getPlotsPn() {
		if (plotsPn == null) {
			plotsPn = new JPanel();
			plotsPn.setBorder(new LineBorder(new Color(0, 0, 0)));
			plotsPn.setLayout(new BoxLayout(plotsPn, BoxLayout.Y_AXIS));
			plotsPn.add(getLblFile());
			plotsPn.add(getMetricsPlotsPn());
			plotsPn.add(getPlotManagerPn());
		}
		return plotsPn;
	}

	private JPanel getScriptPn() {
		if (scriptPn == null) {
			scriptPn = new JPanel();
			scriptPn.setBorder(new LineBorder(new Color(0, 0, 0)));
			scriptPn.setLayout(new BoxLayout(scriptPn, BoxLayout.Y_AXIS));
			scriptPn.add(getScriptEditPn());
			scriptPn.add(getTextAreaScript());
		}
		return scriptPn;
	}

	private JPanel getStatisticsPreviewPn() {
		if (statisticsPreviewPn == null) {
			statisticsPreviewPn = new JPanel();
			statisticsPreviewPn.setBorder(new LineBorder(new Color(0, 0, 0)));
			statisticsPreviewPn.setLayout(new BoxLayout(statisticsPreviewPn, BoxLayout.Y_AXIS));
			statisticsPreviewPn.add(getStatisticsPn());
			statisticsPreviewPn.add(getPlotPreviewPn());
		}
		return statisticsPreviewPn;
	}

	private JLabel getLblFile() {
		if (lblFile == null) {
			lblFile = new JLabel("File: None");
			lblFile.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return lblFile;
	}

	private JPanel getMetricsPlotsPn() {
		if (metricsPlotsPn == null) {
			metricsPlotsPn = new JPanel();
			FlowLayout flowLayout = (FlowLayout) metricsPlotsPn.getLayout();
			flowLayout.setVgap(0);
			flowLayout.setHgap(0);
			metricsPlotsPn.add(getMetricSelectPn());
			metricsPlotsPn.add(getPlotsSelectPn());
		}
		return metricsPlotsPn;
	}

	private JPanel getMetricSelectPn() {
		if (metricSelectPn == null) {
			metricSelectPn = new JPanel();
			metricSelectPn.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			metricSelectPn.setLayout(new BoxLayout(metricSelectPn, BoxLayout.Y_AXIS));
		}
		return metricSelectPn;
	}

	private JPanel getPlotsSelectPn() {
		if (plotsSelectPn == null) {
			plotsSelectPn = new JPanel();
			plotsSelectPn.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			plotsSelectPn.setLayout(new BoxLayout(plotsSelectPn, BoxLayout.Y_AXIS));
		}
		return plotsSelectPn;
	}

	private JPanel getPlotManagerPn() {
		if (plotManagerPn == null) {
			plotManagerPn = new JPanel();
			plotManagerPn.setLayout(new BoxLayout(plotManagerPn, BoxLayout.Y_AXIS));
			plotManagerPn.add(getPlotButtonsPn());
			plotManagerPn.add(getPlotListPn());
		}
		return plotManagerPn;
	}

	private JPanel getPlotButtonsPn() {
		if (plotButtonsPn == null) {
			plotButtonsPn = new JPanel();
			plotButtonsPn.add(getBtnAddPlot());
			plotButtonsPn.add(getBtnRemovePlot());
		}
		return plotButtonsPn;
	}

	private JButton getBtnAddPlot() {
		if (btnAddPlot == null) {
			btnAddPlot = new JButton("Add Plot");
			btnAddPlot.setEnabled(false);
		}
		return btnAddPlot;
	}

	private JButton getBtnRemovePlot() {
		if (btnRemovePlot == null) {
			btnRemovePlot = new JButton("Remove Plot");
			btnRemovePlot.setEnabled(false);
		}
		return btnRemovePlot;
	}

	private JPanel getPlotListPn() {
		if (plotListPn == null) {
			plotListPn = new JPanel();
			plotListPn.setLayout(new BoxLayout(plotListPn, BoxLayout.Y_AXIS));
			plotListPn.add(getLblPlots());
		}
		return plotListPn;
	}

	private JLabel getLblPlots() {
		if (lblPlots == null) {
			lblPlots = new JLabel("Plots");
			lblPlots.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return lblPlots;
	}

	private JPanel getScriptEditPn() {
		if (scriptEditPn == null) {
			scriptEditPn = new JPanel();
			scriptEditPn.add(getLblGeneratedScript());
			scriptEditPn.add(getBtnEditSave());
		}
		return scriptEditPn;
	}

	private JLabel getLblGeneratedScript() {
		if (lblGeneratedScript == null) {
			lblGeneratedScript = new JLabel("Generated Script");
		}
		return lblGeneratedScript;
	}

	private JButton getBtnEditSave() {
		if (btnEditSave == null) {
			btnEditSave = new JButton("Edit");
			btnEditSave.setEnabled(false);
		}
		return btnEditSave;
	}

	private JTextArea getTextAreaScript() {
		if (textAreaScript == null) {
			textAreaScript = new JTextArea();
			textAreaScript.setColumns(65);
			textAreaScript.setText("loren ipsum");
			textAreaScript.setEnabled(false);
			textAreaScript.setEditable(false);
		}
		return textAreaScript;
	}

	private JPanel getStatisticsPn() {
		if (statisticsPn == null) {
			statisticsPn = new JPanel();
			statisticsPn.setLayout(new BoxLayout(statisticsPn, BoxLayout.Y_AXIS));
			statisticsPn.add(getLblStatistics());
			statisticsPn.add(new JScrollPane(getStatisticsTable()));
		}
		return statisticsPn;
	}

	private JPanel getPlotPreviewPn() {
		if (plotPreviewPn == null) {
			plotPreviewPn = new JPanel();
			plotPreviewPn.setLayout(new BoxLayout(plotPreviewPn, BoxLayout.Y_AXIS));
			plotPreviewPn.add(getLblPlotPreview());
			plotPreviewPn.add(getLblPlotImage());
		}
		return plotPreviewPn;
	}

	private JLabel getLblStatistics() {
		if (lblStatistics == null) {
			lblStatistics = new JLabel("Statistics");
			lblStatistics.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return lblStatistics;
	}

	private JTable getStatisticsTable() {
		if (statisticsTable == null) {
			String[] columnNames = Metric.STATISTICS;
			//TODO: Figure these out
			Object[][] data = { { "Fitness", 5000, 80, 70 }, { "Crosses", 7000, 90, 76 } };
			JTable table = new JTable(data, columnNames);
			statisticsTable = new JTable(data, columnNames);
		}
		return statisticsTable;
	}
	private JLabel getLblPlotPreview() {
		if (lblPlotPreview == null) {
			lblPlotPreview = new JLabel("Plot preview");
			lblPlotPreview.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return lblPlotPreview;
	}
	private JLabel getLblPlotImage() {
		if (lblPlotImage == null) {
			lblPlotImage = new JLabel("");
			lblPlotImage.setIcon(new ImageIcon(MainFrame.class.getResource("/gui/img/graph.jpg")));
			lblPlotImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return lblPlotImage;
	}
}
