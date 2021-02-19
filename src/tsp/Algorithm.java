package tsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tsp.geneticAIModern.Individual;
import tsp.utils.MetricStorage;
import tsp.utils.Metric;

public class Algorithm<A> {

	protected static final String POPULATION_SIZE = "populationSize";
	protected static final String ITERATIONS = "iterations";
	protected static final String TIME_IN_MILLISECONDS = "timeInMSec";

	protected MetricStorage metrics = new MetricStorage();
	private List<ProgressTracker> progressTrackers = new ArrayList<>();
	protected String fileUrl;

	// TODO: Maybe using a progress tracker
	public void metricsDumpCheck() {
		if (saveCondition()) {
			//this.notifyProgressTrackers();
		}
	}
	
	//TODO: Default metric save condition to be overriden
	public boolean saveCondition() {
		if (getIterations()%5==0) //Every 5 iterations, save metrics
			return true;
		return false;
	}

	// TODO: Default stop condition to be overriden
	public boolean stopCondition() {
		if (getTimeInMilliseconds() > 30000) //30s default max time
			return true;
		return false;
	}

	// TODO: Write metrics to csv file
	public void flushToFile() {

	}

	/** Metrics */
	public void clearInstrumentation() {
		// Sets the population size and number of iterations to zero.
		updateMetrics(new ArrayList<Individual<A>>(), 0, 0L);
	}

	public MetricStorage getMetrics() {
		return metrics;
	}

	public int getPopulationSize() {
		return metrics.getInt(POPULATION_SIZE);
	}

	public int getIterations() {
		return metrics.getInt(ITERATIONS);
	}

	public long getTimeInMilliseconds() {
		return metrics.getLong(TIME_IN_MILLISECONDS);
	}

	/**
	 * Updates statistic data collected during search.
	 * 
	 * @param itCount the number of iterations.
	 * @param time    the time in milliseconds that the genetic algorithm took.
	 */
	protected void updateMetrics(Collection<Individual<A>> population, int itCount, long time) {
		metrics.setValue(POPULATION_SIZE, population.size());
		metrics.setValue(ITERATIONS, itCount);
		metrics.setValue(TIME_IN_MILLISECONDS, time);
	}

	/** Progress trackers can be used to display progress information. */
	public void addProgressTracker(ProgressTracker pTracker) {
		progressTrackers.add(pTracker);
	}

	protected void notifyProgressTrackers() {
		for (ProgressTracker tracer : progressTrackers)
			tracer.saveProgress();
	}

	public class ProgressTracker {
		
		private String name;
		private Metric value;

		public ProgressTracker(String name, Metric value) {
			this.name = name;
			this.value = value;
		}
		
		void saveProgress() {
			if (!metrics.existsMetrics(this.name)) 
				metrics.createMetric(name);
			
			switch(this.value.getType()) {
				case 0:
					metrics.saveMetric(name, this.value.getValueInt());
					break;
				case 1:
					metrics.saveMetric(name, this.value.getValueDouble());
					break;
				case 2:
					metrics.saveMetric(name, this.value.getValueLong());
					break;
			}
			
		}
	}

}
