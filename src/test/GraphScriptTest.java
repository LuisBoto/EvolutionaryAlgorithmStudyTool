package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import logic.scripter.Metric;
import logic.scripter.Scripter;
import logic.scripter.graphs.GraphCommand;
import logic.scripter.graphs.GraphFactory;
import logic.scripter.graphs.Parameter;

public class GraphScriptTest {

	@Test
	public void parameterTest() {
		// Check for added parameters on plot call
		Metric values1 = new Metric("values1");
		Metric values2 = new Metric("values2");
		values1.addValue("10,0");
		values1.addValue("20,0");
		values1.addValue("30,0");
		values1.addValue("40,0");
		values1.addValue("50,0");
		values2.addValue("10,1");
		values2.addValue("20,1");
		values2.addValue("30,1");
		values2.addValue("40,1");
		values2.addValue("50,1");
		List<Metric> metrics = new ArrayList<Metric>();
		metrics.add(values1);
		metrics.add(values2);
		GraphCommand boxplot = GraphFactory.createGraphObject("BoxPlot", "graphCreationTest", metrics, null);
		String code = boxplot.generateScriptCode();
		Assert.assertEquals(
				"pdf('graphCreationTest.pdf')\n"
				+ "graphCreationTestDevice<-dev.cur()\n"
				+ "png('graphCreationTest.png')\n"
				+ "dev.control('enable')\n"
				+ "boxplot(values1,values2, names = c('values1','values2'))\n"
				+ "dev.copy(which=graphCreationTestDevice)\n"
				+ "dev.off()\ndev.off()",
				code);
		Parameter param1 = new Parameter("parameter1", "valueFirst");
		Parameter param2 = new Parameter("parameter2", "'valueSecond'");
		Parameter param3 = new Parameter("parameter3", "333");
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(param1);
		params.add(param2);
		params.add(param3);
		boxplot = GraphFactory.createGraphObject("BoxPlot", "graphCreationTest", metrics, params);
		code = boxplot.generateScriptCode();
		Assert.assertEquals(
				"pdf('graphCreationTest.pdf')\n"
				+ "graphCreationTestDevice<-dev.cur()\n"
				+ "png('graphCreationTest.png')\n"
				+ "dev.control('enable')\n"
				+ "boxplot(values1,values2, names = c('values1','values2'), "
				+ "parameter1=valueFirst, parameter2='valueSecond', parameter3=333)\n"
				+ "dev.copy(which=graphCreationTestDevice)\n"
				+ "dev.off()\n"
				+ "dev.off()",
				code);
	}

	@Test
	public void scriptGenerationTest() {
		// Setting some metrics
		Metric values1 = new Metric("values1");
		Metric values2 = new Metric("values2");
		Metric values3 = new Metric("values3");
		values1.addValue("10,0");
		values1.addValue("20,0");
		values1.addValue("30,0");
		values1.addValue("40,0");
		values1.addValue("50,0");
		values2.addValue("10,1");
		values2.addValue("20,1");
		values2.addValue("30,1");
		values2.addValue("40,1");
		values2.addValue("50,1");
		values3.addValue("10,2");
		values3.addValue("20,2");
		values3.addValue("30,2");
		values3.addValue("40,2");
		values3.addValue("50,2");
		
		List<Metric> metrics = new ArrayList<Metric>();
		metrics.add(values1);
		
		// Setting some params
		Parameter param1 = new Parameter("parameter1", "valueFirst");
		Parameter param2 = new Parameter("parameter2", "'valueSecond'");
		Parameter param3 = new Parameter("parameter3", "333");
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(param1);
		params.add(param2);
		params.add(param3);
		
		//Creating graph objects
		GraphCommand boxplot = GraphFactory.createGraphObject("BoxPlot", "graphBoxTest", new ArrayList<Metric>(metrics), null);
		metrics.add(values2);
		metrics.add(values3);
		GraphCommand plot = GraphFactory.createGraphObject("Plot", "graphPlotTest", metrics, params);
		List<GraphCommand> graphs = new ArrayList<GraphCommand>();
		graphs.add(boxplot);
		graphs.add(plot);
		
		String script = Scripter.createScript(metrics, graphs);
		String expectedScript = "values1 <- c(10.0,20.0,30.0,40.0,50.0)\n"
								+ "values2 <- c(10.1,20.1,30.1,40.1,50.1)\n"
								+ "values3 <- c(10.2,20.2,30.2,40.2,50.2)\n"
								+ "pdf('graphBoxTest.pdf')\n"
								+ "graphBoxTestDevice<-dev.cur()\n"
								+ "png('graphBoxTest.png')\n"
								+ "dev.control('enable')\n"
								+ "boxplot(values1, names = c('values1'))\n"
								+ "dev.copy(which=graphBoxTestDevice)\n"
								+ "dev.off()\n"
								+ "dev.off()\n"
								+ "pdf('graphPlotTest.pdf')\n"
								+ "graphPlotTestDevice<-dev.cur()\n"
								+ "png('graphPlotTest.png')\n"
								+ "dev.control('enable')\n"
								+ "plot(values1,values2, parameter1=valueFirst, "
								+ "parameter2='valueSecond', parameter3=333, ylim=c(10.1,50.2))\n"
								+ "lines(values1, values3)\n"
								+ "dev.copy(which=graphPlotTestDevice)\n"
								+ "dev.off()\n"
								+ "dev.off()\n";
		Assert.assertEquals(expectedScript, script);
	}
}