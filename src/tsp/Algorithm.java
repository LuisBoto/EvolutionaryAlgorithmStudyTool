package tsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tsp.geneticAIModern.Individual;
import tsp.utils.Metrics;

public class Algorithm<A> {

	protected static final String POPULATION_SIZE = "populationSize";
	protected static final String ITERATIONS = "iterations";
	protected static final String TIME_IN_MILLISECONDS = "timeInMSec";

	protected Metrics metrics = new Metrics();
	private List<ProgressTracker<A>> progressTrackers = new ArrayList<>();
	protected String fileUrl;

	public void saveMetric(String name, long value) {
		metrics.set(name, value);
	}

	public void saveMetric(String name, int value) {
		metrics.set(name, value);
	}

	public void saveMetric(String name, double value) {
		metrics.set(name, value);
	}

	// TODO: Maybe using a progress tracker
	public void checkMetricsRecord(int total, int period, int current) {
		if (current > total / period) {

		}
	}

	// TODO: Default stop condition
	public boolean stopCondition() {
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

	public Metrics getMetrics() {
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
		metrics.set(POPULATION_SIZE, population.size());
		metrics.set(ITERATIONS, itCount);
		metrics.set(TIME_IN_MILLISECONDS, time);
	}

	/** Progress trackers can be used to display progress information. */
	public void addProgressTracker(ProgressTracker<A> pTracker) {
		progressTrackers.add(pTracker);
	}

	protected void notifyProgressTrackers(int itCount, Collection<Individual<A>> generation) {
		for (ProgressTracker<A> tracer : progressTrackers)
			tracer.trackProgress(getIterations(), generation);
	}

	public interface ProgressTracker<A> {
		void trackProgress(int itCount, Collection<Individual<A>> population);
	}

}
