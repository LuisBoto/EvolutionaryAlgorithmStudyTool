package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import internationalization.Internationalization;

import java.awt.Color;

public class PlotParameterDialog extends JDialog {

	private static final long serialVersionUID = 2749878461051136512L;

	// Controller to manage component events and operations
	private PlotParameterDialogController controller;
	private MainFrame mf;

	private JPanel btnPanel;
	private JButton btnFinish;
	private JButton btnCancel;
	private JPanel mainPanel;
	private JPanel plotNamePanel;
	private JLabel lblPlotName;
	private JTextField txtName;
	private JPanel parameterPanel;
	private JPanel parameterListPanel;
	private JTextArea txtParameterList;
	private JScrollPane scrollParamList;
	private JPanel paramInputsPn;
	private JPanel paramManagementButtonsPn;
	private JButton btnAdd;
	private JButton btnRemove;
	private JLabel lblParameterName;
	private JTextField txtParameterName;
	private JLabel lblParameterValue;
	private JTextField txtParameterValue;

	public PlotParameterDialog(MainFrame mf) {
		this.mf = mf;
		this.controller = new PlotParameterDialogController(this);
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 475, 300);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(PlotParameterDialog.class.getResource("/gui/img/dnaIcon.png")));
		setTitle(Internationalization.get("PLOT_DATA"));
		getContentPane().add(getBtnPanel(), BorderLayout.SOUTH);
		getContentPane().add(getMainPanel(), BorderLayout.CENTER);

	}

	protected JPanel getBtnPanel() {
		if (btnPanel == null) {
			btnPanel = new JPanel();
			btnPanel.add(getBtnFinish());
			btnPanel.add(getBtnCancel());
		}
		return btnPanel;
	}

	protected JButton getBtnFinish() {
		if (btnFinish == null) {
			btnFinish = new JButton(Internationalization.get("FINISH"));
			btnFinish.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnFinish.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.finish();
				}
			});
			btnFinish.setMnemonic('f');
		}
		return btnFinish;
	}

	protected JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton(Internationalization.get("CANCEL"));
			btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.cancel();
				}
			});
			btnCancel.setMnemonic('c');
		}
		return btnCancel;
	}

	protected JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			mainPanel.add(getPlotNamePanel());
			mainPanel.add(getParameterPanel());
			mainPanel.add(getParameterListPanel());
		}
		return mainPanel;
	}

	protected JPanel getPlotNamePanel() {
		if (plotNamePanel == null) {
			plotNamePanel = new JPanel();
			plotNamePanel.add(getLblPlotName());
			plotNamePanel.add(getTxtName());
		}
		return plotNamePanel;
	}

	protected JLabel getLblPlotName() {
		if (lblPlotName == null) {
			lblPlotName = new JLabel(Internationalization.get("PLOT_NAME"));
			lblPlotName.setFont(new Font("Tahoma", Font.BOLD, 12));
			lblPlotName.setLabelFor(getTxtName());
		}
		return lblPlotName;
	}

	protected JTextField getTxtName() {
		if (txtName == null) {
			txtName = new JTextField();
			txtName.setColumns(20);
		}
		return txtName;
	}

	protected JPanel getParameterPanel() {
		if (parameterPanel == null) {
			parameterPanel = new JPanel();
			parameterPanel.setLayout(new BoxLayout(parameterPanel, BoxLayout.Y_AXIS));
			parameterPanel.add(getParamInputsPn());
			parameterPanel.add(getParamManagementButtonsPn());
		}
		return parameterPanel;
	}

	protected JPanel getParameterListPanel() {
		if (parameterListPanel == null) {
			parameterListPanel = new JPanel();
			parameterListPanel.setLayout(new BoxLayout(parameterListPanel, BoxLayout.X_AXIS));
			parameterListPanel.add(Box.createRigidArea(new Dimension(5, 5)));
			parameterListPanel.add(getScrollParamList());
			parameterListPanel.add(Box.createRigidArea(new Dimension(5, 5)));
		}
		return parameterListPanel;
	}

	protected JTextArea getTxtParameterList() {
		if (txtParameterList == null) {
			txtParameterList = new JTextArea("");
			txtParameterList.setRows(3);
			txtParameterList.setWrapStyleWord(true);
			txtParameterList.setLineWrap(true);
			txtParameterList.setColumns(40);
			txtParameterList.setEditable(false);
			txtParameterList.setFont(new Font("Tahoma", Font.BOLD, 12));
		}
		return txtParameterList;
	}

	protected JScrollPane getScrollParamList() {
		if (scrollParamList == null) {
			scrollParamList = new JScrollPane();
			scrollParamList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollParamList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollParamList.setViewportView(getTxtParameterList());
		}
		return scrollParamList;
	}

	protected MainFrame getMainFrame() {
		return this.mf;
	}

	protected JPanel getParamInputsPn() {
		if (paramInputsPn == null) {
			paramInputsPn = new JPanel();
			paramInputsPn.setBorder(new LineBorder(new Color(0, 0, 0)));
			paramInputsPn.add(getLblParameterName());
			paramInputsPn.add(getTxtParameterName());
			paramInputsPn.add(getLblParameterValue());
			paramInputsPn.add(getTxtParameterValue());
		}
		return paramInputsPn;
	}

	protected JPanel getParamManagementButtonsPn() {
		if (paramManagementButtonsPn == null) {
			paramManagementButtonsPn = new JPanel();
			paramManagementButtonsPn.add(getBtnAdd_1());
			paramManagementButtonsPn.add(getBtnRemove_1());
		}
		return paramManagementButtonsPn;
	}

	protected JButton getBtnAdd_1() {
		if (btnAdd == null) {
			btnAdd = new JButton(Internationalization.get("ADD_PARAMETER"));
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.addParameter();
				}
			});
			btnAdd.setMnemonic('a');
			btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return btnAdd;
	}

	protected JButton getBtnRemove_1() {
		if (btnRemove == null) {
			btnRemove = new JButton(Internationalization.get("REMOVE_PARAMETER"));
			btnRemove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.removeParameter();
				}
			});
			btnRemove.setMnemonic('r');
			btnRemove.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return btnRemove;
	}

	protected JLabel getLblParameterName() {
		if (lblParameterName == null) {
			lblParameterName = new JLabel(Internationalization.get("PARAMETER_NAME"));
			lblParameterName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lblParameterName;
	}

	protected JTextField getTxtParameterName() {
		if (txtParameterName == null) {
			txtParameterName = new JTextField();
			txtParameterName.setColumns(8);
		}
		return txtParameterName;
	}

	protected JLabel getLblParameterValue() {
		if (lblParameterValue == null) {
			lblParameterValue = new JLabel(Internationalization.get("PARAMETER_VALUE"));
			lblParameterValue.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lblParameterValue;
	}

	protected JTextField getTxtParameterValue() {
		if (txtParameterValue == null) {
			txtParameterValue = new JTextField();
			txtParameterValue.setColumns(8);
		}
		return txtParameterValue;
	}
}
