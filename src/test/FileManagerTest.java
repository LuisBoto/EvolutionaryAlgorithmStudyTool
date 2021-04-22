package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import logic.fileManager.FileMerger;
import logic.fileManager.FileParser;
import logic.scripter.Metric;

public class FileManagerTest {
	
	@BeforeClass
	public static void createTestDirectory() {
		new File("./testFiles").mkdirs();
	}
	
	@AfterClass
	public static void deleteTestDirectory() {
		new File("./testFiles").delete();
	}
	
	@Before
	@After
	public void cleanTestFiles() {
		for (File data : new File("./testFiles/").listFiles()) {
			data.delete();
		}
	}

	@Test
	public void parseTest() {
		// Initial file test with 3 valid metrics
		// Includes decimal (both . and ,), negative, positive, scientific notation
		String fileContents = "iterations;fitness;time;\n8;10;30;\n0.8;0.1;0.3;\n0,8;0,1;0,3;\n8E-10;1E+3;3E-4;\n";
		FileWriter fr;
		try {
			fr = new FileWriter("./testFiles/parseTest.csv");
			BufferedWriter br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();
		} catch (IOException e) {
			// e.printStackTrace();
			Assert.fail("An exception occurred creating the test files");
		}

		try {
			List<Metric> parsed = FileParser.parseMetrics(0, "./testFiles/parseTest.csv");
			Assert.assertEquals(3, parsed.size());
			Assert.assertEquals(4, parsed.get(0).getSize());
			Assert.assertEquals(4, parsed.get(1).getSize());
			Assert.assertEquals(4, parsed.get(2).getSize());
			Assert.assertEquals("iterations0 <- c(8,0.8,0.8,8E-10)", parsed.get(0).toString());
			Assert.assertEquals("fitness0 <- c(10,0.1,0.1,1E+3)", parsed.get(1).toString());
			Assert.assertEquals("time0 <- c(30,0.3,0.3,3E-4)", parsed.get(2).toString());

			// Second file test includes non valid (text) metric, different combination of
			// decimals/negatives/notations
			fileContents = "iterations;fitness;time;\n8;10;30;\n0.8;hola;0.3;\n-0,8;0,1;0,3;\n8E-10;1E+3;3,87E-4;\n";
			try {
				fr = new FileWriter("./testFiles/parseTest.csv");
				BufferedWriter br = new BufferedWriter(fr);
				br.write(fileContents);
				br.close();
				fr.close();
			} catch (IOException e) {
				// e.printStackTrace();
				Assert.fail("An exception occurred creating the test files");
			}

			parsed = FileParser.parseMetrics(0, "./testFiles/parseTest.csv");
			Assert.assertEquals(2, parsed.size());
			Assert.assertEquals(4, parsed.get(0).getSize());
			Assert.assertEquals(4, parsed.get(1).getSize());
			Assert.assertEquals("iterations0 <- c(8,0.8,-0.8,8E-10)", parsed.get(0).toString());
			Assert.assertEquals("time0 <- c(30,0.3,0.3,3.87E-4)", parsed.get(1).toString());
		} catch (IOException e) {
			// e.printStackTrace();
			Assert.fail("An exception occurred parsing the test files");
		}
	}

