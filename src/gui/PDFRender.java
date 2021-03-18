package gui;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

public class PDFRender {

	public static void renderPDF(PagePanel panel, String path) throws IOException {
		System.out.println(path);
		path = "./lorem.pdf";
		File file = new File(path);
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile pdffile = new PDFFile(buf);

		// show the first page
		PDFPage page = pdffile.getPage(0);
		panel.showPage(page);
		raf.close();
	}
}
