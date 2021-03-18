package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.sun.pdfview.PagePanel;

import internationalization.Internationalization;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 4085389089535850911L;

	// Controller to manage component events and operations
	private MainFrameController controller;

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
	private JPanel previewPn;
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
	private JPanel statisticsPreviewPn;
	private JLabel lblStatistics;
	private PagePanel plotPreviewPn;
	private JLabel lblPlotPreview;
	private JScrollPane metricSelectScrollPane;
	private JScrollPane plotSelectScrollPane;
	private JScrollPane plotListScrollPane;
	private JScrollPane textAreaScrollPane;
	private JButton btnClear;
	private JMenuItem mntmLanguage;
	private JSeparator helpMenuSeparator;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (args.length != 0) { // Language parameter is present
						switch (args[0]) {
						case "1": // Spanish
							Internationalization.setSpanish();
							break;
						default: // English
							Internationalization.setEnglish();
							break;
						}
					} else
						Internationalization.setEnglish();

					try {
						UIManager.setLookAndFeel(
								"org.pushingpixels.substance.api.skin.SubstanceNebulaBrickWallLookAndFeel");
					} catch (Exception e) {
						System.out.println(Internationalization.get("SUBSTANCE_FAIL"));
					}

					MainFrame frame = new MainFrame();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainFrame() {
		this.controller = new MainFrameController(this);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/gui/img/dnaIcon.png")));
		setTitle(Internationalization.get("TITLE_TOOLNAME"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 950, 600);
		setJMenuBar(getMenuBar_1());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getOpenFileButtonPn(), BorderLayout.NORTH);
		contentPane.add(getMainPn(), BorderLayout.CENTER);
		contentPane.add(getScriptGenerationButtonPn(), BorderLayout.SOUTH);
	}

	protected JMenuBar getMenuBar_1() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getMnFile());
			menuBar.add(getMnHelp());
		}
		return menuBar;
	}

	protected JMenu getMnFile() {
		if (mnFile == null) {
			mnFile = new JMenu(Internationalization.get("FILE"));
			mnFile.add(getMenuFileNew());
			mnFile.add(getFileMenuSeparator());
			mnFile.add(getMenuFileExit());
		}
		return mnFile;
	}

	protected JMenu getMnHelp() {
		if (mnHelp == null) {
			mnHelp = new JMenu(Internationalization.get("HELP"));
			mnHelp.add(getMntmLanguage());
			mnHelp.add(getHelpMenuSeparator());
			mnHelp.add(getMntmAbout());
		}
		return mnHelp;
	}

	@SuppressWarnings("deprecation")
	protected JMenuItem getMenuFileExit() {
		if (menuFileExit == null) {
			menuFileExit = new JMenuItem(Internationalization.get("EXIT"));
			menuFileExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.closeProgram();
				}
			});
			menuFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		}
		return menuFileExit;
	}

	@SuppressWarnings("deprecation")
	protected JMenuItem getMenuFileNew() {
		if (menuFileNew == null) {
			menuFileNew = new JMenuItem(Internationalization.get("NEW"));
			menuFileNew.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.initialize();
				}
			});
			menuFileNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		}
		return menuFileNew;
	}

	protected JSeparator getFileMenuSeparator() {
		if (fileMenuSeparator == null) {
			fileMenuSeparator = new JSeparator();
		}
		return fileMenuSeparator;
	}

	@SuppressWarnings("deprecation")
	protected JMenuItem getMntmAbout() {
		if (mntmAbout == null) {
			mntmAbout = new JMenuItem(Internationalization.get("ABOUT"));
			mntmAbout.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.showAbout();
				}
			});
			mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
		}
		return mntmAbout;
	}

	protected JPanel getOpenFileButtonPn() {
		if (openFileButtonPn == null) {
			openFileButtonPn = new JPanel();
			FlowLayout flowLayout = (FlowLayout) openFileButtonPn.getLayout();
			flowLayout.setAlignment(FlowLayout.LEADING);
			openFileButtonPn.add(getBtnOpenFile());
			openFileButtonPn.add(getBtnMergeFiles());
		}
		return openFileButtonPn;
	}

	protected JPanel getScriptGenerationButtonPn() {
		if (scriptGenerationButtonPn == null) {
			scriptGenerationButtonPn = new JPanel();
			scriptGenerationButtonPn.add(getBtnGenerateScript());
			scriptGenerationButtonPn.add(getBtnRunScript());
			scriptGenerationButtonPn.add(getBtnExportScript());
		}
		return scriptGenerationButtonPn;
	}

	protected JButton getBtnOpenFile() {
		if (btnOpenFile == null) {
			btnOpenFile = new JButton(Internationalization.get("LOAD_FILE"));
			btnOpenFile.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnOpenFile.setMnemonic('o');
			btnOpenFile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.openFile();
				}
			});
			btnOpenFile.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return btnOpenFile;
	}

	protected JButton getBtnMergeFiles() {
		if (btnMergeFiles == null) {
			btnMergeFiles = new JButton(Internationalization.get("MERGE_FILES"));
			btnMergeFiles.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnMergeFiles.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.mergeFiles();
				}
			});
			btnMergeFiles.setMnemonic('m');
			btnMergeFiles.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return btnMergeFiles;
	}

	protected JButton getBtnGenerateScript() {
		if (btnGenerateScript == null) {
			btnGenerateScript = new JButton(Internationalization.get("GENERATE_SCRIPT"));
			btnGenerateScript.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnGenerateScript.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.generateScript();
				}
			});
			btnGenerateScript.setMnemonic('g');
			btnGenerateScript.setEnabled(false);
		}
		return btnGenerateScript;
	}

	protected JButton getBtnRunScript() {
		if (btnRunScript == null) {
			btnRunScript = new JButton(Internationalization.get("RUN_SCRIPT"));
			btnRunScript.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnRunScript.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.runScript();
				}
			});
			btnRunScript.setMnemonic('u');
			btnRunScript.setEnabled(false);
		}
		return btnRunScript;
	}

	protected JButton getBtnExportScript() {
		if (btnExportScript == null) {
			btnExportScript = new JButton(Internationalization.get("EXPORT_SCRIPT"));
			btnExportScript.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnExportScript.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.exportScript();
				}
			});
			btnExportScript.setMnemonic('s');
			btnExportScript.setEnabled(false);
		}
		return btnExportScript;
	}

	protected JPanel getMainPn() {
		if (mainPn == null) {
			mainPn = new JPanel();
			mainPn.setLayout(new BoxLayout(mainPn, BoxLayout.X_AXIS));
			mainPn.add(getPlotsPn());
			mainPn.add(getScriptPn());
			mainPn.add(getPreviewPn());
		}
		return mainPn;
	}

	protected JPanel getPlotsPn() {
		if (plotsPn == null) {
			plotsPn = new JPanel();
			plotsPn.setPreferredSize(new Dimension(300, 10));
			plotsPn.setBorder(new LineBorder(new Color(0, 0, 0)));
			plotsPn.setLayout(new BoxLayout(plotsPn, BoxLayout.Y_AXIS));
			plotsPn.add(getLblFile());
			plotsPn.add(getMetricsPlotsPn());
			plotsPn.add(Box.createRigidArea(new Dimension(0, 5)));
			plotsPn.add(getPlotManagerPn());
		}
		return plotsPn;
	}

	protected JPanel getScriptPn() {
		if (scriptPn == null) {
			scriptPn = new JPanel();
			scriptPn.setBorder(new LineBorder(new Color(0, 0, 0)));
			scriptPn.setLayout(new BoxLayout(scriptPn, BoxLayout.Y_AXIS));
			scriptPn.add(getScriptEditPn());
			scriptPn.add(getTextAreaScrollPane());
		}
		return scriptPn;
	}

	protected JPanel getPreviewPn() {
		if (previewPn == null) {
			previewPn = new JPanel();
			previewPn.setVisible(false);
			previewPn.setBorder(new LineBorder(new Color(0, 0, 0)));
			previewPn.setLayout(new BoxLayout(previewPn, BoxLayout.Y_AXIS));
			previewPn.add(getLblStatistics());
			previewPn.add(getStatisticsPreviewPn());
			previewPn.add(Box.createRigidArea(new Dimension(0, 20)));
			previewPn.add(getLblPlotPreview());
			previewPn.add(Box.createRigidArea(new Dimension(0, 20)));
			previewPn.add(getPlotPreviewPn());
			previewPn.add(Box.createRigidArea(new Dimension(0, 20)));
		}
		return previewPn;
	}

	protected JLabel getLblFile() {
		if (lblFile == null) {
			lblFile = new JLabel(Internationalization.get("FILE_NONE"));
			lblFile.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblFile.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return lblFile;
	}

	protected JPanel getMetricsPlotsPn() {
		if (metricsPlotsPn == null) {
			metricsPlotsPn = new JPanel();
			metricsPlotsPn.setVisible(false);
			metricsPlotsPn.setAlignmentY(Component.TOP_ALIGNMENT);
			metricsPlotsPn.setLayout(new BoxLayout(metricsPlotsPn, BoxLayout.X_AXIS));
			metricsPlotsPn.add(getMetricSelectScrollPane());
			metricsPlotsPn.add(getPlotSelectScrollPane());
		}
		return metricsPlotsPn;
	}

	protected JPanel getMetricSelectPn() {
		if (metricSelectPn == null) {
			metricSelectPn = new JPanel();
			metricSelectPn.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			metricSelectPn.setLayout(new BoxLayout(metricSelectPn, BoxLayout.Y_AXIS));
		}
		return metricSelectPn;
	}

	protected JPanel getPlotsSelectPn() {
		if (plotsSelectPn == null) {
			plotsSelectPn = new JPanel();
			plotsSelectPn.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			plotsSelectPn.setLayout(new BoxLayout(plotsSelectPn, BoxLayout.Y_AXIS));
		}
		return plotsSelectPn;
	}

	protected JPanel getPlotManagerPn() {
		if (plotManagerPn == null) {
			plotManagerPn = new JPanel();
			plotManagerPn.setLayout(new BoxLayout(plotManagerPn, BoxLayout.Y_AXIS));
			plotManagerPn.add(getPlotButtonsPn());
			plotManagerPn.add(getLblPlots());
			plotManagerPn.add(getPlotListScrollPane());
		}
		return plotManagerPn;
	}

	protected JPanel getPlotButtonsPn() {
		if (plotButtonsPn == null) {
			plotButtonsPn = new JPanel();
			plotButtonsPn.setVisible(false);
			plotButtonsPn.setLayout(new BoxLayout(plotButtonsPn, BoxLayout.X_AXIS));
			plotButtonsPn.add(getBtnAddPlot());
			plotButtonsPn.add(Box.createRigidArea(new Dimension(5, 5)));
			plotButtonsPn.add(getBtnRemovePlot());
		}
		return plotButtonsPn;
	}

	protected JButton getBtnAddPlot() {
		if (btnAddPlot == null) {
			btnAddPlot = new JButton(Internationalization.get("ADD_PLOT"));
			btnAddPlot.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnAddPlot.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.addPlot();
				}
			});
			btnAddPlot.setMnemonic('a');
			btnAddPlot.setEnabled(false);
		}
		return btnAddPlot;
	}

	protected JButton getBtnRemovePlot() {
		if (btnRemovePlot == null) {
			btnRemovePlot = new JButton(Internationalization.get("REMOVE_PLOT"));
			btnRemovePlot.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnRemovePlot.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.removePlot();
				}
			});
			btnRemovePlot.setMnemonic('r');
			btnRemovePlot.setEnabled(false);
		}
		return btnRemovePlot;
	}

	protected JPanel getPlotListPn() {
		if (plotListPn == null) {
			plotListPn = new JPanel();
			plotListPn.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			plotListPn.setLayout(new BoxLayout(plotListPn, BoxLayout.Y_AXIS));
		}
		return plotListPn;
	}

	protected JLabel getLblPlots() {
		if (lblPlots == null) {
			lblPlots = new JLabel(Internationalization.get("PLOTS"));
			lblPlots.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblPlots.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return lblPlots;
	}

	protected JPanel getScriptEditPn() {
		if (scriptEditPn == null) {
			scriptEditPn = new JPanel();
			scriptEditPn.setLayout(new BoxLayout(scriptEditPn, BoxLayout.X_AXIS));
			scriptEditPn.add(getLblGeneratedScript());
			scriptEditPn.add(Box.createRigidArea(new Dimension(30, 0)));
			scriptEditPn.add(getBtnEditSave());
			scriptEditPn.add(Box.createRigidArea(new Dimension(5, 0)));
			scriptEditPn.add(getBtnClear());
		}
		return scriptEditPn;
	}

	protected JLabel getLblGeneratedScript() {
		if (lblGeneratedScript == null) {
			lblGeneratedScript = new JLabel(Internationalization.get("GENERATED_SCRIPT"));
			lblGeneratedScript.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lblGeneratedScript;
	}

	protected JButton getBtnEditSave() {
		if (btnEditSave == null) {
			btnEditSave = new JButton(Internationalization.get("EDIT"));
			btnEditSave.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnEditSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.editSaveScriptArea();
				}
			});
			btnEditSave.setMnemonic('e');
			btnEditSave.setEnabled(false);
		}
		return btnEditSave;
	}

	protected JTextArea getTextAreaScript() {
		if (textAreaScript == null) {
			textAreaScript = new JTextArea();
			textAreaScript.setFont(new Font("Monospaced", Font.PLAIN, 15));
			textAreaScript.setWrapStyleWord(true);
			textAreaScript.setLineWrap(true);
			textAreaScript.setColumns(50);
			textAreaScript.setText(Internationalization.get("NO_SCRIPT_YET"));
			textAreaScript.setEditable(false);
			textAreaScript.setBackground(new Color(199, 190, 165));
		}
		return textAreaScript;
	}

	protected PagePanel getPlotPreviewPn() {
		if (plotPreviewPn == null) {
			plotPreviewPn = new PagePanel();
			plotPreviewPn.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		}
		return plotPreviewPn;
	}

	protected JLabel getLblPlotPreview() {
		if (lblPlotPreview == null) {
			lblPlotPreview = new JLabel(Internationalization.get("PLOT_PREVIEW"));
			lblPlotPreview.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblPlotPreview.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return lblPlotPreview;
	}

	protected JPanel getStatisticsPreviewPn() {
		if (statisticsPreviewPn == null) {
			statisticsPreviewPn = new JPanel();
			statisticsPreviewPn.setLayout(new BoxLayout(statisticsPreviewPn, BoxLayout.Y_AXIS));

		}
		return statisticsPreviewPn;
	}

	protected JLabel getLblStatistics() {
		if (lblStatistics == null) {
			lblStatistics = new JLabel(Internationalization.get("STATISTICS"));
			lblStatistics.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblStatistics.setAlignmentX(Component.CENTER_ALIGNMENT);
		}
		return lblStatistics;
	}

	protected JScrollPane getMetricSelectScrollPane() {
		if (metricSelectScrollPane == null) {
			metricSelectScrollPane = new JScrollPane();
			metricSelectScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			metricSelectScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			metricSelectScrollPane.setViewportView(getMetricSelectPn());
		}
		return metricSelectScrollPane;
	}

	protected JScrollPane getPlotSelectScrollPane() {
		if (plotSelectScrollPane == null) {
			plotSelectScrollPane = new JScrollPane();
			plotSelectScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			plotSelectScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			plotSelectScrollPane.setViewportView(getPlotsSelectPn());
		}
		return plotSelectScrollPane;
	}

	protected JScrollPane getPlotListScrollPane() {
		if (plotListScrollPane == null) {
			plotListScrollPane = new JScrollPane();
			plotListScrollPane.setMinimumSize(new Dimension(23, 75));
			plotListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			plotListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			plotListScrollPane.setViewportView(getPlotListPn());
		}
		return plotListScrollPane;
	}

	protected JScrollPane getTextAreaScrollPane() {
		if (textAreaScrollPane == null) {
			textAreaScrollPane = new JScrollPane();
			textAreaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			textAreaScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			textAreaScrollPane.setViewportView(this.getTextAreaScript());
		}
		return textAreaScrollPane;
	}

	protected MainFrameController getController() {
		return this.controller;
	}

	protected JButton getBtnClear() {
		if (btnClear == null) {
			btnClear = new JButton(Internationalization.get("CLEAR"));
			btnClear.setEnabled(false);
			btnClear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.clearScript();
				}
			});
			btnClear.setMnemonic('c');
			btnClear.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return btnClear;
	}

	@SuppressWarnings("deprecation")
	protected JMenuItem getMntmLanguage() {
		if (mntmLanguage == null) {
			mntmLanguage = new JMenuItem(Internationalization.get("LANGUAGE"));	
			mntmLanguage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
			mntmLanguage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.changeLanguage();
				}
			});
		}
		return mntmLanguage;
	}

	protected JSeparator getHelpMenuSeparator() {
		if (helpMenuSeparator == null) {
			helpMenuSeparator = new JSeparator();
		}
		return helpMenuSeparator;
	}
}