	@Test
	public void lineMergeSameLengthTest() {
		// First case: 3 valid files, merged by last line, all the same in length
		String fileContents = "iterations;fitness;time;\n10;10;10;\n0.1;0.1;0.1;\n0,1;0,1;0,1;\n1E-10;1E+3;1E-4;\n";
		FileWriter fr;
		try {
			fr = new FileWriter("./testFiles/mergeTest1.csv");
			BufferedWriter br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();

			fileContents = "iterations;fitness;time;\n20;20;20;\n0.2;0.2;0.2;\n0,2;0,2;0,2;\n2E-10;2E+3;2E-4;\n";
			fr = new FileWriter("./testFiles/mergeTest2.csv");
			br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();

			fileContents = "iterations;fitness;time;\n30;30;30;\n0.3;0.3;0.3;\n0,3;0,3;0,3;\n3E-10;3E+3;3E-4;\n";
			fr = new FileWriter("./testFiles/mergeTest3.csv");
			br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();
		} catch (IOException e) {
			// e.printStackTrace();
			Assert.fail("An exception occurred creating the test files");
		}

		try {
			FileMerger.mergeByLine("./testFiles/", "./testFiles/", 4);
			File mergedFile = new File("./testFiles/").listFiles()[3];
			FileReader reader = new FileReader(mergedFile.getAbsolutePath());
			BufferedReader bfr = new BufferedReader(reader);
			String contents = "";
			String aux;
			while ((aux = bfr.readLine()) != null) {
				contents += "\n" + aux;
			}
			bfr.close();
			reader.close();
			String[] lines = contents.trim().split("\n");
			Assert.assertEquals(4, lines.length);
			Assert.assertEquals("Cabecera;iterations;fitness;time;", lines[0]);
			Assert.assertEquals("./testFiles/mergeTest1.csv;1E-10;1E+3;1E-4;", lines[1]);
			Assert.assertEquals("./testFiles/mergeTest2.csv;2E-10;2E+3;2E-4;", lines[2]);
			Assert.assertEquals("./testFiles/mergeTest3.csv;3E-10;3E+3;3E-4;", lines[3]);
		} catch (IllegalArgumentException | IOException e) {
			// e.printStackTrace();
			Assert.fail("An error occurred merging the test files");
		}
	}

	@Test
	public void lineMergeDifferentLengthTest() {
		// Second case: 3 valid files, merged by first line, all having different lengths
		String fileContents = "iterations;fitness;time;\n10;10;10;\n0.1;0.1;0.1;\n0,1;0,1;0,1;\n1E-10;1E+3;1E-4;\n";
		FileWriter fr;
		try {
			fr = new FileWriter("./testFiles/mergeTest1.csv");
			BufferedWriter br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();

			fileContents = "iterations;fitness;time;\n20;20;20;\n0.2;0.2;0.2;\n0,2;0,2;0,2;\n";
			fr = new FileWriter("./testFiles/mergeTest2.csv");
			br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();

			fileContents = "iterations;fitness;time;\n30;30;30;\n0.3;0.3;0.3;\n";
			fr = new FileWriter("./testFiles/mergeTest3.csv");
			br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();
		} catch (IOException e) {
			Assert.fail("An exception occurred creating the test files");
		}

		try {
			FileMerger.mergeByLine("./testFiles/", "./testFiles/", 1);
			File mergedFile = new File("./testFiles/").listFiles()[3];
			FileReader reader = new FileReader(mergedFile.getAbsolutePath());
			BufferedReader bfr = new BufferedReader(reader);
			String contents = "";
			String aux;
			while ((aux = bfr.readLine()) != null) {
				contents += "\n" + aux;
			}
			bfr.close();
			reader.close();
			String[] lines = contents.trim().split("\n");
			Assert.assertEquals(4, lines.length);
			Assert.assertEquals("Cabecera;iterations;fitness;time;", lines[0]);
			Assert.assertEquals("./testFiles/mergeTest1.csv;10;10;10;", lines[1]);
			Assert.assertEquals("./testFiles/mergeTest2.csv;20;20;20;", lines[2]);
			Assert.assertEquals("./testFiles/mergeTest3.csv;30;30;30;", lines[3]);
		} catch (IllegalArgumentException | IOException e) {
			Assert.fail("An error occurred merging the test files");
		}
	}

	@Test
	public void lineMergeDifferentLengthOutOfBoundsTest() {
		// Third case: 3 valid files, merged by out of bounds line, all having different lengths
		String fileContents = "iterations;fitness;time;\n10;10;10;\n0.1;0.1;0.1;\n0,1;0,1;0,1;\n1E-10;1E+3;1E-4;\n";
		FileWriter fr;
		try {
			fr = new FileWriter("./testFiles/mergeTest1.csv");
			BufferedWriter br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();

			fileContents = "iterations;fitness;time;\n20;20;20;\n0.2;0.2;0.2;\n0,2;0,2;0,2;\n";
			fr = new FileWriter("./testFiles/mergeTest2.csv");
			br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();

			fileContents = "iterations;fitness;time;\n30;30;30;\n0.3;0.3;0.3;\n";
			fr = new FileWriter("./testFiles/mergeTest3.csv");
			br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();
		} catch (IOException e) {
			Assert.fail("An exception occurred creating the test files");
		}

		try {
			FileMerger.mergeByLine("./testFiles/", "./testFiles/", 4);
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("Out of bounds line parameter on file ./testFiles/mergeTest2.csv", e.getMessage());
		} catch (IOException e) {
			Assert.fail("An error occurred merging the test files");
		}
	}

