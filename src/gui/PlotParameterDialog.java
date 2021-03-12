package gui;

import javax.swing.JDialog;
import java.awt.Toolkit;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PlotParameterDialog extends JDialog {

	private static final long serialVersionUID = 2749878461051136512L;
	
	// Controller to manage component events and operations
	private PlotParameterDialogController controller;
	
	private JPanel btnPanel;
	private JButton btnFinish;
	private JButton btnCancel;
	private JPanel mainPanel;
	private JPanel plotNamePanel;
	private JLabel lblPlotName;
	private JTextField txtName;
	private JPanel parameterPanel;
	private JLabel lblParameterName;
	private JTextField txtParameterName;
	private JLabel lblParameterValue;
	private JTextField txtParameterValue;
	private JPanel parameterListPanel;
	private JLabel lblParameterList;
	private JButton btnAdd;
	private JButton btnRemove;

	public PlotParameterDialog() {
		this.controller = new PlotParameterDialogController(this);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(PlotParameterDialog.class.getResource("/gui/img/dnaIcon.png")));
		setTitle("Plot data");
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
			btnFinish = new JButton("Finish");
			btnFinish.setMnemonic('f');
		}
		return btnFinish;
	}

	protected JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton("Cancel");
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
			lblPlotName = new JLabel("Plot name:");
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
			parameterPanel.add(getLblParameterName());
			parameterPanel.add(getTxtParameterName());
			parameterPanel.add(getLblParameterValue());
			parameterPanel.add(getTxtParameterValue());
			parameterPanel.add(getBtnAdd());
			parameterPanel.add(getBtnRemove());
		}
		return parameterPanel;
	}

	protected JLabel getLblParameterName() {
		if (lblParameterName == null) {
			lblParameterName = new JLabel("Parameter name:");
			lblParameterName.setLabelFor(getTxtParameterName());
		}
		return lblParameterName;
	}

	protected JTextField getTxtParameterName() {
		if (txtParameterName == null) {
			txtParameterName = new JTextField();
			txtParameterName.setColumns(10);
		}
		return txtParameterName;
	}

	protected JLabel getLblParameterValue() {
		if (lblParameterValue == null) {
			lblParameterValue = new JLabel("Parameter value:");
			lblParameterValue.setLabelFor(getTxtParameterValue());
		}
		return lblParameterValue;
	}

	protected JTextField getTxtParameterValue() {
		if (txtParameterValue == null) {
			txtParameterValue = new JTextField();
			txtParameterValue.setColumns(10);
		}
		return txtParameterValue;
	}

	protected JPanel getParameterListPanel() {
		if (parameterListPanel == null) {
			parameterListPanel = new JPanel();
			parameterListPanel.add(getLblParameterList());
		}
		return parameterListPanel;
	}

	protected JLabel getLblParameterList() {
		if (lblParameterList == null) {
			lblParameterList = new JLabel("");
		}
		return lblParameterList;
	}

	protected JButton getBtnAdd() {
		if (btnAdd == null) {
			btnAdd = new JButton("Add parameter");
			btnAdd.setMnemonic('a');
		}
		return btnAdd;
	}

	protected JButton getBtnRemove() {
		if (btnRemove == null) {
			btnRemove = new JButton("Remove parameter");
			btnRemove.setMnemonic('r');
		}
		return btnRemove;
	}
}
