package test;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;

import org.junit.Test;

import internationalization.Internationalization;

import org.junit.Assert;
import logic.Statistics;
import logic.scripter.Metric;

public class StatisticsTest {
	
	@Test
	public void maximumMinimumTest() {
		List<Metric> metrics = new ArrayList<Metric>();
		Metric values1 = new Metric("values1");
		values1.addValue("8");
		values1.addValue("800");
		values1.addValue("435.65");
		values1.addValue("8.0E+10");
		values1.addValue("20");
		values1.addValue("-9000");
		Metric values2 = new Metric("values2");
		values2.addValue("7");
		values2.addValue("200");
		values2.addValue("635.65");
		values2.addValue("3.0E+10");
		values2.addValue("20");
		values2.addValue("-9000.74");
		metrics.add(values1);
		metrics.add(values2);
		String result = Statistics.getStatistic(0, metrics);
		Assert.assertEquals("80000000000", result);
		result = Statistics.getStatistic(1, metrics);
		Assert.assertEquals("-9000,74", result);
	}
	
	@Test
	public void standardDeviationTest() {
		List<Metric> metrics = new ArrayList<Metric>();
		Metric values1 = new Metric("values1");
		values1.addValue("8");
		values1.addValue("800");
		values1.addValue("435.65");
		values1.addValue("8.0E+10");
		values1.addValue("20");
		values1.addValue("-9000");
		Metric values2 = new Metric("values2");
		values2.addValue("7");
		values2.addValue("200");
		values2.addValue("635.65");
		values2.addValue("3.0E+10");
		values2.addValue("20");
		values2.addValue("-9000.74");
		metrics.add(values1);
		metrics.add(values2);
		String result = Statistics.getStatistic(3, metrics);
		Assert.assertEquals("22897719969,642918", result);
	}
	
	@Test
	public void averageTest() {
		List<Metric> metrics = new ArrayList<Metric>();
		Metric values1 = new Metric("values1");
		values1.addValue("8");
		values1.addValue("800");
		values1.addValue("435.65");
		values1.addValue("8.0E+10");
		values1.addValue("20");
		values1.addValue("-9000");
		Metric values2 = new Metric("values2");
		values2.addValue("7");
		values2.addValue("200");
		values2.addValue("635.65");
		values2.addValue("3.0E+10");
		values2.addValue("20");
		values2.addValue("-9000.74");
		metrics.add(values1);
		metrics.add(values2);
		String result = Statistics.getStatistic(2, metrics);
		Assert.assertEquals("9166665343,796665", result);
	}
	
	@Test
	public void normalityTest() {
		List<Metric> metrics = new ArrayList<Metric>();
		Metric values1 = new Metric("values1");
		values1.addValue("8");
		values1.addValue("800");
		values1.addValue("435.65");
		values1.addValue("8.0E+10");
		values1.addValue("20");
		values1.addValue("-9000");
		Metric values2 = new Metric("values2");
		values2.addValue("7");
		values2.addValue("200");
		values2.addValue("635.65");
		values2.addValue("3.0E+10");
		values2.addValue("20");
		values2.addValue("-9000.74");
		Metric values3 = new Metric("values3");
		values3.addValue("20");
		values3.addValue("25");
		values3.addValue("35");
		values3.addValue("40");
		metrics.add(values1);
		String result;
		try {
			result = Statistics.getAdvancedStatistic(0, metrics);
			Assert.assertEquals("p.value=0,00002072930886", result.split("\n")[1]);
			metrics.add(0, values2);
			result = Statistics.getAdvancedStatistic(0, metrics);
			Assert.assertEquals("p.value=0,0000207294123", result.split("\n")[1]);
			metrics.add(0, values3);
			result = Statistics.getAdvancedStatistic(0, metrics);
			Assert.assertEquals("p.value=0,71428015119476", result.split("\n")[1]);
		} catch (ScriptException e) {
			Assert.fail("ScriptException occurred");
		}
	}
	
	@Test
	public void welchTest() {
		List<Metric> metrics = new ArrayList<Metric>();
		Metric values1 = new Metric("values1");
		values1.addValue("20");
		values1.addValue("25");
		values1.addValue("35");
		values1.addValue("40");
		Metric values2 = new Metric("values2");
		values2.addValue("200");
		values2.addValue("250");
		values2.addValue("650");
		values2.addValue("875");
		metrics.add(values1);
		String result;
		try {
			result = Statistics.getAdvancedStatistic(1, metrics);
			Assert.assertEquals(Internationalization.get("SELECT_METRIC_TWO_ERROR"), result);
			metrics.add(values2);
			result = Statistics.getAdvancedStatistic(1, metrics);
			Assert.assertEquals("p.value=0,02883690586541", result.split("\n")[1]);
		} catch (ScriptException e) {
			Assert.fail("ScriptException occurred");
		}
	}
	
