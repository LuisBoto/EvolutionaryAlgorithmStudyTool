package gui;

import java.io.File;

import javax.swing.JFileChooser;

public class MainFrameController {

	private MainFrame mf;

	public MainFrameController(MainFrame mf) {
		this.mf = mf;
	}

	public void openFile() {
		JFileChooser fc = new JFileChooser();
		if (fc.showOpenDialog(mf) != JFileChooser.APPROVE_OPTION)
			return;
		File f = fc.getSelectedFile();
		String filepath = f.getPath();
		System.out.println(filepath);
	}

	public void closeProgram() {
		System.exit(0);
	}

}