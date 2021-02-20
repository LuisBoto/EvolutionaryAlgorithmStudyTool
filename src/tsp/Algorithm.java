package tsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tsp.geneticAIModern.Individual;
import tsp.utils.MetricStorage;

public abstract class Algorithm<A> {

	protected static final String POPULATION_SIZE = "populationSize";
	protected static final String ITERATIONS = "iterations";
	protected static final String TIME_IN_MILLISECONDS = "timeInMSec";

	protected MetricStorage metrics = new MetricStorage();
	private List<ProgressTracker> progressTrackers = new ArrayList<>();
	protected String fileUrl;

	public Algorithm() {
		this.createTrackers();
	}
	
	// TODO: Flush to file
	public void metricsDumpCheck() {
		if (saveCondition()) {
			this.notifyProgressTrackers();
			this.flushToFile();
		}
	}
	
	// Default metric save condition to be overriden
	protected boolean saveCondition() {
		if (getIterations()%100==0) //Every 100 iterations, save metrics
			return true;
		return false;
	}

	// Default stop condition to be overriden
	protected boolean stopCondition() {
		if (getTimeInMilliseconds() > 30000) //30s default max time
			return true;
		return false;
	}

	// TODO: Write metrics to csv file
	private void flushToFile() {

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

	protected void updateMetrics(Collection<Individual<A>> population, int itCount, long time) {
		metrics.setValue(POPULATION_SIZE, population.size());
		metrics.setValue(ITERATIONS, itCount);
		metrics.setValue(TIME_IN_MILLISECONDS, time);
	}
	
	//Method where metric trackers should be created, called from Algorithm() constructor by default
	protected abstract void createTrackers();

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

		public ProgressTracker(String name) {
			this.name = name;
		}
		
		void saveProgress() {
			if (!metrics.existsMetrics(this.name)) 
				metrics.createMetric(name);
			metrics.saveMetric(name);
		}
	}

}