	@Test
	public void wilcoxonTest() {
		List<Metric> metrics = new ArrayList<Metric>();
		// Paired values
		Metric values1 = new Metric("values1");
		values1.addValue("20");
		values1.addValue("25");
		values1.addValue("35");
		values1.addValue("40");
		Metric values2 = new Metric("values2");
		values2.addValue("200");
		values2.addValue("250");
		values2.addValue("650");
		values2.addValue("875");
		metrics.add(values1);
		metrics.add(values2);
		String result;
		try {
			result = Statistics.getAdvancedStatistic(2, metrics); // PAIRED=FALSE
			Assert.assertEquals("p.value=0,02857142857143", result.split("\n")[1].split(", pointprob")[0]);
			
			values1 = new Metric("values1");
			values1.addValue("8");
			values1.addValue("800");
			values1.addValue("435.65");
			values1.addValue("8.0E+10");
			values1.addValue("20");
			values1.addValue("-9000");
			values2 = new Metric("values2");
			values2.addValue("7");
			values2.addValue("200");
			values2.addValue("635.65");
			values2.addValue("3.0E+10");
			values2.addValue("20");
			values2.addValue("-9000.74");
			metrics = new ArrayList<Metric>();
			metrics.add(values1);
			result = Statistics.getAdvancedStatistic(2, metrics);
			Assert.assertEquals(Internationalization.get("SELECT_METRIC_TWO_ERROR"), result);
			metrics.add(values2);
			result = Statistics.getAdvancedStatistic(3, metrics); // PAIRED=TRUE
			Assert.assertEquals("p.value=0,3125", result.split("\n")[1].split(", pointprob")[0]);
		} catch (ScriptException e) {
			Assert.fail("ScriptException occurred");
		}
	}
	
	@Test
	public void kruskalTest() {
		List<Metric> metrics = new ArrayList<Metric>();
		Metric values1 = new Metric("values1");
		values1.addValue("8");
		values1.addValue("800");
		values1.addValue("435.65");
		values1.addValue("8.0E+10");
		values1.addValue("20");
		values1.addValue("-9000");
		Metric values2 = new Metric("values2");
		values2.addValue("7");
		values2.addValue("200");
		values2.addValue("635.65");
		values2.addValue("3.0E+10");
		values2.addValue("20");
		values2.addValue("-9000.74");
		metrics.add(values1);
		String result;
		try {
			result = Statistics.getAdvancedStatistic(4, metrics);
			Assert.assertEquals(Internationalization.get("SELECT_METRIC_TWO_MORE_ERROR"), result);
			metrics.add(values2);
			result = Statistics.getAdvancedStatistic(4, metrics);
			Assert.assertEquals("p.value=0,68840386327569", result.split("\n")[1].split(", ")[1]);
		} catch (ScriptException e) {
			Assert.fail("ScriptException occurred");
		}
	}
	
	@Test
	public void friedmanTest() {
		List<Metric> metrics = new ArrayList<Metric>();
		Metric values1 = new Metric("values1");
		values1.addValue("8");
		values1.addValue("800");
		values1.addValue("435.65");
		values1.addValue("8.0E+10");
		values1.addValue("20");
		values1.addValue("-9000");
		Metric values2 = new Metric("values2");
		values2.addValue("7");
		values2.addValue("200");
		values2.addValue("635.65");
		values2.addValue("3.0E+10");
		values2.addValue("20");
		values2.addValue("-9000.74");
		metrics.add(values1);
		String result;
		try {
			result = Statistics.getAdvancedStatistic(5, metrics);
			Assert.assertEquals(Internationalization.get("SELECT_METRIC_TWO_MORE_ERROR"), result);
			metrics.add(values2);
			result = Statistics.getAdvancedStatistic(5, metrics);
			Assert.assertEquals("p.value=0,10906415794977", result.split("\n")[1]);
		} catch (ScriptException e) {
			Assert.fail("ScriptException occurred");
		}
	}

	@Test
	public void resultsTest() {
		Statistics.cleanResults();
		List<Metric> metrics = new ArrayList<Metric>();
		Metric values1 = new Metric("values1");
		values1.addValue("8");
		values1.addValue("800");
		values1.addValue("435.65");
		values1.addValue("8.0E+10");
		values1.addValue("20");
		values1.addValue("-9000");
		Metric values2 = new Metric("values2");
		values2.addValue("7");
		values2.addValue("200");
		values2.addValue("635.65");
		values2.addValue("3.0E+10");
		values2.addValue("20");
		values2.addValue("-9000.74");
		metrics.add(values1);
		metrics.add(values2);
		try {
			Statistics.getAdvancedStatistic(0, metrics);
			Statistics.getAdvancedStatistic(1, metrics);
			Statistics.getAdvancedStatistic(2, metrics);
			Statistics.getAdvancedStatistic(3, metrics);
			Statistics.getAdvancedStatistic(4, metrics);
			Statistics.getAdvancedStatistic(5, metrics);
			Assert.assertEquals(6, Statistics.getResults().size());
			Statistics.cleanResults();
			Assert.assertEquals(0, Statistics.getResults().size());
		}  catch (ScriptException e) {
			Assert.fail("ScriptException occurred");
		}
	}
}