	@Test
	public void averageMergeSameLengthTest() {
		// Merge by average 3 valid files all the same in length
		String fileContents = "iterations;fitness;time;\n10;10;10;\n0.1;0.1;0.1;\n0,1;0,1;0,1;\n1E-10;1E+3;1E-4;\n";
		FileWriter fr;
		try {
			fr = new FileWriter("./testFiles/mergeAverageTest1.csv");
			BufferedWriter br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();

			fileContents = "iterations;fitness;time;\n20;20;20;\n0.2;0.2;0.2;\n0,2;0,2;0,2;\n2E-10;2E+3;2E-4;\n";
			fr = new FileWriter("./testFiles/mergeAverageTest2.csv");
			br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();

			fileContents = "iterations;fitness;time;\n30;30;30;\n0.3;0.3;0.3;\n0,3;0,3;0,3;\n3E-10;3E+3;3E-4;\n";
			fr = new FileWriter("./testFiles/mergeAverageTest3.csv");
			br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();
		} catch (IOException e) {
			// e.printStackTrace();
			Assert.fail("An exception occurred creating the test files");
		}

		try {
			FileMerger.mergeByAverage("./testFiles/", "./testFiles/");
			File mergedFile = new File("./testFiles/").listFiles()[3];
			FileReader reader = new FileReader(mergedFile.getAbsolutePath());
			BufferedReader bfr = new BufferedReader(reader);
			String contents = "";
			String aux;
			while ((aux = bfr.readLine()) != null) {
				contents += "\n" + aux;
			}
			bfr.close();
			reader.close();
			String[] lines = contents.trim().split("\n");
			Assert.assertEquals(5, lines.length);
			Assert.assertEquals("iterations;fitness;time;", lines[0]);
			Assert.assertEquals("20;20;20;", lines[1]);
			Assert.assertEquals("0.2;0.2;0.2;", lines[2]);
			Assert.assertEquals("0.2;0.2;0.2;", lines[3]);
			Assert.assertEquals("0.0000000002;2000;0.0002;", lines[4]);
		} catch (IllegalArgumentException | IOException e) {
			// e.printStackTrace();
			Assert.fail("An error occurred merging the test files");
		}
	}

	@Test
	public void averageMergeDifferentLengthTest() {
		// Merge by average 3 valid files all different in length
		String fileContents = "iterations;fitness;time;\n10;10;10;\n0.1;0.1;0.1;\n0,1;0,1;0,1;\n1E-10;1E+3;1E-4;\n";
		FileWriter fr;
		try {
			fr = new FileWriter("./testFiles/mergeAverageTest1.csv");
			BufferedWriter br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();

			fileContents = "iterations;fitness;time;\n20;20;20;\n0.2;0.2;0.2;\n0,2;0,2;0,2;\n";
			fr = new FileWriter("./testFiles/mergeAverageTest2.csv");
			br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();

			fileContents = "iterations;fitness;time;\n30;30;30;\n0.3;0.3;0.3;\n";
			fr = new FileWriter("./testFiles/mergeAverageTest3.csv");
			br = new BufferedWriter(fr);
			br.write(fileContents);
			br.close();
			fr.close();
		} catch (IOException e) {
			// e.printStackTrace();
			Assert.fail("An exception occurred creating the test files");
		}

		try {
			FileMerger.mergeByAverage("./testFiles/", "./testFiles/");
		}  catch (IllegalArgumentException e) {
			Assert.assertEquals("Files are not equal in row number", e.getMessage());
		} catch (IOException e) {
			Assert.fail("An error occurred merging the test files");
		}
	}
}